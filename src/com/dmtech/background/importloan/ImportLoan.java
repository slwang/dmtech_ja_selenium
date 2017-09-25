package com.dmtech.background.importloan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * @author wsl
 * 2017-07-18
 * 导标自动化-可以使用，  
 * 内容包含: 导标-审核借款-新建理财包-关联标的-新建产品-发布产品
 * 需要修改的地方：
 * 1、ck: 登录后的cookie
 * 2、loanFile: 标的模板文件的位置
 * 3、loanTile:和导标模板中的标题一致（车险分期模板中是车辆品牌一项）
 * 4、period: 借款周期
 * 5、loanRate: 债权利率
 * 6、interestRate: 平台加息
 * 7、rePaymentMethod: 还款方式 * 
 * 
 * 2017-7-31 modified by wsl
 * 这些参数可以从excel文件中读取， excel要求只有一个sheet， 一个sheet里面只有两行， 一行标题，一行标的数据。
 * 包括：loanFile
 * loanTitle ：包含“品牌”
 * rePaymentMethod: 还款方式
 * period: 期限(月)
 * loanRate  : 还款利率
 * 
 * 该版本需要修改的参数只有cookie和loanFile（可以是文件夹也可以是导标文件名， 如果是文件夹的话， 遍历文件夹下的所有导标文件， 循环进行导入直至发布）
 * 
 * 2017-08-04 modified by wsl
 * 修正为firefox和chrome通用 
 */
public class ImportLoan {
	static WebDriver driver = null;
	private static Cookie ck = new Cookie("JSESSIONID", "7b0a089b362cc7427f693dd38c88"); //登陆豆蔓后台获取cookie后拷贝到这里, 目的，绕过登陆的图形验证码
	private static String importUrl = "http://192.168.2.8:8080/CreditManager/loan/import";
	private static String loanFile="E:\\工作\\导标模板\\小豆\\未发布";//标的文件， 可以是文件夹名字， 也可以是导标文件名
	private static String loanTitle="A002债权匹配1月期";//"w001auto自动1";// 标的的标题， 切记一定唯一
	private static String period = "1";
	private static String loanRate = "9";//债权利率
	private static String interestRate = "1";//平台加息
	private static String rePaymentMethod ="按月付息到期还本";
	private static String loanType  ="车险分期";
	private static String basePath = "http://192.168.2.8:8080/CreditManager";

	public static void main(String[] args) throws InterruptedException {
		importLoanByBrowsers("chrome");
		//importLoanByBrowsers("firefox");
	}
	
	public static void importLoanByBrowsers(String browser) throws InterruptedException{
		File loanFiles = new File(loanFile);
		if (!loanFiles.exists()){
			System.out.println("文件不存在!");
			return;
		}
		if ("chrome".compareToIgnoreCase(browser) == 0){
			String proPath = System.getProperty("user.dir");
			System.setProperty("webdriver.chrome.driver",
					proPath + "/chromedriver2.25/chromedriver.exe");

			driver = new ChromeDriver();
		}else if ("firefox".compareToIgnoreCase(browser) == 0){
			driver = new FirefoxDriver();
		}
		else{
			System.out.println("没有匹配的浏览器");
			return;
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);//设置了这个，很多sleep可以去了， 但是alert之前必须sleep， 留着的sleep 是必须有的
		driver.get("http://192.168.2.8:8080");//不能少
		driver.manage().deleteAllCookies(); // 删除cookie里的内容
		driver.manage().addCookie(ck);

		if (loanFiles.isDirectory()){
			File fa[] = loanFiles.listFiles();
			for (int i = 0; i < fa.length; i++) {
				loanFile = fa[i].getAbsolutePath();
				System.out.println(loanFile);
				importLoans();
			}
		}else{
			importLoans();
		}
		System.out.println("Success All!");
		
	}

