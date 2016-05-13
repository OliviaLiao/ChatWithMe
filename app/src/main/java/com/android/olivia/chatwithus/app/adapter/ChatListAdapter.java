package com.android.olivia.chatwithus.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.olivia.chatwithus.R;
import com.android.olivia.chatwithus.app.models.ChatMessage;
import com.android.olivia.chatwithus.app.models.UserType;
import com.android.olivia.chatwithus.app.views.BillDetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by olivia on 5/4/16.
 */
public class ChatListAdapter extends BaseAdapter {
    private ArrayList<ChatMessage> mChatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");


    public ChatListAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.mChatMessages = chatMessages;
        this.context = context;

    }

    @Override
    public int getCount() {
        return mChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ChatMessage message = mChatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);
                holder1 = new ViewHolder1();
                holder1.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder1.container = (LinearLayout) v.findViewById(R.id.container);
                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();
            }
            if (holder1.container != null) {
                holder1.container.removeAllViews();
            }
            if (message.getMessageText().toLowerCase().contains("bill")) {
                holder1.messageTextView.setText(R.string.auto_response);
                BillDetailView bill = new BillDetailView(context, null);
                bill.setBillDetail("7285 54 88555544 44", "60.85", "8.40");
                holder1.container.addView(bill);
            } else {
                holder1.messageTextView.setText(message.getMessageText());
            }
        } else if (message.getUserType() == UserType.COMPANY) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);
                holder2 = new ViewHolder2();
                holder2.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                v.setTag(holder2);
            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }
            holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = mChatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public LinearLayout container;
    }

    private class ViewHolder2 {
        public TextView messageTextView;
        public TextView timeTextView;

    }
}
