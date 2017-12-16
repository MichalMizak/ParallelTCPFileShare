package sk.ics.upjs.mizak.kopr.project1.GUI.core;

import sk.ics.upjs.mizak.kopr.project1.GUI.ClientMainForm;

import javax.swing.*;

public class ClientRunner {

    public void run() {
        SwingUtilities.invokeLater(() -> {
            ClientMainForm form = new ClientMainForm();
            form.setVisible(true);
        });
    }
}
