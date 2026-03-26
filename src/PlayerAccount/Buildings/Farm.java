package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.Workers;


import static src.GameEngine.FARM_COST;

// Farm is any type of building that produces food
public class Farm extends ProductionBuildings {

  public Farm(){
    this.hitpoints = 30;
    this.feedsPopulationSize = 10;
  }

  @Override
  protected void applyUpgradeEffects() {
    feedsPopulationSize += 5;
    hitpoints += 10;
  }

  public static final Resources cost = FARM_COST;

  private int feedsPopulationSize; // the amount of food the farm produces

  public int getFeedsPopulationSize() {
    return this.feedsPopulationSize;
  }

}