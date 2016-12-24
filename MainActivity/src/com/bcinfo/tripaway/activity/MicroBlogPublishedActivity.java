package com.bcinfo.tripaway.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.TripPhotoAdapter;
import com.bcinfo.tripaway.db.UploadPicturesRecordDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

/**
 * 微游记-发布
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月22日 14:25:11
 */
public class MicroBlogPublishedActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
    private RelativeLayout accessSecretLayout;
    private RelativeLayout trip_blog_published_location;
    
    // 全屏半透明的背景色
    private FrameLayout fullHalfTransParentLayout;
    
    private TextView secretContent;
    
    /**
     * 网格列表
     * 
     */
    private GridView photoView;
 
    private LinearLayout the_microblog_title;
    /**
     * 从手机拍照
     */
    private RelativeLayout photoTakenbyPhoneLayout;
    
    private SparseArray<String> sparseList = new SparseArray<String>(5);
    
    /**
     * 从手机相册选择
     */
    private RelativeLayout photoTakenbyAlbumLayout;
    
    /**
     * 微游记图片适配器
     * 
     */
    private TripPhotoAdapter photoAdapter;
    
    /**
     * list集合
     * 
     */
    private ArrayList<Bitmap> picList;
    
    private HashMap<Bitmap,byte[]> pList=new HashMap<Bitmap,byte[]>();
    
    /**
     * 日期格式化
     */
    private SimpleDateFormat dateFormat;
    
    /**
     * 微游记二级标题 返回图标
     */
    private TextView blogNavBack;
    
    /**
     * 微游记二级标题 发布
     */
    private TextView blogPublish;
    
    /**
     * SlidingDrawer 实现向上的抽屉
     * 
     */
    @SuppressWarnings("deprecation")
    private SlidingDrawer attachDrawer;
    
    /**
     * 照片添加描述
     * 
     */
    private EditText photoEdit;
    
    /**
     * 所在的位置
     */
    private TextView locationTv;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    
    /**
     * 拍照照片的根路径
     */
    private File imageFile;
    
    /**
     * 拍照单个照片的路径
     */
    private File file;
    
    // 从服务端获取到的token
    private String uploadToken;
    
    private final String IMAGE_UNSPECIFIED = "image/*";
    
    private UploadPicturesRecordDB uploadPicturesRecord;
    
    // 存放七牛云上传返回的图片key值的数组
    private ArrayList<String> picKeyList = new ArrayList<String>();
    
    /**
     * 
     * 关闭拍照|从相册选择控件
     */
    private ImageView closeSlidingDrawer;
    
    public EditText getPhotoEdit()
    {
        return photoEdit;
    }
    
    public void setPhotoEdit(EditText photoEdit)
    {
        this.photoEdit = photoEdit;
    }
    
    public SlidingDrawer getAttachDrawer()
    {
        return attachDrawer;
    }
    
    public void setAttachDrawer(SlidingDrawer attachDrawer)
    {
        this.attachDrawer = attachDrawer;
    }
    
    public List<Bitmap> getPicList()
    {
        return picList;
    }
    
    public void setPicList(ArrayList<Bitmap> picList)
    {
        this.picList = picList;
    }
    
    public GridView getPhotoView()
    {
        return photoView;
    }
    
    public void setPhotoView(GridView photoView)
    {
        this.photoView = photoView;
    }
    
    public TextView getSecretContent()
    {
        return secretContent;
    }
    
    public void setSecretContent(TextView secretContent)
    {
        this.secretContent = secretContent;
    }
    
    public RelativeLayout getAccessSecretLayout()
    {
        return accessSecretLayout;
    }
    
    public void setAccessSecretLayout(RelativeLayout accessSecretLayout)
    {
        this.accessSecretLayout = accessSecretLayout;
    }
    
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0x1266:
                    photoAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        };
    };

	private ImageView rightArrow;
	
	private String coordinate;
	
	private String currentAddress;
	
	private boolean check=false;
    
	private String filePath;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
        	filePath=savedInstanceState.getString("filePath");
        }
        setContentView(R.layout.trip_microblog_published);
        initBlogTitle();
        dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        imageFile = new File(Environment.getExternalStorageDirectory() + "/myImage");
        if(!imageFile.exists()){
        	imageFile.mkdir();
        }
        picList = this.getIntent().getParcelableArrayListExtra("listArgs");
        if (picList == null)
        {
            picList = new ArrayList<Bitmap>();
        }
        initLocation();
        the_microblog_title = (LinearLayout)findViewById(R.id.the_microblog_title);
        the_microblog_title.getBackground().setAlpha(255);
        fullHalfTransParentLayout = (FrameLayout)findViewById(R.id.trip_blog_publish_fullTransParentLayout);
        closeSlidingDrawer = (ImageView)this.findViewById(R.id.closeSlidingLayout);
        closeSlidingDrawer.setScaleType(ScaleType.FIT_CENTER);
        // 设置旋转45度后的bitmap
        closeSlidingDrawer.setImageBitmap(MatrixBitmap());
        // 设置点击事件监听器 当点击 "叉号"图标时 关闭这个照片选择控件
        closeSlidingDrawer.setOnClickListener(this);
        fullHalfTransParentLayout.setOnClickListener(this);
        photoTakenbyPhoneLayout = (RelativeLayout)findViewById(R.id.layout_photo_by_phone_taken_container);
        photoTakenbyAlbumLayout = (RelativeLayout)findViewById(R.id.layout_photo_by_album_taken_container);
        photoTakenbyPhoneLayout.setOnClickListener(this);
        photoTakenbyAlbumLayout.setOnClickListener(this);
        photoView = (GridView)this.findViewById(R.id.trip_blogPhoto_gridView);
        photoEdit = (EditText)this.findViewById(R.id.trip_edit_photoDescript);
