package com.bcinfo.tripaway.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;





import com.bcinfo.tripaway.TripawayApplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * TODO<请描述这个类是干什么的>
 * 
 * @author xjp
 * @data: 2014年9月18日 上午10:10:30
 * @version: V1.0
 */
public class BitmapUtil
{
    private static final String TAG = "BitmapUtils";
    
    /**
     * TODO<将控件转换成bitmap类型>
     * 
     * @throw
     * @return Bitmap
     * @param paramView ：需要转换的控件
     */
    public static Bitmap convertViewToBitmap(View paramView)
    {
        paramView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        paramView.layout(0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
        paramView.buildDrawingCache();
        return paramView.getDrawingCache();
    }
    
    /**
     * TODO<创建倒影图片>
     * 
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap, int reflectionHeight)
    {
        if (null == srcBitmap)
        {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }
        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 0;
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        if (0 == srcWidth || srcHeight == 0)
        {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }
        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        try
        {
            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap =
                Bitmap.createBitmap(srcBitmap,
                    0,
                    srcHeight - reflectionHeight,
                    srcWidth,
                    reflectionHeight,
                    matrix,
                    false);
            if (null == reflectionBitmap)
            {
                Log.e(TAG, "Create the reflectionBitmap is failed");
                return null;
            }
            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection =
                Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, Bitmap.Config.ARGB_8888);
            if (null == bitmapWithReflection)
            {
                return null;
            }
            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);
            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);
            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader =
                new LinearGradient(0, srcHeight, 0, bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70FFFFFF,
                    0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.save();
            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth, bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
            if (reflectionBitmap != null && !reflectionBitmap.isRecycled())
            {
                reflectionBitmap.recycle();
                reflectionBitmap = null;
            }
            canvas.restore();
            return bitmapWithReflection;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e(TAG, "Create the reflectionBitmap is failed");
        return null;
    }
    
    /**
     * TODO<图片圆角处理>
     * 
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param ret 圆角的度数
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret)
    {
        if (null == srcBitmap)
        {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }
        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();
        BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        RectF rectf = new RectF(0, 0, bitWidth, bitHight);
        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();
        return outBitmap;
    }
    
    /**
     * TODO<图片沿着Y轴旋转一定角度>
     * 
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @param rotate 图片旋转的角度
     */
    public static Bitmap skewImage(Bitmap srcBitmap, float rotate, int reflectionHeight)
    {
        if (null == srcBitmap)
        {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }
        Bitmap reflecteBitmap = createReflectedBitmap(srcBitmap, reflectionHeight);
        if (null == reflecteBitmap)
        {
            Log.e(TAG, "failed to createReflectedBitmap");
            return null;
        }
        int wBitmap = reflecteBitmap.getWidth();
        int hBitmap = reflecteBitmap.getHeight();
        float scaleWidth = ((float)180) / wBitmap;
        float scaleHeight = ((float)270) / hBitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        reflecteBitmap = Bitmap.createBitmap(reflecteBitmap, 0, 0, wBitmap, hBitmap, matrix, true);
        Camera localCamera = new Camera();
        localCamera.save();
        Matrix localMatrix = new Matrix();
        localCamera.rotateY(rotate);
        localCamera.getMatrix(localMatrix);
        localCamera.restore();
        localMatrix.preTranslate(-reflecteBitmap.getWidth() >> 1, -reflecteBitmap.getHeight() >> 1);
        Bitmap localBitmap2 =
            Bitmap.createBitmap(reflecteBitmap,
                0,
                0,
                reflecteBitmap.getWidth(),
                reflecteBitmap.getHeight(),
                localMatrix,
                true);
        Bitmap localBitmap3 =
            Bitmap.createBitmap(localBitmap2.getWidth(), localBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap3);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
        if (null != reflecteBitmap && !reflecteBitmap.isRecycled())
        {
            reflecteBitmap.recycle();
            reflecteBitmap = null;
        }
        if (null != localBitmap2 && !localBitmap2.isRecycled())
        {
            localBitmap2.recycle();
            localBitmap2 = null;
        }
        localCanvas.save();
        localCanvas.restore();
        return localBitmap3;
    }
    
    /**
     * TODO<图片模糊化处理>
     * 
     * @throw
     * @return Bitmap
     * @param bitmap 源图片
     * @param radius The radius of the blur Supported range 0 < radius <= 25
     * @param context 上下文
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context)
    {
        if (bitmap == null)
        {
            return null;
        }
        // Let's create an empty bitmap with the same size of the bitmap we want
        // to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);
        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // Create the Allocations (in/out) with the Renderscript and the in/out
        // bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        // Set the radius of the blur
        if (radius > 25)
        {
            radius = 25.0f;
        }
        else if (radius <= 0)
        {
            radius = 1.0f;
        }
        blurScript.setRadius(radius);
        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        bitmap.recycle();
        bitmap = null;
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }
    
    /**
     * TODO<给图片添加指定颜色的边框>
     * 
     * @param srcBitmap 原图片
     * @param borderWidth 边框宽度
     * @param color 边框的颜色值
     * @return
     */
    public static Bitmap addFrameBitmap(Bitmap srcBitmap, int borderWidth, int color)
    {
        if (srcBitmap == null)
        {
            Log.e(TAG, "the srcBitmap or borderBitmap is null");
            return null;
        }
        int newWidth = srcBitmap.getWidth() + borderWidth;
        int newHeight = srcBitmap.getHeight() + borderWidth;
        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        // 设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);
        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (srcBitmap != null && !srcBitmap.isRecycled())
        {
            srcBitmap.recycle();
            srcBitmap = null;
        }
        return outBitmap;
    }
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 先将inJustDecodeBounds属性设置为true,解码避免内存分配
        options.inJustDecodeBounds = true;
        // 将图片传入选择器中
        BitmapFactory.decodeResource(res, resId, options);
        // 对图片进行指定比例的压缩
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 待图片处理完成后再进行内存的分配，避免内存泄露的发生
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    
    // 计算图片的压缩比例
    public static int calculateInSampleSize(BitmapFactory.Options option, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = option.outHeight;
        final int width = option.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth)
        {
            final int heightRatio = Math.round((float)height / (float)reqHeight);
            final int widthRatio = Math.round((float)width / (float)reqWidth);
            // 选择长宽高较小的比例，成为压缩比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    
    /**
     * Creates a centered bitmap of the desired size.
     * 
     * @param source original bitmap source
     * @param width targeted width
     * @param height targeted height
     */
    public static Bitmap Thumbnail(Bitmap bm, int reqWidth, int reqHeight)
    {
        Bitmap bmp = null;
        bmp = ThumbnailUtils.extractThumbnail(bm, reqWidth, reqHeight);
        return bmp;
    }
    
    /**
     * Creates a centered bitmap of the desired size.
     * 
     * @param source original bitmap source
     * @param width targeted width
     * @param height targeted height
     * @param options options used during thumbnail extraction
     */
    public static Bitmap Thumbnail(Bitmap bm, int reqWidth, int reqHeight, int options)
    {
        Bitmap bmp = null;
        bmp = ThumbnailUtils.extractThumbnail(bm, reqWidth, reqHeight, options);
        return bmp;
    }
    
    // 创建文件视频的缩略图
    /**
     * Create a video thumbnail for a video. May return null if the video is corrupt or the format is not supported.
     * 
     * @param filePath the path of video file
     * @param kind could be MINI_KIND or MICRO_KIND
     */
    public static Bitmap createVideoThumbnail(String filePath, int kind)
    {
        return ThumbnailUtils.createVideoThumbnail(filePath, kind);
    }
    
    /**
     * 读取图片的旋转的角度
     * 
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path)
    {
        int degree = 0;
        try
        {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation =
                exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }
    
    /**
     * 旋转图片
     * 
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    
    //缩放图片
    public static  Bitmap  scaleImage(Bitmap bm, int newWidth, int newHeight,int degree){
        if (bm == null){
          return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if(degree==0)
        matrix.postRotate(90);
        else 
        if(degree==180)
            matrix.postRotate(-90);
        if(degree==270)
            matrix.postRotate(-180);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
        if (bm != null & !bm.isRecycled()){
          bm.recycle();//销毁原图片
          bm = null;
        }
        return newbm;
      }
    
    //
    public static  Bitmap  scaleImage(Uri newuri,Context context,float hh, float ww)throws FileNotFoundException{
        ContentResolver cr = context.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        options.inJustDecodeBounds = false;
        // recreate the stream
        // make some calculation to define inSampleSize
        int w = options.outWidth;  
        int h = options.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (options.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (options.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        options.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        return compressImage(bitmap);
    }
    
    private static Bitmap compressImage(Bitmap image) {  
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }
    
    public static  byte[]  scaleImage1(Uri newuri)throws FileNotFoundException{
        ContentResolver cr = TripawayApplication.application.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        options.inJustDecodeBounds = false;
        // recreate the stream
        // make some calculation to define inSampleSize
        int w = options.outWidth;  
        int h = options.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (options.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (options.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        options.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        return compressImage1(bitmap);
    }
    
    private static byte[] compressImage1(Bitmap image) {  
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>600) {  //循环判断如果压缩后图片是否大于300kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        return baos.toByteArray();  
    }
    
    //取正方形
    public static  Drawable   scaleImage3(Uri newuri,Context context,float hh,float ww)throws FileNotFoundException{
        Bitmap bitmap = scaleImage( newuri, context,hh,ww);
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        bitmap= Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
        return new BitmapDrawable(bitmap);
    }
    
    //取正方形
    public static  Drawable   scaleSqureImage(Bitmap bitmap ,float hh,float ww){
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        bitmap= Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
        return new BitmapDrawable(bitmap);
    }
    
    public static  Bitmap  scaleImage4(Uri newuri,Activity context,float ww)throws FileNotFoundException{
        ContentResolver cr = context.getContentResolver();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight =metrics.heightPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        options.inJustDecodeBounds = false;
        // recreate the stream
        // make some calculation to define inSampleSize
        int w = options.outWidth;  
        int h = options.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (options.outWidth / ww);  
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
//            be = (int) (options.outHeight / hh);  
//        }  
        if (be <= 0)  
            be = 1;  
        options.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(newuri), null, options);
        return bitmap;
    }
}
