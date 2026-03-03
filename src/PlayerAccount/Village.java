package src.PlayerAccount;

import src.PlayerAccount.Buildings.*;
import src.Utility.Position;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Vector;

import static src.GameEngine.INITIAL_RESOURCES;

// village is the game area the player owns, it holds info relating to the players village such as their resources, remaining gaurd time, and defences
public class Village implements Serializable {

    private final static long serialVersionUID = 1;

    private Map map;

    public LocalTime guardTime; // the time the a player is safe from attacks
    float defenceCapacity; // the defence score a players village has
    int maxBuildings; // the max limit of buildings a player can build
    Resources resources; // the amount of resources the player holds

    Village(){
        guardTime = LocalTime.now();
        defenceCapacity = 0;
        maxBuildings = 0;
        resources = new Resources(INITIAL_RESOURCES, INITIAL_RESOURCES, INITIAL_RESOURCES);

        map = new Map(10,10);
    }

    public float getDefenceCapacity() {
    return this.defenceCapacity;
    }

    public Resources getResources() {
    return this.resources;
  }

  public Map getMap(){
        return this.map;
  }

    public boolean purchaseBuilding(int type, Position pos) {

        Building building = null;

        // TODO: check if the player has resources to build the inputted building
        // TODO: check if max building limit has been reached
        switch(type) {
            case 1:
                building = new ArcherTower();
                break;
            case 2:
                building = new Cannon();
                break;
            case 3:
                building = new GoldMine();
                break;
            case 4:
                building = new IronMine();
                break;
            case 5:
                building = new LumberMill();
                break;
            default:
                return false;
        }

        return map.placeBuilding(building, pos);
    }

}