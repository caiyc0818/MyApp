package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.db.PushFlashDB;
import com.bcinfo.tripaway.fragment.CustomizeRequireFragment;
import com.bcinfo.tripaway.fragment.Fragment1;
import com.bcinfo.tripaway.fragment.Fragment2;
import com.bcinfo.tripaway.fragment.FragmentFactory;
import com.bcinfo.tripaway.fragment.HomeFragment;
import com.bcinfo.tripaway.fragment.MessageFragment;
import com.bcinfo.tripaway.fragment.MicroBlogsNewFragment;
import com.bcinfo.tripaway.fragment.MyInfoFragment;
import com.bcinfo.tripaway.fragment.MyTravelOrderFragment;
import com.bcinfo.tripaway.fragment.PickedFragment;
import com.bcinfo.tripaway.fragment.SettingFragment;
import com.bcinfo.tripaway.getui.receiver.PushDemoReceiver;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.UpdateCilent;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 远行主页
 * 
 * 
 * 底部 菜单栏 为main2 侧边为main
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener{
	private static final String TAG = "MainActivity2";
	private RadioGroup rg;
	private RadioButton lastButton;
	private RadioGroup mic_radio;
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	/**
	 * fragment管理类
	 */
	private FragmentFactory fragmentMgr = FragmentFactory.getInstance();
	private RadioButton ra1;
	private RadioButton ra2;
	FragmentManager manager;
	/**
	 * 标题文字
	 */
	private TextView titleTxt;

	private TextView my_microtravel;

	private TextView setting;

	private PickedFragment pickedFragment;
	
	private HomeFragment homeFragment;

//	private LocateDestinationFragment locateDestinationFragment;

	private Fragment1 fragment1;
	private Fragment2 fragment2;

	private MicroBlogsNewFragment microBlogsNewFragment;

	private SettingFragment settingFragment;

	private MyTravelOrderFragment myTravelOrderFragment;

	private MessageFragment messageFragment;

	private MyInfoFragment myInfoFragment;

	private CustomizeRequireFragment customizeRequireFragment;

	/**
	 * 版本号
	 */
	private int versionCode;

	/**
	 * 版本名
	 */
	private String versionName;

	/**
	 * 包名
	 */
	private String packageName;

	public boolean isNewVersion = false;

	private ImageView titleImg;

	private ImageView titleImg1;

	private ImageView releaseBtn;

	private MainActivity mainActivity;

	public boolean tag = true;
	private Button radio0, radio1, radio2, radio3, radio4;
	/**
	 * 红点布局
	 */
	private FrameLayout hintLayout;

	/**
	 * 红点提示文字
	 */
	private TextView hintTxt;
	// 精选页的定制入口
	private TextView pick;
	private RelativeLayout titleLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button_activity_main);
		titleLayout = (RelativeLayout) findViewById(R.id.main_title_layout);
		titleLayout.setAlpha(1);
		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		getPushFlash();
		// setBehindContentView(R.layout.menu_frame);
		mic_radio = (RadioGroup) findViewById(R.id.squre_rg);
		radio0 = (Button) findViewById(R.id.radio0);
		radio1 = (Button) findViewById(R.id.radio1);
		radio2 = (Button) findViewById(R.id.radio2);
		radio3 = (Button) findViewById(R.id.radio3);
		radio4 = (Button) findViewById(R.id.radio4);
		radio0.setTypeface(TripawayApplication.normalTf);
		radio1.setTypeface(TripawayApplication.normalTf);
		radio2.setTypeface(TripawayApplication.normalTf);
		radio3.setTypeface(TripawayApplication.normalTf);
		radio4.setTypeface(TripawayApplication.normalTf);
		hintLayout = (FrameLayout) findViewById(R.id.hint_layout);
		hintTxt = (TextView) findViewById(R.id.count_txt);
		pick = (TextView) findViewById(R.id.pick);
		pick.setOnClickListener(this);
		radio0.setOnClickListener(this);
		radio1.setOnClickListener(this);
		radio2.setOnClickListener(this);
		radio3.setOnClickListener(this);
		radio4.setOnClickListener(this);
		ra1 = (RadioButton) findViewById(R.id.radio0_frag);
		ra2 = (RadioButton) findViewById(R.id.radio1_frag);
		titleTxt = (TextView) findViewById(R.id.main_title_text);
		titleTxt.setVisibility(View.GONE);
		my_microtravel = (TextView) findViewById(R.id.my_microtravel);
		setting = (TextView) findViewById(R.id.setting);
		setting.setOnClickListener(this);
		releaseBtn = (ImageView) findViewById(R.id.release_button);
		releaseBtn.setOnClickListener(this);
		my_microtravel.setVisibility(View.GONE);
		titleImg = (ImageView) findViewById(R.id.title_logo);
		titleImg1 = (ImageView) findViewById(R.id.title_logo1);
		titleImg1.setVisibility(View.VISIBLE);
		titleImg.setVisibility(View.GONE);
		pick.setVisibility(View.GONE);
		initGeTui();
		registerBoradcastReceiver();
		checkVersion();
		setTabSelection(FragmentFactory.DISCOVER);
		mic_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio0_frag:
					setTabSelection(FragmentFactory.NEWMICROTRAVEL);
					break;
				case R.id.radio1_frag:
					if (!AppInfo.getIsLogin()) {
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(intent);
						ra1.setChecked(true);
						return;
					}
					setTabSelection(FragmentFactory.NEWMICROTRAVEL);
					break;

				default:
					break;
				}
			}
		});
		PushDemoReceiver.ehList.add(mEventHandler);
	}
	
	private PickInterface pickInterface = new PickInterface() {
		
		@Override
		public void setTabSelection1(int index) {
			// TODO Auto-generated method stub
			setTabSelection(index);
		}
	};
	
	public interface PickInterface{
		public void setTabSelection1(int index);
	}
	public void setTabSelection(int index) {
		setting.setVisibility(View.INVISIBLE);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case FragmentFactory.PICK:
			radio1.setSelected(true);
			radio0.setSelected(false);
			radio2.setSelected(false);
			radio3.setSelected(false);
			radio4.setSelected(false);
			titleLayout.setVisibility(View.VISIBLE);
			releaseBtn.setVisibility(View.GONE);
			my_microtravel.setVisibility(View.GONE);
			mic_radio.setVisibility(View.GONE);
			titleTxt.setVisibility(View.GONE);
			titleImg.setVisibility(View.VISIBLE);
			pick.setVisibility(View.VISIBLE);
			titleImg1.setVisibility(View.GONE);
			if (pickedFragment == null) {
				pickedFragment = (PickedFragment) fragmentMgr.getFragmentInCache(index);
				transaction.add(R.id.content_frame, pickedFragment);
			} else {
				transaction.show(pickedFragment);
			}			
			break;
		case FragmentFactory.DISCOVER:
			radio4.setSelected(false);
			radio1.setSelected(false);
			radio2.setSelected(false);
			radio3.setSelected(false);
			radio0.setSelected(true);
			titleLayout.setVisibility(View.GONE);
			releaseBtn.setVisibility(View.GONE);
			mic_radio.setVisibility(View.GONE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setVisibility(View.GONE);
			titleImg1.setVisibility(View.VISIBLE);
			titleImg.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
//			if (locateDestinationFragment == null) {
//				locateDestinationFragment = (LocateDestinationFragment) fragmentMgr.getFragmentInCache(index);
//				transaction.add(R.id.content_frame, locateDestinationFragment);
//			} else {
//				transaction.show(locateDestinationFragment);
//			}
			if (homeFragment == null) {
				homeFragment = (HomeFragment) fragmentMgr.getFragmentInCache(index);
				homeFragment.setPickInterface(pickInterface);
				transaction.add(R.id.content_frame, homeFragment);
				
			} else {
				transaction.show(homeFragment);
			}
			break;
		case FragmentFactory.MESSAGE:
			radio3.setSelected(true);
			radio1.setSelected(false);
			radio2.setSelected(false);
			radio0.setSelected(false);
			radio4.setSelected(false);
			titleLayout.setVisibility(View.VISIBLE);
			releaseBtn.setVisibility(View.GONE);
			mic_radio.setVisibility(View.GONE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setText("消息");
			titleTxt.setVisibility(View.VISIBLE);
			titleImg.setVisibility(View.GONE);
			titleImg1.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
			if (messageFragment == null) {
				messageFragment = (MessageFragment) fragmentMgr.getFragmentInCache(index);
				transaction.add(R.id.content_frame, messageFragment);
			} else {
				transaction.show(messageFragment);
			}
			break;
		case FragmentFactory.ORDER:
			titleLayout.setVisibility(View.VISIBLE);
			releaseBtn.setVisibility(View.GONE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setText("我的旅程");
			mic_radio.setVisibility(View.GONE);
			titleTxt.setVisibility(View.VISIBLE);
			titleImg.setVisibility(View.GONE);
			titleImg1.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
			if (myTravelOrderFragment == null) {
				myTravelOrderFragment = (MyTravelOrderFragment) fragmentMgr.getFragmentInCache(index);
				transaction.add(R.id.content_frame, myTravelOrderFragment);
			} else {
				transaction.show(myTravelOrderFragment);
			}
			break;
		case FragmentFactory.SETTING:
			titleLayout.setVisibility(View.VISIBLE);
			releaseBtn.setVisibility(View.GONE);
			mic_radio.setVisibility(View.GONE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setText("设置");
			titleTxt.setVisibility(View.VISIBLE);
			titleImg.setVisibility(View.GONE);
			titleImg1.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
			if (settingFragment == null) {
				settingFragment = (SettingFragment) fragmentMgr.getFragmentInCache(index);
				transaction.add(R.id.content_frame, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;

		case FragmentFactory.MYINFO:
			radio4.setSelected(true);
			radio1.setSelected(false);
			radio2.setSelected(false);
			radio3.setSelected(false);
			radio0.setSelected(false);
			titleLayout.setVisibility(View.GONE);
			mic_radio.setVisibility(View.GONE);
			setting.setVisibility(View.VISIBLE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setVisibility(View.VISIBLE);
			titleImg.setVisibility(View.GONE);
			titleImg1.setVisibility(View.GONE);
			releaseBtn.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
			titleTxt.setText("");
			if (myInfoFragment == null) {
				myInfoFragment = (MyInfoFragment) fragmentMgr.getFragmentInCache(index);
				transaction.add(R.id.content_frame, myInfoFragment);
			} else {
				transaction.show(myInfoFragment);
			}

			break;
		// case FragmentFactory.CUSTOMIZE:
		// titleLayout.setVisibility(View.VISIBLE);
		// if (!AppInfo.getIsLogin()) {
		// Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		// startActivity(intent);
		//
		// return;
		// }
		// releaseBtn.setVisibility(View.GONE);
		// mic_radio.setVisibility(View.GONE);
		// my_microtravel.setVisibility(View.GONE);
		// titleTxt.setVisibility(View.VISIBLE);
		// titleImg.setVisibility(View.GONE);
		// titleImg1.setVisibility(View.GONE);
		// titleTxt.setText("发布定制");
		// if (customizeRequireFragment == null) {
		// customizeRequireFragment = (CustomizeRequireFragment)
		// fragmentMgr.getFragmentInCache(index);
		// transaction.add(R.id.content_frame, customizeRequireFragment);
		// } else {
		// transaction.show(customizeRequireFragment);
		// }
		// break;
		case FragmentFactory.NEWMICROTRAVEL:
			radio4.setSelected(false);
			radio1.setSelected(false);
			radio0.setSelected(false);
			radio3.setSelected(false);
			radio2.setSelected(true);
			titleLayout.setVisibility(View.VISIBLE);
			my_microtravel.setVisibility(View.GONE);
			titleTxt.setVisibility(View.GONE);
			titleImg.setVisibility(View.GONE);
			titleImg1.setVisibility(View.GONE);
			pick.setVisibility(View.GONE);
			// titleTxt.setText("微游记");
			mic_radio.setVisibility(View.VISIBLE);
			releaseBtn.setVisibility(View.VISIBLE);
			ra1.setTypeface(TripawayApplication.normalTf);
			ra2.setTypeface(TripawayApplication.normalTf);
			if (!AppInfo.getIsLogin() && tag == false) {
				ra1.setChecked(true);
			}
			init_date(transaction);
			// if (microBlogsNewFragment == null) {
			// microBlogsNewFragment = (MicroBlogsNewFragment)
			// fragmentMgr.getFragmentInCache(index);
			// transaction.add(R.id.content_frame, microBlogsNewFragment);
			// } else {
			// transaction.show(microBlogsNewFragment);
			// }
			break;
		default:
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	// 默认为第一个切换
	private void init_date(final FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (ra1.isChecked()) {
			tag = true;
			if (fragment1 == null) {
				fragment1 = (Fragment1) fragmentMgr.getFragmentInCache(8);
				transaction.add(R.id.content_frame, fragment1);

			} else {
				if (fragment2 != null) {
					transaction.hide(fragment2);
				}

				transaction.show(fragment1);

			}
		}
		if (ra2.isChecked()) {
			tag = false;
			if (fragment2 == null) {
				fragment2 = (Fragment2) fragmentMgr.getFragmentInCache(9);
				transaction.add(R.id.content_frame, fragment2);
			} else {
				transaction.hide(fragment1);
				transaction.show(fragment2);
			}

		}
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {

		if (pickedFragment != null) {
			transaction.hide(pickedFragment);
		}
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
//		if (locateDestinationFragment != null) {
//			transaction.hide(locateDestinationFragment);
//		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
		if (myTravelOrderFragment != null) {
			transaction.hide(myTravelOrderFragment);
		}
		if (messageFragment != null) {
			transaction.hide(messageFragment);
		}
		if (fragment2 != null) {
			transaction.hide(fragment2);
		}
		if (fragment1 != null) {
			transaction.hide(fragment1);
		}
		if (microBlogsNewFragment != null) {
			transaction.hide(microBlogsNewFragment);
		}
		if (myInfoFragment != null) {
			transaction.hide(myInfoFragment);
		}
		// if (customizeRequireFragment != null) {
		// transaction.hide(customizeRequireFragment);
		// }
	}

	/**
	 * 检查版本更新
	 */
	private void checkVersion() {
		getVersionInfo();
		ApplicationInfo appInfo = null;
		try {
			appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String channel = appInfo.metaData.getString("BaiduMobAd_CHANNEL", "official");
		checkUpdateVersionUrl(channel);
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getVersionInfo() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionCode = info.versionCode;
			versionName = info.versionName;
			packageName = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 测试 查询最新版本
	 */
	private void checkUpdateVersionUrl(String channel) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("appid", packageName);
			jsonObject.put("channel", channel);
			jsonObject.put("ver", versionCode);
			System.out.println("jsonObject=" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.version_update_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(MainActivity2.this,
					// throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
					// ToastUtil.showToast(MainActivity2.this,
					// throwable.getMessage());
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					LogUtil.e("SettingUpdateVersionActivity", "checkUpdateVersionUrl", response.toString());
					if (response.optString("code").equals("00080") || response.optString("code").equals("00083")) {
						isNewVersion = false;
					} else {
						isNewVersion = true;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("code", response.optString("code"));
					JSONObject dataJson = response.optJSONObject("data");
					if (dataJson != null && dataJson.length() > 0) {
						map.put("versionCode", dataJson.optString("versionCode"));
						map.put("versionName", dataJson.optString("versionName"));
						map.put("url", dataJson.optString("url"));
						map.put("info", dataJson.optString("info"));
						if (PreferenceUtil.getBoolean(
								dataJson.optString("versionCode") + "," + dataJson.optString("versionName"))) {
							return;
						}
					}
					UpdateCilent.getUpdateClient().showDialog(MainActivity.this, map, false, handler);
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

		}
	};

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onRestart() {
		// int id =getIntent().getIntExtra("fragmentId", 0);
		// setTabSelection(id);
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
		PushDemoReceiver.ehList.remove(mEventHandler);
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.ToMyTravelOrderFragment");
		myIntentFilter.addAction("com.bcinfo.CustomizationFragment");
		myIntentFilter.addAction("com.bcinfo.haveLogin");
		myIntentFilter.addAction("com.bcinfo.clearLogin");
		myIntentFilter.addAction("com.bcinfo.publishBlog");
		myIntentFilter.addAction("com.bcinfo.refreshUnread");
		myIntentFilter.addAction("com.bcinfo.CUSTOMIZE");

		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.bcinfo.ToMyTravelOrderFragment")) {
				setTabSelection(FragmentFactory.ORDER);
			}
			if (action.equals("com.bcinfo.clearLogin")) {
				hintLayout.setVisibility(View.GONE);
			}
			if (action.equals("com.bcinfo.CustomizationFragment")) {
				// if (menuFragment != null) {
				//
				// menuFragment.setonClick(FragmentFactory.CUSTOMIZE);
				// }

			}
			if (action.equals("com.bcinfo.haveLogin")) {
				testUnReadMsgUrl();
			}
			if (action.equals("com.bcinfo.clearLogin")) {
				if (tag == false) {
					ra1.setChecked(true);
				}
				setTabSelection(FragmentFactory.DISCOVER);
			}
			if (action.equals("com.bcinfo.publishBlog")) {
				if (tag == false) {
					ra1.setChecked(true);
				}
				setTabSelection(FragmentFactory.NEWMICROTRAVEL);
			}
			if (action.equals("com.bcinfo.refreshUnread")) {
				testUnReadMsgUrl();
			}
			// if (action.equals("com.bcinfo.CUSTOMIZE")) {
			// setTabSelection(FragmentFactory.CUSTOMIZE);
			// }

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != 3 && 1 == resultCode) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			// transaction.remove(fragment1);
			// transaction.remove(Fragment);
			transaction.commitAllowingStateLoss();
			// fragment1 = null;
			// microBlogsNewFragment= null;
			// setTabSelection(FragmentFactory.MICROTRAVEL);
		}
		if (requestCode == 1004 & resultCode == 1004) {
			if (!AppInfo.getIsLogin()) {
				Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
				intent2.putExtra("tag", "messAge");
				startActivityForResult(intent2, 1004);
				return;
			}
			setTabSelection(FragmentFactory.MESSAGE);
		}
		if (requestCode == 1005 & resultCode == 1005) {
			if (!AppInfo.getIsLogin()) {
				Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
				intent2.putExtra("tag", "myInfo");
				startActivityForResult(intent2, 1005);
				return;
			}
			setTabSelection(FragmentFactory.MYINFO);
		}

		// if(arg0==3&&1 == arg1){
		// FragmentTransaction transaction =
		// getSupportFragmentManager().beginTransaction();
		// transaction.remove(microBlogsNewFragment);
		//// transaction.remove(microBlogsNewFragment);
		// transaction.commitAllowingStateLoss();
		// microBlogsNewFragment= null;
		//// microBlogsNewFragment= null;
		// setTabSelection(FragmentFactory.NEWMICROTRAVEL);
		// }
	}

	/**
	 * 初始化个推聊天
	 */
	private void initGeTui() {
		// Constant.getInstance().initData(getApplication());
		PushManager.getInstance().initialize(this.getApplicationContext()); // 初始化个推推送
	}

	private void getPushFlash() {
		HttpUtil.get(Urls.push_flash_url, new JsonHttpResponseHandler() {
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
					List<PushResource> pushResourceList = new ArrayList<PushResource>();
					if (dataArray != null && dataArray.length() > 0) {
						PushFlashDB.getInstance().deleteAll();
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataJson = dataArray.optJSONObject(i);
							PushResource pushResource = new PushResource();
							pushResource.setId(dataJson.optString("id"));
							pushResource.setResTitle(dataJson.optString("resTitle"));
							pushResource.setSubTitle(dataJson.optString("subTitle"));
							pushResource.setTitleImage(dataJson.optString("titleImage"));
							pushResource.setObjectType(dataJson.optString("objectType"));
							pushResource.setObjectId(dataJson.optString("objectId"));
							pushResource.setObject(dataJson.optString("object"));
							pushResourceList.add(pushResource);
							PushFlashDB.getInstance().insertData(pushResource);
						}
					}
					for (int i = 0; i < pushResourceList.size(); i++) {
						if (!StringUtils.isEmpty(pushResourceList.get(i).getTitleImage())) {
							ImageLoader.getInstance().loadImage(Urls.imgHost + pushResourceList.get(i).getTitleImage(),
									new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String arg0, View arg1) {
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								}

								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
								}
							});
						}

						// ImageLoader.getInstance().loadImageSync(Urls.imgHost
						// +
						// pushResourceList.get(i).getTitleImage());
					}
				}
			}
		});
	}

	/**
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			confirmExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 二次退出标识
	 */
	private boolean isExit = false;;

	/**
	 * 退出handle
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 确认退出
	 */
	private void confirmExit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(this, "再按一次返回键关闭程序", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			exit();
		}
	}

	/**
	 * 退出
	 */
	private void exit() {
		System.out.println("yet no");
		System.exit(0);
	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// // TODO Auto-generated method stub
	// super.onNewIntent(intent);
	// String action = intent.getAction();
	// if (action != null && action.equals("com.bcinfo.CustomizationFragment"))
	// {
	// if (customizeRequireFragment != null)
	// customizeRequireFragment.initData();
	// setTabSelection(FragmentFactory.CUSTOMIZE);
	//
	// }
	// if (action != null && action.equals("com.bcinfo.haveLogin")) {
	//
	// setTabSelection(FragmentFactory.MYINFO);
	//
	// }
	// }

	private PopupWindow pw;

	private int screenW;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pick: {
			if (!AppInfo.getIsLogin()) {
				Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent2);
				return;
			}
			Intent intent = new Intent(MainActivity.this, CustomizeRequireFragment.class);
			MainActivity.this.startActivityForResult(intent, 1002);
			break;
		}
		case R.id.radio0: {
			setTabSelection(FragmentFactory.DISCOVER);
			break;
		}
		case R.id.radio1: {
			setTabSelection(FragmentFactory.PICK);
			break;
		}
		case R.id.radio2: {
			setTabSelection(FragmentFactory.NEWMICROTRAVEL);
			break;
		}
		case R.id.radio3: {
			if (!AppInfo.getIsLogin()) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.putExtra("tag", "messAge");
				startActivityForResult(intent, 1004);
				return;
			}
			setTabSelection(FragmentFactory.MESSAGE);
			break;
		}
		case R.id.radio4: {
			if (!AppInfo.getIsLogin()) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.putExtra("tag", "myInfo");
				startActivityForResult(intent, 1005);
				return;
			}
			setTabSelection(FragmentFactory.MYINFO);
			break;
		}
		case R.id.my_microtravel:

			Intent intent = new Intent(MainActivity.this, MyMicroBlogActivity.class);
			startActivityForResult(intent, 1);

			break;
		case R.id.setting:

			Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent1);
			break;

		case R.id.release_button:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(it);
				return;
			}
			// 获取屏幕宽度
			DisplayMetrics metrics = new DisplayMetrics();
			MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			screenW = metrics.widthPixels;
			View convView = LayoutInflater.from(MainActivity.this).inflate(R.layout.shaitupopuwindow, null);
			int gap = DensityUtil.dip2px(MainActivity.this, 30);
			pw = new PopupWindow(convView, screenW - 2 * gap, LayoutParams.WRAP_CONTENT);
			// 设置pw中的控件可点击
			pw.setFocusable(true);
			// 调用展示方法，设置展示位置
			/*
			 * 在展示pw的同时，让窗口的其余部分显示半透明的颜色
			 */
			WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
			params.alpha = 0.6f;
			MainActivity.this.getWindow().setAttributes(params);

			// 设置pw可以在点击外部区域时关闭显示
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.setOutsideTouchable(true);

			// 设置pw弹出和隐藏时的动画效果
			/*
			 * 注意：此处的动画效果是通过style样式指定的
			 */
			// pw.setAnimationStyle(R.style.pw_anim_style);

			// 展示对话框，并设置展示位置
			pw.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
			// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
			pw.setOnDismissListener(new OnDismissListener() {
				public void onDismiss() {
					// TODO Auto-generated method stub
					// 将窗口颜色调回完全透明
					WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
					params.alpha = 1;
					MainActivity.this.getWindow().setAttributes(params);
				}
			});

			// 设置pw中button的点击事件
			TextView photo = (TextView) convView.findViewById(R.id.photo);
			photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent3 = new Intent(MainActivity.this, SquarePicPublishedActivity.class);
					intent3.putExtra("tag", "square");
					startActivity(intent3);
					pw.dismiss();
				}
			});
			LinearLayout series = (LinearLayout) convView.findViewById(R.id.series);
			series.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					Intent intent3 = new Intent(MainActivity.this, SquareSerialPublishedActivity.class);
					intent3.putExtra("tag", "square");
					startActivity(intent3);
					pw.dismiss();
				}
			});

			// Intent intent3 = new Intent(MainActivity2.this,
			// MicroBlogPublishedActivity.class);
			// startActivity(intent3);
			break;
		default:
			break;
		}
	}

	/*
	 * 测试 未读消息接口
	 */
	private void testUnReadMsgUrl() {
		HttpUtil.get(Urls.unreadMessage_url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				System.out.println("未读消息=" + response);
				if ("00000".equals(response.optString("code"))) {
					if (!(response.optJSONObject("data").optString("msgCount")).equals("0")) {
						hintLayout.setVisibility(View.VISIBLE);
						if (Integer.parseInt(response.optJSONObject("data").optString("msgCount")) > 99) {
							hintTxt.setText("99+");
						} else {
							hintTxt.setText(response.optJSONObject("data").optString("msgCount"));
						}
						hintTxt.setText(response.optJSONObject("data").optString("msgCount"));
					} else {
						hintLayout.setVisibility(View.GONE);
					}
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
	 * 事件监听EventHandler
	 */
	private PushDemoReceiver.EventHandler mEventHandler = new PushDemoReceiver.EventHandler() {
		@Override
		public void onMessage(String message) {
			// TODO Auto-generated method stub
			// Message handlerMsg = mHandler.obtainMessage(0x000);
			// handlerMsg.obj = message;
			// mHandler.sendMessage(handlerMsg);
			testUnReadMsgUrl();
		}

		@Override
		public void onNotify(String title, String content) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onNetChange(boolean isNetConnected) {
			// TODO Auto-generated method stub
		}
	};

}
