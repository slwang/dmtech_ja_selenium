package com.ys.test.project;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.ys.test.constant.Constants;
import com.ys.utils.DateUtil;

public class CreateProject extends YSTestBase{

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
	public void test_createProject() throws Exception {
		driver.switchTo().frame("topFrame");

		//关联散标债权， 处理列表
		List<WebElement> lilist=driver.findElements(By.xpath("/html/body/ul/li"));  
		Boolean flag = false;
		for(int r=0;r<lilist.size();r++){  
			String trText=lilist.get(r).getText();  
			System.out.println(trText);
			if("产品中心".compareToIgnoreCase(trText) == 0){ 
				lilist.get(r).click();
				flag = true;
				break;  
			}
		}
		if (!flag){
			fail("没有找到产品中心！");
		}

		driver.switchTo().defaultContent();
		driver.switchTo().frame("leftFrame");
		WebElement proManager = driver.findElement(By.linkText("项目管理"));
		proManager.click();

		driver.switchTo().defaultContent();
		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80201");
		WebElement create = driver.findElement(By.id("top_create"));
		create.click();

		Select position = new Select(driver.findElement(By.id("position")));
		if (position.getOptions().size()<2){
			fail("地理位置下拉框为空");
		}
		position.selectByIndex(1);

		Select  belong = new Select(driver.findElement(By.id("belong")));
		if (belong.getOptions().size()<2){
			fail("项目负责人下拉框为空");
		}
		belong.selectByIndex(1);

		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		projectName="项目"+DateUtil.getDateFormatHour();
		name.sendKeys(projectName);

		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;  
		//remove readonly attribute
		removeAttribute.executeScript("var setDate=document.getElementById(\"validityStart\");setDate.removeAttribute('readonly');") ;
		WebElement validityStart = driver.findElement(By.id("validityStart"));
		Date beginDate=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		validityStart.sendKeys(matter1.format(beginDate));
		
		
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + 7);
		Date endDate = matter1.parse(matter1.format(date.getTime()));

		removeAttribute.executeScript("var setDate=document.getElementById(\"validityEnd\");setDate.removeAttribute('readonly');") ;
		WebElement validityEnd = driver.findElement(By.id("validityEnd"));
		validityEnd.sendKeys(matter1.format(endDate));

		WebElement brand1 = driver.findElement(By.id("brand"));//为了关闭日期空间， 以使被它覆盖的控件能点击。
		brand1.click();

		WebElement projectIntruduction = driver.findElement(By.id("projectIntruduction"));
		projectIntruduction.click();
		projectIntruduction.clear();
		projectIntruduction.sendKeys(projectName + "简介");

		Select businessInfoId = new Select(driver.findElement(By.id("businessInfoId")));
		if (businessInfoId.getOptions().size()<2){
			fail("产品名称下拉框为空");
		}
		
		flag = false;
		for (int i=0; i< businessInfoId.getOptions().size(); i++){
			if (Constants.product.compareToIgnoreCase(businessInfoId.getOptions().get(i).getText()) == 0){
				flag = true;
				businessInfoId.selectByVisibleText(Constants.product);
				break;
			}
		}
		if (!flag){
			businessInfoId.selectByIndex(2);
		}
		driver.findElement(By.id("businessInfoId")).click();

		/*//担保方
		WebElement belong_5 = driver.findElement(By.id("belong_5"));
		belong_5.click();
		Select belongkeySel = new Select(driver.findElement(By.id("belongkey")));
		belongkeySel.selectByVisibleText("客户姓名");
		
		WebElement belongparam = driver.findElement(By.id("belongparam"));
		belongparam.clear();
		belongparam.sendKeys(Constants.belongName);
		
		WebElement belong_user = driver.findElement(By.id("belong_user"));//查询
		belong_user.click();	
		Thread.sleep(1000);
		

		WebElement belongWe = driver.findElement(By.xpath("//*[@id='belongList']/tbody/tr[2]/td[2]/input"));
		belongWe.click();

		WebElement belongSure = driver.findElement(By.id("belongSure"));
		belongSure.click();

		Thread.sleep(1000);*/
		//承租方
		WebElement tenantry = driver.findElement(By.id("tenantry_1"));
		tenantry.click();
		
		Select tenantrykeySel = new Select(driver.findElement(By.id("tenantrykey")));
		tenantrykeySel.selectByVisibleText("客户姓名");
		