//        photoEdit.setText("给这些图片添加描述吧");
        locationTv = (TextView)findViewById(R.id.trip_blog_location_content);
        // 请求获得焦点
        // photoEdit.requestFocus();
        accessSecretLayout = (RelativeLayout)this.findViewById(R.id.trip_blog_published_secret);
        accessSecretLayout.setOnClickListener(this);
        secretContent = (TextView)this.findViewById(R.id.trip_blog_secret_content);
        attachDrawer = (SlidingDrawer)this.findViewById(R.id.attachSlidingDrawer);
        uploadPicturesRecord = new UploadPicturesRecordDB();// 上传数据记录的数据库
        photoAdapter = new TripPhotoAdapter(picList, this);
        photoView.setAdapter(photoAdapter);
        // 设置GridView的点击监听事件
        photoView.setOnItemClickListener(this);
//        testGetUploadTokenUrl();
        trip_blog_published_location = (RelativeLayout) findViewById(R.id.trip_blog_published_location);
        trip_blog_published_location.setOnClickListener(this);
        rightArrow = (ImageView)this.findViewById(R.id.right_arrow);
//        rightArrow.setOnClickListener(this);
          toast =Toast.makeText(MicroBlogPublishedActivity.this, "正在发布中，请稍后", 700);
          toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
    }
    
    private void testBlogPublishedUrl()
    {
    	long t2 = System.currentTimeMillis(); // 排序后取得当前时间  
    	  
        Calendar c = Calendar.getInstance();  
        c.setTimeInMillis(t2 - t1);  
  
        System.out.println("耗时:最后上传 " + c.get(Calendar.MINUTE) + "分 "  
                + c.get(Calendar.SECOND) + "秒 " + c.get(Calendar.MILLISECOND)  
                + " 毫秒");
    	try {
    		String access;
    		if(secretContent.getText().toString().equals("公开")){
    			access="all";
    		}else if(secretContent.getText().toString().equals("好友可见")){
    			access="friends";
    		}else {
    			access="private";
    		}
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("desc", photoEdit.getText().toString());
			jsonObject.put("location", locationTv.getText().toString());
			jsonObject.put("access", access);
			 isRunning=false;
			if (picKeyList.size() != 0 )
	        {
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < picKeyList.size(); i++)
				{
					jsonArray.put(picKeyList.get(i));
				}
				jsonObject.put("pictures", jsonArray);
				StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
//				Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！", 2000).show();
				 HttpUtil.post(Urls.tripStory_self_url, stringEntity, new JsonHttpResponseHandler()
			        {
			            @Override
			            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			            {
			                
			                super.onSuccess(statusCode, headers, response);
			                if("00000".equals(response.optString("code"))){
			                	if(statusCode==00000){
			                		picKeyList.clear();
			                	}
			                	mHandler.postDelayed(kuGRun,200);//这个是延迟执行
			                }else{
			                	Toast.makeText(MicroBlogPublishedActivity.this, "发布失败", 100).show();
				                picKeyList.clear();
			                }
			            }
			            
			            @Override
			            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
			            {
			            	
			                super.onFailure(statusCode, headers, responseString, throwable);
			                Toast.makeText(MicroBlogPublishedActivity.this, "发布失败", 100).show();
			                picKeyList.clear();
			                System.out.println("发布fail:" + responseString);
			            }
			            
			        });
				
                 
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
//        RequestParams params = new RequestParams();
//        params.put("desc", photoEdit.getEditableText().toString()); // 添加描述
//        params.put("location", locationTv.getText().toString());// 所在位置
//        params.put("access", secretContent.getText().toString());// 谁可以看
        //params.put("pictures", picKeyList);
//        if (picKeyList.size() != 0 )
//        {
//            params.put("pictures", picKeyList);
//        }
    }
    private Runnable kuGRun = new Runnable(){

		  public void run() {
			  Toast.makeText(MicroBlogPublishedActivity.this, "发布成功", 1000).show();
			  Intent it = new Intent("com.bcinfo.publishBlog");
			  sendBroadcast(it);
//			  Intent intent=new Intent(MicroBlogPublishedActivity.this,MyMicroBlogActivity.class);
//              startActivity(intent);
              finish();
		  }
		  
		 };
	private long t1;
    /*
     * 测试 获取上传凭证 接口
     */
    private void testGetUploadTokenUrl()
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
                	 Toast.makeText(MicroBlogPublishedActivity.this, "请添加图片", 1000).show();
                	 return;
                 }
              
                 for (int i = 0; i < picList.size(); i++) {
                	   long t2 = System.currentTimeMillis(); // 排序后取得当前时间  
                       
                       Calendar c = Calendar.getInstance();  
                       c.setTimeInMillis(t2 - t1);  
                 
                       System.out.println("耗时: compress前" + c.get(Calendar.MINUTE) + "分 "  
                               + c.get(Calendar.SECOND) + "秒 " + c.get(Calendar.MILLISECOND)  
                               + " 毫秒");
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
                isRunning=false;
            }
        });
        
    }
    private void initLocation()
    {
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location)
        {
        	if(location==null){
        		ToastUtil.showToast(MicroBlogPublishedActivity.this, "定位失败");
        		return;
        	}
            // 定位结束，关闭定位
            String province = "";
            if (location.getProvince().contains("省"))
            {
                province = location.getProvince().substring(0, location.getProvince().length() - 1);
            }
            else
            {
                province = location.getProvince();
            }
            String city = "";
            if (location.getCity().contains("市"))
            {
                city = location.getCity().substring(0, location.getCity().length() - 1);
            }
            else
            {
                city = location.getCity();
            }
            currentAddress=city;
            locationTv.setText(city);
            mLocationClient.stop();
            NumberFormat f = NumberFormat.getNumberInstance();  
            coordinate=  location.getLatitude()+","+  location.getLongitude();
        }
    
    }
    
    /*
     * 测试 上传图片 到云
     */
    private void testUploadToYunCode(final byte [] data,final int i)
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
					sparseList.put(i, picKey);
					if (picKeyList.size()==picList.size()) {
						picKeyList.clear();
						for(int j=0;j<sparseList.size();j++){
							picKeyList.add(sparseList.get(j));
						}
						testBlogPublishedUrl();
					}
			        
			        Calendar c = Calendar.getInstance();  
					  long t3= System.currentTimeMillis(); // 排序后取得当前时间  
	                 	 c.setTimeInMillis(t3 - t1);  
	                 	 System.out.println("datalength" +data.length);	 
	                 	 System.out.println("耗时: compress后" + c.get(Calendar.MINUTE) + "分 "  
	                             + c.get(Calendar.SECOND) + "秒 " + c.get(Calendar.MILLISECOND)  
	                             + " 毫秒");	 
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
    
    /**
     * 设置微游记二级界面标题
     * 
     */
    private void initBlogTitle()
    {
        blogNavBack = (TextView)this.findViewById(R.id.blog_navBack);
        blogPublish = (TextView)this.findViewById(R.id.blog_publish);
        blogNavBack.setOnClickListener(mOnClickListener);
        blogPublish.setOnClickListener(this);
    }
    
    /**
     * Matrix操作获得旋转45度后的图片
     */
    private Bitmap MatrixBitmap()
    {
        // 解析获得resource资源中的addtripphotodrawable资源 并生成bitmap位图
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_photo_unselect);
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        // 以50,50为旋转中心旋转指定的角度 45度
        matrix.postRotate(45f);
        // 生成旋转45度后的bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    
    @Override
    public void onClick(View v)
    {
        String secretStr = secretContent.getText().toString();
        switch (v.getId())
        {
            case R.id.trip_blog_published_secret:
                if (secretStr.equals("公开"))
                {
                    secretContent.setText("自己可见");
                }
                else if (secretStr.equals("自己可见")) {
//                	secretContent.setText("好友可见");
                	secretContent.setText("公开");
				}else{
                }
                break;
            case R.id.closeSlidingLayout:
                attachDrawer.animateClose();
                fullHalfTransParentLayout.setVisibility(View.GONE);
                photoEdit.setClickable(true);
                accessSecretLayout.setClickable(true);
                photoEdit.setFocusable(true);
                photoEdit.setFocusableInTouchMode(true);
                break;
            case R.id.trip_blog_publish_fullTransParentLayout:
                fullHalfTransParentLayout.setVisibility(View.GONE);
                photoEdit.setClickable(true);
                accessSecretLayout.setClickable(true);
                photoEdit.setFocusable(true);
                photoEdit.setFocusableInTouchMode(true);
                if (attachDrawer != null && attachDrawer.isOpened())
                {
                    attachDrawer.animateClose();
                }
                break;
            case R.id.blog_publish:
            	if(!AppInfo.getIsLogin()){
            		Intent it = new Intent(MicroBlogPublishedActivity.this,LoginActivity.class);
            		startActivity(it);
            		return;
            	}
            	if(StringUtils.isEmpty( photoEdit.getText().toString())){
        			ToastUtil.showToast(MicroBlogPublishedActivity.this, "给这些图片添加描述吧");
        			return;
        		}
            	  if(picList.size()==0){
                 	 Toast.makeText(MicroBlogPublishedActivity.this, "请添加图片", 1000).show();
                 	 return;
                  }
            	   t1 = System.currentTimeMillis(); // 排序前取得当前时间  
            	isRunning=true;
            	toast.show();
            	   timer.schedule(new TimerTask(){ 
            		     
            	        @Override 
            	        public void run() { 
            	        // TODO Auto-generated method stub 
            	            while(isRunning){ 
            	                toast.show(); 
            	            } 
            	        } 
            	                             
            	    }, 600); 
            	testGetUploadTokenUrl();
//            	Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！", 2000).show();
                
                break;
            case R.id.layout_photo_by_phone_taken_container: // 点击拍照
                String currMapName = dateFormat.format(new Date()) + ".jpg";
                file = new File(imageFile.getAbsolutePath() + "/" + currMapName);
                filePath=imageFile.getAbsolutePath() + "/" + currMapName;
                Intent pointIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pointIntent3.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(pointIntent3, 1);
                fullHalfTransParentLayout.setVisibility(View.GONE);
                attachDrawer.close();
                break;
            case R.id.layout_photo_by_album_taken_container: // 从手机相册选择
                fullHalfTransParentLayout.setVisibility(View.GONE);
                attachDrawer.close();
                Intent pointIntent2 = new Intent(Intent.ACTION_PICK, null);
                pointIntent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pointIntent2, 2);
                break;
            case R.id.trip_blog_published_location:
            	if (coordinate != null&&!check) {
            		check=true;
    				PoiSearch mPoiSearch = PoiSearch.newInstance();

    				OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
    					public void onGetPoiResult(PoiResult result) {
    						if (result.error == ERRORNO.RESULT_NOT_FOUND){  
    					        Toast.makeText(MicroBlogPublishedActivity.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();  
    					        return ;  
    					    }  
    					    else if (result.error == null) {  
    					        Toast.makeText(MicroBlogPublishedActivity.this, "搜索出错啦..", Toast.LENGTH_LONG).show();  
    					        return;  
    					    } 
    						PoiResult result2 = result;
    						ArrayList<PoiInfo> resultList = (ArrayList<PoiInfo>) result.getAllPoi();
    						// 获取POI检索结果
//    						Toast.makeText(MicroBlogPublishedActivity.this, result.toString(), Toast.LENGTH_LONG).show();

    						Intent it = new Intent(MicroBlogPublishedActivity.this, AreaSelectActivity.class);
    						it.putExtra("currentAddress", currentAddress);
    						it.putExtra("coordinate", coordinate);
    						it.putExtra("poilist",resultList);
    						startActivityForResult(it,4);
    						check=false;
    					}
    					@Override
    					public void onGetPoiDetailResult(PoiDetailResult arg0) {
    						// TODO Auto-generated method stub

    					}
    				};

    				mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

//    				mPoiSearch.searchInCity((new PoiCitySearchOption()).city(currentAddress).keyword("美食").pageNum(0));
    				String[] strs= coordinate.split(",");
    				Double double1 = Double.parseDouble(strs[0].trim());
    				Double double2 = Double.parseDouble(strs[1].trim());
//    				mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(32.006765,118.736427)).keyword("美食").pageCapacity(20).pageNum(0).sortType(PoiSortType.distance_from_near_to_far).radius(10*1000));
    				mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(double1,double2)).keyword("美食号码银行").pageCapacity(10).pageNum(0).sortType(PoiSortType.distance_from_near_to_far).radius(10*1000));
    				
    				
    				
