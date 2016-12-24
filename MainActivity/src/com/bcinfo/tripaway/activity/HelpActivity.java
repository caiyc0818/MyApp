package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
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
public class HelpActivity extends BaseActivity implements OnClickListener
{
    private List<HelpInfo> helpInfoList = new ArrayList<HelpInfo>();
    private BaseAdapter adapter ;
    private ListView helpInfoLisView ;
    
    private  RelativeLayout askRelativeLayout;
    
    private String[]  aboutTitle ;
    private String[]  aboutContent ;
    private String[] payTitle ;
    private String[]  payContent ;
    private String[]  newTitle ;
    private String[]  newContent;
    
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);

        setContentView(R.layout.help_and_consult);
        setSecondTitle("帮助与咨询");
        statisticsTitle="帮助与咨询";
      //  getHelp("100","1");
        findView();
        getStringArray();
        adapter =new HelpAndConsultAdapter(this, helpInfoList);
        helpInfoLisView.setAdapter(adapter);
        
        askRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HelpActivity.this, ConsultOrComplaintActivity.class);
                intent.putExtra("type", "consultation");
                startActivity(intent);
                activityAnimationOpen();
			}
		});
        helpInfoLisView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(helpInfoList.get(position).getLevel()!=0){
					 Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
		                intent.putExtra("title", helpInfoList.get(position).getTitle());
		                intent.putExtra("content", helpInfoList.get(position).getContent());
		                startActivity(intent);
		                activityAnimationOpen();
				}
			}
		});
   }

    private void findView(){
    	helpInfoLisView=(ListView)this.findViewById(R.id.consult_listview);
    	askRelativeLayout=(RelativeLayout)this.findViewById(R.id.ask);
    }

    private void getStringArray(){
    	Resources res =getResources();
    	 aboutTitle =res.getStringArray(R.array.about_title);
          aboutContent =res.getStringArray(R.array.about_content);
       payTitle =res.getStringArray(R.array.pay_title);
       payContent =res.getStringArray(R.array.pay_content);
        newTitle =res.getStringArray(R.array.new_title);
        newContent=res.getStringArray(R.array.new_content);
        addHelpInfo("关于远行","",0);
        String str="在远行网购买商品或服务是支持支付宝交易的，您可以放心购买，简单分为以下四步（不区分境内境外）：\n第一步：拍下商品或服务\n第二步：付款（此付款动作是把钱支付到远行网）\n第三步：在您行程开始的24小时内，远行网将您交易总额的50%打款给远行合伙人\n第四步：在您行程归来后确认余款支付（此动作是将您在远行账户剩余的款支付给远行合伙\n\n系统自动确认将款项支付给远行合伙人的时间如下：\n自“行程开始”状态起的24小时内，系统自动确认将交易额的50%打款给远行合伙人；自“远行合伙人服务已完成”状态起的7日后，系统会自动确认将余款支付给远行合伙人。\n";
        
        for(int i=0;i<aboutTitle.length;i++){
        	addHelpInfo(aboutTitle[i],aboutContent[i],1);
        }
        addHelpInfo("支付与退款","",0);
        for(int i=0;i<payTitle.length;i++){
        	if(i==1)addHelpInfo(payTitle[i],str,1);
        	else
        	addHelpInfo(payTitle[i],payContent[i],1);
        }
        addHelpInfo("新手指南","",0);
        for(int i=0;i<newTitle.length;i++){
        	addHelpInfo(newTitle[i],newContent[i],1);
        }
    }
private void addHelpInfo(String title ,String content,int level){
	HelpInfo helpInfo=new HelpInfo();
    helpInfo.setTitle(title);
    helpInfo.setContent(content);
    helpInfo.setLevel(level);
    helpInfoList.add(helpInfo);
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
    
    private void getHelp(String pagesize, String pagenum) {
		try {
			RequestParams params = new RequestParams();
			params.put("pagesize", pagesize);
			params.put("pagenum", "pagenum");
			params.put("type", "consultation");
			HttpUtil.get(Urls.get_consultation, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							LogUtil.d("HelpActivity",
									"HelpActivity",
									response.toString());
							super.onSuccess(statusCode, headers, response);
							JSONArray datas = response.optJSONArray("data");
							if (datas.length() > 0) {

							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {

							super.onFailure(statusCode, headers,
									responseString, throwable);
							ToastUtil.showErrorToast(
									HelpActivity.this,
									"查询失败" + ":"
											+ statusCode);
							throwable.printStackTrace();
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
