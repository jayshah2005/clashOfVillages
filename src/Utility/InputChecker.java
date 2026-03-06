package src.Utility;

import src.PlayerAccount.Player;
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
                throw new IOException("Input could not be verified.");
                return false;
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

            default:
                // TODO: Throw an error saying player is in an invalid view that is not defined here
                throw new IOException("Input could not be authorized.");
        }
    }
}
