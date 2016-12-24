package com.bcinfo.tripaway.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期相关帮助类
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月16日 上午11:31:57
 */
public class DateUtil
{
    /**
     * 获取两个日期之间的所有日期
     * 
     * @param startDay
     * @param endDay
     * @return
     */
    public static ArrayList<String> getDaysBetweenDate(Calendar startDay, Calendar endDay)
    {
        if (startDay == null || endDay == null)
        {
            return null;
        }
        startDay.set(Calendar.HOUR, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        endDay.set(Calendar.HOUR, 0);
        endDay.set(Calendar.MINUTE, 0);
        endDay.set(Calendar.SECOND, 0);
        if (startDay.compareTo(endDay) > 0)
        {
            return null;
        }
        ArrayList<String> dateList = new ArrayList<String>();
        Calendar currentPrintDay = startDay;
        dateList.add(getFormateDate(currentPrintDay));
        while (true)
        {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 判断是否达到终了日
            if (currentPrintDay.compareTo(endDay) > 0)
            {
                break;
            }
            dateList.add(getFormateDate(currentPrintDay));
        }
        return dateList;
    }
    
    /**
     * 日期格式化 yy-MM-dd
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getFormateDate(int year, int month, int day)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("-");
        builder.append(month < 10 ? "0" + month : month);
        builder.append("-");
        builder.append((day < 10) ? "0" + day : day);
        return builder.toString();
    }
    
    /**
     * 日期格式化 yy-MM-dd
     * 
     * @param calendar
     * @return
     */
    public static String getFormateDate(Calendar calendar)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(calendar.get(Calendar.YEAR));
        builder.append("-");
        builder.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1)
            : (calendar.get(Calendar.MONTH) + 1));
        builder.append("-");
        builder.append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + calendar.get(Calendar.DAY_OF_MONTH)
            : calendar.get(Calendar.DAY_OF_MONTH));
        return builder.toString();
    }
    
    /**
     * 日期格式化 yy/MM/dd
     * 
     * @param time
     * @return
     */
    public static String formateDateToTimeStr(String time)
    {
        String timeOut = "2015年01月01日";
        if (time == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date date = sdfIn.parse(time);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            return timeOut;
        }
        return timeOut;
    }
    
    /**
     * 日期格式化 yy/MM/dd
     * 
     * @param time
     * @return
     */
    public static String formateDateToTime(String time)
    {
        String timeOut = "2015/01/01";
        if (time == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date date = sdfIn.parse(time);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            return timeOut;
        }
        return timeOut;
    }
    
    /**
     * 日期任意格式化
     * 
     * @param time
     * @return
     */
    public static String formateDateToTime1(String time,String formateStr)
    {
        String timeOut = "2016/02/01";
        if (time == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfOut = new SimpleDateFormat(formateStr);
        try
        {
            Date date = sdfIn.parse(time);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            return timeOut;
        }
        return timeOut;
    }
    
    /**
     * 将时间字符串format成 2015/07/20 12:38:00的形式
     * 
     * @param timeIn
     * @return
     */
    public static String getFormateDate(String timeIn)
    {
        String timeOut = "2015年01月01日 00:00:00";
        if (timeIn == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try
        {
            Date date = sdfIn.parse(timeIn);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeOut;
    }
    
    public static String getFormateDateWithOutSencond(String timeIn)
    {
        String timeOut = "2015年01月01日 00:00:00";
        if (timeIn == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try
        {
            Date date = sdfIn.parse(timeIn);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeOut;
    }
    
    public static String getFormateDate1(String timeIn)
    {
        String timeOut = "2015年01月01日 00:00:00";
        if (timeIn == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM-dd");
        try
        {
            Date date = sdfIn.parse(timeIn);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeOut;
    }
    
    public static String getFormateDate2(String timeIn)
    {
        String timeOut = "";
        if (timeIn == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy.MM");
        try
        {
            Date date = sdfIn.parse(timeIn);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeOut;
    }
    
    /**
     * 获取当前时间的毫秒数
     * 
     * @param time
     * @return
     */
    public static long getTimeInMillis(String time)
    {
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(time + "000000"));
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }
    
    /**
     * 将时间字符串转换成毫秒数
     * 
     * @param time
     * @return
     */
    public static long formatStrtoLong(String time)
    {
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(time));
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }
    
    /**
     * 获得当前时间的毫秒数
     * 
     * @return
     */
    public static long getcurrentTimeMillis()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTimeInMillis());
        // SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // String timeOut = sdfOut.format(cal.getTimeInMillis());
        // System.out.println("timeOut=" + timeOut);
        return cal.getTimeInMillis();
    }
    
    /**
     * 判断选择的开始时间结束时间是否符合产品周期
     * 
     * @param beginDate
     * @param endDate
     * @param day
     * @return
     */
    public static boolean isBeyondMaxDay(String beginDate, String endDate, int day)
    {
        long millis = getTimeInMillis(endDate) - getTimeInMillis(beginDate);
        long countDay = millis / (3600 * 24 * 1000);
        if (countDay + 1 == day)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 判断开始时间和结束时间
     * 
     * @param beginDate
     * @param endData
     * @return 开始时间小于等于结束时间返回true
     */
    public static boolean compareTime(String beginDate, String endData)
    {
        long beginMillis = getTimeInMillis(beginDate);
        long endMillis = getTimeInMillis(endData);
        if (beginMillis <= endMillis)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 获得当前时间字符串
     * 
     * @return
     */
    public static String getCurrentTimeStr()
    {
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timString = format.format(new Date());
        return timString;
    }
    
    /**
     * 获得当前时间的年月日
     * 
     * @return
     */
    public static String getCurrentDateStr()
    {
        Format format = new SimpleDateFormat("yyyy/MM/dd");
        String timString = format.format(new Date());
        return timString;
    }
    
    /**
     * 获得线路产品的结束时间
     * 
     * @param beginTime
     * @param day
     * @return
     */
    public static String getProductEndDateStr(String beginTime, int day)
    {
        long endTimeMillis = getTimeInMillis(beginTime) + (day - 1) * 1000 * 3600 * 24;
        Date date = new Date(endTimeMillis);
        Format format = new SimpleDateFormat("yyyy/MM/dd");
        String timString = format.format(date);
        return timString;
    }
    /**
     * 获取 任意时间的星期数；格式：2015-09-12
     * @param pTime
     * @return
     */
    public static String getWeek(String pTime) {

  	  
  	  String Week = "";


  	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  	  Calendar c = Calendar.getInstance();
  	  try {

  	   c.setTime(format.parse(pTime));

  	  } catch (ParseException e) {
  	   // TODO Auto-generated catch block
  	   e.printStackTrace();
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 1) {
  	   Week += "周日";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 2) {
  	   Week += "周一";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 3) {
  	   Week += "周二";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 4) {
  	   Week += "周三";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 5) {
  	   Week += "周四";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 6) {
  	   Week += "周五";
  	  }
  	  if (c.get(Calendar.DAY_OF_WEEK) == 7) {
  	   Week += "周六";
  	  }

  	  return Week;
  	 }
    
    /**
     * 获取任意时期的结束时间
     * @param datestr
     * @param day
     * @return
     */
    public static java.sql.Date getBeforeAfterDate(String datestr, int day) {  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        java.sql.Date olddate = null;  
        try {  
            df.setLenient(false);  
            olddate = new java.sql.Date(df.parse(datestr).getTime());  
        } catch (ParseException e) {  
            throw new RuntimeException("日期转换错误");  
        }  
        Calendar cal = new GregorianCalendar();  
        cal.setTime(olddate);  

        int Year = cal.get(Calendar.YEAR);  
        int Month = cal.get(Calendar.MONTH);  
        int Day = cal.get(Calendar.DAY_OF_MONTH);  

        int NewDay = Day + day;  

        cal.set(Calendar.YEAR, Year);  
        cal.set(Calendar.MONTH, Month);  
        cal.set(Calendar.DAY_OF_MONTH, NewDay);  

        return new java.sql.Date(cal.getTimeInMillis());  
    }  

    
    public static String setTime(String time){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date specifiedDate = sdf.parse(time);
			long specifiedMillions = specifiedDate.getTime();
			Date currentDate = new Date();
			long currentMillions = currentDate.getTime();

			double value = (currentMillions - specifiedMillions) / (3600 * 1000); // 获得的是小时数
			long minutesValue = (currentMillions - specifiedMillions) / (60 * 1000); // 获得的是分钟数
			// System.out.println("小时数:"+value);
			if (value > 24) {
				long daysValue = (long) value / 24;// 获得天数 估值
				System.out.println("天数:" + daysValue);
				time = daysValue + "天之前";

			} else {// 小于24小时 (一天的范围内)
				if (value >= 1) {

					System.out.println("小时数:" + value);
					time = (value + "").substring(0, (value + "").length() - 2) + "小时之前";

				} else {
					if (minutesValue >= 1 && minutesValue < 60) {
						System.out.println("分钟:" + minutesValue);
						time = minutesValue + "分钟之前";

					} else {
						time = "1秒钟之前";

					}
				}
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return time;
    }
    
}
