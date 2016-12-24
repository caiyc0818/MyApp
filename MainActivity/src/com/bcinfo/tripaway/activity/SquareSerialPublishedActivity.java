package com.bcinfo.tripaway.activity;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.bool;
import android.R.integer;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.a.h;
import com.baidu.mobstat.s;
import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.ui.PhotoPreviewActivity;
import com.bcinfo.photoselector.ui.PhotoSelectorActivity;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.SquarePicPublishedActivity.MyLocationListener;
import com.bcinfo.tripaway.adapter.GdAdapter;
import com.bcinfo.tripaway.adapter.NewDayListToListAdapter;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.TextStyleUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil.UploadFinishListener;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.bcinfo.tripaway.view.span.TextClickSpan;
import com.bcinfo.tripaway.view.text.LinkArrowKeyMovementMethod;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

public class SquareSerialPublishedActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = "SquareSerialPublishedActivity";
	private ViewPager viewPager;
	private LinearLayout dotsLayout;
	private List<View> imageViews;
	private ArrayList<View> dots;

	private int count;
	private int currentItem = 0;

	private List<String> topicStrs;

	private int gap;

	private List<List<TopicOrTag>> strsList = new ArrayList<List<TopicOrTag>>();
	private LinearLayout itemPic;
	private LinearLayout itemTopic;
	private LinearLayout itemMusic;
	private LinearLayout itemMovie;
	private LinearLayout itemTag;
	private LinearLayout itemBook;
	private LinearLayout itemSerial;
	private LinearLayout squareBottom;

	private final int ADD_CONTENT_BIG_IMAGE_CODE = 1001;
	private final int ADD_CONTENT_COVER_IMAGE_CODE = 1002;
	private final int ADD_CONTENT_LITTLE_IMAGE_CODE = 1003;
	private final int MAX_PHOTOS = 50;
	private EditText editText;

	private int contentW;
	private TextView addCover;
	private RelativeLayout picLayout;
	private TextView modify;
	private ImageView pic;

	private int currentBottomItem = 0;

	private List<RichText> richTexts = new ArrayList<RichText>();
	private TextView blogPublish;

	private List<TopicOrTag> individualTopics = new ArrayList<TopicOrTag>();

	private List<TopicOrTag> hotTopics = new ArrayList<TopicOrTag>();

	private List<TopicOrTag> tags = new ArrayList<TopicOrTag>();

	private String access = "all";
	private TextView accessText;

	private final String[] accessState = { "all", "private" };
	private final String[] accessString = { "公开", "自己可见" };
	private int currentAccess = 0;
	private TextView locationTv;
	private LocationClient mLocationClient;

	private BDLocationListener myListener = new MyLocationListener();

	private String locationStr = "正在定位中";

	private boolean isShowLocation = true;

	private int tagGap;
	private FlowLayout tagsFlow;

	private String cover;
	private EditText titleTv;

	private ArrayList<TopicOrTag> selecTags = new ArrayList<TopicOrTag>();

	public int clickSpanStart = -1;

	public int clickSpanEnd = -1;

	private UploadPicUtil uploadPicUtil;
	private TextView blogNavBack;

	private String from;
	private String seriesId;
	private View line;
	private LinearLayout bottomTab;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};
	private ProgressDialog dialog;
	private boolean isCancel=false;
	private ScrollView mScrollView;
	private String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_serial_published);
		statisticsTitle = "发布连载";
		tag = getIntent().getStringExtra("tag");
		Intent it = getIntent();
		from = it.getStringExtra("from");
		if (from == null) {
			from = "default";
		}
		seriesId = it.getStringExtra("seriesId");
		AppManager.getAppManager().addActivity(this);
		uploadPicUtil = new UploadPicUtil(uploadFinishListener);
		initViews();

		gap = DensityUtil.dip2px(this, 13);
		tagGap = DensityUtil.dip2px(this, 8);
		querySquareTags();
		querySquareTopics();
		initLocation();

	}

	private void initViews() {
		blogNavBack = (TextView) findViewById(R.id.blog_navBack);
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) findViewById(R.id.carousel_dots);
		itemPic = (LinearLayout) findViewById(R.id.item1);
		itemPic.setVisibility(View.VISIBLE);
		itemTopic = (LinearLayout) findViewById(R.id.item2);
		itemTag = (LinearLayout) findViewById(R.id.item3);
		itemMusic = (LinearLayout) findViewById(R.id.item4);
		itemBook = (LinearLayout) findViewById(R.id.item5);
		itemMovie = (LinearLayout) findViewById(R.id.item6);
		itemSerial = (LinearLayout) findViewById(R.id.item7);
		addCover = (TextView) findViewById(R.id.add_cover);
		blogPublish = (TextView) findViewById(R.id.blog_publish);
		blogPublish.setText("发布");
		accessText = (TextView) findViewById(R.id.access_text);
		locationTv = (TextView) findViewById(R.id.locationTv);
		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		blogNavBack.setOnClickListener(this);
		blogPublish.setOnClickListener(this);
		itemSerial.setVisibility(View.GONE);
		itemPic.setOnClickListener(this);
		itemTopic.setOnClickListener(this);
		itemTag.setOnClickListener(this);
		itemMusic.setOnClickListener(this);
		itemBook.setOnClickListener(this);
		itemMovie.setOnClickListener(this);
		itemSerial.setOnClickListener(this);
		addCover.setOnClickListener(this);
		accessText.setOnClickListener(this);
		locationTv.setOnClickListener(this);
		pic = (ImageView) findViewById(R.id.pic);
		modify = (TextView) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		squareBottom = (LinearLayout) findViewById(R.id.square_bottom_linear);

		editText = (EditText) findViewById(R.id.trip_edit_photoDescript);
		titleTv = (EditText) findViewById(R.id.title_tv);
		bottomTab = (LinearLayout) findViewById(R.id.bottom_tab);
		editText.setTypeface(TripawayApplication.normalTf);
		editText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				squareBottom.setVisibility(View.GONE);
				return false;
			}
		});
		editText.setMovementMethod(LinkArrowKeyMovementMethod.getInstance());
		contentW = screenWidth - 2 * DensityUtil.dip2px(this, 10);
		picLayout = (RelativeLayout) findViewById(R.id.pic_layout);

		editText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
					StringUtils.delEditText(editText);
				}
				if (keyCode == KeyEvent.KEYCODE_DEL)
					return true;
				return false;

			}

		});
		line = findViewById(R.id.line);
		if (from.equals("preview") && !StringUtils.isEmpty(seriesId)) {
			((View) addCover.getParent()).setVisibility(View.GONE);
			titleTv.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			blogPublish.setText("保存");
		} else if (from.equals("pic")) {
			editText.setVisibility(View.GONE);
			blogPublish.setText("确定");
			bottomTab.setVisibility(View.GONE);
		}
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.microblog_title);
		titleLayout.setAlpha(1);
		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		mScrollView = (ScrollView) findViewById(R.id.trip_blog_container1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ADD_CONTENT_LITTLE_IMAGE_CODE:
				String uri = data.getExtras().getString("uri");
				int type = data.getExtras().getInt("type");
				boolean isEdit = data.getExtras().getBoolean("isEdit", false);
				if (isEdit && clickSpanStart != -1 && clickSpanEnd != -1 && clickSpanEnd > 0) {
					Editable editable = editText.getEditableText();
					TextClickSpan[] textClickSpans = editable.getSpans(clickSpanStart, clickSpanEnd,
							TextClickSpan.class);
					EmojiconSpan[] emojiconSpans = editable.getSpans(clickSpanStart, clickSpanEnd, EmojiconSpan.class);
					if (textClickSpans != null && textClickSpans.length == 1) {
						editable.removeSpan(textClickSpans[0]);
					}
					if (emojiconSpans != null && emojiconSpans.length == 1) {
						editable.removeSpan(emojiconSpans[0]);
					} else if (emojiconSpans != null && emojiconSpans.length == 2) {
						editable.removeSpan(emojiconSpans[0]);
						editable.removeSpan(emojiconSpans[1]);
					}
					editable.delete(clickSpanStart, clickSpanEnd);
					clickSpanStart = -1;
					clickSpanEnd = -1;
				}

				if (type == 1)
					TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_music);
				else if (type == 2)
					TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_book);
				else if (type == 3)
					TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_movie);
				float w = DensityUtil.dip2px(this, 20);
				try {
					if (!StringUtils.isEmpty(uri)) {
						Drawable db = BitmapUtil.scaleImage3(Uri.parse("file://" + uri), this, w, w);
						TextStyleUtil.addImageSpan(editText, " ", db, uri);
					}
					TextStyleUtil.addClickableSpan(editText, data.getExtras());
					editText.setSelection(editText.getText().length());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				break;
			case ADD_CONTENT_BIG_IMAGE_CODE:
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				if (photos.size() > 0) {
					PhotoModel info = photos.get(0);
					uri = info.getOriginalPath();
					if (!StringUtils.isEmpty(uri)) {
						try {
							Drawable db = new BitmapDrawable(
									BitmapUtil.scaleImage4(Uri.parse("file://" + uri), this, contentW));
							editText.append("\n");
							TextStyleUtil.addImageSpan(editText, " ", db, contentW, uri);
							editText.append("\n");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			case ADD_CONTENT_COVER_IMAGE_CODE:
				photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				if (photos.size() > 0) {
					addCover.setVisibility(View.GONE);
					picLayout.setVisibility(View.VISIBLE);
					PhotoModel info = photos.get(0);
					cover = info.getOriginalPath();
					ImageLoader.getInstance().displayImage("file://" + info.getOriginalPath(), pic);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.blog_navBack:
			finish();
			activityAnimationClose();
			break;
		case R.id.item1:// 图片
			CommonUtils.launchPhotoSelectorActivity(this, ADD_CONTENT_BIG_IMAGE_CODE);
			break;
		case R.id.item2:// 话题
			if (currentBottomItem != 2) {
				getFlowText(individualTopics, false);
				addCarousel(false);
				((TextView) itemTopic.getChildAt(1)).setTextColor(getResources().getColor(R.color.title_bg));
				((TextView) itemTag.getChildAt(1)).setTextColor(getResources().getColor(R.color.dark_gray));
				currentBottomItem = 2;
				squareBottom.setVisibility(View.VISIBLE);
			} else {
				if (squareBottom.getVisibility() == View.VISIBLE)
					squareBottom.setVisibility(View.GONE);
				else
					squareBottom.setVisibility(View.VISIBLE);
			}
			hideInputManager(this);
			break;
		case R.id.item3:// 标签
			if (currentBottomItem != 3) {
				getFlowText(tags, true);
				addCarousel(true);
				((TextView) itemTag.getChildAt(1)).setTextColor(getResources().getColor(R.color.title_bg));
				((TextView) itemTopic.getChildAt(1)).setTextColor(getResources().getColor(R.color.dark_gray));
				currentBottomItem = 3;
				squareBottom.setVisibility(View.VISIBLE);
			} else {
				if (squareBottom.getVisibility() == View.VISIBLE)
					squareBottom.setVisibility(View.GONE);
				else
					squareBottom.setVisibility(View.VISIBLE);
			}
			hideInputManager(this);
			break;
		case R.id.item4:// 音乐
			Intent musicIntent = new Intent(this, SquareMusicOrBookOrMusicAddActivity.class);
			musicIntent.putExtra("type", 1);
			startActivityForResult(musicIntent, ADD_CONTENT_LITTLE_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item5:// 书籍
			Intent bookIntent = new Intent(this, SquareMusicOrBookOrMusicAddActivity.class);
			bookIntent.putExtra("type", 2);
			startActivityForResult(bookIntent, ADD_CONTENT_LITTLE_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item6:// 电影
			Intent movieIntent = new Intent(this, SquareMusicOrBookOrMusicAddActivity.class);
			movieIntent.putExtra("type", 3);
			startActivityForResult(movieIntent, ADD_CONTENT_LITTLE_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item7:// 连载

			break;
		case R.id.add_cover:
		case R.id.modify:
			CommonUtils.launchPhotoSelectorActivity(this, ADD_CONTENT_COVER_IMAGE_CODE);
			break;
		case R.id.blog_publish:
			if (!from.equals("preview")) {
				if (StringUtils.isEmpty(titleTv.getText().toString())) {
					ToastUtil.showToast(getApplicationContext(), "发布标题不能为空");
					return;
				}
				if (StringUtils.isEmpty(cover)) {
					ToastUtil.showToast(getApplicationContext(), "封面不能为空");
					return;
				}
				if (StringUtils.isEmpty(editText.getText().toString())) {
					ToastUtil.showToast(getApplicationContext(), "发布内容不能为空");
					return;
				}
			}
			dialog = ProgressDialog.show(this, null, "正在发布中，请稍后..");
			isCancel = false;
			if (from.equals("pic")) {
				uploadPic();
			} else {
				richTexts.clear();
				StringUtils.getRichText(editText, richTexts);
				if (richTexts.size() == 0) {
					ToastUtil.showToast(getApplicationContext(), "发布内容不能为空");
					return;
				}
				uploadPic();
			}
			break;
		case R.id.access_text:
			currentAccess = (currentAccess + 1) % accessState.length;
			access = accessState[currentAccess];
			accessText.setText(accessString[currentAccess]);
			break;
		case R.id.locationTv:
			isShowLocation = !isShowLocation;
			if (isShowLocation) {
				locationTv.setText(locationStr);
			} else {
				locationTv.setText("显示位置");
			}
			break;
		default:
			break;
		}

	}

	private void addCarousel(boolean isTag) {
		// count = tempList.size();
		imageViews = new ArrayList<View>();
		if (strsList == null || strsList.size() == 0) {
			View imageView = LayoutInflater.from(this).inflate(R.layout.square_published_item, null);
			imageViews.add(imageView);
			// handler.postDelayed(runnable, 2000);
		} else {
			count = strsList.size();
			dots = new ArrayList<View>();
			if (dotsLayout != null) {
				dotsLayout.removeAllViews();
			}
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			for (int i = 0; i < count; i++) {
				List<TopicOrTag> strs = strsList.get(i);
				View imageView = LayoutInflater.from(this).inflate(R.layout.square_published_item, null);
				FlowLayout tags = (FlowLayout) imageView.findViewById(R.id.tags);
				tags.setTag(i);

				// TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
				ImageView dotsView = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				if (i != count - 1)
					params.rightMargin = 30;
				dotsView.setLayoutParams(params);
				dotsView.setBackgroundResource(R.drawable.shape_less_gray_oval);
				if (0 == i) {
					dotsView.setBackgroundResource(R.drawable.shape_green_oval);
				}
				dotsLayout.addView(dotsView);
				dots.add(dotsView);
				imageViews.add(imageView);
				addFlowView(strs, tags, isTag);
			}
		}

		// TODO 设置填充ViewPager页面的适配器********当滚动主题有时再放开下面代码******
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				if (arg1 == strsList.size()) {

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
				if (arg1 == imageViews.size()) {

					return null;
				}
				((ViewPager) arg0).addView(imageViews.get(arg1), 0);
				return imageViews.get(arg1);
			}

		});
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
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
			dots.get(oldPosition).setBackgroundResource(R.drawable.shape_less_gray_oval);
			dots.get(position).setBackgroundResource(R.drawable.shape_green_oval);
			oldPosition = position;

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	}

	private void addFlowView(List<TopicOrTag> strs, FlowLayout flowLayout, final boolean isTag) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(strs.get(i).getName());
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			if (isTag) {
				newView.setBackgroundResource(R.drawable.shape_tag_bg);
			}
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			if (!isTag) {
				params.rightMargin = gap;
				params.topMargin = gap;
			} else {
				params.rightMargin = tagGap;
				params.topMargin = DensityUtil.dip2px(this, 10);
			}
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
			newView.setTag(strs.get(i));
			newView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String text = ((TextView) v).getText().toString();
					String id = null;
					TopicOrTag topicOrTag = (TopicOrTag) v.getTag();
					id = topicOrTag.getId();
					if (isTag) {
						if (!selecTags.contains(topicOrTag)) {
							selecTags.add(topicOrTag);
							addTagFlow(topicOrTag, tagsFlow);
						}
						mScrollView.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
//								int i= mScrollView.getChildAt(0).getMeasuredHeight() - mScrollView.getHeight();;
								mScrollView.smoothScrollTo(0,tagsFlow.getTop());
							}
						});
					} else {
						if (id.equals("-1")) {
							text = "##";
						}
						int length=editText.getSelectionStart();
						if(length==-1||length==editText.length()){
							mScrollView.smoothScrollTo(0,editText.getTop()+editText.getHeight());
						}
						TextStyleUtil
								.addForegroundColorSpan(editText, text, id);
						if(id.equals("-1")){
							 length=editText.getSelectionStart();
							editText.requestFocus();
							InputMethodManager imm = (InputMethodManager) editText.getContext()
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
							if(length!=-1)
							editText.setSelection(length-1);
							squareBottom.setVisibility(View.GONE);
						}
					}
				}
			});
		}
	}

	private void addTagFlow(TopicOrTag tag, FlowLayout flowLayout) {
		TextView newView = new TextView(this);
		newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		newView.setText(tag.getName());
		newView.setTypeface(TripawayApplication.normalTf);
		newView.setGravity(Gravity.CENTER);
		newView.setTextColor(Color.parseColor("#666666"));
		newView.setBackgroundResource(R.drawable.shape_tag_bg);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.rightMargin = tagGap;
		params.topMargin = tagGap;
		newView.setLayoutParams(params);
		flowLayout.addView(newView);
		newView.setTag(tag);
	}

	private void getFlowText(List<TopicOrTag> strs, boolean isTag) {
		strsList.clear();
		int w = screenWidth - gap;
		TextView newView = new TextView(this);
		newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		newView.setTypeface(TripawayApplication.normalTf);
		newView.setGravity(Gravity.CENTER);
		newView.setTextColor(Color.parseColor("#666666"));
		TextPaint textPaint = newView.getPaint();
		int lineWidth = 0;
		int lineNum = 0;
		List<TopicOrTag> strList = new ArrayList<TopicOrTag>();
		strsList.add(strList);
		for (int i = 0; i < strs.size(); i++) {
			String str = strs.get(i).getName();
			TopicOrTag topicOrTag = strs.get(i);
			int length = (int) textPaint.measureText(str);
			if (isTag) {
				if (lineWidth + gap + 2 * tagGap + length > w) {
					++lineNum;
					if (lineNum == 3) {
						strList = new ArrayList<TopicOrTag>();
						strsList.add(strList);
						lineNum = 0;
					}
					strList.add(topicOrTag);
					lineWidth = 3 * tagGap + length;
				} else {
					strList.add(topicOrTag);
					lineWidth = lineWidth + length + 3 * tagGap;
					System.out.print(22222);
				}
			} else {
				if (lineWidth + gap + length > w) {
					++lineNum;
					if (lineNum == 3) {
						strList = new ArrayList<TopicOrTag>();
						strsList.add(strList);
						lineNum = 0;
					}
					strList.add(topicOrTag);
					lineWidth = gap + length;
				} else {
					strList.add(topicOrTag);
					lineWidth = lineWidth + length + gap;
				}
			}
		}
		System.out.print(22222);
	}

	private void querySquareTags() {
		RequestParams params = new RequestParams();
		params.put("mcode", "sale.square.tag");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject object = dataArray.optJSONObject(i).optJSONObject("object");
							;
							TopicOrTag tag = new TopicOrTag();
							tag.setId(object.optString("tagsId"));
							tag.setName(object.optString("tagsName"));
							// tag.setName("#"+object.optString("tagsName")+"#");
							tags.add(tag);
						}
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	private void querySquareTopics() {
		RequestParams params = new RequestParams();
		params.put("mcode", "sale.square.theme");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					TopicOrTag initTopic = new TopicOrTag();
					initTopic.setId("-1");
					initTopic.setName("#自定义#");
					individualTopics.add(initTopic);
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject object = dataArray.optJSONObject(i).optJSONObject("object");
							;
							TopicOrTag topic = new TopicOrTag();
							String type = object.optString("type");
							topic.setId(object.optString("themeId"));
							if (object.optString("themName").startsWith("#"))
								topic.setName(object.optString("themName"));
							else
								topic.setName("#" + object.optString("themName") + "#");
							if (!StringUtils.isEmpty(type)) {
								if (type.equals("0")) {
									hotTopics.add(topic);
									individualTopics.add(topic);
								}
								// else
								// if(type.equals("1"))
								// individualTopics.add(topic);
							}
						}
					}
					// initHotTopic();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	private void initLocation() {
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

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				ToastUtil.showToast(getApplicationContext(), "定位失败");
				return;
			}
			// 定位结束，关闭定位
			String province = "";
			if (location.getProvince().contains("省")) {
				province = location.getProvince().substring(0, location.getProvince().length() - 1);
			} else {
				province = location.getProvince();
			}
			String city = "";
			if (location.getCity().contains("市")) {
				city = location.getCity().substring(0, location.getCity().length() - 1);
			} else {
				city = location.getCity();
			}
			StringBuffer str = new StringBuffer();
			if (!StringUtils.isEmpty(location.getProvince()) && !StringUtils.isEmpty(location.getCity())
					&& !StringUtils.isEmpty(location.getDistrict()) && !StringUtils.isEmpty(location.getStreet())) {
				str.append(location.getProvince()).append(location.getCity()).append(location.getDistrict())
						.append(location.getStreet());
				locationStr = str.toString();
			} else
				locationStr = city;
			if (isShowLocation)
				locationTv.setText(locationStr);
			mLocationClient.stop();
			NumberFormat f = NumberFormat.getNumberInstance();
			// coordinate= location.getLatitude()+","+ location.getLongitude();
		}

	}

	private void testBlogPublishedUrl(String desc, String coverKey) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("title", titleTv.getText().toString());
			jsonObject.put("desc", desc);
			jsonObject.put("cover", coverKey);
			if (isShowLocation) {
				if (locationStr != null && !locationStr.equals("正在定位中"))
					jsonObject.put("location", locationStr);
				else {
					jsonObject.put("location", "");
				}
			} else {
				jsonObject.put("location", "");
			}
			List<String> tagIdList = new ArrayList<String>();
			for (int i = 0; i < tagsFlow.getChildCount(); i++) {

				TopicOrTag tag = (TopicOrTag) tagsFlow.getChildAt(i).getTag();
				tagIdList.add(tag.getId());
			}
			List<String> themeNameList = new ArrayList<String>();
			for (RichText richText : richTexts) {
				if (richText.getType().equals(Type.TOPIC)) {
					themeNameList.add(richText.getText());
				}
			}
			jsonObject.put("themeName", new JSONArray(themeNameList));
			jsonObject.put("tagId", new JSONArray(tagIdList));
			jsonObject.put("access", access);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			// Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！",
			// 2000).show();
			HttpUtil.post(Urls.series_self_url, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					dialog.dismiss();
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						// picKeyList.clear();
						Toast.makeText(getApplicationContext(), "发布成功", 100).show();
						String id = response.optString("data");
						if (from.equals("pic")) {
							Intent data = new Intent();
							data.putExtra("seriesId", id);
							data.putExtra("title", titleTv.getText().toString());
							setResult(Activity.RESULT_OK, data);
							finish();
						} else {
							Intent preViewIntent = new Intent(SquareSerialPublishedActivity.this,
									SquareSerialPreview1Activity.class);
							preViewIntent.putExtra("id", id);
							if (tag.equals("home")) {
								preViewIntent.putExtra("tag", "home");
							} else if (tag.equals("square")) {
								preViewIntent.putExtra("tag", "square");
							}
							preViewIntent.putExtra("tag2", "new");
							startActivity(preViewIntent);
							activityAnimationOpen();
						}
					} else {
						Toast.makeText(getApplicationContext(), "发布失败", 100).show();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "发布失败", 100).show();
					System.out.println("发布fail:" + responseString);
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private UploadFinishListener uploadFinishListener = new UploadFinishListener() {

		@Override
		public void onUploadFinishListener() {
			// TODO Auto-generated method stub
			List<String> keyList = uploadPicUtil.getPicKeyList();
			Map<String, String> uriKeyMap = uploadPicUtil.getUriKeyMap();
			if (keyList.size() > 0)
				for (RichText richText : richTexts) {
					if (!StringUtils.isEmpty(richText.getUri())) {
						String key = uriKeyMap.get(richText.getUri());
						if (key != null) {
							if (richText.getType() == Type.BOOK || richText.getType() == Type.MUSIC
									|| richText.getType() == Type.FILM) {
								richText.getAttrs().put("img", key);
							} else {
								richText.setText(key);
							}
							keyList.remove(key);
						}
					}
				}
			if (from.equals("preview") && !StringUtils.isEmpty(seriesId)) {
				testPublishedPic(StringUtils.richTextToXml(richTexts));
			} else {
				if (keyList.size() == 1) {
					testBlogPublishedUrl(StringUtils.richTextToXml(richTexts), keyList.get(0));
				}

			}
		}
	};

	private void uploadPic() {
		List<String> uriList = new ArrayList<String>();
		for (RichText richText : richTexts) {
			if (!StringUtils.isEmpty(richText.getUri())) {
				uriList.add(richText.getUri());
			}
		}
		if (!(from.equals("preview") && !StringUtils.isEmpty(seriesId))) {
			uriList.add(cover);
		}
		uploadPicUtil.uploadPicData(uriList);
	}

	private void testPublishedPic(String desc) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("desc", desc);

			if (isShowLocation) {
				if (locationStr != null && !locationStr.equals("正在定位中"))
					jsonObject.put("location", locationStr);
				else {
					jsonObject.put("location", "");
				}
			} else {
				jsonObject.put("location", "");
			}
			List<String> tagIdList = new ArrayList<String>();
			for (int i = 0; i < tagsFlow.getChildCount(); i++) {

				TopicOrTag tag = (TopicOrTag) tagsFlow.getChildAt(i).getTag();
				tagIdList.add(tag.getId());
			}
			List<String> themeNameList = new ArrayList<String>();
			for (RichText richText : richTexts) {
				if (richText.getType().equals(Type.TOPIC)) {
					themeNameList.add(richText.getText());
				}
			}
			jsonObject.put("themeName", new JSONArray(themeNameList));
			jsonObject.put("tagId", new JSONArray(tagIdList));
			jsonObject.put("access", access);
			jsonObject.put("seriesId", seriesId);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			// Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！",
			// 2000).show();
			HttpUtil.post(Urls.tripStory_self_url, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						Toast.makeText(getApplicationContext(), "发布成功", 100).show();
						setResult(Activity.RESULT_OK);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "发布失败", 100).show();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
					Toast.makeText(getApplicationContext(), "发布失败", 100).show();
					System.out.println("发布fail:" + responseString);
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
