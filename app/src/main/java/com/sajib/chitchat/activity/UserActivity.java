package com.sajib.chitchat.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajib.chitchat.NotificationService;
import com.sajib.chitchat.R;
import com.sajib.chitchat.adapter.UserRecyclerviewAdapter;
import com.sajib.chitchat.databinding.ActivityUserBinding;
import com.sajib.chitchat.model.NotificationModel;
import com.sajib.chitchat.model.User;


public class UserActivity extends AppCompatActivity implements UserRecyclerviewAdapter.OnItemClickListner{
    private ActivityUserBinding mBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private String userId;
    private DatabaseReference myRef;
    private ChildEventListener childEventListener;
    private DatabaseReference connectedRef;
    private ValueEventListener valueEventListener;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_user);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.userlist.setVisibility(View.INVISIBLE);

        myRef = FirebaseDatabase.getInstance().getReference("User");

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {

                }

            }
        };

        final UserRecyclerviewAdapter adapter=new UserRecyclerviewAdapter(this,firebaseAuth.getCurrentUser().getUid(),this,firebaseAuth.getCurrentUser().getEmail());

        setUserisConnectedorNot();

        mBinding.userlist.setLayoutManager(new LinearLayoutManager(this));

        getUserdata(adapter);

        firebaseAuth.addAuthStateListener(mAuthlistner);
        connectedRef.addValueEventListener(valueEventListener);

        mBinding.userlist.setAdapter(adapter);

        myRef.addChildEventListener(childEventListener);


    }

    private void setUserisConnectedorNot() {

        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected&&user!=null) {
                    myRef.child(user.getUid()).child("Status").setValue("1");
                    myRef.child(user.getUid()).child("Status").onDisconnect().setValue("0");
                    Toast.makeText(UserActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                    mBinding.progressBar.setVisibility(View.INVISIBLE);
                    mBinding.userlist.setVisibility(View.VISIBLE);

                } else {

                    Toast.makeText(UserActivity.this,"Disconnected",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
    }

    private void getUserdata(final UserRecyclerviewAdapter adapter) {
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userId = dataSnapshot.getKey();

                if (user!=null&&!userId.equals(user.getUid())) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot != null) {

                            User user = dataSnapshot.getValue(User.class);

                            adapter.setData(user, 0);
                        }
                    }
                }

                else {
                    if(dataSnapshot!=null)
                    {
                        User currentuser = dataSnapshot.getValue(User.class);
                        mBinding.toolbar.setTitle(currentuser.getmName());
                        adapter.setCurrentuserdata(currentuser);
                    }
                }

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                userId=dataSnapshot.getKey();
                if(user!=null&&!userId.equals(user.getUid())) {
                    if(dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        adapter.setData(user,1);
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
    }




    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthlistner);
        connectedRef.addValueEventListener(valueEventListener);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthlistner!=null)
        {
            firebaseAuth.removeAuthStateListener(mAuthlistner);

        }
        connectedRef.removeEventListener(valueEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuthlistner!=null)
        {
            firebaseAuth.removeAuthStateListener(mAuthlistner);

        }
        connectedRef.removeEventListener(valueEventListener);

    }

    @Override
    public void OnItemClick(String chatroot, String Senderid, String RecientId) {
        Intent intent=new Intent(UserActivity.this,ChatActivity.class);
        intent.putExtra("child", chatroot);
        intent.putExtra("sender",Senderid);
        intent.putExtra("reciepent",RecientId);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            myRef.removeEventListener(childEventListener);
            SharedPreferences sharedPreferences=getSharedPreferences("Mylogin",MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();

            firebaseAuth.signOut();
            myRef.child(user.getUid()).child("Status").onDisconnect().setValue("0");
            connectedRef.removeEventListener(valueEventListener);
            Intent intent=new Intent(UserActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }



}


