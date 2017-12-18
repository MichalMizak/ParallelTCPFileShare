package core;

import configuration.ClientServerConfiguration;
import sk.ics.upjs.mmizak.kopr.project1.core.ClientRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ClientServerRunner {

    public static void main(String[] args) {
        run();
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
}
