package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
import src.PlayerAccount.Buildings.Building;
import src.PlayerAccount.Buildings.VillageHall;
import src.Utility.Position;
import src.enums.Buildings;
import src.enums.Fighters;
import src.enums.View;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.Map;

// Player class holds all the actions a player can do as well as all the info about the player
public class Player implements Serializable {

    @Serial
    private final static long serialVersionUID = 1;

    public Village village; // the village the player owns
    public String name;
    public Map<Fighters, Integer> fighters;
    public transient GUI gui; // the gui the player interacts with
    public transient GameEngine gameEngine;
    transient View currentView = View.VILLAGE;

    /**
     * player holds their name and village which will get stored when the game ends
     * they also have a current GUI and GameEngine. The player selects the current view
     * @param gameEngine
     */
    public Player(GameEngine gameEngine) {
        gui = new GUI(this);
        name = gui.getName();
        village = new Village();
        this.gameEngine = gameEngine;

        initializeArmy();
    }

    public void initializeArmy(){
        this.fighters = new HashMap<>();

        for(Fighters fighter : Fighters.values()){
            this.fighters.put(fighter,0);
        }
    }

    public void resetArmy() { // players can then determine they want to attack the found village
        initializeArmy(); // Since we attacked our army is reset
    }

    public void resetGuardTime(){
        this.village.guardTime = LocalTime.now().plusHours(1);
    }

      public String getInp(){
            return gui.getInp();
      }

    public String getName() {
        return name;
    }

    public Village getVillage() {
        return village;
    }

    public void showInputOptions() {
        gui.showInputOptions(this);
    }

    public void showAttackDefenceSuccessRates(float attackScore, float defenceScore,  float successRate){
        gui.showAttackDefenceSuccessRates(attackScore, defenceScore, successRate);
    }

    /**
     * given an input the input will be checked to see if its valid and authorized based on the current view.
     * so if you are in the village view you can select shop, upgrade, train, attack or quit
     * these are all of the valid inputs at that current view.
     * @param inp
     * @return
     */
    public String processInput(String inp) {

        // This should never happen so thus if it does, we probably need to restart the game
        String err = "Unable to process input. Please restart the game by quiting (type: 'quit')";
        String inpCased = inp.toLowerCase();

        if(inpCased.equals("quit")) return null;

        switch(currentView){
            case VILLAGE -> {
                return handleVillageInput(inpCased);
            }
            case SHOP -> {
                return handleShopInput(inpCased);
            }
            case UPGRADE -> {
                return handleUpgradeInput(inpCased);
            }
            case TRAIN -> {
                return handleTrainInput(inpCased);
            }
            case ATTACK -> currentView = View.VILLAGE;
            default -> displayError(err);
        }

        return null;
    }

    /**
     * upgrade inputs checks if a valid number is entered, and if you have the resources to build the structure
     * upgrade will list all of your buildings, this checks if you selected a building within the list and if you can do
     * the upgrade
     * @param inp
     * @return
     */
    private String handleUpgradeInput(String inp) {

        if(inp.equals("back")) {
            currentView = View.VILLAGE;
            return null;
        }

        int index;

        try{
            index = Integer.parseInt(inp);
        }catch(Exception e){
            return "Invalid building selection.";
        }

        List<VillageObject> buildings = village.getVillageObjects();

        if(index < 1 || index > buildings.size()){
            return "Invalid building number.";
        }

        VillageObject obj = buildings.get(index - 1);

        if(!(obj instanceof Building)){
            return "Cannot upgrade this object.";
        }

        boolean success = village.upgradeBuilding((Building)obj);

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
    private String handleTrainInput(String inp) {

        if(inp.equals("back")) {
            currentView = View.VILLAGE;
            return null;
        }

        Fighters fighter = Fighters.valueOf(inp.toUpperCase());
        Resources cost = fighter.getFighterCost();

        if(cost == null) {
            throw new NullPointerException("Fighter cost is null.");
        }

        this.village.resources.subtract(cost);
        createUnit(fighter);

        return fighter + " created successfully!";
    }

    private void createUnit(Fighters type){
        this.fighters.compute(type, (k, curr) -> curr + 1);
    }

    /**
     * village is the main menu, the valid inputs are all of the other game states the player can set.
     * shop, attack, upgrade, train and gather. once a view is selected we switch the current view
     * @param inp
     * @return
     */
    private String handleVillageInput(String inp){
        switch (inp) {
            case "shop":
                this.currentView = View.SHOP;
                return null;
            case "attack":
                this.currentView = View.ATTACK;
                return this.gameEngine.facilitateAttack(this);
            case "upgrade":
                this.currentView = View.UPGRADE;
                return null;
            case "train":
                this.currentView = View.TRAIN;
                return null;
            case "gather":
                village.gatherResources();
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
    private String handleShopInput(String inp){

        // exit shop
        if(inp.equals("back")){
            currentView = View.VILLAGE;
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

        boolean success = village.purchaseBuilding(building,pos);

        if(success){
            currentView = View.VILLAGE;
            return "Building was placed";
        }

        return "Could not place building";
    }

    /**
     * same as shop but instead of selecting a building it just checks the validity of the coordinates when your
     * placing your initial town hall
     */
    public void placeInitialTownHall(){
        gui.printVillageHallPlacementMessage();

        while(true){

            gui.displayMessage("Enter X coordinate for your Village Hall:");
            String x_temp = gui.getInp();
            gui.displayMessage("Enter Y coordinate for your Village Hall:");
            String y_temp = gui.getInp();

            int x = Integer.parseInt(x_temp);
            int y = Integer.parseInt(y_temp);

            Position pos = new Position(x,y);

            VillageHall vh = new VillageHall();

            boolean placed = village.getMap().placeBuilding(vh, pos);

            if(placed){
                village.addVillageObject(vh);
                break;
            }

            System.out.println("Invalid position. Try again.");
        }
    }

    /**
     * Displays the result of a player attack
     * @param outcome the percentage of attack success
     * @param loot the amount of loot won
     */
    public void displayAttackResults(double outcome, Resources loot) {
        gui.displayAttackResults(outcome, loot);
    }

    /**
     * Prints the defending village
     * @param defender the defending village
     */
    public void printVillageForAttack(Player defender){
        gui.printVillageForAttack(defender);
    }

    /**
     * Get the current view of the player
     * @return the current view
     */
    public View getCurrentView() {
        return currentView;
    }

    /**
     * Displays an error to the user
     * @param error the error to be displayed
     */
    public void displayError(String error){
        gui.displayError(error);
    }

    /**
     * Displays a message to the user
     * @param message the message to be displayed
     */
    public void displayMessage(String message){
        gui.displayMessage(message);
    }

    /**
     * Reloads player transient variables once player is read from a serialized file
     * @param gameEngine central game engine that manages everything
     */
    public void reload(GameEngine gameEngine) {
        this.gui = new GUI(this);
        this.gameEngine = gameEngine;
        this.currentView = View.VILLAGE;

        if(this.fighters == null) initializeArmy();
    }
}