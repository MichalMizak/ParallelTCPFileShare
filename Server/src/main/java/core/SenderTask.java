package core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.Callable;

import static configuration.ClientServerConfiguration.*;

public class SenderTask implements Runnable, Callable<Integer> {

    private RandomAccessFile fileToSend;

    private final Socket receiverSocket;
    private final int threadId;
    private Long dataSent;

    private Long start;
    private final Long end;


    public SenderTask(Socket receiverSocket, int threadId, Long dataSent, Long start, Long end) {
        this.receiverSocket = receiverSocket;
        this.threadId = threadId;
        this.dataSent = dataSent;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        System.out.println("@SenderTask " + "run()");
        initFile();
        send();
    }

    private void initFile() {
        try {
            fileToSend = new RandomAccessFile(FILE_TO_SEND, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void send() {

        byte[] buffer = new byte[CHUNK_SIZE];

        try {
            while (start < end) {
                long length = Math.min(CHUNK_SIZE, end - start);

                fileToSend.seek(start);
                fileToSend.read(buffer, 0, (int) length);

                receiverSocket.getOutputStream().write(buffer);

                if (length != CHUNK_SIZE)
                    System.out.println("@SenderTask " + "thread:" + threadId +
                            " + start: " + start + "length: " + length + " fileSize: " + fileToSend.length());

                start += length;
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

    @Override
    public String toString() {
        return "SenderTask{" +
                "fileToSend=" + fileToSend +
                ", receiverSocket=" + receiverSocket +
                ", threadId=" + threadId +
                ", dataSent=" + dataSent +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
