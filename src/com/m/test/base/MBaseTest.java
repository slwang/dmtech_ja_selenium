package com.m.test.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MBaseTest {
	protected static WebDriver driver = null;
	protected static Boolean isNeedLogin = false; //默认不登录
	protected static Boolean isLogin = false;
	protected static String loginName ="13000000001";
	protected static String loginPwd ="Ww111111";

	@BeforeClass
	public static void setUpBeforeClassSuper() throws Exception {
		System.out.println("YSTestBase @BeforeClass");
		openMobileBrowser();
		System.out.println("isLogin: " + isLogin);
		if (isNeedLogin){
			if (!isLogin){
				loginM();
				isLogin = true;
				System.out.println("isLogin: " + isLogin);
			}
		}
	}


	private static void openMobileBrowser(){
		System.out.println("M App AutoTest");
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");


		Map<String, String> mobileEmulation = new HashMap<String, String>();
		//mobileEmulation.put("deviceName", "Google Nexus 5");     
		mobileEmulation.put("deviceName", "Apple iPhone 6");    
		Map<String, Object> chromeOptions = new HashMap<String, Object>();     
		chromeOptions.put("mobileEmulation", mobileEmulation);     
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();       
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);       
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		Navigation navigation=driver.navigate();
		//navigation.to("https://m.baidu.com/");
		navigation.to("http://www.cailu360.com:8808");
		String title=driver.getTitle();
		System.out.println("title:"+title);
		driver.findElement(By.xpath("//*[@id='newHandTc']/div[2]/a")).click();
	}

	public static void loginM(){
		//我的
		driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();

		WebElement  user = driver.findElement(By.name("loginName"));
		user.clear();
		user.sendKeys(loginName);

		WebElement  password = driver.findElement(By.name("loginPwd"));
		password.clear();
		password.sendKeys(loginPwd);

		WebElement  doLogin = driver.findElement(By.id("doLogin"));
		doLogin.click();
	}  

	public static void loginOutM(){
		//我的
		driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();
		WebElement sysSetup = driver.findElement(By.xpath(" /html/body/div[2]/ul/li[5]"));
		sysSetup.click();

		WebElement  loginOut = driver.findElement(By.partialLinkText("退出登录"));
		loginOut.click();
	}  
}
