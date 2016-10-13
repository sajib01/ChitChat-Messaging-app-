package com.sajib.chitchat.model;

/**
 * Created by sajib on 11-08-2016.
 */
public class Message {
    String Message;
    String Reciepent;
    String Sender;
    String Locationurl;
    String Latitude;
    String Longitude;
    String Time;

    String mSender;
    String mReciepent;
    String mMessage;

    public String getmTime() {
        return Time;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    String mTime;

    public String getmLatitude() {
        return Latitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    String mLatitude;

    public String getmLongitude() {
        return Longitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    String mLongitude;

    public String getmLocationurl() {
        return Locationurl;
    }

    public void setmLocationurl(String mLocationurl) {
        this.mLocationurl = mLocationurl;
    }

    String mLocationurl;
    private int mRecipientOrSenderStatus;

    public int getmRecipientOrSenderLoactionStatus() {
        return mRecipientOrSenderLoactionStatus;
    }

    public void setmRecipientOrSenderLoactionStatus(int mRecipientOrSenderLoactionStatus) {
        this.mRecipientOrSenderLoactionStatus = mRecipientOrSenderLoactionStatus;
    }

    private int mRecipientOrSenderLoactionStatus;

    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }
    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }

    Message()
    {

    }

    Message(String Message,String Reciepent,String Sender,String Time)
    {
        this.Sender=Sender;
        this.Reciepent=Reciepent;
        this.Message=Message;
        this.Time=Time;
    }

    public String getmSender() {
        return Sender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmReciepent() {
        return Reciepent;
    }

    public void setmReciepent(String mReciepent) {
        this.mReciepent = mReciepent;
    }

    public String getmMessage() {
        return Message;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
