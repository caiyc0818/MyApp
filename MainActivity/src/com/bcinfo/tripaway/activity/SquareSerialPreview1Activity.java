package com.bcinfo.tripaway.activity;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter.UserReplyListener;
import com.bcinfo.tripaway.adapter.SerialAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.TextStyleUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil.UploadFinishListener;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.dialog.MyBlogDeleteDialog.OnBlogDeleteListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SquareSerialPreview1Activity extends BaseActivity implements OnClickListener {

	private final int ADD_CONTENT_COVER_IMAGE_CODE = 1002;

	private final int ADD_NEW_SERIAL = 1003;

	private final int PIC_TO_SERIAL = 1004;

	TripArticle tripArticle;

	RoundImageView product_servicer_icon_iv;
	TextView tvName;
	TextView tvPost;
	TextView tvTime;
	TextView tvDescription;
	TextView map_location_text;
	ImageView map_location;
	ImageView releaseBtn;
	LinearLayout imageall;
	private DisplayMetrics dm;

	private FlowLayout tagsFlow;

	private ImageView pic;

	private EditText title;

	private List<TripArticle> tripArticles = new ArrayList<TripArticle>();

	private LinearLayout serialListview;

	private String id;

	private ImageView isTalent;

	private TextView modify;

	private LinearLayout addSquareSerial;

	private LinearLayout chooseSquarePic;

	private final String[] accessState = { "all", "friends", "private" };
	private final String[] accessString = { "公开", "好友可见", "自己可见" };

	private boolean isCoverChange = false;

	private boolean isTitleChange = false;

	private String titleStr;

	private UploadPicUtil uploadPicUtil;

	private String uri;

	private String tag;
	private String tag2;
	private String tag3="no";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.square_serial_preview1);
		statisticsTitle = "连载编辑";
		initTitleLayout("连载");
		id = getIntent().getStringExtra("id");
		tag = getIntent().getStringExtra("tag");
		tag2 = getIntent().getStringExtra("tag2");
		// commentsList = tripArticle.getCommentList();
		dm = new DisplayMetrics();
		if (id == null) {
			id = "11";
		}
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		initView();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		uploadPicUtil = new UploadPicUtil(uploadFinishListener);
		testTripStorysUrl(id);
	}

	protected void initView() {
		product_servicer_icon_iv = (RoundImageView) findViewById(R.id.product_servicer_icon_iv);
		product_servicer_icon_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final UserInfo user = tripArticle.getPublisher();
				ActivityUtil.toPersonHomePage(user, SquareSerialPreview1Activity.this);
				activityAnimationOpen();
			}
		});
		tvName = (TextView) findViewById(R.id.tvName);
		tvPost = (TextView) findViewById(R.id.tvPost);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		map_location_text = (TextView) findViewById(R.id.map_location_text);
		map_location = (ImageView) findViewById(R.id.map_location);
		imageall = (LinearLayout) findViewById(R.id.imageall);
		// myFooterBar = (MyMBProgressHUD) findViewById(R.id.myfooterBar);

		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		pic = (ImageView) findViewById(R.id.pic);
		title = (EditText) findViewById(R.id.title);
		serialListview = (LinearLayout) findViewById(R.id.serial_listview);
		isTalent = (ImageView) findViewById(R.id.isTalent);
		modify = (TextView) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		addSquareSerial = (LinearLayout) findViewById(R.id.addSquareSerial);
		chooseSquarePic = (LinearLayout) findViewById(R.id.chooseSquarePic);
		addSquareSerial.setOnClickListener(this);
		chooseSquarePic.setOnClickListener(this);

	}

	boolean check = false;

	private TextView save;

	protected ArrayList<String> keyList;

	private UploadFinishListener uploadFinishListener = new UploadFinishListener() {

		@Override
		public void onUploadFinishListener() {
			// TODO Auto-generated method stub
			keyList = uploadPicUtil.getPicKeyList();
			if (keyList.size() == 1) {
				editSeries(id, keyList.get(0));
			}
		}
	};

	private ProgressDialog dialog;

	private List<PhotoModel> photos;

	/**
	 * 设置二级标题
	 * 
	 * @param title
	 *            旅行经历
	 * @param rightText
	 *            保存
	 */
	private void initTitleLayout(String title) {
		TextView titleTV = (TextView) findViewById(R.id.second_title_text);
		titleTV.setText(title);
		LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
		save = (TextView) findViewById(R.id.event_commit_button);
		save.setVisibility(View.VISIBLE);
		save.setOnClickListener(this);
		backButton.setOnClickListener(this);
		((View) titleTV.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
		((View) titleTV.getParent()).getBackground().setAlpha(255);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.release_button:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(SquareSerialPreview1Activity.this, LoginActivity.class);
				startActivity(it);
				return;
			}
			Intent intent = new Intent(SquareSerialPreview1Activity.this, MicroBlogPublishedActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_back_button:
			Intent returnIntent = new Intent();
			returnIntent.putExtra("tripArticle", tripArticle);
			setResult(0, returnIntent);
			finish();
			activityAnimationClose();
			break;
		case R.id.event_commit_button:
			if("new".equals(tag2)){
				AppManager.getAppManager().finishActivity2(SquareSerialPublishedActivity.class);
			}
			String str = title.getText().toString();
			if (str.equals("")) {
				ToastUtil.showToast(this, "标题不能为空");
				return;
			}
			if (!str.equals(titleStr)) {
				isTitleChange = true;
			}
			if(tag3.equals("have")){
				Intent it = new Intent("com.bcinfo.publishBlog2");
				sendBroadcast(it);
			}
			if (isCoverChange || isTitleChange) {
				dialog = ProgressDialog.show(this, null, "正在修改中，请稍后..");
				if (isCoverChange) {
					List<String> uriList = new ArrayList<String>(1);
					uriList.add(uri);
					uploadPicUtil.uploadPicData(uriList);
				} else if (isTitleChange) {
					editSeries(id, null);
				} else {
					editSeries(id, keyList.get(0));
				}
				break;
			}
			if (!isCoverChange && !isTitleChange) {
				if (tag.equals("square")) {
					Intent it = new Intent("com.bcinfo.publishBlog");
					sendBroadcast(it);
					Intent mainIntent = new Intent(SquareSerialPreview1Activity.this, MainActivity.class);
					mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mainIntent);
					return;
				} else if (tag.equals("home")) {
					if("new".equals(tag2)){
						Intent it = new Intent("com.bcinfo.publishBlog2");
						sendBroadcast(it);
						finish();
					}else{
						finish();
					}
					return;
				} else if(tag.equals("edit")){
					if("new".equals(tag2)){
						Intent it = new Intent("com.bcinfo.publishBlog2");
						sendBroadcast(it);
						finish();
					}else{
						finish();
					}
					return;
				}
			}

			break;
		case R.id.modify:
			CommonUtils.launchPhotoSelectorActivity(this, ADD_CONTENT_COVER_IMAGE_CODE);
			break;
		case R.id.addSquareSerial:
			Intent serialPublishedIntent = new Intent(this, SquareSerialPublishedActivity.class);
			serialPublishedIntent.putExtra("from", "preview");
			serialPublishedIntent.putExtra("seriesId", tripArticle.getPhotoId());
			startActivityForResult(serialPublishedIntent, ADD_NEW_SERIAL);
			break;
		case R.id.chooseSquarePic:
			Intent picChooseIntent = new Intent(this, SquareOldPicActivity.class);
			picChooseIntent.putExtra("seriesId", tripArticle.getPhotoId());
			startActivityForResult(picChooseIntent, PIC_TO_SERIAL);
			break;
		case R.id.detail_delete_btn:
			if (v.getTag() == null)
				return;
			String id = (String) v.getTag();
			new MyBlogDeleteDialog(this, new OnBlogDeleteListener() {
				@Override
				public void onDelete(String photoId) {
					delPic(photoId);
				}
			}, id).show();

			break;
		default:
			break;
		}
	}

	private void testTripStorysUrl(String seriesId) {
		HttpUtil.get(Urls.square_serial + seriesId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				// System.out.println(statusCode);
				System.out.println(response.toString());
				getTripStoryList(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/*
	 * 解析从服务端获取的微游记参数
	 */
	private void getTripStoryList(JSONObject response) {
		if ("00000".equals(response.optString("code"))) {

			// responsetv.setText(response.toString());
			JSONObject dataObj = response.optJSONObject("data").optJSONObject("series");
			JSONArray dataArray = response.optJSONObject("data").optJSONArray("tripstory");
			TripArticle tripArticle;
			if (dataObj != null && dataObj.length() != 0) {
				tripArticle = JsonUtil.getSerialTripArticle(dataObj);
				this.tripArticle = tripArticle;
				if (tripArticle != null) {
					// tripArticles.add(tripArticle);
				}

			}
			if (dataArray != null && dataArray.length() > 0) {
				for (int i = 0; i < dataArray.length(); i++) {
					JSONObject dataObj1 = dataArray.optJSONObject(i);
					tripArticle = JsonUtil.getTripArticle(dataObj1);
					if (tripArticle != null) {
						tripArticles.add(tripArticle);
						// break;
					}
				}
			}
			setData();
		}
	}

	private void setData() {
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getIsTalent())) {
			if (tripArticle.getPublisher().getIsTalent().equals("1"))
				isTalent.setVisibility(View.VISIBLE);
			else
				isTalent.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPublisher().getAvatar(),
					product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
		}
		String name = "";
		if (tripArticle.getPublisher().getNickname().equals(tripArticle.getPublisher().getMobile())) {
			name = StringUtils.getSecretStr(tripArticle.getPublisher().getNickname());
			tvName.setText(name);
		} else {
			tvName.setText(tripArticle.getPublisher().getNickname());
		}
		if (!StringUtils.isEmpty(tripArticle.getCover())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getCover(), pic,
					AppConfig.options(R.drawable.ic_launcher));
		}
		if (!StringUtils.isEmpty(tripArticle.getTitle())) {
			title.setText(tripArticle.getTitle());
			titleStr = tripArticle.getTitle();
		}

		for (int i = 0; i < tripArticles.size(); i++) {
			addItem(i);
		}
	}

	private void addTagFlow(List<String> tagList, FlowLayout flowLayout) {
		for (String tag : tagList) {
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(tag);
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			newView.setBackgroundResource(R.drawable.shape_tag_bg);
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(this, 8);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void addItem(int position) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.square_serial_preview_item, null);
		ImageView map_location = (ImageView) view.findViewById(R.id.map_location);
		TextView map_location_text = (TextView) view.findViewById(R.id.map_location_text);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
		View line = view.findViewById(R.id.line);
		LinearLayout imageall = (LinearLayout) view.findViewById(R.id.imageall);
		FlowLayout tagsFlow = (FlowLayout) view.findViewById(R.id.tags_flow);

		TripArticle tripArticle = tripArticles.get(position);

		if (tripArticle != null) {
			if (position == tripArticles.size() - 1) {
				line.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(tripArticle.getLocation()) && !tripArticle.getLocation().equals("不显示")) {
				map_location_text.setText(tripArticle.getLocation());
				map_location.setVisibility(View.VISIBLE);
				map_location_text.setVisibility(View.VISIBLE);
			} else {
				map_location.setVisibility(View.GONE);
				map_location_text.setVisibility(View.GONE);
			}
			if (tripArticle.getType() == 0) {
				imageall.setVisibility(View.VISIBLE);
				imageall.removeAllViews();
				addImage(tripArticle, imageall);
			} else {
				imageall.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(tripArticle.getDescription())) {
				tvDescription.setText("");
			} else {
				List<RichText> richTexts = StringUtils.xmlToRichText(tripArticle.getDescription());
				StringUtils.initRichTextView1(tvDescription, richTexts);

			}
			if (tripArticle.getTagList().size() > 0) {
				tagsFlow.setVisibility(View.VISIBLE);
				addTagFlow(tripArticle.getTagList(), tagsFlow);
			} else {
				tagsFlow.setVisibility(View.GONE);
			}
			String time = DateUtil.setTime(tripArticle.getPublishTime());
			if (tripArticle.getType() == 0)
				tvTime.setText(StringUtils.isEmpty(time) ? "" : time + "晒图");
			else if (tripArticle.getType() == 1)
				tvTime.setText(StringUtils.isEmpty(time) ? "" : time + "连载");
			ImageView deleteBtn = (ImageView) view.findViewById(R.id.detail_delete_btn);
			deleteBtn.setTag(tripArticle.getPhotoId());
			deleteBtn.setOnClickListener(this);
			TextView accessText = (TextView) findViewById(R.id.access_text);
			String access = tripArticle.getAccess();
			for (int i = 0; i < accessState.length; i++) {
				if (accessState[i].equals(access)) {
					accessText.setText(accessString[i]);
					break;
				}
			}
			serialListview.addView(view);
		}
	}

	private void addImage(TripArticle tripArticle, LinearLayout imageall) {
		int gap = 2 * DensityUtil.dip2px(this, 11);
		for (int i = 0; i < tripArticle.getPictureList().size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(dm.widthPixels - gap,
					(dm.widthPixels - gap) * Integer.parseInt(tripArticle.getPictureList().get(i).getHeight())
							/ Integer.parseInt(tripArticle.getPictureList().get(i).getWidth()));

			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(ls);
			imageView.setImageResource(R.drawable.ic_launcher);
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPictureList().get(i).getUrl(),
					imageView, AppConfig.options(R.drawable.ic_launcher));
			imageall.addView(imageView);
			if (i != tripArticle.getPictureList().size() - 1) {
				View view = new View(this);
				LinearLayout.LayoutParams lsview = new LinearLayout.LayoutParams(dm.widthPixels - 14, 6);
				view.setLayoutParams(lsview);
				imageall.addView(view);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ADD_CONTENT_COVER_IMAGE_CODE:
			 photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				if (photos.size() > 0) {
					PhotoModel info = photos.get(0);
					uri = info.getOriginalPath();
					ImageLoader.getInstance().displayImage("file://" + info.getOriginalPath(), pic);
				}
				isCoverChange = true;
				break;
			case ADD_NEW_SERIAL:
			case PIC_TO_SERIAL:
				tag3="have";
				initPicItem();
				break;
			default:
				break;
			}
		}
	}

	private void delPic(final String picId) {
		HttpUtil.delete(Urls.tripStory_delete_url + picId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					Toast.makeText(getApplicationContext(), "删除成功", 100).show();
					Iterator<TripArticle> it = tripArticles.iterator();
					while (it.hasNext()) {
						TripArticle tripArticle = it.next();
						if (picId.equals(tripArticle.getPhotoId())) {
							it.remove();
							break;
						}

					}
					initPicItem();
				} else {
					Toast.makeText(getApplicationContext(), "删除失败", 100).show();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.makeText(getApplicationContext(), "删除失败", 100).show();
			}
		});
	}

	private void editSeries(String seriesId, final String cover) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("seriesId", seriesId);
			jsonObject.put("title", title.getText().toString());
			if (cover != null)
				jsonObject.put("cover", cover);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			// Toast.makeText(MicroBlogPublishedActivity.this, "发布中，请稍候！",
			// 2000).show();
			HttpUtil.post(Urls.series_edit, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					dialog.dismiss();
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						Toast.makeText(getApplicationContext(), "修改成功", 100).show();
						if (tag.equals("square")) {
							Intent it = new Intent("com.bcinfo.publishBlog");
							sendBroadcast(it);
							Intent mainIntent = new Intent(SquareSerialPreview1Activity.this, MainActivity.class);
							mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mainIntent);
							return;
						} else if (tag.equals("edit")) {
//							Intent it = new Intent("com.bcinfo.publishBlog");
//							sendBroadcast(it);
//							Intent mainIntent = new Intent(SquareSerialPreview1Activity.this,
//									TouristHomepageActivity.class);
//							mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							UserInfo user = new UserInfo();
//							user.setUserId(PreferenceUtil.getUserId());
//							mainIntent.putExtra("userInfo", user);
//							startActivity(mainIntent);
							Intent intent = new Intent();
							intent.putExtra("id", tripArticle.getPhotoId());
							intent.putExtra("title", title.getText().toString());
							if (cover != null){								
								intent.putExtra("cover", cover);
							}else{
								intent.putExtra("cover","");
							}
							setResult(Activity.RESULT_OK,intent);
							finish();
							return;
						} else if (tag.equals("home")) {
							Intent it = new Intent("com.bcinfo.publishBlog2");
							sendBroadcast(it);
							finish();
							return;
						}
					} else {
						Toast.makeText(getApplicationContext(), "修改失败", 100).show();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "修改失败", 100).show();
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initPicItem() {
		tripArticles.clear();
		serialListview.removeAllViews();
		testTripStorysUrl(id);
	}
}
