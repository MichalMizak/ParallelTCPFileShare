package sk.ics.upjs.mizak.kopr.project1.GUI.core;

public class DummyProgressInformer implements IProgressInformer {

    private int progressPercent;

    /**
     * Watch out for implementation of ProgressInformer
     * @param clientManager
     * @param progressPercent
     */
    public DummyProgressInformer(ClientManager clientManager, int progressPercent) {
        this.progressPercent = progressPercent;
    }

    @Override
    public Integer getProgressPercent() {
        return progressPercent;
    }
}
