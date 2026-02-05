package PlayerAccount.Buildings;

import PlayerAccount.Attacking;

public abstract class Defence extends Building implements Attacking{

  private float damage;

  private float range;

  public float getDamage() {
    return this.damage;
  }

  public float getRange() {
    return this.range; 
  }

}