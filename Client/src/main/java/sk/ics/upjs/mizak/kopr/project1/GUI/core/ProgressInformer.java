package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sk.ics.upjs.mizak.kopr.project1.GUI.configuration.ClientConfiguration;
import sk.ics.upjs.mizak.kopr.project1.GUI.entities.Progress;

import java.io.IOException;
import java.util.Collections;

public class ProgressInformer {

    ClientManager clientManager;

    public ProgressInformer(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public Integer getProgressPercent() {
        return clientManager.getProgress();
    }

    public static Progress readProgress() {
        ObjectMapper mapper = new ObjectMapper();

        Progress progress;

        try {
            progress = mapper.readValue(ClientConfiguration.PROGRESS_FILE, Progress.class);

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
            objectMapper.writeValue(ClientConfiguration.PROGRESS_FILE, progress);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress format");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Incorrect progress file configuration" + e.getCause());
        }
    }

    public static void flushProgress() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(ClientConfiguration.PROGRESS_FILE, new Progress(true, 0, Collections.emptyMap()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
