package src.PlayerAccount;
import java.time.LocalTime;

// a village object is any entity within the village (buildings and villagers)
public abstract class VillageObject {
  
  final Resources productionCost = new Resources(0, 0, 0); // Production cost is the cost assigned to build that building
  int populationSize; // population size is the population existing in that building

  LocalTime productionTime; // how long it takes to create the building

  int level; // the current level of the building

  private int maxLevel; // the highest level the building can be

  Resources upgradeCost; // the amount of resources to level up the building

  public Village myVillage; // the village the building relates to

  public Resources getProductionCost() {
    return this.productionCost;
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