package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.TipsDetailAdapter;
import com.bcinfo.tripaway.bean.TipsDetailInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 产品提供服务
 * 
 * @function
 * @author weiCH
 * @version 1.0, 2015年8月1日 上午9:41:54
 */
public class TipsDetailActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "TipsDetailActivity";
    
    private ListView TipsDetailListView;
    
    private ArrayList<TipsDetailInfo> list;
    
    private TipsDetailAdapter adapter;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.tips_detail_activity);
        String objectId = getIntent().getStringExtra("objectId");
        String objectType = getIntent().getStringExtra("objectType");
        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        TipsDetailListView = (ListView)findViewById(R.id.tips_detail_listview);
        back_button.setOnClickListener(this);
        tipDetailUrl(objectId, objectType);
        list = new ArrayList<TipsDetailInfo>();
        adapter = new TipsDetailAdapter(list, TipsDetailActivity.this);
        TipsDetailListView.setAdapter(adapter);
        
    }
    
    private void tipDetailUrl(String objectId, String objectType)
    {
        
        RequestParams params = new RequestParams();
        params.put("objectId", objectId);
        params.put("objectType", objectType);
        HttpUtil.get(Urls.tips_detail, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                if ("00000".equals(response.optString("code")))
                {
                    gettipDetailList(response);
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        
    }
    
    private void gettipDetailList(JSONObject response)
    {
        
        JSONArray tipsArray = response.optJSONObject("data").optJSONArray("tips");
        if (tipsArray == null || tipsArray.length() == 0)
        {
            return;
        }
        for (int i = 0; i < tipsArray.length(); i++)
        {
            TipsDetailInfo TipsDetailItem = new TipsDetailInfo();
            TipsDetailItem.setTitle(tipsArray.optJSONObject(i).optString("title"));
            TipsDetailItem.setContent(tipsArray.optJSONObject(i).optString("content"));
            list.add(TipsDetailItem);
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            
            default:
                break;
        }
        
    }
    
}
