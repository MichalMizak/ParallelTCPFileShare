package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import configuration.ClientServerConfiguration;
import entities.Progress;
import utilities.ProgressParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static configuration.ClientServerConfiguration.IP;
import static configuration.ClientServerConfiguration.PORT;

public class ClientManager {

    private ProgressInformer progressInformer;

    public ProgressInformer newDownload(int threadCount) {
        Map<Integer, Long> threadToSentData = new HashMap<>();
        for (int i = 0; i < threadCount; i++) {
            threadToSentData.put(i, 0L);
        }
        Progress progress = new Progress(true, threadCount, threadToSentData);
        return initDownload(progress);
    }

    public ProgressInformer continueDownload() {
        Progress progress = ProgressParser.readProgress();

        return initDownload(progress);
    }

    public ProgressInformer initDownload(Progress progress) {

        String metadataJSON = ProgressParser.getProgressString(progress);

        Socket metadataSocket = null;
        DataOutputStream metadataOutputStream;

        try {
            metadataSocket = new Socket(IP, PORT);

            System.out.println("@ClientManager " + "Successfully opened metadataSocket");

            metadataOutputStream = new DataOutputStream(metadataSocket.getOutputStream());
            metadataOutputStream.writeUTF(metadataJSON);

            System.out.println("@ClientManager " + "Successfully sent metadataJSON");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                metadataSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO: file size sending instead of using size from configuration
        initTasks(progress);

        return null;
    }

    private void initTasks(Progress progress) {

        List<Socket> serverSockets = new LinkedList<>();

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
                    threadToSentData.get(i), offset);

            receiverTasks.add(receiverTask);

            // +1
            offset += partSize;
        }

        execute(receiverTasks);
    }

    @SuppressWarnings("Duplicates")
    private void execute(List<ReceiverTask> receiverTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(receiverTasks.size());

        List<Future<Integer>> futures = null;

        try {
            futures = executorService.invokeAll(receiverTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<Integer> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO: catch a particular exception to get PROGRESS from when closing sockets
            }
        }
    }

    public Progress pause() {
        // pause all threads
        ProgressParser.writeProgress(progressInformer.getProgress());
        return null;
    }

    public Integer getProgress() {
        return null;
    }
}
