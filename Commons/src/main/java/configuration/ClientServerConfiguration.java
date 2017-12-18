package configuration;

import java.io.File;

public class ClientServerConfiguration {

    /**
     * Random server port
     */
    public static final int PORT = 20000;

    /**
     * Localhost IP
     */
    public static final String IP = "127.0.0.1";

    public static final int CHUNK_SIZE = 8 * 1024;

    /**
     * File to save progress to
     */
    public static final File CLIENT_PROGRESS_FILE = new File("progress.txt");

    /**
     * File to send
     */
    public static final File FILE_TO_SEND = new File("zasilka-PWCS8XZRGZNLKCPI.zip");

    /**
     * File on the client's side to write data to
     */
    public static final File FILE_TO_WRITE_TO = new File("file_to_write_to");

    public static Long getPartSize(int threadCount) {
        return FILE_TO_SEND.length() / threadCount;
    }
}
