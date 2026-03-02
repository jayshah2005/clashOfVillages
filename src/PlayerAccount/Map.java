package src.PlayerAccount;

import src.Utility.Region;
import java.util.*;
import src.PlayerAccount.Buildings.Building;
import src.Utility.Position;

public class Map {
    private Building[][] grid; // stores the buildings on the map grid
    //private List<Region> regions; // stores the boundaries
    private int width; // size of the map
    private int height; // size of the map

    public Map(int width, int height){
        this.grid = new Building[width][height];
        //this.regions = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    /**
     * checks if the object being placed is within the regions boundaries
     * check if the x y coordinates fall within the regions height and width
     * @param x
     * @param y
     * @return
     */
    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * checks if the building can be placed (or if the tile is already occupied)
     * @param pos
     * @return
     */
    public boolean canPlace(Position pos){
        int x = pos.getX();
        int y = pos.getY();

        // check if coordinates entered are in bounds
        if (!inBounds(x,y)) {
            return false;
        }

        // check if position (x,y) is occupied
        return grid[x][y] == null;
    }

    /**
     * place the given building on the grid at the specified tile
     * @param building
     * @param pos
     * @return
     */
    public boolean placeBuilding(Building building, Position pos){
        if (!canPlace(pos)){
            return false;
        }

        // place the building at the coordinates provided
        grid[pos.getX()][pos.getY()] = building;
        building.setPosition(pos);
        return true;
    }

    /**
     * Getter method to return the village object at that position on the grid
     * @param pos
     * @return
     */
    public VillageObject getObjectat(Position pos){
        if (inBounds(pos.getY(), pos.getX())){ // check if its a valid position
            return grid[pos.getX()][pos.getY()]; // return the village object
        }
        return null;
    }

    /**
     * print map displays the map grid and shows all of the buildings placed + their level
     */
    public void printMap(){
        System.out.println("--- MAP ---");
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if(grid[x][y] == null){
                    System.out.print("[   ]");

                } else {
                    Building b = grid[x][y];
                    String name = b.getClass().getSimpleName();
                    String code;

                    // switch case for building code
                    switch (name) {
                        case "TownHall":
                            code = "TH";
                            break;
                        case "ArcherTower":
                            code = "AT";
                            break;
                        case "Cannon":
                            code = "Ca ";
                            break;
                        case "GoldMine":
                            code = "GM";
                            break;
                        case "IronMine":
                            code = "IM";
                            break;
                        case "LumberMill":
                            code = "LM";
                            break;
                        default:
                            code = "??";
                    }

                    System.out.print("[" + code + b.getLevel() + "]");
                }
            }
            System.out.println();
        }
    }

}
