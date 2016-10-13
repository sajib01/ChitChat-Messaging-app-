package com.sajib.chitchat.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.os.Binder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sajib.chitchat.LocationService;
import com.sajib.chitchat.activity.MainActivity;
import com.sajib.chitchat.activity.MapsActivity;
import com.sajib.chitchat.activity.RegistrationActivity;
import com.sajib.chitchat.activity.UserActivity;
import com.sajib.chitchat.databinding.ActivityChatBinding;
import com.sajib.chitchat.databinding.ActivityMainBinding;

/**
 * Created by sajib on 09-08-2016.
 */
public class MainViewmodel{
    Context context;
    public ObservableField<Boolean> emailRequired=new ObservableField<>(true);
    public ObservableField<Boolean> passwordRequired=new ObservableField<>(true);
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG="MainViewmodel";
    private ActivityMainBinding mainBinding;
    MainActivity mainActivity;
    String email;
    String password;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String sharedPref="Mylogin";
    public MainViewmodel()
    {

    }
    public MainViewmodel(Context context,MainActivity mainActivity, ActivityMainBinding mainBinding,FirebaseAuth firebaseAuth,FirebaseAuth.AuthStateListener mAuthListener)
    {

        this.context=context;
        this.mainBinding=mainBinding;
        this.mainActivity=mainActivity;
        this.firebaseAuth = firebaseAuth;
        this.mAuthListener = mAuthListener;

    }

    public TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0)
            {
                emailRequired.set(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher watcher2=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0)
            {
                passwordRequired.set(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void GoToRegistrationActivity()
    {
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }
    public void SignInToChatActivity()
    {

        email=mainBinding.inputEmail.getText().toString();
        password=mainBinding.inputPassword.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailRequired.set(false);
        }
        if(TextUtils.isEmpty(password)) {
            passwordRequired.set(false);
        }

        if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            this.progressDialog=new ProgressDialog(context);
            this.progressDialog.setMessage("Logging in...");
            this.progressDialog.getWindow().setGravity(Gravity.CENTER);
            this.progressDialog.show();
            SigninAccount(email,password);
        }

    }


    private void SigninAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful())
                        {

                            sharedPreferences=context.getSharedPreferences(sharedPref,Context.MODE_PRIVATE);
                            editor=sharedPreferences.edit();
                            editor.putString("Email",email);
                            editor.putString("Password",password);
                            editor.putBoolean("IsLoggedin",true);
                            editor.commit();
                            hideKeyboard();

                            Snackbar.make(mainBinding.relative,"Successfully Logged in", Snackbar.LENGTH_SHORT).show();
                            mainBinding.inputEmail.setText("");
                            mainBinding.inputPassword.setText("");
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            Intent intent=new Intent(context, UserActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            context.startActivity(intent);
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            hideKeyboard();
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            Snackbar.make(mainBinding.relative,"Authentication failed", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signOut() {
        firebaseAuth.signOut();
    }

    public void hideKeyboard()
    {

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainBinding.inputPassword.getWindowToken(), 0);

    }

    }





