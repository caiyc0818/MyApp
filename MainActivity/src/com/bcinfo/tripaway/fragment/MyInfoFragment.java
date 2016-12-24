package com.bcinfo.tripaway.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.CashCardActivity;
import com.bcinfo.tripaway.activity.CommonInfoActivity;
import com.bcinfo.tripaway.activity.FollowOrFansActivity;
import com.bcinfo.tripaway.activity.MyAccountActivity2;
import com.bcinfo.tripaway.activity.MyCollectionActivity;
import com.bcinfo.tripaway.activity.MyJourneyActivity;
import com.bcinfo.tripaway.activity.MyScheduletActivity;
import com.bcinfo.tripaway.activity.PersonalCustomizationStepEnd;
import com.bcinfo.tripaway.activity.SettingActivity;
import com.bcinfo.tripaway.activity.SettingUserInfoActivity;
import com.bcinfo.tripaway.activity.TouristHomepageActivity;
import com.bcinfo.tripaway.bean.MyAccSubject;
import com.bcinfo.tripaway.bean.MyInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil.UploadFinishListener;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog.OperationListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

public class MyInfoFragment extends BaseFragment implements OnClickListener, PersonalScrollViewListener {


	// 我的收藏
	private RelativeLayout mMyStoreLayout;

	// 我的定制
	private RelativeLayout mMyCustomizeLayout;

	// 我的旅程
	private RelativeLayout mMyJourneyLayout;

	// 我的微游记
	private RelativeLayout mMyBlogLayout;

	// 常用信息
	private RelativeLayout mCommonInfoLayout;

	// 头像
	private RoundImageView mUserPhotoIv;


	// 待支付
	private TextView mPayTv;

	// 待出行
	private TextView mLeaveTv;

	// 待评价
	private TextView mEvaluteTv;

	private MyInfo myInfo = new MyInfo();

	private LinearLayout payLayout;

	private LinearLayout leaveLayout;

	private LinearLayout evaluteLayout;

	private LinearLayout myshceLayout;
	private View myScheduleimTip;

	private RelativeLayout my_fas_layout;
	private TextView fas;
	MyAccSubject myAccSubject = new MyAccSubject();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	/**
	 * 我的现金券
	 */
	private RelativeLayout my_cashCoupon_layout;
	private TextView nickNameTextView;
	private TextView focusNumTextView;
	private TextView fansNumTextView;

	private ImageView mProductHeadImg;

	private PersonInfoScrollView mProductScrollView;
	
	private String path = "";

	private Uri uri;

	private ImageView changeAvatorTv;
	
	/**
	 * 上传toaken
	 */
	private String uploadToken;

	private LinearLayout fans;

	private LinearLayout follow;

	private TextView descTextView;
	
	private int alpha = -1;
	
	private RelativeLayout layout_product_detail_title;

	private TextView setting;

	private TextView userEditTv;
	
	private boolean isModifyAvar=true;
	
	private final int ADD_BACKGROUND_CODE = 1002;
	
	private UploadPicUtil uploadPicUtil;
	
