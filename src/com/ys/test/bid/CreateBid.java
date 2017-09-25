package com.ys.test.bid;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ys.test.base.YSTestBase;
import com.ys.test.constant.Constants;
import com.ys.utils.DateUtil;

public class CreateBid extends YSTestBase{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		System.out.println("CreateBid @BeforeClass");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("CreateBid @AfterClass");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("CreateBid before");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("CreateBid after");
	}

	@Test
	public void test_createBid() throws InterruptedException {
		driver.switchTo().frame("topFrame");

		//关联散标债权， 处理列表
		List<WebElement> lilist=driver.findElements(By.xpath("/html/body/ul/li"));  
		Boolean flag = false;
		for(int r=0;r<lilist.size();r++){  
			String trText=lilist.get(r).getText();  
			//System.out.println(trText);
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
		WebElement proManager = driver.findElement(By.linkText("进件录入"));
		proManager.click();

		driver.switchTo().defaultContent();
		driver.switchTo().frame("rightFrame");
		driver.switchTo().frame("frame_tab_80301");
		WebElement project = driver.findElement(By.id("project"));
		project.click();
		Thread.sleep(1000);
		
		WebElement projectName = driver.findElement(By.id("projectName"));
		projectName.clear();
		projectName.sendKeys(Constants.projectName);
		
		WebElement projectBtn = driver.findElement(By.id("projectBtn"));
		projectBtn.click();
		
		
		Thread.sleep(1000);
		List<WebElement> projectInfoList = driver.findElements(By.className("cbox"));
		
		System.out.println(projectInfoList.size());
		if (projectInfoList.size()>1){
			projectInfoList.get(1).click();
			WebElement cancelBtn = driver.findElement(By.partialLinkText("确定"));
			cancelBtn.click();
			
		}
		else{
			WebElement cancelBtn = driver.findElement(By.partialLinkText("取消"));
			cancelBtn.click();
			fail("目前没有用于进件录入的项目");
		}
		
		
		Thread.sleep(1000);
		// 添加租赁物
		WebElement leaseholdAdd = driver.findElement(By.id("leaseholdAdd"));
		leaseholdAdd.click();
		
		WebElement name = driver.findElement(By.id("name"));
		name.clear();
		name.sendKeys("租赁物名称"+DateUtil.getDateFormatHour());

		WebElement brand = driver.findElement(By.id("brand"));
		brand.clear();
		brand.sendKeys("租赁物品牌");
	
		WebElement specifications = driver.findElement(By.id("specifications"));
		specifications.sendKeys("租赁物规格型号");
		
	
		WebElement colour = driver.findElement(By.id("colour"));
		colour.clear();
		colour.sendKeys("红色");

		WebElement number = driver.findElement(By.id("number"));
		number.clear();
		number.sendKeys("100");

		WebElement evaluationValue = driver.findElement(By.id("evaluationValue"));
		evaluationValue.clear();
		evaluationValue.sendKeys("30000");

		WebElement subscriptionPrice = driver.findElement(By.id("subscriptionPrice"));
		subscriptionPrice.clear();
		subscriptionPrice.sendKeys("1000");

		WebElement year = driver.findElement(By.id("year"));
		year.clear();
		year.sendKeys("8");

		WebElement dateBuy = driver.findElement(By.id("dateBuy"));
		dateBuy.clear();
		dateBuy.sendKeys("2017-01-01");

		WebElement store = driver.findElement(By.id("store"));
		store.clear();
		store.sendKeys("北京1号仓库");

		WebElement dateDeal = driver.findElement(By.id("dateDeal"));
		dateDeal.clear();
		dateDeal.sendKeys("2018-01-01");

		WebElement brandModel = driver.findElement(By.id("brandModel"));
		brandModel.clear();
		brandModel.sendKeys("厂商型号");

		WebElement frameNumber = driver.findElement(By.id("frameNumber"));
		frameNumber.clear();
		frameNumber.sendKeys("车架1号");

		WebElement certificate = driver.findElement(By.id("certificate"));
		certificate.clear();
		certificate.sendKeys("合格证");

		WebElement engineNumber = driver.findElement(By.id("engineNumber"));
		engineNumber.clear();
		engineNumber.sendKeys("发动机号");

		WebElement credentialName = driver.findElement(By.id("credentialName"));
		credentialName.clear();
		credentialName.sendKeys("证件名称");

		WebElement credentialNumber = driver.findElement(By.id("credentialNumber"));
		credentialNumber.clear();
		credentialNumber.sendKeys("证件编号");

		WebElement keysNumber = driver.findElement(By.id("keysNumber"));
		keysNumber.clear();
		keysNumber.sendKeys("2");

		WebElement mileage = driver.findElement(By.id("mileage"));
		mileage.clear();
		mileage.sendKeys("里程数");
		
		WebElement quedingBtn = driver.findElement(By.partialLinkText("确定"));
		quedingBtn.click();
		Thread.sleep(1000);
		
		//产品
		Select productSel = new Select (driver.findElement(By.id("product")));
		if (productSel.getOptions().size() >1 ){
			productSel.selectByIndex(1);
		}
		else{//手动输入
			WebElement repayRate = driver.findElement(By.id("repayRate"));
			repayRate.clear();
			repayRate.sendKeys("1");
			
			WebElement periods = driver.findElement(By.id("periods"));
			periods.clear();
			periods.sendKeys("3");
			
			WebElement marginProportion = driver.findElement(By.id("marginProportion"));
			marginProportion.clear();
			marginProportion.sendKeys("1");
			
		}
		
		WebElement auditBtn = driver.findElement(By.id("audit"));
		auditBtn.click();
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		
		/*Thread.sleep(1000);
		
		WebElement btnReturn = null;
		flag = false;
		List<WebElement> btn100 = driver.findElements(By.className("btn-100"));
		System.out.println("btn-100:   "+btn100.size());
		for (WebElement we : btn100){
			if ("返回列表".compareToIgnoreCase(we.getAttribute("value")) == 0){
				btnReturn = we;
				flag = true;
				break;
			}
		}
		assertTrue(flag);
		btnReturn.click();
		*/
	}
}
