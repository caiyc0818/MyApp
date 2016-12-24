package com.bcinfo.tripaway.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UploadPicturesRecordDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.view.ScrollView.MyMicroBlogScrollView;
import com.bcinfo.tripaway.view.ScrollView.MyMicroBlogScrollView.OnDelPhotoListener;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.bcinfo.tripaway.view.refreshandload.PullableScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 我的游记
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月15日 上午10:42:25
 */
@SuppressWarnings("deprecation")
public class MyMicroBlogActivity extends BaseActivity implements OnClickListener
{
    /**
     * 微游记-显示微游记数据控件
     */
    private MyMicroBlogScrollView mBlogScrollView;
    
 // 自定义上拉加载相对布局
    private PullToRefreshLayout pullToRefreshLayout;
 // 自定义滑动
    private PullableScrollView pullableScrollView;
    
private boolean isPullRefresh = false;
private int pageNum = 1;
private int pagesize = 10;
    private boolean isLoadMore = false;
    
    private int isRefresh = 0;
    
    /**
     * 我的游记删除标签
     */
    private LinearLayout my_micro_delete;
    
    
    private MyBlogDeleteDialog myBlogDeleteDialog;
    
    /**
     * 
     * 存放微游记实体的list集合
     */
    private ArrayList<TripArticle> articleList = new ArrayList<TripArticle>();
    
    private ArrayList<ArrayList<String>> blogPhotoList;
    
    /**
     * 
     * 添加照片 控件
     * 
     */
    private ImageView addPhotoBtn;
    
    /**
     * 日期格式化
     */
    private SimpleDateFormat dateFormat;
    
    /**
     * 拍照照片存放的根路径
     */
    private File imageFile;
    
    /**
     * 拍照的单一照片的路径
     */
    private File file;
    
    /**
     * 拍照照片集合
     */
    private ArrayList<Bitmap> imageList;
    
    /**
     * 半透明全屏背景
     */
    private LinearLayout fullScreenLayout;
    
    /**
     * 拍照
     */
    private RelativeLayout photoTakenbyPhoneLayout;
    
    /**
     * 从手机相册选择
     * 
     */
    private RelativeLayout photoTakenbyAlbumLayout;
    
    /**
     * 加载旋转框
     */
    private ProgressBar FooterBar;
    
    private ArrayList<String> photoIdList = new ArrayList<String>();
    
    private RelativeLayout second_title;
    
    private TextView TextOfFooterBar;
    
    private String userId;
    
    private LinearLayout mBackBtn;
    /**
     * 向上出现的抽屉
     */
    @SuppressWarnings("deprecation")
    private SlidingDrawer drawle;
    
    /**
     * boolean 标记位 添加照片控件是否已经旋转45度 true表示已经旋转45度 ；false表示没有旋转 "+"
     */
    private boolean isRotate;
    
    public void setBlogPhotoList(ArrayList<ArrayList<String>> blogPhotoList)
    {
        this.blogPhotoList = blogPhotoList;
    }
    
    public MyMicroBlogScrollView getmBlogScrollView()
    {
        return mBlogScrollView;
    }
    
    public void setmBlogScrollView(MyMicroBlogScrollView mBlogScrollView)
    {
        this.mBlogScrollView = mBlogScrollView;
    }
    
    public ArrayList<TripArticle> getArticleList()
    {
        return articleList;
    }
    
    public void setArticleList(ArrayList<TripArticle> articleList)
    {
        this.articleList = articleList;
    }
    
    private final String IMAGE_UNSPECIFIED = "image/*";
    
    private UploadPicturesRecordDB uploadPicturesRecord;
    
    private UserInfo userInfo;
    
    private RoundImageView photoImage;
    
    private ImageView backgroundImage;
    
    private TextView mPhotoName;
    
    private TextView mIntroduce;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
//        userInfo = getIntent().getParcelableExtra("userInfo");
        
        userId=PreferenceUtil.getUserId();
        // 判断手机是否有sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            // 创建存放拍照照片的目录
            imageFile = new File(Environment.getExternalStorageDirectory() + "/myImage");
            imageFile.mkdirs();// 创建目录
        }
        else
        {
            Toast.makeText(this, "无sd卡", 1000).show();
        }
        setContentView(R.layout.my_micro_blog_activity);
        setSecondTitle("我的游记");
