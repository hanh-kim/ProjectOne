package vn.poly.personalmanagement.model;

public class Plan {
    private int id;
    private String planName;
    private  String date;
    private String time;
    private String describe;

    public Plan() {
    }

    public Plan(int id, String planName, String date, String time,String describe) {
        this.id = id;
        this.planName = planName;
        this.date = date;
        this.time = time;
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
