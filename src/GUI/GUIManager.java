package src.GUI;

import src.PlayerAccount.Player;
import src.enums.View;

public interface GUIManager {

    public static boolean promptAccountCreation() {
        return true;
    }

    void showInputOptions(Player player);
    String getInp();
    String getName();

    int promptShopSelection();

    int promptForCoordinate(String message);
}