package com.m.test.my;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.m.test.base.MBaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyTest extends MBaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//我的
        driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_001_tradeRecord() throws InterruptedException {
		WebElement tradeRecord =  driver.findElement(By.xpath("/html/body/div[2]/ul/li[1]"));
		tradeRecord.click();
		
		List<WebElement> list = driver.findElements(By.xpath("/html/body/div[2]/div[2]/ul/li"));
		for (WebElement we: list){
			Thread.sleep(1000);
			we.click();
		}
		
		WebElement returnBtn = driver.findElement(By.xpath("/html/body/div[1]/div/span"));
		returnBtn.click();
	}
	
	@Test
	public void test_002_coupons() throws InterruptedException {
		WebElement coupons =  driver.findElement(By.xpath("/html/body/div[2]/ul/li[2]"));
		coupons.click();
		Thread.sleep(1000);
		
		WebElement returnBtn = driver.findElement(By.xpath("/html/body/div[1]/div/span"));
		returnBtn.click();
	}
	
	@Test
	public void test_003_myAccount() throws InterruptedException {
		WebElement myaccount =  driver.findElement(By.xpath("/html/body/div[2]/ul/li[3]"));
		assertEquals("我的账户",myaccount.getText());
	}

	@Test
	public void test_004_message() throws InterruptedException {
		WebElement message =  driver.findElement(By.xpath("/html/body/div[2]/ul/li[4]"));
		message.click();
		Thread.sleep(1000);
		
		WebElement returnBtn = driver.findElement(By.xpath("/html/body/div[1]/div/span"));
		returnBtn.click();
	}
	
	@Test
	public void test_005_home() throws InterruptedException {
		WebElement home =  driver.findElement(By.xpath("/html/body/div[2]/ul/li[5]"));
		home.click();
		Thread.sleep(1000);
		
		/*WebElement returnBtn = driver.findElement(By.xpath("/html/body/div[1]/div/span"));
		returnBtn.click();*/
	}
	
	
	
}
