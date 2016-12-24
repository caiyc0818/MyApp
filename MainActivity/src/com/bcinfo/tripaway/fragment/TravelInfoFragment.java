package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ProductJoinMebActivity;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductInfoAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TravelInfoFragment extends Fragment implements OnClickListener {
	private ImageView but = null;

	private FlowLayout destinationFlowLayout;

	private LinearLayout startFilter;

	private FilterLayout filterLayout;

	private List<Dest> destList = new ArrayList<Dest>();

	private String userId;

	private ProductInfoAdapter productInfoAdapter;

	private PopupWindow popupWindow;
	private List<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();

	private ListView productDetailList;
	
	private ArrayList<TripArticle> articleList = new ArrayList<TripArticle>();

	public TravelInfoFragment(String userId) {
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.product_info, null);// 注意不要指定父视图
		// but = (TextView) view.findViewById(R.id.vipNum);
		initView(view);
		

		// setValue();
		queryDestInfo();
		queryProductInfo(userId);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// but.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// Toast.makeText(VehicleInfoFragment.this.getActivity(),"hahah",
		// 0).show();
		// }
		// });
	}

	private void initView(View view) {
		startFilter = (LinearLayout) view.findViewById(R.id.startFilter);

		productDetailList = (ListView) view
				.findViewById(R.id.product_detail_listview);

		startFilter.setOnClickListener(this);
		productInfoAdapter = new ProductInfoAdapter(getActivity(), productList);
		productDetailList.setAdapter(productInfoAdapter);
	}


	public void setValue() {
		List<String> strs = new ArrayList<String>();
		strs.add("西藏");
		strs.add("斯里兰卡");
		strs.add("重庆");
		strs.add("桂林");
		strs.add("印度");
		addFlowView(strs, destinationFlowLayout);
		List<String> priceStrs = new ArrayList<String>();
		priceStrs.add("2000元以内");
		priceStrs.add("2001-3000");
		priceStrs.add("3001-5000");
		priceStrs.add("5001-1W");
		priceStrs.add("1W-5W");
		priceStrs.add("5W以上");
		addFlowView(strs, destinationFlowLayout);
		List<String> daysStrs = new ArrayList<String>();
		daysStrs.add("5日以内");
		daysStrs.add("5-10日");
		daysStrs.add("10-15日");
		daysStrs.add("15日以上");
	}

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			RadioButton newView = new RadioButton(getActivity());
			newView.setBackgroundResource(R.drawable.square);
			newView.setText(strs.get(i));
			newView.setButtonDrawable(android.R.color.transparent);
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.BLACK);
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(getActivity(), 80), DensityUtil.dip2px(
							getActivity(), 29));
			params.rightMargin = DensityUtil.dip2px(getActivity(), 5);
			params.bottomMargin = DensityUtil.dip2px(getActivity(), 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startFilter:
			// 设置好参数之后再show
			popupWindow.showAsDropDown(startFilter);

		}
	}

	/**
	 * 查询个人相关目的地
	 * 
	 * @param userId
	 */
	private void queryDestInfo() {
		RequestParams params = new RequestParams();
		params.put("pagesize", 100);
		params.put("pagenum", 1);
		params.put("userId", userId);
		HttpUtil.get(Urls.get_dest, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 查询个人资料
	 * 
	 * @param userId
	 */
	private void queryProductInfo(String userId) {
		RequestParams params = new RequestParams();
		params.put("pagesize", 100);
		params.put("pagenum", 1);
		params.put("userId", userId);
		HttpUtil.get(Urls.get_plist, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray productArray = response.optJSONArray("product");
					if (productArray.length() > 0) {
						for (int i = 0; i < productArray.length(); i++) {
							JSONObject product = productArray.optJSONObject(i);
							ProductNewInfo productInfo = new ProductNewInfo();
							JSONArray expPeriodArray = product
									.optJSONArray("expPeriod");
							if (expPeriodArray != null) {
								// =====行程的开始日期
								ArrayList<AvailableTime> experiodList = new ArrayList<AvailableTime>();
								for (int j = 0; j < expPeriodArray.length(); j++) {
									JSONObject experiodJson = expPeriodArray
											.optJSONObject(i);
									AvailableTime availableTime = new AvailableTime();
									availableTime.setBeginDate(experiodJson
											.optString("beginDate"));
									availableTime.setEndDate(experiodJson
											.optString("endDate"));
									experiodList.add(availableTime);
									// //////////////////////////////
									String begin_time = availableTime
											.getBeginDate();
								}
								productInfo.setExpPeriodList(experiodList);
							}
							JSONArray topics = product.optJSONArray("topics");
							if (topics != null) {
								for (int j = 0; j < topics.length(); j++) {
									productInfo.getTopics().add(
											topics.opt(i).toString());
								}
							}
							productInfo.setId(product.optString("id"));
							productInfo.setDistance(product
									.optString("distance"));
							productInfo.setTitle(product.optString("title"));
							productInfo.setPoiCount(product
									.optString("poiCount"));
							productInfo.setPrice(product.optString("price"));
							productInfo.setDays(product.optString("days"));

							productInfo.setDescription(product
									.optString("description")); // 内容
							productInfo.setPriceMax(product
									.optString("priceMax"));
							productInfo.setTitleImg(product
									.optString("titleImg")); // 标题图片
							productInfo.setMaxMember(product
									.optString("maxMember"));
							productInfo.setProductType(product
									.optString("productType"));
							productInfo.setTotal(product.optString("servTimes"));
							;
							productInfo.setServices(product
									.optString("serviceCodes"));
							try {
								productInfo.setLevel(product.optJSONObject(
										"policy").getString("type"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							JSONObject userJSON = product
									.optJSONObject("creater");
							if (userJSON != null) {
								productInfo.getUser().setUserId(
										userJSON.optString("userId"));
								productInfo.getUser().setNickname(
										userJSON.optString("nickName"));
								productInfo.getUser().setAvatar(
										userJSON.optString("avatar"));
								productInfo.getUser().setIntroduction(
										userJSON.optString("introduction"));
								productInfo.getUser().setUserType(
										userJSON.optString("userType"));
							}
							productList.add(productInfo);
						}
						productInfoAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void initJoinMebPhoto(String productId) {
		// 1.http协议调用接口获取订购人数

		RequestParams requestParams = new RequestParams();
		requestParams.put("pagenum", "1");
		requestParams.put("pagesize", "20");
		requestParams.put("productId", productId);
		HttpUtil.get(Urls.product_buyers, requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						String msg = response.optString("msg");
						if (!code.equals("00000")) {

							return;
						}
						JSONObject dataJSON = response.optJSONObject("data");
						final String total = dataJSON.optString("total");

						JSONArray productArray = dataJSON
								.optJSONArray("buyers");

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);

					}

				});

	}
	
    /*
     * test 我的旅行故事 (微游记列表接口)
     */
    private void testMyStorySelfUrl(String userId)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", 100);
        params.put("pagesize", 1);
//        params.put("userId", userId);
        HttpUtil.get(Urls.tripStory_self_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                
                // System.out.println(statusCode);
               System.out.println(response.toString());
                // System.out.println(statusCode);
                // System.out.println(response);
                getTripStoryList(response);
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
                // System.out.println(statusCode);
                // System.out.println(responseString);
//                FooterBar.setVisibility(View.GONE);
//                TextOfFooterBar.setText("加载失败!");
            }
        });
        System.out.println("******加载完毕******");
    }
    
    
    
    /*
     * 解析从服务端获取的微游记参数
     */
    private void getTripStoryList(JSONObject response)
    {
        
        // System.out.println(response);
//        if ("00000".equals(response.optString("code")))
//        {
//        	if (isLoadMore)
//            {
//                // 上拉返回的结束加载更多布局
//                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//            }
//            if (isPullRefresh)
//            {
//                // 下拉刷新返回的
//                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
        	
            JSONObject dataObj = response.optJSONObject("data");
            JSONArray dataArray = dataObj.optJSONArray("tripstory");
            if (dataArray != null && dataArray.length() != 0)
            {
                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject tripStoryObj = dataArray.optJSONObject(i);
                    TripArticle articleObj = new TripArticle();
                    articleObj.setPublishTime(tripStoryObj.optString("publishTime"));
                    articleObj.setLocation(tripStoryObj.optString("location"));
                    articleObj.setIsLike(tripStoryObj.optString("isLike"));
                    articleObj.setDescription(tripStoryObj.optString("description"));
                    articleObj.setComments(tripStoryObj.optString("comments"));
                    articleObj.setPhotoId(tripStoryObj.optString("photoId"));
                    JSONArray picArray = tripStoryObj.optJSONArray("pictures");
                    if (picArray != null)
                    {
                        for (int j = 0; j < picArray.length(); j++)
                        {
                            JSONObject picObj = picArray.optJSONObject(j);
                            ImageInfo picRes = new ImageInfo();
                            picRes.setDesc(picObj.optString("desc"));
                            picRes.setUrl(picObj.optString("url"));
                            articleObj.getPictureList().add(picRes);
                            
                        }
                    }
                    JSONObject userObj = tripStoryObj.optJSONObject("publisher");
                    
                    if(userObj != null){
                    	 articleObj.getPublisher().setGender(userObj.optString("sex"));
                         articleObj.getPublisher().setStatus(userObj.optString("status"));
                         articleObj.getPublisher().setEmail(userObj.optString("email"));
                         articleObj.getPublisher().setNickname(userObj.optString("nickName"));
                         articleObj.getPublisher().setUserId(userObj.optString("userId"));
                         articleObj.getPublisher().setAvatar(userObj.optString("avatar"));
                         articleObj.getPublisher().setIntroduction(userObj.optString("introduction"));
                         articleObj.getPublisher().setMobile(userObj.optString("mobile"));
                         JSONArray tagsArray = userObj.optJSONArray("tags");
                         if (tagsArray != null)
                         {
                             for (int k = 0; k < tagsArray.length(); k++)
                             {
                                 articleObj.getPublisher().getTags().add(tagsArray.optString(k));
                                 
                             }
                         }
                    }
                   
                    articleList.add(articleObj);
                }
                // System.out.println(articleList.get(0)+"\n"+articleList.get(1));
                
//                FooterBar.setVisibility(View.GONE);
//                TextOfFooterBar.setVisibility(View.GONE);
//                
//                Message msg = mBlogScrollView.tripStoryHandler.obtainMessage();
//                msg.what = 0x0011;
//                msg.obj = articleList;
//                msg.arg1 = isRefresh;
//                msg.sendToTarget();
            }
            else
            {
//                FooterBar.setVisibility(View.GONE);
//                TextOfFooterBar.setText("抱歉，未找到相关数据!");
//                Message msgEmpty = mBlogScrollView.tripStoryHandler.obtainMessage();
//                msgEmpty.what = 0x0012;
//                msgEmpty.obj = articleList;
//                msgEmpty.sendToTarget();
            }
            
            // mBlogScrollView = (MicroBlogScrollView)
            // findViewById(R.id.my_scroll_view);
            // mBlogScrollView.setmActivity(this);
//        }
//        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
}
