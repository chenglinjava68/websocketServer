package com.hoolai.websocket.hangUp.bo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hoolai.websocket.hangUp.util.RandomUtil;

public class BattleRound{

	private List<BattleAction> actions;
	
	public BattleRound(Officer attacker, Officer defencer){
		actions = new ArrayList<BattleAction>();
		for(int i=0;i<1+RandomUtil.nextInt(3);i++){
			if(attacker.isDead() || defencer.isDead()){
				break;
			}
			actions.add(new BattleAction(attacker, defencer));
			if(attacker.isDead() || defencer.isDead()){
				break;
			}
			actions.add(new BattleAction(defencer, attacker));
		}
	}
	
	public Iterator<BattleAction> actions(){
		return actions.iterator();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("[");
		for(BattleAction action:actions){
			sb.append(action);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

}
