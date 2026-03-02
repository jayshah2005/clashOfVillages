package src.PlayerAccount;

import src.GUI.GUI;
import src.GameEngine;
import src.enums.View;

//import java.io.Serial;
import java.io.Serializable;

// Player class holds all the actions a player can do as well as all the info about the player
public class Player implements Serializable {

    //@Serial
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

    public void showInputOptions(){
        gui.showInputOptions();
    }

    public String processInput(String inp){

        String out = null;

        switch (inp.toLowerCase().split(" ")[0]){
            case "shop":
                currentView = View.SHOP;
                break;
                // TODO: Change output if needed
            case "back":
                currentView = View.VILLAGE;
                break;
            case "build":
                // build inp.toLowerCase().split(" ")[1]
                break;

        }

        return out;
    }

    public String getInp(){
        return gui.getInp();
    }

    public String getName() {
        return name;
    }

    public View getCurrentView() {
        return currentView;
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
}