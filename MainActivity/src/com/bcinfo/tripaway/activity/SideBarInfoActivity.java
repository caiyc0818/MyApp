package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 侧边栏-头像点击
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年2月9日 17:41:24
 */
public class SideBarInfoActivity extends BaseActivity implements OnClickListener
{
    private static final String URL = "http://www.qqw21.com/article/UploadPic/2012-7/201275154741900.jpg";
    
    private List<Map<String, Object>> funcList;
    
    private ImageView userIcon;
    
    private LinearLayout individualLayout;// 个人资料
    
    private LinearLayout zoneLayout;// 圈子
    
    private LinearLayout messageLayout;// 消息
    
    private LinearLayout friendshipLayout;// 好友
    
    private LinearLayout myitineraryLayout;// 我的旅程
    
    private LinearLayout itineraryOrderLayout;// 行程单
    
    private LinearLayout travelstoryLayout;// 我的旅行故事
    
    private LinearLayout goWithLayout;// 我的结伴
    
    // 关闭按钮
    private LinearLayout closeIv;
    
    public LinearLayout getCloseIv()
    {
        return closeIv;
    }
    
    public void setCloseIv(LinearLayout closeIv)
    {
        this.closeIv = closeIv;
    }
    
    public List<Map<String, Object>> getFuncList()
    {
        return funcList;
    }
    
    public void setFuncList(List<Map<String, Object>> funcList)
    {
        this.funcList = funcList;
    }
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.sidebar_headinfo);
        closeIv = (LinearLayout)findViewById(R.id.layout_back_button);
        userIcon = (ImageView)findViewById(R.id.sidebar_userHeadIcon);
        individualLayout = (LinearLayout)findViewById(R.id.layout_func_individual_SideBar);
        zoneLayout = (LinearLayout)findViewById(R.id.layout_func_zone_SideBar);
        messageLayout = (LinearLayout)findViewById(R.id.layout_func_message_SideBar);
        friendshipLayout = (LinearLayout)findViewById(R.id.layout_func_friendShip_SideBar);
        myitineraryLayout = (LinearLayout)findViewById(R.id.layout_func_itinerary_SideBar);
        itineraryOrderLayout = (LinearLayout)findViewById(R.id.layout_func_itineraryOrder_SideBar);
        travelstoryLayout = (LinearLayout)findViewById(R.id.layout_func_travel_story_SideBar);
        goWithLayout = (LinearLayout)findViewById(R.id.layout_func_gowith_SideBar);
        individualLayout.setOnClickListener(this);
        zoneLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        friendshipLayout.setOnClickListener(this);
        myitineraryLayout.setOnClickListener(this);
        itineraryOrderLayout.setOnClickListener(this);
        travelstoryLayout.setOnClickListener(this);
        goWithLayout.setOnClickListener(this);
        if(!StringUtils.isEmpty(URL)){
        	ImageLoader.getInstance().displayImage(URL, userIcon);
        }
        initFuncArgs();
        closeIv.setOnClickListener(mOnClickListener);
    }
    
    private void initFuncArgs()
    {
        funcList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapItem1 = new HashMap<String, Object>();
        mapItem1.put("funcImg", R.drawable.individual_sidebar_icon);// 个人资料
                                                                    // icon
        mapItem1.put("funcName", "个人资料");
        Map<String, Object> mapItem2 = new HashMap<String, Object>();
        mapItem2.put("funcImg", R.drawable.zone_sidebar_icon);// 圈子 icon
        mapItem2.put("funcName", "圈子");
        Map<String, Object> mapItem3 = new HashMap<String, Object>();
        mapItem3.put("funcImg", R.drawable.message_sidebar_icon);
        mapItem3.put("funcName", "消息");
        Map<String, Object> mapItem4 = new HashMap<String, Object>();
        mapItem4.put("funcImg", R.drawable.friendship_sidebar_icon);
        mapItem4.put("funcName", "好友");
        Map<String, Object> mapItem5 = new HashMap<String, Object>();
        mapItem5.put("funcImg", R.drawable.myitinerary_sidebar_icon);
        mapItem5.put("funcName", "我的旅程");
        Map<String, Object> mapItem6 = new HashMap<String, Object>();
        mapItem6.put("funcImg", R.drawable.travel_itinerary_sidebar_icon);
        mapItem6.put("funcName", "行程单");
        Map<String, Object> mapItem7 = new HashMap<String, Object>();
        mapItem7.put("funcImg", R.drawable.travel_story_sidebar_icon);
        mapItem7.put("funcName", "旅行故事");
        Map<String, Object> mapItem8 = new HashMap<String, Object>();
        mapItem8.put("funcImg", R.drawable.travel_story_sidebar_icon);
        mapItem8.put("funcName", "旅行故事");
        Map<String, Object> mapItem9 = new HashMap<String, Object>();
        mapItem9.put("funcImg", R.drawable.travel_story_sidebar_icon);
        mapItem9.put("funcName", "旅行故事");
        funcList.add(mapItem1);
        funcList.add(mapItem2);
        funcList.add(mapItem3);
        funcList.add(mapItem4);
        funcList.add(mapItem5);
        funcList.add(mapItem6);
        funcList.add(mapItem7);
        funcList.add(mapItem8);
        funcList.add(mapItem9);
    }
    
    @Override
    public void onClick(View view)
    {
        Intent intent = null;
        switch (view.getId())
        {
            case R.id.layout_func_individual_SideBar:// 个人资料
                Intent intentForIndividual = new Intent(this, PersonalInfoNewActivity.class);
                // UserInfo userInfo=new UserInfo();
                // userInfo.setUserId(Constant.getInstance().getSpUtil().getUserId());
                intentForIndividual.putExtra("identifyId", PreferenceUtil.getUserId());
                startActivity(intentForIndividual);
                activityAnimationOpen();
                break;
            case R.id.layout_func_zone_SideBar:
                startActivity(new Intent(this, ZoneListActivity.class));
                activityAnimationOpen();
                break;
            case R.id.layout_func_message_SideBar:
                // Toast.makeText(this, "消息", 0).show();
                // intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_func_friendShip_SideBar:
                // Toast.makeText(this, "好友", 0).show();
                intent = new Intent(this, GoodFriendsActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                // intent = new Intent(this, FriendActivity.class);
                // startActivity(intent);
                
                break;
            case R.id.layout_func_itinerary_SideBar:
                // Toast.makeText(this, "我的旅程", 0).show();
                intent = new Intent(this, MyJourneyActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_func_itineraryOrder_SideBar:
                // Toast.makeText(this, "行程单", 0).show();
                intent = new Intent(this, MyItineraryActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_func_travel_story_SideBar:
                // Toast.makeText(this, "旅行故事", 0).show();
                intent = new Intent(this, MyMicroBlogActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_func_gowith_SideBar:
                // Toast.makeText(this, "我的结伴", 0).show();
                intent = new Intent(this, MyGoWithActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                break;
            default:
                break;
        }
    }
}
