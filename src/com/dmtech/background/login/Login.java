package com.dmtech.background.login;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * chrome firefox 都可以使用，通过cookie绕过图形验证码
 * @author wsl
 *
 */
public class Login {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriver driver = new FirefoxDriver();
		
		
		Cookie ck = new Cookie("JSESSIONID", "89fc87a197fa7f1ed6c23cd852fd"); //登陆豆蔓后台获取cookie后拷贝到这里, 目的，绕过登陆的图形验证码
		driver.get("http://192.168.2.8:8080");
		driver.manage().deleteAllCookies(); // 删除cookie里的内容
		driver.manage().addCookie(ck);
		driver.get("http://192.168.2.8:8080/CreditManager");
		Thread.sleep(1000);
		

	}

}
