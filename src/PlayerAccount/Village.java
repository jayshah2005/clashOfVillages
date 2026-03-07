package src.PlayerAccount;

import src.GameEngine;
import src.PlayerAccount.Buildings.*;
import src.Utility.Position;
import src.enums.Buildings;

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

    public boolean purchaseBuilding(Buildings buildingType, Position pos) {

        // check if building limit has been reached
        if(villageObjects.size() >= maxBuildings){
            System.out.println("Maximum building limit ( " + maxBuildings + " ) has been reached");
            return false;
        }

        Building building = buildingType.getBuildingObject();
        Resources cost = buildingType.getBuildingCost();

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

        // subtract cost
        resources.subtract(cost);

        villageObjects.add(building);

        return true;
    }

    public Resources gatherResources(){
        int wood = 0;
        int gold = 0;
        int iron = 0;

        for(VillageObject obj : villageObjects){ // gather resources from all production buildings in villageObjects
            if(obj instanceof ProductionBuildings){
                ProductionBuildings pb = (ProductionBuildings) obj;
                int produced = pb.collectResources();
                if(pb instanceof GoldMine){
                    gold += produced;
                }
                else if(pb instanceof IronMine){
                    iron += produced;
                }
                else if(pb instanceof LumberMill){
                    wood += produced;
                }
            }
        }

        // update player storage
        resources.setWood(resources.getWood() + wood);
        resources.setGold(resources.getGold() + gold);
        resources.setIron(resources.getIron() + iron);

        return new Resources(wood, gold, iron);
    }

    public void setResources(Resources resources){
        this.resources = resources;
    }

    public void addVillageObject(VillageHall vh) {
        villageObjects.add(vh);
    }
}