package sk.ics.upjs.mmizak.kopr.project1.core;

import sk.ics.upjs.mmizak.kopr.project1.GUI.ClientMainForm;

import javax.swing.*;

public class ClientRunner {

    public void run() {
        SwingUtilities.invokeLater(() -> {
            ClientMainForm form = new ClientMainForm();
            form.setVisible(true);
        });
    }
}
