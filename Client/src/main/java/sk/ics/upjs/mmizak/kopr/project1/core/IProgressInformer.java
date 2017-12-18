package sk.ics.upjs.mmizak.kopr.project1.core;

import entities.Progress;

public interface IProgressInformer {

    int getProgressPercents();

    Progress getProgress();

    void setDataSent(Integer threadId, Long dataSent);
}
