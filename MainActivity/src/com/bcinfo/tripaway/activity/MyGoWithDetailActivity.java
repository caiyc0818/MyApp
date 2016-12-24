package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.bcinfo.tripaway.view.pop.EditPopWindow;
import com.bcinfo.tripaway.view.pop.EditPopWindow.OperationListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的结伴详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月28日 上午10:46:39
 */
public class MyGoWithDetailActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "MyGoWithDetailActivity";
    
    private ListView responceListView;
    
    private ArrayList<Intention> responceList = new ArrayList<Intention>();
    
    private GoWithResponceListAdapter mResponceAdapter;
    
    private GoWithNew mGoWithNew;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mGoWithNew = getIntent().getParcelableExtra("goWithNew");
        setContentView(R.layout.my_go_with_detail_activity);
        LinearLayout layout_back_button = (LinearLayout)findViewById(R.id.layout_back_button);
        ImageView button_operation = (ImageView)findViewById(R.id.button_operation);
        layout_back_button.setOnClickListener(this);
        button_operation.setOnClickListener(this);
        responceListView = (ListView)findViewById(R.id.responce_listview);
        responceListView.addHeaderView(getListViewHeader());
        responceListView.addFooterView(getListViewFoot());
        initListView();
        queryGoWithDetail(mGoWithNew.getId());
    }
    
    private LinearLayout getListViewHeader()
    {
        LinearLayout header = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.go_with_detail_list_head, null);
        RoundImageView author_photo = (RoundImageView)header.findViewById(R.id.author_photo);
        TextView author_name = (TextView)header.findViewById(R.id.author_name);
        CheckBox is_male = (CheckBox)header.findViewById(R.id.is_male);
        TextView issue_time = (TextView)header.findViewById(R.id.issue_time);
        TextView go_with_title = (TextView)header.findViewById(R.id.go_with_title);
        LinearLayout layout_lable = (LinearLayout)header.findViewById(R.id.layout_lable);
        TextView predict_time = (TextView)header.findViewById(R.id.predict_time);
        TextView start_address = (TextView)header.findViewById(R.id.start_address);
        TextView destination_address = (TextView)header.findViewById(R.id.destination_address);
        TextView go_with_description = (TextView)header.findViewById(R.id.go_with_description);
        TextView them_responce_number = (TextView)header.findViewById(R.id.them_responce_number);
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
                Intent intent = new Intent(MyGoWithDetailActivity.this, AllResponceActivity.class);
                intent.putExtra("togetherId", mGoWithNew.getId());
                startActivity(intent);
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
            case R.id.button_operation:
                EditPopWindow pop = new EditPopWindow(this, R.drawable.icon_edit, "编辑", R.drawable.set_qchc, "删除");
                pop.setOperationListener(new OperationListener()
                {
                    @Override
                    public void clickItem2()
                    {
                        Toast.makeText(MyGoWithDetailActivity.this, "点击了clickItem2", Toast.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void clickItem1()
                    {
                        Toast.makeText(MyGoWithDetailActivity.this, "点击了clickItem1", Toast.LENGTH_SHORT).show();
                    }
                });
                pop.show(v);
                break;
            default:
                break;
        }
    }
    
    /****************** request *******************/
    private void queryGoWithDetail(String id)
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
}
