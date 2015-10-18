package com.hoolai.websocket.hangUp.bo;

import com.hoolai.websocket.hangUp.util.RandomUtil;

public class BattleAction {

	private final String attackerId;
	private final String defencerId;
	private final int defencerHp;
	private final int descHp;
	private final int leftHp;
	
	public BattleAction(Officer attacker, Officer defencer){
		this.attackerId = attacker.getId();
		this.defencerId = defencer.getId();
		this.defencerHp = defencer.getHp();
		this.descHp = 10 + RandomUtil.nextInt(100);
		defencer.descHp(descHp);
		this.leftHp = defencer.getHp();
	}

	@Override
	public String toString() {
//		return "{'attacker':"+attacker.getId()+", 'defencer':"+defencer.getId()
//				+", 'defencerHp':"+defencerHp+", 'descHp':"+descHp+", 'leftHp':"+defencer.getHp()
//				+(defencer.isDead()?", 'isDead':true":"")+"}";
		return String.format("{'attacker':'%s', 'defencer':'%s'"
				+", 'defencerHp':%s, 'descHp':%s, 'leftHp':%s"
				+(leftHp==0?", 'isDead':true":"")+"}", 
				attackerId, defencerId, defencerHp, descHp, leftHp);
	}
	
}
