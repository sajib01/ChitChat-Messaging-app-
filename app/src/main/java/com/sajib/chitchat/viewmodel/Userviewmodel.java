package com.sajib.chitchat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;

import com.sajib.chitchat.model.User;

/**
 * Created by sajib on 10-08-2016.
 */
public class Userviewmodel extends BaseObservable {
    Context context;
    User user;
    public Userviewmodel(Context context, User user) {

        this.context=context;
        this.user=user;
    }

    public Drawable getProfile()
    {

        int resource = context.getResources().getIdentifier("drawable/avatar_"+user.getmProfilecode(),null, context.getPackageName());
        return context.getResources().getDrawable(resource);

    }

    public Drawable getStatus()
    {
        int resource = 0;
        if(user.getmStatus().equals("0"))
        {
            resource = context.getResources().getIdentifier("drawable/offline", null, context.getPackageName());

        }

        if(user.getmStatus().equals("1"))
        {
            resource = context.getResources().getIdentifier("drawable/online", null, context.getPackageName());

        }

        return context.getResources().getDrawable(resource);
    }

    public String getUsername()
    {
        return user.getmName();
    }

    public void setData(User data) {
        this.user = data;
        notifyChange();
    }
}
