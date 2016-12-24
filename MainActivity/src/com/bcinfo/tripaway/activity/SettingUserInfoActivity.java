package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog.OperationListener;
import com.bcinfo.tripaway.view.dialog.SexDialog;
import com.bcinfo.tripaway.view.dialog.SexDialog.SexSelectListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

/**
 * @author hanweipeng
 * @date 2015-7-13 下午2:42:11
 */
public class SettingUserInfoActivity extends BaseActivity implements OnClickListener, SexSelectListener {
	private final static String TAG = "SettingUserInfoActivity";

	/**
	 * 头像布局
	 */
	private RelativeLayout iconLayout;

	/**
	 * 用户头像
	 */
	private RoundImageView userIconImg;

	/**
	 * 昵称布局
	 */
	private RelativeLayout nickNameLayout;

	/**
	 * 昵称
	 */
	private TextView nickNameTxt;

	/**
	 * 账号布局
	 */
	private RelativeLayout accountLayout;

	/**
	 * 账号
	 */
	private TextView accountTxt;

	/**
	 * 真实姓名布局
	 */
	private RelativeLayout realNameLayout;

	/**
	 * 真是姓名
	 */
	private TextView realNameTxt;

	/**
	 * 证件号码布局
	 */
	private RelativeLayout credentialsLayout;

	/**
	 * 证件号码
	 */
	private TextView numberTxt;

	/**
	 * 性别布局
	 */
	private RelativeLayout sexLayout;

	/**
	 * 性别
	 */
	private TextView sexTxt;

	/**
	 * 所在城市布局
	 */
	private RelativeLayout cityLayout;

	/**
	 * 城市
	 */
	private TextView cityTxt;

	/**
	 * 自我介绍布局
	 */
	private RelativeLayout introduceLayout;

	/**
	 * 自我价绍
	 */
	private TextView introduceTxt;

	private String path = "";

	private Uri uri;

	/**
	 * 上传toaken
	 */
	private String uploadToken;

	private UserInfo userInfo;

	private RelativeLayout secondTitle;

