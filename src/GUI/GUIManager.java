package src.GUI;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.View;

public interface GUIManager {

    public static boolean promptAccountCreation() {
        return true;
    }

    String getInp();

    String getName();

    int promptForCoordinate(String message);

    void showInputOptions(Player p, View currentView);

    void displayShopOptions(Player p);

    void displayVillageToBeAttack(Player defender);

    void displayVillage(Player p);

    void displayVillageHallPlacementMessage();

    void displayError(String message);

    void displayMessage(String message);

    void displayUpgradeOptions(Player p);

    void showAttackDefenceSuccessRates(float attackScore, float defenceScore,  float successRate);

    void displayAttackResults(double outcome, Resources loot);
}