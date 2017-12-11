package core;

public class ServerRunner implements Runnable {

    public void run() {
        Server server = new Server();
        server.init();
    }
}
