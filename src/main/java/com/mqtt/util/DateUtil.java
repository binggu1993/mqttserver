package com.mqtt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 操作日期类型数据的工具类
 * 
 * @author Administrator
 * @version 1.0, 2011-7-28
 * @since
 */
@SuppressWarnings("all")
public class DateUtil {
	
	//日期格式
	private static final String DATE_PATTERN_1 = "yyyy-MM-dd";
	private static final String DATE_PATTERN_2 = "yyyyMMdd";
	private static final String DATE_PATTERN_3 = "yyyy/MM/dd";
	
	//时间格式
	private static final String TIME_PATTERN_1 = "HH:mm:ss";
	private static final String TIME_PATTERN_2 = "HHmmss";
	
	//日期时间格式
	public static final String DATETIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATETIME_PATTERN_2 = "yyyyMMddHHmmss";
	
	private static final String TIMESTAMP = "yyyyMMddHHmmsssss";

	
    private  static  ThreadLocal<SimpleDateFormat> dateFor1 = new ThreadLocal<SimpleDateFormat>();    
    //private  static  ThreadLocal<SimpleDateFormat> dateFor2 = new ThreadLocal<SimpleDateFormat>();
    //private  static  ThreadLocal<SimpleDateFormat> dateFor3 = new ThreadLocal<SimpleDateFormat>();
   //private  static  ThreadLocal<SimpleDateFormat> timeFor1 = new ThreadLocal<SimpleDateFormat>();
    //private  static  ThreadLocal<SimpleDateFormat> timeFor2 = new ThreadLocal<SimpleDateFormat>();
    private  static  ThreadLocal<SimpleDateFormat> datetimeFor1 = new ThreadLocal<SimpleDateFormat>();
    private  static  ThreadLocal<SimpleDateFormat> datetimeFor2 = new ThreadLocal<SimpleDateFormat>();
    //private  static  ThreadLocal<SimpleDateFormat> datetimeFor3 = new ThreadLocal<SimpleDateFormat>();
    public  static  final  SimpleDateFormat getDateFormat1(){
    	if(null == dateFor1.get()){
    		dateFor1.set(new SimpleDateFormat(DATE_PATTERN_1));
    	}
    	return dateFor1.get();
    }
//    public  static  final  SimpleDateFormat getDateFormat2(){
//    	if(null == dateFor2.get()){
//    		dateFor2.set(new SimpleDateFormat(DATE_PATTERN_2));
//    	}
//    	return dateFor2.get();
//    }
//    public  static  final  SimpleDateFormat getDateFormat3(){
//    	if(null == dateFor3.get()){
//    		dateFor3.set(new SimpleDateFormat(DATE_PATTERN_3));
//    	}
//    	return dateFor3.get();
//    }
//    public  static  final  SimpleDateFormat getTimeFormat1(){
//    	if(null == timeFor1.get()){
//    		timeFor1.set(new SimpleDateFormat(TIME_PATTERN_1));
//    	}
//    	return timeFor1.get();
//    }
//    public  static  final  SimpleDateFormat getTimeFormat2(){
//    	if(null == timeFor2.get()){
//    		timeFor2.set(new SimpleDateFormat(TIME_PATTERN_2));
//    	}
//    	return timeFor2.get();
//    }    
    public  static  final  SimpleDateFormat getDateTimeFormat1(){
    	if(null == datetimeFor1.get()){
    		datetimeFor1.set(new SimpleDateFormat(DATETIME_PATTERN_1));
    	}
    	return datetimeFor1.get();
    }	
    public  static  final  SimpleDateFormat getDateTimeFormat2(){
    	if(null == datetimeFor2.get()){
    		datetimeFor2.set(new SimpleDateFormat(DATETIME_PATTERN_2));
    	}
    	return datetimeFor2.get();
    }	
//    public  static  final  SimpleDateFormat getDateTimeFormat3(){
//    	if(null == datetimeFor3.get()){
//    		datetimeFor3.set(new SimpleDateFormat(TIMESTAMP));
//    	}
//    	return datetimeFor3.get();
//    }	
    
	
	//private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	
	//private static final SimpleDateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	//private static final SimpleDateFormat DATE_FORMAT_HH_MM_SS = new SimpleDateFormat("HH:mm:ss");
	/**
	 * 返回日期格式化
	 * @throws ParseException 
	 */
	public static Date parseDate(String source, String pattern)
			throws ParseException  {
			return new SimpleDateFormat(pattern).parse(source);
	}
	
