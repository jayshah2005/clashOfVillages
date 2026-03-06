package src.enums;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.*;

public enum Fighters {
    ARCHER("archer"),
    CATAPULT("catapult"),
    KNIGHT("knight"),
    SOLDIER("soldier");

    public final String label;

    Fighters(String label){
        this.label = label;
    }

    public Fighter getFighterObject(){
        switch (label){
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
        switch (label){
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

    public static Resources getFighterCost(String label){
        switch (label.toLowerCase()){
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