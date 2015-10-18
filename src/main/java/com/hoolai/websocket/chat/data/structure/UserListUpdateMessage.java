package com.hoolai.websocket.chat.data.structure;

import java.util.List;

import com.hoolai.websocket.chat.data.ChatMessage;

public class UserListUpdateMessage extends StructuredMessage {

    public UserListUpdateMessage(List<String> usernames) {
        super(ChatMessage.USERLIST_UPDATE, usernames);
    }

    public List<String> getUserList() {
        return super.dataList;
    }

}
