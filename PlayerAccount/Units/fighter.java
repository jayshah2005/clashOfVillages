package PlayerAccount.Units;

import PlayerAccount.Attacking;

public abstract class Fighter extends Villager implements Attacking {

  private float damage;

  private float range;

  private float hitpoints;

  public float getDamage() {
    return this.damage;
  }

  public float getRange() {
    return this.range;
  }

  public float getHitpoints() {
    return this.hitpoints;
  }

}