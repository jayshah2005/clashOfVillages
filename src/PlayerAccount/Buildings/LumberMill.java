package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;

import static src.GameEngine.LUMBER_MILL_COST;

// LumberMill is a type of mine with no additional behaviour needed
public class LumberMill extends Mines {
    public LumberMill() {
        this.productionCapacity = 10;
        this.maxStorage = 200;
    }

    public static final Resources cost = LUMBER_MILL_COST;
}