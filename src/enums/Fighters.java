package src.enums;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.*;

public enum Fighters {
    ARCHER("Archer"),
    CATAPULT("Catapult"),
    KNIGHT("Knight"),
    SOLDIER("Soldier");

    public final String label;

    Fighters(String label){
        this.label = label;
    }

    public Fighter flighterFromLabel(){
        switch (this.label.toLowerCase()){
            case "archer":
                return new Archer();
            case "catapult":
                return new Catapult();
            case "knight":
                return new Knight();
            case "soldier":
                return new Soldiers();
            default:
                return null;
        }
    }

    public Resources getFighterCost(){
        switch (this.label.toLowerCase()){
            case "archer":
                return Archer.cost;
            case "catapult":
                return Catapult.cost;
            case "knight":
                return Knight.cost;
            case "soldier":
                return Soldiers.cost;
            default:
                return null;
        }
    }
}