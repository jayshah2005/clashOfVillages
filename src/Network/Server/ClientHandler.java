package src.Network.Server;

import src.Network.Packet;
import src.PlayerAccount.Player;
import src.Utility.Position;
import src.enums.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable {


    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;


    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.server = server;

    }

    @Override
    public void run() {

        try {
            Player p = getPlayer(out, in);
            if (p == null) return;
            System.out.println("Player " + p.getName() + " created/loaded and ready to go");


            String inp = "";
            Packet packet;

            while(true){

                try{
                    packet =  (Packet) in.readObject();
                } catch (IOException e) {
                    System.out.println("Player " + p.getName() + ": Error receiving packet.");
                    try{
                        this.socket.close();
                        return;
                    } catch (IOException e1) {
                        throw new RuntimeException("Error closing socket: " + e1);
                    }
                }

                inp = packet.getMessage().toLowerCase();

                System.out.println("Player " + p.getName() + " received: " + inp);
            }


        } catch (ClassNotFoundException e){
            throw new RuntimeException("Wrong class: " + e);
        }
    }

    /**
     * given an input the input will be checked to see if its valid and authorized based on the current view.
     * so if you are in the village view you can select shop, upgrade, train, attack or quit
     * these are all of the valid inputs at that current view.
     *
     * @param p player inp is being processed for
     * @param inp the inp being processed
     * @return a string indicating what happened
     */
//    public String processInput(Player p, String inp) {
//
//        // This should never happen so thus if it does, we probably need to restart the game
//        String err = "Unable to process input. Please restart the game by quiting (type: 'quit')";
//        String inpCased = inp.toLowerCase();
//
//        if(inpCased.equals("quit")) return null;
//
//        switch(gui.currentView){
//            case VILLAGE -> {
//                return handleVillageInput(p, inpCased);
//            }
//            case SHOP -> {
//                return handleShopInput(p, inpCased);
//            }
//            case UPGRADE -> {
//                return handleUpgradeInput(p, inpCased);
//            }
//            case TRAIN -> {
//                return handleTrainInput(p, inpCased);
//            }
//            case ATTACK -> gui.currentView = View.VILLAGE;
//            case TEST -> {
//                return handleTestInput(p, inpCased);
//            }
//            default -> gui.displayError(err);
//        }
//
//        return null;
//    }

    /**
     * determines which player account is loaded, if there are no players the user can create a player.
     * If we create a player, we also need to place the initial townhall
     * Once the player is loaded/created, we lock it so others cannot load this account.
     * @return the player that is loaded
     */
    public Player getPlayer(ObjectOutputStream out, ObjectInputStream in) {
        Packet packet;
        String name;
        Player p;

        try{
            // Tell the client if there are any saved players
            if(server.players.isEmpty()) out.writeBoolean(false);
            else out.writeBoolean(true);
            out.writeObject(new Packet(getPlayerNames()));  // send a list of saved player names to client for them to select
        } catch (Exception e) {
            throw new RuntimeException("Error sending player information when loading: " + e);
        }

        try{
            packet = (Packet) in.readObject();
        } catch (Exception e) {
            System.out.println("Sudden connection closed when getting player name: " + e);

            try{
                this.socket.close();
            } catch (IOException e1) {
                throw new RuntimeException("Error closing socket: " + e);
            }

            return null;
        }

        name = packet.getMessage();

        if(server.players.stream().map(Player::getName).anyMatch(name::equals)){
            p = server.players.stream().filter(player -> player.name.equals(name)).findFirst().get();
            server.players.remove(p);
        } else {
            p = new Player(name);
        }

        server.activePlayers.add(p);

        try {
            if(p.getVillage().getVillageObjects().isEmpty()){
                out.writeObject(new Packet("create", new Object[]{p}));

                Position pos;
                boolean placed;

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
        } catch (IOException | ClassNotFoundException e) {
            throw new  RuntimeException(e);
        }

        return p;
    }

    /**
     * Get all player names that we have loaded
     * @return get all player names
     */
    private String[] getPlayerNames(){
        return server.players.stream().map(Player::getName).toArray(String[]::new);
    }

}
