package src.PlayerAccount.Buildings;
import src.PlayerAccount.Resources;
import src.PlayerAccount.VillageObject;
import src.Utility.Position;

import java.io.Serializable;

// abstract class for all buildings (non units) 
public abstract class Building extends VillageObject implements Serializable {

  protected float hitpoints; // total health a building has

  private Position position; // position is the location of the village object on the map

  protected int level = 1;
  protected int maxLevel = 10;

  public boolean upgrade(){

    if(level >= maxLevel){
      return false;
    }

    level++;
    applyUpgradeEffects();

    return true;
  }

  public int getLevel(){
    return level;
  }

  public Resources getUpgradeCost(){

    Resources baseCost = getProductionCost();

    Resources upgradeCost = baseCost.clone();

    upgradeCost.multiply(level + 1);

    return upgradeCost;
  }

  /**
   * sets the village objects position to the inputted position
   * @param position
   * @return
   */
  public void setPosition(Position position) {
    this.position =  position;
  }

  public Position getPostition(Position position){
    return this.position;
  }

  public float getHitpoints() { 
    return this.hitpoints;
  }

  protected void applyUpgradeEffects() {}


}