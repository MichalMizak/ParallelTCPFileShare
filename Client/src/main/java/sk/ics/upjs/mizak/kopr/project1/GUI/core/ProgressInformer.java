package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.ClientServerConfiguration;
import entities.Progress;
import sk.ics.upjs.mizak.kopr.project1.GUI.core.ClientManager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class ProgressInformer implements IProgressInformer {

    private ClientManager clientManager;
    private Progress progress;

    public ProgressInformer(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public Integer getProgressPercent() {
        return clientManager.getProgress();
    }


    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
