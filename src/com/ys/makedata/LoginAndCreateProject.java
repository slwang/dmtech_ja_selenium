package com.ys.makedata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * @author wsl
 *
 */
public class LoginAndCreateProject {

	@Test
	public void testLogin() throws InterruptedException{
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		//WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);

		driver.get("http://192.168.2.240:8091/main");
		WebElement account = driver.findElement(By.id("account"));
		account.clear();
		account.sendKeys("admin");

		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("111111");

		WebElement loginbtn = driver.findElement(By.className("loginbtn"));
		loginbtn.click();

		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_default");

		WebElement welcome = driver.findElement(By.xpath("/html/body/div/div/b"));
		assertEquals(welcome.getText(), "您好，欢迎使用保险管理系统");

		driver.switchTo().defaultContent();
		//driver.switchTo().defaultContent();//只需一个就调回主页面
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
		name.sendKeys("项目名称"+System.currentTimeMillis());

		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;  
		//remove readonly attribute
		removeAttribute.executeScript("var setDate=document.getElementById(\"validityStart\");setDate.removeAttribute('readonly');") ;
		WebElement validityStart = driver.findElement(By.id("validityStart"));
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		validityStart.sendKeys(matter1.format(dt));

		removeAttribute.executeScript("var setDate=document.getElementById(\"validityEnd\");setDate.removeAttribute('readonly');") ;
		WebElement validityEnd = driver.findElement(By.id("validityEnd"));
		validityEnd.sendKeys(matter1.format(dt));

		WebElement brand1 = driver.findElement(By.id("brand"));//为了关闭日期空间， 以使被它覆盖的控件能点击。
		brand1.click();

		WebElement projectIntruduction = driver.findElement(By.id("projectIntruduction"));
		projectIntruduction.click();
		projectIntruduction.clear();
		projectIntruduction.sendKeys("项目名称aaa简介");

		Select businessInfoId = new Select(driver.findElement(By.id("businessInfoId")));
		if (businessInfoId.getOptions().size()<2){
			fail("产品名称下拉框为空");
		}
		businessInfoId.selectByIndex(1);
		driver.findElement(By.id("businessInfoId")).click();

		//担保方
		WebElement belong_5 = driver.findElement(By.id("belong_5"));
		belong_5.click();
		
		WebElement belongWe = driver.findElement(By.xpath("//*[@id='belongList']/tbody/tr[2]/td[2]/input"));
		belongWe.click();
		
		WebElement belongSure = driver.findElement(By.id("belongSure"));
		belongSure.click();
		
		Thread.sleep(1000);
		//承租方
		WebElement tenantry = driver.findElement(By.id("tenantry_1"));
		tenantry.click();
		
		WebElement tenantryWe = driver.findElement(By.xpath("//*[@id='tenantryList']/tbody/tr[2]/td[2]/input"));
		tenantryWe.click();

		WebElement tenantrySure = driver.findElement(By.id("tenantrySure"));
		tenantrySure.click();

		//供应商
		WebElement provider = null;
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

		WebElement providerWe = driver.findElement(By.xpath("//*[@id='providerList']/tbody/tr[2]/td[2]/input"));
		providerWe.click();

		WebElement providerSure = driver.findElement(By.id("providerSure"));
		providerSure.click();

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
	}

	
}
