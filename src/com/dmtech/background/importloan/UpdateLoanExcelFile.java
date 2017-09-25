package com.dmtech.background.importloan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openqa.selenium.WebDriver;

import com.ys.utils.DateUtil;

/**
 * 修改借款人信息
 * @author wsl
 * 2017-09-11
 *
 */
public class UpdateLoanExcelFile {

	static WebDriver driver = null;
	private static String loanFile="E:\\工作\\导标模板\\小豆\\未发布";//标的文件， 可以是文件夹名字， 也可以是导标文件名

	private static String loanTitle="A002债权匹配1月期";//"w001auto自动1";// 标的的标题， 切记一定唯一
	private static String period = "1";
	private static String loanRate = "9";//债权利率
	private static String interestRate = "1";//平台加息
	private static String rePaymentMethod ="按月付息到期还本";
	private static String loanType  ="车险分期";

	private static String name ="宙疑";
	private static String mobile ="13400000005";
	private static String idCard ="632107200008236014";
	private static String bankCard ="4417122747352459";
	private static int loanMount = 100;



	public static void main(String[] args) throws InterruptedException {
		File loanFiles = new File(loanFile);
		if (!loanFiles.exists()){
			System.out.println("文件不存在!");
			return;
		}

		if (loanFiles.isDirectory()){
			File fa[] = loanFiles.listFiles();
			for (int i = 0; i < fa.length; i++) {
				loanFile = fa[i].getAbsolutePath();
				System.out.println(loanFile);
				getContentExcel2003(loanFile,";");

				System.out.println("Success update file:"+ loanFile+"!");
			}
		}else{
			getContentExcel2003(loanFile,";");
			System.out.println("Success update file:"+ loanFile+"!");
		}
		System.out.println("Update excel Success All!");
	}



	/**
	 * 读取Excel数据内容并修改相应的信息
	 * @param fileName
	 * @param delimiter//分割符
	 * @return String
	 */
	public static String getContentExcel2003(String  fileName, String delimiter){  
		try {

			int loanTitleIndex = 0;
			int loanTypeIndex =0; 
			int nameIndex = 0;
			int idCardIndex = 0;
			int bankCardIndex =0;
			int mobileIndex = 0;
			int bankMobileIndex =0;
			int loanTotalIndex =0;

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

							if ("借款类型".compareToIgnoreCase(strCell) ==0 ){
								loanTypeIndex = k;
							}


							if ("姓名".compareToIgnoreCase(strCell) ==0 ){
								nameIndex = k;
							}

							if ("身份证号码".compareToIgnoreCase(strCell) ==0 ){
								idCardIndex = k;
							}

							if ("手机号码".compareToIgnoreCase(strCell) ==0 ){
								mobileIndex = k;
							}

							if ("银行卡号".compareToIgnoreCase(strCell) ==0 ){
								bankCardIndex = k;
							}

							if ("银行预留手机号".compareToIgnoreCase(strCell) ==0 ){
								bankMobileIndex = k;
							}

							if (strCell.contains("申请贷款金额")){
								loanTotalIndex = k;
							}


						}

						if (j ==1){
							if (loanTypeIndex == k){
								loanType = strCell;
							}

							if (loanTitleIndex == k){
								loanTitle = DateUtil.getDateFormatHour()+loanType;
								cell.setCellValue(loanTitle);
							}

							if (loanTotalIndex == k){
								cell.setCellValue(loanMount);
							}
							if (bankMobileIndex == k){
								cell.setCellValue(mobile);
							}

							if (mobileIndex == k){
								cell.setCellValue(mobile);
							}

							if (nameIndex == k){
								cell.setCellValue(name);
							}
							if (idCardIndex == k){
								cell.setCellValue(idCard);
							}

							if (bankCardIndex == k){
								cell.setCellValue(bankCard);
							}

						}

					}  

				}
			}  

			FileOutputStream os = new FileOutputStream(fileName);  
			os.flush();  
			//将Excel写出        
			wb.write(os);  
			
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
