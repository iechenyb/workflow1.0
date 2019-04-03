package com.cyb.jbpm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
   private static SimpleDateFormat formatter = null;
   public static String format(Date date,String model){
	   formatter = new SimpleDateFormat(model);
	   String dateString = formatter.format(date);
	   return dateString;
   }
   public static String format(String date,String model){
	   formatter = new SimpleDateFormat(model);
	   String dateString = formatter.format(Calendar(date).getTime());
	   return dateString;
   }
   public static Long date2long8(Date date){
	   formatter = new SimpleDateFormat("yyyyMMdd");
	   String dateString = formatter.format(date);
	   return Long.valueOf(dateString);
   }
   public static Long date2long14(Date date){
	   formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	   String dateString = formatter.format(date);
	   return Long.valueOf(dateString);
   }
   public static Calendar Calendar(String yyyymmddhhmmss){
	Calendar cal = Calendar.getInstance();
	String dateStr = "20151122093045";
	int year = Integer.valueOf(yyyymmddhhmmss.substring(0, 4));
	int month = Integer.valueOf(yyyymmddhhmmss.substring(4, 6));
	int day = Integer.valueOf(yyyymmddhhmmss.substring(6, 8));
	int hour = Integer.valueOf(yyyymmddhhmmss.substring(8, 10));
	int min = Integer.valueOf(yyyymmddhhmmss.substring(10,12));
	int sec = Integer.valueOf(yyyymmddhhmmss.substring(12, 14));
	cal.set(year, month-1, day, hour, min, sec);
	return cal;
   }
   public static Calendar Calendar(Date date){
		Calendar cal = Calendar.getInstance();
		String dateStr = "20151122093045";
		
		/*int year = Integer.valueOf(yyyymmddhhmmss.substring(0, 4));
		int month = Integer.valueOf(yyyymmddhhmmss.substring(4, 6));
		int day = Integer.valueOf(yyyymmddhhmmss.substring(6, 8));
		int hour = Integer.valueOf(yyyymmddhhmmss.substring(8, 10));
		int min = Integer.valueOf(yyyymmddhhmmss.substring(10,12));
		int sec = Integer.valueOf(yyyymmddhhmmss.substring(12, 14));
		cal.set(year, month-1, day, hour, min, sec);*/
		cal.setTime(date);
		return cal;
	   }
   
   public static boolean between(String shhmmss,String ehhmmss){
	   String day = DateUtil.date2long8(new Date()).toString(); //20150102
		//09ï¼?:30-11:30 13:00-15:00
	   Calendar mornings = DateUtil.Calendar(day+shhmmss);
	   Calendar morninge = DateUtil.Calendar(day+ehhmmss);
	   Calendar curDate = DateUtil.Calendar(new Date());
	   if((curDate.after(mornings)&&curDate.before(morninge))){
		   return true;
	   	}else{
		   return false;
	    }
   }
   public static Date preDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
   }
   public static int getWeekOfYear(Date date) {
       Calendar c = new GregorianCalendar();
       c.setFirstDayOfWeek(Calendar.MONDAY);
       c.setMinimalDaysInFirstWeek(7);
       c.setTime(date);
       return c.get(Calendar.WEEK_OF_YEAR);
   }
	public static int getMaxWeekNumOfYear(int year) {
	        Calendar c = new GregorianCalendar();
	        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
	        return getWeekOfYear(c.getTime());
  }
	
	public static Date getFirstDayOfWeek(Date date) { 
		  Calendar c = new GregorianCalendar(); 
		  c.setFirstDayOfWeek(Calendar.MONDAY); 
		  c.setMinimalDaysInFirstWeek(7);
		  c.setTime(date); 
		  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday 
		  return c.getTime (); 
	}
	public static Date getLasttDayOfWeek(Date date) { 
		  Calendar c = new GregorianCalendar(); 
		  c.setFirstDayOfWeek(Calendar.MONDAY); 
		  c.setMinimalDaysInFirstWeek(7);
		  c.setTime(date); 
		  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()+6); // Monday 
		  return c.getTime (); 
	}
	public static int getWeekNumber(Date date){ 
	       Calendar calendar = Calendar.getInstance();
	        calendar.setFirstDayOfWeek(Calendar.MONDAY);
	        calendar.setMinimalDaysInFirstWeek(7);
	        calendar.setTime(date);
	        return calendar.get(Calendar.WEEK_OF_YEAR);
	 }  
  public static Date nextSomeDay(Date date,int i){
		 Calendar c = Calendar.getInstance();
	     c.setTime(date);
		 c.add(Calendar.DAY_OF_MONTH, i);
		 return c.getTime();
	 }
   public static void main(String[] args) {
	   String time = "20150101122300";
	   String res = DateUtil.format(time, "yyyy-MM-dd HH:mm:ss");
	   System.out.println(res.subSequence(11, res.length()-3));
	 
	//System.out.println(format(new Date(), "yyyyå¹? MMæœˆddæ—¥HHæ—¶mmåˆ†ssç§?"));
   }
}
