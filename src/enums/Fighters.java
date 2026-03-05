package src.enums;

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

    public static Fighter flighterFromLabel(String label){
        switch (label){
            case "archers":
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
}