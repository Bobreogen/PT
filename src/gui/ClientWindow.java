package gui;

import Net.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientWindow extends JDialog {
    private JTextArea infoArea;
    private JTextField inputArea;
    private final int consoleWidth = 500;
    private final int consoleheight = 300;

    private enum Instances {
        INSTANCE(new ClientWindow());
        ClientWindow h;
        Instances(ClientWindow h) {
            this.h = h;
        }
    }

    public static ClientWindow Instance() { return ClientWindow.Instances.INSTANCE.h; }

    private ClientWindow() {
        setTitle("Клиент приложение");
        setBounds(50, 50, consoleWidth, consoleheight);
        setLayout(new BorderLayout());
        setResizable(true);
        setFocusable(true);

        infoArea = new JTextArea("$ Клиент запущен...\n");
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.ITALIC, 12));
        add(infoArea, BorderLayout.CENTER);

        inputArea = new JTextField();
        inputArea.setPreferredSize(new Dimension(consoleWidth, 20));
        inputArea.setRequestFocusEnabled(true);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        inputArea.addActionListener(actionEvent -> {
            String command = inputArea.getText();
            infoArea.append(command + "\n");
            switch (command) {
                case "help" :
                    infoArea.append("$ Доступные команды:\n" +
                            "$ clear - очистить экран\n" +
                            "$ close - закрыть консоль\n" +
                            "$ users - узнать текущих пользователей в сети\n");
                    break;
                case "clear" :
                    infoArea.setText("");
                    break;
                case "close" :
                    dispose();
                    break;
                case "users" : {
                    infoArea.append("$ Список всех пользователей:\n");
                    if(Client.getUsersList().size() == 0)
                        infoArea.append("$ Пусто\n");
                    else
                        for(String user : Client.getUsersList())
                            infoArea.append("$ Имя: " + user + "\n");
                    break;
                }
                case "" : {
                    break;
                }
                default : infoArea.append("$ Неизвестная команда. Список всех команд: help\n");
            }
            inputArea.setText("");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });

        add(new JScrollPane(infoArea), BorderLayout.CENTER);
        add(inputArea, BorderLayout.SOUTH);
        setVisible(false);
    }

    public void write(String text) {
        infoArea.append(text);
    }
}
