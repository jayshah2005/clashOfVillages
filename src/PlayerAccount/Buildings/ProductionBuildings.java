package src.PlayerAccount.Buildings;

import src.PlayerAccount.Units.Gatherer;
import src.PlayerAccount.Units.Workers;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// production building is any building that gathers a resource over a period of time
public abstract class ProductionBuildings extends Building {

  protected int maxStorage;// max storage is the max amount of resources a building can output after a certain amount of time
  //protected int productionCapacity; // production capacity is the rate at which the resource is gathered
  protected List<Gatherer> workers;

  public java.time.LocalTime lastTimeCollected;  // lastTimeCollected is the most recent time a resource was gathered and is used to calculate how much will be aquired when the user goes to grab resources from the building again

  public ProductionBuildings(){
    this.lastTimeCollected = LocalTime.now();
    this.workers = new ArrayList<>();
  }

  public int collectResources(){

    LocalTime now = LocalTime.now();
    long timePassed = Duration.between(lastTimeCollected, now).toSeconds();

    int totalRate = 0;

    for(Gatherer g : workers){
      totalRate += g.getProductionCapacity();
    }

    // multiply time passed by production capacity
    int produced = (int)(timePassed * totalRate);

    // if produced > max storage just return max storage
    if(produced > maxStorage){
      produced = maxStorage;
    }

    resetStorage();

    return produced;
  }


  //public int getProductionCapacity() {
  //  return this.productionCapacity;
  //}

   // reset storage will set the local time to the current time so that the mine can start producing resources again
  public boolean resetStorage() {
    this.lastTimeCollected = LocalTime.now();
    return true;
  }

  public int getStorage() {
    return this.maxStorage;
  }

  public void addWorker(Gatherer worker){
    workers.add(worker);
  }

  public List<Gatherer> getWorkers(){
    return workers;
  }

}