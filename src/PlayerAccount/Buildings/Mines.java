package src.PlayerAccount.Buildings;

// Mines are a type of production building that will produce Ore 
public abstract class Mines extends ProductionBuildings {

  private int maxWorkers; // max worker is the number of workers associate with a mine

  public int getMaxWorkers() {
    return this.maxWorkers;
  }

}