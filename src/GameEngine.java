package src;

import src.GUI.GUI;
import src.GUI.TerminalGUI;
import src.PlayerAccount.Buildings.Building;
import src.PlayerAccount.Buildings.VillageHall;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.VillageObject;
import src.Utility.ArbitrerOld;
import src.Utility.InputChecker;
import src.Utility.Position;
import src.enums.Fighters;
import src.enums.Buildings;
import src.enums.View;
import src.exceptions.NoPlayerFoundException;
import src.Utility.AttackResolver;
import src.Utility.ChallengeAdapter;
import src.enums.AttackResult;

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
    public static final Resources FARM_COST = new Resources(20, 0, 10);

    static final public String[] VILLAGE_OPTIONS = new String[]{"shop", "upgrade", "attack", "train", "gather", "quit"};
    static final public String[] ATTACK_OPTIONS = new String[]{"y", "n", "next", "back"};
    static final public String[] TRAIN_OPTIONS = Stream.concat(Arrays.stream(Fighters.values()).map(val -> val.label), Arrays.stream(new String[]{"back"})).toArray(String[]::new);
    static final public String[] SHOP_OPTIONS = Stream.concat(Arrays.stream(Buildings.values()).map(val -> val.label), Arrays.stream(new String[]{"back"})).toArray(String[]::new);
    static final public String[] UPGRADE_OPTIONS = new String[]{"back"};


    private List<Player> players; // is dependant on the player
    private GUI gui;
    private final String file = "./src/data/players.ser";

    GameEngine() {}

    /**
     * Start the player game by loading/creating the player account and loading the UI.
     * string inp stores the players given input, the main game loop will run until the user enters 'quit'
     * the user can enter anything and the given inp will be checked to see if its valid and execute the command if it is
     */
    public void start() {
        Player p;
        String inp;
        String out;
        players = readPlayerFiles();

        p = getPlayer();
        if (p == null) return;

        gui = new GUI(p);
        inp = "";


        // main game loop, runs until player types "quit"
        // validates all inputs before the user can execute any commands
        while(!inp.equals("quit")){

            gui.showInputOptions(); // Shows input options based on player view

            inp = gui.getInp().toLowerCase();

            if(!this.isInputVerifiedAndAuthorzied(inp, p)) continue; // If the input is wrong then prompt the user again and do not process the input

            try {
                out = this.processInput(p, inp);
            } catch (NoPlayerFoundException e) {
                e.printStackTrace();    // This is server logging so server knows the error happened
                gui.displayError(e.getMessage()); // This is for display for player knows the error happened
                this.processInput(p, "back"); // Player is redirected to the main screen afterwards
                continue;
            }

            if(out != null){
                gui.displayMessage(out);
            }
        }

        // save the player
        savePlayers();
    }

    /**
     * authorize player inputs by checking if that input command exists at that current view
     * @param inp user input
     * @param p player we are verifying the inp for
     * @return a boolean value indicating the valididty of an input
     */
    private boolean isInputVerifiedAndAuthorzied(String inp, Player p) {
        InputChecker ic = new InputChecker();

        if(inp.equals("quit")) return true;

        try{
            if(!ic.isInputValid(inp, gui.currentView)) {
                System.out.println("Not a valid input");
                return false;
            }

            if(!ic.isInputAllowed(inp, gui.currentView, p)){
                System.out.println("You are not allowed to perform this action");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();    // This is server logging so server knows the error happened
            gui.displayError(e.getMessage()); // This is for display for player knows the error happened
            this.processInput(p, "back"); // Player is redirected to the main screen afterwards
            return false;
        }

        return true;
    }

    /**
     * Facilitate attack handles the attacking logic
     * This method calls the adapater class to determine if an attack is successful or not between 2 players
     * @param p
     * @return
     */
    public String facilitateAttackWithAdapter(Player p){

        Player potentialTarget;
        Set<Player> notEligible = new HashSet<>();
        notEligible.add(p);
        Collections.shuffle(players);

        // attack using the Challenge Adapter class
        AttackResolver resolver = new ChallengeAdapter();
        String inp;

        while(true){
            potentialTarget = this.findRandomPlayerToAttack(notEligible);

            if(potentialTarget == null) throw new NoPlayerFoundException("No player found to attack");

            gui.printVillageForAttack(potentialTarget);
            gui.showInputOptions();

            inp = gui.getInp();

            if(inp.equals("y")){

                AttackResult result = resolver.resolveAttack(p, potentialTarget);

                if (result == AttackResult.SUCCESS) {

                    Resources loot = potentialTarget.getVillage().getResources().clone();
                    loot.multiply(0.3);

                    potentialTarget.getVillage().getResources().subtract(loot);
                    p.getVillage().getResources().add(loot);

                    gui.displayAttackResults(1.0, loot);

                } else {
                    gui.displayAttackResults(0.0, new Resources());
                }

                this.processInput(p, "back");
                break;
            }

            if(inp.equals("next")){
                continue;
            }

            if(inp.equals("n")){
                this.processInput(p, "home");
                break;
            }
        }

        return null;
    }

    /**
     * This handles all attacking logic
     * @return an atttack success or a null value ot represent that the attack was canceled
     */
    public String facilitateAttack(Player p){

        Player potentialTarget;
        float attackScore;
        float defenceScore;
        float successRate;
        Set<Player> notEligible = new HashSet<>();
        notEligible.add(p); // Player cannot attack themselves
        Collections.shuffle(players);
        String inp;

        while(true){
            potentialTarget = this.findRandomPlayerToAttack(notEligible);

            if(potentialTarget == null) throw new NoPlayerFoundException("No player found to attack");

            attackScore = this.getAttackScore(p);
            defenceScore = this.getDefenceScore(potentialTarget);
            successRate = this.getSuccessRate(attackScore, defenceScore);

            gui.printVillageForAttack(potentialTarget);
            gui.showInputOptions();
            gui.showAttackDefenceSuccessRates(attackScore, defenceScore, successRate);

            inp = gui.getInp();

            if(inp.equals("y")){
                ArbitrerOld ar = new ArbitrerOld(p, potentialTarget);
                double outcome = ar.simulateAttack(successRate);

                // Update resources based on outcome
                double lootPercentage = outcome * LOOT_RATIO;
                Resources delta = potentialTarget.getVillage().getResources().clone();
                Resources defenderNewResources = potentialTarget.getVillage().getResources().clone();
                Resources attackerNewResources = potentialTarget.getVillage().getResources().clone();
                delta.multiply(lootPercentage);
                defenderNewResources.subtract(delta);
                attackerNewResources.add(delta);
                potentialTarget.village.setResources(defenderNewResources);
                potentialTarget.village.setResources(attackerNewResources);

                gui.displayAttackResults(outcome, delta);
                this.processInput(p, "back");
                break;
            }

            if(inp.equals("next")){
                continue;
            }

            if(inp.equals("N")){
                this.processInput(p, "home"); // Take the player back to home
                break;
            }
        }

        return null;
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
    public String processInput(Player p, String inp) {

        // This should never happen so thus if it does, we probably need to restart the game
        String err = "Unable to process input. Please restart the game by quiting (type: 'quit')";
        String inpCased = inp.toLowerCase();

        if(inpCased.equals("quit")) return null;

        switch(gui.currentView){
            case VILLAGE -> {
                return handleVillageInput(p, inpCased);
            }
            case SHOP -> {
                return handleShopInput(p, inpCased);
            }
            case UPGRADE -> {
                return handleUpgradeInput(p, inpCased);
            }
            case TRAIN -> {
                return handleTrainInput(p, inpCased);
            }
            case ATTACK -> gui.currentView = View.VILLAGE;
            default -> gui.displayError(err);
        }

        return null;
    }

    /**
     * upgrade inputs checks if a valid number is entered, and if you have the resources to build the structure
     * upgrade will list all of your buildings, this checks if you selected a building within the list and if you can do
     * the upgrade
     *
     * @param p curr player
     * @param inp the input being procesesd
     * @return a string indicating the outcome of an event
     */
    private String handleUpgradeInput(Player p, String inp) {

        if(inp.equals("back")) {
            gui.currentView = View.VILLAGE;
            return null;
        }

        int index;

        try{
            index = Integer.parseInt(inp);
        }catch(Exception e){
            return "Invalid building selection.";
        }

        List<VillageObject> buildings = p.village.getVillageObjects();

        if(index < 1 || index > buildings.size()){
            return "Invalid building number.";
        }

        VillageObject obj = buildings.get(index - 1);

        if(!(obj instanceof Building)){
            return "Cannot upgrade this object.";
        }

        boolean success = p.village.upgradeBuilding((Building)obj);

        if(success){
            return "Building upgraded successfully!";
        }

        return "Upgrade failed.";
    }

    /**
     * handles all the inputs for training units, and creates the unit if valid input is provided
     * @param inp
     * @return
     */
    private String handleTrainInput(Player p, String inp) {

        if(inp.equals("back")) {
            gui.currentView = View.VILLAGE;
            return null;
        }

        Fighters fighter = Fighters.valueOf(inp.toUpperCase());
        Resources cost = fighter.getFighterCost();

        if(cost == null) {
            throw new NullPointerException("Fighter cost is null.");
        }

        p.village.resources.subtract(cost);
        p.createUnit(fighter);

        return fighter + " created successfully!";
    }

    /**
     * village is the main menu, the valid inputs are all of the other game states the player can set.
     * shop, attack, upgrade, train and gather. once a view is selected we switch the current view
     * @param inp
     * @return
     */
    private String handleVillageInput(Player p, String inp){
        switch (inp) {
            case "shop":
                gui.currentView = View.SHOP;
                return null;
            case "attack":
                gui.currentView = View.ATTACK;
                //return this.facilitateAttack(p);
                return this.facilitateAttackWithAdapter(p);
            case "upgrade":
                gui.currentView = View.UPGRADE;
                return null;
            case "train":
                gui.currentView = View.TRAIN;
                return null;
            case "gather":
                p.village.gatherResources();
                return null;
            default:
                // This should not happen if we already validate inputs beforehand
                // Try throwing an error
                return "Please enter a proper input";
        }
    }

    /**
     * handles shop inputs, first it checks if you selected a valid building. then it verifies if your coordinates to
     * place the building are valid
     * @param inp
     * @return
     */
    private String handleShopInput(Player p, String inp){

        // exit shop
        if(inp.equals("back")){
            gui.currentView = View.VILLAGE;
            return null;
        }

        Buildings building;

        try{
            building = Buildings.valueOf(inp.toUpperCase());
        }catch(Exception e){
            return "invalid shop selection";
        }

        gui.displayMessage("Enter X coordinate for your building:");
        String x_temp = gui.getInp();
        gui.displayMessage("Enter Y coordinate for your building:");
        String y_temp = gui.getInp();

        int x = Integer.parseInt(x_temp);
        int y = Integer.parseInt(y_temp);

        Position pos = new Position(x,y);

        boolean success = p.village.purchaseBuilding(building,pos);

        if(success){
            gui.currentView = View.VILLAGE;
            return "Building was placed";
        }

        return "Could not place building";
    }

    /**
     * finds random player to attack
     * @param notEligible
     * @return
     */
    public Player findRandomPlayerToAttack(Set<Player> notEligible) {

        List<Player> eligible = players.stream()
                .filter(player -> !notEligible.contains(player))
                .filter(player -> player.getVillage()
                        .guardTime.isBefore(LocalTime.now()))
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
     * determines which player account is loaded, if there are no players the user can create a palyer
     * @return
     */
    public Player getPlayer(){
        Player p;

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

    /**
     * creates a player and adds them to the player list. then has them place their initial town hall to begin the game
     * @return
     */
    public Player createPlayer(){
        Player p;
        String name;
        if( TerminalGUI.promptAccountCreation()) {
            name = gui.getName();
            p = new Player(this, name);
            players.add(p);
            this.placeInitialTownHall(p, gui);
        } else return null;

        return p;
    }

    /**
     * same as shop but instead of selecting a building it just checks the validity of the coordinates when your
     * placing your initial town hall
     */
    public void placeInitialTownHall(Player p, GUI gui){
        gui.printVillageHallPlacementMessage();

        while(true){

            gui.displayMessage("Enter X coordinate for your Village Hall:");
            String x_temp = gui.getInp();
            gui.displayMessage("Enter Y coordinate for your Village Hall:");
            String y_temp = gui.getInp();

            int x = Integer.parseInt(x_temp);
            int y = Integer.parseInt(y_temp);

            Position pos = new Position(x,y);

            boolean placed = p.placeTownHall(pos);

            if(placed){
                break;
            }

            System.out.println("Invalid position. Try again.");
        }
    }

    /**
     * loads player data from serialized file
     * @return
     */
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

    /**
     * serializes the player object and saves their data
     */
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
     * @param player get the players attack score
     * @return players attack score
     */
    public float getAttackScore(Player player) {

        AtomicReference<Float> attackScore = new AtomicReference<>(0f);

        player.fighters.forEach((f, amount) -> attackScore.updateAndGet(v ->
                v + f.getAttackScore() * amount
                        + (amount > 1 ? amount * 0.5f : 0f)
        ));

        return attackScore.get();
    }

    /**
     * gets the defence score from the players village
     * @param player
     * @return
     */
    public float getDefenceScore(Player player) {
        return player.getVillage().getDefenceCapacity();
    }

    /**
     * gets the success rate after an attack
     * @param attackScore
     * @param defenceScore
     * @return
     */
    private float getSuccessRate(float attackScore, float defenceScore) {
        if(attackScore == 0) return 0;
        if(defenceScore == 0) return 1;

        return Math.min(attackScore/defenceScore, 1f);
    }

    /**
     * starts the game
     * @param args
     */
    public static void main(String[] args) {
      GameEngine engine = new GameEngine();
      engine.start();
    }
}