package src.GUI;

import src.enums.View;

public interface GUIManager {

    public static boolean promptAccountCreation() {
        return true;
    }

    String getInp(View view);
    String getInp();

    String getName();

}