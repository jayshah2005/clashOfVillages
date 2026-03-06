package src.PlayerAccount.Buildings;

import java.time.LocalTime;

// production building is any building that gathers a resource over a period of time
public class ProductionBuildings extends Building {

  protected int maxStorage;// max storage is the max amount of resources a building can output after a certain amount of time
  protected int productionCapacity; // production capacity is the rate at which the resource is gathered

  public java.time.LocalTime lastTimeCollected;  // lastTimeCollected is the most recent time a resource was gathered and is used to calculate how much will be aquired when the user goes to grab resources from the building again

  public ProductionBuildings(){
    lastTimeCollected = LocalTime.now();
  }

  public int collectResources() {
    LocalTime now = LocalTime.now();

    long timePassed = java.time.Duration.between(lastTimeCollected, now).toSeconds();

    // multiply time passed by production capacity
    int produced = (int) timePassed * productionCapacity;

    // if produced > max storage just return max storage
    if(produced > maxStorage){
      produced = maxStorage;
    }

    resetStorage();

    return produced;
  }


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