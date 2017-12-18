package sk.ics.upjs.mmizak.kopr.project1.core;

import configuration.ClientServerConfiguration;
import entities.Progress;
import utilities.ProgressParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static configuration.ClientServerConfiguration.IP;
import static configuration.ClientServerConfiguration.PORT;

public class Client {

    private ProgressInformer progressInformer;
    private List<Socket> serverSockets;
    private ExecutorService executorService;

    public ProgressInformer newDownload(int threadCount) {
        ConcurrentMap<Integer, Long> threadToSentData = new ConcurrentHashMap<>();

        for (int i = 0; i < threadCount; i++) {
            threadToSentData.put(i, 0L);
        }

        Progress progress = new Progress(false, threadCount, threadToSentData);

        return initDownload(progress);
    }

    public ProgressInformer continueDownload() {
        Progress progress = ProgressParser.readProgress();

        return initDownload(progress);
    }

    private ProgressInformer initDownload(Progress progress) {

        String metadataJSON = ProgressParser.getProgressString(progress);

        Socket metadataSocket = null;
        DataOutputStream metadataOutputStream;

        try {
            metadataSocket = new Socket(IP, PORT);

            System.out.println("@Client " + "Successfully opened metadataSocket");

            metadataOutputStream = new DataOutputStream(metadataSocket.getOutputStream());
            metadataOutputStream.writeUTF(metadataJSON);

            System.out.println("@Client " + "Successfully sent metadataJSON");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                metadataSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.progressInformer = new ProgressInformer(progress);

        initTasks(progressInformer);

        return progressInformer;
    }

    private void initTasks(ProgressInformer progressInformer) {
        Progress progress = progressInformer.getProgress();

        serverSockets = new LinkedList<>();

        for (int i = 0; i < progress.getThreadCount(); i++) {
            Socket serverSocket = null;
            try {
                serverSocket = new Socket(IP, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSockets.add(serverSocket);
        }

        List<ReceiverTask> receiverTasks = new LinkedList<>();

        long offset = 0;

        for (int i = 0; i < serverSockets.size(); i++) {
            Socket socket = serverSockets.get(i);

            Map<Integer, Long> threadToSentData = progress.getThreadToSentData();
            Long partSize = ClientServerConfiguration.getPartSize(progress.getThreadCount());

            ReceiverTask receiverTask = new ReceiverTask(socket, i,
                    threadToSentData.get(i), offset, progressInformer);

            receiverTasks.add(receiverTask);

            // +1
            offset += partSize;
        }

        execute(receiverTasks);
    }

    @SuppressWarnings("Duplicates")
    private void execute(List<ReceiverTask> receiverTasks) {
        executorService = Executors.newFixedThreadPool(receiverTasks.size());

        for (ReceiverTask task : receiverTasks) {
            executorService.submit(task);
        }
    }

    /**
     * Terminate the download while saving progress
     */
    public void pause() {
        serverSockets.forEach((socket) -> {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        executorService.shutdownNow();
        ProgressParser.writeProgress(progressInformer.getProgress());
    }

    private void awaitResult(List<Future<Void>> futures) {
        for (Future<Void> future : futures) {
            try {
                future.get();
                System.out.println("@Client " + "Got past future.get");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("@Client " + "future threw exception");
                e.printStackTrace();

                // TODO: catch a particular exception to get PROGRESS from when closing sockets
            }
        }
    }
}
