package com.example.crimereporter;

public class Display {
    public String title;
    public String time;
    //public String resolved;
    public String type;
    public String description;

    public Display(String title, String time, String type, String description) {
        this.title = title;
        this.time = time;
        //this.resolved = resolved;
        this.type = type;
        this.description = description;
    }

}
