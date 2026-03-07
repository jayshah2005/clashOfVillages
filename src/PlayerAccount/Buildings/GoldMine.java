package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.Miner;

import static src.GameEngine.GOLD_MINE_COST;

// Goldmine is a type of mine with no additional behaviour needed
public class GoldMine extends Mines {
    public GoldMine(){
        this.maxWorkers = 3;
        this.maxStorage = 100;
        this.hitpoints = 250;

        // optional starting worker
        addWorker(new Miner());
    }

    public static final Resources cost = GOLD_MINE_COST;
}