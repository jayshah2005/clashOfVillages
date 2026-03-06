package src.PlayerAccount;

import org.w3c.dom.ls.LSOutput;
import src.GUI.GUI;
import src.GameEngine;
import src.PlayerAccount.Buildings.VillageHall;
import src.Utility.Position;
import src.enums.Fighters;
import src.enums.View;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.Map;

import static src.enums.Fighters.ARCHER;

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

    public Village findVillageToAttack() { // players can continue to find a village to attack until they find one they want to attack
        return null;
    }

    public float attack() { // players can then determine they want to attack the found village
        return 0.0f;
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


    public GUI getGUI() {
        return gui;
    }

    public void showInputOptions() {
        gui.showInputOptions(this);
    }

    public List<?> processInput(String inp) {

        // This should never happen so thus if it does, we probably need to restart the game
        String err = "Unable to process input. Please restart the game by quiting (type: 'quit')";
        String InpCased = inp.toLowerCase();

        switch(currentView){
            case VILLAGE -> handleVillageInput(InpCased);
            case SHOP -> handleShopInput(InpCased);
            case TRAIN -> handleTrainInput(InpCased);
            case ATTACK -> currentView = View.VILLAGE;
            default -> displayError(err);
        }

        return null;
    }

    private void createUnit(Fighters type){

    }

    private List<?> handleTrainInput(String inp) {
        return null;
    }

    private List<?> handleVillageInput(String inp){
        switch (inp.toLowerCase()) {
            case "shop":
                this.currentView = View.SHOP;
                return null;
            case "attack":
                this.currentView = View.ATTACK;
                return this.gameEngine.facilitateAttack(this);
            case "upgrade":
                return null;
            case "gather":
                // TODO: Need to implement the gather feature
                return null;
            case "quit":
                return null;
            case "train":
                this.currentView = View.TRAIN;
                return null;
            default:
                // This should not happen if we already validate inputs beforehand
                // Try throwing an error
                return Collections.singletonList("Please enter a proper input");
        }
    }

    private List<?> handleShopInput(String inp){
        int choice;

        // Exit shop
        if(inp.equalsIgnoreCase("back")){
            currentView = View.VILLAGE;
            return null;
        }

        try{
            choice = Integer.parseInt(inp);
        } catch (Exception e){
            return Collections.singletonList("Invalid shop seleciton");
        }

        int x = gui.promptForCoordinate("Enter X:");
        int y = gui.promptForCoordinate("Enter Y:");

        Position pos = new Position(x,y);

        boolean success = village.purchaseBuilding(choice,pos);

        if(success){
            printVillage();
            currentView = View.VILLAGE;
            return Collections.singletonList("Building was placed");
        }

        return Collections.singletonList("Could not place building");
    }

    public void placeInitialTownHall(){
        gui.printVillageHallPlacementMessage();

        while(true){

            int x = gui.promptForCoordinate("Enter X coordinate for your Village Hall:");
            int y = gui.promptForCoordinate("Enter Y coordinate for your Village Hall:");

            Position pos = new Position(x,y);

            VillageHall vh = new VillageHall();

            boolean placed = village.getMap().placeBuilding(vh, pos);

            if(placed){

                village.addVillageObject(vh);

                System.out.println("Village Hall placed successfully!");

                printVillage();

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

    public void reload(GameEngine gameEngine) {
        this.gui = new GUI(this);
        this.gameEngine = gameEngine;
        this.currentView = View.VILLAGE;

        if(this.fighters == null) initializeArmy();
    }
}