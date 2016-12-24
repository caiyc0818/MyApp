package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ConsultOrComplaintAdapter;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.FeedBack;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.getui.receiver.PushDemoReceiver;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置-帮助
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月15日 14:49:22
 */
public class ConsultOrComplaintDetailActivity extends BaseActivity implements OnClickListener,IXListViewListener
{
    

    private  XListView consultOrComplaintListview;
    private  BaseAdapter consultOrComplaintAdapter;
    
    private    List<FeedBack> feedBackList = new ArrayList<FeedBack>();
private int pageNum = 1;
    
    private String pageSize = "10";
    
    private String type;
    
    
    /**
     * 下拉刷新
     */
    private boolean isPullRefresh = false;
    
    /**
     * 上拉加载更多
     */
    private boolean isLoadmore = false;
    
    
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);

        setContentView(R.layout.consult_or_complaint_detail);
        setSecondTitle("我的咨询");
        Intent intent=getIntent();
        type = intent.getStringExtra("type");
		if (type.equals("complaint")) {
			setSecondTitle("我的投诉");
		} else
			setSecondTitle("我的咨询");
//      //  getHelp("100","1");
//        Intent intent = getIntent();
//        type=intent.getStringExtra("type");
//        
//        findView();
//        MyTextView.setVisibility(View.VISIBLE);
//        MyTextView.setText("我的");
//        askRelativeLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
        findView();
        consultOrComplaintListview.setPullLoadEnable(false);
        consultOrComplaintListview.setPullRefreshEnable(true);
        consultOrComplaintListview.setXListViewListener(this);
        consultOrComplaintAdapter=new ConsultOrComplaintAdapter(this, feedBackList,type);
        consultOrComplaintListview.setAdapter(consultOrComplaintAdapter);
   }

    private void findView(){
    	consultOrComplaintListview=(XListView)this.findViewById(R.id.consult_or_complaint_listview);
    }

   
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.back_button:
            finish();
            this.overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
            break;

        default:
            break;
        }

    }
    
    
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        isPullRefresh = true;
        pageNum = 1;
        getConsultOrComplaint();
    }
    
    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        isLoadmore = true;
        getConsultOrComplaint();
    }

    private void getConsultOrComplaint()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);// 当前页码
        params.put("pagesize", pageSize);// 页码数
        params.put("type", type);
        HttpUtil.get(Urls.get_consultation, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(getActivity(), "error=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(getActivity(), "error=" + throwable.getMessage());
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore)
                {
                	consultOrComplaintListview.stopLoadMore();
                }
                if (isPullRefresh)
                {
                	consultOrComplaintListview.stopRefresh();
                }
                if (pageNum != 1)
                {
                    pageNum--;
                }
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                String code = response.optString("code");
                if (!isLoadmore && !isPullRefresh)
                {
                	feedBackList.clear();
                }
                if (isLoadmore)
                {
                    // 上拉返回的结束加载更多布局
                	consultOrComplaintListview.stopLoadMore();
                }
                if (code.equals("00000"))
                {
                    if (isPullRefresh)
                    {
                        // 下拉刷新返回的
                    	consultOrComplaintListview.successRefresh();
                    	feedBackList.clear();
                    }
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                    	List<FeedBack> feedBacks = new ArrayList<FeedBack>();
                    	JSONArray feedBacksJson = dataJson.optJSONArray("feedBack");
						for(int i=0;i<feedBacksJson.length();i++){
							JSONObject	feedBackJson=feedBacksJson.optJSONObject(i);
							FeedBack feedBack=new FeedBack();
							feedBack.setContent(feedBackJson.optString("content"));
							feedBack.setCreateTime(feedBackJson.optString("createTime"));
							feedBack.setFeedBackId(feedBackJson.optString("feedBackId"));
							feedBack.setStatus(feedBackJson.optString("status"));
							feedBack.setType(feedBackJson.optString("type"));
							feedBacks.add(feedBack);
						}
                        
                        if (feedBackList.size() < 10)
                        {
                        	consultOrComplaintListview.setPullLoadEnable(false);
                        }
                        else
                        {
                            pageNum++;
                            consultOrComplaintListview.setPullLoadEnable(true);
                        }
                        feedBackList.addAll(feedBacks);
                        consultOrComplaintAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
//                    ToastUtil.showToast(getActivity(), "errorCode=" + response.optString("code"));
                    if (isPullRefresh)
                    {
                        // 下拉刷新接口返回的数据不正确
                    	consultOrComplaintListview.stopRefresh();
                    }
                    if (pageNum != 1)
                    {
                        pageNum--;
                    }
                }
                // 上拉 下拉的初始状态置为false;
                isLoadmore = false;
                isPullRefresh = false;
            }
        });
    }
    
    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        getConsultOrComplaint();
    }
}
