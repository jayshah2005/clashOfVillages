package PlayerAccount.Buildings;

import java.time.LocalTime;
import PlayerAccount.Buildings.Building;

// production building is any building that gathers a resource over a period of time
public class ProductionBuildings extends Building {

  private int productionCapacity; // production capacity is the rate at which the resource is gathered

  public java.time.LocalTime lastTimeCollected;  // lastTimeCollected is the most recent time a resource was gathered and is used to calculate how much will be aquired when the user goes to grab resources from the building again

  public int maxStorage; // max storage is the max amount of resources a building can output after a certain amount of time

  public int getProductionCapacity() { 
    return this.productionCapacity;
  }

   // reset storage will set the local time to the current time so that the mine can start producing resources again
  public boolean resetStorage() {
    this.lastTimeCollected = LocalTime.now();
    return true;
  }

  public int getStorage() {
    return this.maxStorage;
  }

}