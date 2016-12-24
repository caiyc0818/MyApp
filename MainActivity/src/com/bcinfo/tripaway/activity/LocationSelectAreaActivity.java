package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.LocationAreaAdapter;
import com.bcinfo.tripaway.adapter.LocationAreaAdapter1;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.HanziToPinyin;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.SideBar;
import com.bcinfo.tripaway.view.SideBar.OnTouchingLetterChangedListener;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.wefika.flowlayout.FlowLayout;

/**
 * @author hanweipeng
 * @date 2015-7-14 上午10:00:17
 */
public class LocationSelectAreaActivity extends BaseActivity implements
		OnItemClickListener {
	private TextView currentAreaTxt;

	private TextView countryTxt;

	private TextView cityTxt;

	private ListView areaLst;

	private MyListView areaLst1;

	private List<AreaInfo> areaList = new ArrayList<AreaInfo>();

	private LocationAreaAdapter adapter;

	private String selectCity = "";

	private String haveSelectArea;

	private RelativeLayout havaSelectLayout;

	private LocationClient mLocationClient = null;

	private LinearLayout locationLayout;
	
	private FlowLayout flowLayout;

	private BDLocationListener myListener = new MyLocationListener();

	private static final String[] hotCities = new String[] { "北京", "上海", "成都",
			"广州" };

	private static final String CH = "中国";
	
	private ListView citysList;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.location_select_area_activity);
		initView();
		 initLocation();
		
	}

	private String countryStr = "";

	private String cityStr = "";

	private SideBar sideBar;

	private TextView dialog;

	private RelativeLayout otherCountry;

	private DeletedEditText auto;
	
	private FrameLayout frameLayout;
	
	private LocationAreaAdapter1 adapter1;
	
	private RelativeLayout second_title1;
	protected void initView() {
		setSecondTitle("更多城市");
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		areaLst = (ListView) findViewById(R.id.area_listview);
		  View footer = LayoutInflater.from(this).inflate(R.layout.location_select_area_activity_footer, null);
		  View header = LayoutInflater.from(this).inflate(R.layout.location_select_area_activity_header, null);
		  areaLst.addHeaderView(header,null,false);
		  areaLst.addFooterView(footer);
		  flowLayout = (FlowLayout) findViewById(R.id.tagFlow);
		  second_title1 = (RelativeLayout) findViewById(R.id.second_title);
			second_title1.setFocusable(true);
			second_title1.setFocusableInTouchMode(true);
			second_title1.requestFocus();
//		sideBar.setVisibility(View.INVISIBLE);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					// areaLst.requestFocusFromTouch();//获取焦点
					// areaLst.setSelection(position);
					// areaLst.requestFocus()
					// areaLst.setSelection(0);
					areaLst.setAdapter(adapter);  // or  list.setAdapter(list.getAdapter())
//					areaLst.setPosition(position);
					areaLst.setSelection(position+1);
				}

			}
		});
		haveSelectArea = getIntent().getStringExtra("areaAddress");
		if (!StringUtils.isEmpty(haveSelectArea)) {
			if (haveSelectArea.indexOf(" ") > 0) {
				countryStr = haveSelectArea.substring(0,
						haveSelectArea.indexOf(" "));
				cityStr = haveSelectArea.substring(haveSelectArea.indexOf(" "));
			} else {
				countryStr = haveSelectArea;
				cityStr = "已选地区";
			}
		}
		locationLayout = (LinearLayout) findViewById(R.id.location_layout);
		havaSelectLayout = (RelativeLayout) findViewById(R.id.hava_select);
		currentAreaTxt = (TextView) findViewById(R.id.current_area_txt);
		otherCountry = (RelativeLayout) findViewById(R.id.other_country);
		countryTxt = (TextView) findViewById(R.id.country_txt);
		countryTxt.setText(countryStr);
		cityTxt = (TextView) findViewById(R.id.city_txt);
		cityTxt.setText(cityStr);
		frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
		citysList = (ListView) findViewById(R.id.select_citys);
