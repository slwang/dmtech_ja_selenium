package com.ys.test.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class YSTestBase {
	protected static WebDriver driver = null;
	protected static Boolean isLogin = false;
	protected static String loginName ="admin";
	protected static String loginPwd ="111111";
	protected static String projectName = "";
	protected static String tenantryName = "";
	public  YSTestBase(){
		//System.out.println("gouzao");
	}
	
	@BeforeClass
	public static void setUpBeforeClassSuper() throws Exception {
		System.out.println("YSTestBase @BeforeClass");
		System.out.println("isLogin: " + isLogin);
		if (!isLogin){
			loginYS();
			isLogin = true;
			System.out.println("isLogin: " + isLogin);
		}
	}

	@AfterClass
	public static void tearDownAfterClassSuper() throws Exception {
		System.out.println("YSTestBase @AfterClass");
	}
	
	public static void loginYS() throws InterruptedException{
		if (driver == null){
			String proPath = System.getProperty("user.dir");
			System.setProperty("webdriver.chrome.driver",
					proPath + "/chromedriver2.25/chromedriver.exe");

			driver = new ChromeDriver();
			//driver = new FirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		driver.get("http://192.168.2.240:8091/main");
		WebElement account = driver.findElement(By.id("account"));
		account.clear();
		account.sendKeys(loginName);

		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(loginPwd);

		WebElement loginbtn = driver.findElement(By.className("loginbtn"));
		loginbtn.click();
		
		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_default");

		WebElement welcome = driver.findElement(By.xpath("/html/body/div/div/b"));
		assertEquals(welcome.getText(), "您好，欢迎使用保险管理系统");

		driver.switchTo().defaultContent();
		//driver.switchTo().defaultContent();//只需一个就调回主页面
		
		isLogin = true;
	}
	
	public static void switchUser(String userName, String password) throws InterruptedException{
		driver.switchTo().frame("topFrame");
		WebElement exitBtn = driver.findElement(By.partialLinkText("退出"));
		exitBtn.click();
		loginName =userName; // "chushen";
		loginPwd =password; // "111111";
		YSTestBase.loginYS();
	}
	
	/**
	 * @param topMenu   产品中心
	 * @param leftMenu  项目管理
	 */
	public static void openMenu(String topMenu, String leftMenu){
		driver.switchTo().frame("topFrame");

		//关联散标债权， 处理列表
		List<WebElement> lilist=driver.findElements(By.xpath("/html/body/ul/li"));  
		Boolean flag = false;
		for(int r=0;r<lilist.size();r++){  
			String trText=lilist.get(r).getText();  
			System.out.println(trText);
			if(topMenu.compareToIgnoreCase(trText) == 0){ 
				lilist.get(r).click();
				flag = true;
				break;  
			}
		}
		if (!flag){
			fail("没有找到"+topMenu + "！");
		}

		driver.switchTo().defaultContent();
		driver.switchTo().frame("leftFrame");
		WebElement proManager = driver.findElement(By.linkText(leftMenu));
		proManager.click();

		driver.switchTo().defaultContent();
	}

	

}
