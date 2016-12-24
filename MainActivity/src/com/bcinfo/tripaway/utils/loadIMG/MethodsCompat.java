package com.bcinfo.tripaway.utils.loadIMG;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore;

/**
 * Android各版本的兼容方法
 * @author  jiangchangshan
 * @version V1.0  创建时间：2014-8-30 上午9:31:28
 */
public class MethodsCompat
{
    public static void overridePendingTransition(Activity activity, int enter_anim, int exit_anim)
    {
        activity.overridePendingTransition(enter_anim, exit_anim);
    }

    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options)
    {
        return MediaStore.Images.Thumbnails.getThumbnail(cr, origId, kind, options);
    }
}
