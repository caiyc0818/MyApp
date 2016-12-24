package com.bcinfo.tripaway.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAdsPagerAdapter;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.db.PushFlashDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author hanweipeng
 * @date 2015-7-29 下午4:35:11
 */
@SuppressLint("ShowToast")
public class LoadingActivity extends BaseActivity implements OnClickListener {
	protected static final String TAG = "LoadingActivity";

	private ImageView loadImg;

	private TextView skipTxt;

	private ViewPager viewPager;

	private List<View> imageViews;

	List<PushResource> pushResourceList;

	private ArrayList<View> dots;

	private LinearLayout dotsLayout;

	private EdgeEffectCompat leftEdge;

	private EdgeEffectCompat rightEdge;

	private PoiSearch mPoiSearch = null;

	private RelativeLayout loading;
	
	private boolean isCheck1=false;
	
	private boolean isCheck2=false;

	private ImageView loadingLogo;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		statisticsTitle="闪屏页";
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		// 定义全屏参数
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// 获得当前窗体对象
		Window window = getWindow();
		// 设置当前窗体为全屏显示
		window.setFlags(flag, flag);
		setContentView(R.layout.loading_activity);
		// loadImg = (ImageView)findViewById(R.id.load_img);
		skipTxt = (TextView) findViewById(R.id.skip);
		skipTxt.setOnClickListener(this);
		// resource = PushFlashDB.getInstance().randomGetData();
		// if (resource != null &&
		// !StringUtils.isEmpty(resource.getTitleImage()))
		// {
		// loadImg.setOnClickListener(this);
		// ImageLoader.getInstance().displayImage(Urls.imgHost +
		// resource.getTitleImage(), loadImg);
		// }
		// handler.postDelayed(runnable, 2000);
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) findViewById(R.id.carousel_dots);
		loading = (RelativeLayout) findViewById(R.id.loading);
		loadingLogo = (ImageView) findViewById(R.id.loading_logo);
		dotsLayout.setVisibility(View.GONE);
		skipTxt.setVisibility(View.GONE);
		viewPager.setVisibility(View.GONE);
		 ValueAnimator animator3 = ObjectAnimator.ofFloat(loadingLogo, "alpha", 0, 1);// 淡入效果
		animator3.setDuration(3000);
		animator3.setInterpolator(new AccelerateInterpolator());
		animator3.setTarget(loadingLogo);
		animator3.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
//				if(isCheck2){
				loading.setVisibility(View.GONE);
				dotsLayout.setVisibility(View.VISIBLE);
				skipTxt.setVisibility(View.VISIBLE);
				viewPager.setVisibility(View.VISIBLE);
