package com.hoolai.websocket.hangUp.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hoolai.websocket.hangUp.bo.Player;

public class Global {

	private static final int MAX_ROOM_NUM = 3000;
	private static final ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<Integer, Player>(MAX_ROOM_NUM);
	private static ScheduledExecutorService scheduledExecuto;
	private static ExecutorService executor;
	
	static{
		start();
	}
	
	public static void start(){
		assert scheduledExecuto == null;
		assert executor == null;
		
		scheduledExecuto = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		scheduledExecuto.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run(){
				updatePlayers();
			}
		}, 100, 100, TimeUnit.MILLISECONDS);
		executor = Executors.newCachedThreadPool();
	}
	
	private static void updatePlayers() {
		for(Player player:players.values()){
			if(!player.isNeedUpdate()){
				continue;
			}
			executor.execute(player.updateTask());
		}
	}
	
	public static void stop(){
		assert scheduledExecuto != null;
		assert executor != null;
		try {
			scheduledExecuto.shutdown();
			scheduledExecuto.awaitTermination(10, TimeUnit.SECONDS);
			
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		scheduledExecuto = null;
		executor = null;
		
	}
	
	public static boolean addPlayer(Player player){
		return players.putIfAbsent(player.getId(), player) == null;
	}
	
	public static boolean removePlayer(Player player){
		return players.remove(player.getId()) != null;
	}
	
}
