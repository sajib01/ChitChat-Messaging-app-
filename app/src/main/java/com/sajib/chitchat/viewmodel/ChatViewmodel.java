package com.sajib.chitchat.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajib.chitchat.activity.ChatActivity;
import com.sajib.chitchat.activity.MapsActivity;
import com.sajib.chitchat.adapter.ChatRecyclerviewAdapter;
import com.sajib.chitchat.databinding.ActivityChatBinding;
import com.sajib.chitchat.model.Message;
import com.sajib.chitchat.model.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by sajib on 08-08-2016.
 */
public class ChatViewmodel implements MapsActivity.Mapsnap {
    public static final int SENDER_STATUS = 0;
    public static final int RECIPIENT_STATUS = 1;
    public static final int SENDER_LOCATION_STATUS = 2;
    public static final int RECIPIENT_LOCATION_STATUS = 3;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private ActivityChatBinding chatBinding;
    private String Child;
    private String Sender;
    private String Reciepent;
    private SetRecyclerAdapter setrecycleradapter;
    private DatabaseReference myRef;
    private ChildEventListener mChild;
    private ChatRecyclerviewAdapter adapter;
    private String Sendername;
    private String Recipientname;

    public ChatViewmodel(ChatActivity chatActivity, final FirebaseAuth firebaseAuth, FirebaseAuth.AuthStateListener mAuthlistner, final ActivityChatBinding chatBinding, final DatabaseReference myRef, final SetRecyclerAdapter setrecycleradapter, final String child, final String sender, final String reciepent, final ChatRecyclerviewAdapter adapter) {
        this.context = chatActivity;
        this.firebaseAuth = firebaseAuth;
        this.mAuthlistner = mAuthlistner;
        this.chatBinding = chatBinding;
        this.setrecycleradapter = setrecycleradapter;
        this.Child = child;
        this.Sender = sender;
        this.Reciepent = reciepent;
        this.myRef = myRef;
        this.adapter = adapter;
        hideKeyboard();
        ChatAcitvate();
        chatBinding.chat.scrollToPosition(adapter.getItemCount() - 1);

        final FirebaseDatabase databas= FirebaseDatabase.getInstance();
        DatabaseReference notiref = databas.getReference("User");
        notiref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);

                    if(snapshot.getKey().equals(sender)) {
                        Sendername= user.getmName();

                    }
                    if(snapshot.getKey().equals(reciepent)) {
                        Recipientname= user.getmName();

                    }

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public ChatViewmodel() {

    }

    public void ChatAcitvate() {
        mChild = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {

                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getmSender().equals(Sender) && message.getmLocationurl().equals("no")) {
                        message.setRecipientOrSenderStatus(SENDER_STATUS);
                    } else if (message.getmSender().equals(Reciepent) && message.getmLocationurl().equals("no")) {
                        message.setRecipientOrSenderStatus(RECIPIENT_STATUS);
                    } else if (message.getmSender().equals(Sender) && message.getmLocationurl().equals("yes")) {
                        message.setRecipientOrSenderStatus(SENDER_LOCATION_STATUS);
                    } else

                        message.setRecipientOrSenderStatus(RECIPIENT_LOCATION_STATUS);


                    setrecycleradapter.setAdapterData(message);
                    chatBinding.chat.scrollToPosition(adapter.getItemCount() - 1);


                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        myRef.child(Child).addChildEventListener(mChild);

    }

    @Override
    public void readySnap(Double latitude, Double longitude, String child, final String sender, final String recipient, String Staticmapimage, String sendername, long timestamp) {
        this.Child = child;

        final Map<String, String> message = new HashMap<>();
        message.put("Sender", sender);
        message.put("Reciepent", recipient);
        message.put("Message", Staticmapimage);
        message.put("Locationurl", "yes");
        message.put("Latitude", String.valueOf(latitude));
        message.put("Longitude", String.valueOf(longitude));
        message.put("Time", String.valueOf(Calendar.getInstance().getTime()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chat");
        myRef.child(child).push().setValue(message);


        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Notification");
        Random random = new Random();
        int ran = random.nextInt(2000);
        String chatkey = dref.push().getKey();
        Map<String, String> notification = new HashMap<>();
        notification.put("Key", Child);
        notification.put("Recipient", recipient);
        notification.put("Sender", sender);
        notification.put("Chat", "Location shared");
        notification.put("TimeStamp", String.valueOf(Calendar.getInstance().getTime()));
        notification.put("NotiKey", chatkey);
        notification.put("NotificationId", String.valueOf(ran));
        notification.put("NameofSender", sendername);
        dref.child(chatkey).setValue(notification);

    }

    public ChildEventListener getchatListner() {
        return mChild;

    }

    public void LocationSend() {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("firebasechild", Child);
        intent.putExtra("firebasesender", Sender);
        intent.putExtra("firebaserecipient", Reciepent);
        intent.putExtra("sendername",Sendername);
        context.startActivity(intent);


    }

    public void Send() {

        Map<String, String> message = new HashMap<>();
        if (!TextUtils.isEmpty(chatBinding.chatText.getText().toString())) {
            message.put("Sender", Sender);
            message.put("Reciepent", Reciepent);
            message.put("Message", chatBinding.chatText.getText().toString().trim());
            message.put("Locationurl", "no");
            message.put("Latitude", "no latitude");
            message.put("Longitude", "no longitude");
            message.put("Time", String.valueOf(Calendar.getInstance().getTime()));
            Log.d("hbnn",Sendername);
            myRef.child(Child).push().setValue(message);

            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Notification");
            Random random = new Random();
            int ran = random.nextInt(2000);
            String chatkey = dref.push().getKey();
            Map<String, String> notification = new HashMap<>();
            notification.put("Key", Child);
            notification.put("Recipient", Reciepent);
            notification.put("Sender", Sender);
            notification.put("Chat", chatBinding.chatText.getText().toString());
            notification.put("TimeStamp", String.valueOf(Calendar.getInstance().getTime()));
            notification.put("NotiKey", chatkey);
            notification.put("NotificationId", String.valueOf(ran));
            notification.put("NameofSender", Sendername);
            dref.child(chatkey).setValue(notification);


        }

        chatBinding.chatText.setText("");


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(chatBinding.chatText.getWindowToken(), 0);
    }

    public interface SetRecyclerAdapter {
        void setAdapterData(Message message);
    }


}
