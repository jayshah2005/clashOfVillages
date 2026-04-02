package src.Network.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    ObjectInputStream in;
    ObjectOutputStream out;

    public ClientHandler(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void handleClient(){
        System.out.println("Client is being handled now.");
    }
}
