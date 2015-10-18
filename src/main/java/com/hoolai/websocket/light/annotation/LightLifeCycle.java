package com.hoolai.websocket.light.annotation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.hoolai.websocket.light.LightType;

@ServerEndpoint("/light")
public class LightLifeCycle {
	
	private static final String START_TIME = "Start Time";
	private Session session;
	
	@OnOpen
	public void open(Session session){
		this.session = session;
		recordStartTime();
		sendMsg(LightType.green.lightIndex()+":Just opened");
	}
	
	void recordStartTime(){
		session.getUserProperties().put(START_TIME, System.currentTimeMillis());
	}
	
	@OnMessage
	public void message(String msg){
		if(msg.indexOf("xxx") != -1){
			throw new IllegalArgumentException("xxx not allowed!");
		}else if(msg.indexOf("close") != -1){
			sendMsg(LightType.red.lightIndex()+":Server closing after "+getConnectTime()+" s");
			closeSession();
		}else{
			sendMsg(LightType.green.lightIndex()+":Just processed a message");
		}
	}
	
	@OnError
	public void error(Throwable t){
		sendMsg(LightType.yellow.lightIndex()+":Error: "+t.getMessage());
	}
	
	@OnClose
	public void close(){
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
