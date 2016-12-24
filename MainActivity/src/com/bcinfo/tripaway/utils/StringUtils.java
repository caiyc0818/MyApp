package com.bcinfo.tripaway.utils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.loadIMG.TextImageLoadingListener;
import com.bcinfo.tripaway.view.TextViewAware;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.bcinfo.tripaway.view.span.TextClickSpan;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 字符串操作工具包
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月5日 上午10:27:23
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private final static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	 private final static Pattern phonenumber = Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13\\d{9}|14[57]\\d{8}|15\\d{9}|18\\d{9}$");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final static Type[] RICHTEXT_TYPES={Type.PIC,
		Type.LITPIC,
		Type.TEXT,
		Type.TOPIC,
		Type.MUSIC,
		Type.BOOK,
		Type.FILM}; 
	
	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}
		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param timeDate
	 * @return boolean
	 */
	public static boolean isToday(String timeDate) {
		boolean b = false;
		Date today = new Date();
		if (!TextUtils.isEmpty(timeDate)) {
			String nowDate = dateFormater2.format(today);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 将日期类型转位字符串
	 * 
	 * @return [参数说明]
	 */
	public static String dateToString() {
		Date today = new Date();
		return dateFormater.format(today);
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input) || input.equals("null"))
			return true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static String isEmptyReturn(String input) {
		if (input == null || "".equals(input) || input.equals("null"))
			return "";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return input;
			}
		}
		return "";
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	public static float toFloat(String str) {
		if (isEmpty(str) || !isNumeric(str)) {
			return 0;
		}
		float result = 0;
		try {
			result = Float.parseFloat(str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 字符是否为数值
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		int pointNum = 0;
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
				return false;
			}
			if (str.charAt(i) == '.') {
				pointNum++;
				if (pointNum > 1) {
					return false;
				}
			}
		}
		if (str.startsWith(".") || str.endsWith(".")) {
			return false;
		}
		return true;
	}

	/**
	 * 设置字串字体颜色
	 * 
	 * @param explain
	 * @param color
	 * @return
	 */
	public static SpannableString setStringColor(String explain, int color) {
		SpannableString spStr1 = new SpannableString(explain);
		NoLineClickSpan clickSpan1 = new NoLineClickSpan(color) {
			@Override
			public void onClick(View widget) {
			}
		};
		spStr1.setSpan(clickSpan1, 0, explain.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return spStr1;
		// applyProductExplain.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public static boolean isHttpImg(String imgUrl) {
		String str = imgUrl.substring(0, 4);
		if (str.equals("http")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将字符串转成unicode
	 * 
	 * @param str
	 *            待转字符串
	 * @return unicode字符串
	 */
	public static String strConvertUnicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String unicodeRevertString(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i <= str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}

	/**
	 * 用户的输入是否为email的方法
	 */
	public static boolean verifyIsEmail(String str) {
		Pattern pattern = Pattern
				.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用户输入是否为手机号码的方法
	 */
	public static boolean verifyIsPhone(String str) {
		Pattern pattern = Pattern
				.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13\\d{9}|14[57]\\d{8}|15\\d{9}|18\\d{9}$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
    /**
     * 判断是不是一个合法的手机号
     * 
     * @param email
     * @return
     */
    public static boolean isPhone(String phone)
    {
        if (phone == null || phone.trim().length() == 0)
            return false;
        return phonenumber.matcher(phone).matches();
    }
	/**
	 * 隐藏号码
	 * 
	 * @param str
	 * @return
	 */
	public static String getSecretStr(String str) {
		if (isEmpty(str)) {
			return str;
		}
		if (str.length() <= 7) {
			return str;
		}
		if (str.length() >= 11) {
			String strMiddle = str.substring(3, str.length() - 4);
			str = str.replace(strMiddle, "****");
		}
		return str;
	}

	/**
	 * 隐藏身份证号码
	 * 
	 * @param idNo
	 * @return
	 */
	public static String getIdNo(String idNo) {
		if (isEmpty(idNo)) {
			return idNo;
		}
		if (idNo.length() < 18) {
			return idNo;
		}
		String newString = "";
		if (idNo.length() >= 18) {
			char[] cc = idNo.toCharArray();
			for (int i = 0; i < cc.length; i++) {
				if (i > 5 && i < cc.length - 4) {
					newString += "*";
				} else {
					newString += cc[i];
				}
			}
		}
		return newString;
	}

	/***
	 * 护照号隐藏
	 * 
	 * @param passportNo
	 * @return
	 */
	public static String getPassportNo(String passportNo) {
		if (isEmpty(passportNo)) {
			return passportNo;
		}
		if (passportNo.length() <= 4) {
			return passportNo;
		}
		String newString = "";
		char[] cc = passportNo.toCharArray();
		if (passportNo.length() > 4) {
			for (int i = 0; i < cc.length; i++) {
				if (i > 3 && i <= 7) {
					newString += "*";
				} else {
					newString += cc[i];
				}
			}
		}
		return newString;
	}

	/***
	 * 得到最后倒数第二个#的位置
	 * 
	 * @param passportNo
	 * @return
	 */
	public static int getSecondLastIndex(String str) {
		int length = str.length();
		if (length <= 2)
			return -1;
		if (str.charAt(length - 1) != '#')
			return -1;
		String temString = str.substring(0, length - 1);
		return temString.lastIndexOf('#');
	}

	/***
	 * 得到富文本列表
	 * 
	 * @param
	 * @return
	 */
	public static void getRichText(TextView tv, List<RichText> richTexts) {
		Editable editable = tv.getEditableText();
		int w = (int) tv.getTextSize();
		String str = tv.getText().toString();
		if (str == null || str.equals(""))
			return;
		ForegroundColorSpan[] foregroundColorSpans = editable.getSpans(0,
				str.length(), ForegroundColorSpan.class);
		EmojiconSpan[] emojiconSpans = editable.getSpans(0, str.length(),
				EmojiconSpan.class);
		TextClickSpan[] textClickSpans = editable.getSpans(0, str.length(),
				TextClickSpan.class);
		for (ForegroundColorSpan span : foregroundColorSpans) {
			RichText richText = new RichText(Type.TOPIC);
			richText.setStart(editable.getSpanStart(span));
			richText.setEnd(editable.getSpanEnd(span));
			Map<String, String> attrs = richText.getAttrs();
			String string = str.substring(richText.getStart(),
					richText.getEnd());
			if (!StringUtils.isEmpty(string)) {
				richText.setText(string);
			}
			richTexts.add(richText);
		}
		for (TextClickSpan span : textClickSpans) {
			RichText richText = new RichText(span.getType());
			richText.setStart(editable.getSpanStart(span));
			richText.setEnd(editable.getSpanEnd(span));
			richText.setUri(span.getUri());
//			richText.setAttrs(span.getAttrs());
			if (span.getAttrs().containsKey("name1")) {
				richText.setText(span.getAttrs().get("name1"));
			}
			if (span.getAttrs().containsKey("name2")) {
				richText.getAttrs().put("name", span.getAttrs().get("name2"));
			}
			richText.getAttrs().put("desc", span.getAttrs().get("desc"));
			richTexts.add(richText);
		}
		for (EmojiconSpan span : emojiconSpans) {
			RichText richText;
			if (span.getmSize() == w)
				richText = new RichText(Type.LITPIC);
			else
				richText = new RichText(Type.PIC);
			richText.setStart(editable.getSpanStart(span));
			richText.setEnd(editable.getSpanEnd(span));
			richText.setUri(span.getUri());
			richText.setBitmap(((BitmapDrawable) span.getDrawable())
					.getBitmap());
			Bitmap bitmap=richText.getBitmap();
			float ratio=bitmap.getWidth()/(float)bitmap.getHeight();
			richText.getAttrs().put("ratio", Float.toString(ratio));
			richTexts.add(richText);
		}
		Collections.sort(richTexts, new Comparator<RichText>() {
			public int compare(RichText arg0, RichText arg1) {
				return arg0.getStart().compareTo(arg1.getStart());
			}
		});
		if (richTexts.size() > 0) {
			int i = 0;
			int start = 0;
			int end = 0;
			int size = richTexts.size();
			while (i != size + 1) {
				if (i == size)
					end = str.length();
				else
					end = richTexts.get(i).getStart();
				if (start != end) {
					String string = str.substring(start, end).replaceAll("\n",
							"");
					if (!string.equals("")) {
						RichText richText = new RichText(Type.TEXT);
						richText.setStart(start);
						richText.setEnd(end);
						if (!StringUtils.isEmpty(string)) {
							richText.setText(string);
						}
						richTexts.add(richText);
					}
				}
				if (i < size)
					start = richTexts.get(i).getEnd();
				++i;
			}
		}
		Collections.sort(richTexts, new Comparator<RichText>() {
			public int compare(RichText arg0, RichText arg1) {
				return arg0.getStart().compareTo(arg1.getStart());
			}
		});
		Iterator<RichText> it = richTexts.iterator();
		while (it.hasNext()) {// 去除宽度小的图片
			RichText richText = it.next();
			if (richText.getType() == Type.LITPIC)
				it.remove();
		}
		if(richTexts.size()==0&&str.length()>0){
			RichText richText = new RichText(Type.TEXT);
			richText.setStart(0);
			richText.setEnd(str.length());
				richText.setText(str);
			richTexts.add(richText);
		}
		System.out.print(22222);
	}

	public static String richTextToXml(List<RichText> richTexts) {
		StringXmlWriter billXml = new StringXmlWriter();
		for (RichText richText : richTexts) {

			Set<String> keyList = richText.getAttrs().keySet();
			if (!keyList.isEmpty()) {
				for (String key : keyList) {
					billXml.addAttributes(key, richText.getAttrs().get(key));// 在添加标签前，
																				// 你可以添加任意个属性
				}
			}
			billXml.createTag(richText.getType().name().toLowerCase());// 添加标签
			if (!StringUtils.isEmpty(richText.getText()))
				billXml.closeLastTag(richText.getText());
			else

				billXml.closeLastTag();

		}
		billXml.close();
		return billXml.toString();
	}

	
	/***
	 * 得到富文本列表
	 * 
	 * @param
	 * @return
	 */
	public static void delEditText(EditText tv) {
		Editable editable = tv.getEditableText();
		String str = tv.getText().toString();
		if (str == null || str.equals(""))
			return;
		int start = 0;
		int end = tv.getSelectionStart();
			ForegroundColorSpan[] foregroundColorSpans = editable.getSpans(0,
					str.length(), ForegroundColorSpan.class);
			TextClickSpan[] textClickSpans = editable.getSpans(0, str.length(),
					TextClickSpan.class);
			boolean isCheck = false;
			for (ForegroundColorSpan span : foregroundColorSpans) {
				if (editable.getSpanEnd(span) == end) {
					start = editable.getSpanStart(span);
					editable.removeSpan(span);
					isCheck = true;
					break;
				}
			}
			if (!isCheck)
				for (TextClickSpan span : textClickSpans) {
					if (editable.getSpanEnd(span) == end) {
						start = editable.getSpanStart(span);
						editable.removeSpan(span);
						if(StringUtils.isEmpty(span.getUri())){
							if(start>=1){
								EmojiconSpan[] emojiconSpans = editable.getSpans(start-1,
										start, EmojiconSpan.class);	
								if(emojiconSpans!=null&&emojiconSpans.length==1){
									start=start-1;
									editable.removeSpan(emojiconSpans[0]);
								}
							}
						}else{
							if(start>=2){
								EmojiconSpan[] emojiconSpans = editable.getSpans(start-2,
										start, EmojiconSpan.class);	
								if(emojiconSpans!=null&&emojiconSpans.length==2){
									start=start-2;
									editable.removeSpan(emojiconSpans[0]);
									editable.removeSpan(emojiconSpans[1]);
								}
							}
						}
						isCheck = true;
						break;
					}
				}
			if (isCheck) {
				editable.delete(start, end);
			} else {
				editable.delete(end - 1, end);
			}
	}
	
	
	public static List<RichText> xmlToRichText(String xml) {
		
		List<RichText> richTexts=new ArrayList<RichText>();
		ByteArrayInputStream tInputStringStream = null;  
		  try  
		  {  
			  if(xml!=null)
				{
				if(xml.contains("&"))
				xml = xml.replaceAll("&","&amp;");
				if(xml.contains("?"))
				xml = xml.replaceAll("\\?","&#63;");
				}
		  if (xml != null && !xml.trim().equals("")) {  
		   tInputStringStream = new ByteArrayInputStream(xml.getBytes());  
		  }  
		  }  
		  catch (Exception e) {  
		   // TODO: handle exception  
//		   tv1.setText(e.getMessage());  
		   return richTexts;  
		  }  
		  XmlPullParser parser = Xml.newPullParser(); 
		  RichText richText=null;
		  try {  
		   parser.setInput(tInputStringStream, "UTF-8");  
		   int eventType = parser.getEventType();  
		   while (eventType != XmlPullParser.END_DOCUMENT) {  
		    switch (eventType) {  
		    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理  
		    // persons = new ArrayList<Person>();  
		     break;  
		    case XmlPullParser.START_TAG:// 开始元素事件  
		     String name = parser.getName();
		     if(!StringUtils.isEmpty(name)){
		     for(int i=0;i<RICHTEXT_TYPES.length;i++){
		    	 if(name.equals(RICHTEXT_TYPES[i].name().toLowerCase())){
		    		  richText = new RichText(RICHTEXT_TYPES[i]);
		    		 for(int j=0;j<parser.getAttributeCount();j++){
		    			 String key=parser.getAttributeName(j);
		    			 String value=parser.getAttributeValue(j);
		    			 richText.getAttrs().put(key, value);
		    		 }
		    		 richTexts.add(richText);
		             break;
		    	 }
		     }
		     }
		     break;  
		    case XmlPullParser.END_TAG:// 结束元素事件  
		    	break;
		    case XmlPullParser.TEXT:
		    	 String text=parser.getText();
		    	 if(!StringUtils.isEmpty(text)&&richText!=null)
	    		 richText.setText(text);
		     break;  
		    }  
		    eventType = parser.next();  
		   }  
		   tInputStringStream.close();  
		   // return persons;  
		  } catch (XmlPullParserException e) {  
		   // TODO Auto-generated catch block  
		   e.printStackTrace();  
		  } catch (IOException e) {  
		   // TODO Auto-generated catch block  
		   e.printStackTrace();  
		  }  
		  if(richTexts.size()==0&&xml.length()>0){
			  RichText richText2=new RichText(Type.TEXT);
			  richText2.setText(xml);
			  richTexts.add(richText2);
		  }
		  return richTexts;
	}
	
	
	public static void initRichTextView(TextView tv,List<RichText> richTexts){
		Activity context=(Activity)tv.getContext();
		ContentResolver cr = context.getContentResolver();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
		int contentW = screenWidth - 2 * DensityUtil.dip2px(context, 10);
		if(richTexts!=null&&richTexts.size()>0)
			for(RichText richText:richTexts){
				Map<String, String> attrs=richText.getAttrs();
				String uri=richText.getUri();
				if(richText.getType()==Type.TEXT&&richText.getText()!=null){
					tv.append(richText.getText());
				}
				else if(richText.getType()==Type.TOPIC){
					TextStyleUtil.addForegroundColorSpan(tv, richText.getText());
				}
				else if(richText.getType()==Type.PIC){
					Drawable db;
					try {
						db = new BitmapDrawable(
								BitmapUtil.scaleImage4(
										Uri.parse("file://" + uri), context,
										contentW));
						tv.append("\n");
						TextStyleUtil.addImageSpan(tv, " ", db,
								contentW,uri);
						tv.append("\n");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(richText.getType()==Type.MUSIC||richText.getType()==Type.BOOK||richText.getType()==Type.FILM)
				{
					float w = DensityUtil.dip2px(TripawayApplication.application, 20);
					try {
						Drawable db = BitmapUtil.scaleImage3(
								Uri.parse("file://" + uri), TripawayApplication.application, w, w);
						TextStyleUtil.addImageSpan(tv, " ", db,
								uri);
						TextStyleUtil.addClickableSpan(tv, richText);
						if(tv instanceof EditText)
						((EditText)tv).setSelection(tv.getText().length());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
	
	
	public static void initRichTextView1(final TextView tv,List<RichText> richTexts){
		Activity context=(Activity)tv.getContext();
		ContentResolver cr = context.getContentResolver();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
		final int contentW = screenWidth - 2 * DensityUtil.dip2px(context, 10);
		Editable editable=tv.getEditableText();
		if(editable!=null){
//			editable.clearSpans();
			editable.clear();
		}
		if(richTexts!=null&&richTexts.size()>0)
			for(RichText richText:richTexts){
				Map<String, String> attrs=richText.getAttrs();
				String uri=richText.getUri();
				if(richText.getType()==Type.TEXT&&richText.getText()!=null){
					tv.append(richText.getText());
				}
				else if(richText.getType()==Type.TOPIC){
					TextStyleUtil.addForegroundColorSpan(tv, richText.getText());
				}
				else if(richText.getType()==Type.PIC){
					tv.append("\n");
					String url=richText.getText();
					String ratioStr=attrs.get("ratio");
					final int start=tv.getText().length();
					if(!isEmpty(url)&&!isEmpty(ratioStr)){
						float ratio=1;
						try {
							ratio=Float.parseFloat(ratioStr);
						} catch (NumberFormatException  e) {
							// TODO: handle exception
						}
//						TextStyleUtil.insertBigPicSpan(tv, " ", R.drawable.ic_launcher,
//								contentW,(int)(contentW/ratio),start);	
						 TextStyleUtil.addImageSpan(tv, " ",
									R.drawable.ic_launcher,contentW);
						TextViewAware viewAware=new TextViewAware(contentW,(int)(contentW/ratio));
					ImageLoader.getInstance().displayImage(Urls.imgHost
							+ url, viewAware,AppConfig.options(R.drawable.user_defult_photo), new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
						Drawable db=new BitmapDrawable(loadedImage);
							TextStyleUtil.insertImageSpan(tv, " ", db,
									null,contentW,start);
							
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
						}
					});
					}
					tv.append("\n");
					
				}else if(richText.getType()==Type.MUSIC||richText.getType()==Type.BOOK||richText.getType()==Type.FILM)
				{
					final int w = DensityUtil.dip2px(TripawayApplication.application, 20);
					String url=attrs.get("img");
					
						if (richText.getType()==Type.MUSIC)
							TextStyleUtil.addImageSpan(tv, " ",
									R.drawable.square_music);
						else if (richText.getType()==Type.BOOK)
							TextStyleUtil.addImageSpan(tv, " ",
									R.drawable.square_book);
						else if (richText.getType()==Type.FILM)
							TextStyleUtil.addImageSpan(tv, " ",
									R.drawable.square_movie);
						if(!isEmpty(url)){
						 int start=tv.getText().length();
						 TextStyleUtil.addImageSpan(tv, " ",
									R.drawable.ic_launcher);
						 TextViewAware viewAware=new TextViewAware(w,w);
					ImageLoader.getInstance().displayImage(Urls.imgHost
							+ url, viewAware,AppConfig.options(R.drawable.user_defult_photo), new TextImageLoadingListener(start, tv, w));
//					}
					}
					TextStyleUtil.addClickableSpan(tv, richText);
				}
			}
	}
	
	// 根据Unicode编码完美的判断中文汉字和符号
		private static boolean isChinese(char c) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
			if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
					|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
				return true;
			}
			return false;
		}
	 
		// 完整的判断中文汉字和符号
		public static boolean isChinese(String strName) {
			if(isEmpty(strName))
				return false;
			char[] ch = strName.toCharArray();
			for (int i = 0; i < ch.length; i++) {
				char c = ch[i];
				if (!isChinese(c)) {
					return false;
				}
			}
			return true;
		}
}
