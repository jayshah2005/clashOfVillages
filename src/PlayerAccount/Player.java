package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
import src.PlayerAccount.Buildings.VillageHall;
import src.Utility.Position;
import src.enums.Fighters;
import src.enums.View;
import java.io.Serial;
import java.io.Serializable;
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

    /**
     * player holds their name and village which will get stored when the game ends
     * they also have a current GUI and GameEngine. The player selects the current view
     * @param gameEngine the game engine running the player
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
            this.fighters.put(fighter, 0);
        }
    }

    public void resetArmy() { // players can then determine they want to attack the found village
        initializeArmy(); // Since we attacked our army is reset
    }

    public void resetGuardTime(){
        this.village.guardTime = LocalTime.now().plusHours(1);
    }

    public String getName() {
        return name;
    }

    public Village getVillage() {
        return village;
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


    public void createUnit(Fighters type){
        try{
            this.fighters.compute(type, (k, curr) -> curr + 1);
        } catch(Exception e){
            this.initializeArmy();
            try {
                this.fighters.compute(type, (k, curr) -> curr + 1);
            } catch (Exception ignored){}
            // This should never happen. initializeArmy resets all curr to 0 so it cannot be null. If it is, it will probably cuz a bigger error first

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

        if(this.fighters == null) initializeArmy();
    }
}