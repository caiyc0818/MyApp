package com.bcinfo.tripaway.utils;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 随机验证码工具类
 * @function
 * @author  caihelin
 * @version V1.0  2015年3月1日  19:27:22
 * 
 * 
 */
public class AuthCodeUtil
{
	private static Context context;
	// 定义char类型的数组
    private static final char[] CHARS =
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    private static AuthCodeUtil bpUtil;
    public AuthCodeUtil(Context context){
    	this.context=context;
    }
    // 获得单例对象
    public static AuthCodeUtil getInstance(Context context)
    {
        if (bpUtil == null)
        	
            bpUtil = new AuthCodeUtil(context);
        
        return bpUtil;
    }
    //canvas width and height
    // 定义画布(canvas)的初始的宽度和高度
    private int mWidth = 110, mHeight = 40;
    //random word space and pading_top  定义字符之间的间距
    private int base_padding_left = 15, base_padding_top = 35;
    //number of chars, lines; font size
    // codeLength表示随机生成的图片字符中  字符的个数是4；线条的个数�?�?每个字符的大小是30
    private int codeLength = 4, line_number = 3, font_size = 45;
    //variables  定义随机生成的四个字符所组成的字符串code
    private String code;
    private int padding_left, padding_top;
    // 定义随机数发生器
    private Random random = new Random();
// 创建位图bitmap 形参分别是宽度和高度(imageview的宽和高)
    public Bitmap createBitmap(int width,int height)
    {
    	// 创建生成位图对象  bitmap
        mWidth=width;
        mHeight=height;
        padding_left = 10;
        
        if(mWidth==0||mHeight==0)
        {
        	// 如果传�?过来的imageview的宽度和高度�?
            mWidth=DensityUtil.dip2px(context, 160);
            mHeight=DensityUtil.dip2px(context, 45);
            
        }
        // 根据传过来的imageview的宽度和高度 以及系统自带的Config.ARGB_8888颜色定义
        // 使用Bitmap位图对象来产生位�? 这个位图是可交互�?
        Bitmap bp = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
        // Canvas表示画布  定义�?��在画�?canvas)上面的bitmap位图(bp)对象
        //  Construct a canvas with the specified bitmap to draw into. The bitmap must be mutable.
        Canvas c = new Canvas(bp);
        // 产生随机的字符串
        code = createCode();
        // 填充画布的颜�? 为白�?即bitmap位图的颜色是白色�?
        // 用白色来填充画布上面的位图bitmap
        c.drawColor(Color.BLACK);
        // 定义画笔
        Paint paint = new Paint();
        // 设置画笔大小  字符大小30
        paint.setTextSize(font_size);
        // for循环迭代每个字符   �?个字�? code.length=4
        for (int i = 0; i < code.length(); i++)
        {
        	// 设置随机的文本样�? 参数是画笔paint
            randomTextStyle(paint);
            
            // 设置文字的间�?
            randomPadding();
            // 画text文本内容
            
            c.drawText(code.charAt(i) + "", mWidth*i/code.length()+base_padding_left, padding_top, paint);
        }
        // 下面的for循环  是在bitmap位图上面画随机的3段线�?
        
        for (int i = 0; i < line_number; i++)
        {
            drawLine(c, paint);
        }
        // 标记位ALL_SAVE_FLAG表示了当restore()被调用的时�?，保存所画的�?��
        
        
        c.save(Canvas.ALL_SAVE_FLAG);//保存  
        // restore()方法是将上一次save()方法读出�? 应用之前设置的画笔paint�?方便可以不用再次设置canvas的画笔颜色粗细等
        
        c.restore();//
        // 返回画好的bitmap对象
        return bp;
    }

    public String getCode()
    {
        return code;
    }

    private String createCode()
    {
    	// 定义产生随机数的方法
        StringBuilder buffer = new StringBuilder();
        // codeLength�?  0,1,2,3
        for (int i = 0; i < codeLength; i++)
        {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }
// 画线
    private void drawLine(Canvas canvas, Paint paint)
    {
        int color = randomColor();
        // 线条两端�?��端的x坐标
        int startX = random.nextInt(mWidth);
        // y坐标
        int startY = random.nextInt(mHeight);
        int stopX = random.nextInt(mWidth);
        int stopY = random.nextInt(mHeight);
        //设置线条的宽�?
        paint.setStrokeWidth(1);
        paint.setColor(color);
        // 画线
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor()
    {
    	// 参数1 表示rate 颜色�?
        return randomColor(1);
    }

    private int randomColor(int rate)
    {
    	//[0,255]颜色
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        // 返回�?��红，绿，蓝混合的颜色int数�?  
        //  Color.rgb(red, green, blue)方法
        
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint)
    {
    	// randomColor()方法返回了一个由红，绿，蓝三种颜色组成的混合颜色的颜色int类型数�?
        int color = randomColor();
        // 设置画笔颜色
        paint.setColor(color);
        
        // 画笔设置文字text是否是粗体样�?
        paint.setFakeBoldText(random.nextBoolean()); //true为粗体，false为非粗体
        
        
        
        float skewX = random.nextInt(11) / 10;  // [0-10]/10
        
        skewX = random.nextBoolean() ? skewX : -skewX;// 正数是粗体，负数是斜�?   skewx�?-1的小�?
        
        // 设置文字方向
        // 设置文字是否倾斜
        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
        //      paint.setUnderlineText(true); //true为下划线，false为非下划�?
        //      paint.setStrikeThruText(true); //true为删除线，false为非删除�?
    }

    private void randomPadding()
    {
    	// 定义字符之间的随机的padding间隔
        padding_left += base_padding_left + random.nextInt(base_padding_left);
        padding_top = base_padding_top + random.nextInt(base_padding_top);
    }
}
