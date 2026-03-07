package src.PlayerAccount;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

// Resources is a helper class defining the number of resources held
public class Resources implements Serializable, Comparable<Resources> {

    @Serial
    private final static long serialVersionUID = 1;

    int wood;
    int gold;
    int iron;

    public void setWood(int wood) {
        this.wood = wood;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public int getWood() {
        return wood;
    }

    public int getGold() {
        return gold;
    }

    public int getIron() {
        return iron;
    }

    public Resources() {
        this.wood = 0;
        this.gold = 0;
        this.iron = 0;
    }

    public Resources(int wood, int gold, int iron) {
        this.wood = wood;
        this.gold = gold;
        this.iron = iron;
    }

    /**
     * Resource store 3 ints. For every int the caller is more than or equal to result, result increases by 1 (-1 for less than) giving a range of -3..3 with the exclusion of 0.
     * @param o resource object we are comparing with
     * @return a number indicating how much bigger or smaller resource is compared to o overall
     */
    @Override
    public int compareTo(Resources o) {
        int result = 0;

        if(this.wood >= o.wood) {
            result += 1;
        } else result -= 1;

        if(this.gold >= o.gold) {
            result += 1;
        } else result -= 1;

        if(this.iron >= o.iron) {
            result += 1;
        } else result -= 1;

        return result;
    }

    /**
     * To subtract two resources (this - other)
     * @param other the resource subtrahend
     */
    public void subtract(Resources other) {
        this.wood -= other.wood;
        this.gold -= other.gold;
        this.iron -= other.iron;
    }

    public void add(Resources other) {
        this.wood += other.wood;
        this.gold += other.gold;
        this.iron += other.iron;
    }

    public void multiply(double multiplier) {
        this.wood = (int) (this.wood * multiplier);
        this.gold = (int) (this.gold * multiplier);
        this.iron = (int) (this.iron * multiplier);
    }

    public Resources clone(){
        return new Resources(this.wood, this.gold, this.iron);
    }
}