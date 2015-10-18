package com.hoolai.websocket.light.program;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.hoolai.websocket.light.LightType;

public class LightLifeCycle extends Endpoint {

	private static final String START_TIME = "Start Time";
	private Session session;
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		addMessageHandler();
		recordStartTime();
		sendMsg(LightType.green.lightIndex()+":Just opened");
	}
	
	void addMessageHandler(){
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String msg) {
				if(msg.indexOf("xxx") != -1){
					throw new IllegalArgumentException("xxx not allowed!");
				}else if(msg.indexOf("close") != -1){
					sendMsg(LightType.red.lightIndex()+":Server closing after "+getConnectTime()+" s");
					closeSession();
				}else{
					sendMsg(LightType.green.lightIndex()+":Just processed a message");
				}
			}
		});
	}
	
	void recordStartTime(){
		session.getUserProperties().put(START_TIME, System.currentTimeMillis());
	}
	
	@Override
	public void onError(Session session, Throwable t) {
		sendMsg(LightType.yellow.lightIndex()+":Error: "+t.getMessage());
    }
	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Goodbye!"+this.getClass().getName());
	}
	
	void sendMsg(String msg){
		try {
			session.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	long getConnectTime(){
		return TimeUnit.MILLISECONDS.toSeconds(
				System.currentTimeMillis() - 
				(long)(session.getUserProperties().get(START_TIME)));
	}
	
	void closeSession(){
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
