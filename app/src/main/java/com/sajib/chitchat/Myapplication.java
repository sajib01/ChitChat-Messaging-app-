package com.sajib.chitchat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sajib on 09-05-2016.
 */
public class Myapplication extends Application {
    private static Myapplication sInstance;
    public static final String MAP_KEY="AIzaSyDn2IdJ2qK15b7nE_T7eanLtQPFam9EX1s";
    public static final String MAP_IMAGE_KEY="AIzaSyC2LchXwV133mZ8sGjTS--X_8HF8n_ruzw";
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        PrinthashKey();
    }
    public void PrinthashKey()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sajib.chitchat",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Key-Hash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    public static Myapplication getInstance()
    {
        return sInstance;
    }
    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }
}
