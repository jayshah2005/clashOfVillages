package PlayerAccount.Units;

public abstract class Gatherer extends Villager {

  private int productionCapacity;

  private int maxCapacity;

  public int getProductionCapacity() {
    return this.productionCapacity;
  }

  public int getMaxCapacity() {
    return this.maxCapacity;
  }

}