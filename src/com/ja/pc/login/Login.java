package com.ja.pc.login;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Login {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver",
                "D:\\install\\selenium\\chromedriver2.25_win32\\chromedriver.exe");
        
		WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://192.168.2.6:8096/");
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
        mobile.sendKeys("13071175453");
        WebElement code = webDriver.findElement(By.id("code"));
        code.sendKeys("111111");
        WebElement loginButton = webDriver.findElement(By.id("submit"));
        loginButton.click();
        
        System.out.println("Hello World!");

	}

}
