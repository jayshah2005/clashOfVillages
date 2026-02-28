package src.GUI;

import src.PlayerAccount.Player;
import src.enums.View;

public class GUI {

    Player owner;
    GUIManager guiManager;


    public GUI(Player p){
        this.owner = p;
        this.guiManager = new TerminalGUI();
    }

    public String getInp(View currentView){
        return guiManager.getInp(currentView);
    }

}