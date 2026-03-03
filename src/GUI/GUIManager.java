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

    int printShopView();
}