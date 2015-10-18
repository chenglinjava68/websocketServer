package com.hoolai.websocket.chat.data.structure;

import java.util.ArrayList;
import java.util.List;

import com.hoolai.websocket.chat.data.ChatMessage;

public abstract class StructuredMessage extends ChatMessage {
	
    protected List<String> dataList = new ArrayList<>();

    protected StructuredMessage(String type) {
        super(type);
    }

    protected StructuredMessage(String type, List<String> dataList) {
        super(type);
        this.dataList = dataList;
    }

    public List<String> getList() {
        return dataList;
    }

}
