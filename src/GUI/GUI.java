package src.GUI;

import src.PlayerAccount.Player;

public class GUI {

    Player owner;
    GUIManager guiManager;

    public GUI(Player p){
        this.owner = p;
        this.guiManager = new TerminalGUI();
    }
}