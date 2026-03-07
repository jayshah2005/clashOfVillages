package src.enums;

import src.PlayerAccount.Buildings.*;
import src.PlayerAccount.Resources;

import java.io.Serializable;

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

    public Building getBuildingObject(){
        switch(label){
            case "farm":
                return new Farm();
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
            default:
                return null;
        }
    }

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
            default:
                return null;
        }
    }
}

