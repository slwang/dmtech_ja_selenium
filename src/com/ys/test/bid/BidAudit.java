package com.ys.test.bid;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ys.test.base.YSTestBase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BidAudit  extends YSTestBase{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.switchTo().defaultContent();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void test_001_yewuyuanCreateBid() throws Exception{
		//YSTestBase.switchUser("yewuyuan", "111111");
		CreateBid creat = new CreateBid();
		creat.test_createBid();
	}

	@Test
	public void test_002_firstAuditBid() throws InterruptedException{
		//YSTestBase.switchUser("chushen", "111111");
		audit("初审");
	}

	@Test
	public void test_003_finalAuditBid() throws InterruptedException{
		//YSTestBase.switchUser("zhongshen", "111111");
		audit("复审");
	}

	@Test
	public void test_004_submitRiskAudit() throws InterruptedException{
		//YSTestBase.switchUser("yewuyuan", "111111");
		YSTestBase.openMenu("产品中心", "标的列表");

		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80302");

		WebElement firstAudit = driver.findElement(By.partialLinkText("提交风控"));
		firstAudit.click();
		
		Thread.sleep(3000);
		driver.switchTo().alert().accept(); 
		driver.switchTo().defaultContent();
		
		Thread.sleep(3000);
		driver.switchTo().alert().accept(); 
		driver.switchTo().defaultContent();
	}
	
	private void audit(String auditNodeName) throws InterruptedException{
		YSTestBase.openMenu("产品中心", "标的列表");

		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80302");

		
		WebElement firstAudit = driver.findElement(By.partialLinkText(auditNodeName));
		firstAudit.click();
		Thread.sleep(1000);

		WebElement opinion = driver.findElement(By.name("opinion"));
		opinion.click();
		opinion.clear();
		/*opinion.sendKeys(auditNodeName + "通过！pass3333333333333333333333333333333344444444444444444444444444444555555555555555555555566666666666666666666677777777777777777777777777777777778888888888888888888888899999999999999999999900000000000abcda");
		String sOpinion= opinion.getAttribute("value");
		assertEquals(200,sOpinion.length());*/
		
		opinion.sendKeys(auditNodeName + "通过！");

		WebElement pass = driver.findElement(By.id("pass"));
		pass.click();
		
		
		Thread.sleep(3000);
		driver.switchTo().alert().accept(); 
		driver.switchTo().defaultContent();
		
	}

	
}
