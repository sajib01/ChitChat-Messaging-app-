package com.sajib.chitchat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.sajib.chitchat.model.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sajib on 2/9/16.
 */
public class Chat_recipient_location_viewmodel extends BaseObservable{
    private Message data;
    private Context context;
    public Chat_recipient_location_viewmodel(Context context, Message message) {
        this.context=context;
        this.data=message;
    }

    public String getReciepent()
    {
        return data.getmMessage();
    }

    public String getRecipientText()
    {
        Date date = null;
        String dateString=data.getmTime();
        DateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
        try {
            date=dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time=String.format("%tR",date);
        return time;
    }

    public void setData(Message data) {
        this.data = data;
        notifyChange();
    }



    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
