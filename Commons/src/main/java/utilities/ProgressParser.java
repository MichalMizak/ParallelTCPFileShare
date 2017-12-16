package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.ClientServerConfiguration;
import entities.Progress;

import java.io.IOException;
import java.util.Collections;

public class ProgressParser {

    public static Progress readProgress(String string) {
        ObjectMapper mapper = new ObjectMapper();

        Progress progress;

        try {
            progress = mapper.readValue(string, Progress.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress string format ");
        } catch (IOException e) {
            throw new RuntimeException("Incorrect progress string format" + e.getCause());
        }

        return progress;
    }

    public static Progress readProgress() {
        ObjectMapper mapper = new ObjectMapper();

        Progress progress;

        try {
            progress = mapper.readValue(ClientServerConfiguration.CLIENT_PROGRESS_FILE, Progress.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress format ");
        } catch (IOException e) {
            throw new RuntimeException("Incorrect progress file configuration" + e.getCause());
        }

        return progress;
    }

    public static void writeProgress(Progress progress) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(ClientServerConfiguration.CLIENT_PROGRESS_FILE, progress);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress format");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress file configuration" + e.getCause());
        }
    }

    public  static String getProgressString(Progress progress) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(progress);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void flushProgress() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(ClientServerConfiguration.CLIENT_PROGRESS_FILE, new Progress(true, 0, Collections.<Integer, Long>emptyMap()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
