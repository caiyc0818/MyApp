package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity.OnLoadMoreListener;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ProductJoinMebActivity;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductInfoAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.FilterLayout.OnDaysListener;
import com.bcinfo.tripaway.view.FilterLayout.OnDestinationListener;
import com.bcinfo.tripaway.view.FilterLayout.OnPriceListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProductInfoFragment extends Fragment implements OnClickListener,OnLoadMoreListener {
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
	
	private int pageNum=1;
	private int pageSize=10;
	
	private View titleView;
	
	private TextView ok;
	private TextView all;
	
	private List<Dest> dests =new ArrayList<Dest>();
	
	List<String> destinationStrs =new ArrayList<String>();
	List<String> priceStrs =new ArrayList<String>();
	List<String> daysStrs =new ArrayList<String>();
	
	private String priceStrsMatch[]= {"lt2000","2001-3000","3001-5000","5000-1W","1W-5W","gt5W"};
	private String daysStrsMatch[]= {"lt5","5-10","10-15","gt15"};

	private int isFilterChange[][]={{0,0},{0,0},{0,0}};

	private TextView cancel;
private ImageView noproduct;
private LinearLayout maintitle;

private LinearLayout mViewFooter;
	public ProductInfoFragment(String userId,View view) {
		this.userId = userId;
		this.titleView=view;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.product_info, null);// 注意不要指定父视图
		// but = (TextView) view.findViewById(R.id.vipNum);
		initView(view);
		initPop();

		// setValue();
		//queryDestInfo();
		queryProductInfo();

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
		noproduct = (ImageView)view.findViewById(R.id.noproduct);
		maintitle = (LinearLayout)view.findViewById(R.id.maintitle);
		startFilter = (LinearLayout) view.findViewById(R.id.startFilter);
		productDetailList = (ListView) view
				.findViewById(R.id.product_detail_listview);
		all=(TextView) view
				.findViewById(R.id.all);
		productDetailList.setFocusable(false);
		all.setOnClickListener(this);
		startFilter.setOnClickListener(this);
		productInfoAdapter = new ProductInfoAdapter(getActivity(), productList);
		productDetailList.setAdapter(productInfoAdapter);
		productDetailList.setOnItemClickListener(mSimillarListListener);
		registerBoradcastReceiver();
        mViewFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.xlistview_footer1, null);
        productDetailList.addFooterView(mViewFooter);
	}

	private void initPop() {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.filter_pop_window, null);
		// 设置按钮的点击事件
		filterLayout = (FilterLayout) contentView
				.findViewById(R.id.filterLayout);
		cancel=(TextView) contentView
				.findViewById(R.id.cancel);
		ok=(TextView) contentView
				.findViewById(R.id.ok);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		queryDestInfo();
		destinationStrs.add("西藏");
		destinationStrs.add("斯里兰卡");
		destinationStrs.add("重庆");
		destinationStrs.add("桂林");
		destinationStrs.add("印度");
		filterLayout.setDestinationValue(destinationStrs);
		priceStrs.add("2000元以内");
		priceStrs.add("2001-3000");
		priceStrs.add("3001-5000");
		priceStrs.add("5001-1W");
		priceStrs.add("1W-5W");
		priceStrs.add("5W以上");
		filterLayout.setPriceValue(priceStrs);
		daysStrs.add("5日以内");
		daysStrs.add("5-10日");
		daysStrs.add("10-15日");
		daysStrs.add("15日以上");
		filterLayout.setDaysValue(daysStrs);
		filterLayout.setOnDaysListener(new OnDaysListener() {
			
			@Override
			public void OnCheck(String Value, int position,boolean state) {
				// TODO Auto-generated method stub
				isFilterChange[2][0]=1;
				isFilterChange[2][1]=position;
			}
		});
filterLayout.setOnDestinationListener(new OnDestinationListener() {
	
	@Override
	public void OnCheck(String Value, int position,boolean state) {
		// TODO Auto-generated method stub
		isFilterChange[0][0]=1;
		isFilterChange[0][1]=position;
	}
});

