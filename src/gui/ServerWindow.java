package gui;

import Net.Server;
import Net.ServerProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerWindow extends JFrame {
    private JTextArea infoArea;
    private JTextField inputArea;
    private final int consoleWidth = 500;
    private final int consoleheight = 300;
    private boolean isWritingUsers = false;
    private String sourceUser = "";
    private String targetUser = "";

    private enum Instances {
        INSTANCE(new ServerWindow());
        ServerWindow h;
        Instances(ServerWindow h) {
            this.h = h;
        }
    }

    public static ServerWindow Instance() { return Instances.INSTANCE.h; }

    public ServerWindow() {
        setTitle("Cервер приложение");
        setBounds(50, 50, consoleWidth, consoleheight);
        setLayout(new BorderLayout());
        setResizable(true);
        setFocusable(true);

        infoArea = new JTextArea("$ Сервер запущен...\n");
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
                case "help" -> infoArea.append("""
                        $ Доступные команды:
                        $ clear - очистить экран
                        $ close - закрыть консоль
                        $ users - узнать текущих пользователей в сети
                        $ setTarget - установить связь между пользователями для выполнения команд
                        $ shuffleCar - поменять транспорт местами
                        """);
                case "clear" -> infoArea.setText("");
                case "close" -> dispose();
                case "users" -> {
                    infoArea.append("$ Список подключенных пользователей:\n");
                    if (Server.getUsersList().size() == 0)
                        infoArea.append("$ Пусто\n");
                    else
                        for (ServerProcessor user : Server.getUsersList())
                            infoArea.append("$ Имя: " + user.getUserName() + "\n");
                }
                case "setTarget" -> {
                    isWritingUsers = true;
                }
                case "shuffleCar" -> {
                    if(sourceUser.isEmpty() || targetUser.isEmpty())
                        infoArea.append("$ Сначала введите пользователей с помощью команды setTarget)\n");

                    Server.shuffleCars(sourceUser, targetUser);
                }
                case "" -> {
                }
                default -> {
                    if(!isWritingUsers)
                        infoArea.append("$ Неизвестная команда. Список всех команд: help\n");
                    else {
                        if(sourceUser.isEmpty()) sourceUser = command;
                        else {
                            targetUser = command;
                            isWritingUsers = false;
                        }
                    }
                }
            }
            if(isWritingUsers && sourceUser.isEmpty())
                infoArea.append("$ Введите имя первого пользователя:\n");
            if(isWritingUsers && !sourceUser.isEmpty() && targetUser.isEmpty())
                infoArea.append("$ Введите имя второго пользователя:\n");
            inputArea.setText("");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
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
