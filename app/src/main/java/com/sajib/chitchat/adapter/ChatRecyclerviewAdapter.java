package com.sajib.chitchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sajib.chitchat.R;
import com.sajib.chitchat.activity.DirectionMap;
import com.sajib.chitchat.databinding.ActivityChatBinding;
import com.sajib.chitchat.databinding.ActivityChatReciepentBinding;
import com.sajib.chitchat.databinding.ActivityChatReciepentLocationimageBinding;
import com.sajib.chitchat.databinding.ActivityChatSenderBinding;
import com.sajib.chitchat.databinding.ActivityChatSenderLocationimageBinding;
import com.sajib.chitchat.databinding.ActivityUserLayoutBinding;
import com.sajib.chitchat.model.Message;
import com.sajib.chitchat.model.User;
import com.sajib.chitchat.viewmodel.Chat_reciepent_Viewmodel;
import com.sajib.chitchat.viewmodel.Chat_recipient_location_viewmodel;
import com.sajib.chitchat.viewmodel.Chat_sender_Viewmodel;
import com.sajib.chitchat.viewmodel.Chat_sender_location_viewmodel;
import com.sajib.chitchat.viewmodel.Userviewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sajib on 11-08-2016.
 */
public class ChatRecyclerviewAdapter extends RecyclerView.Adapter<ChatRecyclerviewAdapter.Myviewholder> {
    List<Message> data = Collections.emptyList();
    Context context;
    private static final int SENDER=0;
    private static final int RECIPIENT=1;
    private static final int SENDERLocation=2;
    private static final int RECIPIENTLocation=3;


