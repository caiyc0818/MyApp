package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GoWithResponceListAdapter;
import com.bcinfo.tripaway.bean.GoWithNew;
import com.bcinfo.tripaway.bean.Intention;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 结伴详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月28日 上午10:46:39
 */
public class GoWithDetailActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "GoWithDetailActivity";
    
    private ListView responceListView;
    
    private ArrayList<Intention> responceList = new ArrayList<Intention>();
    
    private GoWithResponceListAdapter mResponceAdapter;
    
    private GoWithNew mGoWithNew;
    
    private EditText edit_text;
    
    private TextView them_responce_number;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mGoWithNew = getIntent().getParcelableExtra("goWithNew");
        setContentView(R.layout.go_with_detail_activity);
        LinearLayout layout_back_button = (LinearLayout)findViewById(R.id.layout_back_button);
        layout_back_button.setOnClickListener(this);
        responceListView = (ListView)findViewById(R.id.responce_listview);
        responceListView.addHeaderView(getListViewHeader());
        responceListView.addFooterView(getListViewFoot());
        initListView();
        TextView commit_button = (TextView)findViewById(R.id.responce_commit_button);
        edit_text = (EditText)findViewById(R.id.responce_edit_text);
        commit_button.setOnClickListener(this);
        QueryGoWithDetail(mGoWithNew.getId());
    }
    
    private LinearLayout getListViewHeader()
    {
        LinearLayout header = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.go_with_detail_list_head, null);
        RoundImageView author_photo = (RoundImageView)header.findViewById(R.id.author_photo);
        TextView author_name = (TextView)header.findViewById(R.id.author_name);
        CheckBox is_male = (CheckBox)header.findViewById(R.id.is_male);
        LinearLayout layout_lable = (LinearLayout)header.findViewById(R.id.layout_lable);
        TextView go_with_title = (TextView)header.findViewById(R.id.go_with_title);
        TextView issue_time = (TextView)header.findViewById(R.id.issue_time);
        TextView predict_time = (TextView)header.findViewById(R.id.predict_time);
        TextView start_address = (TextView)header.findViewById(R.id.start_address);
        TextView destination_address = (TextView)header.findViewById(R.id.destination_address);
        TextView go_with_description = (TextView)header.findViewById(R.id.go_with_description);
        them_responce_number = (TextView)header.findViewById(R.id.them_responce_number);
        if (mGoWithNew == null)
        {
            return header;
        }
        UserInfo user = mGoWithNew.getUser();
        if (!StringUtils.isEmpty(user.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + user.getAvatar(),
                author_photo,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        
        author_name.setText(user.getNickname());
        if (user.getGender() != null && user.getGender().equals("1"))
        {
            is_male.setChecked(true);
        }
        else
        {
            is_male.setChecked(false);
        }
        ArrayList<String> tags = user.getTags();
        for (int i = 0; i < tags.size(); i++)
        {
            layout_lable.addView(getLable(tags.get(i)));
        }
        go_with_title.setText(mGoWithNew.getTitle());
        issue_time.setText("发布于" + mGoWithNew.getCreateTime());
        predict_time.setText(mGoWithNew.getPlanTime());
        start_address.setText(mGoWithNew.getStartPoint());
        destination_address.setText(mGoWithNew.getEndPoint());
        go_with_description.setText(mGoWithNew.getDescription());
        them_responce_number.setText("TA们的回应(" + mGoWithNew.getApplyNum() + ")");
        return header;
    }
    
    private LinearLayout getListViewFoot()
    {
        LinearLayout foot = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.responce_listview_foot, null);
        Button lock_all_responce = (Button)foot.findViewById(R.id.lock_all_responce);
        lock_all_responce.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(GoWithDetailActivity.this, AllResponceActivity.class);
                intent.putExtra("togetherId", mGoWithNew.getId());
                startActivity(intent);
                GoWithDetailActivity.this.activityAnimationOpen();
            }
        });
        return foot;
    }
    
    private LinearLayout getLable(String lable)
    {
        LinearLayout lableLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.go_with_lable_layout, null);
        TextView lableText = (TextView)lableLayout.findViewById(R.id.go_with_lable_text);
        lableText.setText(lable);
        return lableLayout;
    }
    
    private void initListView()
    {
        mResponceAdapter = new GoWithResponceListAdapter(this, responceList);
        responceListView.setAdapter(mResponceAdapter);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                break;
            case R.id.responce_commit_button:
                if (!edit_text.getText().toString().equals(""))
                {
                    commitResponce();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "评论不能为空哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    
    /****************** request *******************/
    private void QueryGoWithDetail(String id)
    {
        HttpUtil.get(Urls.go_with + "/" + id, null, new JsonHttpResponseHandler()
        {
            @Override
            public void setRequestURI(URI requestURI)
            {
                // TODO Auto-generated method stub
                super.setRequestURI(requestURI);
                LogUtil.i(TAG, "QueryGoWithDetail", "requestURI=" + requestURI);
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "QueryGoWithDetail", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryGoWithDetail", "response=" + response.toString());
                JSONObject data = response.optJSONObject("data");
                if (data == null)
                {
                    return;
                }
                JSONArray intentionJsonArr = data.optJSONArray("intention");
                if (intentionJsonArr != null)
                {
                    for (int i = 0; i < intentionJsonArr.length(); i++)
                    {
                        JSONObject intentionJson = intentionJsonArr.optJSONObject(i);
                        Intention intention = new Intention();
                        intention.setContent(intentionJson.optString("content"));
                        intention.setStatus(intentionJson.optString("status"));
                        intention.setCreateTime(intentionJson.optString("createTime"));
                        JSONObject userJson = intentionJson.optJSONObject("user");
                        if (userJson != null)
                        {
                            intention.getUser().setUserId(userJson.optString("userId"));
                            intention.getUser().setGender(userJson.optString("sex"));
                            intention.getUser().setNickname(userJson.optString("nickName"));
                            intention.getUser().setAvatar(userJson.optString("avatar"));
                            intention.getUser().setIntroduction(userJson.optString("introduction"));
                            intention.getUser().setMobile(userJson.optString("mobile"));
                            intention.getUser().setStatus(userJson.optString("status"));
                            intention.getUser().setEmail(userJson.optString("email"));
                            JSONArray tagsJSONList = userJson.optJSONArray("tags");
                            if (tagsJSONList != null)
                                for (int k = 0; k < tagsJSONList.length(); k++)
                                {
                                    intention.getUser().getTags().add(tagsJSONList.opt(k).toString());
                                }
                        }
                        responceList.add(intention);
                    }
                    mResponceAdapter.notifyDataSetChanged();
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "onFailure", "statusCode=" + statusCode);
            }
        });
    }
    
    /**
     * 提交回应
     */
    private void commitResponce()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("togetherId", mGoWithNew.getId());
            jsonObject.put("content", edit_text.getText().toString());
            
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
            HttpUtil.post(Urls.my_answer, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    // Log.d("","1111---- code="+statusCode);
                    // Log.d("","1111---- code="+response);
                    if ("00000".equals(response.optString("code")))
                    {
                        Toast.makeText(getBaseContext(), "评论成功", Toast.LENGTH_SHORT).show();
                        responceList.clear();
                        QueryGoWithDetail(mGoWithNew.getId());
                        int num = Integer.valueOf(mGoWithNew.getApplyNum()) + 1;
                        String newnum = String.valueOf(num);
                        them_responce_number.setText("TA们的回应(" + newnum + ")");
                    }
                    else if ("00097".equals(response.optString("code")))
                    {
                        String msg = response.optString("msg");
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // Log.d("","1111---- code="+statusCode);
                    // Log.d("","1111---- code="+responseString);
                    // Log.d("","1111---- code="+throwable);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
