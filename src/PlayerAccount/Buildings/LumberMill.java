package src.PlayerAccount.Buildings;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.Collector;

import static src.GameEngine.LUMBER_MILL_COST;

// LumberMill is a type of mine with no additional behaviour needed
public class LumberMill extends Mines {
    public LumberMill(){
        this.maxWorkers = 10;
        this.maxStorage = 200;
        this.hitpoints = 150;

        // add starting worker
        addWorker(new Collector());
    }

    public static final Resources cost = LUMBER_MILL_COST;
}