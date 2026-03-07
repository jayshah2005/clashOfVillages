package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;

import static src.GameEngine.GOLD_MINE_COST;

// Goldmine is a type of mine with no additional behaviour needed
public class GoldMine extends Mines {
    public GoldMine() {
        this.productionCapacity = 2;
        this.maxStorage = 100;
    }

    public static final Resources cost = GOLD_MINE_COST;
}