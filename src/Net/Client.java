package Net;

import entities.Car;
import entities.Vehicle;
import gui.ClientWindow;
import gui.WindowMain;
import logic.EntityManager;
import logic.Habitat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Client {
    private static LinkedList<String> usersList = new LinkedList<>();
    private static Socket socket;
    private static DataInputStream inStream;
    private static DataOutputStream outStream;
    private static Socket objectSocket;
    private static ObjectInputStream objectInStream;
    private static ObjectOutputStream objectOutStream;

    public Client() {
        try {
            socket = new Socket("127.0.0.1", 6666);
            objectSocket = new Socket("127.0.0.1", 6665);

            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            objectOutStream = new ObjectOutputStream(objectSocket.getOutputStream());
            objectInStream = new ObjectInputStream(objectSocket.getInputStream());
            outStream.writeUTF(WindowMain.Instance().getUserName());
            outStream.flush();
        } catch (Exception e) { e.printStackTrace(); }

        Thread client = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("here");
                    String action = inStream.readUTF();
                    switch (action) {
                        case "update": {
                            System.out.println("update start");
                            usersList.clear();
                            int size = inStream.readInt();
                            for (int i = 0; i < size; i++) {
                                usersList.addLast(inStream.readUTF());
                            }
                            System.out.println("update end");
                            System.out.println(usersList);

                            break;
                        }
                        case "sendCars" : {
                            sendCars();
                            getCars();
                        }

                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        });

        client.setName("Client entity");
        client.start();
    }

    public static void sendCars() {
        var carList = new ArrayList<>(Habitat.instance().getVehicleList());
        carList.removeIf(vehicle -> !(vehicle instanceof Car));
//        try {
//            outStream.close();
//        } catch (Exception e) { e.printStackTrace(); }
        try{
            objectOutStream.writeObject(carList);
            objectOutStream.flush();
        } catch (Exception e) { e.printStackTrace(); }
        carList.forEach(e -> EntityManager.instance().removeEntity(e));
//        try {
//            outStream = new DataOutputStream(socket.getOutputStream());
//        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void getCars() {
        try {
//            inStream.close();
            ArrayList<Vehicle> carList = (ArrayList<Vehicle>) objectInStream.readObject();
            carList.forEach(e -> EntityManager.instance().addEntity(e));
 //           inStream = new DataInputStream(socket.getInputStream());
        }catch (Exception e) { e.printStackTrace(); }
    }

    public static LinkedList<String> getUsersList() { return usersList; }

    public void updateUsersList(LinkedList<String> usersList) { Client.usersList = usersList;}
}
