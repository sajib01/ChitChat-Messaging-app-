package com.sajib.chitchat.model;

/**
 * Created by sajib on 17/10/16.
 */

public class Typing {
    String setTyping;
    String Sender;
    String Recipient;

    public String getmTyping() {
        return setTyping;
    }

    public void setmTyping(String mTyping) {
        this.mTyping = mTyping;
    }

    String mTyping;

    public String getmSender() {
        return Sender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmRecipient() {
        return Recipient;
    }

    public void setmRecipient(String mRecipient) {
        this.mRecipient = mRecipient;
    }

    String mSender;
    String mRecipient;

    Typing()
    {

    }
    Typing(String setTyping,String Sender,String Recipent)
    {
        this.setTyping=setTyping;
        this.Sender=Sender;
        this.Recipient=Recipent;
    }


}
