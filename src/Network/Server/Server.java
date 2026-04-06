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

    private List<Player> players;           // Player we have saved and not laoded
    private List<Player> activePlayers;     // Players currently playing the game that are locked
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

                    try(
                            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ){
//                        ClientHandler clientHandler = new ClientHandler(clientSocket); // Ideally everything is done over here

                        Player p = getPlayer(out, in);

                        if(p == null) {
                            clientSocket.close();
                            return;
                        } else if(p.getVillage().getVillageObjects().isEmpty()){
                            out.writeObject(new Packet("create", new Object[]{p}));

                            Position pos;
                            boolean placed = false;
                            Packet packet;

                            do{
                                packet = (Packet) in.readObject();
                                pos = (Position) packet.getPayload()[0];
                                placed = p.placeTownHall(pos);
                                out.writeBoolean(placed);
                            }while (!placed);

                            out.writeObject(p);

                        } else {
                            out.writeObject(new Packet("load", new Object[]{p}));
                        }

                        System.out.println("Player " + p.getName() + " created/loaded and ready to go");

                    } catch (IOException | ClassNotFoundException e) {
                        // TODO: Handle exception accordingly
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * determines which player account is loaded, if there are no players the user can create a player.
     * If we create a player, we also need to place the initial townhall
     * Once the player is loaded/created, we lock it so others cannot load this account.
     * @return the player that is loaded
     */
    public Player getPlayer(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        Packet packet;
        String name;
        Player p;

        // Tell the client if there are any saved players
        if(players.isEmpty()) out.writeBoolean(false);
        else out.writeBoolean(true);
        out.writeObject(new Packet(getPlayerNames()));  // send a list of saved player names to client for them to select

        packet = (Packet) in.readObject();
        name = packet.getMessage();

        if(players.stream().map(Player::getName).anyMatch(name::equals)){
            p = this.players.stream().filter(player -> player.name.equals(name)).findFirst().get();
            players.remove(p);
        } else {
            p = new Player(name);
        }

        activePlayers.add(p);

        return p;
    }

    /**
     * Get all player names that we have loaded
     * @return get all player names
     */
    private String[] getPlayerNames(){
        return players.stream().map(Player::getName).toArray(String[]::new);
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
