package vn.poly.personalmanagement.model;

public class Exercise {
    private int id;
    private String exerciseName;

    public Exercise() {
    }

    public Exercise(int id, String exerciseName) {
        this.id = id;
        this.exerciseName = exerciseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
}
