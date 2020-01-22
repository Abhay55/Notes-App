package com.example.notes.model;

public class Note {

    private String title;
    private String detail;
    private String dateNoteAdded;
    private int id;

    public Note() {
    }

    public Note(String title, String detail, String dateNoteAdded, int id) {
        this.title = title;
        this.detail = detail;
        this.dateNoteAdded = dateNoteAdded;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDateNoteAdded() {
        return dateNoteAdded;
    }

    public void setDateNoteAdded(String dateNoteAdded) {
        this.dateNoteAdded = dateNoteAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
