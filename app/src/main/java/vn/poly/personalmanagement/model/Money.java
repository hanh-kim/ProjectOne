package vn.poly.personalmanagement.model;

import java.io.Serializable;

public class Money implements Serializable {
    private int id;
    private String title;
    private String date;
    private String time;
    private long amount;
    private String description;

    public Money() {
    }

    public Money(int id, String title, String date, String time, long amount, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
