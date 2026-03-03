package src;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.Village;
import src.PlayerAccount.VillageObject;
import src.PlayerAccount.Units.Fighter;
import src.Utility.Position;
import src.enums.View;

import java.io.*;
import java.util.ArrayList;

// game engine holds all of the methods that control the game
public class GameEngine {

    private ArrayList<Player> players; // is dependant on the player
    private final String file = "./src/data/players.ser";

    GameEngine() {}

    /**
     * Start the player game by loading/creating the player account and loading the UI.
     */
    public void start() {
        Player p;
        String inp;
        String out;
        players = readPlayerFiles();

        p = getPlayer();
        if (p == null) return;

        inp = "";
        while(!inp.equals("quit")){
            p.showInputOptions();
            inp = p.getInp();
            // TODO: Validate input before processing it
            out = p.processInput(inp);

            if(out != null){
                System.out.println(out);
            }
        }

        // save the player
        savePlayers();
    }

    public Player getPlayer(){
        Player p;

        if (players.isEmpty()) {
            if( TerminalGUI.promptAccountCreation()) {
                p = new Player(this);
                players.add(p);
            } else return null;
        }else if(TerminalGUI.promptAccountLoading()){
            p = GUI.selectPlayer(players);
            p.reload(this);
        } else{
            if( TerminalGUI.promptAccountCreation()) {
                p = new Player(this);
                players.add(p);
            } else return null;
        }

        return p;
    }

    public ArrayList<Player> readPlayerFiles() {

        ArrayList<Player> tempPlayersList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
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

    public void savePlayers(){
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
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
     * open shop will prompt the user with the shop selection to purchase a building. Once they have chosen a building
     * they will be prompted to input the X and Y coordinates and the building will be placed on the map
     * @param p
     */
    private void openShop(Player p) {

        // Your code never does this. You can add a setter to the player view but it's better for the player to just handle this
        // p.currentView = View.SHOP
        int choice = p.getGUI().promptShopSelection();

        int x = p.getGUI().promptForCoordinate("Enter X:");
        int y = p.getGUI().promptForCoordinate("Enter Y:");

        Position pos = new Position(x, y);

        boolean success = p.getVillage().purchaseBuilding(choice, pos);

        if(success) {
            p.getVillage().getMap().printMap();
        }
    }

    public Village findRandomVillage() { // finds random village for player to attack
    return null;
    }

    // TODO: is getSuccessRate really needed if we have the Arbitrer class in utility package determining this
    /*
    public int getSuccessRate(Player attacker, Player defender) { // deteremines if the players attack was successful
    return 0;
    }
     */

    public Resources getLoot(Player player) { // if attack is successful, determines the loot they will recieve
    return null;
    }

    public Fighter[] generateArmy(Player player) { // generates the units that will be used to attack / defend
    return null;
    }

    public boolean canUpgrade(Player player, VillageObject obj) { // checks if the player has the resources to upgrade an object
    return false;
    }

    public boolean canAttack(Player player) { // checks if the player can be attacked
    return false;
    }

    public boolean canProduce(Player player, VillageObject obj) { // checks if the building can be produced
    return false;
    }

    public float getAttackScore(Player player) {
    return 0.0f;
    }

    public float getDefenceScore(Player player) {
    return 0.0f;
    }

    public float getLootScore(Player player) {
    return 0.0f;
    }

    public float getOverallScore(Player player) {
    return 0.0f;
    }

    public static void main(String[] args) {
      GameEngine engine = new GameEngine();
      engine.start();

    }
}