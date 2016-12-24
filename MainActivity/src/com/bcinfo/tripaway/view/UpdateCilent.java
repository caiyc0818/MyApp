package com.bcinfo.tripaway.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.dialog.CommDialog;
import com.bcinfo.tripaway.utils.OpenFIleUtils;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * 版本更新
 * 
 * @author hanweipeng
 * @version V1.0 创建时间：2013-12-31 上午9:54:35
 */
public class UpdateCilent
{
    /**
     * APP保存路径
     */
    private static final String ONECARD_UPDATE_APP = Environment.getExternalStorageDirectory() + "/onecard/update/";
    
    /**
     * 最新版本标识
     */
    private static final int DIALOG_TYPE_LATEST = 0;
    
    /**
     * 更新失败标识
     */
    private static final int DIALOG_TYPE_FAIL = 1;
    
    /**
     * 通知对话框
     */
    private Dialog noticeDialog;
    
    /**
     * 已经是最新或者无法获取最新版本信息的对话框
     */
    private Dialog latestOrFailDialog;
    
    /**
     * 返回的安装包url
     */
    private String apkUrl = "";
    
    /**
     * 对话框可取消标识 true:非强制更新可取消， false:强制更新不可取消
     */
    private boolean cancelFlag;
    
    /**
     * 提示语
     */
    private String updateMsg = "";
    
    /**
     * 第一次点击下载，再次点击不下载
     */
    private boolean isFirst = false;
    
    /**
     * 唯一实例对象
     */
    private static UpdateCilent updateClient;
    
    private Context context;
    
    private Handler handler;
    
    private String versionCode;
    
    private String versionName;
    
    
    
    /**
     * 获取UpdateClient唯一实例
     * 
     * @return [参数说明]
     */
    public static UpdateCilent getUpdateClient()
    {
        if (updateClient == null)
        {
            updateClient = new UpdateCilent();
        }
        return updateClient;
    }
    
    public void showDialog(Context context, Map<String, Object> map, boolean isShowMsg, Handler handler)
    {
        this.handler = handler;
        showDialog(context, map, isShowMsg);
    }
    
    /**
     * 判断版本信息
     * 
     * @param context
     * @param map
     * @param isShowMsg [参数说明]
     */
    public void showDialog(Context context, Map<String, Object> map, boolean isShowMsg)
    {
        if (context == null)
        {
            return;
        }
        this.context = context;
        String status = (String)map.get("code");
        apkUrl = (String)map.get("url");
        updateMsg = (String)map.get("info");
        versionCode=(String)map.get("versionCode");
        versionName=(String)map.get("versionName");
        // 已经是最新了
        if ("00080".equals(status) && isShowMsg)
        {
            showLatestOrFailDialog(DIALOG_TYPE_LATEST);
        }
        // 强制更新
        else if ("00082".equals(status))
        {
            cancelFlag = false;
            showNoticeDialog();
        }
        // 建议更新
        else if ("00081".equals(status))
        {
            cancelFlag = true;
            showNoticeDialog();
        }
        else if ("00083".equals(status) && isShowMsg)
        {
            showLatestOrFailDialog(DIALOG_TYPE_FAIL);
        }
        else if (!isShowMsg)
        {
            handler.sendEmptyMessage(0);
        }
    }
    
    /**
     * 显示已经是最新或者无法获取版本信息对话框
     */
    private void showLatestOrFailDialog(int dialogType)
    {
        if (latestOrFailDialog != null)
        {
            // 关闭并释放之前的对话框
            try
            {
                latestOrFailDialog.dismiss();
            }
            catch (Exception e)
            {
                Log.e("UpdateCilent", e.toString());
            }
            latestOrFailDialog = null;
        }
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("系统提示");
        if (dialogType == DIALOG_TYPE_LATEST)
        {
            builder.setMessage("您当前已经是最新版本");
        }
        else if (dialogType == DIALOG_TYPE_FAIL)
        {
            builder.setMessage("无法获取版本更新信息");
        }
        builder.setPositiveButton("确定", null);
        latestOrFailDialog = builder.create();
        latestOrFailDialog.show();
    }
    
    /**
     * 显示版本更新通知对话框
     */
    private void showNoticeDialog()
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_dialog, null);
        TextView version = (TextView)view.findViewById(R.id.version);
        TextView content = (TextView)view.findViewById(R.id.content);
        version.setText(versionName);
        content.setText(updateMsg);
        TextView ok = (TextView)view.findViewById(R.id.ok);
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isFirst)
                {
                    isFirst = true;
                    noticeDialog.dismiss();
                    showDownloadDialog();
                }
			}
        });
//        if (cancelFlag)
//        {
        	cancel.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                	if(versionName!=null&&versionCode!=null)
                	PreferenceUtil.setBoolean(versionCode+","+versionName,true);
                	noticeDialog.dismiss();
                	if(handler!=null)
                    handler.sendEmptyMessage(0);
                }
            });
