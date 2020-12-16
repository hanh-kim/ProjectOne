package vn.poly.personalmanagement.model;

import java.io.Serializable;


public class Eating implements Serializable {
    private int id;
    private String date;
    private int meal;

    public Eating() {
    }

    public Eating(int id, String date, int meal) {
        this.id = id;
        this.date = date;
        this.meal = meal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }
}
