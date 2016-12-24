package com.bcinfo.tripaway.view.ScrollView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.listener.Pullable;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.BlogImageRescource;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.loadIMG.BlogImageLoader;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog.OnBlogDeleteListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 自定义ScrollView
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月17日 15:05:11
 * 
 */
public class MyMicroBlogScrollView extends ScrollView implements OnTouchListener,Pullable
{
    /**
     * 每页要加载的图片数量 每页加载15张照片
     */
    public static final int PAGE_SIZE = 10;
    
    private static final String TAG="MyMicroBlogScrollView";
    
    /**
     * 自定义ScrollView所在的Activity
     */
    private static Activity mActivity;
    
    private OnDelPhotoListener onDelPhotoListener;
    
    private MyMicroBlogScrollView mBlogScollView;
    
    /**
     * 我的游记删除标签
     */
    private LinearLayout my_micro_delete;
    /**
     * 存放微游记实体对象的list集合
     */
    private static ArrayList<TripArticle> articleList = new ArrayList<TripArticle>();
    
    private Map<Integer, ArrayList<String>> imageMap;
    
    /**
     * 微游记作者头像照片的数组
     */
    private String[] headerIcons;
    
    /**
     * 记录当前已加载到第几页
     */
    private int page;
    
    /**
     * 每一列的宽度
     */
    private int columnWidth;
    
    /**
     * 当前第一列的高度
     */
    private int firstColumnHeight;
    
    /**
     * 当前第二列的高度
     */
    private int secondColumnHeight;
    
    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;
    
    /**
     * 对图片进行管理的工具类
     */
    private BlogImageLoader imageLoader;
    private MyBlogDeleteDialog myBlogDeleteDialog;
    /**
     * 第一列的布局
     */
    private LinearLayout firstColumn;
    
    /**
     * 第二列的布局
     */
    private LinearLayout secondColumn;
    
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private static Set<LoadImageTask> taskCollection;
    
    /**
     * MyScrollView下的直接子布局。
     */
    private static View scrollLayout;
    
    /**
     * MyScrollView根布局的高度。
     */
    private static int scrollViewHeight;
    
    /**
     * 记录之前垂直方向的滚动距离。
     */
    private static int lastScrollY = -1;
    
    public Activity getmActivity()
    {
        return mActivity;
    }
    
    public void setmActivity(Activity mActivity)
    {
        this.mActivity = mActivity;
    }
    
    public  Handler tripAddHandler  =new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0x0011)
            {
            	ArrayList<TripArticle> list = (ArrayList<TripArticle>)msg.obj;
                articleList = list;
                if(msg.arg1 == 0){
                	imageViewList.clear();
                	firstColumn.removeAllViews();
                	firstColumnHeight= 0;
                	secondColumnHeight = 0;
                	secondColumn.removeAllViews();
                	page=0;
                }
                loadMoreImages();
                Message message = new Message();
                message.obj = mBlogScollView;
                handler.sendMessageDelayed(message, 5);
            }
        }
        };
    public  Handler tripStoryHandler  =new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0x0011)
            {
            	ArrayList<TripArticle> list = (ArrayList<TripArticle>)msg.obj;
                articleList = list;
                if(msg.arg1 == 0){
                	imageViewList.clear();
                	firstColumn.removeAllViews();
                	firstColumnHeight= 0;
                	secondColumnHeight = 0;
                	secondColumn.removeAllViews();
                	page=0;
                }
                Message message = new Message();
                message.obj = mBlogScollView;
                handler.sendMessageDelayed(message, 5);
            }
            else if (msg.what == 0x0012)
            {
                ArrayList<TripArticle> articleList = (ArrayList<TripArticle>)msg.obj;
                if (articleList.size() == 0)
                {
//                    Toast.makeText(mActivity, "游记列表为空!", 0).show();
                    
                }
                
            }
        }
        };
    
    /**
     * init list集合方法
     */
    private void initList()
    {
        
//        initImageList(); 
//		articleList = new ArrayList<TripArticle>();
//		if (BlogImageRescource.imageUrls != null
//				&& BlogImageRescource.imageUrls.length > 0) {
//			for (int i = 0; i < BlogImageRescource.imageUrls.length; i++) { //
//				articleList.add(new TripArticle());
//			}
//		}
         
    }
    
    /**
     * init imageList
     */
    
	private void initImageList() {
		imageMap = new HashMap<Integer, ArrayList<String>>();
		headerIcons = new String[BlogImageRescource.imageUrls.length];
		for (int i = 0; i < BlogImageRescource.imageUrls.length; i++) {
			ArrayList<String> imgList = new ArrayList<String>();
			imgList.add(BlogImageRescource.imageUrls[i]);
			imgList.add(BlogImageRescource.imageUrls[i]);
			headerIcons[i] = "http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg";
			imageMap.put(i, imgList);
		}
	}
     
    
    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。
     */
    private List<View> imageViewList = new ArrayList<View>();
    
    public MyMicroBlogScrollView(Context context)
    {
        super(context);
    }
    
    public MyMicroBlogScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    /**
     * MyScrollView的构造函数。 自定义控件在初始化时会自动调用下面这个构造函数
     * 
     * @param context
     * @param attrs
     */
    public MyMicroBlogScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        imageLoader = BlogImageLoader.getInstance();
        taskCollection = new HashSet<LoadImageTask>();
