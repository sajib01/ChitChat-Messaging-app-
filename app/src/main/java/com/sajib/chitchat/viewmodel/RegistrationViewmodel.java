package com.sajib.chitchat.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sajib.chitchat.activity.MainActivity;
import com.sajib.chitchat.activity.RegistrationActivity;
import com.sajib.chitchat.activity.UserActivity;
import com.sajib.chitchat.adapter.Avatar_recyclerviewAdapter;
import com.sajib.chitchat.databinding.ActivityRegistrationBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sajib on 09-08-2016.
 */
public class RegistrationViewmodel implements Avatar_recyclerviewAdapter.GetData{
    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;
    RegistrationActivity registrationActivity;
    ActivityRegistrationBinding mBinding;
    public ObservableField<Boolean> fullnameError=new ObservableField<>(true);
    public ObservableField<Boolean> emailError=new ObservableField<>(true);
    public ObservableField<Boolean> passwordError=new ObservableField<>(true);
    public ObservableField<String> Requirement=new ObservableField<>();
    ArrayList<String> uid=new ArrayList<>();
    String fullname;
    String password;
    String email;
    int status=0;
    int code=-1;
    ProgressDialog progressDialog;

    public RegistrationViewmodel()
    {

    }
    public RegistrationViewmodel(Context context, FirebaseAuth firebaseAuth, FirebaseAuth.AuthStateListener mAuthlistner, RegistrationActivity registrationActivity, ActivityRegistrationBinding mBinding) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.mAuthlistener = mAuthlistner;
        this.registrationActivity=registrationActivity;
        this.mBinding=mBinding;
    }

    public void SignUP() {
        fullname=mBinding.inputFirstname.getText().toString();
        email=mBinding.inputEmail.getText().toString();
        password=mBinding.inputPassword.getText().toString();

        if(TextUtils.isEmpty(fullname))
        {
            fullnameError.set(false);
        }
        if(TextUtils.isEmpty(email))
        {
            emailError.set(false);
        }
        if(TextUtils.isEmpty(password))
        {
            passwordError.set(false);
            Requirement.set("Password must not set Empty");
        }
        if(password.length()<6&&!TextUtils.isEmpty(password))
        {
            passwordError.set(false);
            Requirement.set("Password must be greater than 6 and Alphanumeric");
        }

        Log.d("ffcc",password);

        if(code==-1) {
            Toast.makeText(context,"Select an avater",Toast.LENGTH_SHORT).show();
        }

        if(!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)&&passwordError.get()!=false&&code!=-1)
        {
            this.progressDialog=new ProgressDialog(context);
            this.progressDialog.setMessage("Registering user please wait...");
            this.progressDialog.getWindow().setGravity(Gravity.CENTER);
            this.progressDialog.show();
            CreateAccount(email,password);
        }

    }

    public TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0)
            {
                fullnameError.set(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher watcher1=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0)
            {
                emailError.set(true);
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
                passwordError.set(true);

            }
            if(s.length()>6)
            {
                passwordError.set(true);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void GoToLoginActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    private void CreateAccount(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registrationActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideKeyboard();
                            mBinding.inputFirstname.setText("");
                            mBinding.inputEmail.setText("");
                            mBinding.inputPassword.setText("");
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }

                            SigninAccount(email,password);
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
                            Snackbar.make(mBinding.relative,"Authentication failed",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    private void SigninAccount(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(registrationActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            uid.add(firebaseAuth.getCurrentUser().getUid());
                            Calendar calendar=Calendar.getInstance();

                            Map<String,String> users=new HashMap<String, String>();
                            users.put("Email",email);
                            users.put("Name",fullname);
                            users.put("Password",password);
                            users.put("Status","1");
                            users.put("Profilecode", String.valueOf(code));
                            users.put("provider",firebaseAuth.getCurrentUser().getProviderId());
                            users.put("creationtime", String.valueOf(calendar.getTime()));
                            users.put("Id",firebaseAuth.getCurrentUser().getUid());
                            Log.d("hasd", String.valueOf(code));

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("User");

                            myRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(users);
                            Intent intent=new Intent(context, UserActivity.class);
                            intent.putStringArrayListExtra("uid", uid);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                        }

                    }
                });
    }

    public void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.inputPassword.getWindowToken(), 0);
    }

    @Override
    public void Displaydata(int code) {
        Log.d("fcvg", String.valueOf(code));
        this.code=code;
    }
}
