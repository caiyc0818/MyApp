package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.bcinfo.tripaway.view.refreshandload.PullableScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class PersonalHomepageActivity extends BaseActivity implements OnClickListener
{
    
    protected static final String String = null;
    
    private ImageView serverIcon;
    
    private ImageView vIcon;
    
    private TextView personRealname;
    
    private TextView companyRealname;
    
    private TextView serverAge;
    
    private TextView serverGender;
    
    private TextView serverBirthday;
    
    private TextView serverAddress;
    
    private TextView serverCompany;
    
    private TextView serverLanguage;
    
    private TextView serverInterest;
    
    private TextView serverNickname;
    
    private TextView serverIntroduction;
    
    private TextView personalProductCount;
    
    private TextView serverBirthDay;
    
    private TextView company_hp_name;
    
    private ArrayList<Comments> userlist = new ArrayList<Comments>();
    
    private RelativeLayout travelExperience;
    
    private RelativeLayout layout_product_detail_title;
    
    private static final String TAG = "PersonalHomepageActivity";
    
    private LinearLayout personal_info_layout;
    
    private LinearLayout organization_info_layout;
    
    private ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
    
    private TextView company_date;
    
    private TextView company_address;
    
    private String productId;
    
    private String userId;
    
    private UserInfo userInfo;
    
    private LinearLayout layout_back_button;
    
    private ImageView personal_hp_v_icon_iv;
    
    private ImageView company_hp_v_icon_iv;
    
    private View grey_view;
    
    private int pageNum = 1;
    
    private int pageSize = 10;
    
    private boolean isPullRefresh = false;
    
    private boolean isLoadMore = false;
    
    // 自定义上拉加载相对布局
    private PullToRefreshLayout pullToRefreshLayout;
    
    // 自定义滑动
    private PullableScrollView pullableScrollView;
    
    // 发布的产品列表
    private MyListView personal_product_listview;
    
    /*
     * 发布的产品列表适配器
     */
    private ProductAdapter personalProductListAdapter;
    
    private ArrayList<ProductNewInfo> personalProductArrayList = new ArrayList<ProductNewInfo>();// 产品集合
    
    private LinearLayout person_back_pic;
    
    private String backgroundUri;
    
    private ImageView personpic;
    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.personal_homepage);
        initUserInfoView();
        initViewGone();
        layout_product_detail_title = (RelativeLayout)findViewById(R.id.layout_product_detail_title);
        layout_product_detail_title.getBackground().setAlpha(255);
        personal_product_listview = (MyListView)findViewById(R.id.personal_product_listview);
       
        productId = getIntent().getStringExtra("productId");
        
        userId = getIntent().getStringExtra("userId");
        backgroundUri= getIntent().getStringExtra("background");
        personpic=(ImageView) findViewById(R.id.personpic);
        personpic.setImageResource(R.drawable.clubmeb_bg);
        if(!StringUtils.isEmpty(backgroundUri)){
        	ImageLoader.getInstance().displayImage(Urls.imgHost+backgroundUri,personpic,AppConfig.options(R.drawable.clubmeb_bg));
        }
        
        QueryUserInfo(productId);
        QueryUserProduct(userId);
      
        personalProductListAdapter = new ProductAdapter(this, personalProductArrayList);
        personal_product_listview.setAdapter(personalProductListAdapter);
        personal_product_listview.setOnItemClickListener(personalProductListListener);
        // initPersonalProductListView(personalProductArrayList);
       
       
    }
   
    //初始化一些控件消失
    private void initViewGone(){
    	personal_info_layout.setVisibility(View.GONE);
        organization_info_layout.setVisibility(View.GONE);
        personal_hp_v_icon_iv.setVisibility(View.GONE);
        company_hp_v_icon_iv.setVisibility(View.GONE);
        travelExperience.setVisibility(View.GONE);
        grey_view.setVisibility(View.GONE);
    }
    
    // TODO 给发布的产品添加数据
    private void initPersonalProductListView(ArrayList<ProductNewInfo> productList)
    {
        personal_product_listview = (MyListView)findViewById(R.id.personal_product_listview);
        personalProductArrayList.addAll(productList);
        personalProductListAdapter = new ProductAdapter(this, personalProductArrayList);
        personal_product_listview.setAdapter(personalProductListAdapter);
        personal_product_listview.setOnItemClickListener(personalProductListListener);
    }
    ProductNewInfo productNewInfo;
    private OnItemClickListener personalProductListListener = new OnItemClickListener()
    {
        
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            Intent intent = null;
             productNewInfo = personalProductArrayList.get(position);
            LogUtil.i(TAG, "onItemClick", "position=" + position);
            /*
             * if (productNewInfo.getProductType().equals("single")) { if
             * (productNewInfo.getServices().equals("traffic")) { intent = new Intent(PersonalHomepageActivity.this,
             * CarProductDetailActivity.class); intent.putExtra("productId", productNewInfo.getId()); } else if
             * (productNewInfo.getServices().equals("stay")) { intent = new Intent(PersonalHomepageActivity.this,
             * HouseProductDetailActivity.class); intent.putExtra("productId", productNewInfo.getId()); } else {
             */
            
            //判断：若产品下架显示下架界面，否则显示有信息的界面
            HttpUtil.get(Urls.product_detail +productNewInfo.getId(),
    				new JsonHttpResponseHandler(){
    			@Override
    			public void onSuccess(int statusCode, Header[] headers,
    					JSONObject response) {
    				// LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
    				// statusCode);
    				LogUtil.i(TAG, "QueryProductDetail", "response="
    						+ response.toString());
    				
    				String code = response.optString("code");
    				String msg = response.optString("msg");
    				if (!code.equals("00000")) {
    					
    					Intent intent = new  Intent(PersonalHomepageActivity.this, ProductOutTimeActivity.class);
    					startActivity(intent);
    			        activityAnimationOpen();
    				}else {
    					Intent intent = new  Intent(PersonalHomepageActivity.this, GrouponProductNewDetailActivity.class);
    					intent.putExtra("productId", productNewInfo.getId());
    					startActivity(intent);
    			        activityAnimationOpen();
					}
    					return;
    				}
    			
    			@Override
    			public void onFailure(int statusCode, Header[] headers,
    					String responseString, Throwable throwable) {
    				// TODO Auto-generated method stub
    				super.onFailure(statusCode, headers, responseString,
    						throwable);
    				LogUtil.i(TAG, "QueryProductDetail", "statusCode="
    						+ statusCode);
    				LogUtil.i(TAG, "QueryProductDetail", "responseString="
    						+ responseString);
    				for (int i = 0; i < headers.length; i++) {
    					LogUtil.i(TAG, "QueryProductDetail", "key-->"
    							+ headers[i].getName() + "--getValue-->"
    							+ headers[i].getValue());
    				}
    			}

    		});
            
            
            
//            intent = new Intent(PersonalHomepageActivity.this, ProductDetailNewActivity.class);
//            intent.putExtra("productId", productNewInfo.getId());
            // }
            /*
             * } else if (productNewInfo.getProductType().equals("base") ||
             * productNewInfo.getProductType().equals("customization")) { intent = new
             * Intent(PersonalHomepageActivity.this, ProductDetailNewActivity.class); intent.putExtra("productId",
             * productNewInfo.getId()); } else if (productNewInfo.getProductType().equals("team")) { intent = new
             * Intent(PersonalHomepageActivity.this, GrouponProductNewDetailActivity.class);
             * intent.putExtra("productId", productNewInfo.getId()); }
             */
//            startActivity(intent);
//            activityAnimationOpen();
        }
        
    };
    
    private void QueryProductDetail(String productId) {
    	
    	HttpUtil.get(Urls.product_detail + productId,
				new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
				// statusCode);
				LogUtil.i(TAG, "QueryProductDetail", "response="
						+ response.toString());
				
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					
					
				}
					return;
				}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString,
						throwable);
				LogUtil.i(TAG, "QueryProductDetail", "statusCode="
						+ statusCode);
				LogUtil.i(TAG, "QueryProductDetail", "responseString="
						+ responseString);
				for (int i = 0; i < headers.length; i++) {
					LogUtil.i(TAG, "QueryProductDetail", "key-->"
							+ headers[i].getName() + "--getValue-->"
							+ headers[i].getValue());
				}
			}

		});
	
			
			
			
    }
    
    private void QueryUserProduct(String userId)
    {
        
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pagenum", pageNum);
        params.put("pagesize", pageSize);
        HttpUtil.get(Urls.ta_product, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "QueryUserProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryUserProduct", "response=" + response.toString());
                if (isLoadMore)
                {
                    // 上拉返回的结束加载更多布局
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                if (isPullRefresh)
                {
                    // 下拉刷新返回的
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
                // JSONObject dataJSON = response.optJSONObject("data");
                ArrayList<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();
                productList.clear();
                // if (dataJSON != null) {
                JSONArray productJsonList = response.optJSONArray("data");
                if (productJsonList == null)
                {
                    return;
                }
                for (int i = 0; i < productJsonList.length(); i++)
                {
                    JSONObject productJson = productJsonList.optJSONObject(i);
                    ProductNewInfo productNewInfo = new ProductNewInfo();
                    productNewInfo.setId(productJson.optString("id"));
                    productNewInfo.setTitle(productJson.optString("title"));
                    productNewInfo.setPrice(productJson.optString("price"));
                    productNewInfo.setDescription(productJson.optString("description"));
                    productNewInfo.setPriceMax(productJson.optString("priceMax"));
                    productNewInfo.setTitleImg(productJson.optString("titleImg"));
//                    
                  
                    JSONArray topicjsonList = productJson.optJSONArray("topics");
                    String topics = "";
                    ArrayList<String> topicsList = new ArrayList<String>();
                    if (topicjsonList != null && topicjsonList.length() > 0)
                    {
                        for (int k = 0; k < topicjsonList.length(); k++)
                        {
                            
                            try
                            {
                                topicsList.add((java.lang.String)topicjsonList.get(k));
                            }
                            catch (JSONException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            
                        }
                        productNewInfo.setTopics(topicsList);
                    }
                    productList.add(productNewInfo);
                    
                }
                
                personalProductArrayList.addAll(productList);
                personalProductListAdapter.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                // initPersonalProductListView(productList);
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "QueryUserProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryUserProduct", "responseString=" + responseString);
            }
            
        });
        
    }
    

    	
    private void QueryUserInfo(String productId)
    {
        
        // LogUtil.e(TAG, "QueryProductDetail", Urls.product_detail +
        // productId);
        HttpUtil.get(Urls.detail_personal_url + productId, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
                // statusCode);
                LogUtil.i(TAG, "QueryPersonalInfo", "response=" + response.toString());
                System.out.println("QueryPersonalInfo:" + "response=" + response.toString());
                userInfo = new UserInfo();
                String code = response.optString("code");
                String msg = response.optString("msg");
                if (!code.equals("00000"))
                {
                    ToastUtil.showErrorToast(PersonalHomepageActivity.this, msg);
                    return;
                }
                userInfoList.clear();
                JSONObject data = response.optJSONObject("data");
                if (data == null || data.toString().equals("") || data.toString().equals("null"))
                {
                    return;
                }
                JSONObject userInfoObject = data.optJSONObject("userInfo");
                if (userInfo == null || userInfo.toString().equals("") || userInfo.toString().equals("null"))
                {
                    return;
                }
                userInfo.setUserId(userInfoObject.optString("userId"));
                userInfo.setUserType(userInfoObject.optString("userType"));
                userInfo.setAvatar(userInfoObject.optString("avatar"));
                userInfo.setNickname(userInfoObject.optString("nickName"));
                userInfo.setPermission(userInfoObject.optString("profession"));
                userInfo.setIsCertified(userInfoObject.optString("isCertified"));
                userInfo.setRealName(userInfoObject.optString("realName"));
                userInfo.setGender(userInfoObject.optString("sex"));
                userInfo.setIntroduction(userInfoObject.optString("introduction"));
                userInfo.setBirthday(userInfoObject.optString("birthday"));
                userInfo.setAddress(userInfoObject.optString("address"));
                userInfo.setOrgInfo(userInfoObject.optString("orgInfo"));
                userInfo.setProductCount(userInfoObject.optString("productCount"));
                // 获取语言数组
                JSONArray languagesJSONList = userInfoObject.optJSONArray("languages");
                if (languagesJSONList == null || languagesJSONList.toString().equals("")
                    || languagesJSONList.toString().equals("null"))
                {
                    return;
                }
                ArrayList<String> languages = new ArrayList<String>();
                for (int i = 0; i < languagesJSONList.length(); i++)
                {
                    try
                    {
                        languages.add((java.lang.String)languagesJSONList.get(i));
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                userInfo.setLanguagesList(languages);
                
                // 获取旅行经历
                JSONArray experiencesJSONList = userInfoObject.optJSONArray("experiences");
                if (experiencesJSONList == null || experiencesJSONList.toString().equals("")
                    || experiencesJSONList.toString().equals("null"))
                {
                    System.out.println("experiencesJSONList是空" + experiencesJSONList.toString());
                    return;
                }
                for (int j = 0; j < experiencesJSONList.length(); j++)
                {
                    JSONObject experienceJSON = experiencesJSONList.optJSONObject(j);
                    Experiences experience = new Experiences();
                    experience.setDescription(experienceJSON.optString("description"));
                    experience.setTravelTime(experienceJSON.optString("travelTime"));
                    JSONArray imageJSONList = experienceJSON.optJSONArray("images");
                    if (imageJSONList == null || imageJSONList.toString().equals("")
                        || imageJSONList.toString().equals("null"))
                    {
                        return;
                    }
                    for (int m = 0; m < imageJSONList.length(); m++)
                    {
                        ImageInfo imageInfo = new ImageInfo();
                        JSONObject imageJSON = imageJSONList.optJSONObject(m);
                        imageInfo.setUrl(imageJSON.optString("url"));
                        imageInfo.setDesc(imageJSON.optString("description"));
                        experience.getImages().add(imageInfo);
                        
                    }
                    userInfo.getExperiencesList().add(experience);
                    
                }
                
                initData(userInfo);
                userInfoList.add(userInfo);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "QueryProductDetail", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryProductDetail", "responseString=" + responseString);
                for (int i = 0; i < headers.length; i++)
                {
                    LogUtil.i(TAG,
                        "QueryProductDetail",
                        "key-->" + headers[i].getName() + "--getValue-->" + headers[i].getValue());
                }
            }
            
        });
        
    }
    
    private void initData(UserInfo userInfo)
    {
        if (!StringUtils.isEmpty(userInfo.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(),
                serverIcon,
                AppConfig.options(R.drawable.user_defult_photo));
        }        
        serverNickname.setText(StringUtils.isEmpty(userInfo.getNickname())?"":userInfo.getNickname());
        serverIntroduction.setText(StringUtils.isEmpty(userInfo.getIntroduction())?"":userInfo.getIntroduction());
        personalProductCount.setText(StringUtils.isEmpty(userInfo.getProductCount())?"":userInfo.getProductCount());
        if (userInfo.getUserType().equals("person"))
        {
            personal_info_layout.setVisibility(View.VISIBLE);
            organization_info_layout.setVisibility(View.GONE);
            personal_hp_v_icon_iv.setVisibility(View.VISIBLE);
            company_hp_v_icon_iv.setVisibility(View.GONE);
//            travelExperience.setVisibility(View.VISIBLE);
            grey_view.setVisibility(View.VISIBLE);
            serverBirthDay.setText(StringUtils.isEmpty(userInfo.getBirthday())?"":userInfo.getBirthday());
            if (userInfo.getRealName() != null && !userInfo.getRealName().equals("null"))
            {
                personRealname.setText(userInfo.getRealName());
            }
            else
            {
                personRealname.setText("-");
            }
            if (userInfo.getGender().equals("0"))
            {
                serverGender.setText("女");
            }
            else if (userInfo.getGender().equals("1"))
            {
                serverGender.setText("男");
            }
            else
            {
                serverGender.setText("-");
            }
            
            serverAddress.setText(StringUtils.isEmpty(userInfo.getAddress())?"":userInfo.getAddress());
            if (userInfo.getOrgInfo() != null && !userInfo.getOrgInfo().equals("null"))
            {
                serverCompany.setText(userInfo.getOrgInfo());
            }
            else
            {
                serverCompany.setText("-");
            }
            String Language = "";
            for (int k = 0; k < userInfo.getLanguagesList().size(); k++)
            {
                if (k < userInfo.getLanguagesList().size() - 1)
                {
                    Language = Language + userInfo.getLanguagesList().get(k) + "、";
                    
                }
                else
                {
                    Language = Language + userInfo.getLanguagesList().get(k);
                    
                }
                serverLanguage.setText(Language);
            }
            
            serverBirthDay.setText(StringUtils.isEmpty(userInfo.getBirthday())?"":userInfo.getBirthday());
            
        }
        else if (userInfo.getUserType().equals("organization"))
        {
            personal_info_layout.setVisibility(View.GONE);
            organization_info_layout.setVisibility(View.VISIBLE);
            personal_hp_v_icon_iv.setVisibility(View.GONE);
            company_hp_v_icon_iv.setVisibility(View.VISIBLE);
//            travelExperience.setVisibility(View.GONE);
            grey_view.setVisibility(View.GONE);
            companyRealname.setText(StringUtils.isEmpty(userInfo.getRealName())?"":userInfo.getRealName());
            
            company_date.setText(StringUtils.isEmpty(userInfo.getBirthday())?"":userInfo.getBirthday());
            company_address.setText(StringUtils.isEmpty(userInfo.getAddress())?"":userInfo.getAddress());
            personalProductCount.setText(StringUtils.isEmpty(userInfo.getProductCount())?"":userInfo.getProductCount());
            
        }else if (userInfo.getUserType().equals("customer")) {
        	personal_info_layout.setVisibility(View.GONE);
            organization_info_layout.setVisibility(View.GONE);
            personal_hp_v_icon_iv.setVisibility(View.GONE);
            company_hp_v_icon_iv.setVisibility(View.GONE);
//            travelExperience.setVisibility(View.GONE);
            grey_view.setVisibility(View.GONE);
			System.out.println("heh");
		}
        if(userInfo.getExperiencesList().size()>0){
        	  travelExperience.setVisibility(View.VISIBLE);
        }
    }
    
    private void initUserInfoView()
    {
        vIcon = (ImageView)findViewById(R.id.personal_v_icon_iv);
        serverIcon = (ImageView)findViewById(R.id.personal_icon);
        serverBirthDay = (TextView)findViewById(R.id.personal_birthday);
        personRealname = (TextView)findViewById(R.id.personal_realname);
        companyRealname = (TextView)findViewById(R.id.company_realname);
        layout_back_button= (LinearLayout)findViewById(R.id.layout_back_button);
        
        serverGender = (TextView)findViewById(R.id.personal_gender);
        serverBirthday = (TextView)findViewById(R.id.personal_birthday);
        serverAddress = (TextView)findViewById(R.id.personal_address);
        serverCompany = (TextView)findViewById(R.id.personal_company);
        serverLanguage = (TextView)findViewById(R.id.personal_language);
        serverNickname = (TextView)findViewById(R.id.user_nickname);
        serverIntroduction = (TextView)findViewById(R.id.personal_introduction);
        personal_info_layout = (LinearLayout)findViewById(R.id.personal_info_layout);
        organization_info_layout = (LinearLayout)findViewById(R.id.organization_info_layout);
        grey_view = findViewById(R.id.grey_view);
        // company_hp_name = (TextView) findViewById(R.id.company_hp_name);
        company_date = (TextView)findViewById(R.id.company_date);
        company_address = (TextView)findViewById(R.id.company_address);
        personal_hp_v_icon_iv = (ImageView)findViewById(R.id.personal_hp_v_icon_iv);
        company_hp_v_icon_iv = (ImageView)findViewById(R.id.company_hp_v_icon_iv);
        personalProductCount = (TextView)findViewById(R.id.personal_product_count);
        pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.allThemes_container);
        layout_back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				activityAnimationClose();
				
			}
		});
        pullableScrollView = (PullableScrollView)findViewById(R.id.pullableScrollView);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        travelExperience = (RelativeLayout)findViewById(R.id.travel_experience);
        travelExperience.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                Intent intent;
                String nickName = userInfo.getNickname();
                // ArrayList<Experiences> experiences=new ArrayList<Experiences>();
                // experiences=userInfoList.get(0).getExperiencesList();
                intent = new Intent(PersonalHomepageActivity.this, TravelExperienceActivity.class);
                intent.putExtra("nickName", nickName);
                // .putParcelableArrayListExtra("userInfoList", userInfoList);
                intent.putExtra("productId", productId);
                intent.putExtra("userId", userId);
                startActivity(intent);
                activityAnimationOpen();
                
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 定义一个实现OnRefreshListener接口的实现类对象
     * 
     * @author caihelin
     * 
     */
    class MyListener implements OnRefreshListener
    {
        
        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
        {
            if (personalProductArrayList.size() == 0)
            {
                return;
            }
            else if (personalProductArrayList.size() % 10 == 0)
            {
                pageNum = pageNum + 1;
                QueryUserProduct(userId);
                
            }
            else
            {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
                Toast.makeText(PersonalHomepageActivity.this, "加载完毕", Toast.LENGTH_LONG).show();
            }
        }
        
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout)
        {
            // TODO Auto-generated method stub
            personalProductArrayList.clear();
            isPullRefresh = true;
            pageNum = 1;
            QueryUserProduct(userId);
        }
        
    }
    
    
}
