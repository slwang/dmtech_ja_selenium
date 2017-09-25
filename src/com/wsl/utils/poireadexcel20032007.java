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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFCell;




public class poireadexcel20032007   {
	


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Date startdate =new Date();
		 excel2txtPOI();
		Date enddate =new Date();
		long time = enddate.getTime() - startdate.getTime();
		System.out.println("耗时：  " +time);
	}

	public static void excel2txtPOI() throws Exception{ 
		//String fileName="c:\\simpleexcel.xls";
		//String fileName="c:\\simpleexcel1000000.xlsx";//最简单的excel   OK
		//String fileName="c:\\excelmultsheet.xlsx";  //多sheet的excel    OK
		//String fileName="c:\\simpleexcel2sheet1000000.xlsx";  //合并单元格
		String fileName="c:\\test3.xlsx"; 
		//String fileName="c:\\test.xlsx";
		//String fileName="c:\\testdata\\xlsx\\2010.xlsx";
		File f = new File("c:\\tttt2007.txt");
		File sourceFile = new File(fileName);
		if (!sourceFile.exists())
		{
			System.out.print("file not exists");
		}
		else		
		 convertExcel2007ToTxt(fileName,f, "/分隔符/");
		
	}
	
	/**
     * 读取Excel数据内容
     * @param fileName
     * @param targetFile
     * @param delimiter//分隔符
     * @return String
     */
	public static String convertExcel2007ToTxt(String  fileName, File  targetFile, String delimiter){  
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(fileName));
			int sheetNum = wb.getNumberOfSheets();  
			String lResult ="1:success";
			FileOutputStream o=null;  
			o = new FileOutputStream(targetFile);  
			int outNum =0;   
			for (int i = 0; i < sheetNum; i++) 
			{  
				Sheet childSheet = wb.getSheetAt(i);  
				if (childSheet==null)
			      	continue;
			    int rowNum = childSheet.getLastRowNum();  
			    int isMergeRegions = childSheet.getNumMergedRegions();
			    if (isMergeRegions>0){
			       	childSheet = unMergedCell(childSheet);
			    }
			    for (int j = 0; j <= rowNum; j++) 
			    {  
			        Row row = childSheet.getRow(j);
			        if (row ==null)
		            	continue;
			        
			        int cellNum = row.getLastCellNum();  
			        String content ="";
			        for (int k = 0; k < cellNum; k++) {  
				      	XSSFCell cell = (XSSFCell) row.getCell(k);
				      	if (cell ==null)
		            	{
				      		content += delimiter; 
		            		continue;
			            }
				      	int cellType = cell.getCellType();
				        String strCell="";
				        switch (cellType) 
				        {
					        case XSSFCell.CELL_TYPE_NUMERIC:// 0 Numeric
					          	if (HSSFDateUtil.isCellDateFormatted(cell))
					          	{
					          		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						           	strCell =sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			                	}
			                	else{
		//			                		 Object inputValue = null;// 单元格值
		//			                		 long longVal = Math.round(cell.getNumericCellValue());  
		//			                		     if(Double.parseDouble(longVal + ".0") == doubleVal)  
		//			                		         inputValue = longVal;  
		//			                		     else  
		//			                              inputValue = doubleVal; 
		
					                	
			                		// 将被表示成1.3922433397E10的手机号转化为13922433397,不一定是最好的转换方法
					                  DecimalFormat df = new DecimalFormat("#");
					                  strCell = df.format(cell.getNumericCellValue());
					                }
					            break;
					        case XSSFCell.CELL_TYPE_STRING://  1  String
					          	strCell = cell.getStringCellValue();
					           	break;
					        case Cell.CELL_TYPE_BOOLEAN:
					           	strCell =String.valueOf(cell.getBooleanCellValue());
					           	break;		                	
					        case Cell.CELL_TYPE_FORMULA:
						        try {  
						        	/* 
						            * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)判断过程中cell 
						            * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常 
						            */  
						            if (HSSFDateUtil.isCellDateFormatted(cell)) {  
						            	Date date = cell.getDateCellValue();  
						                strCell = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) +"-" + date.getDate();	  
						                break;  
						           	} else 
						           	{  
						           		DecimalFormat df = new DecimalFormat("#");
						           		strCell = df.format(cell.getNumericCellValue());
						            }  
							        } catch (IllegalStateException e) {  
							        	strCell = String.valueOf(cell.getRichStringCellValue());  
							        }  
						        	break;
					        default:
				        }	
				        content +=strCell + delimiter; 
			        }  
			    if (i==sheetNum-1 && j==rowNum )
			    	;
			    else
			    	content +="\r\n"; 
				o.write(content.getBytes("utf-8"));
				outNum++;
            	if (outNum>20)
            		outNum =0;
			    }  		  
	        }  
			o.close();
		    return lResult;
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "10002:格式异常";
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "10001:文件不存在";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	    	
	    	 XSSFCell mergedcell = (XSSFCell) sheet.getRow(range.getFirstRow()).getCell(range.getFirstColumn());

	    	 String  mergedvalue =mergedcell.getStringCellValue();
	    	 
	    	 sheet.removeMergedRegion(i) ;
	    	 num =num -1;
	    	 int rowNum =range.getLastRow()-range.getFirstRow()+1;
	    	 for (int j = range.getFirstRow(); j < range.getFirstRow()+rowNum; j++) {  
		            Row row = sheet.getRow(j);  
		            int cellNum = range.getLastColumn()- range.getFirstColumn()+1;  
		  
		            for (int k = range.getFirstColumn(); k < range.getFirstColumn()+cellNum; k++) {  
		            	XSSFCell cell = (XSSFCell) row.getCell(k);
		            	cell.setCellValue(mergedvalue);
		            }  
		           
		        }          
	     }
	     return sheet;
	 } 
	

}


