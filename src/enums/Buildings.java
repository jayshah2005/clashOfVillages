package src.enums;

import src.PlayerAccount.Buildings.*;
import src.PlayerAccount.Resources;

import java.io.Serializable;

/**
 * Enum representing all building types in the game.
 * Each enum constant has a label and provides methods to get the building object and its cost.
 */
public enum Buildings implements Serializable {
    FARM("farm"),
    GOLDMINE("goldmine"),
    IRONMINE("ironmine"),
    LUMBERMILL("lumbermill"),
    VILLAGEHALL("villagehall"),
    ARCHERTOWER("archertower"),
    CANNON("cannon");


    public final String label;

    Buildings(String label){
        this.label = label;
    }

    /**
     * Returns a new instance of the corresponding Building object.
     * @return Building object for this enum constant
     */
    public Building getBuildingObject(){
        switch(label){
            case "goldmine":
                return new GoldMine();
            case "ironmine":
                return new IronMine();
            case "lumbermill":
                return new LumberMill();
            case "villagehall":
                return new VillageHall();
            case "archertower":
                return new ArcherTower();
            case "cannon":
                return new Cannon();
            case "farm":
                return new Farm();
            default:
                return null;
        }
    }

    /**
     * Returns the cost of constructing this building type.
     * @return Resources object representing the cost
     */
    public Resources getBuildingCost(){
        switch(label){
            case "goldmine":
                return GoldMine.cost;
            case "ironmine":
                return IronMine.cost;
            case "lumbermill":
                return LumberMill.cost;
            case "archertower":
                return ArcherTower.cost;
            case "cannon":
                return Cannon.cost;
            case "farm":
                return Farm.cost;
            default:
                return null;
        }
    }
}

