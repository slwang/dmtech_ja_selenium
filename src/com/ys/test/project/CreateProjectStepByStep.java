package com.ys.test.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateProjectStepByStep extends YSTestBase{
	static WebElement submitAuditBtn = null;
	static WebElement temporaryBtn = null;

	/**
	 * 跳转到新建项目页面， 找到提交审核和暂存项目按钮
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver.switchTo().frame("topFrame");
		//关联散标债权， 处理列表
		List<WebElement> lilist=driver.findElements(By.xpath("/html/body/ul/li"));  
		//Thread.sleep(1000);
		Boolean flag = false;
		for(int r=0;r<lilist.size();r++){  
			String trText=lilist.get(r).getText();  
			System.out.println(trText);
			if("产品中心".compareToIgnoreCase(trText) == 0){ 
				lilist.get(r).click();
				flag = true;
				break;  
			}
			//Thread.sleep(100);
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
		Thread.sleep(1000);
		WebElement create = driver.findElement(By.id("top_create"));
		create.click();
		Thread.sleep(1000);

		flag = false;
		List<WebElement> btn = driver.findElements(By.className("btn-80"));
		for (WebElement temp : btn){
			if ("提交审核".compareToIgnoreCase(temp.getAttribute("value")) == 0){
				flag = true;
				submitAuditBtn = temp;
				break;
			}
		}
		assertTrue(flag);

		for (WebElement temp : btn){
			if ("暂存项目".compareToIgnoreCase(temp.getAttribute("value")) == 0){
				flag = true;
				temporaryBtn = temp;
				break;
			}
		}
		assertTrue(flag);
		Thread.sleep(1000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.switchTo().defaultContent();
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(1000);
	}

	@After
	public void tearDown() throws Exception {
	}

	private void assertToastMsg(String msgStr) throws InterruptedException{
		WebElement toast = driver.findElement(By.xpath("/html/body/div[5]"));
		//System.out.println(toast.getText());
		assertEquals(msgStr, toast.getText());	
	}

	@Test
	public void test_001_projectBelongIsNull_submit() throws InterruptedException {
		submitAuditBtn.click();
		assertToastMsg("请选择项目负责人！");
	}
	
	@Test
	public void test_001_projectBelongIsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请选择项目负责人！");
	}

	@Test
	public void test_002_projectName001_IsNull_submit() throws InterruptedException {
		Select belong = new Select(driver.findElement(By.id("belong")));
		belong.selectByVisibleText("wsl");
		submitAuditBtn.click();
		assertToastMsg("请填写项目名称！");
	}

	@Test
	public void test_002_projectName002_IsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请填写项目名称！");
	}

	@Test
	public void test_002_projectName003_GreaterThan50() throws InterruptedException {
		WebElement name =driver.findElement(By.id("name"));
		name.sendKeys("项目名称1234567890123456789012345678901234567890aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeettttttttttsssssss");
		String sName = name.getAttribute("value");
		assertEquals(50,sName.length());
	}

	@Test
	public void test_003_validityStartIsNull_submit() throws InterruptedException {
		/*WebElement name =driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys("项目名称"+System.currentTimeMillis());
		submitAuditBtn.click();*/
		assertToastMsg("请填写项目有效开始日期！");
	}

	@Test
	public void test_003_validityStartIsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请填写项目有效开始日期！");
	}

	@Test
	public void test_004_validityEndIsNull_submit() throws InterruptedException {
		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;  
		//remove readonly attribute
		removeAttribute.executeScript("var setDate=document.getElementById(\"validityStart\");setDate.removeAttribute('readonly');") ;
		WebElement validityStart = driver.findElement(By.id("validityStart"));
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		validityStart.sendKeys(matter1.format(dt));
		submitAuditBtn.click();
		assertToastMsg("请填写项目有效结束日期！");
	}

	@Test
	public void test_004_validityEndIsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请填写项目有效结束日期！");
	}

	@Test
	public void test_005_projectIntruduction001_IsNull_submit() throws InterruptedException {
		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;  
		removeAttribute.executeScript("var setDate=document.getElementById(\"validityEnd\");setDate.removeAttribute('readonly');") ;
		WebElement validityEnd = driver.findElement(By.id("validityEnd"));
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		validityEnd.sendKeys(matter1.format(dt));
		submitAuditBtn.click();
		assertToastMsg("请填写项目简介！");
	}

	@Test
	public void test_005_projectIntruduction002_IsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请填写项目简介！");
	}

	@Test
	public void test_005_projectIntruduction003_GreaterThan200() throws InterruptedException {
		WebElement projectIntruduction = driver.findElement(By.id("projectIntruduction"));
		projectIntruduction.clear();
		projectIntruduction.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sName = projectIntruduction.getAttribute("value");
		assertEquals(200,sName.length());
	}

	@Test
	public void test_006_businessInfoIdIsNull_submit() throws InterruptedException {
		/*WebElement projectIntruduction = driver.findElement(By.id("projectIntruduction"));
		projectIntruduction.clear();
		projectIntruduction.sendKeys("项目名称aaa简介");*/
		submitAuditBtn.click();
		assertToastMsg("请选择关联产品！");
	}

	@Test
	public void test_006_businessInfoIdIsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请选择关联产品！");
	}


	@Test
	public void test_007_tenantryIsNull_submit() throws InterruptedException {
		Select businessInfoId = new Select(driver.findElement(By.id("businessInfoId")));
		if (businessInfoId.getOptions().size()<2){
			fail("产品名称下拉框为空");
		}
		businessInfoId.selectByIndex(2);
		submitAuditBtn.click();
		assertToastMsg("请选择承租方信息！");
	}

	@Test
	public void test_007_tenantryIsNull_temporary() throws InterruptedException {
		Thread.sleep(1000);
		temporaryBtn.click();
		assertToastMsg("请选择承租方信息！");
	}

	@Test
	public void test_008_belong_5IsNull_submit() throws InterruptedException {
		Thread.sleep(1000);
		//承租方
		WebElement tenantry = driver.findElement(By.id("tenantry_1"));
		tenantry.click();

		WebElement tenantryWe = driver.findElement(By.xpath("//*[@id='tenantryList']/tbody/tr[2]/td[2]/input"));
		tenantryWe.click();

		WebElement tenantrySure = driver.findElement(By.id("tenantrySure"));
		tenantrySure.click();
		Thread.sleep(1000);
		//submitAuditBtn.click();
		
		//assertToastMsg("请选择担保方信息！");
	}

	/*@Test
	public void test_008_belong_5IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请选择担保方信息！");
	}*/

	@Test
	public void test_009_providerIsNull_submit() throws InterruptedException {
		//担保方
		WebElement belong_5 = driver.findElement(By.id("belong_5"));
		belong_5.click();

		WebElement belongWe = driver.findElement(By.xpath("//*[@id='belongList']/tbody/tr[2]/td[2]/input"));
		belongWe.click();

		WebElement belongSure = driver.findElement(By.id("belongSure"));
		belongSure.click();
		/*submitAuditBtn.click();
		assertToastMsg("请选择供应商信息！");*/
	}

	/*@Test
	public void test_009_providerIsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请选择供应商信息！");
	}*/

	@Test
	public void test_010_brand001_IsNull_submit() throws InterruptedException {
		//供应商
		WebElement provider = null;
		Boolean flag = false;
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
		/*submitAuditBtn.click();
		assertToastMsg("请输入品牌信息！");*/
	}

	/*@Test
	public void test_010_brand002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品牌信息！");
	}*/

	@Test
	public void test_010_brand003_GreaterThan100() throws InterruptedException {
		WebElement brand = driver.findElement(By.id("brand"));
		brand.clear();
		brand.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sBrand = brand.getAttribute("value");
		assertEquals(100,sBrand.length());
	}

	/*@Test
	public void test_011_task001_IsNull_submit() throws InterruptedException {
		WebElement brand = driver.findElement(By.id("brand"));
		brand.clear();
		brand.sendKeys("brand");
		submitAuditBtn.click();
		assertToastMsg("请输task品牌信息！");
	}

	@Test
	public void test_011_task002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入task牌信息！");
	}*/

	@Test
	public void test_011_task003_GreaterThan200() throws InterruptedException {
		WebElement task = driver.findElement(By.id("task"));
		task.clear();
		task.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sTask= task.getAttribute("value");
		assertEquals(200,sTask.length());
	}

	/*@Test
	public void test_012_taskIsfinish001_IsNull_submit() throws InterruptedException {
		WebElement task = driver.findElement(By.id("task"));
		task.clear();
		task.sendKeys("task");
		submitAuditBtn.click();
		assertToastMsg("请输入taskIsfinish品牌信息！");
	}

	@Test
	public void test_012_taskIsfinish002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入taskIsfinish品牌信息！");
	}*/

	@Test
	public void test_012_taskIsfinish003_GreaterThan200() throws InterruptedException {
		WebElement taskIsfinish = driver.findElement(By.id("taskIsfinish"));
		taskIsfinish.clear();
		taskIsfinish.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sTaskIsfinish= taskIsfinish.getAttribute("value");
		assertEquals(200,sTaskIsfinish.length());
	}

	/*@Test
	public void test_013_managerMentRepay001_IsNull_submit() throws InterruptedException {
		WebElement taskIsfinish = driver.findElement(By.id("taskIsfinish"));
		taskIsfinish.clear();
		taskIsfinish.sendKeys("taskIsfinish");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌managerMentRepay信息！");
	}

	@Test
	public void test_013_managerMentRepay002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品managerMentRepay牌信息！");
	}*/

	@Test
	public void test_013_managerMentRepay003_GreaterThan200() throws InterruptedException {
		WebElement managerMentRepay = driver.findElement(By.id("managerMentRepay"));
		managerMentRepay.clear();
		managerMentRepay.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sManagerMentRepay= managerMentRepay.getAttribute("value");
		assertEquals(200,sManagerMentRepay.length());
	}

	/*@Test
	public void test_014_object001_IsNull_submit() throws InterruptedException {
		WebElement managerMentRepay = driver.findElement(By.id("managerMentRepay"));
		managerMentRepay.clear();
		managerMentRepay.sendKeys("managerMentRepay");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌object信息！");
	}

	@Test
	public void test_014_object002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品object牌信息！");
	}*/

	@Test
	public void test_014_object003_GreaterThan200() throws InterruptedException {
		WebElement object = driver.findElement(By.id("object"));
		object.clear();
		object.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		Thread.sleep(1000);
		String sObject= object.getAttribute("value");
		assertEquals(200,sObject.length());
	}

	/*@Test
	public void test_015_proceed001_IsNull_submit() throws InterruptedException {
		WebElement object = driver.findElement(By.id("object"));
		object.clear();
		object.sendKeys("object");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌proceed信息！");
	}

	@Test
	public void test_015_proceed002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品proceed牌信息！");
	}*/

	@Test
	public void testt_015_proceed003_GreaterThan200() throws InterruptedException {
		WebElement proceed = driver.findElement(By.id("proceed"));
		proceed.clear();
		proceed.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sProceed= proceed.getAttribute("value");
		assertEquals(200,sProceed.length());
	}

	/*@Test
	public void test_016_idea001_IsNull_submit() throws InterruptedException {
		WebElement proceed = driver.findElement(By.id("proceed"));
		proceed.clear();
		proceed.sendKeys("proceed");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌proceed信息！");
	}

	@Test
	public void test_016_idea002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品proceed牌信息！");
	}*/

	@Test
	public void testt_016_idea003_GreaterThan200() throws InterruptedException {
		WebElement idea = driver.findElement(By.id("idea"));
		idea.clear();
		idea.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sIdea= idea.getAttribute("value");
		assertEquals(200,sIdea.length());
	}

	/*@Test
	public void test_017_reportCheckIsNull_submit() throws InterruptedException {
		WebElement idea = driver.findElement(By.id("idea"));
		idea.clear();
		idea.sendKeys("idea");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌reportCheck信息！");
	}

	@Test
	public void test_017_reportCheckIsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品reportCheck牌信息！");
	}

	@Test
	public void test_018_reportCreditIsNull_submit() throws InterruptedException {
		WebElement reportCheck = driver.findElement(By.id("reportCheck"));
		reportCheck.clear();
		reportCheck.sendKeys("reportCheck");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌reportCheck信息！");
	}

	@Test
	public void test_018_reportCreditIsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品reportCheck牌信息！");
	}

	@Test
	public void test_019_issue001_IsNull_submit() throws InterruptedException {
		WebElement reportCredit = driver.findElement(By.id("reportCredit"));
		reportCredit.clear();
		reportCredit.sendKeys("issue");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌issue信息！");
	}

	@Test
	public void test_019_issue002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品issue牌信息！");
	}*/

	@Test
	public void testt_019_issue003_GreaterThan200() throws InterruptedException {
		WebElement issue = driver.findElement(By.id("issue"));
		issue.clear();
		issue.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sIssue= issue.getAttribute("value");
		assertEquals(200,sIssue.length());
	}

	/*@Test
	public void test_020_clause001_IsNull_submit() throws InterruptedException {
		WebElement issue = driver.findElement(By.id("issue"));
		issue.clear();
		issue.sendKeys("issue");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌clause信息！");
	}

	@Test
	public void test_020_clause002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品clause牌信息！");
	}*/

	@Test
	public void test_020_clause003_GreaterThan200() throws InterruptedException {
		WebElement clause = driver.findElement(By.id("clause"));
		clause.clear();
		String strCause = "1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef";
		clause.sendKeys(strCause+strCause+strCause);
		String sCause= clause.getAttribute("value");
		assertEquals(500,sCause.length());
	}

	/*@Test
	public void test_021_risk001_IsNull_submit() throws InterruptedException {
		WebElement clause = driver.findElement(By.id("clause"));
		clause.clear();
		clause.sendKeys("clause");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌risk信息！");
	}

	@Test
	public void test_021_risk002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品risk牌信息！");
	}*/

	@Test
	public void test_021_risk003_GreaterThan200() throws InterruptedException {
		WebElement risk = driver.findElement(By.id("risk"));
		risk.clear();
		risk.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sRisk= risk.getAttribute("value");
		assertEquals(200,sRisk.length());
	}

	/*@Test
	public void test_022_riskRelease001_IsNull_submit() throws InterruptedException {
		WebElement risk = driver.findElement(By.id("risk"));
		risk.clear();
		risk.sendKeys("risk");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌riskRelease信息！");
	}

	@Test
	public void test_022_riskRelease002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品riskRelease牌信息！");
	}*/

	@Test
	public void test_022_riskRelease003_GreaterThan200() throws InterruptedException {
		WebElement riskRelease = driver.findElement(By.id("riskRelease"));
		riskRelease.clear();
		riskRelease.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sRiskRelease= riskRelease.getAttribute("value");
		assertEquals(200,sRiskRelease.length());
	}

	/*@Test
	public void test_023_factor001_IsNull_submit() throws InterruptedException {
		WebElement riskRelease = driver.findElement(By.id("riskRelease"));
		riskRelease.clear();
		riskRelease.sendKeys("riskRelease");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌factor信息！");
	}

	@Test
	public void test_023_factor002_IsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品factor牌信息！");
	}*/

	@Test
	public void test_023_factor003_GreaterThan200() throws InterruptedException {
		WebElement factor = driver.findElement(By.id("factor"));
		factor.clear();
		factor.sendKeys("1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgdjh1234567890abcdefgabcdef");
		String sFactor= factor.getAttribute("value");
		assertEquals(200,sFactor.length());
		submitAuditBtn.click();
	}


	/*@Test
	public void test_024_calculateIsNull_submit() throws InterruptedException {
		WebElement factor = driver.findElement(By.id("factor"));
		factor.clear();
		factor.sendKeys("factor");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌calculate信息！");
	}

	@Test
	public void test_024_calculateIsNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品calculate牌信息！");
	}


	@Test
	public void test_025_notNull_submit() throws InterruptedException {
		WebElement calculate = driver.findElement(By.id("calculate"));
		calculate.clear();
		calculate.sendKeys("calculate");
		submitAuditBtn.click();
		assertToastMsg("请输入品牌calculate信息！");
	}

	@Test
	public void test_025_notNull_temporary() throws InterruptedException {
		temporaryBtn.click();
		assertToastMsg("请输入品calculate牌信息！");
	}*/
}
