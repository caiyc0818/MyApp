package com.bcinfo.tripaway.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UploadPicturesRecordDB;
import com.bcinfo.tripaway.getui.util.TimeUtil;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.ScrollView.MyMicroBlogScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.bcinfo.tripaway.view.refreshandload.PullableScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的游记
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月15日 上午10:42:25
 */
@SuppressWarnings("deprecation")
public class CustomerHomePageActivity extends BaseActivity implements OnClickListener, PersonalScrollViewListener {
	/**
	 * 微游记-显示微游记数据控件
	 */
	private MyMicroBlogScrollView mBlogScrollView;

	// 自定义上拉加载相对布局
	// private PullToRefreshLayout pullToRefreshLayout;
	// 自定义滑动
	@SuppressWarnings("unused")
	private PullableScrollView pullableScrollView;

	private boolean isPullRefresh = false;
	private int pageNum = 1;
	private int pagesize = 10;
	private boolean isLoadMore = false;
	private boolean isLoading = false;

	private int isRefresh = 0;
	private RelativeLayout layout_product_detail_title;

	/**
	 * 
	 * 存放微游记实体的list集合
	 */
	private ArrayList<TripArticle> articleList = new ArrayList<TripArticle>();

	private ArrayList<ArrayList<String>> blogPhotoList;

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

	private ArrayList<String> photoIdList = new ArrayList<String>();

	private String userId;

	private LinearLayout mBackBtn;
	/**
	 * 向上出现的抽屉
	 */
	@SuppressWarnings("deprecation")
	/**
	 * boolean 标记位 添加照片控件是否已经旋转45度 true表示已经旋转45度 ；false表示没有旋转 "+"
	 */
	private boolean isRotate;

	public void setBlogPhotoList(ArrayList<ArrayList<String>> blogPhotoList) {
		this.blogPhotoList = blogPhotoList;
	}

	public MyMicroBlogScrollView getmBlogScrollView() {
		return mBlogScrollView;
	}

	public void setmBlogScrollView(MyMicroBlogScrollView mBlogScrollView) {
		this.mBlogScrollView = mBlogScrollView;
	}

	private final String IMAGE_UNSPECIFIED = "image/*";

	private UploadPicturesRecordDB uploadPicturesRecord;

	private UserInfo userInfo;

	private RoundImageView photoImage;

	private ImageView backgroundImage;

	private TextView mPhotoName;

	private TextView mIntroduce;

	private ProductDetailScrollView mProductScrollView;
	private ImageView mProductHeadImg;

	private LinearLayout firstLayout;

	private LinearLayout secondLayout;

