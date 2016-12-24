package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.BlogPagerAdapter;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyGridView;
import com.bcinfo.tripaway.view.ScrollView.PersonalNewScrollView;
import com.bcinfo.tripaway.view.ScrollView.PersonalNewScrollView.onTurnListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 新个人资料-个人主页
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月1日 17:16:22
 */
public class PersonalInfoNewActivity extends BaseActivity implements PersonalScrollViewListener, OnItemClickListener,
    OnClickListener

{
    private UserInfo user; // 用户对象
    
    private ImageView backgroundIv; // 新个人资料-背景图片
    
    private LinearLayout backLayout;// 新个人资料-后退按钮
    
    private LinearLayout moreLayout;// 新个人资料-更多
    
    private RoundImageView headerView;// 用户-圆形头像
    
    private final TextView[] userTitleNameTv = new TextView[5];// 用户的头衔数组
    
    private TextView userTitleNameTv1; // 用户头衔1
    
    private TextView userTitleNameTv2;// 用户头衔2
    
    private TextView userTitleNameTv3;// 用户头衔3
    
    private TextView userTitleNameTv4;// 用户头衔4
    
    private TextView userTitleNameTv5;// 用户头衔5
    
    private TextView userDescriptionTv;// 用户自我介绍
    
    private TextView blogCountTv;// 游记 数量
    
    private TextView focusCountTv;// 关注 数量
    
    private TextView userIsFocusTv;// 是否关注 textview
    
    private TextView fansCountTv;// 粉丝 数量
    
    private LinearLayout headTitleLayout;// 用户-头衔layout
    
    private PersonalNewScrollView personalScrollView;// 自定义滚动条
    
    private RelativeLayout titleHeader;// 标题栏
    
    private LinearLayout navigateBarLayout;// tab 导航条
    
    private final ArrayList<String> blogUrlList = new ArrayList<String>();// 存放游记照片的list集合
    
    private BlogPagerAdapter blogPagerAdapter;// 游记adapter
    
    private TextView titleText;// 标题
    
    private TextView userNameTv;// 用户名
    
    // private Constant constance;
    
    // private SharePreferenceUtil shareUtils = constance.getInstance().getSpUtil();
    
    private static final int count = 3;// 头衔数量
    
    private int scrollX;// 记录条滚动的x坐标位置
    
    private int scrollY;// 记录条滚动的y坐标位置
    
    public static int firstVisiblePosition;// 记录gridView的第一个可见item的position
    
    private MyGridView photoGridView;
    
    private LinearLayout travelLayout;
    
    private LinearLayout followLayout;
    
    private LinearLayout fansLayout;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.personal_info_new_activity);
        titleHeader = (RelativeLayout)findViewById(R.id.personal_title_header);
        backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
        moreLayout = (LinearLayout)findViewById(R.id.layout_more_button);
        userNameTv = (TextView)findViewById(R.id.userNameIv);
        blogCountTv = (TextView)findViewById(R.id.amount_personal_blog);
        focusCountTv = (TextView)findViewById(R.id.amount_personal_attention);
        userIsFocusTv = (TextView)findViewById(R.id.user_focused_tv);
        fansCountTv = (TextView)findViewById(R.id.amount_personal_fans);
        userDescriptionTv = (TextView)findViewById(R.id.personal_introduce_tv);
        titleText = (TextView)findViewById(R.id.title_text);
        photoGridView = (MyGridView)findViewById(R.id.photo_gridview);
        // 游记adapter
        blogPagerAdapter = new BlogPagerAdapter(this, blogUrlList);
        photoGridView.setAdapter(blogPagerAdapter);
        initViews();
        initUserTitleView();
        navigateBarLayout = (LinearLayout)findViewById(R.id.personal_navigation_Bar);
        backLayout.setOnClickListener(mOnClickListener);
        moreLayout.setOnClickListener(this);
        
        photoGridView.setOnItemClickListener(this);// gridView 点击监听
        titleHeader.getBackground().setAlpha(0);
        navigateBarLayout.getBackground().setAlpha(255);// 设置tab导航条上的背景透明度初始为完全不透明
        
        titleText.setAlpha(0);
        backgroundIv = (ImageView)findViewById(R.id.personal_scence_background_iv);
        headerView = (RoundImageView)findViewById(R.id.personal_headIcon_iv);
        headTitleLayout = (LinearLayout)findViewById(R.id.layout_personal_titles_container);
        personalScrollView = (PersonalNewScrollView)findViewById(R.id.personal_scrollView_new_container);
        personalScrollView.setScrollListener(this);
        ImageLoader.getInstance().displayImage("http://img.my.csdn.net/uploads/201309/01/1378037128_3531.jpg",
            backgroundIv);
        
        personalScrollView.setImageView(backgroundIv);
        personalScrollView.setTurnListener(new onTurnListener()
        {
            
            @Override
            public void onTurn(boolean isShowing)
            {
                
            }
        });
        personalScrollView.post(new Runnable()
        {
            
            @Override
            public void run()
            {
                // 初始化时 在(0,0)的位置
                personalScrollView.scrollTo(0, 0);
                
            }
        });
        
    }
    
    /*
     * init 用户头衔
     */
    private void initUserTitleView()
    {
        userTitleNameTv1 = (TextView)findViewById(R.id.personal_titleName1);
        userTitleNameTv2 = (TextView)findViewById(R.id.personal_titleName2);
        userTitleNameTv3 = (TextView)findViewById(R.id.personal_titleName3);
        userTitleNameTv4 = (TextView)findViewById(R.id.personal_titleName4);
        userTitleNameTv5 = (TextView)findViewById(R.id.personal_titleName5);
        userTitleNameTv[0] = userTitleNameTv1;
        userTitleNameTv[1] = userTitleNameTv2;
        userTitleNameTv[2] = userTitleNameTv3;
        userTitleNameTv[3] = userTitleNameTv4;
        userTitleNameTv[4] = userTitleNameTv5;
    }
    
    /*
     * 测试 个人主页-个人资料(自己) url
     */
    private void testIndividualUrl()
    {
        
        HttpUtil.get(Urls.individual_url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println(statusCode);
                System.out.println("个人资料:" + response.toString());
                getIndividualInfos(response);
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(statusCode);
                System.out.println(responseString.toString());
                
            }
        });
    }
    
    private void getIndividualInfos(JSONObject response)
    {
        if (!response.optString("code").equals("00000"))
        {
            ToastUtil.showErrorToast(PersonalInfoNewActivity.this,  response.optString("msg"));
            return;
        }
        JSONObject jsonObj = response.optJSONObject("data");
        String msg = response.optString("msg");
        if ("success".equals(msg))
        {
            UserInfo userInfo = new UserInfo();
            JSONObject userObj = jsonObj.optJSONObject("user");
            String userAvartar = userObj.optString("avatar"); // 个人头像
            userInfo.setAvatar(userAvartar);
            String userName = userObj.optString("nickName");
            userInfo.setNickname(userName);
            JSONArray jsonArray = userObj.optJSONArray("tags");
            if (jsonArray != null)
            {
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    userInfo.getTags().add(jsonArray.optString(i));
                    userTitleNameTv[i].setText(jsonArray.optString(i));
                    userTitleNameTv[i].setVisibility(View.VISIBLE);
                }
            }
            String userDescription = userObj.optString("introduction");
            if (TextUtils.isEmpty(userDescription))
            {
                userDescriptionTv.setText(userDescription);
            }
            
            if ("0".equals(userObj.optString("sex"))) // 女
            {
                userNameTv.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.female_mark), null);
                
            }
            else if ("1".equals(userObj.optString("sex"))) // 男
            {
                userNameTv.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.male_mark), null);
                
            }
            userNameTv.setText(userName);
            String storeNum = userObj.optString("storeNum");
            String focusCount = userObj.optString("focus");
            String fansCount = userObj.optString("fansCount");
            blogCountTv.setText(storeNum);
            focusCountTv.setText(focusCount);
            fansCountTv.setText(fansCount);
            JSONArray experienceArray = userObj.optJSONArray("experiences");
            if (experienceArray != null)
            {
                for (int j = 0; j < experienceArray.length(); j++)
                {
                    Experiences experience = new Experiences();
                    experience.setExperienceId(experienceArray.optJSONObject(j).optString("id"));
                    JSONArray imageArray = experienceArray.optJSONObject(j).optJSONArray("images");
                    if (imageArray != null)
                    {
                        for (int k = 0; k < imageArray.length(); k++)
                        {
                            experience.getImagesUrls().add(imageArray.optString(k));
                        }
                    }
                    experience.setRank(experienceArray.optJSONObject(j).optString("rank"));
                    experience.setDescription(experienceArray.optJSONObject(j).optString("description"));
                    userInfo.getExperiencesList().add(experience);
                }
            }
            userInfo.setHasMobile(userObj.optString("hasMobile"));
            userInfo.setStatus(userObj.optString("status"));
            userInfo.setJob(userObj.optString("job"));
            userInfo.setStoreNum(storeNum);
            userInfo.setEducation(userObj.optString("education"));
            userInfo.setAvatar(userObj.optString("avatar"));
            user = userInfo;
            JSONArray languagesArray = userObj.optJSONArray("languages");
            if (languagesArray != null)
            {
                for (int m = 0; m < languagesArray.length(); m++)
                {
                    userInfo.getLanguagesList().add(languagesArray.optString(m));
                }
            }
            userInfo.setProductCount(userObj.optString("productCount"));
            userInfo.setSchool(userObj.optString("school"));
            userInfo.setHasEmail(userObj.optString("hasEmail"));
            userInfo.setEmail(userObj.optString("email"));
            userInfo.setAddress(userObj.optString("address"));
            userInfo.setNickname(userObj.optString("nickName"));
            userInfo.setAge(userObj.optString("age"));
            userInfo.setUserId(userObj.optString("userId"));
            userInfo.setHasIdentity(userObj.optString("hasIdentity"));
            userInfo.setFocus(userObj.optString("focus"));
            userInfo.setFansCount(userObj.optString("fansCount"));
            userInfo.setIntroduction(userObj.optString("introduction"));
            userInfo.setMobile(userObj.optString("mobile"));
            if (!StringUtils.isEmpty(userAvartar))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + userAvartar,
                    headerView,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            if (!getIntent().getStringExtra("identifyId").equals(PreferenceUtil.getUserId())) // 判断
            // 如果不是本用户的话
            {
                if ("true".equals(String.valueOf(jsonObj.optBoolean("isFocus"))))
                {
                    userIsFocusTv.setText(R.string.personal_focusOff_tv);
                    
                }
                else if ("false".equals(String.valueOf(jsonObj.optBoolean("isFocus"))))
                {
                    userIsFocusTv.setText(R.string.personal_focusOn_tv);
                }
                userIsFocusTv.setOnClickListener(this);
            }
            JSONArray photoArray = jsonObj.optJSONArray("photo");
            if (photoArray != null)
            {
                List<String> resultBlogList = initBlogUrl(photoArray);
                blogUrlList.addAll(resultBlogList);
                blogPagerAdapter.notifyDataSetChanged();
            }
            
        }
        
    }
    
    /*
     * 测试 个人主页 url
     */
    private void testPersonalUrl(String userId)
    {
        
        HttpUtil.get(Urls.personal_url + userId, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println("个人主页=" + response);
                getIndividualInfos(response);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("statusCode=" + statusCode);
                System.out.println(responseString);
            }
        });
    }
    
    private int maxer(int value1, int value2)
    {
        if (value1 >= value2)
        {
            return value1;
        }
        else
        {
            return value2;
        }
    }
    
    /**
     * 初始化View
     */
    private void initViews()
    {
        // tabLayout = new LinearLayout[3];
        travelLayout = (LinearLayout)findViewById(R.id.layout_blog_container);
        followLayout = (LinearLayout)findViewById(R.id.layout_attention_container);
        fansLayout = (LinearLayout)findViewById(R.id.layout_fans_container);
        if (getIntent().getStringExtra("identifyId").equals(PreferenceUtil.getUserId())) // 个人
        {
            findViewById(R.id.layout_focusOn_container).setVisibility(View.INVISIBLE); // 关注TA
            testIndividualUrl();
            
        }
        else
        { // 其他用户的
            testPersonalUrl(getIntent().getStringExtra("identifyId"));
        }
        travelLayout.setSelected(true);// 默认设置 游记 tab为选中状态
        followLayout.setOnClickListener(this);
        fansLayout.setOnClickListener(this);
    }
    
    /**
     * 初始化游记url
     */
    private List<String> initBlogUrl(JSONArray photoArray)
    {
        List<String> blogUrlSubList = new ArrayList<String>();
        if (photoArray != null)
        {
            for (int n = 0; n < photoArray.length(); n++)
            {
                blogUrlSubList.add(photoArray.optString(n));
                
            }
        }
        
        return blogUrlSubList;
    }
    
    @Override
    public void onScrollChanged(ScrollView scrollView, int positionX, int positionY, int prePositionX, int prePositionY)
    {
        if (scrollView != null && scrollView == personalScrollView)
        {
            scrollX = positionX;
            scrollY = positionY;
            int alpha = (int)(scrollY / 2f);
            if (alpha >= 255)
            {
                alpha = 255;// 如果透明度大于255 那么就设置alpha为255
            }
            float fontAlpha = alpha / 240f;// 个人资料标题栏左边的字体的透明度
            int alpha2 = (int)(scrollY / 3f);
            int navigateBarAlpha = 255 - alpha2;
            if (navigateBarAlpha <= 0)
            {
                navigateBarAlpha = 0;
            }
            
            titleHeader.getBackground().setAlpha(alpha);
            navigateBarLayout.getBackground().setAlpha(navigateBarAlpha);
            titleText.setAlpha(fontAlpha);
            
        }
        else
        {
            return;
        }
        
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        // Toast.makeText(this, "点击了:" + position, 0).show();
        Intent intent = new Intent(PersonalInfoNewActivity.this, PhotoActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("photos", blogUrlList);
        startActivity(intent);
    }
    
    /*
     * 测试 关注好友接口
     */
    private void testFocusOnUrl(String userId)
    {
        
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.friend_focus_on_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("关注接口=" + response);
                    if (response.optString("code").equals("00000"))
                    {
                        Toast.makeText(PersonalInfoNewActivity.this, "已关注", 0).show();
                        userIsFocusTv.setText("取消关注");
                    }
                }
                
            });
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    /*
     * 测试 取消关注好友 接口
     */
    private void testFocusOffUrl(String userId)
    {
        HttpUtil.delete(Urls.friend_focus_off_url + userId, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                System.out.println("取消关注=" + response);
                if (response.optString("code").equals("00000"))
                {
                    Toast.makeText(PersonalInfoNewActivity.this, "取消关注", 0).show();
                    userIsFocusTv.setText("关注TA");
                }
            }
            
        });
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_more_button:
                
                Intent intentForMore = new Intent(this, PersonalInfoMoreActivity.class);
                if (user != null)
                {
                    intentForMore.putExtra("userInfo_id_more_value", user.getUserId());
                    startActivity(intentForMore);
                    activityAnimationOpen();
                }
                
                break;
            case R.id.user_focused_tv:
                if ("关注TA".equals((((TextView)view).getText().toString())))
                {
                    testFocusOnUrl(getIntent().getStringExtra("identifyId"));
                    
                }
                else if ("取消关注".equals((((TextView)view).getText().toString())))
                {
                    testFocusOffUrl(getIntent().getStringExtra("identifyId"));
                }
                
                break;
            case R.id.layout_attention_container:
                Intent intentFollow = new Intent(this, FollowMemberActivity.class);
                if (user != null)
                {
                    intentFollow.putExtra("userId", user.getUserId());
                    startActivity(intentFollow);
                    activityAnimationOpen();
                }
                break;
            case R.id.layout_fans_container:
                Intent intentFans = new Intent(this, FansMemberActivity.class);
                if (user != null)
                {
                    intentFans.putExtra("userId", user.getUserId());
                    startActivity(intentFans);
                    activityAnimationOpen();
                }
                break;
            default:
                break;
        }
        
    }
}
