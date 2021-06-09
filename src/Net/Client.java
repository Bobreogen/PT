package Net;

import java.util.LinkedList;

public class Client extends Thread {
/*    private static final LinkedList<String> usersList = new LinkedList<>();
    private final String configDirectory = "src/ConfigFiles/" + MainWindow.userName;

    private ConfigActions config = new ConfigActions();
    public Client() {
        Thread client = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("here");
                    String action = MainWindow.inStream.readUTF();
                    switch (action) {
                        case "update": {
                            System.out.println("update start");
                            usersList.clear();
                            MainWindow.clients.removeAllItems();
                            int size = MainWindow.inStream.readInt();
                            for (int i = 0; i < size; i++) {
                                usersList.addLast(MainWindow.inStream.readUTF());
                                MainWindow.clients.addItem(usersList.get(i));
                            }

                            if (size > 0)
                                MainWindow.clients.setSelectedItem(0);
                            System.out.println("update end");
                            System.out.println(usersList);

                            break;
                        }

                        case "request": {
                            System.out.println("start req");
                            String bufFile = configDirectory + "/buf.txt";
                            new File(bufFile).deleteOnExit();
                            config.writeConfig(bufFile);
                            String target = MainWindow.inStream.readUTF();
                            MainWindow.outStream.writeUTF("answer");
                            MainWindow.outStream.writeUTF(target);
                            MainWindow.outStream.writeUTF(bufFile);
                            System.out.println("end req");
                            break;
                        }

                        case "accept": {
                            String data = MainWindow.inStream.readUTF();
                            config.readConfig(data);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                MainWindow.sendButton.setEnabled(false);
                MainWindow.clients.removeAllItems();
                MainWindow.clients.setEnabled(false);
                MainWindow.serverActButton.setText("Подключиться");
                MainWindow.isConnected = false;
            }
        });

        client.setDaemon(true);
        client.setName("Client updater");
        client.start();
    }
*/
}