	private RelativeLayout footprintLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			path = savedInstanceState.getString("path");
			uri = savedInstanceState.getParcelable("uri");
		}
		statisticsTitle="编辑资料";
		// setContentView(R.layout.add_travel);
		setContentView(R.layout.set_userinfo_activity);
		setSecondTitle("个人信息");
		secondTitle = (RelativeLayout) findViewById(R.id.second_title);
		secondTitle.getBackground().setAlpha(255);
		initView();
	}

	protected void initView() {
		userInfo = UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
		iconLayout = (RelativeLayout) findViewById(R.id.user_icon);
		userIconImg = (RoundImageView) findViewById(R.id.user_photo);
		nickNameLayout = (RelativeLayout) findViewById(R.id.nick_layout);
		accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
		realNameLayout = (RelativeLayout) findViewById(R.id.real_name_layout);
		credentialsLayout = (RelativeLayout) findViewById(R.id.credentials_layout);
		sexLayout = (RelativeLayout) findViewById(R.id.sex_layout);
		cityLayout = (RelativeLayout) findViewById(R.id.city_layout);
		introduceLayout = (RelativeLayout) findViewById(R.id.introduce_layout);
		nickNameTxt = (TextView) findViewById(R.id.nick_name_txt);
		accountTxt = (TextView) findViewById(R.id.account_txt);
		realNameTxt = (TextView) findViewById(R.id.real_name_txt);
		numberTxt = (TextView) findViewById(R.id.number);
		sexTxt = (TextView) findViewById(R.id.sex_txt);
		cityTxt = (TextView) findViewById(R.id.city_txt);
		introduceTxt = (TextView) findViewById(R.id.introduce_txt);
		iconLayout.setOnClickListener(this);
		nickNameLayout.setOnClickListener(this);
		realNameLayout.setOnClickListener(this);
		credentialsLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		cityLayout.setOnClickListener(this);
		introduceLayout.setOnClickListener(this);
		accountTxt.setText(PreferenceUtil.getAccount());
		initUserInfo(userInfo);
		queryUserInfo();
		footprintLayout = (RelativeLayout) findViewById(R.id.footprint_layout);
		footprintLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_icon:
			new SelectPicDialog(SettingUserInfoActivity.this, mOperationListener).show();
			break;
		case R.id.nick_layout:
			Intent nickIntent = new Intent(SettingUserInfoActivity.this, ModifyInfoActivity.class);
			nickIntent.putExtra("type", 0);
			nickIntent.putExtra("userInfo", userInfo);
			startActivityForResult(nickIntent, 101);
			activityAnimationOpen();
			break;
		case R.id.real_name_layout:
			Intent realNameIntent = new Intent(SettingUserInfoActivity.this, ModifyInfoActivity.class);
			realNameIntent.putExtra("type", 1);
			realNameIntent.putExtra("userInfo", userInfo);
			startActivityForResult(realNameIntent, 102);
			activityAnimationOpen();
			break;
		case R.id.credentials_layout:
			Intent credentialsIntent = new Intent(SettingUserInfoActivity.this, ModifyInfoActivity.class);
			credentialsIntent.putExtra("type", 2);
			credentialsIntent.putExtra("userInfo", userInfo);
			startActivityForResult(credentialsIntent, 103);
			activityAnimationOpen();
			break;
		case R.id.sex_layout:
			new SexDialog(SettingUserInfoActivity.this, this, userInfo).show();
			break;
		case R.id.city_layout:
			Intent cityIntent = new Intent(SettingUserInfoActivity.this, SelectAreaActivity.class);
			cityIntent.putExtra("areaAddress", userInfo.getAddress());
			startActivityForResult(cityIntent, 105);
			activityAnimationOpen();
			break;
		case R.id.introduce_layout:
			Intent introduceIntent = new Intent(SettingUserInfoActivity.this, ModifyInfoActivity.class);
			introduceIntent.putExtra("type", 3);
			introduceIntent.putExtra("userInfo", userInfo);
			startActivityForResult(introduceIntent, 104);
			activityAnimationOpen();
			break;
		case R.id.footprint_layout:
			Intent footprintIntent = new Intent(SettingUserInfoActivity.this, FootprintActivity.class);
			footprintIntent.putExtra("type", 3);
			footprintIntent.putExtra("userInfo", userInfo);
			startActivityForResult(footprintIntent, 104);
			activityAnimationOpen();
			break;
		default:
			break;
		}
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
					Toast.makeText(SettingUserInfoActivity.this, "存储卡不可用", Toast.LENGTH_LONG).show();
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

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save away the original text, so we still have it if the activity
		// needs to be killed while paused.
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("path", path);
		savedInstanceState.putParcelable("uri", uri);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 0:
			if (arg0 == 0 && arg1 == RESULT_OK && null != arg2) {
				Uri selectedImage = arg2.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
			if (arg1 == RESULT_OK && arg2 != null) {
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
		case 101:
			if (arg2 != null) {
				nickNameTxt.setText(arg2.getStringExtra("nickName"));
				userInfo.setNickname(arg2.getStringExtra("nickName"));
			}
			break;
		case 102:
			if (arg2 != null) {
				realNameTxt.setText(arg2.getStringExtra("realName"));
				userInfo.setRealName(arg2.getStringExtra("realName"));
			}
			break;
		case 103:
			if (arg2 != null) {
				numberTxt.setText(arg2.getStringExtra("usersIdentity"));
				userInfo.setUsersIdentity(arg2.getStringExtra("usersIdentity"));
			}
			break;
		case 104:
			if (arg2 != null) {
				introduceTxt.setText(arg2.getStringExtra("introduction"));
				userInfo.setIntroduction(arg2.getStringExtra("introduction"));
			}
			break;
		case 105:
			if (arg1 == RESULT_OK) {
				// LogUtil.i(TAG, "area", arg2.getStringExtra("area"));
				String areaAddress = arg2.getStringExtra("area");
				cityTxt.setText(areaAddress);
				userInfo.setAddress(areaAddress);
				userInfoEdit();
			}
			break;
		default:
			break;
		}
	}

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
					ToastUtil.showToast(SettingUserInfoActivity.this, "头像修改失败");
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				ToastUtil.showToast(SettingUserInfoActivity.this, "头像修改失败");
			}
		});

	}

	private void initUserInfo(UserInfo userInfo) {
		if (!StringUtils.isEmpty(userInfo.getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(), userIconImg,
					AppConfig.options(R.drawable.user_defult_photo));
		}

		nickNameTxt.setText(StringUtils.isEmpty(userInfo.getNickname()) ? "" : userInfo.getNickname());
		accountTxt.setText(StringUtils.isEmpty(PreferenceUtil.getAccount()) ? "" : PreferenceUtil.getAccount());
		realNameTxt.setText(StringUtils.isEmpty(userInfo.getRealName()) ? "" : userInfo.getRealName());
		numberTxt.setText(StringUtils.isEmpty(userInfo.getUsersIdentity()) ? "" : userInfo.getUsersIdentity());
		if (userInfo.getGender().equals("0")) {
			sexTxt.setText("女");
		} else {
			sexTxt.setText("男");
		}
		cityTxt.setText(StringUtils.isEmpty(userInfo.getAddress()) ? "" : userInfo.getAddress());
		introduceTxt.setText(StringUtils.isEmpty(userInfo.getIntroduction()) ? "" : userInfo.getIntroduction());
	}

	/**
	 * 上传头片到七牛返回的key
	 */
	private String picKey = "";

	/*
	 * 上传图片 到七牛云
	 */
	private void testUploadToYunCode(byte[] fileByte) {
		if (uploadToken != null) {
			TripawayApplication.uploadManager.put(fileByte, null, uploadToken, new UpCompletionHandler() {

				@Override
				public void complete(String arg0, ResponseInfo arg1, JSONObject response) {
					// TODO Auto-generated method stub
					userInfo.setAvatar(response.optString("key"));
					userInfoEdit();

				}
			}, new UploadOptions(null, null, false, new UpProgressHandler() {

				@Override
				public void progress(String arg0, double arg1) {
					// TODO Auto-generated method stub
					LogUtil.e(TAG, "progress", arg1 + "");
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
	 * 查询个人信息
	 */
	private void queryUserInfo() {
		HttpUtil.get(Urls.userinfo_detail_url, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				// ToastUtil.showToast(SettingUserInfoActivity.this,
				// "throwable=" + throwable.getMessage());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				// ToastUtil.showToast(SettingUserInfoActivity.this,
				// "throwable=" + throwable.getMessage());
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "获取个人信息接口", response.toString());
				if (response.optString("code").equals("00000")) {
					JSONObject dataJson = response.optJSONObject("data");
					if (dataJson != null && dataJson.length() > 0) {
						userInfo.setAvatar(dataJson.optString("avatar"));
						userInfo.setNickname(dataJson.optString("nickname"));
						userInfo.setRealName(dataJson.optString("realName"));
						userInfo.setUsersIdentity(dataJson.optString("usersIdentity"));
						userInfo.setGender(dataJson.optString("sex"));
						userInfo.setAddress(dataJson.optString("address"));
						userInfo.setAccount(dataJson.optString("usersName"));
						userInfo.setIntroduction(dataJson.optString("introduction"));
						initUserInfo(userInfo);
					}
				} else {
					// ToastUtil.showToast(SettingUserInfoActivity.this,
					// "errorCode=" + response.optString("code"));
				}
			}
		});
	}

	/**
	 * 修改个人信息接口
	 */
	private void userInfoEdit() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("avatar", userInfo.getAvatar());
			jsonObject.put("nickname", userInfo.getNickname());
			jsonObject.put("realName", userInfo.getRealName());
			jsonObject.put("usersIdentity", userInfo.getUsersIdentity());
			jsonObject.put("sex", userInfo.getGender());
			jsonObject.put("address", userInfo.getAddress());
			jsonObject.put("introduction", userInfo.getIntroduction());
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
					LogUtil.i(TAG, "设置个人信息接口", response.toString());
					if (response.optString("code").equals("00000")) {
						ToastUtil.showToast(SettingUserInfoActivity.this, "修改成功");
						UserInfoDB.getInstance().insertData(userInfo);
						Intent intent = new Intent("com.bcinfo.modifyUserInfo");
						sendBroadcast(intent);
						// UserInfo userInfo =
						// UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
						initUserInfo(userInfo);
						PreferenceUtil.setString("avatar", userInfo.getAvatar());
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

	@Override
	public void selectSex(int type) {
		// TODO Auto-generated method stub
		if (type == 1) {
			userInfo.setGender("1");
		} else if (type == 2) {
			userInfo.setGender("0");
		}
		userInfoEdit();
	}

}
