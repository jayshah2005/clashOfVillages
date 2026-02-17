package src.PlayerAccount.Units;

// Gatherers are any villagers who are able to produce resources
public abstract class Gatherer extends Villager {

  private int productionCapacity; // resource gather rate of the villager

  private int maxCapacity; // the amount of resources a vilalger can hold

  public int getProductionCapacity() {
    return this.productionCapacity;
  }

  public int getMaxCapacity() {
    return this.maxCapacity;
  }

}