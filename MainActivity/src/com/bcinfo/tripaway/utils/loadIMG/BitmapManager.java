package com.bcinfo.tripaway.utils.loadIMG;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bcinfo.tripaway.utils.FileUtils;
import com.bcinfo.tripaway.utils.HmacSha1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * 异步线程加载图片工具类
 * 使用说明：
 * BitmapManager bmpManager;
 * bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading));
 * bmpManager.loadBitmap(imageURL, imageView);
 * @author  zhangbingkang
 * @version V1.0  创建时间：2014-1-6 下午3:16:46
 */
public class BitmapManager
{
    /**
     * 图片保存路径
     */
    private final static String ONECARD_PHOTO = Environment.getExternalStorageDirectory() + "/onecard_ecard/photos/";
    private static HashMap<String, SoftReference<Bitmap>> cache;
    private static ExecutorService pool;
    private static Map<ImageView, String> imageViews;
    private Bitmap defaultBmp;
    private boolean isRounded;
    static
    {
        cache = new HashMap<String, SoftReference<Bitmap>>();
        pool = Executors.newFixedThreadPool(5); //固定线程池
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }

    public BitmapManager()
    {
    }

    public BitmapManager(Bitmap def)
    {
        this.defaultBmp = def;
    }

    /**
     * 设置默认图片
     * @param bmp
     */
    public void setDefaultBmp(Bitmap bmp)
    {
        defaultBmp = bmp;
    }

    public void setRounded(boolean isRounded)
    {
        this.isRounded = isRounded;
    }

    /**
     * 加载图片
     * @param url
     * @param imageView 
     */
    public void loadBitmap(String url, ImageView imageView)
    {
        loadBitmap(url, imageView, this.defaultBmp, 0, 0);
    }

    /**
     * 加载图片-可设置加载失败后显示的默认图片
     * @param url
     * @param imageView
     * @param defaultBmp
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp)
    {
        loadBitmap(url, imageView, defaultBmp, 0, 0);
    }

    /**
     * 加载图片-可指定显示图片的高宽
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp, int width, int height)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap = null;
        if (isRounded)
        {
            bitmap = getBitmapFromCache(HmacSha1.hmacSha1(url));
        }
        else
        {
            bitmap = getBitmapFromCache(url);
        }
        if (bitmap != null)
        {
            //显示缓存图片
            if (isRounded)
            {
                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 14);
            }
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            //加载SD卡中的图片缓存
            String filename = FileUtils.getFileName(url);
            if (isRounded)
            {
                filename = HmacSha1.hmacSha1(url);
            }
            String filepath = ONECARD_PHOTO + filename;
            File file = new File(filepath);
            if (!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
            if (file.exists())
            {
                //显示SD卡中的图片缓存
                //                Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(), filename);
                //                imageView.setImageBitmap(bmp);
                Bitmap bmp = ImageUtils.getBitmapByPath(filepath);
                if (null != bmp)
                {
                    if (isRounded)
                    {
                        bmp = ImageUtils.getRoundedCornerBitmap(bmp, 14);
                    }
                    imageView.setImageBitmap(bmp);
                }
                else
                {
                    //删除破损图片
                    FileUtils.deleteFile(filepath);
                    //线程加载网络图片
                    imageView.setImageBitmap(defaultBmp);
                    queueJob(url, imageView, width, height);
                }
            }
            else
            {
                //线程加载网络图片
                imageView.setImageBitmap(defaultBmp);
                queueJob(url, imageView, width, height);
            }
        }
    }

    /**
     * 从缓存中获取图片
     * @param url
     */
    public Bitmap getBitmapFromCache(String url)
    {
        Bitmap bitmap = null;
        if (cache.containsKey(url))
        {
            bitmap = cache.get(url).get();
        }
        return bitmap;
    }

    /**
     * 从网络中加载图片
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void queueJob(final String url, final ImageView imageView, final int width, final int height)
    {
        /* Create handler in UI thread. */
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                String tag = imageViews.get(imageView);
                if (tag != null && tag.equals(url))
                {
                    if (msg.obj != null)
                    {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        if (isRounded)
                        {
                            bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 14);
                        }
                        imageView.setImageBitmap(bitmap);
                        try
                        {
                            String filename = FileUtils.getFileName(url);
                            if (isRounded)
                            {
                                filename = HmacSha1.hmacSha1(url);
                            }
                            String filepath = ONECARD_PHOTO + filename;
                            File file = new File(filepath);
                            try
                            {
                                file.createNewFile();
                            }
                            catch (IOException e)
                            {
                                Log.e("IOException", "createNewFile failed");
                            }
                            //向SD卡中写入图片缓存
                            ImageUtils.saveImageToSD(filepath, bitmap, 100);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        pool.execute(new Runnable()
        {
            public void run()
            {
                Message message = Message.obtain();
                message.obj = downloadBitmap(url, width, height);
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 下载图片-可指定显示图片的高宽
     * @param url
     * @param width
     * @param height
     */
    private Bitmap downloadBitmap(String url, int width, int height)
    {
        Bitmap bitmap = null;
        try
        {
            //http加载图片
            bitmap = loadImageFromUrl(url);
            if (width > 0 && height > 0)
            {
                //指定显示图片的高宽
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            //放入缓存
            if (isRounded)
            {
                url = HmacSha1.hmacSha1(url);
            }
            cache.put(url, new SoftReference<Bitmap>(bitmap));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap loadImageFromUrl(String bitmapUrl)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        InputStream in = null;
        try
        {
            in = new DataInputStream(new URL(bitmapUrl).openStream());
            bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
            in.close();
        }
        catch (IOException e)
        {
            Log.d("ImageFromUrlLoader", e.getMessage());
        }
        return bitmap;
    }
}