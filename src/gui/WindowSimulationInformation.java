package gui;

import logic.Habitat;

import javax.swing.*;
import java.awt.*;

public class WindowSimulationInformation extends JDialog {
    WindowSimulationInformation(JFrame owner, String info) {
        super(owner, "Результаты симуляции");
        setSize(300, 200);
        setResizable(false);

        JTextArea comp = new JTextArea(info);
        setLayout(new BorderLayout());
        comp.setEnabled(false);
        add(comp, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton ok = new JButton("OK");
        ok.addActionListener(evt -> {
            Habitat.instance().stopSimulation();
            dispose();
        });

        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(evt -> {
            Habitat.instance().startSimulation();
            dispose();
        });
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