    public ChatRecyclerviewAdapter(Context context, String uid) {
        data = new ArrayList<>();
        this.context = context;

    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case SENDER:
                ActivityChatSenderBinding Sbinding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_chat_sender, parent, false);
                return new SenderViewHolder(Sbinding);
            case RECIPIENT:
                ActivityChatReciepentBinding Rbinding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_chat_reciepent, parent, false);
                return new ReciepentViewholder(Rbinding);
            case SENDERLocation:
                ActivityChatSenderLocationimageBinding activityChatSenderLocationimageBinding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_chat_sender_locationimage, parent, false);
                return new SenderLoactionViewholder(activityChatSenderLocationimageBinding);
            case RECIPIENTLocation:
                ActivityChatReciepentLocationimageBinding activityChatReciepentLocationimageBinding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_chat_reciepent_locationimage, parent, false);
                return new ReciepentLocationViewholder(activityChatReciepentLocationimageBinding);
            default:
                ActivityChatSenderBinding SbindingDefault=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_chat_sender, parent, false);
                return new SenderViewHolder(SbindingDefault);

        }

    }

    @Override
    public void onBindViewHolder(Myviewholder holder, final int position) {
        Log.d("yo",data.get(position).getmMessage()+position);
        switch (holder.getItemViewType())
        {
            case SENDER:
                SenderViewHolder senderViewHolder=(SenderViewHolder) holder;
                configureSenderView(senderViewHolder,position);
                break;
            case RECIPIENT:
                ReciepentViewholder reciepentViewholder=(ReciepentViewholder) holder;
                configureRecipientView(reciepentViewholder,position);
                break;
            case SENDERLocation:
                SenderLoactionViewholder senderViewHolder1=(SenderLoactionViewholder) holder;
                configureSenderLocationView(senderViewHolder1,position);
                break;
            case RECIPIENTLocation:
                ReciepentLocationViewholder reciepentViewholder1=(ReciepentLocationViewholder) holder;
                configureRecipientLocationView(reciepentViewholder1,position);
                reciepentViewholder1.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message=data.get(position);
                        Intent data=new Intent(context, DirectionMap.class);
                        data.putExtra("latitude",message.getmLatitude());
                        data.putExtra("longitude",message.getmLongitude());
                        data.putExtra("sender",message.getmSender());
                        data.putExtra("recipient",message.getmReciepent());
                        context.startActivity(data);

                        /*String geoCode = "google.navigation:q=" + message.getmLatitude() + ","
                                + message.getmLongitude();
                        Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(geoCode));
                        context.startActivity(sendLocationToMap);*/
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getRecipientOrSenderStatus()==SENDER)
        {
            return SENDER;
        }
        else if(data.get(position).getRecipientOrSenderStatus()==SENDERLocation)
        {
            return SENDERLocation;
        }
        else if(data.get(position).getRecipientOrSenderStatus()==RECIPIENTLocation)
        {
            return RECIPIENTLocation;
        }
        else
            return RECIPIENT;

    }

    public class Myviewholder extends RecyclerView.ViewHolder {

        public Myviewholder(View itemView) {
            super(itemView);
        }
    }

        public class SenderViewHolder extends Myviewholder
        {
            ActivityChatSenderBinding binding;

            public SenderViewHolder(ActivityChatSenderBinding binding) {
                super(binding.sender);
                this.binding=binding;
            }

            public ActivityChatSenderBinding getBinding() {
                return binding;
            }

            void bindRecyclerviewModel(Message message) {
                if (binding.getViewmodel() == null) {
                    binding.setViewmodel(new Chat_sender_Viewmodel(context, message));
                } else {
                    binding.getViewmodel().setData(message);
                }

            }
        }

        public class ReciepentViewholder extends Myviewholder {
            ActivityChatReciepentBinding binding;

            public ReciepentViewholder(ActivityChatReciepentBinding binding) {
                super(binding.reciepent);
                this.binding = binding;
            }

            public ActivityChatReciepentBinding getBinding() {
                return binding;
            }

            void bindRecyclerviewModel(Message message) {
                if (binding.getViewmodel() == null)
                {
                    binding.setViewmodel(new Chat_reciepent_Viewmodel(context, message));
                }
                else
                {
                    binding.getViewmodel().setData(message);
                }

            }
        }

            public class ReciepentLocationViewholder extends Myviewholder {
                ActivityChatReciepentLocationimageBinding binding;

                public ReciepentLocationViewholder(ActivityChatReciepentLocationimageBinding binding) {
                    super(binding.reciepent);
                    this.binding = binding;
                }

                public ActivityChatReciepentLocationimageBinding getBinding() {
                    return binding;
                }

                void bindRecyclerviewModel(Message message) {
                    if (binding.getViewmodel() == null) {
                        binding.setViewmodel(new Chat_recipient_location_viewmodel(context, message));
                    } else {
                        binding.getViewmodel().setData(message);
                    }

                }
            }
                public class SenderLoactionViewholder extends Myviewholder {
                    ActivityChatSenderLocationimageBinding binding;
                    public SenderLoactionViewholder(ActivityChatSenderLocationimageBinding binding) {
                        super(binding.reciepent);
                        this.binding=binding;
                    }

                    public ActivityChatSenderLocationimageBinding getBinding() {
                        return binding;
                    }

                    void bindRecyclerviewModel(Message message) {
                        if (binding.getViewmodel() == null) {
                            binding.setViewmodel(new Chat_sender_location_viewmodel(context, message));
                        } else
                        {
                            binding.getViewmodel().setData(message);
                        }

                    }
        }

    private void configureSenderView(SenderViewHolder viewHolderSender, int position) {

        Message senderFireMessage=data.get(position);
        viewHolderSender.bindRecyclerviewModel(senderFireMessage);
    }

    private void configureRecipientView(ReciepentViewholder viewHolderRecipient, int position) {
        Message recipientFireMessage=data.get(position);
        viewHolderRecipient.bindRecyclerviewModel(recipientFireMessage);
    }

    private void configureSenderLocationView(SenderLoactionViewholder viewHolderSenderLocation, int position) {
        Message senderLocationFireMessage=data.get(position);
        viewHolderSenderLocation.bindRecyclerviewModel(senderLocationFireMessage);
    }

    private void configureRecipientLocationView(ReciepentLocationViewholder viewHolderRecipientLocation, int position) {
        Message recipientLocationFireMessage=data.get(position);
        viewHolderRecipientLocation.bindRecyclerviewModel(recipientLocationFireMessage);
    }

    public void setData(Message message) {
        data.add(message);
        notifyItemInserted(getItemCount()-1);
    }



    public List<Message> getData() {
        return data;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        data.clear();
    }
}
