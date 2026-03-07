package src.GUI;

import src.PlayerAccount.Player;
import src.enums.View;

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

    void showAttackDefenceSuccessRates(float attackScore, float defenceScore,  float successRate);
}