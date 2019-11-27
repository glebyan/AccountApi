package com.revolut.assignment.datamodel;

public class Message {
    final boolean status;
    final String message;
    final int historyIndex;

    public Message(boolean status, String message, int historyIndex){
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

    public boolean getStatus(){
        return status;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }


}
