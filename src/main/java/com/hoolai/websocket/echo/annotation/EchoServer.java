package com.hoolai.websocket.echo.annotation;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class EchoServer {
	
	private int count;

	@OnMessage
	public String echo(String msg){
		count++;
		return "I already got "+count+" message, 【"+msg+"】"+this;
	}
	
}
