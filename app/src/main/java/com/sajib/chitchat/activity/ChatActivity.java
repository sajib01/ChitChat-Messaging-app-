package com.sajib.chitchat.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajib.chitchat.NotificationService;
import com.sajib.chitchat.R;
import com.sajib.chitchat.adapter.ChatRecyclerviewAdapter;
import com.sajib.chitchat.databinding.ActivityChatBinding;
import com.sajib.chitchat.model.Message;
import com.sajib.chitchat.model.NotificationModel;
import com.sajib.chitchat.model.Typing;
import com.sajib.chitchat.model.User;
import com.sajib.chitchat.viewmodel.ChatViewmodel;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements ChatViewmodel.SetRecyclerAdapter{
    private ActivityChatBinding chatBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private ChatRecyclerviewAdapter adapter;
    private String Child;
    private String Sender;
    private String Reciepent;
    private ChatViewmodel chatViewmodel;
    private ValueEventListener notivalueeventlistner;
    private ValueEventListener statuseventlistner;
    private DatabaseReference databaseref;
    private DatabaseReference dref;
    private DatabaseReference Typingref;
    private DatabaseReference checkTypingref;
    private ValueEventListener checkTypingornot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding= DataBindingUtil.setContentView(this,R.layout.activity_chat);

        setSupportActionBar(chatBinding.toolbar);

        Child=getIntent().getStringExtra("child");
        Sender=getIntent().getStringExtra("sender");
        Reciepent=getIntent().getStringExtra("reciepent");
        chatBinding.typing.setText("");

        final TextView name= (TextView) findViewById(R.id.name);
        final TextView status= (TextView) findViewById(R.id.status);

        FirebaseDatabase databasee=FirebaseDatabase.getInstance();
        databaseref=databasee.getReference("Notification");
        final NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notivalueeventlistner=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    NotificationModel model=snapshot.getValue(NotificationModel.class);
                    if(model.getmRecipient().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())&&Reciepent.equals(model.getmSender()))
                    {
                        databaseref.child(snapshot.getKey()).removeValue();
                        notificationManager.cancel(1235);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        firebaseAuth = FirebaseAuth.getInstance();
        mAuthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };

        final FirebaseDatabase databas= FirebaseDatabase.getInstance();
        DatabaseReference myRef = databas.getReference("User");
        myRef.child(Reciepent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                int resources = getResources().getIdentifier("drawable/avatar_" + user.getmProfilecode(), null, getPackageName());
                chatBinding.profile.setImageDrawable(ChatActivity.this.getResources().getDrawable(resources));
                name.setText(user.getmName());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        HashMap<String,String> typing=new HashMap<>();
        typing.put("setTyping","0");
        typing.put("Sender",Sender);
        typing.put("Recipient",Reciepent);
        Typingref=FirebaseDatabase.getInstance().getReference("Typing");
        Typingref.child(Sender).setValue(typing);


        chatBinding.chatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    Typingref.child(Sender).child("setTyping").setValue("1");
                }
                if(s.length()==0)
                {
                    Typingref.child(Sender).child("setTyping").setValue("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkTypingref=FirebaseDatabase.getInstance().getReference("Typing");
        checkTypingornot=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Typing tping=snapshot.getValue(Typing.class);
                    if(tping.getmSender().equals(Reciepent)&&tping.getmRecipient().equals(Sender))
                    {
                        if(tping.getmTyping().equals("0"))
                        {
                            chatBinding.typing.setText("");
                        }
                        if(tping.getmTyping().equals("1"))
                        {
                            chatBinding.typing.setText("typing...");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        checkTypingref.addValueEventListener(checkTypingornot);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chat");

        dref=FirebaseDatabase.getInstance().getReference("User");
        statuseventlistner=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user.getmStatus().equals("0"))
                {
                    status.setText("Offline");
                }
                if(user.getmStatus().equals("1"))
                {
                    status.setText("Online");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dref.child(Reciepent).addValueEventListener(statuseventlistner);


        adapter=new ChatRecyclerviewAdapter(this,firebaseAuth.getCurrentUser().getUid());



        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    Toast.makeText(ChatActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ChatActivity.this,"Disconnected",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("firebb","Listner was cancelled");
            }
        });

        chatBinding.chat.setLayoutManager(new LinearLayoutManager(this));

        chatBinding.chat.setAdapter(adapter);
        chatViewmodel=new ChatViewmodel(this,firebaseAuth,mAuthlistner,chatBinding,myRef,this,Child,Sender,Reciepent,adapter);
        chatBinding.setViewmodel(chatViewmodel);



    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseref.addValueEventListener(notivalueeventlistner);
        dref.child(Reciepent).addValueEventListener(statuseventlistner);
        checkTypingref.addValueEventListener(checkTypingornot);

    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseref.removeEventListener(notivalueeventlistner);
        dref.child(Reciepent).removeEventListener(statuseventlistner);
        checkTypingref.removeEventListener(checkTypingornot);
        Typingref.child(Sender).child("setTyping").setValue("0");
    }


    @Override
    public void setAdapterData(Message message) {

        adapter.setData(message);
    }
}
