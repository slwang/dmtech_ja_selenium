package com.wsl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class poireadexcel19972003 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date startdate =new Date();
		try {
			excel2txtPOI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date enddate =new Date();
		long time = enddate.getTime() - startdate.getTime();
		System.out.println("耗时：  " +time);
	}
	
	public static void excel2txtPOI() throws Exception{ 
				
		//String fileName="c:\\13694673373MBBliu.xls";
		String fileName="d:\\workspace\\hkj\\2007企业会计准则.xls";
		
		
		File f = new File("d:\\tttt2003.txt");
		String  content = convertExcel2003ToTxt(fileName,f,";");
		
		//writeTxtFile(content,f);	
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
			            	
		                content +=strCell + delimiter; 
			            }  
		            if (i==sheetNum-1 && j==rowNum )
				    	;
				    else
			            content +="\r\n"; 
			            outNum++;
			            }
		        
	            if (outNum>20){
	            	 o.write(content.getBytes("utf-8"));
	            	 content = "";
	            	 outNum =0;	
			            
			        }  		  
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
