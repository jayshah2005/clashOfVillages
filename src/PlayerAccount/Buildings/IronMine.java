package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;

import static src.GameEngine.IRON_MINE_COST;

// Goldmine is a type of mine with no additional behaviour needed
public class IronMine extends Mines {
    public IronMine() {
        this.productionCapacity = 4;
        this.maxStorage = 150;
    }

    public static final Resources cost = IRON_MINE_COST;
}