package src.PlayerAccount.Buildings;

import src.PlayerAccount.Attacking;

// Defence is an abstract class for any building capable of attacking
public abstract class Defence extends Building implements Attacking{

  private float damage; // the number of damage a building can output

  private float range; // the distance a building can output that damage

  public float getDamage() {
    return this.damage;
  }

  public float getRange() {
    return this.range; 
  }

}