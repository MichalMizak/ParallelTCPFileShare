package sk.ics.upjs.mmizak.kopr.project1.GUI;

import sk.ics.upjs.mmizak.kopr.project1.core.DummyProgressInformer;
import sk.ics.upjs.mmizak.kopr.project1.core.IProgressInformer;

import javax.swing.*;
import java.util.List;

public class ProgressBarSwingWorker extends SwingWorker<Void, Integer> {

    private IProgressInformer progressInformer;
    private JProgressBar progressBar;
    private JButton cancelButton;

    public ProgressBarSwingWorker(IProgressInformer progressInformer, JProgressBar progressBar, JButton cancelButton) {
        this.progressInformer = progressInformer;
        this.progressBar = progressBar;
        this.cancelButton = cancelButton;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {

            Integer percents = getPercents();

            publish(percents);

            if (percents == 100) {
                finish();
            }

            Thread.sleep(2000);
        }
        return null;
    }

    private Integer getPercents() {
        return progressInformer.getProgressPercents();
    }

    @Override
    protected void process(List<Integer> chunks) {
        Integer percents = chunks.get(chunks.size() - 1);
        progressBar.setValue(percents);
    }


    public void finish() {
        cancelButton.doClick();
    }

    public void setProgressInformer(IProgressInformer progressInformer) {
        this.progressInformer = progressInformer;
    }

    public void pausedState() {
        setProgressInformer(new DummyProgressInformer(progressInformer.getProgressPercents()));
    }

    public void resetState() {
        setProgressInformer(new DummyProgressInformer(0));
    }

    /**
     * Sets progress 99 for simpler handling
     */
    public void doneState() {
        setProgressInformer(new DummyProgressInformer(99));
    }
}
