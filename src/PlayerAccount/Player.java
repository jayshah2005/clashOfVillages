package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
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

    public boolean build(VillageObject obj) { // players can build a specified village object
        return false;
    }

    public boolean upgrade(VillageObject obj) { // players can upgrade a specified object
        return false;
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
            case TRAIN -> {
                return handleTrainInput(inpCased);
            }
            case ATTACK -> currentView = View.VILLAGE;
            default -> displayError(err);
        }

        return null;
    }

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

    private String handleVillageInput(String inp){
        switch (inp) {
            case "shop":
                this.currentView = View.SHOP;
                return null;
            case "attack":
                this.currentView = View.ATTACK;
                return this.gameEngine.facilitateAttack(this);
            case "upgrade":
                return null;
            case "train":
                this.currentView = View.TRAIN;
                return null;
            case "gather":
                this.currentView = View.GATHER;
                return null;
            default:
                // This should not happen if we already validate inputs beforehand
                // Try throwing an error
                return "Please enter a proper input";
        }
    }

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

        gui.displayMessage("Enter X coordinate for your Village Hall:");
        String x_temp = gui.getInp();
        gui.displayMessage("Enter Y coordinate for your Village Hall:");
        String y_temp = gui.getInp();

        int x = Integer.parseInt(x_temp);
        int y = Integer.parseInt(y_temp);

        Position pos = new Position(x,y);

        boolean success = village.purchaseBuilding(building,pos);

        if(success){
            printVillage();
            currentView = View.VILLAGE;
            return "Building was placed";
        }

        return "Could not place building";
    }

    public void placeInitialTownHall(){
        gui.printVillageHallPlacementMessage();

        while(true){

            gui.displayMessage("Enter X coordinate for your Village Hall:");
            String x_temp = gui.getInp();
            gui.displayMessage("Enter Y coordinate for your Village Hall:");
            String y_temp = gui.getInp();

            // TODO: Make sure get input return integer
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

    public void printVillage() {
        gui.printVillage(this);
    }

    public void printVillageForAttack(Player defender){
        gui.printVillageForAttack(defender);
    }

    public View getCurrentView() {
        return currentView;
    }

    public void displayError(String error){
        gui.displayError(error);
    }

    public void displayMessage(String message){
        gui.displayMessage(message);
    }

    public void reload(GameEngine gameEngine) {
        this.gui = new GUI(this);
        this.gameEngine = gameEngine;
        this.currentView = View.VILLAGE;
        this.village.defenceCapacity = 5;

        if(this.fighters == null) initializeArmy();
    }
}