//				}
//				isCheck1=true;
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		animator3.start();
		getPushFlash();
	}

	Handler handler = new Handler();

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
			startActivity(mainIntent);
			finish();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.load_img:
			if (pushResourceList == null || pushResourceList.size() == 0)
				return;
			handler.removeCallbacks(runnable);
			finish();
			int i = (Integer) v.getTag();

			PushResource resource = pushResourceList.get(i);

			if (resource.getObjectType().equals("product")) {
				ProductNewInfo productNewInfo = (ProductNewInfo) resource.getObject();
				String productType = productNewInfo.getProductType();
				String serviceCodes = productNewInfo.getServices();
				if (productType.equals("team")) {
					// GrouponProductNewDetailActivity
					Intent intent = new Intent(LoadingActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", resource.getObjectId());
					intent.putExtra("productTitle", productNewInfo.getTitle());
					intent.putExtra("formLoading", true);
					startActivity(intent);
					activityAnimationOpen();
				} else if (productType.equals("base") || productType.equals("customization")) {
					Intent intent = new Intent(LoadingActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productTitle", productNewInfo.getTitle());
					intent.putExtra("productId", resource.getObjectId());
					intent.putExtra("formLoading", true);
					startActivity(intent);
					activityAnimationOpen();
				} else if (productType.equals("single")) {
					if (serviceCodes.equals("traffic")) {
						Intent intent = new Intent(LoadingActivity.this, CarProductDetailActivity.class);
						intent.putExtra("productId", resource.getObjectId());
						intent.putExtra("formLoading", true);
						startActivity(intent);
						activityAnimationOpen();
					} else if (serviceCodes.equals("stay")) {
						Intent intent = new Intent(LoadingActivity.this, HouseProductDetailActivity.class);
						intent.putExtra("productId", resource.getObjectId());
						intent.putExtra("formLoading", true);
						startActivity(intent);
						activityAnimationOpen();
					}
				}
			} else if (resource.getObjectType().equals("column") || resource.getObjectType().equals("topic")) {
				TopicInfo topicInfo = (TopicInfo) resource.getObject();
				String topicId = resource.getObjectId();
				Intent intentForTopicProductList = new Intent(this, ThemeDetailActivity.class);
				intentForTopicProductList.putExtra("themeId", topicId);
				intentForTopicProductList.putExtra("themeName", topicInfo.getTitle());
				intentForTopicProductList.putExtra("description", topicInfo.getDescription());
				;
				intentForTopicProductList.putExtra("formLoading", true);
				startActivity(intentForTopicProductList);
				activityAnimationOpen();
			} else if (resource.getObjectType().equals("destination")) {
				Intent intent = new Intent(LoadingActivity.this, LocationCountryDetailActivity.class);
				intent.putExtra("destId", resource.getObjectId());
				intent.putExtra("formLoading", true);
				startActivity(intent);
				activityAnimationOpen();
			} else if (resource.getObjectType().equals("link")) {
				Intent intent = new Intent(LoadingActivity.this, ContentActivity.class);
				intent.putExtra("title", "专题活动");
				intent.putExtra("path", resource.getObject().toString());
				intent.putExtra("formLoading", true);
				intent.putExtra("resTitle", resource.getResTitle());
				intent.putExtra("desc",resource.getDescription());
				intent.putExtra("titleImage", resource.getTitleImage());
				startActivity(intent);
				activityAnimationOpen();
			}
			break;
		case R.id.skip:
			handler.removeCallbacks(runnable);
			Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
			startActivity(mainIntent);
			finish();
			break;
		default:
			break;
		}
	}

	private void getPushFlash() {
		RequestParams params = new RequestParams();
		params.put("pagesize", 10);
		params.put("pagenum", 1);
		params.put("mcode", "app.flash");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			// HttpUtil.get(Urls.push_flash_url, new JsonHttpResponseHandler()
			// {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "getPushCarousel", throwable.getMessage());

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				LogUtil.i(TAG, "getPushCarousel", throwable.getMessage());
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "闪屏接口=", response.toString());
				String code = response.optString("code");
				if (code.equals("00000")) {
					JSONArray dataArray = response.optJSONArray("data");
					pushResourceList = new ArrayList<PushResource>();
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataJson = dataArray.optJSONObject(i);
							PushResource pushResource = new PushResource();
							pushResource.setId(dataJson.optString("id"));
							pushResource.setDescription(dataJson.optString("description"));
							pushResource.setResTitle(dataJson.optString("resTitle"));
							pushResource.setSubTitle(dataJson.optString("subTitle"));
							pushResource.setTitleImage(dataJson.optString("titleImage"));
							pushResource.setObjectType(dataJson.optString("objectType"));
							pushResource.setObjectId(dataJson.optString("objectId"));
							pushResource.setObject(dataJson.optString("object"));
							JSONObject jSONObject = dataJson.optJSONObject("object");
							if (jSONObject != null && jSONObject.length() > 0) {
								if ("product".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getProductNewInfo(jSONObject));
								} else if ("topic".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getTopicInfo(jSONObject));
								} else if ("user".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getUserInfo(jSONObject));
								} else if ("destination".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getDestinationInfo(jSONObject));
								} else if ("softtext".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
								} else if ("story".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
								} else if ("subject".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(JsonUtil.getSubjectInfo(jSONObject));
								} else if ("link".equals(dataJson.optString("objectType"))) {
									pushResource.setObject(jSONObject.toString());
								}
							}
							pushResourceList.add(pushResource);
						}

					}
					addCarousel(pushResourceList);
				}

			}
		});
	}

	/**
	 * 当前图片的索引号
	 */
	private int currentItem = 0;

	private int count;

	/**
	 * 换行切换任务
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				viewPager.setCurrentItem(currentItem);
				// 通过Handler切换图片
				handler.postDelayed(this, 3000);
			}
		}
	}

	private void addCarousel(final List<PushResource> pushResourceList) {
		// count = tempList.size();
		imageViews = new ArrayList<View>();
		if (pushResourceList == null || pushResourceList.size() == 0) {
			View imageView = LayoutInflater.from(this).inflate(R.layout.loading_activity_item, null);
			imageViews.add(imageView);
//			handler.postDelayed(runnable, 2000);
		} else {
			count = pushResourceList.size();
			dots = new ArrayList<View>();
			if (dotsLayout != null) {
				dotsLayout.removeAllViews();
			}
			for (int i = 0; i < count; i++) {
				PushResource resource = pushResourceList.get(i);
				View imageView = LayoutInflater.from(this).inflate(R.layout.loading_activity_item, null);
				ImageView loadImg = (ImageView) imageView.findViewById(R.id.load_img);
				loadImg.setTag(i);

				if (resource != null && !StringUtils.isEmpty(resource.getTitleImage())) {
					loadImg.setOnClickListener(this);
					ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), loadImg,
							new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri, View view) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
									// TODO Auto-generated method stub
//									if(isCheck1){
//										dotsLayout.setVisibility(View.VISIBLE);
//										skipTxt.setVisibility(View.VISIBLE);
//										viewPager.setVisibility(View.VISIBLE);
//										loading.setVisibility(View.GONE);
//										}
//										isCheck2=true;
//									final ValueAnimator animator3 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);// 淡入效果
//									animator3.setDuration(1000);
//									animator3.setInterpolator(new AccelerateInterpolator());
//									animator3.setTarget(view);
//									animator3.start();
									
								}

								@Override
								public void onLoadingCancelled(String imageUri, View view) {
									// TODO Auto-generated method stub

								}
							});
				}
				// TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
				ImageView dotsView = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				if (i != count - 1)
					params.rightMargin = 30;
				dotsView.setLayoutParams(params);
				dotsView.setBackgroundResource(R.drawable.shape_gray_oval);
				if (0 == i) {
					dotsView.setBackgroundResource(R.drawable.shape_white_oval);
				}
				dotsLayout.addView(dotsView);
				dots.add(dotsView);
				imageViews.add(imageView);
			}
		}

		// TODO 设置填充ViewPager页面的适配器********当滚动主题有时再放开下面代码******
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				if (arg1 == pushResourceList.size()) {

					return;
				}
				((ViewPager) arg0).removeView(imageViews.get(arg1));
			}

			@Override
			public void finishUpdate(View arg0) {
			}

			@Override
			public int getCount() {
				return imageViews.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == (arg1);
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
			}

			@Override
			public Object instantiateItem(View arg0, final int arg1) {
				// mListViews.get(arg1).setBackgroundResource(resId[arg1]);
				if (arg1 == pushResourceList.size()) {

					return null;
				}
				((ViewPager) arg0).addView(imageViews.get(arg1), 0);
				return imageViews.get(arg1);
			}

		});
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		// 设置一个监听器，当ViewPager中的页面改变时调用
		// viewPager.setOnPageChangeListener(new MyPageChangeListener());
		// if (count > 1) {
		// handler.postDelayed(new ScrollTask(), 3000);
		//
		// }
		try {
			Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
			Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
			if (leftEdgeField != null && rightEdgeField != null) {
				leftEdgeField.setAccessible(true);
				rightEdgeField.setAccessible(true);
				leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
				rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			// if (position == pushResourceList.size() ) {
			// Intent mainIntent = new Intent(LoadingActivity.this,
			// MainActivity.class);
			// startActivity(mainIntent);
			// activityAnimationOpen();
			// finish();
			// return;
			// }

			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.shape_gray_oval);
			dots.get(position).setBackgroundResource(R.drawable.shape_white_oval);
			oldPosition = position;

		}

		public void onPageScrollStateChanged(int arg0) {
			if (rightEdge != null && !rightEdge.isFinished()) {// 到了最后一张并且还继续拖动，出现蓝色限制边条了
				Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
//				Intent mainIntent = new Intent(LoadingActivity.this, SquarePicPublishedActivity.class);
//				Intent mainIntent = new Intent(LoadingActivity.this, SquareSerialPublishedActivity.class);
				startActivity(mainIntent);
				activityAnimationOpen();
				finish();
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
}
