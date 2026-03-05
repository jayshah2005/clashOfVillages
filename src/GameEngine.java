package src;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.VillageObject;
import src.PlayerAccount.Units.Fighter;
import src.Utility.Arbitrer;
import src.Utility.Position;
import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// game engine holds all of the methods that control the game
public class GameEngine {

    /**
     * Constant variables that can be changed easily from a central position
     */
    static final public double LOOT_RATIO = 0.5;
    static final public int INITIAL_RESOURCES = 100;
    static final public Resources ARCHER_COST = new Resources(15, 0, 5);
    static final public Resources CATAPULT_COST = new Resources(10, 10, 10);
    static final public Resources SOLDIER_COST = new Resources(10, 0, 5);
    static final public Resources KNIGHT_COST = new Resources(2, 5, 10);
    static final public Resources DEFAULT_COST = new Resources(10, 10, 10);

    private List<Player> players; // is dependant on the player
    private final String file = "./src/data/players.ser";

    GameEngine() {}

    /**
     * Start the player game by loading/creating the player account and loading the UI.
     */
    public void start() {
        Player p;
        String inp;
        List<?> out;
        players = readPlayerFiles();

        p = getPlayer();
        if (p == null) return;

        inp = "";
        while(!inp.equals("quit")){
            p.showInputOptions();
            inp = p.getInp();
            // TODO: Validate input before processing it
            // Basically have a function that validateInput() that looks at a player view and check if the view allows for an input.
            // If the input is wrong then prompt the user again and do not process the input
            out = p.processInput(inp);

            if(out != null){
                handleOutput(out);
            }
        }

        // save the player
        savePlayers();
    }

    private void handleOutput(List<?> out) {
        for (Object o : out) {
            if(o instanceof String) {
                System.out.println(o);
            }
        }
    }

    /**
     * This handles all attacking logic
     * @return an atttack success or a null value ot represent that the attack was canceled
     */
    public List<?> facilitateAttack(Player p){

        Player potentialTarget;
        Set<Player> notEligible = new HashSet<>();
        notEligible.add(p); // Player cannot attack themselves
        Collections.shuffle(players);
        String inp = "";

        if(players.size() < 2){
            // There is no player you can attack since you cannot attack yourself
        }

        while(true){
            potentialTarget = this.findRandomPlayerToAttack(notEligible);
            p.printVillageForAttack(potentialTarget);
            p.showInputOptions();
            inp = p.getInp();

            if(inp.equals("y")){
                // perform attack
            }

            if(inp.equals("next")){
                continue;
            }

            if(inp.equals("N")){
                p.processInput("home"); // Take the player back to home
                break;
            }
        }

        return null;
    }

    public Player findRandomPlayerToAttack(Set<Player> notEligible) {

        List<Player> eligible = players.stream()
                .filter(player -> !notEligible.contains(player))
                .filter(player -> player.getVillage()
                        .guardTime.isAfter(LocalTime.now()))
                .collect(Collectors.toList());

        if (eligible.isEmpty()) {
            // Maybe throw an error that there is isn't any player to play with
            return null;
        }

        Collections.shuffle(eligible);
        return eligible.get(0);
    }

    public Player getPlayer(){
        Player p = null;

        if (players.isEmpty()) {
            p = createPlayer();
            if(p == null) return null;
        }else if(TerminalGUI.promptAccountLoading()){
            p = GUI.selectPlayer(players);
            if(p == null) return getPlayer();
            p.reload(this);
        } else{
            p = createPlayer();
            if(p == null) return null;
        }

        return p;
    }

    public Player createPlayer(){
        Player p;
        if( TerminalGUI.promptAccountCreation()) {
            p = new Player(this);
            players.add(p);
        } else return null;

        return p;
    }

    public List<Player> readPlayerFiles() {

        List<Player> tempPlayersList = new ArrayList<>();

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