filterLayout.setonPriceListener(new OnPriceListener() {
	
	@Override
	public void OnCheck(String Value, int position,boolean state) {
		// TODO Auto-generated method stub
		isFilterChange[1][0]=1;
		isFilterChange[1][1]=position;
	}
});
		
		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(mContext, "button is pressed",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f); 
			}
		});
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}

		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.transparent));
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

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

	  public void backgroundAlpha(float bgAlpha)  
	    {  
	        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();  
	        lp.alpha = bgAlpha; //0.0-1.0  
	        getActivity().getWindow().setAttributes(lp);  
	    }  
	  
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startFilter:
			// 设置好参数之后再show
			int[] location = new int[2];  
			titleView.getLocationOnScreen(location);
			backgroundAlpha(0.5f);  
			popupWindow.showAtLocation(startFilter, Gravity.RIGHT,  0, 0);
			break;
		case R.id.ok:
			if(isFilterChange[0][0]!=0||isFilterChange[1][0]!=0||isFilterChange[2][0]!=0){
				productList.clear();
				isNoMore=false;
				pageNum=1;
				queryProductInfo();
				isFilterChange[0][0]=0;
				isFilterChange[1][0]=0;
				isFilterChange[2][0]=0;
			}
			popupWindow.dismiss();
			break;
		case R.id.cancel:
			popupWindow.dismiss();
			break;
		case R.id.all:
			productList.clear();
			queryProductInfo();
			break;

		}
	}

	/**
	 * 查询个人相关目的地
	 * 
	 * @param userId
	 */
	private void queryDestInfo() {
		RequestParams params = new RequestParams();
		params.put("pagesize", pageSize);
		params.put("pagenum", pageNum);
		params.put("userId", userId);
		HttpUtil.get(Urls.get_dest, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response
							.optJSONObject("data");
					if(data!=null&&data.length()>0){
					JSONArray destArray = response
							.optJSONArray("dest");
					if (destArray != null&&destArray.length()>0) {
						for (int i = 0; i < destArray.length(); i++) {
							Dest dest=new Dest();
							dest.setDestId(destArray.optJSONObject(i).optString("destId"));
							dest.setDestName(destArray.optJSONObject(i).optString("destName"));
							dests.add(dest);
							if(dests.size()>0){
								maintitle.setVisibility(View.VISIBLE);
							
								}
						}
					}
					}
					
				}
				if(dests.size()==0){
					filterLayout.setDestinationLayoutGone();
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

	private boolean isNoMore=false;
	
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.loadMoreProducts1");
		// 注册广播
		getActivity().registerReceiver(broadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.loadMoreProducts1".equals(action)) {
				++pageNum;
				queryProductInfo();
			}
		}
	};
	boolean isLoading=false;
	/**
	 * test 产品详情url
	 * 
	 * @param userId
	 */
	private void queryProductInfo() {
		if(isNoMore||isLoading){
			--pageNum;
			return;
			}
		isLoading=true;
		RequestParams params = new RequestParams();
		params.put("pagesize", pageSize);
		params.put("pagenum",pageNum );
		params.put("userId", userId);
		if(isFilterChange[0][0]!=0){
			params.put("destId", destinationStrs.get(isFilterChange[0][1]));
		}
		if(isFilterChange[1][0]!=0){
			params.put("price", priceStrsMatch[isFilterChange[1][1]]);
		}
		if(isFilterChange[2][0]!=0){
			params.put("day", daysStrsMatch[isFilterChange[2][1]]);
		}
		HttpUtil.get(Urls.get_plist, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray productArray = response.optJSONArray("data");
					if (productArray != null && productArray.length() != 0) {
						
					} else {
						((ClubMebHomepageActivity) getActivity()).noMoreFinish();
						--pageNum;
						isNoMore=true;
						mViewFooter.setVisibility(View.GONE);
//						productDetailList.removeFooterView(mViewFooter);
					}
					if (productArray!=null&&productArray.length() > 0) {
						if(productArray.length()%pageSize!=0){
							isNoMore=true;
							mViewFooter.setVisibility(View.GONE);
//							productDetailList.removeFooterView(mViewFooter);
						}
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
											.optJSONObject(j);
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
											topics.opt(j).toString());
								}
							}
							if (product.optJSONObject("exts") != null) {
								HashMap<String, String> exts = new HashMap<String, String>();
								if (!StringUtils
										.isEmpty(product.optJSONObject("exts").optString("refer_tags"))) {
									exts.put("refer_tags",
											product.optJSONObject("exts").optString("refer_tags"));
								}
								if (!StringUtils
										.isEmpty(product.optJSONObject("exts").optString("big_refer_tags"))) {
									exts.put("big_refer_tags",
											product.optJSONObject("exts").optString("big_refer_tags"));
								}
								if (exts.size() > 0) {
									productInfo.setExts(exts);
								}
							}
							productInfo.setOriginalPrice(product.optString("originalPrice"));
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
							if(productList.size()>0){
								maintitle.setVisibility(View.VISIBLE);
								}
						}
						
						((ClubMebHomepageActivity) getActivity()).loadmoreFinish();
					}
					productInfoAdapter.notifyDataSetChanged();
					//setListViewHeight(productDetailList);
				}
