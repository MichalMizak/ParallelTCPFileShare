package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import core.ServerRunner;

public class ClientServerRunner {

    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner();
        serverRunner.run();

        ClientRunner clientRunner = new ClientRunner();
        clientRunner.run();
    }

}