//        }
        noticeDialog = CommDialog.createDialog(context, view);
        noticeDialog.show();
    }
    
    /**
     * 是否取消更新
     **/
    private boolean cancelUpdate = false;
    
    /**
     * 更新进度条
     */
    private ProgressBar mProgress;
    
    /**
     * 下载进度文字
     */
    private TextView progressTv;
    
    private Dialog mDownloadDialog;
    
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_download, null);
        mProgress = (ProgressBar)view.findViewById(R.id.update_progress);
        TextView nameTv = (TextView)view.findViewById(R.id.update_project_name);
        nameTv.setText(apkUrl.substring(apkUrl.lastIndexOf("/") + 1));
        progressTv = (TextView)view.findViewById(R.id.update_progress_txt);
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        // 取消更新
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	mDownloadDialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
                if (!cancelFlag)
                {
                    showNoticeDialog();
                }
            }
        });
        mDownloadDialog =CommDialog.createDialog(context, view);
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        String filePath = ONECARD_UPDATE_APP + "onecard.apk";
        new DownLoadTask().execute(apkUrl, filePath);
    }
    
    class DownLoadTask extends AsyncTask<String, Integer, String>
    {
        private static final String TAG = "DownLoadTask";
        
        /**
         * 所要下载的文件的大小
         */
        private int fileSize;
        
        /**
         * 下载存放路径
         */
        private String filePath;
        
        /**
         * 下载完成度
         */
        private int compeleteSize;
        
        /**
         * 下载完成标志
         */
        private boolean isCompleted;
        
        protected String doInBackground(String... params)
        {
            Log.d(TAG, "==========doInBackground()===========");
            filePath = params[1];
            Log.d(TAG, "urlstr = " + params[0]);
            Log.d(TAG, "filePath = " + filePath);
            File file = new File(filePath);
            HttpURLConnection httpConn = null;
            RandomAccessFile randomFile = null;
            FileOutputStream fos = null;
            int dataBlockLength = 1024 * 1000;
            byte[] data = new byte[dataBlockLength];
            int readLength = -1;
            InputStream is = null;
            try
            {
                URL url = new URL(params[0]);
                httpConn = (HttpURLConnection)url.openConnection();
                httpConn.setConnectTimeout(1000*60*5);
                httpConn.setReadTimeout(1000*60*5);
                // 将RespondCode值返回
                int respondCode = httpConn.getResponseCode();
                if (respondCode == HttpURLConnection.HTTP_OK)
                {
                    if (!file.getParentFile().exists())
                    {
                        file.getParentFile().mkdirs();
                    }
                    if (!file.exists())
                    {
                        file.createNewFile();
                    }
                    // 采用普通的下载方式
                    fos = new FileOutputStream(file);
                    fileSize = httpConn.getContentLength();
                    is = httpConn.getInputStream();
                    // 点击取消或下载完成停止下载
                    while ((readLength = is.read(data)) != -1 && !cancelUpdate)
                    {
                        fos.write(data, 0, readLength);
                        compeleteSize += readLength;
                        // 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                        publishProgress((int)((compeleteSize / (float)fileSize) * 100));
                        if (compeleteSize == fileSize)
                        {
                            isCompleted = true;
                        }
                        // 为了演示进度,休眠500毫秒
//                        Thread.sleep(100);
                    }
                }
            }
            catch (Exception e)
            {
                isCompleted = false;
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (is != null)
                    {
                        is.close();
                    }
                    if (httpConn != null)
                    {
                        httpConn.disconnect();
                    }
                    if (fos != null)
                    {
                        fos.close();
                    }
                    if (randomFile != null)
                    {
                        randomFile.close();
                    }
                }
                catch (Exception e)
                {
                    isCompleted = false;
                    e.printStackTrace();
                }
            }
            return null;
        }
        
        /**
         * onProgressUpdate方法用于更新进度信息
         */
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {
            Log.d(TAG, "==========onProgressUpdate()===========");
            mProgress.setProgress(progresses[0]);
            progressTv.setText("已下载：" + progresses[0] + "%");
        }
        
        /**
         * onPostExecute方法用于在执行完后台任务后更新UI,显示结果
         */
        @Override
        protected void onPostExecute(String result)
        {
            Log.d(TAG, "==========onPostExecute()===========");
            isFirst = false;
            cancelUpdate = false;
            mDownloadDialog.dismiss();
            if (isCompleted)
            {
                context.startActivity(OpenFIleUtils.openFile(filePath));
                if (!cancelFlag)
                {
//                    exit();
                }
            }
            else
            {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 退出
     */
    private void exit()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
        System.exit(0);
    }
}
