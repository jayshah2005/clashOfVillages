package src;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.VillageObject;
import src.PlayerAccount.Units.Fighter;
import src.Utility.InputChecker;
import src.enums.Fighters;
import src.exceptions.NoPlayerFoundException;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    // building costs
    public static final Resources ARCHER_TOWER_COST = new Resources(20, 40, 10);
    public static final Resources CANNON_COST = new Resources(30, 30, 20);
    public static final Resources GOLD_MINE_COST = new Resources(0, 50, 10);
    public static final Resources IRON_MINE_COST = new Resources(10, 20, 50);
    public static final Resources LUMBER_MILL_COST = new Resources(50, 0, 10);

    // This is used by the input check to check for inputs.
    // For TRAIN_OPTIONS we use Fighters enum
    // TODO: Change shop to take proper input
    static final public String[] VILLAGE_OPTIONS = new String[]{"shop", "upgrade", "attack", "train", "gather", "quit"};
    static final public String[] ATTACK_OPTIONS = new String[]{"y", "n", "next", "back"};
    static final public String[] TRAIN_OPTIONS = Stream.concat(Arrays.stream(Fighters.values()).map(val -> val.label), Arrays.stream(new String[]{"back"})).toArray(String[]::new);
    static final public String[] SHOP_OPTIONS = new String[]{""};

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

            p.showInputOptions(); // Shows input options based on player view

            inp = p.getInp().toLowerCase();

            if(!this.isInputVerifiedAndAuthorzied(inp, p)) continue; // If the input is wrong then prompt the user again and do not process the input

            try {
                out = p.processInput(inp);
            } catch (NoPlayerFoundException e) {
                e.printStackTrace();    // This is server logging so server knows the error happened
                p.displayError(e.getMessage()); // This is for display for player knows the error happened
                p.processInput("back"); // Player is redirected to the main screen afterwards
                continue;
            }

            if(out != null){
                handleOutput(out);
            }
        }

        // save the player
        savePlayers();
    }

    private boolean isInputVerifiedAndAuthorzied(String inp, Player p) {
        InputChecker ic = new InputChecker();

        if(inp.equals("quit")) return true;

        try{
            if(!ic.isInputValid(inp, p)) {
                System.out.println("Not a valid input");
                return false;
            }

            if(!ic.isInputAllowed(inp, p)){
                System.out.println("You are not allowed to perform this action");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();    // This is server logging so server knows the error happened
            p.displayError(e.getMessage()); // This is for display for player knows the error happened
            p.processInput("back"); // Player is redirected to the main screen afterwards
            return false;
        }

        return true;
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
        float attackScore;
        float defenceScore;
        float successRate;
        Set<Player> notEligible = new HashSet<>();
        notEligible.add(p); // Player cannot attack themselves
        Collections.shuffle(players);
        String inp = "";

        if(players.size() < 2){
            // There is no player you can attack since you cannot attack yourself
        }

        while(true){
            potentialTarget = this.findRandomPlayerToAttack(notEligible);

            if(potentialTarget == null) throw new NoPlayerFoundException("No player found to attack");

            attackScore = this.getAttackScore(p);
            defenceScore = this.getDefenceScore(potentialTarget);
            successRate = this.getSuccessRate(attackScore, defenceScore);



            p.printVillageForAttack(potentialTarget);
            p.showInputOptions();
            p.showAttackDefenceSuccessRates(attackScore, defenceScore, successRate);

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
                        .guardTime.isBefore(LocalTime.now()))
                .collect(Collectors.toList());

        System.out.println("Final eligible players: " + eligible);

        if (eligible.isEmpty()) {
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
            p.placeInitialTownHall();
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
     * Calculates attack score based on army composition
     * There is static attackScore for each unit and then multiple units of same type give additional attack score
     * @param player
     * @return
     */
    public float getAttackScore(Player player) {

        AtomicReference<Float> attackScore = new AtomicReference<>(0f);

        player.fighters.forEach((f, amount) -> {

            attackScore.updateAndGet(v ->
                    v + f.getAttackScore() * amount
                            + (amount > 1 ? amount * 0.5f : 0f)
            );

        });

        return attackScore.get();
    }

    public float getDefenceScore(Player player) {
        return player.getVillage().getDefenceCapacity();
    }

    private float getSuccessRate(float attackScore, float defenceScore) {
        if(attackScore == 0) return 0;
        if(defenceScore == 0) return 100;

        return Math.min(attackScore/defenceScore, 1f);
    }

    /**
     * open shop will prompt the user with the shop selection to purchase a building. Once they have chosen a building
     * they will be prompted to input the X and Y coordinates and the building will be placed on the map
     * @param
     */
    //private void openShop(Player p) {

        // Your code never does this. You can add a setter to the player view but it's better for the player to just handle this
        // p.currentView = View.SHOP
        //int choice = p.getGUI().promptShopSelection();

        //int x = p.getGUI().promptForCoordinate("Enter X:");
        //int y = p.getGUI().promptForCoordinate("Enter Y:");

        //Position pos = new Position(x, y);

        //boolean success = p.getVillage().purchaseBuilding(choice, pos);

        //if(success) {
            //p.printVillage();
        //}
    //}

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

    public boolean canProduce(Player player, VillageObject obj) { // checks if the building can be produced
        return false;
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