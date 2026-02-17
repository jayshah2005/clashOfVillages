package src.GUI;

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

}