	private String backgroundKey;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_info_fragment_layout, container, false);
		initView(view);
		// 初始化用户头像信息
		initLoginUserInfo();
		uploadPicUtil = new UploadPicUtil(uploadFinishListener);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!StringUtils.isEmpty(PreferenceUtil.getUserId())) {
			initLoginUserInfo();
		}

		if (alpha != -1) {
			layout_product_detail_title.getBackground().setAlpha(alpha);
		}
	}

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	statisticsTitle="我的";
	    	if (savedInstanceState != null) {
				path = savedInstanceState.getString("path");
				uri = savedInstanceState.getParcelable("uri");
			}
	    }
	 
	private void initView(View view) {
		changeAvatorTv = (ImageView) view.findViewById(R.id.change_avator_tv);
		layout_product_detail_title = (RelativeLayout) view.findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_product_detail_title.setFocusable(true);
		layout_product_detail_title.setFocusableInTouchMode(true);
		layout_product_detail_title.requestFocus();
		mProductHeadImg = (ImageView) view.findViewById(R.id.personpic);
		mProductScrollView = (PersonInfoScrollView) view.findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setPullListener(mPullListener);
		mProductScrollView.setScrollListener(this);
		mMyBlogLayout = (RelativeLayout) view.findViewById(R.id.my_blog_layout);
		mMyBlogLayout.setOnClickListener(this);
		nickNameTextView = (TextView) view.findViewById(R.id.username);
		focusNumTextView = (TextView) view.findViewById(R.id.focus_num);
		fansNumTextView = (TextView) view.findViewById(R.id.fans_num);
		mMyStoreLayout = (RelativeLayout) view.findViewById(R.id.my_store_layout);
		mMyCustomizeLayout = (RelativeLayout) view.findViewById(R.id.my_customize_layout);
		mUserPhotoIv = (RoundImageView) view.findViewById(R.id.personal_icon);
		mCommonInfoLayout = (RelativeLayout) view.findViewById(R.id.my_custominfo_layout);
		my_cashCoupon_layout = (RelativeLayout) view.findViewById(R.id.my_cashCoupon_layout);
		mCommonInfoLayout.setOnClickListener(this);
		mMyStoreLayout.setOnClickListener(this);
//		mUserPhotoIv.setOnClickListener(this);
		mMyStoreLayout.setOnClickListener(this);
		mMyCustomizeLayout.setOnClickListener(this);
		my_cashCoupon_layout.setOnClickListener(this);
		mPayTv = (TextView) view.findViewById(R.id.will_pay_tv);
		mLeaveTv = (TextView) view.findViewById(R.id.will_leave_tv);
		mEvaluteTv = (TextView) view.findViewById(R.id.will_evalute_tv);
		mMyJourneyLayout = (RelativeLayout) view.findViewById(R.id.my_travel_layout);
		myshceLayout = (LinearLayout) view.findViewById(R.id.my_schedule_layout);
		mMyJourneyLayout.setOnClickListener(this);
		payLayout = (LinearLayout) view.findViewById(R.id.pay_layout);
		leaveLayout = (LinearLayout) view.findViewById(R.id.leave_layout);
		evaluteLayout = (LinearLayout) view.findViewById(R.id.evalute_layout);
		myshceLayout.setOnClickListener(this);
		payLayout.setOnClickListener(this);
		leaveLayout.setOnClickListener(this);
		evaluteLayout.setOnClickListener(this);
		myScheduleimTip = view.findViewById(R.id.myScheduleimTip);
		my_fas_layout = (RelativeLayout) view.findViewById(R.id.my_fas_layout);
		my_fas_layout.setOnClickListener(this);
		fas = (TextView) view.findViewById(R.id.fas);
		changeAvatorTv.setOnClickListener(this);
		fans = (LinearLayout) view.findViewById(R.id.fans);
		follow = (LinearLayout) view.findViewById(R.id.follow);
		fans.setOnClickListener(this);
		follow.setOnClickListener(this);
		descTextView = (TextView) view.findViewById(R.id.desc);
		setting = (TextView) view.findViewById(R.id.setting);
		userEditTv = (TextView) view.findViewById(R.id.user_edit_tv);
		setting.setOnClickListener(this);
		userEditTv.setOnClickListener(this);
		mProductHeadImg.setOnClickListener(this);
		mUserPhotoIv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	
	private UploadFinishListener uploadFinishListener = new UploadFinishListener() {

		@Override
		public void onUploadFinishListener() {
			// TODO Auto-generated method stub
			List<String> keyList = uploadPicUtil.getPicKeyList();
			if (keyList.size() == 1) {
				userInfoEdit(keyList.get(0));
			}
		}
	};
	
	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {
		}
	};
	
	private void initLoginUserInfo() {
		if (!StringUtils.isEmpty(PreferenceUtil.getUserId())) {
			UserInfo userInfo = UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
			if (!StringUtils.isEmpty(userInfo.getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(), mUserPhotoIv,
						AppConfig.options(R.drawable.user_defult_photo));

			}

			if (!StringUtils.isEmpty(userInfo.getNickname()))
				nickNameTextView.setText(userInfo.getNickname());
			else
				nickNameTextView.setText(userInfo.getRealName());
			myInfo.setUserInfo(userInfo);
			queryUserInfoSubject();

			// 提现功能 不放开接口没好
			initfas();

		} else {
		}
	}

	private void initfas() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("facId", "0001");
		HttpUtil.get(Urls.my_account_subject, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					JSONObject resultObj = response.optJSONObject("data");
					myAccSubject.setTotalBalance(resultObj.optString("totalBalance"));// 账户余额
					myAccSubject.setAvailableBalance(resultObj.optString("availableBalance"));// 可用余额（可提现，可消费）
				} else if ("00099".equals(response.optString("code"))) {
					PreferenceUtil.delUserInfo();
					UserInfoDB.getInstance().deleteAll();
					goLoginActivity();
				}
				if (!StringUtils.isEmpty(myAccSubject.getAvailableBalance())) {
					fas.setText("¥ " + decimalFormat.format(Double.parseDouble(myAccSubject.getAvailableBalance()))
							+ "可提现");
				}

			}
		});

	}

	private void queryUserInfoSubject() {
		HttpUtil.get(Urls.userinfo_subject, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");

				if (!"00000".equals(code)) {
					return;
				}

				JSONObject data = response.optJSONObject("data");

				if (null != data) {

					JSONObject userInfo = data.optJSONObject("userInfo");

					UserInfo info = new UserInfo();
					if (null != userInfo) {
						info=JsonUtil.getUserInfo(userInfo);
					}

					myInfo.setUserInfo(info);
					myInfo.setFansNum(data.optString("fansNum"));
					myInfo.setFocusNum(data.optString("focusNum"));
					myInfo.setTripstoryNum(data.optString("tripstoryNum"));
					myInfo.setWaitAppraiseNum(data.optString("waitAppraiseNum"));
					myInfo.setWaitDepartNum(data.optString("waitDepart"));
					myInfo.setWaitPayNum(data.optString("waitPayNum"));
					myInfo.setItineraryNum(data.optString("itineraryNum"));
				}
				setUserInfo(myInfo);
			}
		});
	}

	private void setUserInfo(MyInfo info) {
		// 待支付
		if (!StringUtils.isEmpty(info.getWaitPayNum()) && !"0".equals(info.getWaitPayNum())) {
			mPayTv.setText(info.getWaitPayNum());
			mPayTv.setVisibility(View.VISIBLE);
		} else {
			mPayTv.setVisibility(View.GONE);
		}

		if (!StringUtils.isEmpty(info.getWaitDepartNum()) && !"0".equals(info.getWaitDepartNum())) {
			mLeaveTv.setText(info.getWaitDepartNum());
			mLeaveTv.setVisibility(View.VISIBLE);
		} else {
			mLeaveTv.setVisibility(View.GONE);
		}

		if (!StringUtils.isEmpty(info.getWaitAppraiseNum()) && !"0".equals(info.getWaitAppraiseNum())) {
			mEvaluteTv.setText(info.getWaitAppraiseNum());
			mEvaluteTv.setVisibility(View.VISIBLE);
		} else {
			mEvaluteTv.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(info.getItineraryNum()) && !"0".equals(info.getItineraryNum())) {
			if (!StringUtils.isEmpty(PreferenceUtil.getString(PreferenceUtil.getUserId()))
					&& PreferenceUtil.getUserId().equals(PreferenceUtil.getString(PreferenceUtil.getUserId()))
					&&!StringUtils.isEmpty(PreferenceUtil.getString(PreferenceUtil.getUserId()+"出行单条数"))
					&&(PreferenceUtil.getUserId()+info.getItineraryNum()).equals(PreferenceUtil.getString(PreferenceUtil.getUserId()+"出行单条数"))
					) {
				myScheduleimTip.setVisibility(View.GONE);
			} else {
				myScheduleimTip.setVisibility(View.VISIBLE);
			}
		} else {
			myScheduleimTip.setVisibility(View.GONE);
		}
		UserInfo user=myInfo.getUserInfo();
		
		if (!StringUtils.isEmpty(user.getNickname()))
			nickNameTextView.setText(user.getNickname());
		else
			nickNameTextView.setText(user.getRealName());
		focusNumTextView.setText(user.getFocus() == null ? "0" : user.getFocus());
		fansNumTextView.setText(user.getFansCount() == null ? "0" : user.getFansCount());
		if (!StringUtils.isEmpty(user.getBackground())) {
			if (backgroundKey != null
					&& backgroundKey.equals(user.getBackground())) {
			} else {
				backgroundKey = user.getBackground();
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + user.getBackground(), mProductHeadImg);
			}
		}
		if (!StringUtils.isEmpty(user.getIntroduction()))
			descTextView.setText(user.getIntroduction());
		else
			descTextView.setText("这个人很懒，什么都没留下！");
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + user.getAvatar(), mUserPhotoIv,
					AppConfig.options(R.drawable.user_defult_photo));

		}
	}

	@Override
	public void onClick(View v) {
		Intent it = null;
		switch (v.getId()) {
		case R.id.my_blog_layout:
			it = new Intent(getActivity(), TouristHomepageActivity.class);
			it.putExtra("userInfo", myInfo.getUserInfo());
			startActivity(it);
			animationOpen();
			break;
		case R.id.my_cashCoupon_layout:
			it = new Intent(getActivity(), CashCardActivity.class);
			startActivity(it);
			animationOpen();
			break;
			case R.id.fans:
			Intent intentFans = new Intent(getActivity(),FollowOrFansActivity.class);
			intentFans.putExtra("title", "粉丝");
			intentFans.putExtra("userId", myInfo.getUserInfo().getUserId());
			startActivity(intentFans);
			break;
		case R.id.follow:
			Intent intentFollow = new Intent(getActivity(),FollowOrFansActivity.class);
			intentFollow.putExtra("title", "关注");
			intentFollow.putExtra("userId", myInfo.getUserInfo().getUserId());
			startActivity(intentFollow);
			break;
		case R.id.my_store_layout:
			// 我的收藏
			it = new Intent(getActivity(), MyCollectionActivity.class);

			startActivity(it);
			animationOpen();
			break;
		case R.id.my_travel_layout:
			// 我的旅程
			it = new Intent(getActivity(), MyJourneyActivity.class);
			it.putExtra("state", "all");
			startActivity(it);
			animationOpen();
			break;
		// 常用信息
		case R.id.my_custominfo_layout:
			it = new Intent(getActivity(), CommonInfoActivity.class);
			startActivity(it);
			animationOpen();
			break;
		case R.id.pay_layout:
			it = new Intent(getActivity(), MyJourneyActivity.class);
			it.putExtra("state", "wait_pay");
			it.putExtra("title", "待支付");
			startActivity(it);
			animationOpen();
			break;
		case R.id.leave_layout:
			it = new Intent(getActivity(), MyJourneyActivity.class);
			it.putExtra("state", "wait_depart");
			it.putExtra("title", "待出行");
			startActivity(it);
			animationOpen();
			break;
		case R.id.evalute_layout:
			it = new Intent(getActivity(), MyJourneyActivity.class);
			it.putExtra("state", "wait_appraise");
			it.putExtra("title", "待评价");
			startActivity(it);
			animationOpen();
			break;

		case R.id.my_customize_layout:
			it = new Intent(getActivity(), PersonalCustomizationStepEnd.class);
			startActivity(it);
			animationOpen();
			break;
		case R.id.my_schedule_layout:
			it = new Intent(getActivity(), MyScheduletActivity.class);
			// it = new Intent(getActivity(), SquareOldPicActivity.class);
			startActivityForResult(it, 8188);
			animationOpen();
			PreferenceUtil.setString(PreferenceUtil.getUserId(), PreferenceUtil.getUserId());
			PreferenceUtil.setString(PreferenceUtil.getUserId()+"出行单条数", PreferenceUtil.getUserId()+myInfo.getItineraryNum());
			break;
		case R.id.my_fas_layout:
			it = new Intent(getActivity(), MyAccountActivity2.class);
			startActivity(it);
			animationOpen();
			break;
		case R.id.user_info_layout:
			it = new Intent(getActivity(), TouristHomepageActivity.class);
			it.putExtra("userInfo", myInfo.getUserInfo());
			startActivity(it);
			animationOpen();
			break;
		case R.id.change_avator_tv:
			isModifyAvar=true;
			new SelectPicDialog(getActivity(), mOperationListener).show();
			break;
		case R.id.personpic:
			isModifyAvar=false;
			CommonUtils.launchPhotoSelectorActivity(this, ADD_BACKGROUND_CODE);
			break;
		case R.id.user_edit_tv:
			// setTabSelection(2);
			Intent intent2 = new Intent(getActivity(), SettingUserInfoActivity.class);
			startActivity(intent2);
			break;
		case R.id.setting:

			Intent intent1 = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	/**
	 * 页面启动动画
	 */
	protected void animationOpen() {
		getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
	}


	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 8188) {
			myScheduleimTip.setVisibility(View.GONE);
		}
		switch (arg0) {
	case 0:
		if (arg0 == 0 && arg1 == Activity.RESULT_OK && null != arg2) {
			Uri selectedImage = arg2.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			if (null == picturePath)
				return;
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(selectedImage, "image/*");// mUri是已经选择的图片Uri
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);// 裁剪框比例
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 150);// 输出图片大小
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, 200);

		}
		break;
	case 2:
		if (null == path)
			return;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);// 输出图片大小
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 200);
		break;
	case 100:
		break;
	case 200:
		if (arg1 == Activity.RESULT_OK && arg2 != null) {
			// 拿到剪切数据
			Bitmap bmap = arg2.getParcelableExtra("data");
			ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
			bmap.compress(CompressFormat.PNG, 100, output);// 把bitmap100%高质量压缩
															// 到 output对象里

			bmap.recycle();// 自由选择是否进行回收

			byte[] result = output.toByteArray();// 转换成功了
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			getUploadTokenUrl(result);
		}
		// File file = new File(picturePath);
		// getUploadTokenUrl(file);
		break;
		case ADD_BACKGROUND_CODE:
			if (arg2 != null) {
				List<PhotoModel> photos = (List<PhotoModel>) arg2.getExtras()
						.getSerializable("photos");
				if (photos.size() > 0) {
					PhotoModel info = photos.get(0);
					List<String> uriList = new ArrayList<String>(1);
					uriList.add(info.getOriginalPath());
					uploadPicUtil.uploadPicData(uriList);
				}
			}
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save away the original text, so we still have it if the activity
		// needs to be killed while paused.
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("path", path);
		savedInstanceState.putParcelable("uri", uri);
	}
	
	OperationListener mOperationListener = new OperationListener() {
		@Override
		public void operationPhoto(int witch) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (witch) {
			case 1:
				Intent intent1 = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent1, 0);
				break;
			case 2:
				String haveSD = Environment.getExternalStorageState();
				if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getActivity(), "存储卡不可用", Toast.LENGTH_LONG).show();
					return;
				}
				File dir = new File(Environment.getExternalStorageDirectory() + "/" + "tripaway");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
				Date date = new Date();
				String str = format.format(date);
				path = Environment.getExternalStorageDirectory() + "/" + "tripaway" + "/" + str + "photo.jpg";
				intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File imgFile = new File(dir, str + "photo.jpg");
				uri = Uri.fromFile(imgFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 2);
				break;
			default:
			}
		}
	};
	/*
	 * 获取上传凭证 接口
	 */
	private void getUploadTokenUrl(final byte[] fileByte) {

		RequestParams params = new RequestParams();
		params.put("bucket", "tripaway");// 存储空间
		HttpUtil.get(Urls.getUploadToken_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					uploadToken = response.optJSONObject("data").optString("token");
					testUploadToYunCode(fileByte);
				} else {
//					ToastUtil.showToast(getActivity(), "头像修改失败");
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
//				ToastUtil.showToast(getActivity(), "头像修改失败");
			}
		});

	}
	
	/*
	 * 上传图片 到七牛云
	 */
	private void testUploadToYunCode(byte[] fileByte) {
		if (uploadToken != null) {
			TripawayApplication.uploadManager.put(fileByte, null, uploadToken, new UpCompletionHandler() {

				@Override
				public void complete(String arg0, ResponseInfo arg1, JSONObject response) {
					// TODO Auto-generated method stub
					userInfoEdit(response.optString("key"));

				}
			}, new UploadOptions(null, null, false, new UpProgressHandler() {

				@Override
				public void progress(String arg0, double arg1) {
					// TODO Auto-generated method stub
				}
			}, null));
			// TripawayApplication.uploadManager.put(file, null, uploadToken,
			// new UpCompletionHandler()
			// {
			//
			// @Override
			// public void complete(String key, ResponseInfo info, JSONObject
			// response)
			// {
			// userInfo.setAvatar(response.optString("key"));
			// userInfoEdit();
			// }
			// }, null);
		}

	}
	/**
	 * 修改个人信息接口
	 */
	private void userInfoEdit(String key) {
		try {
			JSONObject jsonObject = new JSONObject();
			if(isModifyAvar){
				myInfo.getUserInfo().setAvatar(key);
			jsonObject.put("avatar", myInfo.getUserInfo().getAvatar());}
			else{
				myInfo.getUserInfo().setBackground(key);
				jsonObject.put("background", myInfo.getUserInfo().getBackground());
			}
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.userinfo_edit_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(SettingUserInfoActivity.this, "修改失败
					// errorMessage=" + throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {
						ToastUtil.showToast(getActivity(), "修改成功");
						UserInfoDB.getInstance().insertData(myInfo.getUserInfo());
						Intent intent = new Intent("com.bcinfo.modifyUserInfo");
						getActivity().sendBroadcast(intent);
						initUserInfo(myInfo.getUserInfo());
						PreferenceUtil.setString("avatar", myInfo.getUserInfo().getAvatar());
					} else {
						// ToastUtil.showToast(SettingUserInfoActivity.this,
						// "修改失败 errorCode=" + response.optString("code"));
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initUserInfo(UserInfo userInfo) {
		if (isModifyAvar) {
			if (!StringUtils.isEmpty(userInfo.getAvatar())) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + userInfo.getAvatar(), mUserPhotoIv,
						AppConfig.options(R.drawable.user_defult_photo));
			}
		} else {
			if (!StringUtils.isEmpty(userInfo.getBackground())) {
				backgroundKey = userInfo.getBackground();
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + userInfo.getBackground(),
						mProductHeadImg);
			}
		}
	}

	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
		if (view != null && view == mProductScrollView) {

			alpha = positionY / 3;
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
		} else {
			return;
		}
	}
	
}
