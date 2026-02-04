package PlayerAccount;
import java.time.LocalTime;
import PlayerAccount.Resources;

public abstract class VillageObject {

  final Resources productionCost = new Resources(0, 0, 0);
  int populationSize;

  LocalTime productionTime;

  int level;

  private int maxLevel;

  Resources upgradeCost;

  public Village myVillage;

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