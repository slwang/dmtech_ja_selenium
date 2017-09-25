package com.dmcc.login;

import org.openqa.selenium.Cookie;



public class Constants {
	public String TEST_ENV = null;//test integration simulate online
	public static String appKey;
	public static String appSecret;
	public static String title;
	public static String content_blackWords;
	public static String existSmsId;
	public static String noExistSmsId;
	public static String mailType;
	public static String referer;
	public static String url;
	public static Cookie adminCk = new Cookie("JSESSIONID", "4f8a8ccbbf5964e8f3d81e74dd6f");
}