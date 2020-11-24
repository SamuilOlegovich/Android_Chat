package com.samuilolegovich.vedroidchat;

import android.provider.ContactsContract;

import java.util.Date;

public class  Message {
    private String textMessage;
    private String userName;

    private Long messageTime;


    public Message() { }

    public Message(String userName, String textMessage) {
        this.messageTime = new Date().getTime();
        this.textMessage = textMessage;
        this.userName = userName;
    }


    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
}