//        initList();
        // 设置触摸监听器对象
        this.setOnTouchListener(this);
    }
    
    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
     */
    private static Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
//             if(msg.what==0x0011)
//             {
//            	 Log.i("TEST", "呈贡了");
//             System.out.println("呈贡了");
//             articleList=(ArrayList<TripArticle>)msg.obj;
//             }
            MyMicroBlogScrollView myScrollView = (MyMicroBlogScrollView)msg.obj;
            // 获得ScrollView滚动的位置
            int scrollY = myScrollView.getScrollY();
            // 如果当前的滚动位置和上次相同，表示已停止滚动
            if (scrollY == lastScrollY)
            {
                // 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight() && taskCollection.isEmpty())
                {
                    myScrollView.loadMoreImages();
                }
                myScrollView.checkVisibility();
            }
            else
            {
                lastScrollY = scrollY;
                Message message = new Message();
                message.obj = myScrollView;
                // 5毫秒后再次对滚动位置进行判断
                handler.sendMessageDelayed(message, 5);
            }
        };
    };
    
    /**
     * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce)
        {
            // 获得根布局ScrollView的高度
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout)findViewById(R.id.first_column);
            secondColumn = (LinearLayout)findViewById(R.id.second_column);
            columnWidth = firstColumn.getWidth();
            // true 表示已加载过一次layout
            loadOnce = true;
            // 开始加载所有的图片
           Timer timer =new Timer();
           timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				 Looper.prepare(); 
				 loadMoreImages();
            	 Looper.loop(); 
				
			}
		},1000);
            
            // loadMoreImages();
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return super.onInterceptTouchEvent(event);
    }
    
    /**
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // 当手指抬起的时候，隔5秒发送消息进行处理
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
        
    }
    
    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
     * 
     */
    public void loadMoreImages()
    {
        // 首先判断是否有sd卡
        if (hasSDCard())
        {
            // 开始加载的第一张图片的index
            int startIndex = page * PAGE_SIZE;
            // 每次加载的最后一张图片的index
            int endIndex = page * PAGE_SIZE + PAGE_SIZE;
            
            if (articleList != null && startIndex < articleList.size())
            {
//                Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
                if (endIndex > articleList.size())
                {
                    endIndex = articleList.size();
                }
                // for循环迭代对每一张背景照片创建一个异步线程去加载它们
                for (int i = startIndex; i < endIndex; i++)
                {
                    // 多个异步线程下载图片
                    LoadImageTask task = new LoadImageTask();
                    taskCollection.add(task);
                    task.execute(articleList.get(i),i);
                }
                page++;
            }
            else if (articleList != null && articleList.size() == 0)
            {
                
            }
            else
            {
            }
        }
        else
        {
            Toast.makeText(getContext(), "未发现sd卡", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张默认的图片。
     */
    public void checkVisibility()
    {
        // for循环遍历所有原先addView()过的View
        for (int i = 0; i < imageViewList.size(); i++)
        {
            // View imageView = imageViewList.get(i);
            View convertView = imageViewList.get(i);
            // ImageView imageVew = (ImageView)
            // imageView.findViewById(R.id.img_item_trip_blog);
            ImageView imageVew = (ImageView)convertView.findViewById(R.id.img_item_trip_blog);
            int borderTop = (Integer)imageVew.getTag(R.id.border_top);
            int borderBottom = (Integer)imageVew.getTag(R.id.border_bottom);
            if (borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight)
            {
                String imageUrl = (String)(imageVew.getTag(R.id.image_url));
                if (!StringUtils.isEmpty(imageUrl))
                {
                	ImageLoader.getInstance().displayImage(Urls.imgHost + imageUrl, imageVew);
                }
                 Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
                 if (bitmap != null)
                 {
                 imageVew.setImageBitmap(bitmap);
                 }
                 else
                 // 如果在LruCache缓存中找不到指定的图片了，那么就开启异步线程重新下载图片
                 // 这里带的参数是原先重复利用的ImageView
                 {
                 // 开启一个异步线程重新下载
                
                 LoadImageTask task = new LoadImageTask(convertView);
                 task.execute(articleList.get(i),i);
                
                 }
            }
            else
            {
                // else 表示ImageView不在屏幕的可见范围之内，那么把ImageView设置为一个空的图片
                //imageVew.setImageResource(R.drawable.ic_launcher);
                // //我看着效果不太好，所以引掉。
            }
        }
    }
    
    /**
     * 判断手机是否有SD卡。
     * 
     * @return 有SD卡返回true，没有返回false。
     */
    private boolean hasSDCard()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    
    /**
     * 异步下载微游记背景照片的任务。
     * 
     * @function
     * @author caihelin
     * @version 1.0 2015年1月20日 11:40:11
     */
    class LoadImageTask extends AsyncTask<Object, Void, Bitmap>
    {
        /**
         * 欲加载的微游记对象
         */
//        private TripArticle article;
        
        private LayoutInflater inflator;
        
        private TripArticle article;
        
        private int artId;
        Bitmap authorIcon;
        
        /**
         * 可重复使用的ImageView
         * 
         */
        // private View mImageView;
        private View convertView;
        
        public LoadImageTask()
        {
        }
        
        /**
         * 将可重复使用的ImageView传入
         * 
         * @param imageView
         */
        public LoadImageTask(View view)
        {
            // mImageView = imageView;
            convertView = view;
        }
        
        @Override
        protected Bitmap doInBackground(Object... params)
        {
            // 获得传递过来的TripArticle实例
            article = (TripArticle)params[0];
            artId = (Integer)params[1];
            // 在下载一张照片之前，要先判断LruCache照片缓存中是否存在指定url的照片
            Bitmap imageBitmap = null;
            if(article.getPictureList().size()>0){
             imageLoader.getBitmapFromMemoryCache(Urls.imgHost + article.getPictureList().get(0).getUrl());
             if (imageBitmap == null)
             {
             imageBitmap = loadImage(Urls.imgHost + article.getPictureList().get(0).getUrl());
             }
            }
            return imageBitmap;
           
            
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap != null)
            {
                // 获得加载到的原图片的宽
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int)(bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight,artId);
            }
            // 从集合中移除下载好的异步任务
            taskCollection.remove(this);
        }
        
        /**
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         * 
         * @param imageUrl 图片的URL地址
         * @return 加载到内存的图片。
         */
        private Bitmap loadImage(String imageUrl)
        {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists())
            { // 如果指定的文件不存在，那么就下载文件
                downloadImage(imageUrl);
            }
            if (imageUrl != null)
            {
                Bitmap bitmap = BlogImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), columnWidth);
                if (bitmap != null)
                {
                    // 将该图片添加到缓存中
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }
        
        /**
         * 向View中添加图片和其他一些信息
         * 
         * @param bitmap 待添加的图片
         * @param imageWidth 图片的宽度
         * @param imageHeight 图片的高度
         */
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight,int artId)
        {
            inflator = LayoutInflater.from(MyMicroBlogScrollView.this.getContext());
            LayoutParams params = new LayoutParams(imageWidth, imageHeight);
            if (convertView != null) // if 存在可重复利用的view控件
            {
                // ImageView imgView = (ImageView)
                // mImageView.findViewById(R.id.img_item_trip_blog);
                // imgView.setImageBitmap(bitmap);
                ImageView imgView = (ImageView)convertView.findViewById(R.id.img_item_trip_blog);
                imgView.setImageBitmap(bitmap);
                
                 CheckBox cbox2 = (CheckBox)
                 convertView.findViewById(R.id.isLike_trip_blog);
                 
                 ImageView circleView2 = (ImageView)
                 convertView.findViewById(R.id.master_icon_trip_blog);
                 TextView authorNameView2 = (TextView)
                 convertView.findViewById(R.id.name_trip_bloger);
                 TextView timeView2 = (TextView)
                 convertView.findViewById(R.id.time_trip_blog);
                 TextView descriptView2 = (TextView)
                 convertView.findViewById(R.id.describe_trip_blog);
                 TextView location = (TextView) convertView.findViewById(R.id.location_trip_blog);
                 if(!StringUtils.isEmpty(article.getLocation())&&!article.getLocation().contains("定位失败")){
                	 location.setText(article.getLocation());
                 }else{
                	 location.setText("");
                 }
                 if("0".equals(article.getIsLike()))
                 {
                 cbox2.setChecked(false);
                 }else if("1".equals(article.getIsLike()))
                 {
                 cbox2.setChecked(true);
                 }
                 String nickname=article.getPublisher().getNickname();
                 if(nickname!=null){
                     if (nickname.length() > 12)
                     {
                         nickname = nickname.substring(0, 8) + "...";
                     }
                     authorNameView2.setText(nickname);
                     }
                 SimpleDateFormat sdf = new
                 SimpleDateFormat("yyyyMMddhhmmss");
                 try
                 {
                 Date specifiedDate = sdf.parse(article.getPublishTime());
                 long specifiedMillions = specifiedDate.getTime();
                 Date currentDate = new Date();
                 long currentMillions = currentDate.getTime();
                
                 double value = (currentMillions - specifiedMillions) / (3600
                 * 1000); // 获得的是小时数
                 // System.out.println("小时数:"+value);
                 if (value > 24)
                 {
                 long daysValue = (long) value / 24;// 获得天数 估值
                 System.out.println("天数:" + daysValue);
                 timeView2.setText(daysValue + "天之前");
                
                 }
                 else
                 {// 小于24小时 (一天的范围内)
                 if (value >= 1)
                 {
                
                 System.out.println("小时数:" + value);
                 timeView2.setText(value + "小时之前");
                
                 }
                 else
                 {
                 int minutesValue = (int) (value * 60);
                 if (minutesValue >= 1 && minutesValue < 60)
                 {
                 System.out.println("分钟:" + minutesValue);
                 timeView2.setText(minutesValue + "分钟之前");
                
                 }
                 else
                 {
                 timeView2.setText("1分钟之前");
                
                 }
                 }
                 }
                 }
                 catch (ParseException e)
                 {
                
                 e.printStackTrace();
                 }
                 descriptView2.setText(article.getDescription());
                 imgView.setLayoutParams(params);
                 convertView.setPadding(10, 10, 10, 10);
                 imgView.setImageBitmap(bitmap);
                 imgView.setScaleType(ScaleType.FIT_XY);
                 // imageView.setPadding(5, 5, 5, 5);
                 // 设置标签tag
                 imgView.setTag(R.id.image_url, Urls.imgHost +
                 article.getPictureList().get(0).getUrl());
                 if(!StringUtils.isEmpty(article.getPublisher().getAvatar())){
                	 ImageLoader.getInstance().displayImage( Urls.imgHost + article.getPublisher().getAvatar(),circleView2);
                 ImageLoader.getInstance().displayImage(
                 Urls.imgHost + article.getPublisher().getAvatar(),
                 circleView2, AppConfig.options(R.drawable.user_defult_photo));}
//                 circleView2.setOnClickListener(new OnClickListener() //
//                // 微游记发布者头像的点击监听事件
//                 {
//                 @Override
//                 public void onClick(View v)
//                 {
//                 Intent blogIntentForPersonal = new Intent(v.getContext(),
//                		 VistorHomepageActivity.class);
//                 blogIntentForPersonal.putExtra("userId", article.getPublisher().getUserId());
//                 v.getContext().startActivity(blogIntentForPersonal);
//                 mActivity.overridePendingTransition(R.anim.activity_new,
//                 R.anim.activity_out);
//                 }
//                 });
                 // 点击微游记照片显示大图查看界面
                 imgView.setOnClickListener(new OnClickListener()
                 {
                 @Override
                 public void onClick(View v)
                 {
                 Intent blogIntentForDetail = new
                 Intent(MyMicroBlogScrollView.this.getContext(),
                 BlogDetailActivity.class);
                 blogIntentForDetail.putExtra("blog_article", article);
                 System.out.println("******micro*****article*************"+article.toString());
                 blogIntentForDetail.putExtra("blog_image_index", 0);
                 v.getContext().startActivity(blogIntentForDetail);
                 mActivity.overridePendingTransition(R.anim.activity_new,
                 R.anim.activity_out);
                 }
                 });
                 View blockView = new
                 View(MyMicroBlogScrollView.this.getContext());
                 blockView.setLayoutParams(new
                 LayoutParams(LayoutParams.MATCH_PARENT, 15));
                 blockView.setBackgroundResource(R.color.blog_gray);
                 LinearLayout addViewLayout = findColumnToAdd(imgView,
                 imageHeight);
                 // 找到当前两列中高度最小的一列，添加微游记View
                 addViewLayout.addView(blockView);
                 addViewLayout.addView(convertView);
                
            }
            else
            {
                View blogView = inflator.inflate(R.layout.item_blog, null);
                // 获得Imageview
                ImageView imageView = (ImageView)blogView.findViewById(R.id.img_item_trip_blog);
                CheckBox cbox = (CheckBox)blogView.findViewById(R.id.isLike_trip_blog);
                ImageView circleView = (ImageView)blogView.findViewById(R.id.master_icon_trip_blog);
                TextView authorNameView = (TextView)blogView.findViewById(R.id.name_trip_bloger);
                TextView timeView = (TextView)blogView.findViewById(R.id.time_trip_blog);
                TextView descriptView = (TextView)blogView.findViewById(R.id.describe_trip_blog);
                TextView location = (TextView) blogView.findViewById(R.id.location_trip_blog);
                
                my_micro_delete=(LinearLayout)blogView.findViewById(R.id.my_micro_delete);
                my_micro_delete.setVisibility(View.GONE);
                final int artIddel = artId;
                my_micro_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Toast.makeText(mActivity, "点到了", 1000).show();
						/*onDelete(article.getPhotoId());*/
		               String photoId =article.getPhotoId(); // 获得欲删除的微游记标识 id
		               new MyBlogDeleteDialog(getContext(),new OnBlogDeleteListener() {
							@Override
							public void onDelete(String photoId) {
								delMyBlogPhoto(photoId,artIddel);
							}
						} , photoId).show();
		                
					}
				});
                if ("0".equals(article.getIsLike()))
                {
                    cbox.setChecked(false);
                }
                else if ("1".equals(article.getIsLike()))
                {
                    cbox.setChecked(true);
                }
                String nickname = article.getPublisher().getNickname();
                if(nickname!=null){
                if (nickname.length() > 12)
                {
                    nickname = nickname.substring(0, 8) + "...";
                }
                authorNameView.setText(nickname);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                try
                {
                    Date specifiedDate = sdf.parse(article.getPublishTime());
                    long specifiedMillions = specifiedDate.getTime(); // 返回 发布时间
                                                                      // 指定的毫秒数
                    Date currentDate = new Date();
                    long currentMillions = currentDate.getTime();
                    
                    double value = (currentMillions - specifiedMillions) / (3600 * 1000); // 获得的是小时数
                    // System.out.println("小时数:"+value);
                    if (value > 24)
                    {
                        long daysValue = (long)value / 24;// 获得天数 估值
                        System.out.println("天数:" + daysValue);
                        timeView.setText(daysValue + "天之前");
                        
                    }
                    else
                    { // 小于24小时 (一天的范围内)
                        if (value >= 1)
                        {
                            
                            System.out.println("小时数:" + value);
                            timeView.setText((value+"").substring(0, (value+"").length()-2) + "小时之前");
                            
                        }
                        else
                        {
                            int minutesValue = (int)(value * 60);
                            if (minutesValue >= 1 && minutesValue < 60)
                            {
                                System.out.println("分钟:" + minutesValue);
                                timeView.setText(minutesValue + "分钟之前");
                                
                            }
                            else
                            {
                                timeView.setText("1分钟之前");
                                
                            }
                        }
                    }
                }
                catch (ParseException e)
                {
                    
                    e.printStackTrace();
                }
                // String currentTime=sdf.format(new Date());
                // System.out.println("当前时间:"+currentTime);
                
                // timeView.setText(article.getPublishTime());
                descriptView.setText(article.getDescription());
                imageView.setLayoutParams(params);
                if(!StringUtils.isEmpty(article.getLocation())&&!article.getLocation().contains("定位失败")){
                	location.setText(article.getLocation());
                }else{
                	location.setText("");
                }
                blogView.setPadding(10, 10, 10, 10);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ScaleType.FIT_XY);
                // imageView.setPadding(5, 5, 5, 5);
                // 设置标签tag
                if (!StringUtils.isEmpty(article.getPictureList().get(0).getUrl()))
                {
                    imageView.setTag(R.id.image_url, Urls.imgHost + article.getPictureList().get(0).getUrl());
                }
                else
                {
                    imageView.setTag(R.id.image_url, null);
                }
                if (!StringUtils.isEmpty(article.getPublisher().getAvatar()))
                {
                	System.out.println("touxiang的统一资源定位器"+article.getPublisher().getAvatar());
                	//finalBitmap.display(circleView, Urls.imgHost + article.getPublisher().getAvatar());
                    ImageLoader.getInstance().displayImage(Urls.imgHost + article.getPublisher().getAvatar(),
                        circleView,
                        AppConfig.options(R.drawable.user_defult_photo));
                }
