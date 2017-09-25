package com.wsl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String formatTime(String pattern, long timeMillis){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timeMillis));
	}
	
	public static String formatTime(SimpleDateFormat sdf, long timeMillis){
		return sdf.format(new Date(timeMillis));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(System.currentTimeMillis());
		String s = formatTime("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
		System.out.println(s);
	}

}