//    				PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption(); 
//    			
//    		        nearbySearchOption.location(new LatLng(double1, double2));
//    		        nearbySearchOption.keyword("美食");  
//    		        nearbySearchOption.radius(1000);// 检索半径，单位是米  
//    		        nearbySearchOption.pageNum(0);  
//    		        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求  
    				
    				// mPoiSearch.destroy();
            	}
            	break;
            default:
                break;
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == picList.size())
        {
        	if(picList.size() == 5){
        		ToastUtil.showToast(MicroBlogPublishedActivity.this, "最多选择5张图片");
        		return;
        	}
        	
            if (!attachDrawer.isOpened())
            {
                fullHalfTransParentLayout.setVisibility(View.VISIBLE);
                photoEdit.setFocusable(true);
                accessSecretLayout.setClickable(false);
                photoView.setClickable(false);
                /**
                 * 判断软键盘是否已经打开 如果打开了，要关闭软键盘
                 * 
                 */
                InputMethodManager inputManager =
                    (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 或者是 inputManager.isActive()也可以
                if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
                {
                    // 隐藏软键盘
                    inputManager.hideSoftInputFromWindow(photoEdit.getWindowToken(), 0);
                }
                attachDrawer.animateOpen();
            }
        }
        else
        {
            /*
             * Intent intentForPhoto = new Intent(this, TripBlogPhotoFlipperActivity.class); Bundle bundleGet = new
             * Bundle(); bundleGet.putInt("position", position); bundleGet.putIntegerArrayList("listValue", picList);
             */
            // intentForPhoto.putExtra("bundleValue", bundleGet);
            /*
             * intentForPhoto.putExtras(bundleGet); startActivity(intentForPhoto);
             * 
             * this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
             */
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Uri newuri = Uri.fromFile(new File(filePath));
            uploadPicturesRecord.saveloadPicturesRecord(newuri.toString(), newuri.toString(), true);
            ContentResolver cr = this.getContentResolver();  
            try {
//				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(newuri));
//				Bitmap bitmap = BitmapUtil.scaleImage(newuri, this);
            	byte[] bytes = BitmapUtil.scaleImage1(newuri);
            	  ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);//把压缩后的数据baos存放到ByteArrayInputStream中  
                  Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
                  pList.put(bitmap, bytes);
				 picList.add(bitmap);
		         mHandler.sendEmptyMessage(0x1266);
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            if (data == null)
            {
                Toast.makeText(getBaseContext(), "无法加载此图片!", Toast.LENGTH_SHORT).show();
                return;
            }
            file = null;
//            startPhotoZoom(data.getData());
            ContentResolver cr = this.getContentResolver();  
            try {
//            	Bitmap bitmap = BitmapUtil.scaleImage(data.getData(), this);
//				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
            	byte[] bytes = BitmapUtil.scaleImage1(data.getData());
          	  ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);//把压缩后的数据baos存放到ByteArrayInputStream中  
                Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
                pList.put(bitmap, bytes);
				 picList.add(bitmap);
		         mHandler.sendEmptyMessage(0x1266);
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }
        else if (requestCode == 3 && resultCode == Activity.RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            // 获得拍照的照片
            Bitmap selectedBitMap = (Bitmap)bundle.get("data");
            picList.add(selectedBitMap);
            mHandler.sendEmptyMessage(0x1266);
        }
        else if(requestCode==4&&resultCode==0&&data!=null){
        	String address = data.getStringExtra("address");
        	locationTv.setText(address);
        }
        accessSecretLayout.setClickable(true);
    }
    
