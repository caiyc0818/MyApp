package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.FilterLayout.OnDaysListener;
import com.bcinfo.tripaway.view.FilterLayout.OnDestinationListener;
import com.bcinfo.tripaway.view.FilterLayout.OnPriceListener;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView.PullListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wefika.flowlayout.FlowLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class FootprintActivity extends BaseActivity implements OnClickListener {

	private FlowLayout tagsFlow;
	private TextView add;

	private int tagGap;

	private List<TopicOrTag> topicOrTags = new ArrayList<TopicOrTag>();
	private EditText name;
	private TextView eventCommitButton;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.footprint);
		initViews();
		tagGap = DensityUtil.dip2px(this, 5);
		Intent it=getIntent();
		statisticsTitle="足迹";
		if(it.getParcelableArrayListExtra("topicOrTags")!=null){
			topicOrTags=it.getParcelableArrayListExtra("topicOrTags");
			for(TopicOrTag tag:topicOrTags ){
				addTagFlow(tag, tagsFlow);
			}
		}
		else{
		getFootPrint(PreferenceUtil.getUserId());
		}

	}

	private void initViews() {
		setSecondTitle("足迹");
		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		add = (TextView) findViewById(R.id.add);
		name = (EditText) findViewById(R.id.name);
		name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String str=s.toString();
				if(StringUtils.isEmpty(str)){
					add.setBackgroundResource(R.drawable.shape_gray_rectangle);
				}else{
					add.setBackgroundResource(R.drawable.shape_green_rectangle);
				}
			}
		});
		eventCommitButton = (TextView)findViewById(R.id.event_commit_button);
		eventCommitButton.setVisibility(View.VISIBLE);
		eventCommitButton.setText("保存");
		add.setOnClickListener(this);
		eventCommitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			break;
		case R.id.add:
			String tagName = name.getText().toString();
			if (StringUtils.isEmpty(tagName))
				return;
			TopicOrTag tag1 = new TopicOrTag();
			tag1.setId("-2");
			tag1.setName(tagName);
			topicOrTags.add(tag1);
			addTagFlow(tag1, tagsFlow);
			break;
		case R.id.footprint_del:
			TopicOrTag tag = (TopicOrTag) v.getTag();
			if (topicOrTags.contains(tag)) {
				topicOrTags.remove(tag);
				tagsFlow.removeView((View) v.getParent());
			}
			if (topicOrTags.size() == 0) {
				tagsFlow.setVisibility(View.GONE);
			}
			break;
			case R.id.event_commit_button:
				if(topicOrTags.size()==0)
					{
					ToastUtil.showToast(getApplicationContext(), "足迹不能为空");
					}
				else{
					List<String> strs=new ArrayList<String>();
					for(TopicOrTag mTag:topicOrTags){
						strs.add(mTag.getName());
					}
					addFootprint(strs);
				}
				break;
		default:
			break;
		}
	}

	private void addTagFlow(TopicOrTag tag, FlowLayout flowLayout) {
		flowLayout.setVisibility(View.VISIBLE);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.footprint_item, null);

		TextView name = (TextView) view.findViewById(R.id.footprint_name);
		ImageView footprintDel = (ImageView) view
				.findViewById(R.id.footprint_del);
		name.setText(tag.getName());
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.rightMargin = tagGap;
		params.topMargin = DensityUtil.dip2px(this, 10);
		view.setLayoutParams(params);
		flowLayout.addView(view);
		footprintDel.setTag(tag);
		footprintDel.setOnClickListener(this);
	}
	
	
	
    private void addFootprint(List<String> tagNames)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tagNames", new JSONArray(tagNames));
            StringEntity stringEntity = new StringEntity(jsonObject.toString(),"UTF-8");
            HttpUtil.post(Urls.add_footprint, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    if ("00000".equals(response.optString("code")))
                    {
                    	Intent it=new Intent();
                    	it.putParcelableArrayListExtra("topicOrTags", (ArrayList<TopicOrTag>)topicOrTags);
                        setResult(Activity.RESULT_OK,it);
                        ToastUtil.showToast(getApplicationContext(), "保存成功");
                        finish();
                    }
                    else
                    {
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }
        
    }

    
	
	 /**
    * 查询足迹
    */
   private void getFootPrint(String userId)
   {
       RequestParams params = new RequestParams();
       params.put("userId", userId);
       params.put("pagenum", 1);
       params.put("pagesize", 100);
       HttpUtil.get(Urls.footprint, params, new JsonHttpResponseHandler()
       {
           
           @Override
           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
           {
               // TODO Auto-generated method stub
               super.onFailure(statusCode, headers, responseString, throwable);
//               ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
           }
           
           @Override
           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
           {
               // TODO Auto-generated method stub
               super.onFailure(statusCode, headers, throwable, errorResponse);
//               ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
           }
           
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response)
           {
               // TODO Auto-generated method stub
               super.onSuccess(statusCode, headers, response);
               String code = response.optString("code");
               if (code.equals("00000"))
               {
            		JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject object = dataArray.optJSONObject(i);
							TopicOrTag tag = new TopicOrTag();
							tag.setId(object.optString("tagId"));
							tag.setName(object.optString("tagName"));
							topicOrTags.add(tag);
						}
						if (topicOrTags.size() > 0) {
							tagsFlow.setVisibility(View.VISIBLE);
						}
						for(TopicOrTag tag:topicOrTags ){
							addTagFlow(tag, tagsFlow);
						}
					}
               }
           }
           
       });
   }
}
