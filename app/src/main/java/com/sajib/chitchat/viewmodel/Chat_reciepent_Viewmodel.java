package com.sajib.chitchat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.sajib.chitchat.model.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sajib on 11-08-2016.
 */
public class Chat_reciepent_Viewmodel extends BaseObservable{
    private Message data;
    private Context context;
    public Chat_reciepent_Viewmodel(Context context,Message message) {
        this.context=context;
        this.data=message;
    }

    public String getRecipient()
    {
        return data.getmMessage();
    }

    public String getRecipientTime()  {
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
}
