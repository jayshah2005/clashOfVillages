package src.GUI;

import src.enums.View;

import java.io.InputStream;
import java.util.Scanner;
import src.PlayerAccount.Map;

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

    public String getInp(View view) {
        printView(view);
        return getInp();
    }

    public String getInp() {
        String c =  scanner.next();
        return c;
    }

    private void printView(View view){
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | gather | map || quit");
    }

    public int promptShopSelection(){
        System.out.println("--- SHOP ---");
        System.out.println("1. Archer tower");
        System.out.println("2. Cannon");
        System.out.println("3. Gold Mine");
        System.out.println("4. Iron Mine");
        System.out.println("5. Lumber Mill");
        System.out.println("Enter the number of the building you would like to buy.");

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

    public String getName(){
        System.out.println("What is your name chief!?");
        return getInp();
    }


}
