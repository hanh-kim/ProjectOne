package vn.poly.personalmanagement.model;

import java.io.Serializable;

public class Income extends Money implements Serializable {

    public Income() {
    }

    public Income(int id, String title, String date, String time, long amount, String note) {
        super(id, title, date, time, amount, note);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public String getDate() {
        return super.getDate();
    }

    @Override
    public void setDate(String date) {
        super.setDate(date);
    }

    @Override
    public String getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(String time) {
        super.setTime(time);
    }

    @Override
    public long getAmount() {
        return super.getAmount();
    }

    @Override
    public void setAmount(long amount) {
        super.setAmount(amount);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }
}