//				判断没数据 展示NULL
				showNull();
				isLoading=false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoading=false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoading=false;
			}
		});
	}

	private void showNull() {
		if(dests.size()==0&&productList.size()==0){
			noproduct.setVisibility(View.VISIBLE);
			maintitle.setVisibility(View.GONE);
		}else 
			noproduct.setVisibility(View.GONE);
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
	
	public void setListViewHeight(ListView listView) {  
		  
	    // 获取ListView对应的Adapter  
	  
	    ListAdapter listAdapter = listView.getAdapter();  
	  
	    if (listAdapter == null) {  
	        return;  
	    }  
	    int totalHeight = 0;  
	    for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目  
	        View listItem = listAdapter.getView(i, null, listView);  
	        listItem.measure(0, 0); // 计算子项View 的宽高  
	        totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度  
	    }  
	  
	    ViewGroup.LayoutParams params = listView.getLayoutParams();  
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	    listView.setLayoutParams(params);}
	
	
	@Override
	public int isNeedLoad() {
		// TODO Auto-generated method stub
		if (productList.size() == 0) {
			return 0;
		} else if (productList.size() % pageSize == 0) {
			pageNum = pageNum + 1;
			// pagesize = pagesize + 10;
			queryProductInfo();
			return 1;
		}
		return 2;
	}
	
	
	  OnItemClickListener mSimillarListListener = new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	            // TODO Auto-generated method stub
	            Intent intent = null;
	            ProductNewInfo productNewInfo = productList.get(position);
	            if (productNewInfo.getProductType().equals("single"))
	            {
	                if (productNewInfo.getServices().equals("traffic"))
	                {
	                    intent = new Intent(getActivity(), CarProductDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	                else if (productNewInfo.getServices().equals("stay"))
	                {
	                    intent = new Intent(getActivity(), HouseProductDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	                else
	                {
	                    intent = new Intent(getActivity(), GrouponProductNewDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	            }
	            else if (productNewInfo.getProductType().equals("base")
	                || productNewInfo.getProductType().equals("customization"))
	            {
	                intent = new Intent(getActivity(), GrouponProductNewDetailActivity.class);
	                intent.putExtra("productId", productNewInfo.getId());
	            }
	            else if (productNewInfo.getProductType().equals("team"))
	            {
	                intent = new Intent(getActivity(), GrouponProductNewDetailActivity.class);
	                intent.putExtra("productId", productNewInfo.getId());
	            }
	            intent.putExtra("productTitle", productNewInfo.getTitle());
	            startActivity(intent);
	            activityAnimationOpen();
	        }
	    };
	    
	    /**
	     * 页面启动动画
	     */
	    protected void activityAnimationOpen()
	    {
	        getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
	    }
		
}
