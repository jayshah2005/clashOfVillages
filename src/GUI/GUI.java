package src.GUI;

import src.PlayerAccount.Player;
import src.enums.View;

import java.util.ArrayList;
import java.util.Scanner;

public class GUI {

    Player owner;
    GUIManager guiManager;


    public GUI(Player p){
        this.owner = p;
        this.guiManager = new TerminalGUI();
    }

    public String getInp(){
        return guiManager.getInp();
    }

    public void showInputOptions(View view){
        guiManager.showInputOptions(view);
    }

    public String getName(){
        return guiManager.getName();
    }

    public static Player selectPlayer(ArrayList<Player> players){

        Scanner scanner = new Scanner(System.in);
        String inp;
        char c;


        System.out.println("Select a player from the list to load the game:");

        for(Player p : players) {
            System.out.println(p.getName());
        }

        do{
            inp = scanner.nextLine();

            for(Player p : players) {
                if(p.getName().equals(inp)){
                    return p;
                }
            }

            System.out.println("Please select a player from the list to load the game:");
        }while (true);



    }

}