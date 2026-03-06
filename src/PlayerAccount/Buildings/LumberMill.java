package src.PlayerAccount.Buildings;

// LumberMill is a type of mine with no additional behaviour needed
public class LumberMill extends Mines {
    public LumberMill() {
        this.productionCapacity = 10;
        this.maxStorage = 200;
    }
}