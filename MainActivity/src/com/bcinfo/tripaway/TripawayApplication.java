package com.bcinfo.tripaway;

import java.io.File;
import java.util.UUID;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class TripawayApplication extends Application
{
    /**
     * 网络请求队列
     */
    public static TripawayApplication application;
    
    /**
     * 七牛上传对象
     */
    public static UploadManager uploadManager;
    
    private NotificationManager mNotificationManager;
    
    public static  boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;

    public static Typeface boldTf;

    public static final String strKey = "35mRPsbtX40O13gliCr3RBBx";//请输入你的key
    
    
    public static Typeface normalTf;
    
    public static Typeface moreBoldTf;
    
    public static boolean isDebug=true;//打包的时候记得设置false
    
    
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        StatService.setDebugOn(true);
        application = this;
    	AssetManager mgr = this.getAssets();// 得到AssetManager
        normalTf = Typeface.createFromAsset(mgr, "fonts/Lantinghei_0.ttf");// 根据路径得到Typeface
        boldTf = Typeface.createFromAsset(mgr, "fonts/Lantinghei_1.ttf");// 根据路径得到Typeface
        moreBoldTf = Typeface.createFromAsset(mgr, "fonts/Lantinghei_2.ttf");// 根据路径得到Typeface
        // 七牛图片上传 sdk 配置
        Configuration cfg =
            new Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(10)
                .responseTimeout(60)
                .recorder(null)
                .recorder(null, null)
                .build();
        getInstance(cfg);
        File cacheDir = StorageUtils.getOwnCacheDirectory(application, "com.bcinfo.tripaway");
        // 全局的显示选项
        DisplayImageOptions options =
            new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_photo)
                .showImageForEmptyUri(R.drawable.default_photo)
                .showImageOnFail(R.drawable.default_photo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
        
        ImageLoaderConfiguration config =
            new ImageLoaderConfiguration.Builder(application).memoryCacheExtraOptions(480, 800)
            // default = device screen dimensions即保存的每个缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3)
                // 线程池数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 线程优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // 设置图片下载和显示的工作队列排序
                .denyCacheImageMultipleSizesInMemory()
                // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                // 设置内存缓存方式 大小
                .diskCache(new UnlimitedDiscCache(cacheDir))
                // 磁盘缓存路径
                .diskCacheSize(50 * 1024 * 1024)
                // 磁盘缓存大小
                .diskCacheFileCount(100)
                // 缓存数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 设置缓存文件的名字 将保存的时候的URI名称用MD5 加密
                .imageDownloader(new BaseImageDownloader(application, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                // .writeDebugLogs()
                // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
//        initEngineManager(this) ;
        SDKInitializer.initialize(this);
//        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/stliti.ttf");
//        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/stliti.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/stliti.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "fonts/stliti.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/stliti.ttf");

        
    }
    
//	public void initEngineManager(Context context) {
//        if (mBMapManager == null) {
//            mBMapManager = new BMapManager(context);
//        }
//
//	}
	
    /**
     * 获得 uploadManager的单例类对象
     */
    public static void getInstance(Configuration cfg)
    {
        if (uploadManager == null)
        {
            uploadManager = new UploadManager(cfg);
        }
        
    }
    
    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo()
    {
        PackageInfo info = null;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
    
    /**
     * 检测网络是否可用
     * 
     * @return
     */
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    
    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId()
    {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (TextUtils.isEmpty(uniqueID))
        {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }
    
    public void setProperty(String key, String value)
    {
        AppConfig.getAppConfig(this).set(key, value);
    }
    
    public String getProperty(String key)
    {
        return AppConfig.getAppConfig(this).get(key);
    }
    
    public NotificationManager getNotificationManager()
    {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager)getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }
    
// // 常用事件监听，用来处理通常的网络错误，授权验证错误等
//    static class MyGeneralListener implements MKGeneralListener {
//        
//        @Override
//        public void onGetNetworkState(int iError) {
//            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
////                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
////                    Toast.LENGTH_LONG).show();
//            }
//            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
////                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
////                        Toast.LENGTH_LONG).show();
//            }
//            // ...
//        }
//
//        @Override
//        public void onGetPermissionState(int iError) {
//            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
//                //授权Key错误：
////                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), 
////                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
//               m_bKeyRight = false;
//            }
//        }
//    }
    
    

	
	
}