	public static void importLoans() throws InterruptedException {
		System.out.println("*************************************************");
		getContentExcel2003(loanFile,";");

		driver.get(importUrl);
		importLoan();
		auditLoan();
		addBag();
		addProduct();
		System.out.println("Success from import Loan to release product!");
		System.out.println("----------------------------------------------------------------");
	}


	/**
	 * 导标
	 * @throws InterruptedException
	 */
	public static void importLoan() throws InterruptedException {
		WebElement importLoan = driver.findElement(By.id("uploadfile"));

		File file = new File(loanFile);
		importLoan.sendKeys(file.getPath());

		WebElement importBtn = driver.findElement(By.id("importbtn"));
		importBtn.click();
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		System.out.println("Success importLoan!");
	}

	/**
	 * 审核       借贷管理--借款申请列表
	 * @throws InterruptedException
	 */
	public static void auditLoan() throws InterruptedException{
		driver.get("http://192.168.2.8:8080/CreditManager/loan/request/list");//借款申请列表

		WebElement search = driver.findElement(By.xpath("//*[@id='loanList_filter']/label/input"));
		search.sendKeys(loanTitle);
		
		WebElement loanCheckBox = driver.findElement(By.id("selectLoanRequest"));
		loanCheckBox.click();

		WebElement audit = driver.findElement(By.id("pre_batchAudit"));
		audit.click();

		WebElement publishNow = driver.findElement(By.id("publishNow"));
		publishNow.click();

		WebElement pre_batchAudit = driver.findElement(By.id("batchAudit_submit"));//  firefox的id是 pre_batchAudit
		pre_batchAudit.click();
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		System.out.println("Success auditLoan!");
	}

	/**
	 * 新建理财包         理财产品管理-理财包列表
	 * @throws InterruptedException
	 */
	public static void addBag() throws InterruptedException{
		driver.get("http://192.168.2.8:8080/CreditManager/p2p/cltAssetsBaseBag/listBag");
		
		WebElement addBag = driver.findElement(By.partialLinkText("添加理财包"));//新建理财包		
		addBag.click();

		WebElement name = driver.findElement(By.id("name"));
		name.sendKeys(loanTitle);
		WebElement productCycle = driver.findElement(By.id("productCycle"));
		productCycle.sendKeys(period);

		WebElement minPurchaseAmount = driver.findElement(By.id("minPurchaseAmount"));
		minPurchaseAmount.sendKeys("100");
		WebElement maxPurchaseAmount = driver.findElement(By.id("maxPurchaseAmount"));
		maxPurchaseAmount.sendKeys("100000");

		WebElement rate = driver.findElement(By.id("rate"));
		rate.clear();
		rate.sendKeys(loanRate);

		Select sel = new Select(driver.findElement(By.id("method")));
		//Select sel = new Select(driver.findElement(By.xpath("//select[@id='method']")));
		sel.selectByVisibleText(rePaymentMethod);

		WebElement submitFormBtn = driver.findElement(By.id("submitFormBtn"));

		System.out.println("save bag");
		submitFormBtn.click();
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();

		// 理财包关联的包反应慢，所以手动关联
		/*WebElement searchBag = driver.findElement(By.id("name_"));

		searchBag.clear();
		searchBag.sendKeys(loanTitle);

		WebElement queryBtn = driver.findElement(By.id("queryBtn"));
		queryBtn.click();
		Thread.sleep(1000);

		WebElement editLink = driver.findElement(By.partialLinkText("编辑"));
		editLink.click();


		WebElement relationLoan = driver.findElement(By.partialLinkText("添加"));
		relationLoan.click();

		//关联散标债权， 处理列表
		Thread.sleep(5000);
		Boolean flag = false;
		List<WebElement> trlist=driver.findElements(By.xpath("//*[@id='OPENED_LOANS']/tbody/tr"));  
		for(int r=0;r<trlist.size();r++){  
			String trText=trlist.get(r).getText();  
			System.out.println(trText);
			if(trText.contains(loanTitle+loanType)){  
				flag = true;
				WebElement checkBox =   trlist.get(r).findElement(By.xpath(".//td[1]/input")); 
				checkBox.click();
				break;  
			}
		}
		if (!flag)
		{
			System.out.println("理财包没有找到关联的标");
		}

		WebElement queding = driver.findElement(By.xpath("//*[@id='loanList']/div/div/div[2]/div/div/div/button"));
		queding.click();
		Thread.sleep(1000);

		submitFormBtn = driver.findElement(By.id("submitFormBtn"));
		System.out.println("save relationLoan");
		submitFormBtn.click();
		Thread.sleep(3000);
		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();*/
		System.out.println("Success addBag");
	}

