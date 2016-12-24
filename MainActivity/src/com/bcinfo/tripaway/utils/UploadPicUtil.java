package com.bcinfo.tripaway.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.MicroBlogPublishedActivity;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.widget.Toast;

/**
 * 上传图片操作工具包
 * 
 * @version V1.0  创建时间：2014-8-30 上午9:33:48
 */
public class UploadPicUtil
{
	 // 从服务端获取到的token
    private String uploadToken;
    
    // 存放七牛云上传返回的图片key值的数组
    private ArrayList<String> picKeyList = new ArrayList<String>();
    
    public ArrayList<String> getPicKeyList() {
		return picKeyList;
	}

	/**
     * list集合
     * 
     */
    private ArrayList<Bitmap> picList= new ArrayList<Bitmap>();
    
    
    private HashMap<Bitmap,byte[]> pList=new HashMap<Bitmap,byte[]>();
    
    private UploadFinishListener uploadFinishListener;
	/*
     * 测试 获取上传凭证 接口
     */
    public void testGetUploadTokenUrl()
    {
        
        RequestParams params = new RequestParams();
        params.put("bucket", "tripaway");// 存储空间
        HttpUtil.get(Urls.getUploadToken_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if ("00000".equals(response.optString("code")))
                {
                 uploadToken = response.optJSONObject("data").optString("token");
                 if(picList.size()==0){
                	 Toast.makeText(TripawayApplication.application, "请添加图片", 1000).show();
                	 return;
                 }
              
                 for (int i = 0; i < picList.size(); i++) {
                       
                	 Bitmap picBitmap = picList.get(i);
//                 	 ByteArrayOutputStream os = new ByteArrayOutputStream();
//                 	 picBitmap.compress(CompressFormat.PNG, 100,os);
//                 	picBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//             		 byte[] bytes = os.toByteArray();
             		 byte[] bytes = pList.get(picBitmap);
                	 testUploadToYunCode(bytes,i);
                 }
                    
//                    System.out.println("*********发布uploadToken*****:" + uploadToken);
                }
                //testUploadToYunCode(file);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("结果:=" + responseString);
            }
        });
        
        
        
        
    }
    
    /*
     * 测试 上传图片 到云
     */
    public void testUploadToYunCode(final byte [] data,final int i)
    {
		if (uploadToken != null) {
//			System.out.println("fileTarget=******"+fileTarget+"**uploadToken*"+ uploadToken);
			TripawayApplication.uploadManager.put(data, null, uploadToken, new UpCompletionHandler() {
				
				@Override
				public void complete(String arg0, ResponseInfo arg1, JSONObject response) {
					// TODO Auto-generated method stub
					System.out.println("ResponseInfo=" + response);
					String picKey = response.optString("key");
//					System.out.println("picKey=" + picKey);
					picKeyList.add(picKey);
					uriKeyMap.put(uriList.get(i), picKey);
					if (picKeyList.size()==picList.size()) {
//						picKeyList.clear();
						uploadFinishListener.onUploadFinishListener();
					}
			        
				}
			}, null);
//			TripawayApplication.uploadManager.put(fileTarget, null,
//					uploadToken, new UpCompletionHandler() {
//				
//				
//						@Override
//						public void complete(String key, ResponseInfo info,
//								JSONObject response) {
//							System.out.println("ResponseInfo=" + response);
//							String picKey = response.optString("key");
//							System.out.println("picKey=" + picKey);
//							picKeyList.add(picKey);
//							 testBlogPublishedUrl();
//						}
//					}, null);
		}

	}
    
    private static final int COMPRESS_PIC_FINISH=1;
    public void uploadPicData(final List<String>  uriList){
    	pList.clear();
    	picList.clear();
    	picKeyList.clear();
    	uriKeyMap.clear();
    	this.uriList=uriList;
    	if(uriList.size()==0){
    		uploadFinishListener.onUploadFinishListener();
    		return;
    	}
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(String uri:uriList){
		    		 try {
//		             	Bitmap bitmap = BitmapUtil.scaleImage(data.getData(), this);
//		 				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
		             	byte[] bytes = BitmapUtil.scaleImage1(Uri.fromFile(new File(uri)));
		           	  ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);//把压缩后的数据baos存放到ByteArrayInputStream中  
		                 Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		                 pList.put(bitmap, bytes);
		 				 picList.add(bitmap);
		             } catch (FileNotFoundException e) {
		 				// TODO Auto-generated catch block
		 				e.printStackTrace();
		 			}
		    		
		    	}
				 if(picList.size()>0)
						handler.sendEmptyMessage(COMPRESS_PIC_FINISH);
			}
		}).start();
    	
    	
    }
    
    public interface UploadFinishListener{
    	public void onUploadFinishListener();
    }
    
    public UploadPicUtil(UploadFinishListener uploadFinishListener) {
		// TODO Auto-generated constructor stub
    	this.uploadFinishListener=uploadFinishListener;
	}
    
    private List<String> uriList;
    
    private Map<String,String> uriKeyMap=new HashMap<String,String>();

	public Map<String, String> getUriKeyMap() {
		return uriKeyMap;
	}
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COMPRESS_PIC_FINISH:
				testGetUploadTokenUrl();
				break;

			default:
				break;
			}
			
		};
	};
}
