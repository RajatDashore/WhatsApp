package com.example.whatsappclone.Modules;

public class Users {
    String proPicture, userName, mail, password, userId, lastMessage, aboutMe;

    public Users(String proPicture, String userName, String mail, String password, String userId, String lastMessage) {
        this.proPicture = proPicture;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public Users(String userId, String userName, String mail, String password) {
        this.userId = userId;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
    }

    public Users() {

    }

    public Users(String userName, String mail, String password) {
        this.userName = userName;
        this.mail = mail;
        this.password = password;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getProPicture() {
        return proPicture;
    }

    public void setProPicture(String proPicture) {
        this.proPicture = proPicture;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}


