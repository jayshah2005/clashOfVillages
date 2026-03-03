package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
import src.enums.View;

//import java.io.Serial;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Player class holds all the actions a player can do as well as all the info about the player
public class Player implements Serializable {

    @Serial
    private final static long serialVersionUID = 1;

    public Village village; // the village the player owns
    public String name;
    public transient GUI gui; // the gui the player interacts with
    public transient GameEngine gameEngine;
    transient View currentView = View.VILLAGE;

    public Player(GameEngine gameEngine) {
        gui = new GUI(this);
        name = gui.getName();
        village = new Village();
        this.gameEngine = gameEngine;
    }

    public boolean build(VillageObject obj) { // players can build a specified village object
        return false;
    }

    public boolean upgrade(VillageObject obj) { // players can upgrade a specified object
        return false;
    }

    public Village findVillageToAttack() { // players can continue to find a village to attack until they find one they want to attack
        return null;
    }

    public float attack() { // players can then determine they want to attack the found village
        return 0.0f;
    }

      public String getInp(){
            return gui.getInp();
      }

    public String getName() {
        return name;
    }

    public void reload(GameEngine gameEngine) {
        this.gui = new GUI(this);
        this.gameEngine = gameEngine;
        this.currentView = View.VILLAGE;
    }

    public Village getVillage() {
        return village;
    }


    public GUI getGUI() {
        return gui;
    }

    public void showInputOptions() {
        gui.showInputOptions(this);
    }

    public List<?> processInput(String inp) {

        switch (inp) {
            case "shop":
                this.currentView = View.SHOP;
                return new ArrayList<String>();
            case "attack":
                this.currentView = View.ATTACK;
                return this.gameEngine.findRandomPlayersToAttack();
            default:
                return Collections.singletonList("Please enter a proper input");
        }
    }

    public View getCurrentView() {
        return currentView;
    }
}