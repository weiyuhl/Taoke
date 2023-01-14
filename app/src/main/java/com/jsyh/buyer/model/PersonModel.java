package com.jsyh.buyer.model;

/**
 * Created by mo on 17-4-19.
 */

public class PersonModel {
    
    
    private String nick;
    private String avatarUrl;
    public String openId;
    public String openSid;

    public PersonModel() {
    }

    public PersonModel(String nick, String avatarUrl, String openId, String openSid) {
        this.nick = nick;
        this.avatarUrl = avatarUrl;
        this.openId = openId;
        this.openSid = openSid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenSid() {
        return openSid;
    }

    public void setOpenSid(String openSid) {
        this.openSid = openSid;
    }
}
