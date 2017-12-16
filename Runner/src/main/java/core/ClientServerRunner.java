package core;

import configuration.ClientServerConfiguration;
import sk.ics.upjs.mizak.kopr.project1.GUI.core.ClientRunner;

import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Scanner;

public class ClientServerRunner {

    public static void main(String[] args) {
        run();

        compare();
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

    private static void compare() {
        File file1 = ClientServerConfiguration.FILE_TO_SEND;
        File file2 = ClientServerConfiguration.FILE_TO_WRITE_TO;

        Long length = 0L;
        int counter = 0;

        try (RandomAccessFile raf1 = new RandomAccessFile(file1, "rw")) {
            try (RandomAccessFile raf2 = new RandomAccessFile(file2, "rw")) {
                lol:
                while (length < ClientServerConfiguration.FILE_TO_SEND.length()) {

                    byte[] buf1 = new byte[ClientServerConfiguration.CHUNK_SIZE];
                    byte[] buf2 = new byte[ClientServerConfiguration.CHUNK_SIZE];

                    raf1.read(buf1);
                    raf2.read(buf2);

                    length += ClientServerConfiguration.CHUNK_SIZE;

                    raf1.seek(buf1.length + length);
                    raf2.seek(buf2.length + length);

                    if (!Arrays.equals(buf1, buf2)) {
                        counter++;
                        // System.out.println(length + " " + ClientServerConfiguration.FILE_TO_SEND.length());
                        for (int i = 0; i < buf1.length; i++) {
                            if (buf1[i] != buf2[i]) {
                                System.out.print("[" + String.format("%8s", Integer.toBinaryString(buf1[i] & 0xFF)).replace(' ', '0')
                                        + ", " + String.format("%8s", Integer.toBinaryString(buf2[i] & 0xFF)).replace(' ', '0') + "]");
                                System.out.println();
                            }
                        }
                        if (counter > 1) {
                            break lol;
                        }
                    }
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
