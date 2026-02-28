package src.GUI;

import src.enums.View;

public interface GUIManager {

    public static boolean promptAccountCreation() {
        return true;
    }

    void showInputOptions(View view);
    String getInp();

    String getName();

}