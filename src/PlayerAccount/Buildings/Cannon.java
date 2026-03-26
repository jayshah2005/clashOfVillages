package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;

import static src.GameEngine.CANNON_COST;

// Cannon is a type of Attack building with no additional behaviour needed
public class Cannon extends Defence {

    public Cannon(){
        this.hitpoints = 75;
        this.damage = 20;
        this.range = 3;
    }

    public static final Resources cost = CANNON_COST;
}