//        photoImage = (RoundImageView)findViewById(R.id.club_author_photo);
//        backgroundImage = (ImageView) findViewById(R.id.page_background);
//        mPhotoName = (TextView)findViewById(R.id.club_name);
//        mIntroduce = (TextView) findViewById(R.id.person_intro);
        mBlogScrollView = (MyMicroBlogScrollView)findViewById(R.id.my_scroll_view);
        
        mBlogScrollView.setmActivity(this);
        mBlogScrollView.setmBlogScollView(mBlogScrollView);
        mBlogScrollView.setOnDelPhotoListener(new OnDelPhotoListener() {
			@Override
			public void delPhotoById(String id) {
				//删除的Photo集合
				photoIdList.add(id);
			}
		});
        LayoutInflater layout=this.getLayoutInflater();
        View view=layout.inflate(R.layout.item_blog, null);
        my_micro_delete=(LinearLayout)view.findViewById(R.id.my_micro_delete);
        my_micro_delete.setVisibility(View.VISIBLE);
        my_micro_delete.setOnClickListener(this);
        fullScreenLayout = (LinearLayout)findViewById(R.id.topPhotoLayout);
        second_title = (RelativeLayout)findViewById(R.id.second_title);
        second_title.getBackground().setAlpha(255);
        mBackBtn = (LinearLayout)findViewById(R.id.layout_back_button);
        mBackBtn.setOnClickListener(this);
        photoTakenbyPhoneLayout = (RelativeLayout)findViewById(R.id.layout_photo_by_phone_taken_container);
        photoTakenbyPhoneLayout.setOnClickListener(this);
        photoTakenbyAlbumLayout = (RelativeLayout)findViewById(R.id.layout_photo_by_album_taken_container);
        photoTakenbyAlbumLayout.setOnClickListener(this);
        addPhotoBtn = (ImageView)findViewById(R.id.addPhotoBtn);
        pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.allThemes_container);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        fullScreenLayout.setOnClickListener(this);
        addPhotoBtn.setOnClickListener(this);
        drawle = (SlidingDrawer)findViewById(R.id.upSlidingDrawer);
        uploadPicturesRecord = new UploadPicturesRecordDB();// 上传数据记录的数据库
        FooterBar = (ProgressBar)findViewById(R.id.footerBar);
        TextOfFooterBar = (TextView)findViewById(R.id.text_of_footerBar);
        testMyStorySelfUrl(userId,0);
        IntentFilter intentFilter = new IntentFilter();
    	intentFilter.addAction("close");
    	intentFilter.addAction("com.bcinfo.view.delMyBlog");
    	intentFilter.addAction("com.bcinfo.refreshCommentsCount");
    	intentFilter.addAction("com.bcinfo.publishBlog");
    	registerReceiver(broadcastReceiver, intentFilter);
    }
    
    /*
     * test 我的旅行故事 (微游记列表接口)
     */
    private void testMyStorySelfUrl(String userId,final int flag)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);
        params.put("pagesize", pagesize);
