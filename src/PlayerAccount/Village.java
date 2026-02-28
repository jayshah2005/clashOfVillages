package src.PlayerAccount;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Vector;

// village is the game area the player owns, it holds info relating to the players village such as their resources, remaining gaurd time, and defences
public class Village implements Serializable {

    @Serial
    private final static long serialVersionUID = 1;

    public LocalTime guardTime; // the time the a player is safe from attacks
    float defenceCapacity; // the defence score a players village has
    int maxBuildings; // the max limit of buildings a player can build
    Resources resources; // the amount of resources the player holds

    Village(){
        guardTime = LocalTime.now();
        defenceCapacity = 0;
        maxBuildings = 0;
        resources = new Resources(100, 100, 100);
    }

    public float getDefenceCapacity() {
    return this.defenceCapacity;
    }

    public Resources getResources() {
    return this.resources;
  }

}