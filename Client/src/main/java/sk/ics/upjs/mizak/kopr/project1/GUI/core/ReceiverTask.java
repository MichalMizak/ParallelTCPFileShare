package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import configuration.ClientServerConfiguration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.Callable;

import static configuration.ClientServerConfiguration.CHUNK_SIZE;
import static configuration.ClientServerConfiguration.FILE_TO_WRITE_TO;

public class ReceiverTask implements Runnable, Callable<Integer> {

    private RandomAccessFile fileToWriteTo;

    private Socket senderSocket;
    private int threadId;
    private Long dataReceived;
    private Long offset;

    public ReceiverTask(Socket senderSocket, int threadId, Long dataReceived, Long offset) {
        this.senderSocket = senderSocket;
        this.threadId = threadId;
        this.dataReceived = dataReceived;
        this.offset = offset;
    }

    private void initFile() {
        try {

            fileToWriteTo = new RandomAccessFile(FILE_TO_WRITE_TO, "rw");
            long dataStart = dataReceived + offset;
            fileToWriteTo.seek(dataStart);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initFile();
        receive();
    }

    private void receive() {

        try {
            InputStream inputStream = senderSocket.getInputStream();

            while (true) {
                byte[] buffer = new byte[CHUNK_SIZE];

                int readDataLength = inputStream.read(buffer);

                if (readDataLength == 0) {
                    break;
                }

                // also moves the file pointer
                fileToWriteTo.write(buffer, 0, readDataLength);

                // TODO: proper progress saving
                dataReceived += readDataLength;

                /*if (readDataLength != CHUNK_SIZE)
                    System.out.println("@ReceiverTask " + "threadId: " + threadId +
                            " dataReceived: " + dataReceived + "" +
                            " readDataLength: " + readDataLength +
                            " dataStart: " + dataStart);*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer call() throws Exception {
        run();
        return null;
    }
}
