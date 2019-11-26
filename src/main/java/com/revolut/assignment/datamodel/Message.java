package com.revolut.assignment.datamodel;

public class Message {
    boolean result;
    String message;
    long historyIndex;

    public Message(boolean result, String message, long historyIndex){
        this.result = result;
        this.message = message; // TODO must be enum
        this.historyIndex = historyIndex;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
