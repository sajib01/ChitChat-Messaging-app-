package com.sajib.chitchat.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sajib.chitchat.R;
import com.sajib.chitchat.model.Avatar;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sajib on 21/9/16.
 */

public class Avatar_recyclerviewAdapter extends RecyclerView.Adapter<Avatar_recyclerviewAdapter.Myviewholder>{


    Context context;
    List<Avatar> avatardata= Collections.emptyList();
    boolean alreadyselected;
    GetData data;

    public Avatar_recyclerviewAdapter(Context context, List<Avatar> avatardata,GetData data)
    {
        this.context=context;
        this.avatardata=avatardata;
        this.data=data;
    }
    @Override
    public Avatar_recyclerviewAdapter.Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.avatar_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final Avatar_recyclerviewAdapter.Myviewholder holder, final int position) {
        final Avatar avatar=avatardata.get(position);
        holder.bind(avatar);
        holder.profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,(holder.profileSelect.getVisibility()==View.VISIBLE)+"",Toast.LENGTH_SHORT).show();
                if(holder.profileSelect.getVisibility()==View.INVISIBLE&&!alreadyselected)
                {
                    Log.d("fcdds", String.valueOf(avatar.getCode()));
                    holder.profileSelect.setVisibility(View.VISIBLE);
                    avatar.setSelected(true);
                    alreadyselected=true;
                    data.Displaydata(avatar.getCode());
                }
                else if(holder.profileSelect.getVisibility()==View.VISIBLE&&alreadyselected)
                {
                    holder.profileSelect.setVisibility(View.INVISIBLE);
                    avatar.setSelected(false);
                    alreadyselected=false;
                }

            }
        });

        if(avatar.isSelected())
        {
            holder.profileSelect.setVisibility(View.VISIBLE);
        }

        else if(!avatar.isSelected())
        {
            holder.profileSelect.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return avatardata.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        ImageView profileView;
        ImageView profileSelect;
        CardView cardView;
        public Myviewholder(View itemView) {
            super(itemView);
                profileView= (ImageView) itemView.findViewById(R.id.profileView);
                profileSelect= (ImageView) itemView.findViewById(R.id.profileSelect);
            cardView= (CardView) itemView.findViewById(R.id.root);

        }

        public void bind(final Avatar avatar)

        {
            int resources = context.getResources().getIdentifier("drawable/avatar_" + avatar.getCode(), null, context.getPackageName());
            profileView.setImageDrawable(context.getResources().getDrawable(resources,null));
        }
    }

    public interface GetData
    {
        void Displaydata(int code);
    }

}