		WebElement tenantryparam = driver.findElement(By.id("tenantryparam"));
		tenantryparam.clear();
		tenantryparam.sendKeys(Constants.tenantryName);
		
		WebElement tenantry_user = driver.findElement(By.id("tenantry_user"));//查询
		tenantry_user.click();	
		Thread.sleep(1000);

		WebElement tenantryWe = driver.findElement(By.xpath("//*[@id='tenantryList']/tbody/tr[2]/td[2]/input"));
		tenantryWe.click();

		WebElement tenantrySure = driver.findElement(By.id("tenantrySure"));
		tenantrySure.click();

		//供应商
		/*WebElement provider = null;
		flag = false;
		List<WebElement> btnProvider = driver.findElements(By.className("btn-80"));
		for (WebElement temp : btnProvider){
			System.out.println(temp.getAttribute("value"));
			if ("选择供应商".compareToIgnoreCase(temp.getAttribute("value")) == 0){
				System.out.println("供应商ok!");
				provider = temp;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		provider.click();
		
		Select providerkeySel = new Select(driver.findElement(By.id("providerkey")));
		providerkeySel.selectByVisibleText("客户姓名");
		
		WebElement providerparam = driver.findElement(By.id("providerparam"));
		providerparam.clear();
		providerparam.sendKeys(Constants.providerName);
		
		WebElement provider_user = driver.findElement(By.id("provider_user"));//查询
		provider_user.click();	
		Thread.sleep(1000);
		

		WebElement providerWe = driver.findElement(By.xpath("//*[@id='providerList']/tbody/tr[2]/td[2]/input"));
		providerWe.click();

		WebElement providerSure = driver.findElement(By.id("providerSure"));
		providerSure.click();
		*/

		WebElement brand = driver.findElement(By.id("brand"));
		brand.clear();
		brand.sendKeys("brand");

		WebElement task = driver.findElement(By.id("task"));
		task.clear();
		task.sendKeys("task");

		WebElement taskIsfinish = driver.findElement(By.id("taskIsfinish"));
		taskIsfinish.clear();
		taskIsfinish.sendKeys("taskIsfinish");

		WebElement managerMentRepay = driver.findElement(By.id("managerMentRepay"));
		managerMentRepay.clear();
		managerMentRepay.sendKeys("managerMentRepay");

		WebElement object = driver.findElement(By.id("object"));
		object.clear();
		object.sendKeys("object");

		WebElement proceed = driver.findElement(By.id("proceed"));
		proceed.clear();
		proceed.sendKeys("proceed");

		WebElement idea = driver.findElement(By.id("idea"));
		idea.clear();
		idea.sendKeys("idea");

		WebElement reportCheck = driver.findElement(By.id("reportCheck"));
		reportCheck.clear();
		reportCheck.sendKeys("reportCheck");

		WebElement reportCredit = driver.findElement(By.id("reportCredit"));
		reportCredit.clear();
		reportCredit.sendKeys("reportCredit");

		WebElement issue = driver.findElement(By.id("issue"));
		issue.clear();
		issue.sendKeys("issue");

		WebElement clause = driver.findElement(By.id("clause"));
		clause.clear();
		clause.sendKeys("clause");

		WebElement risk = driver.findElement(By.id("risk"));
		risk.clear();
		risk.sendKeys("risk");

		WebElement riskRelease = driver.findElement(By.id("riskRelease"));
		riskRelease.clear();
		riskRelease.sendKeys("riskRelease");

		WebElement factor = driver.findElement(By.id("factor"));
		factor.clear();
		factor.sendKeys("factor");

		WebElement calculate = driver.findElement(By.id("calculate"));
		calculate.clear();
		calculate.sendKeys("calculate");

		flag = false;
		WebElement btnAudit = null;
		List<WebElement> btn = driver.findElements(By.className("btn-80"));
		for (WebElement we : btn){
			if ("提交审核".compareToIgnoreCase(we.getAttribute("value")) == 0){
				btnAudit = we;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		btnAudit.click();

		WebElement btnReturn = null;
		flag = false;
		List<WebElement> btn100 = driver.findElements(By.className("btn-100"));
		for (WebElement we : btn100){
			if ("返回列表".compareToIgnoreCase(we.getAttribute("value")) == 0){
				btnReturn = we;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		btnReturn.click();
		
		driver.switchTo().defaultContent();
	}



}
