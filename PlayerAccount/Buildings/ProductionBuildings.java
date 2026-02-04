package PlayerAccount.Buildings;

import java.time.LocalTime;
import PlayerAccount.Buildings.Building;

public class ProductionBuildings extends Building {

  private int productionCapacity;

  public java.time.LocalTime lastTimeCollected;

  public int maxStorage;

  public int getProductionCapacity() {
    return this.productionCapacity;
  }

  public boolean resetStorage() {
    this.lastTimeCollected = LocalTime.now();
    return true;
  }

  public int getStorage() {
    return this.maxStorage;
  }

}