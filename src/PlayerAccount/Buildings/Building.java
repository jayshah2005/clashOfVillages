package src.PlayerAccount.Buildings;
import src.PlayerAccount.VillageObject;
import src.Utility.Position;

import java.io.Serializable;

// abstract class for all buildings (non units) 
public abstract class Building extends VillageObject implements Serializable {

  protected float hitpoints; // total health a building has

  private Position position; // position is the location of the village object on the map

  protected int level = 1;

  public void upgrade(){
    level++;
  }

  public int getLevel(){
    return level;
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

}