	private int size = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
		userInfo = getIntent().getParcelableExtra("userInfo");
		if (userInfo != null) {
			userId = userInfo.getUserId();
		}
		// 判断手机是否有sd卡
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 创建存放拍照照片的目录
			imageFile = new File(Environment.getExternalStorageDirectory() + "/myImage");
			imageFile.mkdirs();// 创建目录
		} else {
			Toast.makeText(this, "无sd卡", 1000).show();
		}
		setContentView(R.layout.customer_homepage);
		// setSecondTitle("我的游记");
		photoImage = (RoundImageView) findViewById(R.id.club_author_photo);
		backgroundImage = (ImageView) findViewById(R.id.page_background);
		mPhotoName = (TextView) findViewById(R.id.club_name);
		mIntroduce = (TextView) findViewById(R.id.person_intro);

		if (!StringUtils.isEmpty(userInfo.getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(), photoImage);
		}

		if (!StringUtils.isEmpty(userInfo.getBackground())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getBackground(), backgroundImage);
		}

		mPhotoName.setText(userInfo.getNickname());
		if (StringUtils.isEmpty(userInfo.getIntroduction()))
			mIntroduce.setVisibility(View.GONE);
		else
			mIntroduce.setText(userInfo.getIntroduction());

		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_product_detail_title.setFocusable(true);
		layout_product_detail_title.setFocusableInTouchMode(true);
		layout_product_detail_title.requestFocus();
		firstLayout = (LinearLayout) findViewById(R.id.first_column);
		secondLayout = (LinearLayout) findViewById(R.id.second_column);

		// fullScreenLayout = (LinearLayout)findViewById(R.id.topPhotoLayout);
		mBackBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		mBackBtn.setOnClickListener(this);
		// pullToRefreshLayout =
		// (PullToRefreshLayout)findViewById(R.id.allThemes_container);
		// pullToRefreshLayout.setOnRefreshListener(new MyListener());
		// fullScreenLayout.setOnClickListener(this);
		// drawle = (SlidingDrawer)findViewById(R.id.upSlidingDrawer);
		uploadPicturesRecord = new UploadPicturesRecordDB();// 上传数据记录的数据库
		// FooterBar = (ProgressBar)findViewById(R.id.footerBar);
		// TextOfFooterBar = (TextView)findViewById(R.id.text_of_footerBar);
		testMyStorySelfUrl(userId);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("close");
		intentFilter.addAction("com.bcinfo.view.delMyBlog");
		intentFilter.addAction("com.bcinfo.refreshCommentsCount");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	/*
	 * test 我的旅行故事 (微游记列表接口)
	 */
	private void testMyStorySelfUrl(String userId) {
		if (isLoading) {
			return;
		}
		isLoading = true;
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pagesize);
		params.put("userId", userId);
		HttpUtil.get(Urls.tripStory_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);

				// System.out.println(statusCode);
				System.out.println(response.toString());
				// System.out.println(statusCode);
				// System.out.println(response);
				getTripStoryList(response);
				isLoading = false;

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				// System.out.println(statusCode);
				// System.out.println(responseString);
				// FooterBar.setVisibility(View.GONE);
				if (pageNum != 1) {
					pageNum--;
				}
				isLoading = false;
				// TextOfFooterBar.setText("加载失败!");
			}
		});
		System.out.println("******加载完毕******");
	}

	/*
	 * 解析从服务端获取的微游记参数
	 */
	private void getTripStoryList(JSONObject response) {
		// ArrayList<TripArticle> list = new ArrayList<>();
		if ("00000".equals(response.optString("code"))) {
			if (isLoadMore) {
				// 上拉返回的结束加载更多布局
				// pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
			if (isPullRefresh) {
				// 下拉刷新返回的
				// pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}

			JSONObject dataObj = response.optJSONObject("data");
			JSONArray dataArray = dataObj.optJSONArray("tripstory");
			if (dataArray != null && dataArray.length() != 0) {
				for (int i = 0; i < dataArray.length(); i++) {
					JSONObject tripStoryObj = dataArray.optJSONObject(i);
					if (tripStoryObj != null) {
						TripArticle articleObj = new TripArticle();
						articleObj.setLocation(tripStoryObj.optString("location"));
						articleObj.setPublishTime(tripStoryObj.optString("publishTime"));
						articleObj.setLocation(tripStoryObj.optString("location"));
						articleObj.setIsLike(tripStoryObj.optString("isLike"));
						articleObj.setDescription(tripStoryObj.optString("description"));
						articleObj.setComments(tripStoryObj.optString("comments"));
						articleObj.setPhotoId(tripStoryObj.optString("photoId"));
						JSONArray picArray = tripStoryObj.optJSONArray("pictures");
						if (picArray != null) {
							for (int j = 0; j < picArray.length(); j++) {
								JSONObject picObj = picArray.optJSONObject(j);
								ImageInfo picRes = new ImageInfo();
								picRes.setDesc(picObj.optString("desc"));
								picRes.setUrl(picObj.optString("url"));
								articleObj.getPictureList().add(picRes);
							}
						}
						JSONObject userObj = tripStoryObj.optJSONObject("publisher");

						if (userObj != null) {
							articleObj.getPublisher().setGender(userObj.optString("sex"));
							articleObj.getPublisher().setStatus(userObj.optString("status"));
							articleObj.getPublisher().setEmail(userObj.optString("email"));
							articleObj.getPublisher().setNickname(userObj.optString("nickName"));
							articleObj.getPublisher().setUserId(userObj.optString("userId"));
							articleObj.getPublisher().setAvatar(userObj.optString("avatar"));
							articleObj.getPublisher().setIntroduction(userObj.optString("introduction"));
							articleObj.getPublisher().setMobile(userObj.optString("mobile"));
							JSONArray tagsArray = userObj.optJSONArray("tags");
							if (tagsArray != null) {
								for (int k = 0; k < tagsArray.length(); k++) {
									articleObj.getPublisher().getTags().add(tagsArray.optString(k));

								}
							}
						}
						articleList.add(articleObj);
					}

				}
				addPhotoView(articleList);
				pageNum++;
			}
		}
	}

	private void addPhotoView(ArrayList<TripArticle> list) {

		LayoutInflater layout = this.getLayoutInflater();
		if (size % 2 == 0) {
			// firstcolumn开始
			for (int i = 0; i < list.size(); i++) {
				final TripArticle artice = list.get(i);
				View view = layout.inflate(R.layout.item_blog1, null);
				ImageView img = (ImageView) view.findViewById(R.id.img_item_trip_blog);
				if (artice.getPictureList().size() > 0
						&& !StringUtils.isEmpty(artice.getPictureList().get(0).getUrl())) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + artice.getPictureList().get(0).getUrl(), img);
				}
				TextView txt = (TextView) view.findViewById(R.id.describe_trip_blog);
				// 描述格式化
				txt.setText("");
				List<RichText> richTexts = StringUtils.xmlToRichText(artice.getDescription());
				StringUtils.initRichTextView1(txt, richTexts);
				ImageView img1 = (ImageView) view.findViewById(R.id.map_img);
				TextView txt3 = (TextView) view.findViewById(R.id.location_trip_blog);
				if (!StringUtils.isEmpty(artice.getLocation()) && !artice.getLocation().contains("定位失败")) {
					txt3.setText(artice.getLocation());
					img1.setVisibility(View.VISIBLE);
				} else {
					txt3.setText("");
					img1.setVisibility(View.GONE);
				}
				RoundImageView photo = (RoundImageView) view.findViewById(R.id.master_icon_trip_blog);
				if (!StringUtils.isEmpty(artice.getPublisher().getAvatar())) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + artice.getPublisher().getAvatar(), photo,
							AppConfig.options(R.drawable.user_defult_photo));
				}
				TextView txt1 = (TextView) view.findViewById(R.id.name_trip_bloger);
				txt1.setText(artice.getPublisher().getNickname());
				TextView txt2 = (TextView) view.findViewById(R.id.time_trip_blog);
				txt2.setText(TimeUtil.getDayTime1(DateUtil.formatStrtoLong(artice.getPublishTime())));
				img.setTag(artice);
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent blogIntentForDetail = new Intent(CustomerHomePageActivity.this,
								BlogDetailActivity.class);
						blogIntentForDetail.putExtra("blog_article_author_avatar",
								((TripArticle) v.getTag()).getPublisher().getAvatar());
						blogIntentForDetail.putExtra("blog_article_comments", ((TripArticle) v.getTag()).getComments());
						blogIntentForDetail.putExtra("blog_article_id", ((TripArticle) v.getTag()).getPhotoId());
						blogIntentForDetail.putExtra("blog_article_author_id",
								((TripArticle) v.getTag()).getPublisher().getUserId()); // 获取游记发布者的id
						blogIntentForDetail.putExtra("blog_article_pic_list",
								((TripArticle) v.getTag()).getPictureList());
						blogIntentForDetail.putExtra("blog_article_current_date",
								((TripArticle) v.getTag()).getPublishTime());
						blogIntentForDetail.putExtra("blog_article_author_nickName",
								((TripArticle) v.getTag()).getPublisher().getNickname());
						blogIntentForDetail.putExtra("blog_article_description",
								((TripArticle) v.getTag()).getDescription());
						blogIntentForDetail.putExtra("blog_image_index", 0);
						startActivity(blogIntentForDetail);
						overridePendingTransition(R.anim.activity_new, R.anim.activity_out);

					}
				});
				if (i % 2 == 0) {
					firstLayout.addView(view);
				} else {
					secondLayout.addView(view);
				}
			}
		} else {
			// secondcolumn开始
			for (int i = 0; i < list.size(); i++) {
				final TripArticle artice = list.get(i);
				View view = layout.inflate(R.layout.item_blog1, null);
				ImageView img = (ImageView) view.findViewById(R.id.img_item_trip_blog);
				if (artice.getPictureList().size() > 0
						&& !StringUtils.isEmpty(artice.getPictureList().get(0).getUrl())) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + artice.getPictureList().get(0).getUrl(), img);
				}
				TextView txt = (TextView) view.findViewById(R.id.describe_trip_blog);
				ImageView img1 = (ImageView) view.findViewById(R.id.map_img);
				TextView txt3 = (TextView) view.findViewById(R.id.location_trip_blog);
				if (!StringUtils.isEmpty(artice.getLocation()) && !artice.getLocation().contains("定位失败")) {
					txt3.setText(artice.getLocation());
					img1.setVisibility(View.VISIBLE);
				} else {
					txt3.setText("");
					img1.setVisibility(View.GONE);
				}
				txt.setText(artice.getDescription());
				RoundImageView photo = (RoundImageView) view.findViewById(R.id.master_icon_trip_blog);
				if (!StringUtils.isEmpty(artice.getPublisher().getAvatar())) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + artice.getPublisher().getAvatar(), photo,
							AppConfig.options(R.drawable.user_defult_photo));
				}
				TextView txt1 = (TextView) view.findViewById(R.id.name_trip_bloger);
				txt1.setText(artice.getPublisher().getNickname());
				TextView txt2 = (TextView) view.findViewById(R.id.time_trip_blog);
				txt2.setText(TimeUtil.getDayTime1(DateUtil.formatStrtoLong(artice.getPublishTime())));
				img.setTag(artice);
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent blogIntentForDetail = new Intent(CustomerHomePageActivity.this,
								BlogDetailActivity.class);
						blogIntentForDetail.putExtra("blog_article_author_avatar",
								((TripArticle) v.getTag()).getPublisher().getAvatar());
						blogIntentForDetail.putExtra("blog_article_comments", ((TripArticle) v.getTag()).getComments());
						blogIntentForDetail.putExtra("blog_article_id", ((TripArticle) v.getTag()).getPhotoId());
						blogIntentForDetail.putExtra("blog_article_author_id",
								((TripArticle) v.getTag()).getPublisher().getUserId()); // 获取游记发布者的id
						blogIntentForDetail.putExtra("blog_article_pic_list",
								((TripArticle) v.getTag()).getPictureList());
						blogIntentForDetail.putExtra("blog_article_current_date",
								((TripArticle) v.getTag()).getPublishTime());
						blogIntentForDetail.putExtra("blog_article_author_nickName",
								((TripArticle) v.getTag()).getPublisher().getNickname());
						blogIntentForDetail.putExtra("blog_article_description",
								((TripArticle) v.getTag()).getDescription());
						blogIntentForDetail.putExtra("blog_image_index", 0);
						startActivity(blogIntentForDetail);
						overridePendingTransition(R.anim.activity_new, R.anim.activity_out);

					}
				});
				if (i % 2 == 0) {
					secondLayout.addView(view);
				} else {
					firstLayout.addView(view);
				}
			}
		}
		size += list.size();
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.my_micro_delete:

			break;
		case R.id.layout_back_button:
			if (photoIdList.size() > 0) {
				Intent it = new Intent();
				it.setAction("com.bcinfo.delPhoto");
				sendBroadcast(it);
				finish();
			} else {
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Uri newuri = Uri.fromFile(file);
			uploadPicturesRecord.saveloadPicturesRecord(newuri.toString(), newuri.toString(), true);
			startPhotoZoom(newuri);

		} else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			if (data == null) {
				Toast.makeText(getBaseContext(), "无法加载此图片!", Toast.LENGTH_SHORT).show();
				return;
			}
			file = null;
			startPhotoZoom(data.getData());
		} else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
			// 获得拍照的数据
			Bundle bundle = data.getExtras();
			// 获得拍照的照片
			Bitmap currentBitMap = (Bitmap) bundle.get("data");
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
	public void startPhotoZoom(Uri uri) {

		if (file == null) {
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
	private void isRecordExist(Uri uri) {
		Bitmap selectedBitMap = null;
		String imageuri = uploadPicturesRecord.readimageuriRecord(uri.toString());
		if (imageuri == null) {
			return;
		}
		Uri myuri = Uri.parse(imageuri);
		try {
			selectedBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), myuri);// 显得到bitmap图片
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (selectedBitMap != null) {
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
			if ("com.bcinfo.view.delMyBlog".equals(action)) {
				articleList.clear();
				isRefresh = 0;
				isPullRefresh = true;
				pageNum = 1;
				photoIdList.add("");
				firstLayout.removeAllViews();
				secondLayout.removeAllViews();
				testMyStorySelfUrl(userId);
			} else if ("close".equals(action)) {
				finish();
			}
			if ("com.bcinfo.refreshCommentsCount".equals(action)) {
				String count = intent.getStringExtra("count");
				String microId = intent.getStringExtra("microId");
				if (count != null && microId != null) {
					for (TripArticle article : articleList) {
						if (article.getPhotoId().equals(microId)) {
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
	class MyListener implements OnRefreshListener {

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			isRefresh = 1;
			if (articleList.size() == 0) {
				return;
			} else if (articleList.size() % 10 == 0) {
				pageNum = pageNum + 1;
				// pagesize = pagesize + 10;
				testMyStorySelfUrl(userId);

			} else {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
				Toast.makeText(CustomerHomePageActivity.this, "加载完毕", Toast.LENGTH_LONG).show();

			}
		}

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			// TODO Auto-generated method stub
			// mBlogScrollView.refreshDrawableState();
			articleList.clear();
			isRefresh = 0;
			isPullRefresh = true;
			pageNum = 1;
			testMyStorySelfUrl(userId);
		}

	}

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		if (view != null && view == mProductScrollView) {

			int alpha = positionY / 3;
			if (positionY < 50 || positionY == 50) {
				// detail_product_title.setAlpha(1f);
			} else {
				// detail_product_title.setAlpha(0);
			}

			if (positionY > 220)

			{
				alpha = 255;
			}
			layout_product_detail_title.getBackground().setAlpha(alpha);
			// product_title.setAlpha(alpha / 255f);

			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();
			int scollHeight = mProductScrollView.getHeight();
			if (height <= scollY + scollHeight) {
				// 底部
				testMyStorySelfUrl(userId);
			} else if (scollY == 0) {
				// 顶部
			}
		} else {
			return;
		}
	}

	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {
			// TODO Auto-generated method stub
			// LogUtil.i(TAG, "mPullListener", "height=" + height);
			float alpha = height * 3 - 200;
			if (alpha > 255) {
				alpha = 255;
			}
			if (alpha < 50) {
				alpha = 0;
			}
			// layout_service_list.setAlpha(alpha / 255f);
			// product_introduce.setAlpha(alpha / 255f);
		}
	};
}
