package com.group1.project3.model;

public class DataModel {
    /**Variables to initialize a card in pipeline
     */
    private String title;
    private String assigned_user;
    private String assigned_date;
    private int id;
    private int image;
    private String content;

    /**
     * Constructor to instantiate DataModel
     * @param title
     * @param assigned_user
     * @param assigned_date
     * @param id
     * @param image
     * @param content
     */
    public DataModel(String title, String assigned_user, String assigned_date, int id, int image,
                     String content) {
        this.title = title;
        this.assigned_user = assigned_user;
        this.assigned_date = assigned_date;
        this.id = id;
        this.image = image;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAssigned_user() {
        return assigned_user;
    }

    public String getAssigned_date() {
        return assigned_date;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getContent(){
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAssigned_user(String assigned_user) {
        this.assigned_user = assigned_user;
    }

    public void setAssigned_date(String assigned_date) {
        this.assigned_date = assigned_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
