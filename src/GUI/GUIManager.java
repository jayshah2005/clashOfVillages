package src.GUI;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;

public interface GUIManager {

    public static boolean promptAccountCreation() {
        return true;
    }

    String getInp();

    String getName();

    int promptForCoordinate(String message);

    void showInputOptions(Player view);

    void printShopOptions(Player p);

    void printVillageToBeAttack(Player defender);

    void printVillage(Player p);

    void printVillageHallPlacementMessage();

    void displayError(String message);

    void displayMessage(String message);

    void printUpgradeOptions(Player p);

    void showAttackDefenceSuccessRates(float attackScore, float defenceScore,  float successRate);

    void displayAttackResults(double outcome, Resources loot);
}