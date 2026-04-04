package src.Network;

import java.io.Serializable;

public class Packet implements Serializable {

    private String message;
    private Object[] payload;
    private boolean success;

    public Packet(String message, Object[] payload) {
        this.message = message;
        this.payload = payload;
    }

    public Packet(Object[] payload) {
        this.payload = payload;
    }

    public Packet(String message) {
        this.message = message;
        this.payload = payload;
    }

    public Packet(Boolean success) {
        this.success = success;
    }

    public Packet(){}

    public String getMessage() {
        return message;
    }

    public Object[] getPayload() {
        return payload;
    }

    public boolean isSuccess() {
        return success;
    }

}
