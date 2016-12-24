package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.TravelItineraryListAdapter;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.MyJourneyCheckRemarkDialog;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;

/**
 * 我的行程单
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月13日 下午3:31:52
 */
public class TravelItineraryActivity extends BaseActivity implements OnClickListener, IXListViewListener
{
    private TravelItineraryListAdapter travelItineraryListAdapter;
    
    private ArrayList<ProductInfo> travelItineraryListArr = new ArrayList<ProductInfo>();
    
    private XListView travelItineraryListView;
    
    private LinearLayout bottomButton;
    
    private String mRemark;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.travel_itinerary_activity);
        setSecondTitle("行程单");
        travelItineraryListView = (XListView)findViewById(R.id.travel_itinerary_listview);
        bottomButton = (LinearLayout)findViewById(R.id.layout_bottom_button);
        bottomButton.setOnClickListener(this);
        initRemarkButton();
        initListview();
    }
    
    private void initRemarkButton()
    {
        mRemark = PreferenceUtil.getString("my_journey_remark");
        ImageView remark_logo = (ImageView)findViewById(R.id.write_remark_logo);
        TextView remark_text = (TextView)findViewById(R.id.write_remark_text);
        if (mRemark.isEmpty())
        {
            remark_logo.setVisibility(View.VISIBLE);
            remark_text.setText("写备注");
        }
        else
        {
            remark_logo.setVisibility(View.GONE);
            remark_text.setText("查看备注");
        }
    }
    
    @Override
    public void onClick(View v)
    {
        Intent intent;
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_bottom_button:
                if (mRemark.isEmpty())
                {
                    intent = new Intent(this, MyJourneyRemarkActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                }
                else
                {
                    intent = new Intent(this, MyJourneyCheckRemarkDialog.class);
                    startActivity(intent);
                }
                
                break;
            default:
                break;
        }
    }
    
    private void initListview()
    {
        for (int i = 0; i < 6; i++)
        {
            ProductInfo group = new ProductInfo();
            group.setName("澳洲西海岸八日游捉鲨鱼");
            group.setOrderNumber("2" + i);
            group.setService("SUV.六位旅客.行程规划.代订机票");
            group.setDescription("大家好，我叫大锤，是个活泼开朗的西北大汉，来美国当总统已经5年了,对这里的人文，美食有独特的研究");
            group.setBuyDate("2014-12-12");
            group.setPrice("Y2345");
            group.setEvaluate("3.6");
            group.setOfferExplain("1、自由活动的餐费，车费，景点门票费");
            group.setTotalPople("整团12人");
            group.setRecruitDate("11.03-11.08");
            group.setExperienceDate("12.09-12.12");
            group.setEvaluateNum("22条评论");
            group.setAuthorName("steady");
            group.setOrderState((i % 4 + 1) + "");
            group.setAddress("DOWNTOWN 洛杉矶 美国");
            group.setAuthorContact("+86 13892834924");
            group.setAuthorPhotoUrl("http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1501/07/c5/1597923_1597923_1420634132548_mthumb.jpg");
            ArrayList<String> logoUrls = new ArrayList<String>();
            for (int j = 0; j < 4; j++)
            {
                logoUrls.add("http://d.hiphotos.baidu.com/image/w%3D2048/sign=43b9711cd62a283443a6310b6f8dc8ea/adaf2edda3cc7cd9aef055d83b01213fb90e91d5.jpg");
            }
            group.setLogoUrls(logoUrls);
            if (i % 3 == 0)
            {
                group.setType("1");
                group.setEnable(true);
            }
            else if (i % 3 == 1)
            {
                group.setType("2");
                group.setEnable(true);
            }
            else if (i % 3 == 2)
            {
                group.setType("3");
                group.setEnable(false);
            }
            travelItineraryListArr.add(group);
        }
        travelItineraryListAdapter = new TravelItineraryListAdapter(this, travelItineraryListArr);
        travelItineraryListView.setAdapter(travelItineraryListAdapter);
        travelItineraryListView.setPullLoadEnable(true);
        travelItineraryListView.setXListViewListener(this);
    }
    
    //
    // private IXListViewListener mIXListViewListener = new IXListViewListener()
    // {
    // @Override
    // public void onRefresh()
    // {
    // // TODO Auto-generated method stub
    // pullRefreshHandler.sendEmptyMessageDelayed(0, 2000);
    // }
    //
    // @Override
    // public void onLoadMore()
    // {
    // // TODO Auto-generated method stub
    // loadMoreHandler.sendEmptyMessageDelayed(0, 2000);
    // }
    // };
    
    /**
     * 下拉刷新handler
     */
    private Handler pullRefreshHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                Toast.makeText(TravelItineraryActivity.this, "刷新结束", Toast.LENGTH_LONG).show();
                onLoad(travelItineraryListView, true);
            }
        }
    };
    
    /**
     * 加载更多handler
     */
    private Handler loadMoreHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                Toast.makeText(TravelItineraryActivity.this, "加载结束", Toast.LENGTH_LONG).show();
                onLoad(travelItineraryListView, true);
            }
        }
    };
    
    private void onLoad(XListView mListView, boolean isUpdateTime)
    {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        if (isUpdateTime)
        {
            mListView.setRefreshTime(StringUtils.dateToString());
        }
    }
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        pullRefreshHandler.sendEmptyMessageDelayed(0, 2000);
    }
    
    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        loadMoreHandler.sendEmptyMessageDelayed(0, 2000);
    }
}
