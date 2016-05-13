package com.android.olivia.chatwithus.app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.olivia.chatwithus.R;
import com.android.olivia.chatwithus.app.Utils;
import com.android.olivia.chatwithus.app.adapter.ChatListAdapter;
import com.android.olivia.chatwithus.app.models.ChatMessage;
import com.android.olivia.chatwithus.app.models.UserType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main_activity";
    private ListView mChatListView;
    private EditText mChatEditText;
    private static ArrayList<ChatMessage> mChatMessage;
    private ImageView mEnterChatView;
    private ChatListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setStatusBarHeight(getStatusBarHeight());

        mChatMessage = new ArrayList<>();
        mChatListView = (ListView) findViewById(R.id.chat_list_view);
        mChatEditText = (EditText) findViewById(R.id.chat_edit_text1);
        mEnterChatView = (ImageView) findViewById(R.id.enter_chat1);
        mListAdapter = new ChatListAdapter(mChatMessage, this);
        mChatListView.setAdapter(mListAdapter);
        mChatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mChatListView.setSelection(mListAdapter.getCount() - 1);

            }
        });

        mChatEditText.setOnKeyListener(keyListener);
        mEnterChatView.setOnClickListener(clickListener);
        mChatEditText.addTextChangedListener(watcher1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (readArray(this) != null && readArray(this).size() != 0) {
            mChatMessage.clear();
            mChatMessage.addAll(readArray(this));
            mListAdapter.notifyDataSetChanged();
        }
    }

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mEnterChatView) {
                sendMessage(mChatEditText.getText().toString(), UserType.COMPANY);
            }
            mChatEditText.setText("");
        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!mChatEditText.getText().toString().equals("")) {
                mEnterChatView.setImageResource(R.drawable.ic_chat_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                mEnterChatView.setImageResource(R.drawable.ic_chat_send);
            } else {
                mEnterChatView.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                EditText editText = (EditText) v;
                if (v == mChatEditText) {
                    sendMessage(editText.getText().toString(), UserType.COMPANY);
                }
                mChatEditText.setText("");
                return true;
            }
            return false;
        }
    };

    private void sendMessage(final String messageText, final UserType userType) {
        if (messageText.trim().length() == 0) {
            return;
        }
        final ChatMessage message = new ChatMessage();
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());
        mChatMessage.add(message);

        if (mListAdapter != null)
            mListAdapter.notifyDataSetChanged();

        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable() {
            @Override
            public void run() {
                final ChatMessage message = new ChatMessage();
                message.setMessageText(messageText);
                message.setUserType(UserType.SELF);
                message.setMessageTime(new Date().getTime());
                mChatMessage.add(message);

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveArray(this);
    }

    public static boolean saveArray(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mChatMessage);
        editor.putString(TAG, json);
        return editor.commit();
    }

    public ArrayList<ChatMessage> readArray(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        Type type = new TypeToken<ArrayList<ChatMessage>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