//        params.put("userId", userId);
        HttpUtil.get(Urls.tripStory_self_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                
                // System.out.println(statusCode);
               System.out.println(response.toString());
                // System.out.println(statusCode);
                // System.out.println(response);
                getTripStoryList(response,flag);
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
                // System.out.println(statusCode);
                // System.out.println(responseString);
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setText("加载失败!");
            }
        });
        System.out.println("******加载完毕******");
    }
    
    /*
     * 解析从服务端获取的微游记参数
     */
    private void getTripStoryList(JSONObject response,int flag)
    {
        
        // System.out.println(response);
        if ("00000".equals(response.optString("code")))
        {
        	if (isLoadMore)
            {
                // 上拉返回的结束加载更多布局
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
            if (isPullRefresh)
            {
                // 下拉刷新返回的
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        	
            JSONObject dataObj = response.optJSONObject("data");
            JSONArray dataArray = dataObj.optJSONArray("tripstory");
            if (dataArray != null && dataArray.length() != 0)
            {
                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject tripStoryObj = dataArray.optJSONObject(i);
                    TripArticle articleObj = new TripArticle();
                    articleObj.setPublishTime(tripStoryObj.optString("publishTime"));
                    articleObj.setLocation(tripStoryObj.optString("location"));
                    articleObj.setIsLike(tripStoryObj.optString("isLike"));
                    articleObj.setDescription(tripStoryObj.optString("description"));
                    articleObj.setComments(tripStoryObj.optString("comments"));
                    articleObj.setPhotoId(tripStoryObj.optString("photoId"));
                    JSONArray picArray = tripStoryObj.optJSONArray("pictures");
                    if (picArray != null)
                    {
                        for (int j = 0; j < picArray.length(); j++)
                        {
                            JSONObject picObj = picArray.optJSONObject(j);
                            ImageInfo picRes = new ImageInfo();
                            picRes.setDesc(picObj.optString("desc"));
                            picRes.setUrl(picObj.optString("url"));
                            articleObj.getPictureList().add(picRes);
                            
                        }
                    }
                    JSONObject userObj = tripStoryObj.optJSONObject("publisher");
                    
                    if(userObj != null){
                    	 articleObj.getPublisher().setGender(userObj.optString("sex"));
                         articleObj.getPublisher().setStatus(userObj.optString("status"));
                         articleObj.getPublisher().setEmail(userObj.optString("email"));
                         articleObj.getPublisher().setNickname(userObj.optString("nickName"));
                         articleObj.getPublisher().setUserId(userObj.optString("userId"));
                         articleObj.getPublisher().setAvatar(userObj.optString("avatar"));
                         articleObj.getPublisher().setIntroduction(userObj.optString("introduction"));
                         articleObj.getPublisher().setMobile(userObj.optString("mobile"));
                         JSONArray tagsArray = userObj.optJSONArray("tags");
                         if (tagsArray != null)
                         {
                             for (int k = 0; k < tagsArray.length(); k++)
                             {
                                 articleObj.getPublisher().getTags().add(tagsArray.optString(k));
                                 
                             }
                         }
                    }
                   
                    articleList.add(articleObj);
                }
                // System.out.println(articleList.get(0)+"\n"+articleList.get(1));
                
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setVisibility(View.GONE);
                if(flag == 0){
                	Message msg = mBlogScrollView.tripStoryHandler.obtainMessage();
                	msg.what = 0x0011;
                	msg.obj = articleList;
                	msg.arg1 = isRefresh;
                	msg.sendToTarget();
                }else if(flag == 1){
                	Message msg = mBlogScrollView.tripAddHandler.obtainMessage();
                	msg.what = 0x0011;
                	msg.obj = articleList;
                	msg.arg1 = isRefresh;
                	msg.sendToTarget();
                }
            }
            else
            {
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setText("抱歉，未找到相关数据!");
                Message msgEmpty = mBlogScrollView.tripStoryHandler.obtainMessage();
                msgEmpty.what = 0x0012;
                msgEmpty.obj = articleList;
                msgEmpty.sendToTarget();
            }
            
            // mBlogScrollView = (MicroBlogScrollView)
            // findViewById(R.id.my_scroll_view);
            // mBlogScrollView.setmActivity(this);
        }
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
    
    @Override
    public void onDestroy()
    {
        // TODO 自动生成的方法存根
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addPhotoBtn:
                /*Animation alphaAnimation1 = AnimationUtils.loadAnimation(this, R.anim.alpha_animation1);
                Animation alphaAnimation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_animation2);
                if (!isRotate) // 第一次点击 打开照片上传控件
                {
                    fullScreenLayout.setAnimation(alphaAnimation1); // 为半透明的背景设置淡入淡出动画
                    fullScreenLayout.setVisibility(View.VISIBLE);
                    RotateAnimation rotateAnimation =
                        new RotateAnimation(0, 135, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(500);
                    rotateAnimation.setRepeatCount(0);
                    rotateAnimation.setFillAfter(true);
                    view.startAnimation(rotateAnimation);
                    // 设置抽屉是可见的
                    drawle.setVisibility(View.VISIBLE);
                    isRotate = true;
                    // 设置 自定义列表标记flag为true，表示该自定义ScrollView不可以滑动
                    // mBlogScrollView.setFlag(true);
                }
                else
                { // 第二次点击 关闭上传照片控件
                    fullScreenLayout.setAnimation(alphaAnimation2);
                    RotateAnimation rotateReserveAnimation =
                        new RotateAnimation(135, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateReserveAnimation.setDuration(500);
                    rotateReserveAnimation.setRepeatCount(0);
                    rotateReserveAnimation.setFillAfter(true);
                    view.startAnimation(rotateReserveAnimation);
                    fullScreenLayout.setVisibility(View.GONE);
                    drawle.setVisibility(View.VISIBLE);
                    isRotate = false;
                    // 设置标记位为false 表示该ScrollView变成可以滑动了
                    // mBlogScrollView.setFlag(false);
                }
                drawle.animateToggle();*/
            	Intent intent=new Intent(MyMicroBlogActivity.this,MicroBlogPublishedActivity.class);
            	startActivity(intent);
                break;
            case R.id.my_micro_delete:
            	
            	break;
            case R.id.layout_back_button:
            	if(photoIdList.size()>0){
            		Intent it = new Intent();
            		it.setAction("com.bcinfo.delPhoto");
            		sendBroadcast(it);
                	finish();
            	}else{
            		finish();
            	}
            	break;
            case R.id.layout_photo_by_phone_taken_container: // 拍照
                // 调用系统相册的intent
                RotateAnimation rotateReserveAnimationRb1 =
                    new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateReserveAnimationRb1.setDuration(300);
                rotateReserveAnimationRb1.setRepeatCount(0);
                rotateReserveAnimationRb1.setFillAfter(true);
                addPhotoBtn.startAnimation(rotateReserveAnimationRb1);
                rotateReserveAnimationRb1.setAnimationListener(new AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                        // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        // TODO Auto-generated method stub
                        String currMapName = dateFormat.format(new Date()) + ".jpg";
                        Intent pointIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file = new File(imageFile.getAbsolutePath() + "/" + currMapName);
                        pointIntent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(pointIntent1, 1);
                    }
                });
                fullScreenLayout.setVisibility(View.GONE);
                drawle.close();
                isRotate = false;
                // mBlogScrollView.setFlag(false);
                break;
            case R.id.layout_photo_by_album_taken_container: // 从手机相册选择
                RotateAnimation rotateReserveAnimationRb2 =
                    new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateReserveAnimationRb2.setDuration(300);
                rotateReserveAnimationRb2.setRepeatCount(0);
                rotateReserveAnimationRb2.setFillAfter(true);
                addPhotoBtn.startAnimation(rotateReserveAnimationRb2);
                rotateReserveAnimationRb2.setAnimationListener(new AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                        // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        // TODO Auto-generated method stub
                        Intent pointIntent2 = new Intent(Intent.ACTION_PICK, null);
                        pointIntent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pointIntent2, 2);
                    }
                });
                drawle.close();
                fullScreenLayout.setVisibility(View.GONE);
                isRotate = false;
                // mBlogScrollView.setFlag(false);
                break;
            case R.id.topPhotoLayout:
                Animation alphaAnimation3 = AnimationUtils.loadAnimation(this, R.anim.alpha_animation2);
                fullScreenLayout.setAnimation(alphaAnimation3); // 为半透明的背景设置淡入淡出动画
                fullScreenLayout.setVisibility(View.GONE);
                RotateAnimation rotateReserveAnimationTop =
                    new RotateAnimation(135, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateReserveAnimationTop.setDuration(500);
                rotateReserveAnimationTop.setRepeatCount(0);
                rotateReserveAnimationTop.setFillAfter(true);
                addPhotoBtn.startAnimation(rotateReserveAnimationTop);
                isRotate = false;
                if (drawle != null && drawle.isOpened())
                {
                    drawle.animateClose();
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Uri newuri = Uri.fromFile(file);
            uploadPicturesRecord.saveloadPicturesRecord(newuri.toString(), newuri.toString(), true);
            startPhotoZoom(newuri);
            
        }
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            if (data == null)
            {
                Toast.makeText(getBaseContext(), "无法加载此图片!", Toast.LENGTH_SHORT).show();
                return;
            }
            file = null;
            startPhotoZoom(data.getData());
        }
        else if (requestCode == 3 && resultCode == Activity.RESULT_OK)
        {
            // 获得拍照的数据
            Bundle bundle = data.getExtras();
            // 获得拍照的照片
            Bitmap currentBitMap = (Bitmap)bundle.get("data");
            imageList = new ArrayList<Bitmap>();
            imageList.add(currentBitMap);
            Intent publishIntent = new Intent(this, MicroBlogPublishedActivity.class);
            publishIntent.putParcelableArrayListExtra("listArgs", imageList);
            startActivity(publishIntent);
            overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
        }
        
    }
    
    /**
     * 收缩图片
     * 
     * @param uri
     */
    public void startPhotoZoom(Uri uri)
    {
        
        if (file == null)
        {
            // if(("-1").equals(uploadPicturesRecord.isRecordExist(uri.toString()))){
            file = new File(imageFile.getAbsolutePath() + "/" + dateFormat.format(new Date()) + ".jpg");
            uploadPicturesRecord.saveloadPicturesRecord(uri.toString(), Uri.fromFile(file).toString(), true);
            // }else{
            // uploadPicturesRecord.saveloadPicturesRecord(uri.toString(),"",false);
            // isRecordExist(uri);
            // return;
            // }
        }
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, 3);
        
    }
    
    /**
     * 图片已加载过
     * 
     * @param uri
     */
    private void isRecordExist(Uri uri)
    {
        Bitmap selectedBitMap = null;
        String imageuri = uploadPicturesRecord.readimageuriRecord(uri.toString());
        if (imageuri == null)
        {
            return;
        }
        Uri myuri = Uri.parse(imageuri);
        try
        {
            selectedBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), myuri);// 显得到bitmap图片
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (selectedBitMap != null)
        {
            imageList = new ArrayList<Bitmap>();
            imageList.add(selectedBitMap);
            Intent publishIntent1 = new Intent(this, MicroBlogPublishedActivity.class);
            publishIntent1.putParcelableArrayListExtra("listArgs", imageList);
            startActivity(publishIntent1);
            overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if("com.bcinfo.view.delMyBlog".equals(action)){
    			mBlogScrollView.refreshDrawableState();
    			articleList.clear();
            	isRefresh =0;
                isPullRefresh = true;
                pageNum = 1;
                photoIdList.add("");
                testMyStorySelfUrl(userId,0);
    		}else if("com.bcinfo.publishBlog".equals(action)){
    			articleList.clear();
            	isRefresh =0;
                isPullRefresh = true;
                pageNum = 1;
                photoIdList.add("");
                testMyStorySelfUrl(userId,1);
    		}
    	     if("com.bcinfo.refreshCommentsCount".equals(action)){
             	String count=intent.getStringExtra("count");
             	String microId=intent.getStringExtra("microId");
             	if(count!=null&&microId!=null){
             	for(TripArticle article:articleList){
             		if(article.getPhotoId().equals(microId)){
             			article.setComments(count);
             			break;
             		}
             	}
             	}
             	
             }
    	}
    	};
    	@Override
    	protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

    	}
    	/**
         * 定义一个实现OnRefreshListener接口的实现类对象
         * 
         * @author caihelin
         * 
         */
        class MyListener implements OnRefreshListener
        {
            
            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
            {
            	isRefresh = 1;
                if (articleList.size() == 0)
                {
                    return;
                }
                else if (articleList.size() % 10 == 0)
                {
                    pageNum = pageNum + 1;
                    //pagesize = pagesize + 10;
                    testMyStorySelfUrl(userId,0);
                    
                }
                else
                {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
                    Toast.makeText(MyMicroBlogActivity.this, "加载完毕", Toast.LENGTH_LONG).show();
                    
                }
            }
            
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout)
            {
                // TODO Auto-generated method stub
            	//mBlogScrollView.refreshDrawableState();
            	articleList.clear();
            	isRefresh =0;
                isPullRefresh = true;
                pageNum = 1;
                testMyStorySelfUrl(userId,0);
            }
            
        }
}
