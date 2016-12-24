package com.bcinfo.tripaway.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @function
 * @author JiangCS
 * @version 1.0, 2015年3月16日 下午2:38:37
 */
public class AppInfo
{
    private final static String APP_CONFIG = "config";
    
    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
    
    private static String appUserAgent;
    
    private static String appCookie;
    
    private Context mContext;
    
    private static AppInfo appInfo;
    
    public static AppInfo getAppInfo(Context context)
    {
        if (appInfo == null)
        {
            appInfo = new AppInfo();
            appInfo.mContext = context;
        }
        return appInfo;
    }
    
    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId()
    {
        String uniqueID = getProperty(CONF_APP_UNIQUEID);
        if (TextUtils.isEmpty(uniqueID))
        {
            uniqueID = UUID.randomUUID().toString();
            setProperty(CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
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
            info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
    
    public String getUserAgent()
    {
        if (appUserAgent == null || appUserAgent == "")
        {
            StringBuilder ua = new StringBuilder();
            ua.append(getPackageInfo().versionName + '_' + getPackageInfo().versionCode);// App版本
            ua.append("/Android");// 手机系统平台
            ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
            ua.append("/" + android.os.Build.MODEL); // 手机型号
            ua.append("/" + getAppId());// 客户端唯一ID?
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }
    
    public String getCookie()
    {
        if (appCookie == null || appCookie == "")
        {
            appCookie = getProperty("cookie");
        }
        return appCookie;
    }
    
    public void cleanCookie()
    {
        appCookie = "";
    }
    
    /**
     * 检测网络是否可用
     * 
     * @return
     */
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    
    public Properties getProperties()
    {
        FileInputStream fis = null;
        Properties props = new Properties();
        try
        {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
            props.load(fis);
        }
        catch (Exception e)
        {
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
        return props;
    }
    
    private void setProps(Properties p)
    {
        FileOutputStream fos = null;
        try
        {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);
            p.store(fos, null);
            fos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }
    
    public void removeProperty(String... key)
    {
        Properties props = getProperties();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
    
    public boolean containsProperty(String key)
    {
        Properties props = getProperties();
        return props.containsKey(key);
    }
    
    public void setProperty(Properties ps)
    {
        Properties props = getProperties();
        props.putAll(ps);
        setProps(props);
    }
    
    public void setProperty(String key, String value)
    {
        Properties props = getProperties();
        props.setProperty(key, value);
        setProps(props);
    }
    
    public String getProperty(String key)
    {
        Properties props = getProperties();
        return (props != null) ? props.getProperty(key) : null;
    }
    
    public static float getDisplayScale(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }
    
    public static boolean getIsLogin()
    {
        if (StringUtils.isEmpty(PreferenceUtil.getUserId()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 判断程序是否在前台运行
     * 
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 判断程序是否已运行
     * 
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context)
    {
        // 获取ActivityManager
        ActivityManager mAm = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        // 获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList)
        {
            // 找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())
                && rti.baseActivity.getPackageName().equals(context.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }
}
