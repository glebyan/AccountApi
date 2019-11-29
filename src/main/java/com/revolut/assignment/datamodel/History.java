package com.revolut.assignment.datamodel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class History {
    private final Long timestamp;
    private final BigDecimal delta;
    private final String comment;
    private final boolean result;
    private final boolean isRevert;
    private final BigDecimal currentAmount;

    History(BigDecimal delta, BigDecimal currentAmount, String comment, boolean result){
        this.timestamp = System.currentTimeMillis();
        this.delta = delta;
        this.comment = comment;
        this.result = result;
        this.isRevert = false;
        this.currentAmount = currentAmount;
    }

    public History(long timestamp, BigDecimal delta,  String comment,  boolean result, boolean isRevert, BigDecimal currentAmount){
        // for test's only
        this.timestamp = timestamp;
        this.delta = delta;
        this.comment = comment;
        this.result = result;
        this.isRevert = false;
        this.currentAmount = currentAmount;
    }

    History(BigDecimal delta, BigDecimal currentAmount, String comment, boolean result, boolean isRevert){
        this.timestamp = System.currentTimeMillis();
        this.delta = delta;
        this.comment = comment;
        this.result = result;
        this.isRevert = isRevert;
        this.currentAmount = currentAmount;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public String getComment() {
        return comment;
    }

    public boolean isResult() {
        return result;
    }

    public boolean isRevert() {
        return isRevert;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    @Override
    public String toString() {
        return "History{" +
                "timestamp=" + timestamp +
                ", delta=" + delta +
                ", comment='" + comment + '\'' +
                ", result=" + result +
                ", isRevert=" + isRevert +
                ", currentAmount=" + currentAmount +
                '}';
    }
}
