package com.m.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.m.test.base.MBaseTest;



public class RegisterTest extends MBaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		isNeedLogin = false;
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
	public void test_register() throws InterruptedException {
		//我的
		driver.findElement(By.xpath("/html/body/div[1]/ul/li[3]/p")).click();

		WebElement  register = driver.findElement(By.partialLinkText("注册"));
		register.click();
	}


}
