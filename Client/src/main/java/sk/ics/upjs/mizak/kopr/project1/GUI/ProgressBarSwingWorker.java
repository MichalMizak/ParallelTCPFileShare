package sk.ics.upjs.mizak.kopr.project1.GUI;

import sk.ics.upjs.mizak.kopr.project1.GUI.core.IProgressInformer;
import sk.ics.upjs.mizak.kopr.project1.GUI.core.ProgressInformer;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProgressBarSwingWorker extends SwingWorker<Boolean, Integer> {

    private IProgressInformer progressInformer;
    private JProgressBar progressBar;

    public ProgressBarSwingWorker(IProgressInformer progressInformer, JProgressBar progressBar) {
        this.progressInformer = progressInformer;
        this.progressBar = progressBar;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {

            publish(getPercents());// korektny text

            Thread.sleep(2000);
        }
        // TODO: progress informer value review
        return getPercents() == 100;
    }

    private Integer getPercents() {
        return progressInformer.getProgressPercent();
    }

    @Override
    protected void process(List<Integer> chunks) {
        Integer percents = chunks.get(chunks.size() - 1);
        progressBar.setValue(percents);
    }

    protected void done() {
        try {
            if (get()) {
                progressBar.setValue(100);
            } else {
                // unhandled
            }
        } catch (InterruptedException e) {
            // unhandled
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException("Error completing the task" + e.getCause());
        }

    }

    public void setProgressInformer(ProgressInformer progressInformer) {
        this.progressInformer = progressInformer;
    }
}
