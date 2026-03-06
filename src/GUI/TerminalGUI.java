package src.GUI;

import src.GameEngine;
import src.PlayerAccount.Buildings.Building;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.Fighters;

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
                printShopView(p);
                break;
            case ATTACK:
                printAttackOptions(p);
            case TRAIN:
                printTrainOptions(p);
            case GATHER:
                printGatherResources(p);
                break;
        }
    }

    public String getInp() {
        String c =  scanner.next();
        return c;
    }

    private void printTrainOptions(Player p) {
        int temp = 1;
        StringBuilder output = new StringBuilder();
        char[] charArray;
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

        output.append("Your army: ");
        for(Fighters fighter : Fighters.values()){
            output.append(fighter.label).append(": ").append(p.fighters.get(fighter)).append(" | ");

        }

        output.setCharAt(output.length()-2, ' ');
        System.out.println(output);

        System.out.print("Resources: ");
        printResources(p);
    }

    private void printAttackOptions(Player p){
        System.out.println("Would you like to attack?(y/N)");
        System.out.println("To scout another village enter 'next'");
    }

    public void printVillageToBeAttack(Player defender){
        System.out.println(defender.getName());
        //defender.getVillage().getMap().printMap();
        printVillage(defender);

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
        printVillage(p);
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | train | gather || quit");
        printResources(p);
    }

    public void printShopView(Player p){
        System.out.println("--- SHOP ---");
        System.out.println("Resoures:");
        printResources(p);
        System.out.println("---------");

        System.out.println("1.Archer Tower");
        printResources(GameEngine.ARCHER_TOWER_COST);
        System.out.println("2.Cannon");
        printResources(GameEngine.CANNON_COST);
        System.out.println("3.Gold Mine");
        printResources(GameEngine.GOLD_MINE_COST);
        System.out.println("4.Iron Mine");
        printResources(GameEngine.IRON_MINE_COST);
        System.out.println("5.Lumber Mill");
        printResources(GameEngine.LUMBER_MILL_COST);

        System.out.println("---------");
        System.out.println("Type 'back' to return to your village");

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

    /**
     * print map displays the map grid and shows all of the buildings placed + their level
     */
    public void printVillage(Player p){
        Building[][] grid = p.getVillage().getMap().getGrid();
        int width = grid.length;
        int height = grid[0].length;

        System.out.println("--- MAP ---");
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if(grid[x][y] == null){
                    System.out.print("[   ]");

                } else {
                    Building b = grid[x][y];
                    String name = b.getClass().getSimpleName();
                    String code;

                    // switch case for building code
                    switch (name) {
                        case "VillageHall":
                            code = "VH";
                            break;
                        case "ArcherTower":
                            code = "AT";
                            break;
                        case "Cannon":
                            code = "CA";
                            break;
                        case "GoldMine":
                            code = "GM";
                            break;
                        case "IronMine":
                            code = "IM";
                            break;
                        case "LumberMill":
                            code = "LM";
                            break;
                        default:
                            code = "??";
                    }

                    System.out.print("[" + code + b.getLevel() + "]");
                }
            }
            System.out.println();
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

    public void displayError(String error){
        System.out.println(error);
        return;
    }

    public void printVillageHallPlacementMessage(){
        System.out.println("Welcome Chief! You must place your Village Hall to begin.");
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

    public static boolean promptAccountLoading() {
        Scanner scanner = new Scanner(System.in);
        char c;

        System.out.println("Would you like to load your account?(y/N)");
        do {
            c = scanner.next().charAt(0);
        } while (c != 'y' && c != 'N');

        if (c == 'N') {
            return false;
        }

        return true;
    }

    public void printGatherResources(Player p){

        Resources gathered = p.getVillage().gatherResources();

        System.out.println("Resources gathered:");
        System.out.println("Wood: " + gathered.getWood());
        System.out.println("Gold: " + gathered.getGold());
        System.out.println("Iron: " + gathered.getIron());

        System.out.println("Type: 'back' to return to village");

        //TODO: fix handling
        p.processInput("home");
    }


}
