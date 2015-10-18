package com.hoolai.websocket.hangUp.bo;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import com.hoolai.websocket.hangUp.util.RandomUtil;

public class Player {

	private final int id;
	private final Session session;
	
	private long nextUpdateTime;
	
	private int countdown;
	private boolean isSearching;
	private boolean isFighting;
	private boolean isFinishedFight;
	
	private Officer officer;
	private Officer npcOfficer;
	
	private BattleResult battleResult;
	private Iterator<BattleRound> battleResultRounds;
	
	public Player(int id, Session session){
		this.id = id;
		this.session = session;
	}
	
	public int getId() {
		return id;
	}
	
	public void searching(){
		assert isFinishedFight == true;
		isFinishedFight = false;
		
		assert isSearching == false;
		isSearching = true;
		
		countdown = 5 + RandomUtil.nextInt(5);
		sendMessage(String.format("{'type':'searching', 'countdown':%s}", countdown));
		nextUpdateTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(countdown);
	}
	
	public boolean isNeedUpdate(){
		long now = System.currentTimeMillis();
		return now >= nextUpdateTime;
	}
	
	public Runnable updateTask() {
		return new Runnable(){
			@Override
			public void run(){
//				long now = System.currentTimeMillis();
//				if(now < nextUpdateTime){
//					return;
//				}
				
				if(isSearching){
					beginFight();
				}else if(isFighting){
					fighting();
				}else if(isFinishedFight){
					finishedFight();
					searching();
				}
			}
		};
	}
	
	private void beginFight(){
		assert isSearching == true;
		isSearching = false;
		
		assert isFighting == false;
		isFighting = true;
		
		officer = officer();
		npcOfficer = npcOfficer();
		sendMessage(String.format("{'type':'beginFight', 'attackerHp':'%s', 'defencerHp':'%s'}", officer.getHp(), npcOfficer.getHp()));
		
		battleResult = fight();
		battleResultRounds = battleResult.rounds();
		
		long now = System.currentTimeMillis();
		nextUpdateTime = now + TimeUnit.MILLISECONDS.toMillis(100);
	}
	
	private Officer npcOfficer(){
		return new Officer(/*"npc_"+id*/"def_0", 10*countdown + RandomUtil.nextInt(300));
	}
	
	private BattleResult fight(){
		return new BattleResult(officer, npcOfficer);
	}
	
	private void fighting(){
		assert isFighting == true;
		assert isFinishedFight == false;
		
		if(battleResultRounds.hasNext()){
			sendMessage(String.format("{'type':'fighting', 'round':%s}", battleResultRounds.next()));
			nextUpdateTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2);
		}else{
			isFighting = false;
			isFinishedFight = true;
			nextUpdateTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
		}
	}
	
	private void finishedFight(){
		sendMessage(String.format("{'type':'finishedFight', 'isAttackerWin':%s}", battleResult.isAttackerWin()));
	}
	
	protected void sendMessage(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException ioe) {
            CloseReason cr = new CloseReason(CloseCodes.CLOSED_ABNORMALLY, ioe.getMessage());
            try {
                session.close(cr);
            } catch (IOException ioe2) {
                // Ignore
            }
        }
    }

	public Officer officer() {
		return new Officer(/*"player_"+id*/"att_0", 100 + RandomUtil.nextInt(300));
	}
	
}
