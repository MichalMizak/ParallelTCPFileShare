package core;

import configuration.ClientServerConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class FinalTest {

    public static void compare(int stepSize) {
        File file1 = ClientServerConfiguration.FILE_TO_SEND;
        File file2 = ClientServerConfiguration.FILE_TO_WRITE_TO;

        Long length = 0L;
        int counter = 0;

        byte[] buf1 = new byte[stepSize];
        byte[] buf2 = new byte[stepSize];

        try (RandomAccessFile raf1 = new RandomAccessFile(file1, "rw")) {
            try (RandomAccessFile raf2 = new RandomAccessFile(file2, "rw")) {

                compareWhile:
                while (length < ClientServerConfiguration.FILE_TO_SEND.length()) {

                    raf1.seek(length);
                    raf2.seek(length);

                    raf1.read(buf1);
                    raf2.read(buf2);

                    if (!Arrays.equals(buf1, buf2)) {
                        counter++;
                        System.out.println("Problem at " + length + "th chunk out of "
                                + ClientServerConfiguration.FILE_TO_SEND.length() + " bytes");

                        break compareWhile;
                    }

                    length += stepSize;
                }

                if (counter > 0) {
                    System.out.print("Problem at " + counter + " chunks");
                } else {
                    System.out.println("File download successful");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
