package sk.ics.upjs.mizak.kopr.project1.GUI.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Progress {

    private boolean isDownloadFinished;

    private Integer threadCount;

    private Map<Integer, Long> threadToSentData;

    public Progress() {
        super();
    }

    public Progress(boolean isDownloadFinished, Integer threadCount, Map<Integer, Long> threadToSentData) {
        this.isDownloadFinished = isDownloadFinished;
        this.threadCount = threadCount;
        this.threadToSentData = threadToSentData;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public Map<Integer, Long> getThreadToSentData() {
        return threadToSentData;
    }

    public boolean isDownloadFinished() {
        return isDownloadFinished;
    }

    /*
        Prettier to create private setters than annotate constructor parameters
     */

    @JsonProperty
    private void setDownloadFinished(boolean downloadFinished) {
        isDownloadFinished = downloadFinished;
    }

    @JsonProperty
    private void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty
    private void setThreadToSentData(Map<Integer, Long> threadToSentData) {
        this.threadToSentData = threadToSentData;
    }
}
