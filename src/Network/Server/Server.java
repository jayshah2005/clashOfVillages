package src.Network.Server;

import src.Network.Packet;
import src.PlayerAccount.Player;
import src.Utility.Position;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private static final int PORT = 2222;
    private static final String FILE = "./src/data/players.ser";

    List<Player> players;           // Player we have saved and not laoded
    List<Player> activePlayers;     // Players currently playing the game that are locked
    private String message;                 // A global buffer that will store the messages we will send to the client

    Server() {
        players = readPlayerFiles();
        activePlayers = new ArrayList<>();
    }

    /**
     * Start the server
     */
    public void start(){
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started on port " + PORT);

            while(true){
                Socket clientSocket = serverSocket.accept();
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

                threadPoolExecutor.execute(() -> {

                    try{
                        new ClientHandler(clientSocket, this).run(); // Ideally everything is done over here
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * loads player data from serialized file
     * @return
     */
    public List<Player> readPlayerFiles() {

        List<Player> tempPlayersList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE); ObjectInputStream ois = new ObjectInputStream(fis)) {
            while(fis.available() > 0) {
                Player p = (Player) ois.readObject();
                tempPlayersList.add(p);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved players found.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return tempPlayersList;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
