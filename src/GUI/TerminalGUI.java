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

        System.out.println("We could not find your account");
        System.out.println("Would you like to create a new account?(y/N)");

        do{
            c = scanner.next().charAt(0);
        }while(c != 'y' && c != 'N');

        if(c == 'N'){
            return false;
        }

        return true;
    }

    @Override
    public String getInp(View view) {
        printView(view);
        String c =  scanner.next();
        return c;
    }

    private void printView(View view){
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | gather || quit");
    }

}
