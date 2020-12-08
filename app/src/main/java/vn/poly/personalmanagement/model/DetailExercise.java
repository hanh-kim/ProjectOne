package vn.poly.personalmanagement.model;

public class DetailExercise {
    private int id;
    private String date;
    private String exercise;
    private String describe;

    public DetailExercise() {
    }

    public DetailExercise(int id, String date, String exercise, String describe) {
        this.id = id;
        this.date = date;
        this.exercise = exercise;
        this.describe = describe;
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

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
