package src.PlayerAccount;

import src.GameEngine;
import src.PlayerAccount.Buildings.*;
import src.Utility.Position;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static src.GameEngine.INITIAL_RESOURCES;

// village is the game area the player owns, it holds info relating to the players village such as their resources, remaining gaurd time, and defences
public class Village implements Serializable {

    private final static long serialVersionUID = 1;

    private Map map;
    private List<VillageObject> villageObjects;

    public LocalTime guardTime; // the time the a player is safe from attacks
    float defenceCapacity; // the defence score a players village has
    int maxBuildings; // the max limit of buildings a player can build
    Resources resources; // the amount of resources the player holds

    Village(){
        guardTime = LocalTime.now();
        defenceCapacity = 0;
        maxBuildings = 20;
        resources = new Resources(INITIAL_RESOURCES, INITIAL_RESOURCES, INITIAL_RESOURCES);

        map = new Map(10,10);

        villageObjects = new ArrayList<>();
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
        Resources cost = null;

        // check if building limit has been reached
        if(villageObjects.size() >= maxBuildings){
            System.out.println("Maximum building limit ( " + maxBuildings + " ) has been reached");
            return false;
        }

        switch(type) {
            case 1:
                building = new ArcherTower();
                cost = GameEngine.ARCHER_TOWER_COST;
                break;

            case 2:
                building = new Cannon();
                cost = GameEngine.CANNON_COST;
                break;

            case 3:
                building = new GoldMine();
                cost = GameEngine.GOLD_MINE_COST;
                break;

            case 4:
                building = new IronMine();
                cost = GameEngine.IRON_MINE_COST;
                break;

            case 5:
                building = new LumberMill();
                cost = GameEngine.LUMBER_MILL_COST;
                break;
            default:
                return false;
        }

        // check if player has enough resources
        if(resources.getWood() < cost.getWood() ||
                resources.getGold() < cost.getGold() ||
                resources.getIron() < cost.getIron()){

            System.out.println("Not enough resources!");
            return false;
        }

        // try to place building
        boolean placed = map.placeBuilding(building, pos);
        if(!placed){
            return false;
        }

        //take cost away from resources
        resources.setWood(resources.getWood() - cost.getWood());
        resources.setGold(resources.getGold() - cost.getGold());
        resources.setIron(resources.getIron() - cost.getIron());

        villageObjects.add(building);
        return true;
    }

    public void addVillageObject(VillageHall vh) {
        villageObjects.add(vh);
    }
}