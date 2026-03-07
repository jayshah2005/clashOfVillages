package src.PlayerAccount.Units;

// Collectors are a Gatherers with no extra behaviour
public class Collector extends Gatherer {
    public Collector(){
        this.productionCapacity = 2;
        this.maxCapacity = 50;
    }
}