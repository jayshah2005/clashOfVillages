package src;

import src.GUI.TerminalGUI;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.Village;
import src.PlayerAccount.VillageObject;
import src.PlayerAccount.Units.Fighter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// game engine holds all of the methods that control the game
public class GameEngine {

    private ArrayList<Player> players; // is dependant on the player

    GameEngine() {
        players = new ArrayList<>();
    }

    /**
     * Start the player game by loading/creating the player account and loading the UI.
     */
    public void start() {
        // TODO: check if the player account exists
        boolean accExists = false;
        Player p;

        if (!accExists){
            if( TerminalGUI.promptAccountCreation()) {
                p = new Player(this);
                players.add(p);
            } else return;
        } else {
            p = new Player(this);
        }

        String inp = "";

        while(!inp.equals("quit")){
            // Somehow print options and then get input
            inp = p.getInp();
            // based on input perform action
        }

        // save the player
        savePlayers();
    }

    public void savePlayers(){
        try (FileOutputStream fileOut = new FileOutputStream("./playerData/players.ser")) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (Player p : players){
                out.writeObject(p);
            }
            System.out.println("Players have been saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public static void main() {
      GameEngine engine = new GameEngine();
      engine.start();

    }
}