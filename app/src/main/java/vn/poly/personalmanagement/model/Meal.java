package vn.poly.personalmanagement.model;

import java.io.Serializable;

public class Meal implements Serializable {
    private int id;
    private String title;
    private String date;
    private String time;
    private String detail;

    public Meal() {
    }

    public Meal(String title, String date, String time, String detail) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.detail = detail;
    }

    public Meal(int id, String title, String date, String time, String detail) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.detail = detail;

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

    public String getDetailMeal() {
        return detail;
    }

    public void setDetailMeal(String detailMeal) {
        this.detail = detailMeal;
    }
}
