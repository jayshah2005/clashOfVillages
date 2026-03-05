package src.PlayerAccount;
import java.io.Serializable;
import java.time.LocalTime;

import static src.GameEngine.DEFAULT_COST;

// a village object is any entity within the village (buildings and villagers)
public abstract class VillageObject implements Serializable {

    public static final Resources cost = DEFAULT_COST; // Production cost is the cost assigned to build that building

    int populationSize; // population size is the population existing in that building

    LocalTime productionTime; // how long it takes to create the building

    int level = 1; // the current level of the building

    private int maxLevel = 5; // the highest level the building can be

    Resources upgradeCost; // the amount of resources to level up the building

    public Village myVillage; // the village the building relates to

    public Resources getProductionCost() {
    return this.cost;
    }

    public int getPopulationSize() {
    return this.populationSize;
    }

    public Resources getUpgradeCost() {
    return this.upgradeCost;
    }

    public int getLevel() {
    return this.level;
    }

    public LocalTime getProductionTime() {
    return this.productionTime;
    }

    public int getMaxLevel() {
    return this.maxLevel;
    }
}