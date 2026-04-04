package src.Network.Client;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.Network.Packet;
import src.Network.Server.ClientHandler;
import src.PlayerAccount.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {
    //TODO: Create player client, choose type of socket, interact with server,
    // needs to have some for of authentication method to check if player exists in a database,
    // doesnt need to be able to support multiple clients connecting

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
//                this.placeInitialTownHall(p, gui);
            }

            return p;
        } catch (IOException e) {
            throw new RuntimeException("Error sending information about player selection/creation: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
