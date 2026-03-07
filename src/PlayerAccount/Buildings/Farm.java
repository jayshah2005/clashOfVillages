package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;


import static src.GameEngine.FARM_COST;

// Farm is any type of building that produces food
public class Farm extends ProductionBuildings {

  public Farm(){
    this.hitpoints = 100;
  }

  public static final Resources cost = FARM_COST;

  private int feedsPopulationSize; // the amount of food the farm produces

  public int getFeedsPopulationSize() {
    return this.feedsPopulationSize;
  }

}