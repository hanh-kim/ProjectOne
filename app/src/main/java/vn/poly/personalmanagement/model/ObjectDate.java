package vn.poly.personalmanagement.model;

public class ObjectDate {

    private String date;
    private int amount;

    public ObjectDate() {
    }

    public ObjectDate(String date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
