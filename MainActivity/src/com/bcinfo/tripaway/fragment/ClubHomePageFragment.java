package com.bcinfo.tripaway.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.co;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.CatsInfoActivity;
import com.bcinfo.tripaway.activity.ClubMebActivity;
import com.bcinfo.tripaway.activity.CompanyInfoActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.adapter.ArticleAdapter;
import com.bcinfo.tripaway.adapter.CustAdapter;
import com.bcinfo.tripaway.adapter.GoldMedalAdapter;
import com.bcinfo.tripaway.bean.Article;
import com.bcinfo.tripaway.bean.Cust;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wefika.flowlayout.FlowLayout;

@SuppressLint("ShowToast")
public class ClubHomePageFragment extends BaseFragment {

	private UserInfo userInfo;

	private TextView mCompanyNm;

	private ImageView mIsCertifiedIv;

	private LinearLayout mStarLayout;

	private Context mContext;

	private TextView mCLubIntroMore;
	
	private ViewPager mGoldViewPager;

	public void setContext(Context Context) {
		mContext = Context;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	private TextView mLocationTv;

	private TextView mSerTimesTv;

	private TextView mReferTxt;

	private TextView mClubIntroduce;

	private boolean hasMesure = false;

	private boolean isClickMore = false;

	private int maxLines;

	// 会员人数
	private TextView mMebCount;

	private FlowLayout mAreaLayout;

	private FlowLayout mClubFlowLayout;
	
	private FlowLayout mClubFootFlowLayout;

	private MyListView mServList;

	private List<Article> articleList = new ArrayList<Article>();

	private ArticleAdapter articleAdapter;

	private int servPageNum = 1;

	private int servPageSize = 3;

	private RelativeLayout mServMoreLy;
	
	private List<BaseFragment> goldFragmentList = new ArrayList<BaseFragment>();
	
	private GoldMedalAdapter goldMedalAdapter;
	
	private int destPageNum = 1;
	
	private int destPageSize = 5;
	
	private List<Cust> custList = new ArrayList<Cust>();
	
	private MyListView mDestListView;
	
	private CustAdapter mCustAdapter;
	
	private View mCustLoadMoreLy;
	
	private RelativeLayout goldLayout;
	
	private RelativeLayout companyInfoLayout;

	private LinearLayout recommendLayout;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.club_homepage_fragment, null);
		
