package src.Utility;

import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.enums.Buildings;
import src.enums.Fighters;
import src.enums.View;
import src.exceptions.InvalidInputException;
import src.exceptions.NotEnoughResourcesException;

import java.io.IOException;
import java.util.Arrays;

import static src.GameEngine.*;

public class InputChecker {

    public InputChecker(){

    }

    /**
     * Check if an input is valid based on player's view
     * @param inp inp to verify
     * @param cureentView the current view of the player
     * @return a boolean value indicating the validity of input
     * @throws IOException thrown if input cannot be authorized
     */
    public boolean isInputValid(String inp, View cureentView) throws IOException {

        switch (cureentView){
            case VILLAGE:
                return Arrays.asList(VILLAGE_OPTIONS).contains(inp);
            case SHOP:
                return Arrays.asList(SHOP_OPTIONS).contains(inp);
            case TRAIN:
                return Arrays.asList(TRAIN_OPTIONS).contains(inp);
            case ATTACK:
                return Arrays.asList(ATTACK_OPTIONS).contains(inp);
            case TEST:
                return Arrays.asList(TEST_OPTIONS).contains(inp);
            case UPGRADE:
                if(Arrays.asList(UPGRADE_OPTIONS).contains(inp))
                    return true;

                try{
                    Integer.parseInt(inp);
                    return true;
                }
                catch(NumberFormatException e){
                    return false;
                }
            default:
                throw new IOException("Input could not be verified becuase of incorrect player view.");
        }
    }

    public boolean isInputAllowed(String inp,  View currentView, Player player) throws IOException, InvalidInputException, NotEnoughResourcesException {
        switch (currentView){
            case VILLAGE:
                return true;
            case SHOP:
                if(inp.equals("back")){
                    return true;
                }

                Buildings building;

                try{
                    building = Buildings.valueOf(inp.toUpperCase());
                }
                catch(IllegalArgumentException e){
                    throw new InvalidInputException("Invalid building selection.");
                }

                Resources price = building.getBuildingCost();

                if(player.getVillage().getResources().compareTo(price) >= 0){
                    return true;
                }

                throw new NotEnoughResourcesException("Not enough resources.");
            case TRAIN:

                Resources cost = Fighters.getFighterCost(inp);

                if(cost == null) {
                    return inp.equals("back");
                }

                if(player.getVillage().getResources().compareTo(cost) > 0) return true;
                else return false;

            case TEST:
                return true;

            case UPGRADE:
                if(inp.equals("back")) return true;

                try{
                    Integer.parseInt(inp);
                    return true;
                }catch(NumberFormatException e){
                    throw new InvalidInputException("Invalid upgrade selection.");
                }

            default:
                throw new IOException("Input could not be authorized because incorrect player view.");
        }
    }
}
