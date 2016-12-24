package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CatsProductAdapter;
import com.bcinfo.tripaway.bean.Cust;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.FilterDialog;
import com.bcinfo.tripaway.view.dialog.FilterDialog.OperationListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CatsInfoActivity extends BaseActivity implements
		PersonalScrollViewListener, OnClickListener {

	private RelativeLayout layout_product_detail_title;

	private MyListView mListView;

	private CatsProductAdapter adapter;

	private ProductDetailScrollView mProductScrollView;

	private ImageView mProductHeadImg;

	private List<ProductNewInfo> list = new ArrayList<ProductNewInfo>();

	private TextView product_title;

	private Cust cust;

	private String userId;

	private int pageNum = 1;

	private int pageSize = 10;

	private boolean isLoadMore = false;
	
	private boolean isMore =true;

	private String destId = "";

	private String price = "";

	private String day = "";

	
	private ImageView filterBtn;
	
	private List<Dest> destList = new ArrayList<Dest>();
	
	private ImageView noView;
	
	private LinearLayout backLy;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.cats_info_activity);
		cust = getIntent().getParcelableExtra("cust");
		userId = getIntent().getStringExtra("userId");
		//加载目的地
		getDestList(userId);
		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		backLy = (LinearLayout) findViewById(R.id.layout_back_button);
		backLy.setOnClickListener(this);
		noView = (ImageView) findViewById(R.id.no_list);
		filterBtn = (ImageView) findViewById(R.id.fliter_btn);
		filterBtn.setOnClickListener(this);
		product_title = (TextView) findViewById(R.id.product_title);
		product_title.setText(cust.getCustTitle());
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title1);
		layout_product_detail_title.getBackground().setAlpha(0);
		mProductHeadImg = (ImageView) findViewById(R.id.product_head_imageView);
		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);

		ImageLoader.getInstance().displayImage(
				"http://pic25.nipic.com/20121117/6240454_094125531000_2.jpg",
				mProductHeadImg);
		if (!StringUtils.isEmpty(cust.getCover())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + cust.getCover(), mProductHeadImg);
		}

		mListView = (MyListView) findViewById(R.id.product_cats_listview);
		adapter = new CatsProductAdapter(this, list);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 第一次加在
		queryMyProduct(destId, price, day, cust.getCatId());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProductNewInfo info = (ProductNewInfo)parent.getItemAtPosition(position);
				 Intent  intentForProductDetail = new Intent(CatsInfoActivity.this, GrouponProductNewDetailActivity.class);
                intentForProductDetail.putExtra("productId", info.getId());
                intentForProductDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                intentForProductDetail.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(intentForProductDetail);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fliter_btn:
			new FilterDialog(CatsInfoActivity.this, new OperationListener() {

				@Override
				public void queryByTag(String dest, String prices, String days) {
					list.clear();
					isMore = true;
					pageNum = 1;
					destId = dest;
					price = prices;
					day = days;
					queryMyProduct(destId, price, day, cust.getCatId());
				}
			}, destList,destId,price,day).show();
			break;
		case R.id.layout_back_button:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY,
			int prePositionX, int prePositionY) {
		if (view != null && view == mProductScrollView) {
			int alpha = positionY / 3;

			if (positionY > 220)

			{
				alpha = 255;
			}
			layout_product_detail_title.getBackground().setAlpha(alpha);
			product_title.setAlpha(alpha / 255f);

			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();
			int scollHeight = mProductScrollView.getHeight();
			if (height <= scollY + scollHeight) {
				// 底部
				queryMyProduct(destId, price, day, cust.getCatId());
			} else if (scollY == 0) {
				// 顶部
//				isMore = true;
//				if(!isLoadMore){
//					list.clear();
//					pageNum = 1;
//					queryMyProduct(destId, price, day, cust.getCatId());
//				}
			}
		} else {
			return;
		}
	}

	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {
			// TODO Auto-generated method stub
			float alpha = height * 3 - 200;
			if (alpha > 255) {
				alpha = 255;
			}
			if (alpha < 50) {
				alpha = 0;
			}
		}
	};

	private void queryMyProduct(String destId, String price, String day,
			String cat) {
		if (isLoadMore||!isMore) {
			return;
		}
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("destId", destId);
		params.put("price", price);
		params.put("day", day);
		params.put("cat", cat);
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		HttpUtil.get(Urls.get_plist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
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
							HashMap<String, String> exts=new HashMap<String, String>();
							if(!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("refer_tags"))){
								exts.put("refer_tags", productJson.optJSONObject("exts").optString("refer_tags"));
							}
							if(!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("big_refer_tags"))){
								exts.put("big_refer_tags", productJson.optJSONObject("exts").optString("big_refer_tags"));
							}
							if(exts.size()>0){
								productNewInfo.setExts(exts);	
							}
						}
						productNewInfo.setOriginalPrice(productJson.optString("originalPrice"));
						productNewInfo.setId(productJson.optString("id"));
						productNewInfo.setExpStart(productJson
								.optString("expStart"));
						productNewInfo.setExpend(productJson
								.optString("expend"));
						productNewInfo.setProductType(productJson
								.optString("productType"));
						productNewInfo.setDistance(productJson
								.optString("distance"));
						productNewInfo.setTitle(productJson.optString("title"));
						productNewInfo.setPoiCount(productJson
								.optString("poiCount"));
						productNewInfo.setPrice(productJson.optString("price"));
						productNewInfo.setDays(productJson.optString("days"));
						productNewInfo.setDescription(productJson
								.optString("description"));
						productNewInfo.setPriceMax(productJson
								.optString("priceMax"));
						productNewInfo.setTitleImg(productJson
								.optString("titleImg"));
						productNewInfo.setMaxMember(productJson
								.optString("maxMember"));
						productNewInfo.setServices(productJson
								.optString("serviceCodes"));
						productNewInfo.setServTime(productJson
								.optString("servTimes"));
						productNewInfo.setPriceFrequency(productJson
								.optString("priceFrequency"));
						productNewInfo.setPv(productJson
								.optString("pv"));
						ArrayList<String> topics = new ArrayList<String>();
						JSONArray topicJsonArray = productJson
								.optJSONArray("topics");
						if (topicJsonArray != null
								&& topicJsonArray.length() > 0) {
							for (int j = 0; j < topicJsonArray.length(); j++) {
								topics.add(topicJsonArray.optString(j));
							}
							productNewInfo.setTopics(topics);
						}
						proList.add(productNewInfo);
					}
//					list.addAll(proList);
//					adapter.notifyDataSetChanged();
					notifyList(proList);
					if (proList.size() < pageSize) {
						isMore = false;
					} else {
						pageNum++;
					}
					isLoadMore = false;
				}else{
					notifyList(new ArrayList<ProductNewInfo>());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoadMore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoadMore = false;
			}
		});
	}
	
	private void getDestList(String userId){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pagenum", 1);
		params.put("pagesize", 100);
		
		HttpUtil.get(Urls.dest_list, params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code =response.optString("code");
				
				if("00000".equals(code)){
					JSONArray data= response.optJSONArray("data");
					if(null != data){
						for(int i =0;i<data.length();i++ ){
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
	
	private void notifyList(List<ProductNewInfo> infoList){
		list.addAll(infoList);
		adapter.notifyDataSetChanged();
		if(list.size() == 0){
			noView.setVisibility(View.VISIBLE);
		}else{
			noView.setVisibility(View.GONE);
		}
	}
}
