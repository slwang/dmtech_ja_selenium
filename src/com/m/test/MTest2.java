package com.m.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteTouchScreen;

public class MTest2 {
	public static void main(String arg[]){
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
        WebDriver driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        Navigation navigation=driver.navigate();
        //navigation.to("https://m.baidu.com/");
        navigation.to("http://www.cailu360.com:8808");
        String title=driver.getTitle();
        System.out.println("title:"+title);
        driver.findElement(By.xpath("//*[@id='newHandTc']/div[2]/a")).click();
        
        
        //我的
        driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();
        
        WebElement  loginName = driver.findElement(By.name("loginName"));
        loginName.clear();
        loginName.sendKeys("13000000001");
        
        WebElement  loginPwd = driver.findElement(By.name("loginPwd"));
        loginPwd.clear();
        loginPwd.sendKeys("Ww111111");
        
        WebElement  doLogin = driver.findElement(By.id("doLogin"));
        doLogin.click();
        
        //我的
        driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();
        WebElement sysSetup = driver.findElement(By.xpath(" /html/body/div[2]/ul/li[5]"));
        sysSetup.click();
        
        WebElement  loginOut = driver.findElement(By.partialLinkText("退出登录"));
        loginOut.click();
        
        
        
        //navigation.forward();
	}  

	@Test
	public void test_m_baidu(){
		System.out.println("M App AutoTest");
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");

		
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", "Google Nexus 5");       
        Map<String, Object> chromeOptions = new HashMap<String, Object>();     
        chromeOptions.put("mobileEmulation", mobileEmulation);     
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();       
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);       
        WebDriver driver = new ChromeDriver(capabilities);
        Navigation navigation=driver.navigate();
        //navigation.to("https://m.baidu.com/");
        navigation.to("http://m.baidu.com");
        String title=driver.getTitle();
        System.out.println("title:"+title);
        driver.findElement(By.name("word")).sendKeys("手机模式");
	}
}
