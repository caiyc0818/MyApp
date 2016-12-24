package com.bcinfo.tripaway.view.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;





/**
 * 日期格式转换
 * @author weichanghang
 * @version   1.0, 2015年7月8日 下午16:50:32
 */

public class MyDateFormat 
{
    // 把日期转为字符串
    public static String ConverToString(Date date,String format)
    {
        DateFormat df = new SimpleDateFormat(format);//format中填形如"yyyy/MM/dd HH:mm"
        if(date==null){
            return "- -";
        }
        return df.format(date);
    }

    // 把字符串转为日期
    public static Date ConverToDate(String strDate,String format)
    {
        DateFormat df = new SimpleDateFormat(format);//format中填形如 yyyyMMddHHmmss
        Date date=null;
        try
        {
            date=df.parse(strDate);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

}