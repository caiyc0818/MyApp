package com.bcinfo.tripaway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.ui.PhotoPreviewActivity;
import com.bcinfo.photoselector.ui.PhotoSelectorActivity;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.GdAdapter;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
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
import com.wefika.flowlayout.FlowLayout;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SquarePicPublishedActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private final static String TAG = "SquarePicPublishedActivity";
	private ViewPager viewPager;
	private LinearLayout dotsLayout;
	private List<View> imageViews;
	private ArrayList<View> dots;

	private int count;
	private int currentItem = 0;

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

	private List<PhotoModel> selected;
	private final static int SELECT_IMAGE_CODE = 1001;
	private final static int SHOW_IMAGE_CODE = 1002;
	private final static int ADD_IMAGE_CODE = 1003;
	private final static int PIC_TO_SERIAL = 1004;
	private final int MAX_PHOTOS = 50;
	private GdAdapter adapter;
	private GridView gd;
	private EditText editText;

	private int tagGap;

	private List<TopicOrTag> individualTopics = new ArrayList<TopicOrTag>();

	private List<TopicOrTag> hotTopics = new ArrayList<TopicOrTag>();

	private List<TopicOrTag> tags = new ArrayList<TopicOrTag>();

	private List<RichText> richTexts = new ArrayList<RichText>();
	private TextView blogPublish;
	private TextView squareTitle;
	private FlowLayout tagsFlow;
	private TextView hotTopic1;
	private TextView hotTopic2;
	private TextView hotTopic3;
	private TextView hotTopic4;
	private LinearLayout hotTopicLinear;

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

	private UploadPicUtil uploadPicUtil;

	public int clickSpanStart = -1;

	public int clickSpanEnd = -1;
	private TextView blogNavBack;

	private String seriesId;
	private RelativeLayout closeRelate;
	private TextView serialTitle;
	private ImageView close;
	private ProgressDialog dialog;

	private int currentBottomItem = 0;
	private String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_pic_published);
		statisticsTitle = "发布晒图";
		AppManager.getAppManager().addActivity(this);
		tag = getIntent().getStringExtra("tag");
		initViews();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		gap = DensityUtil.dip2px(this, 13);
		tagGap = DensityUtil.dip2px(this, 8);
		querySquareTags();
		querySquareTopics();
		initLocation();
		uploadPicUtil = new UploadPicUtil(uploadFinishListener);
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
	}

	private void initViews() {
		blogNavBack = (TextView) findViewById(R.id.blog_navBack);
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) findViewById(R.id.carousel_dots);
		itemPic = (LinearLayout) findViewById(R.id.item1);
		itemTopic = (LinearLayout) findViewById(R.id.item2);
		itemTag = (LinearLayout) findViewById(R.id.item3);
		itemMusic = (LinearLayout) findViewById(R.id.item4);
		itemBook = (LinearLayout) findViewById(R.id.item5);
		itemMovie = (LinearLayout) findViewById(R.id.item6);
		itemSerial = (LinearLayout) findViewById(R.id.item7);
		blogPublish = (TextView) findViewById(R.id.blog_publish);
		squareTitle = (TextView) findViewById(R.id.blog_title);
		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		hotTopic1 = (TextView) findViewById(R.id.hot_topic1);
		hotTopic2 = (TextView) findViewById(R.id.hot_topic2);
		hotTopic3 = (TextView) findViewById(R.id.hot_topic3);
		hotTopic4 = (TextView) findViewById(R.id.hot_topic4);
		accessText = (TextView) findViewById(R.id.access_text);
		locationTv = (TextView) findViewById(R.id.locationTv);
		hotTopicLinear = (LinearLayout) findViewById(R.id.hot_topic_linear);
		blogNavBack.setOnClickListener(this);
		squareTitle.setText("晒图");
		blogPublish.setOnClickListener(this);
		itemPic.setOnClickListener(this);
		itemTopic.setOnClickListener(this);
		itemTag.setOnClickListener(this);
		itemMusic.setOnClickListener(this);
		itemBook.setOnClickListener(this);
		itemMovie.setOnClickListener(this);
		itemSerial.setOnClickListener(this);
		hotTopic1.setOnClickListener(this);
		hotTopic2.setOnClickListener(this);
		hotTopic3.setOnClickListener(this);
		hotTopic4.setOnClickListener(this);
		accessText.setOnClickListener(this);
		locationTv.setOnClickListener(this);
		squareBottom = (LinearLayout) findViewById(R.id.square_bottom_linear);

		editText = (EditText) findViewById(R.id.trip_edit_photoDescript);
		editText.setTypeface(TripawayApplication.normalTf);
		editText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				squareBottom.setVisibility(View.GONE);
				return false;
			}
		});
		editText.setMovementMethod(LinkArrowKeyMovementMethod.getInstance());
		gd = (GridView) findViewById(R.id.gd);
		selected = new ArrayList<PhotoModel>();
		PhotoModel photoModel = new PhotoModel();
		photoModel.setOriginalPath("default");
		selected.add(photoModel);
		adapter = new GdAdapter(this, selected);
		gd.setAdapter(adapter);
		gd.setOnItemClickListener(this);
		closeRelate = (RelativeLayout) findViewById(R.id.close_relate);
		serialTitle = (TextView) findViewById(R.id.serial_title);
		close = (ImageView) findViewById(R.id.close);
		close.setOnClickListener(this);
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.microblog_title);
		titleLayout.setAlpha(1);
		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (position == 0) {// 如果是最后一个
			enterChoosePhoto();
		} else {
			List<PhotoModel> lists = new ArrayList<PhotoModel>();
			lists.addAll(selected);
			lists.remove(0);
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", (Serializable) lists);
			if (position - 1 >= 0)
				position = position - 1;
			bundle.putInt("position", position);
			Intent intent = new Intent(this, PhotoPreviewActivity.class);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivityForResult(intent, SHOW_IMAGE_CODE);
		}
	}

	@SuppressWarnings("unchecked")
	private void enterChoosePhoto() {
		// TODO Auto-generated method stub

		List<PhotoModel> choosed = new ArrayList<>();
		if (selected.size() > 0) {
			choosed.addAll(selected);
			choosed.remove(0);
		}
		Intent intent = new Intent(this, PhotoSelectorActivity.class);
		intent.putExtra(PhotoSelectorActivity.KEY_MAX, MAX_PHOTOS);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("selected", (ArrayList<? extends Parcelable>) choosed);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, SELECT_IMAGE_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_IMAGE_CODE:
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				selected.clear();
				PhotoModel addModel = new PhotoModel();
				addModel.setOriginalPath("default");
				selected.add(addModel);
				int i = 0;
				for (PhotoModel photoModel : photos) {
					if (i == 5)
						break;
					selected.add(photoModel);
					++i;
				}
				adapter.notifyDataSetChanged();
				break;
			case SHOW_IMAGE_CODE:
				List<PhotoModel> photos1 = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				if (photos1.size() != selected.size() - 1) {
					Iterator<PhotoModel> iterator = selected.iterator();
					while (iterator.hasNext()) {
						PhotoModel photoModel = iterator.next();
						if (photoModel.getOriginalPath().equals("default"))
							continue;
						boolean check = false;
						for (PhotoModel photoModel1 : photos1) {
							if (photoModel1.getOriginalPath().equals(photoModel.getOriginalPath())) {
								check = true;
								break;
							}
						}
						if (!check) {
							iterator.remove();
						}
					}
					adapter.notifyDataSetChanged();
				}
				break;
			case ADD_IMAGE_CODE:
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
				float w = DensityUtil.dip2px(this, 20);
				try {
					if (type == 1)
						TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_music);
					else if (type == 2)
						TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_book);
					else if (type == 3)
						TextStyleUtil.addImageSpan(editText, " ", R.drawable.square_movie);
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
			case PIC_TO_SERIAL:
				if (data != null) {
					seriesId = data.getStringExtra("seriesId");
					String title = data.getStringExtra("title");
					serialTitle.setText("已晒图至连载：" + title);
					closeRelate.setVisibility(View.VISIBLE);
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
			startActivityForResult(musicIntent, ADD_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item5:// 书籍
			Intent bookIntent = new Intent(this, SquareMusicOrBookOrMusicAddActivity.class);
			bookIntent.putExtra("type", 2);
			startActivityForResult(bookIntent, ADD_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item6:// 电影
			Intent movieIntent = new Intent(this, SquareMusicOrBookOrMusicAddActivity.class);
			movieIntent.putExtra("type", 3);
			startActivityForResult(movieIntent, ADD_IMAGE_CODE);
			activityAnimationOpen();
			break;
		case R.id.item7:// 连载
			addToSerials();
			break;
		case R.id.blog_publish:
			richTexts.clear();
			if (StringUtils.isEmpty(editText.getText().toString())) {
				ToastUtil.showToast(this, "发布内容不能为空");
				return;
			}
			if (selected.size() < 2) {
				ToastUtil.showToast(this, "发布图片不能为空");
				return;
			}
			StringUtils.getRichText(editText, richTexts);
			// StringUtils.xmlToRichText(StringUtils.richTextToXml(richTexts));
			dialog = ProgressDialog.show(this, null, "正在发布中，请稍后..");
			uploadPic();
			break;
		case R.id.hot_topic1:
		case R.id.hot_topic2:
		case R.id.hot_topic3:
		case R.id.hot_topic4:
			String text = ((TextView) v).getText().toString();
			TopicOrTag topicOrTag = (TopicOrTag) v.getTag();
			String id = topicOrTag.getId();
			TextStyleUtil.addForegroundColorSpan(editText, text, id);
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
		case R.id.close:
			closeRelate.setVisibility(View.GONE);
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
						boolean isCheck = false;
						for (int i = 0; i < tagsFlow.getChildCount(); i++) {

							TopicOrTag tag = (TopicOrTag) tagsFlow.getChildAt(i).getTag();
							if (tag == topicOrTag) {
								isCheck = true;
								break;
							}
						}
						if (!isCheck)
							addTagFlow(topicOrTag, tagsFlow);
					} else {
						if (id.equals("-1")) {
							text = "##";
						}
						TextStyleUtil.addForegroundColorSpan(editText, text, id);
						if (id.equals("-1")) {
							int length = editText.getSelectionStart();
							editText.requestFocus();
							InputMethodManager imm = (InputMethodManager) editText.getContext()
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
							editText.setSelection(length - 1);
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
		params.topMargin = DensityUtil.dip2px(this, 10);
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
							TopicOrTag tag = new TopicOrTag();
							tag.setId(object.optString("tagsId"));
							tag.setName(object.optString("tagsName"));
							// tag.setName("#" + object.optString("tagsName")
							// + "#");
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
								// else if (type.equals("1"))
								// individualTopics.add(topic);
							}
						}
					}
					initHotTopic();
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

	private void initHotTopic() {
		if (hotTopics.size() == 0) {
			hotTopicLinear.setVisibility(View.GONE);
		} else {
			hotTopic1.setText(hotTopics.get(0).getName());
			hotTopic1.setTag(hotTopics.get(0));
			if (hotTopics.size() == 1) {
				((View) hotTopic2.getParent()).setVisibility(View.GONE);
				((View) hotTopic3.getParent().getParent()).setVisibility(View.GONE);
			} else {
				hotTopic2.setText(hotTopics.get(1).getName());
				hotTopic2.setTag(hotTopics.get(1));
				if (hotTopics.size() == 2) {
					((View) hotTopic3.getParent().getParent()).setVisibility(View.GONE);
				} else {
					hotTopic3.setText(hotTopics.get(2).getName());
					hotTopic3.setTag(hotTopics.get(2));
					if (hotTopics.size() == 3) {
						hotTopic4.setVisibility(View.INVISIBLE);
					} else {
						hotTopic4.setText(hotTopics.get(3).getName());
						hotTopic4.setTag(hotTopics.get(3));
					}
				}
			}
		}
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

	private void testBlogPublishedUrl(String desc) {
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
			if (!StringUtils.isEmpty(seriesId))
				jsonObject.put("seriesId", seriesId);
			List<String> picKeyList = uploadPicUtil.getPicKeyList();
			if (picKeyList.size() != 0) {
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < picKeyList.size(); i++) {
					jsonArray.put(picKeyList.get(i));
				}
				jsonObject.put("pictures", jsonArray);
				StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
				// Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！",
				// 2000).show();
				HttpUtil.post(Urls.tripStory_self_url, stringEntity, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

						super.onSuccess(statusCode, headers, response);
						dialog.dismiss();
						if ("00000".equals(response.optString("code"))) {
							Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_LONG).show();
							if (tag.equals("square")) {
								Intent it = new Intent("com.bcinfo.publishBlog");
								sendBroadcast(it);
								Intent mainIntent = new Intent(SquarePicPublishedActivity.this, MainActivity.class);
								mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(mainIntent);
							} else if (tag.equals("home")) {
								Intent it = new Intent("com.bcinfo.publishBlog2");
								sendBroadcast(it);
								finish();
							}
						} else {
							Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {

						super.onFailure(statusCode, headers, responseString, throwable);
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_LONG).show();
						System.out.println("发布fail:" + responseString);
					}

				});

			}

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
							richText.getAttrs().put("img", key);
							keyList.remove(key);
						}
					}
				}
			testBlogPublishedUrl(StringUtils.richTextToXml(richTexts));
		}
	};

	private void uploadPic() {
		List<String> uriList = new ArrayList<String>();
		for (RichText richText : richTexts) {
			if (!StringUtils.isEmpty(richText.getUri())) {
				uriList.add(richText.getUri());
			}
		}
		for (PhotoModel photoModel : selected) {
			if (!photoModel.getOriginalPath().equals("default"))
				uriList.add(photoModel.getOriginalPath());
		}

		uploadPicUtil.uploadPicData(uriList);
	}

	private PopupWindow pw;

	private void addToSerials() {
		int gap = DensityUtil.dip2px(this, 30);
		View convView = LayoutInflater.from(this).inflate(R.layout.shaitupopuwindow, null);
		pw = new PopupWindow(convView, screenWidth - 2 * gap, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.alpha = 0.6f;
		getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);

		pw.showAtLocation(editText, Gravity.CENTER | Gravity.CENTER, 0, 0);
		// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
		pw.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// TODO Auto-generated method stub
				// 将窗口颜色调回完全透明
				WindowManager.LayoutParams params = getWindow().getAttributes();
				params.alpha = 1;
				getWindow().setAttributes(params);
			}
		});

		// 设置pw中button的点击事件
		TextView photo = (TextView) convView.findViewById(R.id.photo);
		photo.setText("创建新连载");
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent3 = new Intent(SquarePicPublishedActivity.this, SquareSerialPublishedActivity.class);
				intent3.putExtra("from", "pic");
				startActivityForResult(intent3, PIC_TO_SERIAL);
				pw.dismiss();
			}
		});
		LinearLayout series = (LinearLayout) convView.findViewById(R.id.series);
		((TextView) series.getChildAt(0)).setText("添加至连载");
		((TextView) series.getChildAt(1)).setVisibility(View.GONE);
		series.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				Intent intent3 = new Intent(SquarePicPublishedActivity.this, AddSeriesActivity.class);
				intent3.putExtra("from", "pic");
				startActivityForResult(intent3, PIC_TO_SERIAL);
				pw.dismiss();
			}
		});
	}
}
