package com.hoolai.websocket.hangUp.server;

import java.io.EOFException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.hoolai.websocket.hangUp.bo.Player;

@ServerEndpoint("/hangUp")
public class HangUpServerEndpoint {

	private static final AtomicInteger idGenerator = new AtomicInteger(0);

	private final int id;
	private Player player;

	public HangUpServerEndpoint() {
		this.id = idGenerator.getAndIncrement();
	}

	@OnOpen
	public void onOpen(Session session) {
		player = new Player(id, session);
		Global.addPlayer(player);
		player.searching();
	}

	@OnMessage
	public void onMessage(String message) {
		//System.out.println("HangUp-【" + message + "】");
	}

	@OnClose
	public void onClose() {
		Global.removePlayer(player);
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		// Most likely cause is a user closing their browser. Check to see if
		// the root cause is EOF and if it is ignore it.
		// Protect against infinite loops.
		int count = 0;
		Throwable root = t;
		while (root.getCause() != null && count < 20) {
			root = root.getCause();
			count++;
		}
		if (root instanceof EOFException) {
			// Assume this is triggered by the user closing their browser and
			// ignore it.
		} else {
			throw t;
		}
	}

}
