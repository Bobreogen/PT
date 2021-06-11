package Net;

import entities.Vehicle;
import gui.ServerWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {
    private static LinkedList<ServerProcessor> usersList = new LinkedList<>();// список всех нитей - экземпляров сервера, слушающих каждый своего клиента
    private static final int serverPort = 6666;
    private static final int serverObjectPort = 6665;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(serverPort); ServerSocket serverObjectSocket = new ServerSocket(serverObjectPort)) {
            ServerWindow.Instance().setVisible(true);
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    Socket objectSocket = serverObjectSocket.accept();
                    ServerProcessor user = new ServerProcessor(socket, objectSocket);
                    user.start();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shuffleCars(String sourceName, String targetName) {
        ArrayList<Vehicle> sourceCarList = null;
        ArrayList<Vehicle> targetCarList = null;
        for(var processors : usersList) {
            if(processors.getUserName().equals(sourceName)) {
                processors.sendData("sendCars");
                sourceCarList = (ArrayList<Vehicle>)processors.getObjectData();
            }

            if(processors.getUserName().equals(targetName)) {
                processors.sendData("sendCars");
                targetCarList = (ArrayList<Vehicle>)processors.getObjectData();
            }
        }
        for(var processors : usersList) {
            if(processors.getUserName().equals(sourceName)) {
                processors.sendObjectData(targetCarList);
            }

            if(processors.getUserName().equals(targetName)) {
                processors.sendObjectData(sourceCarList);
            }
        }

    }

    public static LinkedList<ServerProcessor> getUsersList() { return usersList; }
}
