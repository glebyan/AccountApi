package com.revolut.assignment.datamodel;

public class Message {
    final boolean status;
    final String message;
    final long historyIndex;

    public Message(boolean status, String message, long historyIndex){
        this.status = status;
        this.message = message; // TODO must be enum
        this.historyIndex = historyIndex;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getHistoryIndex() {
        return historyIndex;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
