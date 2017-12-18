package sk.ics.upjs.mmizak.kopr.project1.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;

import static configuration.ClientServerConfiguration.CHUNK_SIZE;
import static configuration.ClientServerConfiguration.FILE_TO_WRITE_TO;

public class ReceiverTask implements Callable<Void> {

    private RandomAccessFile fileToWriteTo;

    private Socket senderSocket;
    private int threadId;
    private Long dataReceived;
    private Long offset;

    private ProgressInformer progressInformer;


    public ReceiverTask(Socket senderSocket, int threadId, Long dataReceived, Long offset, ProgressInformer progressInformer) {
        this.senderSocket = senderSocket;
        this.threadId = threadId;
        this.dataReceived = dataReceived;
        this.offset = offset;
        this.progressInformer = progressInformer;
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

                if (readDataLength == -1) {
                    senderSocket.close();
                    break;
                }

                // also moves the file pointer
                fileToWriteTo.write(buffer, 0, readDataLength);

                // TODO: proper progress saving
                dataReceived += readDataLength;
                progressInformer.setDataSent(threadId, dataReceived);
            }
        } catch (SocketException e) {
            System.out.println("Receiver socket closing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Void call() throws Exception {
        run();
        return null;
    }
}
