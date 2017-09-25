package com.dmcc.login;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ys.utils.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DmccSetup {
	static WebDriver driver = null;
	//private static Cookie ck = new Cookie("JSESSIONID", "74a4f8b6c0c7102e2b9a774d0439"); 
	private static String businessUrl = "/businessType/list";
	private static String productUrl = "/productType/list";
	private static String rolePage = "/workflow/role/page";
	private static String wfUrl = "/workflow";

	private static String basePath = "http://192.168.2.10:8080/dmcc";
	private static String businessNumber = "";
	private static String businessName = "融资";
	private static String productNumber = "";
	private static String productName = "债权转移融资";
	private static String workflowName = "最终产品类型工作流";

	public static void main(String[] args) throws InterruptedException {
		openBrowser();
		
		//createBusiness();
		//createProduct();
		//配置角色页面没问题
//		createRolePage("估审","/riskControl/evaluate");
//		createRolePage("初审","/riskControl/firstTrial");
//		createRolePage("终审","/riskControl/finalJudgment");
		createWf();
	}
	
	public static void openBrowser(){
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);//设置了这个，很多sleep可以去了， 但是alert之前必须sleep， 留着的sleep 是必须有的
		driver.get(basePath);//不能少
		driver.manage().deleteAllCookies(); // 删除cookie里的内容
		driver.manage().addCookie(Constants.adminCk);
	}

	public static void createBusiness() throws InterruptedException{
		driver.get(basePath + businessUrl);
		Thread.sleep(1000);
		WebElement createBtn = driver.findElement(By.partialLinkText("新建业务类型"));
		createBtn.click();
		Thread.sleep(1000);

		businessNumber = genRandNum();
		WebElement number = driver.findElement(By.id("number"));
		number.clear();
		number.sendKeys(businessNumber);

		businessName ="业务类型"+ genRandString();
		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys(businessName);


		Select statusSel = new Select(driver.findElement(By.id("status")));
		statusSel.selectByVisibleText("正常");

		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById(\"updateBusinessType\").style.zIndex=2;");
		
		WebElement updateBusinessType = driver.findElement(By.id("updateBusinessType"));
		updateBusinessType.click();
	}

	public static void createProduct() throws InterruptedException{
		driver.get(basePath + productUrl);
		Thread.sleep(1000);
		WebElement createBtn = driver.findElement(By.partialLinkText("新建产品类型"));
		createBtn.click();
		Thread.sleep(1000);

		WebElement businessNumberWe = driver.findElement(By.id("businessNumber"));
		businessNumberWe.clear();
		businessNumberWe.sendKeys(businessNumber);

		productNumber = genRandNum();
		WebElement number = driver.findElement(By.id("number"));
		number.clear();
		number.sendKeys(productNumber);

		productName ="产品类型" + genRandString();
		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys(productName);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById(\"updateProductType\").style.zIndex=2;");

		WebElement updateProductType = driver.findElement(By.id("updateProductType"));
		updateProductType.click();
	}

	public static void createRolePage(String roleName, String roleUrl) throws InterruptedException{
		driver.get(basePath + rolePage);
		Thread.sleep(1000);
		WebElement createBtn = driver.findElement(By.partialLinkText("新增配置"));
		createBtn.click();
		Thread.sleep(1000);

		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys(productName + roleName);

		Select roleIdSel = new Select (driver.findElement(By.id("roleId")));
		roleIdSel.selectByVisibleText(roleName+"角色");

		Select subsystemCodeSel = new Select (driver.findElement(By.id("subsystemCode")));
		subsystemCodeSel.selectByVisibleText("涌时融资租赁");

		Select businessCodeSel = new Select (driver.findElement(By.id("businessCode")));
		businessCodeSel.selectByVisibleText(businessName);

		Select productTypeSel = new Select (driver.findElement(By.id("productType")));
		productTypeSel.selectByVisibleText(productName);

		WebElement href = driver.findElement(By.id("href"));
		href.clear();
		href.sendKeys(roleUrl);

		WebElement saveBtn = driver.findElement(By.id("saveBtn"));
		saveBtn.click();
	}

	public static void createWf() throws InterruptedException{
		driver.get(basePath + wfUrl);
		Thread.sleep(1000);
		WebElement createBtn = driver.findElement(By.partialLinkText("新建工作流"));
		createBtn.click();
		Thread.sleep(1000);

		workflowName = productName + "工作流";
		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys(workflowName);

		Select subsystemCodeSel = new Select (driver.findElement(By.id("subsystemCode")));
		subsystemCodeSel.selectByVisibleText("涌时融资租赁");

		Select businessCodeSel = new Select (driver.findElement(By.id("businessCode")));
		businessCodeSel.selectByVisibleText(businessName);

		Select productTypeSel = new Select (driver.findElement(By.id("productType")));
		productTypeSel.selectByVisibleText(productName);

		Select workFlowTypeSel = new Select (driver.findElement(By.id("workFlowType")));
		workFlowTypeSel.selectByVisibleText("业务产品流程");

		WebElement saveBtn = driver.findElement(By.id("saveBtn"));
		saveBtn.click();
		createNodeList();
	}

	public static void createNodeList() throws InterruptedException{
		Thread.sleep(1000);

		WebElement workflowNameWe = driver.findElement(By.id("workflowName"));
		workflowNameWe.clear();
		workflowNameWe.sendKeys(workflowName);
		Thread.sleep(1000);
		
		WebElement queryBtn = null; 
		List<WebElement> btns = driver.findElements(By.tagName("button"));
		for (WebElement we: btns){
			if ("query".compareToIgnoreCase(we.getAttribute("role"))==0 ){
				queryBtn = we;
				break;
			}
		}
		queryBtn.click();
		
		Thread.sleep(1000);
		WebElement nodeBtn = driver.findElement(By.partialLinkText("节点列表"));
		nodeBtn.click();
		Thread.sleep(1000);

		createNode("估审", "", "是","开始");
		createNode("初审", "", "是","中间");
		createNode("终审", "初审", "否","结束");

		releaseWorkFlow();

	}

	public static void  createNode(String nodeName, String returnNode, String returnExternal, String nodeType) throws InterruptedException{
		Thread.sleep(1000);
		WebElement nodeAddBtn = driver.findElement(By.partialLinkText("增加节点"));
		nodeAddBtn.click();
		Thread.sleep(1000);

		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys(nodeName);


		Select  roleIdSel  = new Select (driver.findElement(By.id("roleId")));
		roleIdSel.selectByVisibleText(nodeName+"角色");


		Select  isAllocateSel  = new Select (driver.findElement(By.id("isAllocate")));
		isAllocateSel.selectByVisibleText("是");

		Select  returnSubSysSel  = new Select (driver.findElement(By.id("returnSubSys")));
		returnSubSysSel.selectByVisibleText(returnExternal);


		Select  typeSel  = new Select (driver.findElement(By.id("type")));
		typeSel.selectByVisibleText(nodeType);

		WebElement saveBtn = null; 
		List<WebElement> btns = driver.findElements(By.tagName("button"));
		for (WebElement we: btns){
			if ("submit".compareToIgnoreCase(we.getAttribute("type"))==0 ){
				saveBtn = we;
				break;
			}
		}
		saveBtn.click();
		Thread.sleep(1000);	
	}
	public static void releaseWorkFlow() throws InterruptedException{
		Thread.sleep(1000);
		driver.get(basePath + wfUrl);
		Thread.sleep(1000);
		WebElement workflowNameWe = driver.findElement(By.id("workflowName"));
		workflowNameWe.clear();
		workflowNameWe.sendKeys(workflowName);

		WebElement release = driver.findElement(By.partialLinkText("发布"));
		release.click();
	}


	public static String genRandNum(){
		String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",  
				"8", "9", "1", "0"};  
		List list = Arrays.asList(beforeShuffle);  
		Collections.shuffle(list);  
		StringBuilder sb = new StringBuilder();  
		for (int i = 0; i < list.size(); i++) {  
			sb.append(list.get(i));  
		}  
		String afterShuffle = sb.toString();  
		String result = afterShuffle.substring(5, 9);  
		return result;  
	}

	public static String genRandString(){
		String[] beforeShuffle = new String[] { "a", "b", "c", "d", "e", "f",  
				"g", "h", "i", "j"};  
		List list = Arrays.asList(beforeShuffle);  
		Collections.shuffle(list);  
		StringBuilder sb = new StringBuilder();  
		for (int i = 0; i < list.size(); i++) {  
			sb.append(list.get(i));  
		}  
		String afterShuffle = sb.toString();  
		String result = afterShuffle.substring(5, 9);  
		return result;  
	}




}
