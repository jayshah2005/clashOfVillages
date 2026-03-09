package src.PlayerAccount.Units;

// Gatherers are any villagers who are able to produce resources
public abstract class Gatherer extends Villager {

  int productionCapacity; // resource gather rate of the villager

  int maxCapacity; // the amount of resources a vilalger can hold

  public int getProductionCapacity() {
    return this.productionCapacity;
  }

  public void increaseProduction(int amount){
    productionCapacity += amount;
  }

}