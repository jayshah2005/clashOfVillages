package src.exceptions;

public class NoPlayerFoundException extends RuntimeException{
    public NoPlayerFoundException(String message){
        super(message);
    }
}
