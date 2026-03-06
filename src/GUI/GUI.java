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

    public String getName(){
        return guiManager.getName();
    }

    public static Player selectPlayer(List<Player> players){

        Scanner scanner = new Scanner(System.in);
        String inp;
        char c;

        System.out.println("Select a player from the list to load the game or enter 'back' to go back:");

        do{
            for(Player p : players) {
                System.out.println(p.getName());
            }

            inp = scanner.nextLine();

            for(Player p : players) {
                if(p.getName().equals(inp)){
                    return p;
                }
            }

            if(inp.equals("back")){
                return null;
            }

            System.out.println("Please select a player from the list to load the game or enter 'back' to create a new account:");
        }while (true);

    }

    public void printVillageHallPlacementMessage(){guiManager.printVillageHallPlacementMessage();}

    public void printVillage(Player p){guiManager.printVillage(p);}

    public int promptForCoordinate(String message) {
        return guiManager.promptForCoordinate(message);
    }

    public void showInputOptions(Player player) {
        guiManager.showInputOptions(player);
    }

    public void printVillageForAttack(Player defender) {
        guiManager.printVillageToBeAttack(defender);
    }

    public void displayError(String error){guiManager.displayError(error);}

}