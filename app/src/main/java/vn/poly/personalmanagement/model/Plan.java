package vn.poly.personalmanagement.model;

import java.io.Serializable;

public class Plan implements Serializable {
    private int id;
    private String planName;
    private  String date;
    private String time;
    private String timeAlarm;
    private int isAlarm;
    private String describe;

    public Plan() {
    }
    public Plan( String planName, String date, String time, String timeAlarm, int isAlarmed, String describe) {
        this.planName = planName;
        this.date = date;
        this.time = time;
        this.timeAlarm = timeAlarm;
        this.isAlarm = isAlarmed;
        this.describe = describe;
    }

    public Plan(int id, String planName, String date, String time, String timeAlarm, int isAlarmed, String describe) {
        this.id = id;
        this.planName = planName;
        this.date = date;
        this.time = time;
        this.timeAlarm = timeAlarm;
        this.isAlarm = isAlarmed;
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

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(String timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public int getAlarmed() {
        return isAlarm;
    }

    public void setAlarmed(int isAlarmed) {
        this.isAlarm = isAlarmed;
    }
}