	/**
	 * 增加产品   理财产品管理-产品列表
	 * @throws InterruptedException
	 */
	public static void addProduct() throws InterruptedException{
		driver.get("http://192.168.2.8:8080/CreditManager/p2p/cltAssetsStore/listStore");//产品列表

		WebElement addProduct = driver.findElement(By.partialLinkText("添加产品"));//新家产品
		addProduct.click();

		Select se = new Select( driver.findElement(By.name("assetsBaseBag")));
		se.selectByVisibleText(loanTitle);

		WebElement productName = driver.findElement(By.id("name"));
		productName.clear();
		productName.sendKeys(loanTitle);

		Select typeSelect = new Select(driver.findElement(By.name("type")));
		typeSelect.selectByVisibleText("散标");

		WebElement timeOut = driver.findElement(By.id("timeOut"));
		timeOut.clear();
		timeOut.sendKeys("7");

		/*WebElement intervalDay = driver.findElement(By.id("intervalDay"));
		intervalDay.clear();
		intervalDay.sendKeys("1");*/
		//Thread.sleep(100);

		WebElement addRate = driver.findElement(By.id("interestRate"));
		addRate.clear();
		addRate.sendKeys(interestRate);

		/*Select repayMethod = new Select( driver.findElement(By.name("repayMethod")));
		repayMethod.selectByVisibleText(rePaymentMethod);*/

		Select whereToPut = new Select( driver.findElement(By.name("whereToPut")));
		whereToPut.selectByVisibleText("PC端 + M站 + APP");

		WebElement openingTime = driver.findElement(By.id("openingTime"));
		openingTime.clear();
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		openingTime.sendKeys(matter1.format(dt));

		Select newSelect = new Select(driver.findElement(By.name("noviceTask")));
		newSelect.selectByVisibleText("否");

		WebElement submitFormBtn = driver.findElement(By.id("submitFormBtn"));
		System.out.println("save product");
		submitFormBtn.click();
		Thread.sleep(3000);

		driver.switchTo().alert().accept(); //处理弹出窗口
		driver.switchTo().defaultContent();
		System.out.println("Success addProduct");

		System.out.println("release product");
		WebElement searchProduct = driver.findElement(By.id("name_"));
		searchProduct.clear();
		searchProduct.sendKeys(loanTitle);

		WebElement queryBtn = driver.findElement(By.id("queryBtn"));
		queryBtn.click();
		
		Thread.sleep(3000);
		WebElement releaseBtn = driver.findElement(By.partialLinkText("发布"));
		releaseBtn.click();
		System.out.println("Success ReleaseProduct!");
	}


