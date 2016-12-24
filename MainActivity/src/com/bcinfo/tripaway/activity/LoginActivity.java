package com.bcinfo.tripaway.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.AuthCodeUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.CustomProgressDialog;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 登录页面
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2014年12月24日 19:21:11
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = "LoginActivity";

	/**
	 * 定义三个RadioButton单选按钮
	 * 
	 */
	private RadioButton rb1;

	private RadioButton rb2;

	private RadioButton rb3;

	// private SharePreferenceUtil sharedPreferenceUtil;

	/**
	 * 登录账号输入框
	 */
	private DeletedEditText loginNameEdt;

	/**
	 * 登录密码输入框
	 */
	private DeletedEditText loginPasswordEdt;

	/**
	 * 显示验证码的imageView
	 */
	private ImageView authCodeIv;

	private LinearLayout authCodeLayout;

	private RelativeLayout the_first_title;
	/**
	 * 定义一个boolean型变量 flag
	 * 
	 */
	private boolean flag = true;

	/**
	 * 绘制验证码控件的flag标记
	 */
	private boolean isFirst = true;

	/**
	 * 验证码控件的真实宽度和高度
	 */
	private int mWidth;

	private int mHeight;

	private AuthCodeUtil authCode;

	/**
	 * 显示提示信息的toast
	 * 
	 */
	// private Toast mToast;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public RadioButton getRb1() {
		return rb1;
	}

	public void setRb1(RadioButton rb1) {
		this.rb1 = rb1;
	}

	public RadioButton getRb2() {
		return rb2;
	}

	public void setRb2(RadioButton rb2) {
		this.rb2 = rb2;
	}

	public RadioButton getRb3() {

		return rb3;
	}

	public void setRb3(RadioButton rb3) {
		this.rb3 = rb3;
	}

	// private SharePreferenceUtil spUtil;

	/**
	 * 定义 登录按钮
	 */
	private LinearLayout loginLayout;

	/**
	 * 定义 忘记密码 控件
	 */
	private TextView forgetPassWord;

	/**
	 * 定义一个继承Dialog的自定义对话框子类
	 * 
	 */

	private CustomProgressDialog loginDialog;

	public CustomProgressDialog getLoginDialog() {
		return loginDialog;
	}

	public void setLoginDialog(CustomProgressDialog loginDialog) {
		this.loginDialog = loginDialog;
	}

	private String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		statisticsTitle = "登录";
		this.initFirstTitle(true, false, true);
		AppManager.getAppManager().addActivity(this);
		if (getIntent().getStringExtra("tag") == null) {
			tag = "1";
		} else {
			tag = getIntent().getStringExtra("tag");
		}
		loginNameEdt = (DeletedEditText) findViewById(R.id.loginName);
		loginPasswordEdt = (DeletedEditText) findViewById(R.id.loginPassWord);
		the_first_title = (RelativeLayout) findViewById(R.id.the_first_title);
		the_first_title.getBackground().setAlpha(255);
		regTextView.setOnClickListener(mOnClickListener);
		authCode = AuthCodeUtil.getInstance(this);
		init();

		/**
		 * 
		 * 下面定义重置密码成功时显示的toast提示 (先放在oncreate()方法中)
		 * 
		 */
		// View resetToast = inflator.inflate(R.layout.error_toast, null);
		// TextView tvToast = (TextView) resetToast.findViewById(R.id.hintInfo);
		// tvToast.setText(R.string.successInfo);
		// mToast = this.showToast(resetToast);
		// mToast.setDuration(Toast.LENGTH_LONG);// 持续时间
		// mToast.setGravity(Gravity.TOP, (int)
		// this.getResources().getDimension(R.dimen.login_toast_positionX),
		// (int) getResources().getDimension(R.dimen.login_toast_positionY));//
		// 位置
		// // -10
		// // 190
		//
		// mToast.show();
	}

	private OnPreDrawListener onPreDrawlistener = new OnPreDrawListener() {

		@Override
		public boolean onPreDraw() {
			if (isFirst) {
				isFirst = false;

				mWidth = DensityUtil.dip2px(LoginActivity.this, 160);
				mHeight = DensityUtil.dip2px(LoginActivity.this, 45);

				authCodeIv.setImageBitmap(authCode.createBitmap(mWidth, mHeight));
			}
			return true;
		}

	};

	/*
	 * 测试 游客端 登录接口的方法
	 */
	private void testClientLoginUrl(final String loginAccount, String loginPassword) {
		try {
			MCryptUtil mcryptUtil = new MCryptUtil();
			// spUtil = Constant.getInstance().getSpUtil();
			String clientId = PreferenceUtil.getClientId();
			System.out.println("这个id=" + clientId);
			// RequestParams params = new RequestParams();
			// params.put("clientid", clientId);
			// params.put("account", loginAccount);
			// params.put("password", mcryptUtil.Encrypt(loginPassword));
			// params.put("type", "Client");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("clientid", clientId);
			jsonObject.put("account", loginAccount);
			jsonObject.put("password", mcryptUtil.Encrypt(loginPassword));
			jsonObject.put("type", "Client");
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			LogUtil.d(TAG, "testClientLoginUrl", stringEntity.toString());
			HttpUtil.post(Urls.loginUrl, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					LogUtil.d(TAG, "testClientLoginUrl", response.toString());
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {

						UserInfo myInfo = new UserInfo();

						JSONObject valueObj = response.optJSONObject("data");
						PreferenceUtil.setAccount(loginAccount);
						PreferenceUtil.setUserId(valueObj.optJSONObject("userInfo").optString("userId")); // 向preferences.xml中写入userId
						PreferenceUtil.setToken(valueObj.optString("token"));// 向preferences.xml写入token
						PreferenceUtil.setSession(valueObj.optString("session"));// 向preferences.xml写入session
						PreferenceUtil.setString("avatar", valueObj.optJSONObject("userInfo").optString("avatar"));
						myInfo.setUserId(valueObj.optJSONObject("userInfo").optString("userId"));// 设置个人id
						myInfo.setAvatar(valueObj.optJSONObject("userInfo").optString("avatar"));// 用户头像
						myInfo.setNickname(valueObj.optJSONObject("userInfo").optString("nickName")); // 用户昵称
						myInfo.setRealName(valueObj.optJSONObject("userInfo").optString("realName")); // 用户昵称
						myInfo.setMobile(valueObj.optJSONObject("userInfo").optString("mobile")); // 用户手机号
						myInfo.setEmail(valueObj.optJSONObject("userInfo").optString("email")); // 用户email
						myInfo.setStatus(valueObj.optJSONObject("userInfo").optString("status"));
						myInfo.setGender(valueObj.optJSONObject("userInfo").optString("sex"));// 用户性别
						myInfo.setIntroduction(valueObj.optJSONObject("userInfo").optString("introduction")); // 用户介绍
						myInfo.setAddress(valueObj.optJSONObject("userInfo").optString("address")); // 地址
						myInfo.setUsersIdentity(valueObj.optJSONObject("userInfo").optString("usersIdentity")); // 身份证
						UserInfoDB.getInstance().insertData(myInfo);
						// Intent intentForMain = new Intent(LoginActivity.this,
						// MainActivity.class);
						// intentForMain.putExtra("userInfoValue", myInfo);
						// intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// intentForMain.putExtra("unreadMsgCount",
						// String.valueOf(response.optJSONObject("data").opt("msgCount")));
						Intent intent = new Intent("com.bcinfo.haveLogin");
						sendBroadcast(intent);
						// setResult(200, intentForMain);
						// startActivity(intentForMain);
						if ("myInfo".equals(tag)) {
							setResult(1005);
							finish();
						} else if ("messAge".equals(tag)) {
							setResult(1004);
							finish();
						}
						finish();
						activityAnimationClose();
						// testUnReadMsgUrl(myInfo);

					} else if ("00009".equals(response.optString("code"))) {
						ToastUtil.showErrorToast(LoginActivity.this, getString(R.string.errorInfo));
					} else {
						ToastUtil.showErrorToast(LoginActivity.this,
								getString(R.string.login_fail) + ":" + response.optString("code"));
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
					ToastUtil.showErrorToast(LoginActivity.this, getString(R.string.login_fail) + ":" + statusCode);
					throwable.printStackTrace();
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/*
	 * 测试 未读消息接口
	 */
	private void testUnReadMsgUrl(final UserInfo userInfo) {
		HttpUtil.get(Urls.unreadMessage_url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				System.out.println("未读消息=" + response);
				if ("00000".equals(response.optString("code"))) {
					Intent intentForMain = new Intent(LoginActivity.this, MainActivity.class);
					intentForMain.putExtra("userInfoValue", userInfo);
					intentForMain.putExtra("unreadMsgCount",
							String.valueOf(response.optJSONObject("data").opt("msgCount")));
					setResult(200, intentForMain);
					finish();
					activityAnimationClose();

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("未读消息=" + responseString);
			}
		});
	}

	/**
	 * 初始化View
	 * 
	 */
	public void init() {

		authCodeIv = (ImageView) findViewById(R.id.authCodeIv);
		authCodeLayout = (LinearLayout) findViewById(R.id.authCodeLayout);
		authCodeIv.getViewTreeObserver().addOnPreDrawListener(onPreDrawlistener);
		// 为验证码图片控件添加点击方法
		authCodeIv.setOnClickListener(this);
		loginTextView = (TextView) findViewById(R.id.login);
		// regTextView = (TextView) findViewById(R.id.register);
		rb1 = (RadioButton) findViewById(R.id.weiboLogin);
		rb2 = (RadioButton) findViewById(R.id.qqLogin);
		rb3 = (RadioButton) findViewById(R.id.twitterLogin);
		loginLayout = (LinearLayout) findViewById(R.id.submitBtn);
		this.forgetPassWord = (TextView) findViewById(R.id.forgetPassWord);

		loginLayout.setOnClickListener(this);

		forgetPassWord.setOnClickListener(this);
		loginTextView.setOnClickListener(this);
		// regTextView.setOnClickListener(this);

		// viewList.add(registerView);

	}

	@Override
	public void onClick(View v) {

		/**
		 * switch 根据ID判断点击的是哪一个控件
		 */
		switch (v.getId()) {
		case R.id.forgetPassWord:

			Intent findIntent = new Intent();
			findIntent.setClass(this, FindPassWordActivity.class);
			startActivity(findIntent);
			activityAnimationOpen();
			break;

		case R.id.submitBtn:
			final String loginAccount = loginNameEdt.getText().toString().trim();
			final String loginPassword = loginPasswordEdt.getText().toString().trim();
			if (TextUtils.isEmpty(loginAccount)) {
				ToastUtil.showToast(this, "登录账号不能为空!");
				return;
			}
			if (TextUtils.isEmpty(loginPassword)) {
				ToastUtil.showToast(this, "登录密码不能为空!");
				return;
			}
			if (verifyIsPhone(loginAccount) || verifyIsEmail(loginAccount)) // 判断登录账号是否是手机或邮箱
			{
				testClientLoginUrl(loginAccount, loginPassword);

			} else {
				ToastUtil.showToast(this, "登录账号不合法!");
				return;
			}
			// Intent i=new Intent(LoginActivity.this,MainActivity.class);
			// i.putExtra("fragmentId", 7);
			// startActivity(i);
			break;

		case R.id.authCodeIv: // 点击验证码

			authCodeIv.setImageBitmap(authCode.createBitmap(mWidth, mHeight));
			break;
		default:
			break;
		}
	}

	/**
	 * 用户的输入是否为email的方法
	 */
	public static boolean verifyIsEmail(String accountStr) {
		Pattern pattern = Pattern
				.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		Matcher matcher = pattern.matcher(accountStr);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用户输入是否为手机号码的方法
	 */
	public static boolean verifyIsPhone(String accountStr) {
		Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13\\d{9}|14[57]\\d{8}|15\\d{9}|18\\d{9}$");
		Matcher matcher = pattern.matcher(accountStr);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

}
