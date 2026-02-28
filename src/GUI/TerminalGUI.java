package src.GUI;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.View;

import java.io.InputStream;
import java.util.Scanner;

public class TerminalGUI implements GUIManager{

    Scanner scanner = new Scanner(System.in);

    TerminalGUI(){
        System.out.println("Welcome to Clash of Villages");
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


    public String getInp() {
        String c =  scanner.next();
        return c;
    }

    public void showInputOptions(Player p) {

        switch (p.getCurrentView()){
            case View.VILLAGE:
                printVillageView(p);
                break;
            case View.SHOP:
                printShopView(p);
        }
    }

    private void printShopView(Player p) {
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("back | build [name of the building]");
        printResources(p);
    }

    private void printVillageView(Player p){
        System.out.println("Resouces:");
        printResources(p);
        // TODO: Print the village
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | gather || quit");
    }

    private void printResources(Player p){
        Resources r = p.village.getResources();
        System.out.println("Wood: " + r.getWood() + " | Gold: " + r.getGold() + " | Iron: " + r.getIron());
    }

    public String getName(){
        System.out.println("What is your name chief!?");
        return getInp();
    }
}