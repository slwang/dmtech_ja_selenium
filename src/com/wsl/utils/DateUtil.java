package com.wsl.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间处理类
 *
 */
public class DateUtil {

	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dn = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat dnhour = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dtime = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sfmh = new SimpleDateFormat("HHmmssSSS");
	
	/**
	 * 获取当前时间的后n分钟
	 * @return
	 */
    public static String getNext1min(String ...args){
		int time = Integer.parseInt(args[0]);
		
		Calendar now=Calendar.getInstance();
		 
		  now.add(Calendar.MINUTE,time);
		 
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
//		  future_timing = sdf.format(now.getTimeInMillis());
		  return sdf.format(now.getTimeInMillis());
		 
	}
    /**
     * 获取当前时间的前n分钟
     * @return
     */
	public static String getbefore1min(String ...args){
		int time = Integer.parseInt(args[0]);
		
		Calendar now=Calendar.getInstance();
		 
		  now.add(Calendar.MINUTE,-time);
		 
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		  return sdf.format(now.getTimeInMillis());
		  
		 
	}
	/**
	 * 获取当前时间
	 * @return String
	 */
	public static String getDate(){
		return df.format(new Date());
	}
	/**
	 * 获取上一年
	 * @return
	 */
	public static String getLastYear(){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR,-1);
		Date date = calendar.getTime();
		String dateString = df.format(date);
		return dateString;
	}
	/**
	 * 获取上一年一月一号
	 * @return
	 */
	public static String getBeginLastYear(){
		StringBuffer beginDate = new StringBuffer();
		beginDate.append(getLastYear().substring(0,4))
		.append("-01").append("-01");
		return beginDate.toString();
	}
	/**
	 * 获取上一年最后一天
	 * @return
	 */
	public static String getEndLastYear(){
		StringBuffer beginDate = new StringBuffer();
		beginDate.append(getLastYear().substring(0,4))
		.append("-12").append("-31");
		return beginDate.toString();
	}
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Date getStartDateDay(Date date) throws ParseException{
		String dateStr = df.format(date)+" 00:00:00";
		return sdf.parse(dateStr);
	}
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Date getEndDateDay(Date date) throws ParseException{
		String dateStr = df.format(date)+" 23:59:59";
		return sdf.parse(dateStr);
		
	}
	
	/**
	 * 获取当前时间并追加指定自然日
	 * @return String
	 */
	public static String getDateAddDays(int days){
		Date d=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, c.get(Calendar.DATE) +days);
		return df.format(new Date(c.getTimeInMillis()));
	}
	
	/**
	 * 判断2个日期之间是否是30天
	 * @param createDate 开始时间  格式：yyyy-MM-dd
	 * @param nowDate 结束时间  格式：yyyy-MM-dd
	 * @return boolean
	 */
	public static boolean is30Days(String createDate, String nowDate){
		
		int result = daysBetween(createDate, nowDate);
		if (result > 30) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取2个日期之间的天数
	 * @param createDate 开始时间   格式：yyyy-MM-dd
	 * @param nowDate 结束时间   格式：yyyy-MM-dd
	 * @return int
	 */
	public static int daysBetween(String createDate, String nowDate){
		int result = 0;
		try {
			Date cDate = df.parse(createDate);
			Date nDate = df.parse(nowDate);
			result = (int) ((nDate.getTime() - cDate.getTime())/(1000*60*60*24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取2个日期之间的天数
	 * @param createDate 开始时间   格式：yyyy/MM/dd
	 * @param nowDate 结束时间   格式：yyyy/MM/dd
	 * @return int
	 */
	public static int daysBetweenFor(String createDate, String nowDate){
		int result = 0;
		try {
			Date cDate = dtime.parse(createDate);
			Date nDate = dtime.parse(nowDate);
			result = (int) ((nDate.getTime() - cDate.getTime())/(1000*60*60*24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * String 转 date 
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date){
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * date 转String
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		return df.format(date);
	}
	/**
	 * 获取2个日期之间的天数
	 * @param createDate 开始时间   格式：yyyy-MM-dd
	 * @param nowDate 结束时间   格式：yyyy-MM-dd
	 * @return int
	 */
	public static int daysBetween(Date createDate, Date nowDate){
		int result = 0;
		result = (int) ((createDate.getTime() - nowDate.getTime())/(1000*60*60*24));
		return result;
	}
	/**
	 * 计算税龄
	 * @param createDate
	 * @param nowDate
	 * @return
	 */
	public static int yearBetween(Date createDate, Date nowDate){
		
		int days = Math.abs(daysBetween(createDate,nowDate));
		if(days <=365){
			return 1;
		}
		return (int) Math.ceil((double)days/365);
		//ceil 向上取整。 floor向下取整
	}
	
	/**
	 * 当前日期格式化 格式：yyyyMMdd
	 * @return String
	 */
	public static String getDateFormat(){
		return dn.format(new Date());
	}
	/**
	 * 当前日期格式化 格式：yyyyMMddHHmmss
	 * @return String
	 */
	public static String getDateFormatHour(){
		return dnhour.format(new Date());
	}
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String getDateFormat(String dateString) throws Exception{
		Date date =df.parse(dateString);
		return df.format(date);
	}
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String getDateFormat1(String dateString){
		Date date=null;
		try {
			date = df.parse(dateString);
			return df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 返回当月的第一天
	 * @return
	 */
	public static String getFirstDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return df.format(calendar.getTime());
	}
	/**
	 * 返回当月的最后一天
	 * @return
	 */
	public static String getLastDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return df.format(calendar.getTime());
	}
	 /**
	  *  得到 num  天后的日期   包括当天
	  * @param num 
	  * @return
	  */
	public static String getMorDay(int num){
		java.util.Calendar rightNow = java.util.Calendar.getInstance();
		rightNow.add(java.util.Calendar.DAY_OF_MONTH, num-1);
		return df.format(rightNow.getTime());
	}
	

	/**
	 * 指定某个日期某天后的日期
	 * @param beginTime
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public static String getLastDay(String beginTime, int i) throws Exception{
		java.sql.Date olddate = null;
		try {
			df.setLenient(false);
			olddate = new java.sql.Date(df.parse(beginTime).getTime());
		} catch (Exception e) {
			throw new RuntimeException("日期转换错误");
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(olddate);
		
		int Year = cal.get(Calendar.YEAR);
		int Month = cal.get(Calendar.MONTH);
		int Day = cal.get(Calendar.DAY_OF_MONTH);
		
		int NewDay = Day+i;
		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.MONTH, Month);
		cal.set(Calendar.DAY_OF_MONTH, NewDay);
		
		return df.format(new java.sql.Date(cal.getTimeInMillis()));
	}
	/**
	 * 获取年初第一天日期
	 * @return
	 */
	public static String getBeginYearDate(){
		StringBuffer beginDate = new StringBuffer();
		beginDate.append(getDate().substring(0,4))
		.append("-01").append("-01");
		return beginDate.toString();
	}
	
	/**
	 * 获取年末最后一天日期
	 * @return
	 */
	public static String getEndYearDate(){
		StringBuffer beginDate = new StringBuffer();
		beginDate.append(getDate().substring(0,4))
		.append("-12").append("-31");
		return beginDate.toString();
	}
	
	/**
	 * 获取上一个月的年
	 * @return
	 */
	public static String getPreviousYear(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String previousYear = String.valueOf(c.get(Calendar.YEAR));
		return previousYear;
	}
	
	/**
	 * 获取上一个月的月份
	 * @return
	 */
	public static String getPreviousMouth(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String previousMouth = String.valueOf(c.get(Calendar.MONTH)+1).length()==2?String.valueOf(c.get(Calendar.MONTH)+1):"0"+String.valueOf(c.get(Calendar.MONTH)+1);
		return previousMouth;
	}
	
	/**
	 * 获取上一个月的最后一天
	 * @return
	 */
	public static String getPreviousLastDay(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String previousLastDay = String.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return previousLastDay;
	}
	
	public String getNextDay(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(dateString == null || dateString.equals("")){
			return sdf.format(new Date());
		}
		Date date = sdf.parse(dateString);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 1);
		date = calendar.getTime();
		return sdf.format(date);
	}
	
	public String getToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取时分秒毫秒值
	 * @return
	 */
	public static Long getTimetoMillis(){
		return Long.getLong(sfmh.format(System.currentTimeMillis()));
	}
	
	/**
	 * 校验webservice请求时间是否正确
	 * @param tranDate
	 * @param tranTime
	 * @return
	 */
	public static boolean isRequestTime(String tranDate,Long tranTime){
		
		int cou = getDate().replaceAll("-", "").compareTo(tranDate);
		//判断当前日期是否大于等于传入的日期
		if (cou >= 0) {
			return true;
//			//获取当前时间时分秒毫秒数
//			Long nowTimes = new Long(sfmh.format(System.currentTimeMillis()));
//			//传入时间小于等于当前时间时分秒毫秒数
//			if (tranTime<=nowTimes) {
//				return true;
//			}else{//传入时间大于当前时间时分秒毫秒数
//				return false;
//			}
		}
		return false;
	}
	
	public static void main(String[] args) {
	}
}
