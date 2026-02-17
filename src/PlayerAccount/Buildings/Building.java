package src.PlayerAccount.Buildings;
import src.PlayerAccount.VillageObject;

// abstract class for all buildings (non units) 
public abstract class Building extends VillageObject {

  private float hitpoints; // total health a building has

  public float getHitpoints() { 
    return this.hitpoints;
  }

}