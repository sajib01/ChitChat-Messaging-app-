package com.sajib.chitchat.model;

import java.sql.Time;
import java.util.jar.Attributes;

/**
 * Created by sajib on 24/9/16.
 */

public class NotificationModel {
    String Key;
    String TimeStamp;
    String Recipient;
    String Sender;
    String Chat;
    String NotiKey;
    String NameofSender;
    String NotificationId;

    public String getmNotificationId() {
        return NotificationId;
    }

    public void setmNotificationId(String mNotificationId) {
        this.mNotificationId = mNotificationId;
    }

    String mNotificationId;

    public String getmKey() {
        return Key;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmTimeStamp() {
        return TimeStamp;
    }

    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    String mKey;
    String mTimeStamp;

    public String getmNotiKey() {
        return NotiKey;
    }

    public void setmNotiKey(String mNotiKey) {
        this.mNotiKey = mNotiKey;
    }

    String mNotiKey;

    public String getmChat() {
        return Chat;
    }

    public void setmChat(String mChat) {
        this.mChat = mChat;
    }

    String mChat;

    public String getmSender() {
        return Sender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    String mSender;

    public String getmRecipient() {
        return Recipient;
    }

    public void setmRecipient(String mRecipient) {
        this.mRecipient = mRecipient;
    }

    String mRecipient;

    public String getmNameofSender() {
        return NameofSender;
    }

    public void setmNameofSender(String mNameofSender) {
        this.mNameofSender = mNameofSender;
    }

    String mNameofSender;

    NotificationModel()
    {

    }

    NotificationModel(String Key,String TimeStamp,String Recipient,String Sender,String Chat,String NotiKey,String NameofSender,String NotificationId)
    {
        this.Key=Key;
        this.TimeStamp=TimeStamp;
        this.Recipient=Recipient;
        this.Chat=Chat;
        this.Sender=Sender;
        this.NotiKey=NotiKey;
        this.NameofSender= NameofSender;
        this.NotificationId=NotificationId;
    }
}
