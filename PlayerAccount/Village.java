package PlayerAccount;

import java.time.LocalTime;
import java.util.Vector;

// village is the game area the player owns, it holds info relating to the players village such as their resources, remaining gaurd time, and defences
public abstract class Village {

  public LocalTime guardTime; // the time the a player is safe from attacks

  float defenceCapacity; // the defence score a players village has

  int maxBuildings; // the max limit of buildings a player can build

  Resources resources; // the amount of resources the player holds

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