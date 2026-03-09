package src.exceptions;

/**
 * Thrown when a player when a player does not have enough resources to perform an action
 */
public class NotEnoughResourcesException extends Exception {

    public NotEnoughResourcesException(String message){
        super(message);
    }

}