//                circleView.setOnClickListener(new OnClickListener() // 微游记发布者头像的点击监听事件
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Intent blogIntentForPersonal = new Intent(v.getContext(), VistorHomepageActivity.class);
//                        blogIntentForPersonal.putExtra("userId", article.getPublisher().getUserId());
//                        v.getContext().startActivity(blogIntentForPersonal);
//                        mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
//                    }
//                });
                // 点击微游记照片显示大图查看界面
                imageView.setTag(article);
                imageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent blogIntentForDetail =
                            new Intent(MyMicroBlogScrollView.this.getContext(), BlogDetailActivity.class);
                        
                        blogIntentForDetail.putExtra("blog_article_author_avatar", ((TripArticle)v.getTag()).getPublisher().getAvatar());
                        blogIntentForDetail.putExtra("blog_article_comments", ((TripArticle)v.getTag()).getComments());
                        blogIntentForDetail.putExtra("blog_article_id", ((TripArticle)v.getTag()).getPhotoId());
                        blogIntentForDetail.putExtra("blog_article_author_id", ((TripArticle)v.getTag()).getPublisher()
                            .getUserId()); // 获取游记发布者的id
                        blogIntentForDetail.putExtra("blog_article_pic_list",
                            ((TripArticle)v.getTag()).getPictureList());
                        blogIntentForDetail.putExtra("blog_article_current_date",
                            ((TripArticle)v.getTag()).getPublishTime());
                        blogIntentForDetail.putExtra("blog_article_author_nickName",
                            ((TripArticle)v.getTag()).getPublisher().getNickname());
                        blogIntentForDetail.putExtra("blog_article_description",
                            ((TripArticle)v.getTag()).getDescription());
                        blogIntentForDetail.putExtra("blog_image_index", 0);
                        v.getContext().startActivity(blogIntentForDetail);
                        mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                    }
                });
                View blockView = new View(MyMicroBlogScrollView.this.getContext());
                blockView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 15));
                blockView.setBackgroundResource(R.color.blog_gray);
                LinearLayout addViewLayout = findColumnToAdd(imageView, imageHeight);
                // 找到当前两列中高度最小的一列，添加微游记View
                addViewLayout.addView(blockView);
                addViewLayout.addView(blogView);
                imageViewList.add(blogView);
            }
        }
        public void onDelete(String photoId){

        	//评论内容
    		HttpUtil.delete(Urls.tripStory_delete_url+photoId,new JsonHttpResponseHandler(){
    			@Override
    			public void onSuccess(int statusCode, Header[] headers,
    					JSONObject response) {
    				super.onSuccess(statusCode, headers, response);
    				
    				
    			}
    			
    			@Override
    			public void onFailure(int statusCode, Header[] headers,
    					String responseString, Throwable throwable) {
    				super.onFailure(statusCode, headers, responseString, throwable);
    				
    			}
    		});
        
        }
        /**
         * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
         * 
         * @param imageView
         * @param imageHeight
         * @return 应该添加图片的一列
         */
        private LinearLayout findColumnToAdd(View view, int imageHeight)
        {
            if (firstColumnHeight <= secondColumnHeight)
            {
                // 设置tag标记为之前的第一列的高度
                view.setTag(R.id.border_top, firstColumnHeight);
                // firstColumnHeight += imageHeight;
                firstColumnHeight =
                    firstColumnHeight + imageHeight + DensityUtil.dip2px(MyMicroBlogScrollView.this.getContext(), 95);
                view.setTag(R.id.border_bottom, firstColumnHeight);
                return firstColumn;
            }
            else
            {
                view.setTag(R.id.border_top, secondColumnHeight);
                // secondColumnHeight += imageHeight;
                secondColumnHeight =
                    secondColumnHeight + imageHeight + DensityUtil.dip2px(MyMicroBlogScrollView.this.getContext(), 95);
                view.setTag(R.id.border_bottom, secondColumnHeight);
                return secondColumn;
            }
        }
        
        /**
         * 将图片下载到SD卡缓存起来。
         * 
         * @param imageUrl 图片的URL地址。
         */
        private void downloadImage(String imageUrl)
        {
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try
            {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection)url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(15 * 1000);
                con.setDoInput(true);
                con.setDoOutput(true);
                bis = new BufferedInputStream(con.getInputStream());
                imageFile = new File(getImagePath(imageUrl));
                if (!imageFile.exists())
                {
                    imageFile.createNewFile();
                }
                fos = new FileOutputStream(imageFile);
                bos = new BufferedOutputStream(fos);
                byte[] b = new byte[1024];
                int length;
                // 将网络照片下载到本地指定的文件名中
                while ((length = bis.read(b)) != -1)
                {
                    bos.write(b, 0, length);
                    bos.flush();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (bis != null)
                    {
                        bis.close();
                    }
                    if (bos != null)
                    {
                        bos.close();
                    }
                    if (con != null)
                    {
                        con.disconnect();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            // 下载到本地之后，在从本地加载到这个Bitmap类型的照片
            if (imageFile != null)
            {
                Bitmap bitmap = BlogImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), columnWidth);
                if (bitmap != null)
                {
                    // 在将该照片添加到LruCache缓存中
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }
        }
        
        /**
         * 获取图片的本地存储路径。
         * 
         * @param imageUrl 图片的URL地址。
         * @return 图片的本地存储路径。
         */
        private String getImagePath(String imageUrl)
        {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageName = imageUrl.substring(lastSlashIndex + 1);
            String imageDir = Environment.getExternalStorageDirectory().getPath() + "/PhotoWallFalls/";
            File file = new File(imageDir);
            if (!file.exists())
            {
                // 创建目录
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;
            return imagePath;
        }
    }

	@Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }
    
    @Override
    public boolean canPullUp()
    {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

	/**
	 * @return the mBlogScollView
	 */
	public MyMicroBlogScrollView getmBlogScollView() {
		return mBlogScollView;
	}

	/**
	 * @param mBlogScollView the mBlogScollView to set
	 */
	public void setmBlogScollView(MyMicroBlogScrollView mBlogScollView) {
		this.mBlogScollView = mBlogScollView;
	}

	private void delMyBlogPhoto(String photoId,int artId){
		final int delId = artId;
		final String photoIdDel = photoId;
		HttpUtil.delete(Urls.tripStory_delete_url+photoId,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					ToastUtil.showToast(mActivity, "删除成功");
					//删除我的微游记中的对象
					articleList.remove(delId);
					imageViewList.remove(delId);
					firstColumn.removeAllViews();
					secondColumn.removeAllViews();
					//重新刷新页面
					page=0;
					loadMoreImages();
					onDelPhotoListener.delPhotoById(photoIdDel);
//					Intent it = new Intent();
//					it.setAction("com.bcinfo.tripaway.delMicro");
//					it.putExtra("photo", photoIdDel);
//					mActivity.sendBroadcast(it);
//					checkVisibility();
		            
		            // 此时需要更新微游记页面，删除指定对象
		            // 发送handler执行
		            
				}else if("00099".equals(response.optString("code"))){
					ToastUtil.showToast(mActivity, "用户未登录");
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "delete_myblog_responseString", responseString);
				LogUtil.i(TAG, "delete_myblog_statusCode", statusCode+"");
			}
		});
	}
    
	/**
	 * @return the onDelPhotoListener
	 */
	public OnDelPhotoListener getOnDelPhotoListener() {
		return onDelPhotoListener;
	}

	/**
	 * @param onDelPhotoListener the onDelPhotoListener to set
	 */
	public void setOnDelPhotoListener(OnDelPhotoListener onDelPhotoListener) {
		this.onDelPhotoListener = onDelPhotoListener;
	}

	public  interface OnDelPhotoListener{
		
		public void delPhotoById(String id);
	}
}
