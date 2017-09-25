package com.dmcc.login;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DMCCLogin {
	private static String browser="chrome";
	private static WebDriver driver = null;
	private static Cookie AdminCk = new Cookie("JSESSIONID", "f3913a40811c9751d43f4f2cdc47");  //username:admin
	private static Cookie SignatureCk = new Cookie("JSESSIONID", "f8cea9e8a4b93c69ed364db736cd"); //fund
	private static Cookie SignatureCk2 = new Cookie("JSESSIONID", "f91356b1ee14c2a94e06c2e4dd8b"); //阿玲
	private static Cookie ApprovalCk = new Cookie("JSESSIONID", "4db3c14e627c309d283e6685f628"); //anjingxing
	private static Cookie AssessmentCk = new Cookie("JSESSIONID", "f3e273e460d2cc404c2bde87b42b"); //wukeke
	private static Cookie FirstJudgmentCk = new Cookie("JSESSIONID", "fc552c3848420080ff304e8e7dd7"); //wujia
	private static Cookie FinalJudgmentCk = new Cookie("JSESSIONID", "4e1d1f9376386d012b1c13d3e25e"); //chengjinghuo
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		login("admin", AdminCk);//超级用户登录
//		login("Signature", SignatureCk);//汇签角色登录
//		login("Assessment", SignatureCk2);//评估角色登录
//		login("Approval", ApprovalCk);//审贷会角色登录
		login("Assessment", AssessmentCk);//评估角色登录

//		login("FirstJudgment", FirstJudgmentCk);//初审角色登录
//		login("FinalJudgment", FinalJudgmentCk);//终审角色登录
	}
	
	public static void login(String role, Cookie ck){
		if ("chrome".compareToIgnoreCase(browser) == 0){
			String proPath = System.getProperty("user.dir");
			System.setProperty("webdriver.chrome.driver",
					proPath + "/chromedriver2.25/chromedriver.exe");

			driver = new ChromeDriver();
		}else if ("firefox".compareToIgnoreCase(browser) == 0){
			driver = new FirefoxDriver();
		}
		else{
			System.out.println("没有匹配的浏览器");
			return;
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);//设置了这个，很多sleep可以去了， 但是alert之前必须sleep， 留着的sleep 是必须有的
		driver.get("http://192.168.2.10:8080/dmcc/dashboard");//不能少
		driver.manage().deleteAllCookies(); // 删除cookie里的内容
		driver.manage().addCookie(ck);
		
		driver.get("http://192.168.2.10:8080/dmcc/dashboard");//不能少
	}

}
