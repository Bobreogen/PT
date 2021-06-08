package gui;

import AI.AIManager;
import entities.VehicleType;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Console extends JDialog {
    JTextArea mainField;
    JTextField inputField;
    ByteArrayOutputStream stream;

    Console() {
        setTitle("Консоль");
        setBackground(Color.white);
        setMinimumSize(new Dimension(500, 300));
        setLayout(new BorderLayout());
        setModal(false);
        setAlwaysOnTop(true);
        setResizable(true);
        setFocusable(true);
        stream = new ByteArrayOutputStream();

        mainField = new JTextArea("");
        mainField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainField.setEditable(false);
        add(mainField, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(mainField.getWidth(), 25));
        inputField.setRequestFocusEnabled(true);
        inputField.setFocusable(true);
        inputField.addActionListener(actionEvent -> {
            try
            {
                stream.write(inputField.getText().getBytes(StandardCharsets.UTF_8));
                stream.flush();
            }
            catch (Exception e) { e.printStackTrace(); }
            String command = stream.toString();
            mainField.append(command + "\n");
            try {
                stream.reset();
            } catch (Exception e) { e.printStackTrace(); }

            switch (command) {
                case "help" -> mainField.setText(GetHelp());
                case "clear" -> mainField.setText("");
                case "close" -> {
                    dispose();
                }
                case "stopAI" -> {
                    AIManager.instance().StopAIForVehicleType(VehicleType.CAR);
                    AIManager.instance().StopAIForVehicleType(VehicleType.TRUCK);
                }
                case "startAI" -> {
                    AIManager.instance().StartAIForVehicleType(VehicleType.CAR);
                    AIManager.instance().StartAIForVehicleType(VehicleType.TRUCK);
                }
            }
            inputField.setText("");
        });
        add(inputField, BorderLayout.SOUTH);

        this.add(new JScrollPane(mainField), BorderLayout.CENTER);
        this.setVisible(true);
    }

    String GetHelp() {
        return "help - get commands\nclear - clear text area\nclose - close console\n stopAI - stop AI\n StartAI - start AI\n";
    }
}
