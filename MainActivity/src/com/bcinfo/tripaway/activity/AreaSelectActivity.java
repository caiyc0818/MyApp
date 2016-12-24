package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaSelectAdapter;
import com.bcinfo.tripaway.adapter.LocationAreaAdapter1;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hanweipeng
 * @date 2015-7-18 上午9:58:37
 */
public class AreaSelectActivity extends BaseActivity implements IXListViewListener, OnClickListener {
	protected static final String TAG = "AreaSelectActivity";

	private XListView areaLst;

	private AreaSelectAdapter adapter;

	private List<HashMap<String, String>> areaList = new ArrayList<HashMap<String, String>>();

	private String currentAddress;
	private String coordinate;
	private ArrayList<PoiInfo> poilist;

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private int pagesize = 10;

	private int pagenum = 0;

	PoiSearch mPoiSearch = null;

	private DeletedEditText autoCompleteTextView1;

	LinearLayout head_one;
	LinearLayout head_two;

	boolean isEmpty = true;

	TextView textviewaddress;
	TextView addressold;
	RelativeLayout oldaddressRelative;

	RelativeLayout head_one_no;
	/**
	 * 输入关键字
	 */
	String txt;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.area_select_activity);
		setSecondTitle("选择地点");
		mPoiSearch = PoiSearch.newInstance();
		currentAddress = getIntent().getStringExtra("currentAddress");
		coordinate = getIntent().getStringExtra("coordinate");
		head_one = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_one, null);
		addressold = (TextView) head_one.findViewById(R.id.addressold);
		addressold.setText(currentAddress);
		oldaddressRelative = (RelativeLayout) head_one.findViewById(R.id.oldaddressRelative);
		oldaddressRelative.setOnClickListener(this);
		head_one_no = (RelativeLayout) head_one.findViewById(R.id.head_one_no);
		head_one_no.setOnClickListener(this);
		head_two = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_two, null);
		head_two.setOnClickListener(this);
		textviewaddress = (TextView) head_two.findViewById(R.id.textviewaddress);
		poilist = getIntent().getParcelableArrayListExtra("poilist");
		areaLst = (XListView) findViewById(R.id.area_listview);
		areaLst.setPullRefreshEnable(true);
		areaLst.setPullLoadEnable(false);
		autoCompleteTextView1 = (DeletedEditText) findViewById(R.id.autoCompleteTextView1);
		if (StringUtils.isEmpty(autoCompleteTextView1.getText().toString())) {
			areaLst.addHeaderView(head_one);
		} else {
			areaLst.addHeaderView(head_two);
		}
		// areaLst.addHeaderView(head_one);
		areaLst.setXListViewListener(this);
		autoCompleteTextView1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				txt = autoCompleteTextView1.getText().toString();
				if (StringUtils.isEmpty(txt)) {
					// 如果为空
					if (!isEmpty && areaLst.getHeaderViewsCount() != 0) {
						areaLst.removeHeaderView(head_two);
						areaLst.addHeaderView(head_one);
						isEmpty = true;
					}
				} else {
					if (isEmpty && areaLst.getHeaderViewsCount() != 0) {
						areaLst.removeHeaderView(head_one);
						areaLst.addHeaderView(head_two);
						isEmpty = false;
					}
				}
				textviewaddress.setText(txt);
				pagenum = 0;
				getInfo();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// getAreas();
		adapter = new AreaSelectAdapter(this, areaList);
		areaLst.setAdapter(adapter);
		if (poilist == null || poilist.size() <= 0) {
			return;
		}
		for (PoiInfo p : poilist) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", p.name);
			map.put("address", p.address);
			areaList.add(map);
		}
		if (areaList.size() >= pagesize) {
			pagenum++;
			areaLst.setPullLoadEnable(true);
		} else {
			areaLst.setPullLoadEnable(false);
		}
		isLoadmore = false;
		isPullRefresh = false;
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullRefresh = true;
		pagenum = 0;
		// getAreas();
		getInfo();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		// getAreas();
		getInfo();
	}

	private void getInfo() {
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			public void onGetPoiResult(PoiResult result) {
				if (!isLoadmore && !isPullRefresh) {
					areaList.clear();
				}
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					areaLst.stopLoadMore();
				}
				if (isPullRefresh) {
					// 下拉刷新返回的
					areaLst.successRefresh();
					areaList.clear();
				}
				if (result.error == ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(AreaSelectActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
				} else if (result.error == null) {
					Toast.makeText(AreaSelectActivity.this, "搜索出错啦..", Toast.LENGTH_LONG).show();
				}else if(result.error == ERRORNO.AMBIGUOUS_KEYWORD) {
					
				}
				else {
					PoiResult result2 = result;
					ArrayList<PoiInfo> resultList = (ArrayList<PoiInfo>) result.getAllPoi();
					if(resultList==null){
						Toast.makeText(AreaSelectActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
						return;
					}
					// 获取POI检索结果
					for (PoiInfo p : resultList) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("name", p.name);
						map.put("address", p.address);
						areaList.add(map);
					}
					if (areaList.size() >= pagesize) {
						pagenum++;
						areaLst.setPullLoadEnable(true);
					} else {
						areaLst.setPullLoadEnable(false);
					}
				}
				isLoadmore = false;
				isPullRefresh = false;
				adapter.notifyDataSetChanged();
			}

			// public void onGetPoiDetailResult(PoiDetailResult result){
			// //获取Place详情页检索结果
			// }
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				// TODO Auto-generated method stub

			}
		};

		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		if (StringUtils.isEmpty(autoCompleteTextView1.getText().toString())) {
			String[] strs = coordinate.split(",");
			Double double1 = Double.parseDouble(strs[0].trim());
			Double double2 = Double.parseDouble(strs[1].trim());
			mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword("美食号码银行")
					.location(new LatLng(double1, double2)).pageCapacity(10)
					.pageNum(pagenum).sortType(PoiSortType.distance_from_near_to_far).radius(10 * 1000));
		} else {
			mPoiSearch.searchInCity((new PoiCitySearchOption()).city(currentAddress).keyword(txt).pageNum(pagenum));
		}
	}

	private void getAreas() {
		RequestParams params = new RequestParams();
		params.put("ak", "v8usndPOQxRZhaAurhOkgUps");
		// params.put("location", coordinate);
		params.put("output", "json");
		params.put("pois", "1");
		params.put("page_num", pagenum);
		params.put("page_size", pagesize);
		params.put("mcode", "E1:15:2D:9A:6A:F2:41:06:B7:2D:FD:99:5E:87:62:48:87:18:CC:A1;com.bcinfo.tripaway");
		String string = Urls.BAIDUURL + params.toString();
		HttpUtil.get(string, new JsonHttpResponseHandler() {
			// HttpUtil.get(Urls.push_flash_url, new JsonHttpResponseHandler()
			// {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					areaLst.stopLoadMore();
				}
				if (isPullRefresh) {
					areaLst.stopRefresh();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					areaLst.stopLoadMore();
				}
				if (isPullRefresh) {
					areaLst.stopRefresh();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				String status = response.optString("status");
				if (!isLoadmore && !isPullRefresh) {
					areaList.clear();
				}
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					areaLst.stopLoadMore();
				}
				if (isPullRefresh) {
					// 下拉刷新返回的
					areaLst.successRefresh();
					areaList.clear();
				}
				if (status.equals("0")) {

					JSONObject result = response.optJSONObject("result");
					if (result != null && result.length() > 0) {
						JSONArray poisArray = result.optJSONArray("pois");
						if (poisArray != null && poisArray.length() > 0) {
							for (int i = 0; i < poisArray.length(); i++) {
								JSONObject pois = poisArray.optJSONObject(i);
								if (pois != null && pois.length() > 0) {
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("name", pois.optString("name"));
									map.put("address", pois.optString("addr"));
									areaList.add(map);
								}
							}
							if (poisArray.length() < 10) {
								areaLst.setPullLoadEnable(false);
							} else {
								pagenum++;
								areaLst.setPullLoadEnable(true);
							}
							if (areaList.size() > 0) {
								adapter.notifyDataSetChanged();
							}
						}
					}
				} else {
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						areaLst.stopRefresh();
					}
				}
				isLoadmore = false;
				isPullRefresh = false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_one_no:
			Intent intent1 = new Intent();
			intent1.putExtra("address", "不显示");
			setResult(0, intent1);
			activityAnimationClose();
			finish();
			break;
		case R.id.oldaddressRelative:
			Intent intent2 = new Intent();
			intent2.putExtra("address", addressold.getText());
			setResult(0, intent2);
			activityAnimationClose();
			finish();
			break;
		case R.id.head_two:
			Intent intent3 = new Intent();
			intent3.putExtra("address", textviewaddress.getText());
			setResult(0, intent3);
			activityAnimationClose();
			finish();
			break;
		default:
			break;
		}
	}

}
