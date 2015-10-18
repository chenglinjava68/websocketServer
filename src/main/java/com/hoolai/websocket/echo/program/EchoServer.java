package com.hoolai.websocket.echo.program;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class EchoServer extends Endpoint{
	
	private int count;

	@Override
	public void onOpen(final Session session, EndpointConfig config) {
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String msg) {
				count++;
				String result = "I already got "+count+" message, 【"+msg+"】"+this;
				try {
					session.getBasicRemote().sendText(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
