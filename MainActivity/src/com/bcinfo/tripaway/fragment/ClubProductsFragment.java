package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.CatsInfoActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.adapter.CatsAdapter;
import com.bcinfo.tripaway.adapter.CatsProductAdapter;
import com.bcinfo.tripaway.bean.Cats;
import com.bcinfo.tripaway.bean.Cust;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.fragment.CatsFragment.OperationListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.dialog.FilterDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ClubProductsFragment extends BaseFragment {

	private ViewPager myViewPager;

	private Context mContext;

	private UserInfoEx userInfoEx;

	private List<BaseFragment> catsFragmentList = new ArrayList<BaseFragment>();

	private CatsAdapter catsAdapter;

	private ListView mListView;

	private CatsProductAdapter adapter;

	private int pageNum = 1;

	private int pageSize = 10;

	private boolean isLoadMore = false;

	private boolean isMore = true;

	private String destId = "";

	private String price = "";

	private String day = "";

	private List<ProductNewInfo> list = new ArrayList<ProductNewInfo>();

	private ImageView filterBtn;

	private List<Dest> destList = new ArrayList<Dest>();

	private ImageView noView;

	private RelativeLayout filterLayout;
	// 下方筛选的控制影藏
	private RelativeLayout layout_product_detail_title;

	// 设置VIEWPAGE模块的空数据影藏
	private LinearLayout noviewpager;

	public ClubProductsFragment() {

	}

	public ClubProductsFragment(Context mContext, UserInfoEx userInfoEx) {
		this.mContext = mContext;
		this.userInfoEx = userInfoEx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.club_products_fragment, null);
		// 加载目的地
		initView(view);
		getDestList(userInfoEx.getUserInfo().getUserId());
		registerBoradcastReceiver();

		return view;
	}

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.loadMoreProducts");
		// 注册广播
		mContext.registerReceiver(broadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.loadMoreProducts".equals(action)) {
				queryMyProduct(destId, price, day, "");
			}
		}
	};

	private void initView(View view) {
		layout_product_detail_title = (RelativeLayout) view.findViewById(R.id.layout_product_detail_title);
		noviewpager = (LinearLayout) view.findViewById(R.id.noviewpage);
		filterLayout = (RelativeLayout) view.findViewById(R.id.filter_layout);
		filterLayout.setOnClickListener(clickListener);
		noView = (ImageView) view.findViewById(R.id.no_list);
		filterBtn = (ImageView) view.findViewById(R.id.fliter_btn);
		// filterBtn.setOnClickListener(clickListener);
		myViewPager = (ViewPager) view.findViewById(R.id.cats_page);
		ArrayList<Cats> catsList = userInfoEx.getCats();
		if (catsList != null && catsList.size() > 0) {
			for (int i = 0; i < catsList.size(); i++) {
				CatsFragment fragment = new CatsFragment(mContext, catsList.get(i), new OperationListener() {

					@Override
					public void queryProductById(Cats cust) {
						Intent it = new Intent(mContext, CatsInfoActivity.class);
						Cust c = new Cust();
						c.setCatId(cust.getCatId());
						c.setCustTitle(cust.getCustTitle());
						c.setCover(cust.getCover());
						it.putExtra("cust", c);
						it.putExtra("userId", userInfoEx.getUserInfo().getUserId());
						startActivity(it);
					}
				});
				catsFragmentList.add(fragment);
			}
		}
		catsAdapter = new CatsAdapter(getChildFragmentManager(), catsFragmentList, (Activity) mContext);
		if (catsFragmentList.size() > 0) {
			noviewpager.setVisibility(View.VISIBLE);
		}
		myViewPager.setAdapter(catsAdapter);
		myViewPager.setPageMargin(10);
		myViewPager.setOffscreenPageLimit(catsFragmentList.size());
		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mListView = (ListView) view.findViewById(R.id.product_cats_listview);
		adapter = new CatsProductAdapter(mContext, list);
		if (list.size() > 0) {
			layout_product_detail_title.setVisibility(View.VISIBLE);
			noView.setVisibility(View.GONE);
		}
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 第一次加在
		queryMyProduct(destId, price, day, "");
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductNewInfo info = (ProductNewInfo) parent.getItemAtPosition(position);
				Intent intentForProductDetail = new Intent(mContext, GrouponProductNewDetailActivity.class);
				intentForProductDetail.putExtra("productId", info.getId());
				intentForProductDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentForProductDetail.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				startActivity(intentForProductDetail);
			}
		});

	}

	/*
	 * test 产品详情url
	 */
	private void queryMyProduct(String destId, String price, String day, String cat) {
		if (isLoadMore || !isMore) {
			return;
		}
		RequestParams params = new RequestParams();
		params.put("userId", userInfoEx.getUserInfo().getUserId());
		params.put("destId", destId);
		params.put("price", price);
		params.put("day", day);
		params.put("cat", cat);
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		HttpUtil.get(Urls.get_plist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				String code = response.optString("code");
				isLoadMore = true;
				if (!"00000".equals(code)) {
					return;
				}

				JSONArray products = response.optJSONArray("data");

				if (null != products) {
					List<ProductNewInfo> proList = new ArrayList<ProductNewInfo>();
					for (int i = 0; i < products.length(); i++) {
						JSONObject productJson = products.optJSONObject(i);
						ProductNewInfo productNewInfo = new ProductNewInfo();
						if (productJson.optJSONObject("exts") != null) {
							HashMap<String, String> exts = new HashMap<String, String>();
							if (!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("refer_tags"))) {
								exts.put("refer_tags", productJson.optJSONObject("exts").optString("refer_tags"));
							}
							if (!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("big_refer_tags"))) {
								exts.put("big_refer_tags",
										productJson.optJSONObject("exts").optString("big_refer_tags"));
							}
							if (exts.size() > 0) {
								productNewInfo.setExts(exts);
							}
						}
						productNewInfo.setPv(productJson.optString("pv"));
						productNewInfo.setOriginalPrice(productJson.optString("originalPrice"));
						productNewInfo.setId(productJson.optString("id"));
						productNewInfo.setExpStart(productJson.optString("expStart"));
						productNewInfo.setExpend(productJson.optString("expend"));
						productNewInfo.setProductType(productJson.optString("productType"));
						productNewInfo.setDistance(productJson.optString("distance"));
						productNewInfo.setTitle(productJson.optString("title"));
						productNewInfo.setPoiCount(productJson.optString("poiCount"));
						productNewInfo.setPrice(productJson.optString("price"));
						productNewInfo.setDays(productJson.optString("days"));
						productNewInfo.setDescription(productJson.optString("description"));
						productNewInfo.setPriceMax(productJson.optString("priceMax"));
						productNewInfo.setTitleImg(productJson.optString("titleImg"));
						productNewInfo.setMaxMember(productJson.optString("maxMember"));
						productNewInfo.setServices(productJson.optString("serviceCodes"));
						productNewInfo.setServTime(productJson.optString("servTimes"));
						productNewInfo.setPriceFrequency(productJson.optString("priceFrequency"));
						ArrayList<String> topics = new ArrayList<String>();
						JSONArray topicJsonArray = productJson.optJSONArray("topics");
						if (topicJsonArray != null && topicJsonArray.length() > 0) {
							for (int j = 0; j < topicJsonArray.length(); j++) {
								topics.add(topicJsonArray.optString(j));
							}
							productNewInfo.setTopics(topics);
						}
						proList.add(productNewInfo);
					}
					// list.addAll(proList);
					// adapter.notifyDataSetChanged();
					notifyList(proList);
					if (proList.size() > 0) {
						layout_product_detail_title.setVisibility(View.VISIBLE);
						noView.setVisibility(View.GONE);
					}
					if (proList.size() < pageSize) {
						isMore = false;
					} else {
						pageNum++;
					}
					isLoadMore = false;
				} else {
					notifyList(new ArrayList<ProductNewInfo>());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoadMore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoadMore = false;
			}
		});
	}

	private void notifyList(List<ProductNewInfo> infoList) {
		 LayoutParams ls=	mListView.getLayoutParams(); 
		 int h=DensityUtil.dip2px(getActivity(), 150+0.5f);
		 ls.height=(list.size()+infoList.size())*h;
		list.addAll(infoList);
		
		if (list.size() == 0 && catsFragmentList.size() == 0) {
			noView.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.filter_layout) {
				new FilterDialog(mContext, new FilterDialog.OperationListener() {

					@Override
					public void queryByTag(String dest, String prices, String days) {
						list.clear();
						isMore = true;
						pageNum = 1;
						destId = dest;
						price = prices;
						day = days;
						queryMyProduct(destId, price, day, "");
					}
				}, destList, destId, price, day).show();
			}
		}
	};

	private void getDestList(String userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pagenum", 1);
		params.put("pagesize", 100);

		HttpUtil.get(Urls.dest_list, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray data = response.optJSONArray("data");
					if (null != data) {
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.optJSONObject(i);
							Dest dest = new Dest();
							dest.setDestId(obj.optString("destId"));
							dest.setDestName(obj.optString("destName"));
							destList.add(dest);
						}
					}
				}
			}
		});
	}
}
