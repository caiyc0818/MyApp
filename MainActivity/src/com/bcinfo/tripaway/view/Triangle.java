package com.bcinfo.tripaway.view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Triangle  extends View {  
    
    
    private Paint paint = new Paint();  
  
    
  
    private Context context;
  
    public Triangle(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context=context;
    }  
  
    public Triangle(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.context=context;
    }  
  
    public Triangle(Context context) {  
        super(context);  
        this.context=context;
    }  
  
    /** 
     * 重写这个方法 
     */  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        // 获取焦点改变背景颜色.  
        int height = getHeight();// 获取对应高度  
        int width = getWidth(); // 获取对应宽度  
       Path path1=new Path();  
       path1.moveTo(0, 0); 
       path1.lineTo(width, 0);  
       path1.lineTo(width/2, width/2*1.732f);  
       
      
       path1.close(); // 使这些点构成封闭的多边形  
       paint.setColor(Color.parseColor("#ffffff"));
       paint.setStyle(Paint.Style.FILL);//充满 
       canvas.drawPath(path1, paint); 
  
    }  
  
 
  
}  
