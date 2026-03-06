package src.PlayerAccount.Buildings;

// Goldmine is a type of mine with no additional behaviour needed
public class GoldMine extends Mines {
    public GoldMine() {
        this.productionCapacity = 2;
        this.maxStorage = 100;
    }
}