//		areaLst1 = (MyListView) findViewById(R.id.area_listview1);
		AreaInfo info = SqliteDBHelper.getHelper().getAreaInfoByName(CH);
		areaList = (List<AreaInfo>) SqliteDBHelper.getHelper()
				.getAreaListByPid(info.getAreaId());
		for (AreaInfo areaInfo : areaList) {
			areaInfo.setSortLetter(HanziToPinyin.getFirstPinYin(areaInfo
					.getAreaName()));
		}
		adapter = new LocationAreaAdapter(LocationSelectAreaActivity.this, areaList);
		areaLst.setAdapter(adapter);
		areaLst.setOnItemClickListener(this);
		if (StringUtils.isEmpty(countryStr) && StringUtils.isEmpty(cityStr)) {
			havaSelectLayout.setVisibility(View.GONE);
		}
//		havaSelectLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (StringUtils.isEmpty(countryStr)) {
//					return;
//				} else {
//					AreaInfo info = SqliteDBHelper.getHelper()
//							.getAreaInfoByName(countryStr);
//					ArrayList<AreaInfo> newAreaList = SqliteDBHelper
//							.getHelper().getAreaListByPid(info.getAreaId());
//					if (newAreaList == null || newAreaList.size() == 0) {
//						Intent intent = new Intent();
//						intent.putExtra("area", info.getAreaName());
//						setResult(RESULT_OK, intent);
//						finish();
//						activityAnimationClose();
//					} else {
//						selectCity = info.getAreaName();
//						Intent intent = new Intent(
//								LocationSelectAreaActivity.this,
//								AreaListActivity.class);
//						intent.putParcelableArrayListExtra("areaList",
//								newAreaList);
//						startActivityForResult(intent, 101);
//						activityAnimationOpen();
//					}
//				}
//			}
//		});
		areaLst.setFocusable(false);
		areaLst.setFocusableInTouchMode(false);