	/**
	 * 获取当前日期
	 * @return String YYYY-MM-DD
	 */
	public static String getCurrentDateStr1() {
		return getDateFormat1().format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * @return String YYYY-MM-DD
	 */
//	public static String getCurrentDateStr2() {
//		return dateFor2.get().format(new Date());
//	}
	
	/**
	 * 获取当前日期
	 * @return String YYYY-MM-DD
	 */
//	public static String getCurrentDateStr3() {
//		return dateFor3.get().format(new Date());
//	}
	
	
	/**
	 * 获取当前时间
	 * @return String HH:mm:ss
	 */
//	public static String getCurrentTimeStr1() {
//		return timeFor1.get().format(new Date());
//	}
	
	/**
	 * 获取当前时间
	 * @return String HHmmss
	 */
//	public static String getCurrentTimeStr2() {
//		return timeFor2.get().format(new Date());
//	}
	
	/**
	 * 获取当前时间
	 * @param format
	 * @return
	 */
	public static String getCurrentTimeStr() {
	   return  getDateTimeFormat1().format(new Date());
	}
	

	/**
	 * 获取当前日期时间
	 * @return String yyyyMMddHHmmss
	 */
//	public static String getTimeStamp() {
//		return datetimeFor3.get().format(new Date());
//	}
	
	
//	public static String formatDate(Date d) {
//		return dateFor1.get().format(d);
//	}
	
	
//	public static String formatTime(Date d) {
//		return timeFor1.get().format(d);
//	}
	

	
	public static String formatDateTime(Date d) {
		return getDateTimeFormat1().format(d);
	}
	public static String formatDateTime2(Date d) {
		return getDateTimeFormat2().format(d);
	}
	
	public static int getCurYear()
    {
        Calendar today = GregorianCalendar.getInstance();
        return today.get(Calendar.YEAR);
    }

    public static int getCurMonth()
    {
        Calendar today = GregorianCalendar.getInstance();
        return today.get(Calendar.MONTH) + 1;
    }

    public static int getCurDay()
    {
        Calendar today = GregorianCalendar.getInstance();
        return today.get(Calendar.DATE);
    }
	
	/**
     * 获取前后N年的年集合
     * 
     * @return
     */
    public static List<Integer> getYears(int prevYearNumber, int nextYearNumber)
    {
        List<Integer> years = new ArrayList<Integer>();
        Integer curYear = DateUtil.getCurYear();
        for (int i = curYear - prevYearNumber; i <= curYear + nextYearNumber; i++)
        {
            years.add(i);
        }
        return years;
    }
    
    /**
     * 获取月列表
     * @方法名称：
     * @方法描述：
     * @引用表：
     * @详细流程：
     *
     * @param n 
     *   n = 0 返回[1...12]
     *   n = 1.. 12 返回  [1.. n]
     *   n = -1 返回[1... 当前月]
     * @return
     */
    public static List<Integer> getMonths(int n)
    {
        if (n == 0)
        {
            n = 12; // 返回12个月
        }
        else if (n == -1)
        {
            n = DateUtil.getCurMonth();
        }
        
        List<Integer> month = new ArrayList<Integer>();
        
        for (int i = 1; i <= n; i++)
        {
            month.add(i);
        }
        return month;
    }
    
    /**
     * 获取前/后几个月的第一天
     * 
     * @return
     * @throws Exception
     */
    public static Calendar getFirstDayByAddMonth(int n)
    {
        Calendar lastDate = GregorianCalendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, n);// 减N个月，变为上月的1号
        return lastDate;
    }

    /**
     * 
     * @方法名称：格式化一个字符串日期
     * @方法描述：
     *
     * @param strDate
     * @param pattren
     * @return
     * @throws Exception
     */
    public static String formatDate(String strDate, String pattren) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattren);
        Date d = sdf.parse(strDate);
        String date = sdf.format(d);
        return date;
    }
    
    /**
     * 
     * @方法名称：对某日期加减月
     *
     * @param now
     * @param n
     * @return
     */
    public static Date addMonth(Date date, int n)
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        
        calendar.add(Calendar.MONTH, n);
        
        return new Date(calendar.getTimeInMillis());
    }
    
    /**
     * 
     * @方法名称：对某日期加减天
     *
     * @param now
     * @param n
     * @return
     */
    public static Date addDay(Date date, int n)
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        
        calendar.add(Calendar.DAY_OF_MONTH, n);
        
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date addMinute(Date date, int n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        calendar.add(Calendar.MINUTE, n);

        return new Date(calendar.getTimeInMillis());
    }
    
    /**
     * 对日期的秒加减
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addSecond(Date date, int n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        calendar.add(Calendar.SECOND, n);

        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date toDate(String dateStr) {
    	try {
			return datetimeFor1.get().parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    public static Date getCurrentDateTime() {
        return Calendar.getInstance().getTime();
    }
public static void main(String[] args)
{
    System.out.println(getCurrentTimeStr());;
}
}