		initView(view);
		// 服务内容列表
		queryServList(userInfo.getUserId());
		// 金牌
		queryGoldMedal();
		// 目的地
		queryDestList(userInfo.getUserId());
		registerBoradcastReceiver();
		return view;
	}

	private void initView(View view) {
		companyInfoLayout = (RelativeLayout) view.findViewById(R.id.companyinfo_layout);
		companyInfoLayout.setOnClickListener(clickListener);
		goldLayout = (RelativeLayout) view.findViewById(R.id.gold_layout);
		goldLayout.setOnClickListener(clickListener);
		mDestListView =(MyListView) view.findViewById(R.id.dest_list);
		mLocationTv = (TextView) view.findViewById(R.id.map_location_text);
		mLocationTv.setText(userInfo.getLocation());
		mSerTimesTv = (TextView) view.findViewById(R.id.serTimes_text);
		la=(LinearLayout) view.findViewById(R.id.jcjj);
		mSerTimesTv.setText("累计接待" + userInfo.getServTimes() + "人");
		mCompanyNm = (TextView) view.findViewById(R.id.company_name);
		mCompanyNm.setText(userInfo.getRealName());
		mIsCertifiedIv = (ImageView) view.findViewById(R.id.is_certified);
		if ("yes".equals(userInfo.getIsCertified())) {
			mIsCertifiedIv.setVisibility(View.VISIBLE);
		} else {
			mIsCertifiedIv.setVisibility(View.GONE);
		}
		// 评分更改
		mStarLayout = (LinearLayout) view.findViewById(R.id.star_layout);
		recommendLayout= (LinearLayout) view.findViewById(R.id.recommend_ly);
		HashMap<String, String> map = userInfo.getExts();
		if (map.containsKey("referScore")) {
			String stars = map.get("referScore");
			if (!StringUtils.isEmpty(stars)) {
				LayoutParams params = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(10, 0, 0, 0);
				if (Double.parseDouble(stars) >= 1
						&& Double.parseDouble(stars) < 2) {
					for (int i = 0; i < 1; i++) {
						ImageView img = new ImageView(mContext);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 2
						&& Double.parseDouble(stars) < 3) {
					for (int i = 0; i < 2; i++) {
						ImageView img = new ImageView(mContext);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 3
						&& Double.parseDouble(stars) < 4) {
					for (int i = 0; i < 3; i++) {
						ImageView img = new ImageView(mContext);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 4
						&& Double.parseDouble(stars) < 5) {
					for (int i = 0; i < 4; i++) {
						ImageView img = new ImageView(mContext);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 5) {
					for (int i = 0; i < 5; i++) {
						ImageView img = new ImageView(mContext);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				}
			}else {
				recommendLayout.setVisibility(View.GONE);
			}
		}else {
			recommendLayout.setVisibility(View.GONE);
		}
		mReferTxt = (TextView) view.findViewById(R.id.refer_content);
		if (map.containsKey("referContent")) {
			mReferTxt.setText(map.get("referContent"));
		}

		mClubIntroduce = (TextView) view.findViewById(R.id.club_intro);
		mClubIntroduce.setText(userInfo.getIntroduction());
		// 计算俱乐部介绍高度
		ViewTreeObserver viewTreeObserver = mClubIntroduce
				.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMesure) {
					maxLines = mClubIntroduce.getLineCount();
					if (maxLines > 3) {
						mClubIntroduce.setMaxLines(3);
						mClubIntroduce.setEllipsize(TruncateAt.END);
					}
					if (maxLines > 3) {
						mCLubIntroMore.setVisibility(View.VISIBLE);
					} else {
						mCLubIntroMore.setVisibility(View.GONE);
					}
					hasMesure = true;
				}
				return true;
			}
		});
		mCLubIntroMore = (TextView) view.findViewById(R.id.club_btn_more);
		mCLubIntroMore.setOnClickListener(clickListener);
		mMebCount = (TextView) view.findViewById(R.id.meb_count);
		mMebCount.setText(userInfo.getMembers());
		mClubFootFlowLayout = (FlowLayout) view.findViewById(R.id.clubarea_flowlayout);
		//服务区域
		if(userInfo.getTag().getFootprints() != null){
			List<String> list = userInfo.getTag().getFootprints();
			for (int i = 0; i < list.size(); i++) {
				TextView tv = new TextView(mContext);
				tv.setPadding(DensityUtil.dip2px(mContext, 13), 2,DensityUtil.dip2px(mContext, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(list.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 5);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mClubFootFlowLayout.addView(tv);
			}
		}
		mAreaLayout = (FlowLayout) view.findViewById(R.id.area_flowlayout);
		//服务内容
		if (userInfo.getTag().getServAreas() != null) {
			List<String> list = userInfo.getTag().getServAreas();
			for (int i = 0; i < list.size(); i++) {
				TextView tv = new TextView(mContext);
				tv.setPadding(DensityUtil.dip2px(mContext, 13), 2,DensityUtil.dip2px(mContext, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(list.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 5);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mAreaLayout.addView(tv);
			}
		}
		// 服务类型
		mClubFlowLayout = (FlowLayout) view
				.findViewById(R.id.clubtype_flowlayout);
		if (null != userInfo.getTag().getClubTypes()) {
			List<String> clubtypeList = userInfo.getTag().getClubTypes();
			for (int i = 0; i < clubtypeList.size(); i++) {
				TextView tv = new TextView(mContext);
				tv.setPadding(DensityUtil.dip2px(mContext, 13), 2,DensityUtil.dip2px(mContext, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(clubtypeList.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 0);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mClubFlowLayout.addView(tv);
			}
		}

		mServList = (MyListView) view.findViewById(R.id.serv_list);
		articleAdapter = new ArticleAdapter(mContext, articleList);
		mServList.setAdapter(articleAdapter);
		mServList.setOnItemClickListener(itemClickListener);
		articleAdapter.notifyDataSetChanged();
		mServMoreLy = (RelativeLayout) view.findViewById(R.id.serv_more_btn);
		mServMoreLy.setOnClickListener(clickListener);
		mGoldViewPager = (ViewPager) view.findViewById(R.id.goldmedal_page);
		mGoldViewPager.setPageMargin(5);
		if(goldFragmentList.size()>0){
			mGoldViewPager.setCurrentItem(0);
		}
//		for (int i = 0; i < 5; i++) {
//			goldFragmentList.add(new GoldMedalFragment());
//		}
		mGoldViewPager.setOffscreenPageLimit(goldFragmentList.size());
		goldMedalAdapter = new GoldMedalAdapter(getChildFragmentManager(), goldFragmentList,(Activity)mContext);
		mGoldViewPager.setAdapter(goldMedalAdapter);
		mGoldViewPager.setOnPageChangeListener(new OnPageChangeListener() {
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
		
		mCustAdapter = new CustAdapter(mContext, custList);
		
		mCustLoadMoreLy =(View) LayoutInflater.from(mContext).inflate(R.layout.load_more_footer, null);
		mCustLoadMoreLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				queryDestList(userInfo.getUserId());
			}
		});
		mDestListView.addFooterView(mCustLoadMoreLy);
		mDestListView.setOnItemClickListener(itemClickListener);
		mDestListView.setAdapter(mCustAdapter);
		mCustAdapter.notifyDataSetChanged();
		
	
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.club_btn_more:
				isClickMore = !isClickMore;
				if (isClickMore) {
					mCLubIntroMore.setText("收起");
					mClubIntroduce.setMaxLines(maxLines);
					mClubIntroduce.requestLayout();
					// mClubIntroduce.postInvalidate();
				} else {
					mCLubIntroMore.setText("展开");
					mClubIntroduce.setMaxLines(3);
					mClubIntroduce.requestLayout();
					// mClubIntroduce.postInvalidate();
				}
				break;
			case R.id.serv_more_btn:
				queryServList(userInfo.getUserId());
				break;
			case R.id.gold_layout:
				Intent it = new Intent(mContext,ClubMebActivity.class);
				it.putExtra("userId",userInfo.getUserId());
				startActivityForResult(it, 1);
				break;
			case R.id.companyinfo_layout:
				Intent its = new Intent(mContext,CompanyInfoActivity.class);
				its.putExtra("userId",userInfo.getUserId());
				startActivity(its);
				break;
			default:
				break;
			}
		}
	};
	LinearLayout la=null;
	private void queryServList(String userId) {
		RequestParams params = new RequestParams();
		params.put("type", "service");
		params.put("pagenum", servPageNum);
		params.put("pagesize", servPageSize);
		params.put("type", "service");
		params.put("userId", userId);
		
		HttpUtil.get(Urls.article_list, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					List<Article> list = new ArrayList<Article>();
					JSONArray data = response.optJSONArray("data");
					if (data != null) {
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.optJSONObject(i);
							Article art = new Article();
							art.setArticleId(obj.optString("articleId"));
							art.setTitle(obj.optString("title"));
							art.setAbstracts(obj.optString("abstracts"));
							art.setCover(obj.optString("cover"));
							JSONArray imgArray = obj.optJSONArray("pics");
							if (imgArray != null) {
								List<ImageInfo> imageList = new ArrayList<ImageInfo>();
								for (int j = 0; j < imgArray.length(); j++) {
									JSONObject imgObj = imgArray
											.optJSONObject(j);
									ImageInfo info = new ImageInfo();
									info.setDesc(imgObj.optString("desc"));
									info.setWidth(imgObj.optString("width"));
									info.setHeight(imgObj.optString("height"));
									info.setUrl(imgObj.optString("url"));
									imageList.add(info);
								}
								art.setImageList(imageList);
							}
							list.add(art);
						}
					}
					servPageNum ++;
					if (list.size() != 0) {
						articleList.addAll(list);

						articleAdapter.notifyDataSetChanged();
					}else{
						la.setVisibility(View.GONE);
					}
					if (list.size() < servPageSize) {
						mServMoreLy.setVisibility(View.GONE);
					} else {
						mServMoreLy.setVisibility(View.VISIBLE);
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	
	private void queryGoldMedal(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", "all");
			jsonObject.put("keyword", "");
			jsonObject.put("userId", userInfo.getUserId());
			jsonObject.put("isGold", "yes");
			jsonObject.put("pagenum", 1);
			jsonObject.put("pagesize",15);
			StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.goldmedal_list, entity,new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if(!"00000".equals(code)){
						return;
					}
					List<UserInfo> userList= new ArrayList<UserInfo>();

		            LogUtil.i("greed", "abbc", response.toString());
		            String sb=response.toString();
					JSONArray data =response.optJSONArray("data");
					if(data != null){
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.optJSONObject(i);
							UserInfo info = new UserInfo();
							info.setUserId(obj.optString("userId"));
							info.setAvatar(obj.optString("avatar"));
							info.setNickname(obj.optString("nickName"));
							info.setUserName(obj.optString("usersName"));
							info.setRealName(obj.optString("realName"));
							info.setFansCount(obj.optString("fansCount"));
							info.setIsFocus(obj.optString("isFocus"));
							info.setIsGold(obj.optString("isGold"));
							JSONObject orgRole = obj.optJSONObject("orgRole");
							JSONObject tags = obj.optJSONObject("tags");
							Tags tag = new Tags();
							if(null != tags){
								JSONArray interest = tags.optJSONArray("interest");
								List<String> interList = new ArrayList<String>();
								if(null != interest){
									for(int j = 0;j<interest.length();j++){
										interList.add(interest.optString(j));
									}
								}
								tag.setInterests(interList);
								info.setTag(tag);
							}
							if(null != orgRole){
								OrgRole role= new OrgRole();
								role.setRoleName(orgRole.optString("roleName"));
								role.setRoleCode(orgRole.optString("roleCode"));
								info.setOrgRole(role);
							}
							userList.add(info);
						}
					}
					for (int i = 0; i < userList.size(); i++) {
						UserInfo infos = userList.get(i);
						GoldMedalFragment fragment = new GoldMedalFragment(mContext, infos);
						
//						if(i == 0){
//							Bundle bundle = new Bundle();
//							bundle.putString("isFirst", "yes");
//							fragment.setArguments(bundle);
//						}
						
						goldFragmentList.add(fragment);
					}
					if(goldFragmentList.size()==0){
						mGoldViewPager.setVisibility(View.GONE);
					}else
					goldMedalAdapter.notifyDataSetChanged();
				}
				
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	   public void registerBoradcastReceiver()
	    {
	        IntentFilter myIntentFilter = new IntentFilter();
	        myIntentFilter.addAction("com.bcinfo.refreshFocus");
	        // 注册广播
	        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	    }
	   
	   private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	    {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            String action = intent.getAction();
	            if("com.bcinfo.refreshFocus".equals(action)){
	            	String userId=intent.getStringExtra("userId");
	            	boolean isFocus=intent.getBooleanExtra("isFocus",false);
	            	if(userId!=null){
	            		for(BaseFragment fragment:goldFragmentList){
	            			UserInfo userInfo=((GoldMedalFragment)fragment).getUserInfo();
	            			if(userInfo!=null&&userInfo.getUserId().equals(userId)){
	            				((GoldMedalFragment)fragment).setFocusText(isFocus);
	            				break;
	            			}
	            		}
	            	}
	            }
	        }
	    };
	    
	private void queryDestList(String userId){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pagenum", destPageNum);
		params.put("pagesize", destPageSize);
		
		HttpUtil.get(Urls.cust_list, params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				
				String code = response.optString("code");
				
				List<Cust> list = new ArrayList<Cust>();
				if("00000".equals(code)){
					JSONArray data = response.optJSONArray("data");
					if(null != data){
						for(int i = 0;i<data.length();i++){
							JSONObject obj = data.optJSONObject(i);
							Cust cust = new Cust();
							if(null != obj){
								cust.setCatId(obj.optString("catId"));
								cust.setCover(obj.optString("cover"));
								cust.setCustTitle(obj.optString("custTitle"));
								cust.setLineNum(obj.optString("lineNum"));
								
								ProductNewInfo product = new ProductNewInfo();
								
								JSONObject proObj = obj.optJSONObject("product");
								if(null != proObj){
									product.setId(proObj.optString("id"));
									product.setTitle(proObj.optString("title"));
									product.setTitleImg(proObj.optString("titleImg"));
								}
								cust.setProduct(product);
								list.add(cust);
							}
						}
						if(list.size() < destPageSize){
							mDestListView.removeFooterView(mCustLoadMoreLy);
						}else{
							destPageNum++;
						}
						custList.addAll(list);
						mCustAdapter.notifyDataSetChanged();
					}else{
						destPageNum -- ;
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				destPageNum = 1;
				custList.clear();
				mCustAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(parent.getId() == R.id.dest_list){
				Cust cust = (Cust)parent.getItemAtPosition(position);
				Intent it = new Intent(mContext,CatsInfoActivity.class);
				it.putExtra("cust", cust);
				it.putExtra("userId", userInfo.getUserId());
				startActivity(it);
			}else if(parent.getId() ==R.id.serv_list){
				Article art = (Article)parent.getItemAtPosition(position);
				String path = Urls.content_host +art.getArticleId();
				Intent it = new Intent(mContext,ContentActivity.class);
				it.putExtra("path", path);
				mContext.startActivity(it);
			}
		}
	};
	
	@Override
	 public void onDestroy() {
	    	super.onDestroy();
	    	getActivity().unregisterReceiver(mBroadcastReceiver);
	    };
}