//		areaLst1.setAdapter(new ArrayAdapter<String>(this,
//				R.layout.location_area_item, hotCities));
//		areaLst1.setFocusable(false);
//		areaLst1.setFocusableInTouchMode(false);
//		areaLst1.setOnItemClickListener(this);

		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		otherCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AreaInfo info = SqliteDBHelper.getHelper()
						.getAreaInfoByName(CH);
				ArrayList<AreaInfo> newAreaList = SqliteDBHelper.getHelper()
						.getAreaListByPid("0", info.getAreaId());
				Iterator<AreaInfo> sListIterator = newAreaList.iterator();
				while (sListIterator.hasNext()) {
					AreaInfo areainfo = sListIterator.next();
					if (areainfo.getAreaId().equals("CN")
							|| areainfo.getAreaId().equals("HK")
							|| areainfo.getAreaId().equals("MO")
							|| areainfo.getAreaId().equals("TW")) {
						sListIterator.remove();
					}
				}
				Intent intent = new Intent(LocationSelectAreaActivity.this,
						LocationListActivity.class);
				intent.putParcelableArrayListExtra("areaList", newAreaList);
				startActivityForResult(intent, 101);
				activityAnimationOpen();
			}
		});
		 addFlowView();
		 
		 
		  auto = (DeletedEditText) findViewById(R.id.autoCompleteTextView1);
		  auto.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String txt = auto.getText().toString();
				if(StringUtils.isEmpty(txt)){
					//如果为空
					frameLayout.setVisibility(View.VISIBLE);
					citysList.setVisibility(View.GONE);
				}else{
					//如果不为空
					frameLayout.setVisibility(View.GONE);
					citysList.setVisibility(View.VISIBLE);
					List<AreaInfo> list = SqliteDBHelper.getHelper().getAreaListByName(txt.trim());
					adapter1 = new LocationAreaAdapter1(LocationSelectAreaActivity.this, list);
					citysList.setAdapter(adapter1);
					adapter1.notifyDataSetChanged();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
//		 
//			ArrayList<AreaInfo> areas=SqliteDBHelper.getHelper()
//					.getAreaList();
//			String str3[] =new String[areas.size()];
//			int i=0;
//			for(AreaInfo areaInfo:areas){
//				str3[i]=areaInfo.getAreaName();
//				++i;
//			}
////			 ArrayAdapter<String> av = new ArrayAdapter<String>(this,
////						android.R.layout.simple_dropdown_item_1line, str3);
////				AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
////				auto.setAdapter(av);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 101 && arg2 != null) {
			String areaString = selectCity + " " + arg2.getStringExtra("area");
			Intent intent = new Intent();
			intent.putExtra("area", areaString);
			setResult(RESULT_OK, intent);
			finish();
			activityAnimationClose();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent.getId() == R.id.area_listview) {
			
			
			position=position-1;
			ArrayList<AreaInfo> newAreaList = SqliteDBHelper.getHelper()
					.getAreaListByPid(areaList.get(position).getAreaId());
			if (newAreaList == null || newAreaList.size() == 0) {
				Intent intent = new Intent();
				intent.putExtra("area", areaList.get(position).getAreaName());
				setResult(RESULT_OK, intent);
				finish();
				activityAnimationClose();
			} else {
				selectCity = areaList.get(position).getAreaName();
				Intent intent = new Intent(LocationSelectAreaActivity.this,
						LocationListActivity.class);
				intent.putParcelableArrayListExtra("areaList", newAreaList);
				startActivityForResult(intent, 101);
				activityAnimationOpen();
			}
		} else {
			Intent intent = new Intent();
			intent.putExtra("area", hotCities[position]);
			setResult(RESULT_OK, intent);
			finish();
			activityAnimationClose();
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
			// 定位结束，关闭定位
			String province = "";
			if (location.getProvince().contains("省")) {
				province = location.getProvince().substring(0,
						location.getProvince().length() - 1);
			} else {
				province = location.getProvince();
			}
			String city = "";
			if (location.getCity().contains("市")) {
				city = location.getCity().substring(0,
						location.getCity().length() - 1);
			} else {
				city = location.getCity();
			}
			currentAreaTxt.setText(location.getCountry() + " " + province + " "
					+ city);
			mLocationClient.stop();
			locationLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String locationArea = currentAreaTxt.getText().toString();
					if (!StringUtils.isEmpty(locationArea)) {
						if (locationArea.indexOf(" ") > 0) {
							countryStr = locationArea.substring(0,
									locationArea.indexOf(" "));
							cityStr = locationArea.substring(locationArea
									.indexOf(" "));
						} else {
							countryStr = locationArea;
							cityStr = "已选地区";
						}
						countryTxt.setText(countryStr);
						cityTxt.setText(cityStr);
					}
					Intent intent = new Intent();
					intent.putExtra("area", locationArea);
					setResult(RESULT_OK, intent);
					finish();
					activityAnimationClose();
				}
			});
		}
	}
	
	private void addFlowView(){
for(int i=0;i<hotCities.length;i++){
        TextView newView = new TextView(this);
        newView.setBackgroundResource(R.drawable.square_normal);;
        newView.setText(hotCities[i]);
        newView.setTag(i);
        newView.setGravity(Gravity.CENTER);
        newView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        newView.setTextColor(Color.parseColor("#333333"));
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(DensityUtil.dip2px(this, 90), DensityUtil.dip2px(this, 25));
        params.rightMargin =DensityUtil.dip2px(this, 20);
        params.bottomMargin = DensityUtil.dip2px(this, 10);
        newView.setLayoutParams(params);
        flowLayout.addView(newView);
        newView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("area", hotCities[(Integer)v.getTag()]);
				setResult(RESULT_OK, intent);
				finish();
				activityAnimationClose();
			}
		});
	}
	}
	
	
	  public  void activityAnimationClose()
	    {
	        overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
	    }
}
