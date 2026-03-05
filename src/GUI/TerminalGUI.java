package src.GUI;

import src.GameEngine;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.Fighters;
import src.enums.View;

import java.util.List;
import java.util.Scanner;

public class TerminalGUI implements GUIManager{

    Scanner scanner = new Scanner(System.in);

    TerminalGUI(){
        System.out.println("Welcome to Clash of Villages");
    }


    public void showInputOptions(Player p) {
        switch (p.getCurrentView()){
            case VILLAGE:
                printVillageView(p);
                break;
            case SHOP:
                printShopView();
                break;
            case ATTACK:
                printAttackOptions(p);
            case TRAIN:
                printTrainOptions(p);
        }
    }

    public String getInp() {
        String c =  scanner.next();
        return c;
    }

    private void printTrainOptions(Player p) {
        int temp = 1;
        Resources cost;

        System.out.println("What would you like to train? Enter the name of the troop.");
        System.out.println("You can go back to main screen by entering 'back'.");

        for(Fighters fighter : Fighters.values()){
            cost = fighter.getFighterCost();

            System.out.print(temp + ". " + fighter.label);
            System.out.print(" --> Cost: ");
            printResources(cost);
            temp++;
        }
    }

    private void printAttackOptions(Player p){
        System.out.println("Would you like to attack?(y/N)");
        System.out.println("To scout another village enter 'next'");
    }

    public void printVillageToBeAttack(Player defender){
        System.out.println(defender.getName());
        defender.getVillage().getMap().printMap();

        System.out.println("Possible Loot:");

        Resources resources = defender.getVillage().getResources();
        int lootableWood = (int) (resources.getWood() * GameEngine.LOOT_RATIO);
        int lootableGold = (int) (resources.getGold() * GameEngine.LOOT_RATIO);
        int lootableIron = (int) (resources.getIron() * GameEngine.LOOT_RATIO);
        resources = new Resources(lootableWood, lootableGold, lootableIron);
        printResources(resources);

        System.out.println("Village Defence Capacity: " + defender.getVillage().getDefenceCapacity());
    }

    private void printVillageView(Player p){
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | train | gather || quit");
        printResources(p);
    }

    public int printShopView(){
        System.out.println("--- SHOP ---");
        System.out.println("1. Archer tower");
        System.out.println("2. Cannon");
        System.out.println("3. Gold Mine");
        System.out.println("4. Iron Mine");
        System.out.println("5. Lumber Mill");
        System.out.println("Enter the number of the building you would like to buy.");
        // TODO: Use printResources to show the resources the player has. Player should be a parameter here for consistency.

        int choice;

        do{
            choice = Integer.parseInt(scanner.next());
        } while (choice < 1 || choice > 5);

        return choice;
    }

    public int promptForCoordinate(String message) {

        int value;

        System.out.println(message);

        while (true) {
            try {
                value = Integer.parseInt(scanner.next());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number:");
            }
        }
    }

    private void printResources(Player p){
        Resources r = p.village.getResources();
        System.out.println("Wood: " + r.getWood() + " | Gold: " + r.getGold() + " | Iron: " + r.getIron());
    }

    private void printResources(Resources r){
        System.out.println("Wood: " + r.getWood() + " | Gold: " + r.getGold() + " | Iron: " + r.getIron());
    }

    public String getName(){
        System.out.println("What is your name chief!?");
        return getInp();
    }

    public static boolean promptAccountCreation(){

        Scanner scanner = new Scanner(System.in);
        char c;

        System.out.println("Would you like to create a new account?(y/N)");

        do{
            c = scanner.next().charAt(0);
        }while(c != 'y' && c != 'N');

        if(c == 'N'){
            return false;
        }

        return true;
    }

    public static boolean promptAccountLoading(){
        Scanner scanner = new Scanner(System.in);
        char c;

        System.out.println("Would you like to load your account?(y/N)");
        do{
            c = scanner.next().charAt(0);
        }while(c != 'y' && c != 'N');

        if(c == 'N'){
            return false;
        }

        return true;
    }


}
