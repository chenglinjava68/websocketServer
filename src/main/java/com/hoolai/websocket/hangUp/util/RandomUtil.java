package com.hoolai.websocket.hangUp.util;

import java.util.Random;

public class RandomUtil {

	private static final Random RANDOM = new Random();
	
	public static int nextInt(int n){
		return RANDOM.nextInt(n);
	}
	
}
