package sk.ics.upjs.mizak.kopr.project1.GUI.configuration;

import sun.security.x509.IPAddressName;

import java.io.File;

public class ClientConfiguration {

    /**
     * Random server port
     */
    public static final String PORT = "20000";

    /**
     * Localhost IP
     */
    public static final String IP = "127.0.0.1";

    public static final File PROGRESS_FILE = new File("progress.txt");

    public static final int CHUNK_SIZE = 8 * 1024;

}
