package com.dmtech.background.robot;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class RobotByChromeDriver {

	static WebDriver driver = null;
	private static Cookie ck = new Cookie("JSESSIONID", "89fc87a197fa7f1ed6c23cd852fd"); //登陆豆蔓后台获取cookie后拷贝到这里, 目的，绕过登陆的图形验证码
	private static String robotName="auto自动11";// 名称
	private static String joinTimes = "3";//领取次数
	private static String loanType = "质押快投";//债权类型
	private static String cycle = "9个月";//期限 6  9  12
	private static String discountRate ="3";//小豆机器人贴息3
	
	private static String minPurchaseAmount ="100";//起购金额
	private static String purchaseCumulativeBase ="100";//购买累加基数
	private static String openingTime ="2017-7-20";//开放时间
	private static String endTime ="2017-7-20";//结束时间
	
	
	public static void main(String[] args) throws InterruptedException {
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");

		driver = new ChromeDriver();
		String robotUrl = "http://192.168.2.8:8080/CreditManager/cltBuyRobot/list";
		driver.get("http://192.168.2.8:8080");//不能少
		driver.manage().deleteAllCookies(); // 删除cookie里的内容
		driver.manage().addCookie(ck);
		driver.get(robotUrl);
		Thread.sleep(1000);
		
		WebElement addRobotBtn = driver.findElement(By.xpath("//*[@id='page-content']/div/div[2]/div[1]/div[2]/a"));  
		addRobotBtn.click();
		
		WebElement robotNameWE = driver.findElement(By.id("robotName"));
		robotNameWE.clear();
		robotNameWE.sendKeys(robotName);
		Thread.sleep(100);
		
		WebElement joinTimesWE = driver.findElement(By.id("joinTimes"));
		joinTimesWE.clear();
		joinTimesWE.sendKeys(robotName);
		Thread.sleep(100);
		
		
		Select firstTypeSE = new Select(driver.findElement(By.name("firstType")));
		firstTypeSE.selectByVisibleText(loanType);
		Thread.sleep(100);
		
		Select cycleSE = new Select(driver.findElement(By.name("cycle")));
		cycleSE.selectByVisibleText(cycle);
		Thread.sleep(100);
		
		WebElement discountRateWE = driver.findElement(By.id("discountRate"));
		discountRateWE.clear();
		discountRateWE.sendKeys(discountRate);
		Thread.sleep(100);
		
		WebElement minPurchaseAmountWE = driver.findElement(By.id("minPurchaseAmount"));
		minPurchaseAmountWE.clear();
		minPurchaseAmountWE.sendKeys(minPurchaseAmount);
		Thread.sleep(100);
		
		
		
		WebElement openingTimeWE = driver.findElement(By.id("openingTime"));
		openingTimeWE.clear();
		openingTimeWE.sendKeys(openingTime);
		Thread.sleep(100);
		
		WebElement endTimeWE = driver.findElement(By.id("endTime"));
		endTimeWE.clear();
		endTimeWE.sendKeys(endTime);
		Thread.sleep(1000);
		
		WebElement purchaseCumulativeBaseWE = driver.findElement(By.id("purchaseCumulativeBase"));
		purchaseCumulativeBaseWE.clear();
		purchaseCumulativeBaseWE.sendKeys(purchaseCumulativeBase);
		Thread.sleep(100);
		
		
		WebElement submitFormBtnWE = driver.findElement(By.id("submitFormBtn"));   
		submitFormBtnWE.click();
		Thread.sleep(1000);
		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		
		System.out.println("Success addRobot!");
	}
	
	

}
