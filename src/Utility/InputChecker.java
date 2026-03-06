package src.Utility;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.Fighters;

import java.io.IOException;
import java.util.Arrays;

import static src.GameEngine.*;

public class InputChecker {

    public InputChecker(){

    }

    public boolean isInputValid(String inp, Player player) throws IOException {

        switch (player.getCurrentView()){
            case VILLAGE:
                if(Arrays.asList(VILLAGE_OPTIONS).contains(inp)) return true;
            case SHOP:
                if(Arrays.asList(SHOP_OPTIONS).contains(inp)) return true;
                return true;
            case TRAIN:
                if(Arrays.asList(TRAIN_OPTIONS).contains(inp)) return true;
            case ATTACK:
                if(Arrays.asList(ATTACK_OPTIONS).contains(inp)) return true;
            default:
                throw new IOException("Input could not be verified becuase of incorrect player view.");
        }
    }

    public boolean isInputAllowed(String inp,  Player player) throws IOException {
        switch (player.getCurrentView()){
            case VILLAGE:
                return true;
            case SHOP:
                // TODO: Add shop logic here
                return true;
            case TRAIN:

                Resources cost = Fighters.getFighterCost(inp);

                if(cost == null) {
                    return inp.equals("back");
                }

                if(player.getVillage().getResources().compareTo(cost) > 0) return true;
                else return false;
            default:
                throw new IOException("Input could not be authorized because incorrect player view.");
        }
    }
}
