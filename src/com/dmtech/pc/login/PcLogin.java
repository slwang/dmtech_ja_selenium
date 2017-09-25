package com.dmtech.pc.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PcLogin {
	public static void  main(String[] arg) throws InterruptedException{
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.cailu360.com:8808");
		Thread.sleep(1000);
		WebElement loginPage = driver.findElement(By.linkText("登录"));
		loginPage.click();
		Thread.sleep(1000);
		
		WebElement userName = driver.findElement(By.id("username"));
		userName.sendKeys("13071175453");
		WebElement password = driver.findElement(By.id("pwd"));
		password.sendKeys("Wsl111111");
		WebElement login = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/dl[5]/input"));
		login.click();
		Thread.sleep(5000);
		
		WebElement info = driver.findElement(By.xpath("/html/body/div[1]/div/div/p[1]/a"));
		info.click();
		
		
		
				
		
	}

}
