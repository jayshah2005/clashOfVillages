package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.Miner;

import static src.GameEngine.IRON_MINE_COST;

// Goldmine is a type of mine with no additional behaviour needed
public class IronMine extends Mines {
    public IronMine(){
        this.maxWorkers = 5;
        this.maxStorage = 200;
        this.hitpoints = 30;

        // optional starting worker
        addWorker(new Miner());
    }

    public static final Resources cost = IRON_MINE_COST;
}