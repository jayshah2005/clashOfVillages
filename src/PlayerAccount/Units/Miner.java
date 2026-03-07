package src.PlayerAccount.Units;

// Miners are a type of gather that works in a mine
public class Miner extends Gatherer {
    public Miner(){
        this.productionCapacity = 2;
        this.maxCapacity = 50;
    }
}