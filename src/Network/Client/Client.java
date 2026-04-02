package src.Network.Client;

import src.Network.Server.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    //TODO: Create player client, choose type of socket, interact with server,
    // needs to have some for of authentication method to check if player exists in a database,
    // doesnt need to be able to support multiple clients connecting

    public int port = 2222;
    public String hostname = "localhost";

    Client(){

    }

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        try(Socket socket = new Socket(this.hostname, this.port)) {

            try(
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
