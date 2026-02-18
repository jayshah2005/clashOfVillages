package src.PlayerAccount;

import src.Utility.Region;
import java.util.*;

public class Map {
    private List<Region> regions;

    public Map(){
        this.regions = new ArrayList<>();
    }

    public List<Region> getRegions(){
        return regions;
    }
}
