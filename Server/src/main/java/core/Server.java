package core;

import configuration.ClientServerConfiguration;
import entities.Progress;
import utilities.ProgressParser;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static configuration.ClientServerConfiguration.FILE_TO_SEND;
import static configuration.ClientServerConfiguration.PORT;

public class Server {
    private ServerSocket serverSocket;

    public void init() {
        Progress progress;

        try {
            serverSocket = new ServerSocket(PORT);
            while (!Thread.currentThread().isInterrupted()) {
                Socket metadataSocket = serverSocket.accept();

                DataInputStream metadataInputStream = new DataInputStream(metadataSocket.getInputStream());

                String metadataJSON = metadataInputStream.readUTF();
                progress = ProgressParser.readProgress(metadataJSON);
                System.out.println("@Server " + "Succesfully parsed metadataJSON");
                initTasks(progress, serverSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTasks(Progress progress, ServerSocket serverSocket) {
        List<Socket> clientSockets = new LinkedList<>();

        for (int i = 0; i < progress.getThreadCount(); i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long offset = 0;
        long fileSize = FILE_TO_SEND.length();
        long partLength = ClientServerConfiguration.getPartSize(progress.getThreadCount());

        List<SenderTask> senderTasks = new LinkedList<>();

        for (int i = 0; i < clientSockets.size(); i++) {
            Socket socket = clientSockets.get(i);

            Map<Integer, Long> threadToSentData = progress.getThreadToSentData();

            long end = offset + partLength;

            // last thread most likely sends more data
            if (i == clientSockets.size() - 1) {
                end = fileSize;
            }

            // progress holds offset from the start of the tasks's file part
            SenderTask senderTask = new SenderTask(socket, i, threadToSentData.get(i),
                    offset + progress.getThreadToSentData().get(i), end);

            // +1
            offset += partLength;

            senderTasks.add(senderTask);
        }

        execute(senderTasks);
    }

    private void execute(List<SenderTask> senderTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(senderTasks.size());
        try {
            List<Future<Integer>> futures = executorService.invokeAll(senderTasks);
            for (Future<Integer> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // catch a particular exception to get more info from
        }
    }
}
