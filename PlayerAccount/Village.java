package PlayerAccount;

import java.time.LocalTime;
import java.util.Vector;

public abstract class Village {

  public LocalTime guardTime;

  float defenceCapacity;

  int maxBuildings;

  Resources resources;

  /**
   * 
   * @element-type VillageObject
   */
  public Vector  myvillageObject;
  public Player Player;

  public float getDefenceCapacity() {
    return this.defenceCapacity;
  }

  public Resources getResources() {
    return this.resources;
  }

}