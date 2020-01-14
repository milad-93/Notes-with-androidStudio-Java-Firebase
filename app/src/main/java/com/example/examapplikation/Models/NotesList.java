package com.example.examapplikation.Models;

public class NotesList {

    private String title;
    private String text;
    private String time;

    public NotesList(){

    }
    public NotesList(String title, String text, String time){

        this.title=title;
        this.text= text;
        this.time= time;
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


    public void setTime(String time){
        this.time=time;
    }
    public String GetTime(){
        return time;
    }
}


// remove time if needed but this affects HOMEACTIVITY ON DISPLAY AND THE EDIT
// also the input to database from inputactivity
// also remove get setters