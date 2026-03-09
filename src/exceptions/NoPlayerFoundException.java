package src.exceptions;

/**
 * thrown when player is found to attack
 */
public class NoPlayerFoundException extends RuntimeException{
    public NoPlayerFoundException(String message){
        super(message);
    }
}
