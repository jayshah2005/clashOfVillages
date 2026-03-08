package src.PlayerAccount.Buildings;

import src.PlayerAccount.Units.Gatherer;
import src.PlayerAccount.Units.Workers;

// Mines are a type of production building that will produce Ore
public abstract class Mines extends ProductionBuildings {
  protected int maxWorkers; // max worker is the number of workers associate with a mine

  public void addWorker(Gatherer worker){
    workers.add(worker);
  }

  public int getMaxWorkers() {
    return this.maxWorkers;
  }
  @Override
  protected void applyUpgradeEffects() {
    maxStorage += 50;
    hitpoints += 50;

    for(Gatherer g : workers){
      g.increaseProduction(1);
    }
  }

}