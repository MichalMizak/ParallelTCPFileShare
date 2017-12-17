package core;

import configuration.ClientServerConfiguration;
import sk.ics.upjs.mizak.kopr.project1.GUI.core.ClientRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ClientServerRunner {

    public static void main(String[] args) {
        run();

        compare(ClientServerConfiguration.CHUNK_SIZE);
    }

    private static void run() {
        Thread server = new Thread(() ->
        {
            ServerRunner serverRunner = new ServerRunner();
            serverRunner.run();
        });

        Thread client = new Thread(() ->
        {
            ClientRunner clientRunner = new ClientRunner();
            clientRunner.run();
        });

        server.start();
        client.start();
    }

    private static void compare(int stepSize) {
        File file1 = ClientServerConfiguration.FILE_TO_SEND;
        File file2 = ClientServerConfiguration.FILE_TO_WRITE_TO;

        Long length = 0L;
        int counter = 0;

        byte[] buf1 = new byte[stepSize];
        byte[] buf2 = new byte[stepSize];

        try (RandomAccessFile raf1 = new RandomAccessFile(file1, "rw")) {
            try (RandomAccessFile raf2 = new RandomAccessFile(file2, "rw")) {
                lol:
                while (length < ClientServerConfiguration.FILE_TO_SEND.length()) {

                    raf1.seek(length);
                    raf2.seek(length);

                    raf1.read(buf1);
                    raf2.read(buf2);

                    if (!Arrays.equals(buf1, buf2)) {
                        counter++;
                        System.out.println(length + " " + ClientServerConfiguration.FILE_TO_SEND.length());
                        /*for (int i = 0; i < buf1.length; i++) {
                            if (buf1[i] != buf2[i]) {*/
                               /* System.out.print("[" + String.format("%8s", Integer.toBinaryString(buf1[i] & 0xFF)).replace(' ', '0')
                                        + ", " + String.format("%8s", Integer.toBinaryString(buf2[i] & 0xFF)).replace(' ', '0') + "]");*/

                        if (counter > 10) {
                            break lol;
                        }
                    }

                    length += stepSize;
                }
                // System.out.println(counter);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
