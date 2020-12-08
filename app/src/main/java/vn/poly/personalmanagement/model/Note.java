package vn.poly.personalmanagement.model;

import java.io.Serializable;

public class Note implements Serializable {
    private int id;
    private String title;
    private int folderID;
    private String date;
    private String time;
    private String content;
    private int isDeleted;

    public Note() {
    }


    public Note(int id, String title, int folderID, String date, String time, String content, int isDeleted) {
        this.id = id;
        this.title = title;
        this.folderID = folderID;
        this.date = date;
        this.time = time;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDelete) {
        this.isDeleted = isDelete;
    }
}
