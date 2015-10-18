package com.hoolai.websocket.hangUp.bo;

public class Officer {

	private final String id;
	private int hp;
	private boolean isDead;
	
	public Officer(String id, int hp) {
		this.id = id;
		this.hp = hp;
	}
	
	public void descHp(int descHp){
		hp = Math.max(hp -descHp , 0);
		if(hp == 0){
			isDead = true;
		}
	}
	
	public boolean isDead(){
		return isDead;
	}
	
	public String getId() {
		return id;
	}
	
	public int getHp() {
		return hp;
	}

}