	/**
	 * 读取Excel数据内容
	 * @param fileName
	 * @param delimiter//分割符
	 * @return String
	 */
	public static String getContentExcel2003(String  fileName, String delimiter){  
		try {
			//一下字段对饮excel中的项
			//loanTitle ：包含“品牌”
			//rePaymentMethod: 还款方式
			//period: 期限(月)
			//loanRate  : 还款利率
			//loanType: 借款类型
			int loanTitleIndex = 0;
			int rePaymentMethodIndex = 0;
			int periodIndex = 0;
			int loanRateIndex = 0; 
			int loanTypeIndex =0; //这五个参数记录要读取数据内容的列

			Workbook wb = WorkbookFactory.create(new FileInputStream(fileName));
			int sheetNum = wb.getNumberOfSheets();  
			String lResult ="1:success";
			FileOutputStream o=null;  
			String content ="";
			for (int i = 0; i < sheetNum; i++) {  
				Sheet childSheet = wb.getSheetAt(i);  
				if (childSheet==null)
					continue;
				int rowNum = childSheet.getLastRowNum();  
				int isMergeRegions = childSheet.getNumMergedRegions();
				if (isMergeRegions>0){
					childSheet = unMergedCell(childSheet);
				}
				for (int j = 0; j <= rowNum; j++) {  
					Row row = childSheet.getRow(j);  
					if (row ==null)
						continue;
					int cellNum = row.getLastCellNum();  
					for (int k = 0; k < cellNum; k++) {  
						HSSFCell cell = (HSSFCell) row.getCell(k);
						if (cell ==null)
						{
							content += delimiter;
							continue;
						}
						int cellType = cell.getCellType();			         
						String strCell="";
						switch (cellType) {
						case HSSFCell.CELL_TYPE_NUMERIC:// 0 Numeric
							if (HSSFDateUtil.isCellDateFormatted(cell))
							{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								strCell =sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
							}
							else{
								// 将被表示成1.3922433397E10的手机号转化为13922433397,不一定是最好的转换方法
								DecimalFormat df = new DecimalFormat("#");
								strCell = df.format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING://  1  String
							strCell = cell.getStringCellValue();
							break;
						default:
						}	

						if (j ==0){//第一行标题取出四个数据下表
							if (strCell.contains("品牌")){
								loanTitleIndex = k;
							}
							if (strCell.contains("还款方式")){
								rePaymentMethodIndex = k;
							}
							if (strCell.contains("期限(月)")){
								periodIndex = k;
							}
							if (strCell.contains("还款利率")){
								loanRateIndex = k;
							}
							if (strCell.contains("借款类型")){
								loanTypeIndex = k;
							}
							
						}

						if (j ==1){//第二行标题取出四个数据
							if (loanTitleIndex == k){
								loanTitle = strCell;
							}
							if (rePaymentMethodIndex == k){
								rePaymentMethod = strCell;
							}
							if (periodIndex == k){
								period = strCell;
							}
							if (loanRateIndex == k){
								loanRate = strCell;
							}
							if (loanTypeIndex == k){
								loanType = strCell;
							}
							
						}
						content +=strCell + delimiter; 
					}  
					if (i==sheetNum-1 && j==rowNum )
						;
					else
						content +="\r\n"; 
				}
				System.out.println(loanTitle);
				System.out.println(rePaymentMethod);
				System.out.println(period);
				System.out.println(loanRate);
			}  
			//o.close(); 
			return lResult;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return "10002:格式异常";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "10001:文件不存在";
		} catch (IOException e) {
			e.printStackTrace();
			return "10003:IO异常";
		}		   
	}


	/**
	 * 拆分sheet中的单元格
	 * @param sheet
	 * @return sheet
	 */
	public static Sheet unMergedCell(Sheet sheet)
	{
		int num = sheet.getNumMergedRegions();
		for(int i = 0; i < num; i++)
		{		       
			CellRangeAddress range = sheet.getMergedRegion(i);
			HSSFCell mergedcell = (HSSFCell) sheet.getRow(range.getFirstRow()).getCell(range.getFirstColumn());
			String  mergedvalue =mergedcell.getStringCellValue();
			sheet.removeMergedRegion(i) ;
			num =num -1;
			int rowNum =range.getLastRow()-range.getFirstRow()+1;
			for (int j = range.getFirstRow(); j < range.getFirstRow()+rowNum; j++) 
			{  
				Row row = sheet.getRow(j);  
				int cellNum = range.getLastColumn()- range.getFirstColumn()+1;  
				for (int k = range.getFirstColumn(); k < range.getFirstColumn()+cellNum; k++) 
				{  
					HSSFCell cell = (HSSFCell) row.getCell(k);
					cell.setCellValue(mergedvalue);
				}  
			}          
		}
		return sheet;
	} 

}
