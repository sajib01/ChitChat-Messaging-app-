package com.sajib.chitchat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajib.chitchat.activity.ChatActivity;
import com.sajib.chitchat.activity.UserActivity;
import com.sajib.chitchat.model.Message;
import com.sajib.chitchat.model.NotificationModel;
import com.sajib.chitchat.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by sajib on 24/9/16.
 */

public class NotificationService extends Service {

    final static String GROUP_KEY = "group_message";
    String message ;
    String NameofSender ;
    String Recipient ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Notification");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                builder.setSmallIcon(R.drawable.messenger);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                final Intent intent = new Intent(NotificationService.this, UserActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.messenger));

                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final NotificationModel notificationModel = postSnapshot.getValue(NotificationModel.class);
                    message = notificationModel.getmChat();
                    Recipient = notificationModel.getmRecipient();
                    NameofSender = notificationModel.getmNameofSender();

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (Recipient.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            inboxStyle.addLine(NameofSender + ": " + "   " + message);
                            i++;
                        }
                    }
                }
                if (i > 0&&Recipient.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    builder.setStyle(inboxStyle);
                    builder.setContentTitle("ChitChat");
                    builder.setContentText("You have " + i + " new message");
                    builder.setVibrate(new long[]{500L, 1000L});
                    builder.setAutoCancel(true);
                    notificationManager.notify(1235, builder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }


}