package com.itranswarp.learnjava.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Diary {
    private int id;
    private String name;
    private String content;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;
    private String title;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
