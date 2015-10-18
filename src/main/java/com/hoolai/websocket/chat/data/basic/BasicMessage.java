package com.hoolai.websocket.chat.data.basic;

import com.hoolai.websocket.chat.data.ChatMessage;

public abstract class BasicMessage extends ChatMessage {
	
    protected String dataString;

    BasicMessage(String type, String dataString) {
        super(type);
        this.dataString = dataString;
    }

    public String getData() {
        return this.dataString;
    }
    
}
