package gui;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JDialog {

    public LoginWindow() {
        setModal(true);
        setSize(new Dimension(500, 200));
        setLayout(new BorderLayout());
        JLabel loginText = new JLabel("Введите имя пользователя (по умолчанию - Local)");
        add(loginText, BorderLayout.NORTH);
        JTextField inputArea = new JTextField();
        inputArea.setSize(new Dimension(200, 100));
        inputArea.setRequestFocusEnabled(true);
        add(inputArea, BorderLayout.SOUTH);
        inputArea.addActionListener(actionEvent -> {
            String command = inputArea.getText();
            if(!command.isEmpty())
                WindowMain.Instance().setUserName(command);
            setModal(false);
            setVisible(false);
        });
        pack();
        setVisible(true);
    }
}
