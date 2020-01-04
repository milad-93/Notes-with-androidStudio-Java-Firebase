package com.example.examapplikation.Models;

public class NotesList {

    private String title;
    private String text;

    public NotesList(){

    }
    public NotesList(String title, String text){

        this.title=title;
        this.text= text;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
