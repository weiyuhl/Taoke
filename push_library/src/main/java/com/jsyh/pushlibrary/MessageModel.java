package com.jsyh.pushlibrary;

/**
 * Created by mo on 17-4-19.
 */

public class MessageModel {

    private int id;
    private String time;
    private String content;
    private int read;       // -1未读 0,已读


    public MessageModel() {
    }

    public MessageModel( String time, String content,int read) {
        this.time = time;
        this.content = content;
        this.read = read;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
