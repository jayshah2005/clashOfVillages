package PlayerAccount.Buildings;

public abstract class Defence extends Building {

  private float damage;

  private float range;

  public float getDamage() {
    return this.damage;
  }

  public float getRange() {
    return this.range; 
  }

}