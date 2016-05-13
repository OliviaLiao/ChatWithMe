package com.android.olivia.chatwithus.app.models;

/**
 * Created by olivia on 5/4/16.
 */
public class ChatMessage {
    private String mMessageText;
    private UserType mUserType;
    private Long mMessageTime;

    public String getMessageText() {
        return mMessageText;
    }

    public void setMessageText(String messageText) {
        this.mMessageText = messageText;
    }

    public UserType getUserType() {
        return mUserType;
    }

    public void setUserType(UserType userType) {
        this.mUserType = userType;
    }

    public long getMessageTime() {
        return mMessageTime;
    }

    public void setMessageTime(long messageTime) {
        this.mMessageTime = messageTime;
    }
}
