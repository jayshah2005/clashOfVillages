package src.Network.Client;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.Network.Packet;
import src.Network.Server.ClientHandler;
import src.PlayerAccount.Player;
import src.Utility.Position;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {
    //TODO: Create player client, choose type of socket, interact with server,
    // needs to have some for of authentication method to check if player exists in a database,
    // doesn't need to be able to support multiple clients connecting

    public static int port = 2222;
    public static String hostname = "localhost";
    private GUI gui;
    Player p;

    Client(){
        gui = new GUI();
    }

    @Override
    public void run() {
        try(
                Socket socket = new Socket(this.hostname, this.port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {

            String playerInp;
            Player p = getPlayer(out, in);

            if(p == null){} // This mean the client does not want to load/create a player. Thus, we close the connection to the server.



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getPlayer(ObjectOutputStream out, ObjectInputStream in){

        Packet packet;

        try {
            boolean playersAvailableToLoad = in.readBoolean();
            packet = (Packet) in.readObject();
            String[] playerNames = (String[]) Arrays.stream(packet.getPayload()).toArray(String[]::new);

            String name = null;
            if (playersAvailableToLoad && TerminalGUI.promptAccountLoading()) {  // If there are pre-existing players, ask to load an account
                name = GUI.selectPlayer(playerNames);
            } else {    // If there are no pre-existing players or client does not want to load a player, create a new one
                name = getPlayerName();
                if (name == null) return null;
            }

            out.writeObject(new Packet(name));
            packet = (Packet) in.readObject();
            Player p = (Player) packet.getPayload()[0];

            if(packet.getMessage().equals("create")){   // If this is a player that was created, place a townhall
                System.out.println("Start creating townhall");
                this.placeInitialTownHall(gui, in, out);
            }

            return p;
        } catch (IOException e) {
            throw new RuntimeException("Error sending information about player selection/creation: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the position where we want to place the townhall.
     * Validate the position with the server
     * Recieve the updated player from the server where the townhall is placed
     * Update current GUI
     */
    public void placeInitialTownHall(GUI gui, ObjectInputStream in, ObjectOutputStream out){
        gui.printVillageHallPlacementMessage();

        while(true){


            gui.displayMessage("Enter X coordinate for your Village Hall:");
            String x_temp = gui.getInp();
            gui.displayMessage("Enter Y coordinate for your Village Hall:");
            String y_temp = gui.getInp();

            int x;
            int y;

            try{
                x = Integer.parseInt(x_temp);
                y = Integer.parseInt(y_temp);
            } catch (NumberFormatException e) {
                gui.displayMessage("Please enter integer coordinate for your Village Hall");
                continue;
            }

            Position pos = new Position(x,y);

            try {
                out.writeObject(new Packet(new Object[]{pos}));

            } catch (IOException e) {
                throw new RuntimeException("Error sending information about player selection/creation: " + e);
            }

            try{
                boolean placed = in.readBoolean();

                if(placed){
                    Player p = (Player) in.readObject();
                    gui.setOwner(p);
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Invalid position. Try again.");
        }
    }

    public Player getUpdatedPlayer(ObjectOutputStream out, ObjectInputStream in){
        Player p = null;

        try{
            out.writeObject(new Packet("fetchUpdatedModel"));
            p = (Player) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error Recieving updated player model: " + e);
        }

        return p;
    }

    /**
     * get a player name you want to load/create
     * @return
     */
    public String getPlayerName(){
        String name;
        if( TerminalGUI.promptAccountCreation()) {
            name = gui.getName();
        } else name = null;

        return name;
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
