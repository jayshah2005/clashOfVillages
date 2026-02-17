package src.PlayerAccount.Buildings;

// Farm is any type of building that produces food
public class Farm extends ProductionBuildings {

  private int feedsPopulationSize; // the amount of food the farm produces

  public int getFeedsPopulationSize() {
    return this.feedsPopulationSize;
  }

}