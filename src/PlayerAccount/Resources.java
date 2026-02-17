package src.PlayerAccount;

// Resources is a helper class defining the number of resources held
public class Resources {

    int wood;
    int gold;
    int iron;

    Resources() {
        this.wood = 0;
        this.gold = 0;
        this.iron = 0;
    }

    Resources(int wood, int gold, int iron) {
        this.wood = wood;
        this.gold = gold;
        this.iron = iron;
    }
}