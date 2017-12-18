package sk.ics.upjs.mmizak.kopr.project1.core;

import configuration.ClientServerConfiguration;
import entities.Progress;

public class ProgressInformer implements IProgressInformer {

    private Progress progress;

    public ProgressInformer(Progress progress) {
        this.progress = progress;
    }

    @Override
    public synchronized int getProgressPercents() {
        long sum = progress.getThreadToSentData().values().stream().mapToLong(Long::intValue).sum();

        // TODO: File size sending
        return (int) (100 * ((sum * 1.0) / ClientServerConfiguration.FILE_TO_SEND.length()));
    }

    @Override
    public synchronized Progress getProgress() {
        return progress;
    }

    @Override
    public synchronized void setDataSent(Integer threadId, Long dataSent) {
        progress.getThreadToSentData().put(threadId, dataSent);
    }
}
