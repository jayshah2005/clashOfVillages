package src.PlayerAccount;

import src.GameEngine;
import src.PlayerAccount.Buildings.*;
import src.PlayerAccount.Units.Gatherer;
import src.PlayerAccount.Units.Villager;
import src.PlayerAccount.Units.Workers;
import src.PlayerAccount.Units.Miner;
import src.PlayerAccount.Units.Collector;
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
    private int maxInhabitants;
    private List<Villager> inhabitants;
    private int foodCapacity;

    Village(){
        guardTime = LocalTime.now();
        defenceCapacity = 0;
        maxBuildings = 20;
        resources = new Resources(INITIAL_RESOURCES, INITIAL_RESOURCES, INITIAL_RESOURCES);

        map = new Map(10,10);

        villageObjects = new ArrayList<>();

        maxInhabitants = 50;
        inhabitants = new ArrayList<>();

        foodCapacity = 5;

        //default Village inhabitants
        addInhabitant(new Workers());
        addInhabitant(new Workers());
        addInhabitant(new Workers());
    }

    public boolean addInhabitant(Villager v){

        // check village capacity
        if(inhabitants.size() >= maxInhabitants){
            //System.out.println("Village has reached max inhabitants.");
            return false;
        }

        // check food capacity
        if(inhabitants.size() + 1 > getFeedPopulationSize()){
            System.out.println("Not enough food to support more villagers.");
            return false;
        }

        inhabitants.add(v);
        return true;
    }

    public float getDefenceCapacity() {

        float defence = 0;

        for(VillageObject obj : villageObjects){

            // add building health
            if(obj instanceof Building){
                Building b = (Building) obj;
                defence += b.getHitpoints();
            }

            // add damage / range for defences
            if(obj instanceof ArcherTower){
                ArcherTower tower = (ArcherTower) obj;
                defence += tower.getDamage();
                defence += tower.getRange();
            }

            if(obj instanceof Cannon){
                Cannon cannon = (Cannon) obj;
                defence += cannon.getDamage();
                defence += cannon.getRange();
            }
        }

        return defence;
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

        // check if the worker can be fed before building
        if(!(building instanceof Farm)){
            if(inhabitants.size() + 1 > getFeedPopulationSize()){
                System.out.println("Not enough food capacity to support another villager.");
                return false;
            }
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

        // subtract cost
        resources.subtract(cost);

        villageObjects.add(building);

        if(building instanceof ProductionBuildings){
            ProductionBuildings pb = (ProductionBuildings) building;

            for(Gatherer g : pb.getWorkers()){
                addInhabitant(g);
            }
        }

        return true;
    }

    public int getFeedPopulationSize(){
        int food = foodCapacity;

        for(VillageObject obj : villageObjects){
            if(obj instanceof Farm){
                food += ((Farm)obj).getFeedsPopulationSize();
            }
        }

        return food;
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

    public int getPopulation(){
        return inhabitants.size();
    }

    public int getMaxPopulation(){
        return maxInhabitants;
    }

    public List<VillageObject> getVillageObjects(){
        return villageObjects;
    }

    public boolean upgradeBuilding(Building b){

        int villageHallLevel = getVillageHallLevel();

        // village hall sets the max level
        if(!(b instanceof VillageHall) && b.getLevel() >= villageHallLevel){
            System.out.println("Upgrade your Village Hall first.");
            return false;
        }

        Resources cost = b.getUpgradeCost();

        if(resources.getWood() < cost.getWood() || resources.getGold() < cost.getGold() || resources.getIron() < cost.getIron()){
            System.out.println("Not enough resources to upgrade.");
            return false;
        }

        resources.subtract(cost);

        return b.upgrade();
    }

    public int getVillageHallLevel() {

        for(VillageObject obj : villageObjects){
            if(obj instanceof VillageHall){
                return ((VillageHall)obj).getLevel();
            }
        }

        return 1;
    }
}