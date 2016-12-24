package com.bcinfo.tripaway.utils;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
/*
 * 自定义方法，将图片处理成圆角图片
 * */
public class ImageUtils {
	//
		public static  Bitmap getRoundBitmap(Bitmap bit) {
			Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Config.ARGB_8888);
			
			
			Canvas canvas = new Canvas(bitmap);
			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.YELLOW);
			//绘制一个作为基准的圆角矩形
			RectF rect = new RectF(0, 0, bit.getWidth(), bit.getHeight());
			canvas.drawRoundRect(rect, 10, 10, paint);
			
			//设置相交后的保留原则
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			
			canvas.drawBitmap(bit, 0, 0, paint);
			
			return bitmap;
		}
}
