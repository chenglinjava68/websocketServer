package com.hoolai.websocket.light;

public enum LightType{
	red(1),
	yellow(2),
	green(3),
	grey(0);
	
	private int lightIndex;
	LightType(int lightIndex){
		this.lightIndex = lightIndex;
	}
	public int lightIndex(){
		return lightIndex;
	}
}
