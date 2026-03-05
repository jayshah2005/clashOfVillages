package src.PlayerAccount.Units;
import src.PlayerAccount.Attacking;
import src.PlayerAccount.Resources;


// fighters are any units in the village capable of attacking
public abstract class Fighter extends Villager implements Attacking {

    private float damage; // the amount of damage a fighter can do
    private float range; // the distance a fighter can do damage at
    private float hitpoints; // the amount of health a fighter has

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