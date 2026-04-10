package src.Network.Server;

import src.PlayerAccount.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class Server {

    private static final int PORT = 2222;
    private static final String FILE = "./src/data/players.ser";

    List<Player> players;           // Player we have saved and not laoded
    List<Player> activePlayers;     // Players currently playing the game that are locked
    private String message;                 // A global buffer that will store the messages we will send to the client
    ServerSocket serverSocket;

    Server() {
        players = readPlayerFiles();
        activePlayers = new ArrayList<>();
    }

    /**
     * Start the server
     */
    public void start(){
        try {
            serverSocket = new ServerSocket(PORT);

            System.out.println("Server started on port " + PORT);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

            loop:
            while(!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();

                threadPoolExecutor.execute(() -> {

                    try{
                        new ClientHandler(clientSocket, this).run(); // Ideally everything is done over here
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {

            if(serverSocket.isClosed()){
                System.out.println("Server has stopped");
                return;
            }

            throw new RuntimeException(e);
        }
    }

    public void logout(Player player){
        activePlayers.remove(player);
        players.add(player);

        if(activePlayers.isEmpty()){

            System.out.println("There are no more active players");

            try {this.serverSocket.close();}
            catch (IOException e) {e.printStackTrace();}

            this.savePlayers();
        }
    }

    /**
     * serializes the player object and saves their data
     */
    public void savePlayers(){
        try (FileOutputStream fileOut = new FileOutputStream(Server.FILE)) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (Player p : players){
                out.writeObject(p);
            }
            System.out.println("Players have been saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * finds random player to attack
     * @param notEligible list of not eligible players
     * @return a player that we can attack
     */
    public Player findRandomPlayerToAttack(Set<Player> notEligible) {

        List<Player> eligible = players.stream()
                .filter(player -> !notEligible.contains(player))
                .filter(player -> player.getVillage()
                        .guardTime.isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        System.out.println("Final eligible players: " + eligible);

        // checks for anyone who doesnt have a gaurd
        if (eligible.isEmpty()) {
            return null;
        }

        Collections.shuffle(eligible);
        return eligible.get(0);
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
