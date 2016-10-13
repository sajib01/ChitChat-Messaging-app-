package com.sajib.chitchat.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajib.chitchat.R;
import com.sajib.chitchat.adapter.Avatar_recyclerviewAdapter;
import com.sajib.chitchat.databinding.ActivityRegistrationBinding;
import com.sajib.chitchat.model.Avatar;
import com.sajib.chitchat.viewmodel.RegistrationViewmodel;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding mBinding;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthlistner;
    private static String TAG="RegistrationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding =DataBindingUtil.setContentView(this,R.layout.activity_registration);
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


        RegistrationViewmodel registrationViewmodel=new RegistrationViewmodel(this, firebaseAuth, mAuthlistner, this, mBinding);
        mBinding.setViewmodel(registrationViewmodel);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.avatarRecycler.setLayoutManager(linearLayoutManager);
        List<Avatar> avatars=new ArrayList<>();
        for(int i=0;i<16;i++)
        {
            Avatar avatar=new Avatar();
            avatar.setSelected(false);
            avatar.setCode(i);
            avatars.add(avatar);
        }

        mBinding.avatarRecycler.setAdapter(new Avatar_recyclerviewAdapter(this,avatars,registrationViewmodel));


    }
}
