package com.revolut.assignment.datamodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.math.RoundingMode.HALF_EVEN;

public class Account {

    private BigDecimal value;
    private Long lastModifyTimestamp;
    List<History> historyList = new ArrayList<>();
    Semaphore semaphore = new Semaphore(1, true); // TODO replace the semaphore with reentantlock

    public Account() {
        this.value = new BigDecimal(0);
    }

    public Account(BigDecimal value) {
        this.value = value;
        this.value.setScale(2, HALF_EVEN);
        this.lastModifyTimestamp = System.currentTimeMillis();
    }

    public Message changeValue(BigDecimal delta) {

        Message message;

        try {
            semaphore.acquire();

            BigDecimal newValue = value.add(delta);

            if (newValue.compareTo(BigDecimal.ZERO) == -1) {
                String comment = "Not Enough Money";
                historyList.add(new History(delta, comment, false));
                long historyIndex = historyList.size() - 1;
                message = new Message(false, comment, historyIndex);
            } else {
                String comment = "Success";
                value = newValue;
                historyList.add(new History(delta, comment, true));
                long historyIndex = historyList.size() - 1;
                message = new Message(true, comment, historyIndex);
            }

        } catch (InterruptedException e) {
            String comment = "Internal Error";
            e.printStackTrace();
            historyList.add(new History(delta, comment, false));
            long historyIndex = historyList.size() - 1;
            message = new Message(false, comment, historyIndex);
        } finally {
            semaphore.release();
        }
        return message;
    }

    public Message revertChangeValue(int historyIndex) {

        History history = historyList.get(historyIndex);

        BigDecimal delta = history.getDelta().negate();

        if (!history.isRevert()) {

            return changeValue(delta);
        }
        String comment = "Can't revert this operation";
        historyList.add(new History(delta, comment + " " + historyIndex, false));
        return new Message(false, comment, historyIndex);
    }

    public List<History> getHistoryList() {
        // return all history, mostly narrow for testing not for frequently use in production
        return historyList;
    }

    public List<History> getHistoryList(int pageSize, int pageNumber) {
        // TOTO implement paging
        return null;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getQueueLength() { // for query length monitoring
        return semaphore.getQueueLength();
    }

    @Override
    public String toString() {
        return "Account{" +
                "value=" + value +
                ", lastModifyTimestamp=" + lastModifyTimestamp +
                ", historyList=" + historyList +
                '}';
    }
}
