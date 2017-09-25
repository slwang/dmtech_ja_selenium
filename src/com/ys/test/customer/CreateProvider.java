package com.ys.test.customer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ys.test.base.YSTestBase;
import com.ys.utils.DateUtil;

public class CreateProvider extends YSTestBase{
	
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
	public void test_createProject() throws InterruptedException {
		driver.switchTo().frame("topFrame");

		//关联散标债权， 处理列表
		List<WebElement> lilist=driver.findElements(By.xpath("/html/body/ul/li"));  
		Boolean flag = false;
		for(int r=0;r<lilist.size();r++){  
			String trText=lilist.get(r).getText();  
			System.out.println(trText);
			if("客户中心".compareToIgnoreCase(trText) == 0){ 
				lilist.get(r).click();
				flag = true;
				break;  
			}
		}
		if (!flag){
			fail("没有找到客户中心！");
		}

		driver.switchTo().defaultContent();
		driver.switchTo().frame("leftFrame");
		WebElement proManager = driver.findElement(By.linkText("供应商管理"));
		proManager.click();

		driver.switchTo().defaultContent();
		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_50202");
		WebElement create = driver.findElement(By.id("top_create"));
		create.click();
		
       //企业基本信息
		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		String providerName = "供应商"+DateUtil.getDateFormatHour();
		name.sendKeys(providerName);
		
		WebElement shortName = driver.findElement(By.id("shortName"));
		shortName.clear();
		shortName.sendKeys(providerName+"简称");

		Select industryTypeSel = new Select (driver.findElement(By.id("industryType")));
		if (industryTypeSel.getOptions().size()>1){
			industryTypeSel.selectByIndex(1);
		}else{
			fail("没有行业类型数据");
		}
		
		
		
		Select companyCertificateTypeSel = new Select (driver.findElement(By.id("companyCertificateType")));
		if (companyCertificateTypeSel.getOptions().size()>1){
			companyCertificateTypeSel.selectByIndex(0);//1 是选第二个
		}else{
			fail("没有证件类型");
		}
		
		
		/*WebElement organCode = driver.findElement(By.id("organCode"));//三证合一的时候有， companyCertificateTypeSel 选1的时候出现
		organCode.clear();
		organCode.sendKeys("122222");*/
		
		WebElement socialCode = driver.findElement(By.id("socialCode"));
		//socialCode.clear();
		socialCode.sendKeys(DateUtil.getDateFormatHour());
		
		WebElement registerMonery = driver.findElement(By.id("registerMonery"));
		registerMonery.clear();
		registerMonery.sendKeys("1000");
		
		Select scaleSel = new Select(driver.findElement(By.id("scale")));
		if (scaleSel.getOptions().size()>1){
			scaleSel.selectByIndex(1);
		}else{
			fail("没有纳税人规模数据");
		}
		
		WebElement idCardNum = driver.findElement(By.id("idCardNum"));
		idCardNum.clear();
		idCardNum.sendKeys("130102198902032029");
		
		WebElement gudongName = driver.findElement(By.id("gudongName"));
		gudongName.clear();
		gudongName.sendKeys("股东姓名");
		
		
		WebElement gudongPhone = driver.findElement(By.id("gudongPhone"));
		gudongPhone.clear();
		gudongPhone.sendKeys("13000000001");
		
		
		WebElement gudongAddress = driver.findElement(By.id("gudongAddress"));
		gudongAddress.clear();
		gudongAddress.sendKeys(providerName + "股东地址");
		
		//企业联系方式
		WebElement contact = driver.findElement(By.id("contact"));
		contact.clear();
		contact.sendKeys(providerName + "联系人");
		
		WebElement phone = driver.findElement(By.id("phonre"));
		phone.clear();
		phone.sendKeys("13000000002");
		
		WebElement remarkers = driver.findElement(By.id("remarkers"));
		remarkers.clear();
		remarkers.sendKeys(providerName + "企业备注信息");
		
		
		//企业法人信息
		
		WebElement legalMan = driver.findElement(By.id("legalMan"));
		legalMan.clear();
		legalMan.sendKeys(providerName + "法人");
		
		//银行卡基本信息
		
		WebElement cardNum = driver.findElement(By.id("cardNum"));
		cardNum.clear();
		cardNum.sendKeys("6225881065434322");
		
		WebElement cardNum1 = driver.findElement(By.id("cardNum1"));
		cardNum1.clear();
		cardNum1.sendKeys("6225881065434322");
		
		
		

		flag = false;
		WebElement btnCreate = null;
		List<WebElement> btn = driver.findElements(By.className("btn-80"));
		for (WebElement we : btn){
			if ("创建".compareToIgnoreCase(we.getAttribute("value")) == 0){
				btnCreate = we;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		btnCreate.click();

		
	}

}
