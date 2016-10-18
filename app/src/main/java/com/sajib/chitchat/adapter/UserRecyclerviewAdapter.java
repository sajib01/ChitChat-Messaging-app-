package com.sajib.chitchat.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.sajib.chitchat.R;
import com.sajib.chitchat.databinding.ActivityUserBinding;
import com.sajib.chitchat.databinding.ActivityUserLayoutBinding;
import com.sajib.chitchat.model.User;
import com.sajib.chitchat.viewmodel.Userviewmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sajib on 10-08-2016.
 */
public class UserRecyclerviewAdapter extends RecyclerView.Adapter<UserRecyclerviewAdapter.MyViewholder> {
    private List<User> data = Collections.emptyList();
    private Context context;
    private int position;
    private boolean hasdata;
    private String uid;
    private String sender;
    private User currentuser;
    private OnItemClickListner onItemClickListner;

    public UserRecyclerviewAdapter(Context context, String uid, OnItemClickListner onItemClickListner, String email) {
        data = new ArrayList<>();
        this.context = context;
        this.uid=uid;
        this.onItemClickListner=onItemClickListner;
        this.sender=email;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityUserLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_user_layout, parent, false);
        return new MyViewholder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
        final User user = data.get(position);
        holder.bindRecyclerviewModel(user);
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateClicked = null;
                Date dateUser = null;
                String dateString=user.getMcreationtime();
                String currentdateString=currentuser.getMcreationtime();
                DateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");

                try {

                    dateClicked=dateFormat.parse(dateString);
                    dateUser=dateFormat.parse(currentdateString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String Chatroot="";
                if(dateUser.compareTo(dateClicked)>0)
                {
                    String childname=currentuser.getmName() +"-"+user.getmName();
                    Chatroot=childname.replace(".","-");
                }
                if(dateUser.compareTo(dateClicked)<0)
                {
                    String childname=user.getmName() +"-"+currentuser.getmName();
                    Chatroot=childname.replace(".","-");

                }



                onItemClickListner.OnItemClick(Chatroot,uid,user.getmId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setCurrentuserdata(User currentuserdata) {
        this.currentuser = currentuserdata;
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        ActivityUserLayoutBinding binding;

        public MyViewholder(ActivityUserLayoutBinding binding) {
            super(binding.card);
            this.binding = binding;
        }

        public ActivityUserLayoutBinding getBinding() {
            return binding;
        }

        void bindRecyclerviewModel(User user) {
            if (binding.getViewmodel() == null) {
                binding.setViewmodel(new Userviewmodel(context, user));
            } else {
                binding.getViewmodel().setData(user);
            }

        }
    }

    public void setData(User user, int code) {
        if (data.size() > 0 && code == 1) {
            for (int i = 0; i < data.size(); i++) {
                if (user.getmName() != null) {
                    if (user.getmName().equals(data.get(i).getmName())) {
                        data.remove(i);
                        data.add(i, user);
                        position = i;
                        notifyItemChanged(position);
                    }
                }
            }
        }
        if (data.size() >= 0 && code == 0) {

            data.add(user);
            notifyDataSetChanged();


        }

    }

    public interface OnItemClickListner
    {
        void OnItemClick(String chatroot,String Senderid,String RecientId);
    }
}
