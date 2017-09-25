package com.wsl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ReadExcel {
	private static String loanTitle="w001auto自动1";// 标的的标题， 切记一定唯一
	private static String period = "6";
	private static String loanRate = "7";//债权利率
	//private static String interestRate = "7";//平台加息
	private static String rePaymentMethod ="按月付息到期还本";
	
	 //loanTitle ：包含“品牌”
	//rePaymentMethod: 还款方式
	//period: 期限(月)
	//loanRate  : 还款利率
    static int loanTitleIndex = 0;
    static int rePaymentMethodIndex = 0;
    static int periodIndex = 0;
    static int loanRateIndex = 0;
    
    
    
    

	public static void main(String[] args) {
		String loanFile="E:\\工作\\导标模板\\使用的标的\\车险分期_等额本息.xls";//标的文件
		
		File f = new File("d:\\tttt2003.txt");
		String  content = convertExcel2003ToTxt(loanFile,f,";");
		
		
	}
	
	/**
     * 读取Excel数据内容
     * @param fileName
     * @param targetFile
     * @param delimiter//分割符
     * @return String
     */
	public static String convertExcel2003ToTxt(String  fileName, File  targetFile, String delimiter){  
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(fileName));
			int sheetNum = wb.getNumberOfSheets();  
			String lResult ="1:success";
			FileOutputStream o=null;  
			o = new FileOutputStream(targetFile);  
			int outNum =0;  
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
			            }
			            	
		                content +=strCell + delimiter; 
			            }  
		            if (i==sheetNum-1 && j==rowNum )
				    	;
				    else
			            content +="\r\n"; 
			            outNum++;
			            }
		        
	            //if (outNum>83){
	            	 o.write(content.getBytes("utf-8"));
	            	 System.out.println(content);
	            	 //content = "";
	            	// outNum =0;	
			            
			      //  }  	
	            	 
	            	 
	            	 System.out.println(loanTitle);
	            	 System.out.println(rePaymentMethod);
	            	 System.out.println(period);
	            	 System.out.println(loanRate);
			    }  
			    o.close(); 
			    return lResult;
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "10002:格式异常";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "10001:文件不存在";
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
