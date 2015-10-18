package com.hoolai.websocket.hangUp.bo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BattleResult{

	private final List<BattleRound> rounds;
	private boolean isAttackerWin;
	
	public BattleResult(Officer attacker, Officer defencer){
		rounds = new ArrayList<BattleRound>();
		while(!attacker.isDead() && !defencer.isDead()){
			rounds.add(new BattleRound(attacker, defencer));
		}
		isAttackerWin = !attacker.isDead();
	}
	
	public Iterator<BattleRound> rounds(){
		return rounds.iterator();
	}
	
	public boolean isAttackerWin(){
		return isAttackerWin;
	}
	
}
