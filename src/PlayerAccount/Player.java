package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
import src.PlayerAccount.Buildings.VillageHall;
import src.Utility.Position;
import src.enums.Fighters;
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

    /**
     * player holds their name and village which will get stored when the game ends
     * they also have a current GUI and GameEngine. The player selects the current view
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        village = new Village();
        initializeArmy();
    }

    /**
     * Initialize the army with all troop values set to 0
     */
    public void initializeArmy(){
        this.fighters = new HashMap<>();

        for(Fighters fighter : Fighters.values()){
            this.fighters.put(fighter, 0);
        }
    }

    /**
     * Reset guard time to be current time + 1 hour
     */
    public void resetGuardTime(){
        this.village.guardTime = LocalDateTime.now().plusHours(1);
    }

    /**
     * Place the townhall
     * @param pos position where we want to place the townhall
     * @return whether the townhall is successfully placed or not
     */
    public Boolean placeTownHall(Position pos) {
        VillageHall vh = new VillageHall();
        Boolean placed = this.village.getMap().placeBuilding(new VillageHall(), pos);

        if(placed){
            village.addVillageObject(vh);
        }

        return placed;
    }

    /**
     * Create a new unit of type type
     * @param type the type of unit we want to create
     */
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

    public String getName() {
        return name;
    }

    public Village getVillage() {
        return village;
    }

    /**
     * Reloads player transient variables once player is read from a serialized file
     * @param gameEngine central game engine that manages everything
     */
    public void reload(GameEngine gameEngine) {
        if(this.fighters == null) initializeArmy();
    }
}