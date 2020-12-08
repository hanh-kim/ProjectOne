package vn.poly.personalmanagement.model;

import java.util.List;

public class Fitness {
    private int id;
    private String date;
    private int amountExercises;


    public Fitness() {
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

    public int getAmountExercises() {
        return amountExercises;
    }

    public void setAmountExercises(int amountExercises) {
        this.amountExercises = amountExercises;
    }
}
