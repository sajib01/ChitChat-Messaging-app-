package com.sajib.chitchat.model;

import android.databinding.tool.util.L;

/**
 * Created by sajib on 3/9/16.
 */
public class Location {
    Double Lat;
    Double Long;
    Double bearing;
    Location()
    {

    }
    public Double getmLat() {
        return Lat;
    }

    public void setmLat(Double mLat) {
        this.mLat = mLat;
    }

    public Double getmLong() {
        return Long;
    }

    public void setmLong(Double mLong) {
        this.mLong = mLong;
    }

    Double mLat;
    Double mLong;

    public Double getMbearing() {
        return bearing;
    }

    public void setMbearing(Double mbearing) {
        this.mbearing = mbearing;
    }

    Double mbearing;
    public Location(Double Lat,Double Long,Double bearing)
    {
        this.Lat=Lat;
        this.Long=Long;
        this.bearing=bearing;
    }


}
