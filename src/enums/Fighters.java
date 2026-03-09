package src.enums;

import src.PlayerAccount.Resources;
import src.PlayerAccount.Units.*;

import java.io.Serializable;

/**
 * Enum representing all fighter unit types in the game.
 * Each enum constant has a label and provides methods for attack score, object, and cost.
 */
public enum Fighters implements Serializable {
    ARCHER("archer"),
    CATAPULT("catapult"),
    KNIGHT("knight"),
    SOLDIER("soldier");

    public final String label;

    Fighters(String label){
        this.label = label;
    }

    /**
     * Returns the attack score for this fighter type.
     * @return float value representing attack score
     */
    public float getAttackScore(){
        return switch (this.label){
            case "archer" -> 1.5f;
            case "catapult" -> 2.5f;
            case "knight" -> 2;
            case "soldier" -> 1;
            default -> 0;
        };
    }

    /**
     * Returns a new instance of the corresponding Fighter object.
     * @return Fighter object for this enum constant
     */
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

    /**
     * Returns the cost of training this fighter type.
     * @return Resources object representing the cost
     */
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

    /**
     * Returns the cost of training a fighter by label.
     * @param label the string label for the fighter
     * @return Resources object representing the cost
     */
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