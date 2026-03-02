package src.GUI;

import src.PlayerAccount.Player;
import java.util.List;
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

    public void showInputOptions(){
        guiManager.showInputOptions(this.owner);
    }

    public String getName(){
        return guiManager.getName();
    }

    public static Player selectPlayer(List<Player> players){

        Scanner scanner = new Scanner(System.in);
        String inp;
        char c;


        System.out.println("Select a player from the list to load the game or enter \"back\" to create a new player:");

        do{
            for(Player p : players) {
                System.out.println(p.getName());
            }

            inp = scanner.nextLine();

            if(inp.equals("back")){
                return null;
            }

            for(Player p : players) {
                if(p.getName().equals(inp)){
                    return p;
                }
            }

            System.out.println("Please select a player from the list to load the game:");
        }while (true);



    }

    public int promptShopSelection(){
        return guiManager.promptShopSelection();
    }

    public void showMap(){
        owner.getVillage().getMap().printMap();
    }


    public int promptForCoordinate(String message) {
        return guiManager.promptForCoordinate(message);
    }
}