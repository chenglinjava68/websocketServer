package com.hoolai.websocket.hangUp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String format(long time){
		return DF.format(new Date(time));
	}
	
}
