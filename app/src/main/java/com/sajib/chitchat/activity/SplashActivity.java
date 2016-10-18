package com.sajib.chitchat.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sajib.chitchat.NotificationService;
import com.sajib.chitchat.R;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final String sharedPref="Mylogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/green avocado.ttf");
        TextView textView= (TextView) findViewById(R.id.chitchat);
        ImageView imageView= (ImageView) findViewById(R.id.image1);
        textView.setTypeface(custom_font);
        startService(new Intent(SplashActivity.this, NotificationService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences=getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                if(sharedPreferences.contains("Email")&&sharedPreferences.contains("Password")&&sharedPreferences.contains("IsLoggedin"))
                {
                    if(sharedPreferences.getBoolean("IsLoggedin",false)&& FirebaseAuth.getInstance().getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(SplashActivity.this, UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        },3000);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

