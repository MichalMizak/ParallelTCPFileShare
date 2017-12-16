package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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


    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public <T, E> Set<T> getKeysByValue(E value) {
        return getKeysByValue((Map<T, E>) threadToSentData, value);
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
