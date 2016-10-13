package com.sajib.chitchat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajib on 09-08-2016.
 */
public class User {
    String Email;
    String Name;
    String Password;
    String Status;
    String Provider;
    String Id;
    String creationtime;
    String Profilecode;
    String Count;

    String mEmail;
    String mName;
    String mPassword;
    String mStatus;
    String mProvider;
    String mId;

    public String getmCount() {
        return Count;
    }

    public void setmCount(String mCount) {
        this.mCount = mCount;
    }

    String mCount;

    public String getmProfilecode() {
        return Profilecode;
    }

    public void setmProfilecode(String mProfilecode) {
        this.mProfilecode = mProfilecode;
    }

    String mProfilecode;

    public String getMcreationtime() {
        return creationtime;
    }

    public void setMcreationtime(String mcreationtime) {
        this.mcreationtime = mcreationtime;
    }

    String mcreationtime;

    public String getmId() {
        return Id;
    }

    User()
    {

    }

    User(String Email,String Id,String Name,String Password,String Status,String creationtime,String Provider,String Count)
    {
        this.Email=Email;
        this.Name=Name;
        this.Password=Password;
        this.Status=Status;
        this.Provider=Provider;
        this.creationtime=creationtime;
        this.Id=Id;
        this.Count=Count;

    }

    public String getmName() {
        return Name;
    }


    public String getmPassword() {
        return Password;
    }

    public void setmStatus(String status)
    {
        this.Status=status;
    }

    public String getmStatus() {
        return Status;
    }

    public String getmEmail() {
        return Email;
    }
}
