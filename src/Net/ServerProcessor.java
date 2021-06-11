package Net;


import gui.ServerWindow;

import java.io.*;
import java.net.Socket;

public class ServerProcessor extends Thread {
    private static int usersCount = 0;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private ObjectInputStream inObjectStream;
    private ObjectOutputStream outObjectStream;
    private Socket socket;
    private Socket objectSocket;
    private String userName;

    public ServerProcessor(Socket s, Socket os) {
        socket = s;
        objectSocket = os;
    }

    public void sendData(int data) {
        try {
            outStream.writeInt(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        try {
            outStream.writeUTF(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObjectData() {
        Object result = null;
        try {
            result = inObjectStream.readObject();
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    public void sendObjectData(Object obj) {
        try {
            outObjectStream.writeObject(obj);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateUsers() {
        for (ServerProcessor user : Server.getUsersList()) {
            user.sendData("update");
            user.sendData(Server.getUsersList().size());
            for(ServerProcessor usr : Server.getUsersList())
                user.sendData(usr.getUserName());
        }
    }

    public String getUserName() { return userName; }

    public void run() {
        try {
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            outObjectStream = new ObjectOutputStream(objectSocket.getOutputStream());
            inObjectStream = new ObjectInputStream(objectSocket.getInputStream());
            userName = inStream.readUTF();
            ServerWindow.Instance().write(userName + " подключился\n");
            Server.getUsersList().addLast(this);

            updateUsers();
            boolean exit = false;

            while(!exit) {
                String action = inStream.readUTF();
                switch (action) {
                    case "disconnect" : {
                        exit = true;
                        Server.getUsersList().remove(this);
                        inStream.close();
                        outStream.close();
                        socket.close();
                        updateUsers();
                        ServerWindow.Instance().write("$ " + this.getUserName() + " был исключен\n");
                        break;
                    }
                }
            }
        }
        catch (IOException e) {
            Server.getUsersList().remove(this);
            updateUsers();
            ServerWindow.Instance().write("$ " + this.getUserName() + " отключился.\n");
        }

    }
}
