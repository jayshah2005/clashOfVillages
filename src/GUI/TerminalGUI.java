package src.GUI;

import src.GameEngine;
import src.PlayerAccount.Buildings.Building;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.VillageObject;
import src.enums.Fighters;
import src.enums.View;

import java.util.Scanner;

public class TerminalGUI implements GUIManager{

    Scanner scanner = new Scanner(System.in);

    TerminalGUI(){
        System.out.println("Welcome to Clash of Villages");
    }

    /**
     * Show input options based on player view
     * @param p the player who owns the gui
     */
    public void showInputOptions(Player p, View currentView) {
        switch (currentView){
            case VILLAGE:
                displayVillageView(p);
                break;
            case SHOP:
                displayShopOptions(p);
                break;
            case ATTACK:
                displayAttackOptions(p);
                break;
            case TRAIN:
                displayTrainOptions(p);
                break;
            case UPGRADE:
                displayUpgradeOptions(p);

        }
    }

    /**
     * display the upgrade view
     * @param p the player who owns the gui
     */
    public void displayUpgradeOptions(Player p) {
        System.out.println("=== Upgrade Menu ===");
        System.out.println("Select a building (1,2,3, etc.) to upgrade or type 'back' to return.\n");

        int i = 1;
        for(VillageObject obj : p.getVillage().getVillageObjects()){
            if(obj instanceof Building){
                Building b = (Building) obj;
                Resources cost = b.getUpgradeCost();
                System.out.println(i + ". " + b.getClass().getSimpleName() + " (Level " + b.getLevel() + ")" + " → Upgrade Cost: " + "Wood " + cost.getWood() + " | Gold " + cost.getGold() + " | Iron " + cost.getIron());
                i++;
            }
        }
    }

    /**
     * display the train view
     * @param p the player who owns the gui
     */
    private void displayTrainOptions(Player p) {
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
            displayResources(cost);
            temp++;
        }

        output.append("Your army: ");
        for(Fighters fighter : Fighters.values()){
            output.append(fighter.label).append(": ").append(p.fighters.get(fighter)).append(" | ");

        }

        output.setCharAt(output.length()-2, ' ');
        System.out.println(output);

