package src.Utility;

import src.PlayerAccount.Player;
import src.enums.Fighters;

import java.util.Arrays;

import static src.GameEngine.*;

public class InputChecker {

    public InputChecker(){

    }

    public boolean checkInput(String inp, Player player){

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
                // TODO: Throw an error saying player is in an invalid view that is not defined here
                return false;
        }
    }
}
