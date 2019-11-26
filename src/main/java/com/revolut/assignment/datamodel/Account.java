package com.revolut.assignment.datamodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.math.RoundingMode.HALF_EVEN;

public class Account {

    BigDecimal value;
    Long lastModifyTimestamp;
    List<History> historyList = new ArrayList<>();
    Semaphore semaphore = new Semaphore(1, true);

    public Account(){
        this.value = new BigDecimal(0);
    }

    public Account(BigDecimal value){
        this.value = value;
        this.value.setScale(2, HALF_EVEN);
        this.lastModifyTimestamp = System.currentTimeMillis();
    }

    public Message changeValue(BigDecimal delta){
        boolean result = false;
        String message = "UNEXPECTED_ERROR"; // TODO create enum for this
        long historyIndex = -1;

        try {
            semaphore.acquire();

            BigDecimal newValue = value.add(delta);

            if (newValue.compareTo(BigDecimal.ZERO) == -1){
                message = "NO_ENOUGH_MONEY"; // TODO create enum for this
                result = false;
                historyList.add(new History());
                historyIndex = historyList.size() - 1;
            } else {
                message = "OK"; // TODO create enum for this
                result = true;
                value = newValue;
                historyList.add(new History());
                historyIndex = historyList.size() - 1;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            result = false;
            message = "CANT_GET_CONTROL"; // TODO create enum for this
        } finally {
            semaphore.release();
        }

        return new Message(result, message, historyIndex);
    }

    public void revertChangeValue(long historyIndex){
        // revert operation based on historyIndex immediately after performing
        // or in other time using historyIndex
        // this operation may put account in technical overdraft
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    public List<History> getHistoryList(){
        // return all history, mostly narrow for testing not for frequently use in production
        return null;
    }

    public List<History> getHistoryList(int pageSize, int pageNumber){

        return null;
    }

    public History getHistoryByTransactionId(long TransactionId){ //slow! not for frequently using

        return null;
    }

    public BigDecimal getValue(){
        return value; // BigDecimal is immutable so in my humble opinion its pretty safe
    }

    public int queueLength(){ // for query length monitoring
        return semaphore.getQueueLength();
    }

    // TODO Create my own semaphore with method that return queue. For checking process that not released account.


    @Override
    public String toString() {
        return "Account{" +
                "value=" + value +
                ", lastModifyTimestamp=" + lastModifyTimestamp +
                ", historyList=" + historyList +
                '}';
    }
}
