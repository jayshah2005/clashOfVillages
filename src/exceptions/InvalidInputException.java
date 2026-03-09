package src.exceptions;

/**
 * Thrown when an invalid input is received
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message){
        super(message);
    }

}