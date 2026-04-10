package src.Network.Client;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.Network.Packet;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.Utility.Position;
import src.enums.View;
import src.exceptions.NoPlayerFoundException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {

    public static int port = 2222;
    public static String hostname = "localhost";
    private final GUI gui;

    Client(){
        gui = new GUI();
    }

    @Override
    public void run() {
        try(
                Socket socket = new Socket(hostname, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            Player p = getPlayer(out, in);

            if(p == null) return; // This mean the client does not want to load/create a player. Thus, we close the connection to the server.
            gui.setOwner(p);

            String inp = "";
            Packet packet;

            while(!inp.equals("quit")){
                gui.showInputOptions();

                inp = gui.getInp();

                try {
                    out.writeObject(new Packet(inp, gui.currentView));
                    packet = (Packet) in.readObject();
                    if(!packet.isSuccess()) {
                        gui.displayError("Please select a proper input");
                        continue;
                    }
                } catch (IOException e) {throw new IOException("Error sending input information to player" + e);}

                try {
                    packet = (Packet) in.readObject();
                } catch (IOException e) {
                    throw  new IOException("Error receiving output information from server" + e);
                }

                // If the input is to build something, the server needs the co-ordinates
                // This requires a few extra steps
                if(packet.getMessage() != null && packet.getMessage().equals("build")){
                    sendCoordinates(out);
                    packet = (Packet) in.readObject();  // Get the new player object in case any changes were done
                }

                // If the input is attack, the server needs extra inputs
                // This requires a few extra steps
                if(packet.getMessage() != null && packet.getMessage().equals("attack")){

                    do{
                        if(!packet.isSuccess()) throw new NoPlayerFoundException("No players fround to attack");

                        Player potentialTarget = (Player) packet.getPayload()[0];
                        gui.printVillageForAttack(potentialTarget);
                        gui.showInputOptions(p, View.ATTACK);

                        // Make sure we get a proper input
                        do {
                            inp = gui.getInp();
                        } while (!(inp.equals("y") || inp.equals("N") || inp.equals("next")));

                        out.writeObject(new Packet(inp));

                        if(inp.equals("y")){
                            packet = (Packet) in.readObject();
                            gui.displayAttackResults((Double) packet.getPayload()[0], (Resources) packet.getPayload()[1]);
                        }

                        packet = (Packet) in.readObject();
                    } while (!(inp.equals("N") || inp.equals("y")));
                }

                gui.setOwner((Player) packet.getPayload()[0]);
                gui.setCurrentView(packet.getCurrentView());

                if(packet.getMessage() != null){
                    gui.displayMessage(packet.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e){
            throw  new RuntimeException("Class not found: " + e);
        }
    }

    public void sendCoordinates(ObjectOutputStream out){
        gui.displayMessage("Enter X coordinate for your building:");
        String x_temp = gui.getInp();
        gui.displayMessage("Enter Y coordinate for your building:");
        String y_temp = gui.getInp();

        try{
            out.writeObject(new Packet(new Object[]{x_temp, y_temp}));
        } catch (IOException e){throw new RuntimeException("Error sending co-ordinates information to server" + e);}

    }

    public Player getPlayer(ObjectOutputStream out, ObjectInputStream in){

        Packet packet;

        try {
            boolean playersAvailableToLoad = in.readBoolean();
            packet = (Packet) in.readObject();
            String[] playerNames = Arrays.stream(packet.getPayload()).toArray(String[]::new);

            String name;
            if (playersAvailableToLoad && TerminalGUI.promptAccountLoading()) {  // If there are pre-existing players, ask to load an account
                name = GUI.selectPlayer(playerNames);
            } else {    // If there are no pre-existing players or client does not want to load a player, create a new one
                name = getPlayerName();
            }

            if (name == null){
                return  null;
            }

            out.writeObject(new Packet(name));
            packet = (Packet) in.readObject();

            if(packet.getMessage().equals("create")){   // If this is a player that was created, place a townhall
                System.out.println("Start creating townhall");
                this.placeInitialTownHall(gui, in, out);
            }

            packet = (Packet) in.readObject();
            Player p = (Player) packet.getPayload()[0];

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
     * Receive the updated player from the server where the townhall is placed
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
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Invalid position. Try again.");
        }
    }

    public Player getUpdatedPlayer(ObjectOutputStream out, ObjectInputStream in){
        Player p;

        try{
            out.writeObject(new Packet("fetchUpdatedModel"));
            p = (Player) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error Receiving updated player model: " + e);
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