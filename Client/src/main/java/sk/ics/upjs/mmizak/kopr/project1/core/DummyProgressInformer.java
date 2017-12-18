package sk.ics.upjs.mmizak.kopr.project1.core;

import entities.Progress;

public class DummyProgressInformer implements IProgressInformer {

    private int progressPercents;

    /**
     * Constant progress value returner
     * @param progressPercents
     */
    public DummyProgressInformer(int progressPercents) {
        this.progressPercents = progressPercents;
    }

    @Override
    public int getProgressPercents() {
        return progressPercents;
    }

    @Override
    public Progress getProgress() {
        return null;
    }

    @Override
    public void setDataSent(Integer threadId, Long dataSent) {

    }
}
