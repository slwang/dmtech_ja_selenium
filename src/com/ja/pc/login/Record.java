package com.ja.pc.login;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Record {
	static WebDriver webDriver = null;
	static String  jaUrl = "http://192.168.2.6:8096/";
	static String chromeDriverPath = "D:\\install\\selenium\\chromedriver2.25_win32\\chromedriver.exe";
	static String mobileValue ="13071175453";
	static String codeValue ="111111";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//login
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        
		webDriver = new ChromeDriver();
        webDriver.get(jaUrl);
        String url = webDriver.getCurrentUrl();
        System.out.println(url);
        WebElement loginHref = webDriver.findElement(By.linkText("用户登录"));
       
        loginHref.click();
        
        
      //得到当前窗口的句柄  
        String currentWindow = webDriver.getWindowHandle();  
        //得到所有窗口的句柄  
        Set<String> handles = webDriver.getWindowHandles();  
        Iterator<String> it = handles.iterator();  
        while(it.hasNext()){  
            String handle = it.next();  
            if(currentWindow.equals(handle)) continue;  
            WebDriver window = webDriver.switchTo().window(handle);  
            System.out.println("title,url = "+window.getTitle()+","+window.getCurrentUrl());  
        }  
        
       
        
        WebElement mobile = webDriver.findElement(By.id("mobile"));
        mobile.sendKeys(mobileValue);
        WebElement code = webDriver.findElement(By.id("code"));
        code.sendKeys(codeValue);
        WebElement loginButton = webDriver.findElement(By.id("submit"));
        loginButton.click();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//webDriver.close();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test()
	public void test() {
		
	}

}
