package com.ys.test.project;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ys.test.base.YSTestBase;
import com.ys.test.constant.Constants;
import com.ys.utils.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectAudit extends YSTestBase{

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
	public void test_001_yewuyuanCreateProject() throws Exception{
		YSTestBase.switchUser("yewuyuan", "111111");
		CreateProject creat = new CreateProject();
		creat.test_createProject();
	}

	@Test
	public void test_002_firstAuditProject() throws InterruptedException{
		YSTestBase.switchUser("chushen", "111111");
		audit("初审");
	}

	@Test
	public void test_003_secondAuditProject() throws InterruptedException{
		YSTestBase.switchUser("fushen", "111111");
		audit("复审");
	}

	@Test
	public void test_004_finalAuditProject() throws InterruptedException{
		YSTestBase.switchUser("zhongshen", "111111");
		audit("终审");
	}
	
	@Test
	public void test_005_submitRiskAudit() throws InterruptedException{
		YSTestBase.switchUser("yewuyuan", "111111");
		YSTestBase.openMenu("产品中心", "项目管理");

		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80201");

		Select key = new Select(driver.findElement(By.id("key")));
		key.selectByVisibleText("项目名称");

		WebElement param = driver.findElement(By.id("param"));
		param.clear();
		param.sendKeys(projectName);

		WebElement queryBtn = driver.findElement(By.id("q_project"));
		queryBtn.click();
		
		Thread.sleep(1000);
		WebElement firstAudit = driver.findElement(By.partialLinkText("提交风控"));
		firstAudit.click();
		
		Thread.sleep(3000);
		WebElement submitRiskAssure = driver.findElement(By.partialLinkText("确定"));
		submitRiskAssure.click();
		
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
	}

	private void audit(String auditNodeName) throws InterruptedException{
		YSTestBase.openMenu("产品中心", "项目管理");

		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80201");

		Select key = new Select(driver.findElement(By.id("key")));
		key.selectByVisibleText("项目名称");

		WebElement param = driver.findElement(By.id("param"));
		param.clear();
		param.sendKeys(projectName);

		WebElement queryBtn = driver.findElement(By.id("q_project"));
		queryBtn.click();

		Thread.sleep(1000);
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

		Boolean flag = false;
		WebElement btnAudit = null;
		List<WebElement> btn = driver.findElements(By.className("btn-80"));
		for (WebElement we : btn){
			if ("通过".compareToIgnoreCase(we.getAttribute("value")) == 0){
				btnAudit = we;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		btnAudit.click();

		Thread.sleep(3000);
		WebElement auditAssure = driver.findElement(By.partialLinkText("确定"));
		auditAssure.click();
		driver.switchTo().defaultContent();
	}


}
