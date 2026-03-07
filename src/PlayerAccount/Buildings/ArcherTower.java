package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;

import static src.GameEngine.ARCHER_TOWER_COST;


// ArcherTower is a type of Attack building with no additional behaviour needed
public class ArcherTower extends Defence {
     public ArcherTower(){
          this.hitpoints = 300;
          this.damage = 15;
          this.range = 10;
     }

     public static final Resources cost = ARCHER_TOWER_COST;
}