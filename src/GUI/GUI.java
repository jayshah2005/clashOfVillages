package src.GUI;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.View;

import java.util.List;
import java.util.Scanner;


/**
 * Acts as an interface between gui manager and player.
 */
public class GUI {

    Player owner;
    GUIManager guiManager;
    public View currentView;

    public GUI(Player p){
        this.owner = p;
        this.guiManager = new TerminalGUI();
        currentView = View.VILLAGE;
    }

    /**
     * Select player to load
     * @param players a list of available players to load
     * @return a player that user selected
     */
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

    public String getInp(){
        return guiManager.getInp();
    }

    public String getName(){
        return guiManager.getName();
    }

    public void printVillageHallPlacementMessage(){guiManager.displayVillageHallPlacementMessage();}

    public void showInputOptions() {
        guiManager.showInputOptions(owner, currentView);
    }

    public void showAttackDefenceSuccessRates(float attackScore, float defenceScore,  float successRate) {
        guiManager.showAttackDefenceSuccessRates(attackScore, defenceScore, successRate * 100);
    }

    public void printVillageForAttack(Player defender) {
        guiManager.displayVillageToBeAttack(defender);
    }

    public void displayMessage(String message){guiManager.displayMessage(message);}

    public void displayError(String error){guiManager.displayError(error);}

    public void displayAttackResults(double outcome, Resources loot){guiManager.displayAttackResults(outcome, loot);}
}