//    /**
//     * 收缩图片
//     * 
//     * @param uri
//     */
//    public void startPhotoZoom(Uri uri)
//    {
//        if (file == null)
//        {
//            // if(("-1").equals(uploadPicturesRecord.isRecordExist(uri.toString()))){
//            file = new File(imageFile.getAbsolutePath() + "/" + dateFormat.format(new Date()) + ".jpg");
//            System.out.println("**startPhotoZoom***file***"+file);
//            uploadPicturesRecord.saveloadPicturesRecord(uri.toString(), Uri.fromFile(file).toString(), true);
//            // }else{
//            // uploadPicturesRecord.saveloadPicturesRecord(uri.toString(),"",false);
//            // isRecordExist(uri);
//            // return;
//            // }
//        }
//        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");// 进行修剪
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, 3);
//    }
//    
//    
//    /**
//     * 图片已加载过
//     * 
//     * @param uri
//     */
//    private void isRecordExist(Uri uri)
//    {
//        Bitmap selectedBitMap = null;
//        String imageuri = uploadPicturesRecord.readimageuriRecord(uri.toString());
//        if (imageuri == null)
//        {
//            return;
//        }
//        Uri myuri = Uri.parse(imageuri);
//        try
//        {
//            selectedBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), myuri);// 显得到bitmap图片
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if (selectedBitMap != null)
//        {
//            picList.add(selectedBitMap);
//            mHandler.sendEmptyMessage(0x1266);
//            file = new File(imageuri.substring(7));
////            testUploadToYunCode(file);
//        }
//    }
    
    /**
     * 重写键盘按下的事件代码
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 判断按下的是否是back键
            if (attachDrawer != null && attachDrawer.isOpened())
            {
                attachDrawer.animateClose();
                fullHalfTransParentLayout.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    Timer timer = new Timer(); 
    Toast  toast;
    boolean isRunning=true;
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	if(filePath!=null)
    	outState.putString("filePath", filePath);
    }
}