        System.out.print("Resources: ");
        displayResources(p);
    }

    /**
     * display the attack view
     * @param p the player who owns the gui
     */
    private void displayAttackOptions(Player p){
        System.out.println("Would you like to attack?(y/N)");
        System.out.println("To scout another village enter 'next'");
    }

    /**
     * display the village view
     * @param p the player who owns the gui
     */
    private void displayVillageView(Player p){
        displayVillage(p);
        System.out.println("What would you like to do? Type one of the options");
        System.out.println("shop | upgrade | attack | train | gather || quit");
        displayResources(p);
    }

    /**
     * display the shop view
     * @param p the player who owns the gui
     */
    public void displayShopOptions(Player p){
        System.out.println("--- SHOP ---");
        System.out.println("Resources:");
        displayResources(p);
        System.out.println("---------");

        System.out.println("1.Archer Tower");
        displayResources(GameEngine.ARCHER_TOWER_COST);
        System.out.println("2.Cannon");
        displayResources(GameEngine.CANNON_COST);
        System.out.println("3.Gold Mine");
        displayResources(GameEngine.GOLD_MINE_COST);
        System.out.println("4.Iron Mine");
        displayResources(GameEngine.IRON_MINE_COST);
        System.out.println("5.Lumber Mill");
        displayResources(GameEngine.LUMBER_MILL_COST);
        System.out.println("6.Farm");
        displayResources(GameEngine.FARM_COST);

        System.out.println("---------");
        System.out.println("Type 'archertower, cannon, goldmine, ironmine, lumbermill, or farm'");
        System.out.println("Type 'back' to return to your village");

    }

    /**
     * display the village view
     * @param defender the player who is a potential target for attacking
     */
    public void displayVillageToBeAttack(Player defender){
        System.out.println(defender.getName());
        //defender.getVillage().getMap().displayMap();
        displayVillage(defender);

        System.out.println("Possible Loot:");

        Resources resources = defender.getVillage().getResources();
        int lootableWood = (int) (resources.getWood() * GameEngine.LOOT_RATIO);
        int lootableGold = (int) (resources.getGold() * GameEngine.LOOT_RATIO);
        int lootableIron = (int) (resources.getIron() * GameEngine.LOOT_RATIO);
        resources = new Resources(lootableWood, lootableGold, lootableIron);
        displayResources(resources);
    }

    /**
     * Get inp from player
     * @return inp
     */
    public String getInp() {
        String c =  scanner.next();
        return c;
    }

    /**
     * Display a message to the player
     * @param message the message to be displayed
     */
    public void displayMessage(String message){
        System.out.println(message);
    }

    @Override
    public void showAttackDefenceSuccessRates(float attackScore, float defenceScore, float successRate) {
        String output = "Attack Score: " + attackScore + " | Defence Score: " + defenceScore + " | Success Rate: " + successRate;
        System.out.println(output);;
    }

    /**
     * Display attack results based on attack success
     * @param outcome attack success
     * @param loot the loot won
     */
    @Override
    public void displayAttackResults(double outcome, Resources loot) {
        String out = "";

        if(outcome < 0.5){
            out += "It was a tough battle. We barely made it out alive.";
        } else if(outcome > 0.5 && outcome < 0.75){
            out += "It was a tough battle you did well.";
        } else if(outcome > 0.75 && outcome < 0.99){
            out += "Your amry dominated. The enimies struggled to keep up with your greatness.";
        } else{
            out += "It was a perfectly executed attack. Good job!";
        }

        System.out.println(out);
        System.out.println("Overall Score: " + outcome);
        System.out.print("You won: ");
        displayResources(loot);
    }

    /**
     * Prompt player to enter co-ordinates
     * @param message the message that will be asked before prompting
     * @return the co-ordinates
     */
    public int promptForCoordinate(String message) {

        int value;

        System.out.println(message);

        while (true) {
            try {
                value = Integer.parseInt(getInp());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number:");
            }
        }
    }

    /**
     * display map displays the map grid and shows all the buildings placed + their level
     */
    public void displayVillage(Player p){
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
                        case "Farm":
                            code = "FA";
                            break;
                        default:
                            code = "??";
                    }

                    System.out.print("[" + code + b.getLevel() + "]");
                }
            }
            System.out.println();
        }
        System.out.println("Population:" + p.getVillage().getPopulation() + "/" + p.getVillage().getMaxPopulation() + " villagers");
        System.out.println("Food production feeds: " + p.getVillage().getFeedPopulationSize() + " Villagers MAX");
    }

    /**
     * display resources player has
     * @param p player
     */
    private void displayResources(Player p){
        Resources r = p.village.getResources();
        System.out.println("Wood: " + r.getWood() + " | Gold: " + r.getGold() + " | Iron: " + r.getIron());
    }

    /**
     * display resources
     * @param r resource object to be displayed
     */
    private void displayResources(Resources r){
        System.out.println("Wood: " + r.getWood() + " | Gold: " + r.getGold() + " | Iron: " + r.getIron());
    }

    /**
     * Get name from user
     * @return user input
     */
    public String getName(){
        System.out.println("What is your name chief!?");
        return getInp();
    }

    /**
     * Display an error to a user
     * @param error error to be displayed
     */
    public void displayError(String error){
        System.out.println(error);
    }

    public void displayVillageHallPlacementMessage(){
        System.out.println("Welcome Chief! You must place your Village Hall to begin.");
    }

    /**
     * prompt the user to create an account
     * @return a boolean representing if the acc was created
     */
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

    /**
     * Prompt the user to load an account
     * @return a boolean representing if the acc was loaded
     */
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

}
