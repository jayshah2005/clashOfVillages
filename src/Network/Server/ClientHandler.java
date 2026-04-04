package src.Network.Server;

import src.Network.Packet;
import src.PlayerAccount.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable {


    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = (ObjectInputStream) socket.getInputStream();
        out = (ObjectOutputStream) socket.getOutputStream();
    }

    @Override
    public void run() {

    }
}
