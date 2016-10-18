package com.sajib.chitchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sajib.chitchat.R;
import com.sajib.chitchat.databinding.ActivityMainBinding;
import com.sajib.chitchat.viewmodel.MainViewmodel;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mainBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
        mainBinding.setViewmodel(new MainViewmodel(this, this, mainBinding, firebaseAuth, mAuthlistner));



    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthlistner);

    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAuthlistner != null) {
            firebaseAuth.removeAuthStateListener(mAuthlistner);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
