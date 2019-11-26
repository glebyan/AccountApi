package mynonohttpd.pool.example;

public class Message {

    public Message(String result, String from, String to, String amount) {
        this.result = result;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    private String result;
    private String from;
    private String to;
    private String amount;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
