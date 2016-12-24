package com.bcinfo.tripaway.utils.loadIMG;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/**
 * 网络图片加载类 可用于从网络加载图片，压缩图片
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月17日 14：:3:11
 */
@SuppressLint("NewApi")
public class BlogImageLoader
{

    // 图片缓存技术类
    /**
     * 背景图片缓存技术的核心类，用于缓存微游记背景照片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, Bitmap> mMemoryCache;
    /**
     * 头像图片缓存技术的核心类 用于缓存所有作者的头像照片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    // private static LruCache<String, Bitmap> mMemoryCache2;
    /**
     * ImageLoader的实例。 单例实例对象mImageLoader
     */
    private static BlogImageLoader mImageLoader;

    @SuppressLint("NewApi")
    public BlogImageLoader()
    {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 可用内存的1/8作为LruCache图片缓存的容量大小
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
        {

            protected int sizeOf(String key, Bitmap bitmap)
            {
                return bitmap.getByteCount();
            }
        };
        // mMemoryCache2=new LruCache<String, Bitmap>(cacheSize){
        // protected int sizeOf(String key, Bitmap bitmap) {
        // return bitmap.getByteCount();
        //
        // }
        // };
    }

    /**
     * 获取BlogImageLoader的实例。
     * 
     * @return ImageLoader的实例。
     */
    public static BlogImageLoader getInstance()
    {
        if (mImageLoader == null)
        {
            // 调用上面的构造方法
            mImageLoader = new BlogImageLoader();
        }
        return mImageLoader;
    }

    /**
     * 将背景照片图片添加到LruCache缓存中
     * 
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
     
    public void addBitmapToMemoryCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromMemoryCache(key) == null)
        {
            // LruCache类似于Map集合
            mMemoryCache.put(key, bitmap);

        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     * 
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
     
    public Bitmap getBitmapFromMemoryCache(String key)
    {

        return mMemoryCache.get(key);
    }

    /**
     * 计算网络源图片放到界面上宽度和高度的压缩比例
     * 
     * @param options
     *            BitmapFactory的附加选项参数
     * @param reqWidth
     *            显示在界面上的宽度(目标图片的宽度)
     * @return 压缩比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth)
    {
        // 源图片的宽度
        final int width = options.outWidth;
        // 假设缩放比例为1
        int inSampleSize = 1;
        if (width > reqWidth)
        {
            // 计算出实际宽度和目标宽度的比率 widthRatio 比例
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 从指定目录上加载图片 (该目录是本地sd卡目录，不是网络资源路径)
     * 
     * @param pathName
     *            路径
     * @param reqWidth
     * @return 位图
     */
    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth)
    {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        // 返回从指定path路径解析得到的Bitmap位图
        return BitmapFactory.decodeFile(pathName, options);
    }
}
