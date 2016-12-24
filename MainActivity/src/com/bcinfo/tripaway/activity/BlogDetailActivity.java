package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.imageviewer.fragment.ImageDetailFragment;
import com.bcinfo.imageviewer.view.HackyViewPager;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.net.HttpDeleteWithBody;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog.OnBlogDeleteListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 微游记记录详情Activity
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月7日 11:45:01
 */
public class BlogDetailActivity extends BaseActivity implements OnPageChangeListener, OnClickListener,
    OnBlogDeleteListener
{
    // 静态常量STATE_POSITION 当手机屏幕方向改变时传入的参数值
    public static final String STATE_POSITION = "state_position";
    
    // 静态常量BLOG_IMAGE_INDEX 表示当前显示的是发布者发布的第几张照片 默认显示的是一个张照片 即position为0
    public static final String BLOG_IMAGE_INDEX = "blog_image_index";
    
    private MyBlogDeleteDialog myBlogDeleteDialog;
    
    public static final String BLOG_FLAG = "blog_article";
    
    // 自定义微游记照片滑动显示控件
    private HackyViewPager blogPager;
    
    private BlogImagePagerAdapter blogAdapter;
    
    private ImageView blogDeleteBtn;
    
    private LinearLayout backBtn;
    
    private RoundImageView micro_publisher_photo;
    
    // 微游记发布者姓名
    private TextView authorNameTV;
    
    // 返回按钮
    private ImageView detailBackBtn;
    
    // 更多选项
    private TextView detailMoreBtn;
    
    // 喜欢
    private CheckedTextView detailIsLoveBtn;
    
    // 删除图标
    private ImageView deleteIconBtn;
    
    private TextView micro_comments;
    
    // 页码
    private TextView pageCountTv;
    
    // 游记内容
    private TextView blogContentTV;
    
    private TripArticle article=new TripArticle();
    
    // 定义当前显示的是第几张照片
    private int pagerPosition;
    
    // 发布时间
    private TextView blogTimeTv;
    
    // 传过来的microId
    private String microId;
 // 微游记标识id
    private String photoId;
    
    private FrameLayout microLayout;
    
    public int getPagerPosition()
    {
        return pagerPosition;
    }
    
    public void setPagerPosition(int pagerPosition)
    {
        this.pagerPosition = pagerPosition;
    }
    
    public TextView getBlogContentTV()
    {
        return blogContentTV;
    }
    
    public void setBlogContentTV(TextView blogContentTV)
    {
        this.blogContentTV = blogContentTV;
    }
    
    public TextView getAuthorNameTV()
    {
        return authorNameTV;
    }
    
    public void setAuthorNameTV(TextView authorNameTV)
    {
        this.authorNameTV = authorNameTV;
    }
    
    public HackyViewPager getBlogPager()
    {
        return blogPager;
    }
    
    public void setBlogPager(HackyViewPager blogPager)
    {
        this.blogPager = blogPager;
    }
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        
        setContentView(R.layout.blog_image_viewer_activity);
        blogDeleteBtn = (ImageView)findViewById(R.id.detail_delete_btn);
        micro_publisher_photo= (RoundImageView)findViewById(R.id.micro_publisher_photo);
        micro_comments= (TextView)findViewById(R.id.micro_comments);
        microLayout = (FrameLayout) findViewById(R.id.micro_layout);
        microLayout.setOnClickListener(this);
        microId = getIntent().getStringExtra("blog_article_id");
        System.out.println("*****blog_article_id********"+microId);
        // SharePreferenceUtil spUtil = Constant.getInstance().getSpUtil();
        String blog_article_author_avatar = getIntent().getStringExtra("blog_article_author_avatar");
        String comments=getIntent().getStringExtra("blog_article_comments");
        micro_comments.setText(comments);
        micro_comments.setOnClickListener(this);
        if (!StringUtils.isEmpty(blog_article_author_avatar))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + blog_article_author_avatar,
            		micro_publisher_photo,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        
        
        // System.out.println("preferences.xml  id="+spUtil.getUserId());
        if (!PreferenceUtil.getUserId().equals(getIntent().getStringExtra("blog_article_author_id")))
        {
            blogDeleteBtn.setVisibility(View.GONE); // 设置 "删除游记"隐藏
        }
        blogDeleteBtn.setTag(photoId);
        /*micro_comments.setTag(microId);*/
        blogDeleteBtn.setOnClickListener(this);
        ArrayList<ImageInfo> blogResList = getIntent().getParcelableArrayListExtra("blog_article_pic_list"); // 获得微游记的照片集
        // 获得转向intent所携带的BLOG_IMAGE_INDEX数值 当前所显示的照片的index 默认显示第一个position处的照片
        pagerPosition = getIntent().getIntExtra(BLOG_IMAGE_INDEX, 0);
        blogAdapter = new BlogImagePagerAdapter(this.getSupportFragmentManager(), blogResList);
        blogPager = (HackyViewPager)findViewById(R.id.blogPager);
        pageCountTv = (TextView)findViewById(R.id.pageCountTv);
        // HttpDelete httpDelete=new HttpDelete(Urls);
        // 设置适配器
        blogPager.setAdapter(blogAdapter);
        // 设置ViewPager的监听器
        blogPager.setOnPageChangeListener(this);
        authorNameTV = (TextView)findViewById(R.id.blog_authorName);
        authorNameTV.setText(getIntent().getStringExtra("blog_article_author_nickName"));
        // System.out.println("authorNameTV=" + authorNameTV);
        //Toast.makeText(this, "" + authorNameTV.getText().toString(), 0).show();
        //Typeface typeFace = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        detailBackBtn = (ImageView)findViewById(R.id.detail_back_icon);
        //detailMoreBtn = (TextView)findViewById(R.id.detail_more_icon);
        //detailIsLoveBtn = (CheckedTextView)findViewById(R.id.detail_is_like_icon);
        deleteIconBtn = (ImageView)findViewById(R.id.detail_delete_btn);
        //detailBackBtn.setTypeface(typeFace);
        //detailMoreBtn.setTypeface(typeFace);
        //detailIsLoveBtn.setTypeface(typeFace);
        //deleteIconBtn.setTypeface(typeFace);
        blogContentTV = (TextView)findViewById(R.id.blog_content);
        backBtn = (LinearLayout)findViewById(R.id.back);
        blogTimeTv = (TextView)findViewById(R.id.blog_published_time_tv);
        detailBackBtn.setOnClickListener(mOnClickListener);
        
        String publishTime = getIntent().getStringExtra("blog_article_current_date");
        String publishTimeValue = publishTime.substring(0, 8);
        String publishYear = publishTimeValue.substring(0, 4);// 年
        String publishMonth = publishTimeValue.substring(4, 6); // 月
        String publishDay = publishTimeValue.substring(6);// 日
        blogTimeTv.setText(publishYear + "/" + publishMonth + "/" + publishDay);
        
        // 设置 微游记的内容
        blogContentTV.setText(getIntent().getStringExtra("blog_article_description"));
        
        if (bundle != null)
        {
            
            pagerPosition = bundle.getInt(STATE_POSITION);
        }
        
        blogPager.setCurrentItem(pagerPosition);
        pageCountTv.setText(this.getString(R.string.viewpager_indicator, pagerPosition + 1, blogAdapter.getCount()));
    }
    
    /*
     * onSaveInstanceState()方法 表示当Activity遇到屏幕方向改变等情况时，会先调用onSaveInstanceState()方法保存bundle参数
     * 
     * 改变后会重新执行oncreate()方法 返回原来保存的参数
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        
        outState.putInt(STATE_POSITION, blogPager.getCurrentItem());
        
    }
    
    /**
     * 自定义微游记照片适配器
     * 
     * @function
     * @author caihelin
     * @version 1.0 2015年1月27日 16:54:33
     * 
     */
    public class BlogImagePagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<ImageInfo> blogResList;
        
        public BlogImagePagerAdapter(FragmentManager fragmentManager, ArrayList<ImageInfo> blogResList)
        {
            super(fragmentManager);
            
            this.blogResList = blogResList;
        }
        
        @Override
        public Fragment getItem(int position)
        {
            String url = "";
            if (!StringUtils.isEmpty(blogResList.get(position).getUrl()))
            {
                url = blogResList.get(position).getUrl();
            }
            ImageDetailFragment blogDetailFragment = new ImageDetailFragment();
            
            return blogDetailFragment.newInstance(url);
            
        }
        
        @Override
        public int getCount()
        {
            
            return blogResList != null ? blogResList.size() : 0;
        }
        
    }
    
    @Override
    public void onPageScrollStateChanged(int arg0)
    {
        
    }
    
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
        
    }
    
    @Override
    public void onPageSelected(int position)
    {
        pageCountTv.setText(this.getString(R.string.viewpager_indicator, position + 1, blogAdapter.getCount()));
        
    }
    
    @Override
    public void onClick(View view)
    {
        
        switch (view.getId())
        {
            case R.id.detail_delete_btn:
            	/*ImageView deleteBtn = (ImageView)view;
                String photoId = (String)deleteBtn.getTag(); // 获得欲删除的微游记标识 id
                System.out.println("****photoId**********"+photoId);*/
                new MyBlogDeleteDialog(this, new OnBlogDeleteListener() {
					@Override
					public void onDelete(String photoId) {
						delMyBlogPhoto(photoId);
					}
				}, microId).show();
                
                break;
            case R.id.detail_back_icon:
                finish();
                activityAnimationClose();
                break;
            case R.id.micro_layout:
            case R.id.micro_comments:
            	/*TextView deleteBtn0 = (TextView)view;
                String microId = (String)deleteBtn0.getTag(); // 获得欲删除的微游记标识 id                
*/            	System.out.println("****microId**********"+microId);
TextView countBtn;
if(view instanceof FrameLayout)
			 countBtn = (TextView)((FrameLayout)view).getChildAt(1);
else 
	 countBtn = (TextView)view;
            	Intent intent=new Intent(BlogDetailActivity.this,UserMicroCommentActivity.class);
            	intent.putExtra("objectType", "tripStory");
            	intent.putExtra("microId", microId);
            	String counts = countBtn.getText().toString();
            	if(StringUtils.isEmpty(counts)){
            		intent.putExtra("count",0);
            	}else{
            		intent.putExtra("count",Integer.parseInt(counts));
            	}
            	
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int  requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	if(resultCode == 1){
    		Bundle  b =intent.getExtras();
    		String count = b.getString("count");
    		micro_comments.setText(count);
    		Intent intent1=new Intent("com.bcinfo.refreshCommentsCount");
			intent1.putExtra("count", count);
			intent1.putExtra("microId", microId);
			sendBroadcast(intent1);
    	}
    }
    @Override
    public void onDelete(final String photoId)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(Urls.tripStory_delete_url + photoId); // HttpDelete请求方式
                
                try
                {
                    Map<String, String> mapParams = HttpUtil.getHeadersMap(Urls.tripStory_delete_url);
                    for (Map.Entry<String, String> entry : mapParams.entrySet())
                    {
                        httpDelete.addHeader(entry.getKey(), entry.getValue()); // 添加请求头
                        
                    }
                    // 添加请求头 token和session
                    httpDelete.addHeader("token", PreferenceUtil.getToken());
                    httpDelete.addHeader("session", PreferenceUtil.getSession());
                    HttpClient httpClient = new DefaultHttpClient();
                    
                    HttpResponse httpResponse = httpClient.execute(httpDelete);
                    System.out.println("tag" + photoId);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                    {
                        
                        String strResult = EntityUtils.toString(httpResponse.getEntity());// 服务端返回的数据
                        System.out.println("服务端返回的数据:" + strResult);
                        
                        if (myBlogDeleteDialog != null && myBlogDeleteDialog.isShowing())
                        {
                            myBlogDeleteDialog.dismiss();
                            
                        }
                        
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    
                }
            }
        }).start();
        
    }
    
    private void delMyBlogPhoto(String photoId){
		final String photoIdDel = photoId;
		HttpUtil.delete(Urls.tripStory_delete_url+photoId,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					ToastUtil.showToast(BlogDetailActivity.this, "删除成功");
					//删除我的微游记中的对象
					Intent it = new Intent("com.bcinfo.view.delMyBlog");
					sendBroadcast(it);
					finish();
//					articleList.remove(delId);
//					imageViewList.remove(delId);
//					firstColumn.removeAllViews();
//					secondColumn.removeAllViews();
//					//重新刷新页面
//					page=0;
//					loadMoreImages();
//					onDelPhotoListener.delPhotoById(photoIdDel);
//					Intent it = new Intent();
//					it.setAction("com.bcinfo.tripaway.delMicro");
//					it.putExtra("photo", photoIdDel);
//					mActivity.sendBroadcast(it);
//					checkVisibility();
		            
		            // 此时需要更新微游记页面，删除指定对象
		            // 发送handler执行
		            
				}else if("00099".equals(response.optString("code"))){
					ToastUtil.showToast(BlogDetailActivity.this, "用户未登录");
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i("BlogDetailActivity", "delete_myblog_responseString", responseString);
				LogUtil.i("BlogDetailActivity", "delete_myblog_statusCode", statusCode+"");
			}
		});
    }
}
