package PlayerAccount.Units;

public abstract class Fighter extends Villager {

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