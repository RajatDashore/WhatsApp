package com.example.whatsappclone.Modules;

public class MessageModel {

    private String uID, message, messageId, senderId;
    private Long timeStamp;

    public MessageModel(String uID, String message, Long timeStamp) {
        this.uID = uID;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;

    }

    public MessageModel(String uID, String message) {
        this.uID = uID;
        this.message = message;
    }

    public MessageModel() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
