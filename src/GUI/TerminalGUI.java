package src.GUI;

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

    public void showInputOptions(View view) {
        if(view == View.VILLAGE){
            printVillageView();
        }
    }

    private void printVillageView(){
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | gather || quit");
    }

    public String getName(){
        System.out.println("What is your name chief!?");
        return getInp();
    }

}
