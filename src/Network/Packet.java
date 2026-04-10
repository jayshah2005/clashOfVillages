package src.Network;

import src.enums.View;

import java.io.Serializable;

public class Packet implements Serializable {

    private String message;
    private Object[] payload;
    private boolean success;
    private View currentView;

    public Packet(String message, Object[] payload) {
        this.message = message;
        this.payload = payload;
    }

    public Packet(Object[] payload) {
        this.payload = payload;
    }

    public Packet(String message) {
        this.message = message;
    }

    public Packet(Boolean success) {
        this.success = success;
    }

    public Packet(String inp, View currentView) {
        this.message = inp;
        this.currentView = currentView;
    }

    public Packet(String output, View currentView, Object[] objects) {
        this.message = output;
        this.payload = objects;
        this.currentView = currentView;
    }

    public Packet(String message, boolean b) {
        this.message = message;
        this.success = b;
    }

    public Packet(String message, boolean b, Object[] objects) {
        this.message = message;
        this.success = b;
        this.payload = objects;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getPayload() {
        return payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public View getCurrentView() {
        return currentView;
    }
}
