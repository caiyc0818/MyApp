package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人主页-更多
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 14：16：22
 * 
 */
public class PersonalInfoMoreActivity extends BaseActivity
{
    private String userId;
    
    private RoundImageView individualRoundIv;// 圆形头像
    
    private TextView userNameTv;// 用户姓名
    
    private LinearLayout userTitleContainer;// 用户头衔
    
    private TextView userAgeTv;// 用户年龄
    
    private TextView userRegionTv;// 用户地区
    
    private TextView userJobTv;// 用户职业
    
    private TextView userEducateTv;// 用户教育
    
    private TextView userLanguageTv;// 用户语言
    
    private TextView userIntroduceTv;// 用户简介
    
    private LinearLayout experienceViewContainer;// 用户的过往
    
    private TextView[] userTitleTv = new TextView[5];
    
    private ImageLoader imgLoader;
    
    private ArrayList<Experiences> experienceList = new ArrayList<Experiences>();
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.personal_info_more_activity);
        setSecondTitle("");
        userId = (String)getIntent().getStringExtra("userInfo_id_more_value");
        individualRoundIv = (RoundImageView)findViewById(R.id.personal_more_headicon_iv);
        userNameTv = (TextView)findViewById(R.id.personal_name_more_tv);
        userTitleContainer = (LinearLayout)findViewById(R.id.layout_personal_more_titles_container);
        userIntroduceTv = (TextView)findViewById(R.id.personal_introduce_more_tv);
        userAgeTv = (TextView)findViewById(R.id.personal_more_age_text_tv);
        userRegionTv = (TextView)findViewById(R.id.personal_more_area_text_tv);
        userJobTv = (TextView)findViewById(R.id.personal_more_job_text_tv);
        userEducateTv = (TextView)findViewById(R.id.personal_more_eduated_text_tv);
        userLanguageTv = (TextView)findViewById(R.id.personal_more_language_text_tv);
        experienceViewContainer = (LinearLayout)findViewById(R.id.layout_personal_more_records_content_container);
        initUserTitleTvs();
        testIndividualMoreUrl();
        
    }
    
    private void initUserTitleTvs()
    {
        userTitleTv[0] = (TextView)findViewById(R.id.personal_more_titleName1);
        userTitleTv[1] = (TextView)findViewById(R.id.personal_more_titleName2);
        userTitleTv[2] = (TextView)findViewById(R.id.personal_more_titleName3);
        userTitleTv[3] = (TextView)findViewById(R.id.personal_more_titleName4);
        userTitleTv[4] = (TextView)findViewById(R.id.personal_more_titleName5);
    }
    
    /*
     * 测试 个人资料-更多接口
     */
    private void testIndividualMoreUrl()
    {
        
        HttpUtil.get(Urls.personal_more_url + userId + "/more", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if ("00000".equals(response.optString("code")))
                {
                    System.out.println("个人资料-更多=" + response);
                    initUserInfo(response);
                    
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(responseString);
            }
        });
    }
    
    private void initUserInfo(JSONObject response)
    {
        
        JSONObject userObj = response.optJSONObject("data").optJSONObject("user");
        if (!StringUtils.isEmpty(userObj.optString("avatar")))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + userObj.optString("avatar"),
                individualRoundIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        if ("1".equals(userObj.optString("sex"))) // 男
        {
            userNameTv.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.male_mark), null);
            
        }
        else if ("0".equals(userObj.optString("sex"))) // 女
        {
            userNameTv.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.female_mark), null);
        }
        userNameTv.setText(userObj.optString("nickName"));
        setSecondTitle(userObj.optString("nickName") + "的资料");
        JSONArray tagsArray = userObj.optJSONArray("tags");
        if (tagsArray != null && tagsArray.length() != 0)
        {
            for (int i = 0; i < tagsArray.length(); i++)
            {
                userTitleTv[i].setVisibility(View.VISIBLE);
                userTitleTv[i].setText(tagsArray.optString(i));
                
            }
        }
        
        userIntroduceTv.setText(userObj.optString("introduction"));
        
        userAgeTv.setText(userObj.optString("age"));
        
        userRegionTv.setText(userObj.optString("address"));
        
        userJobTv.setText(userObj.optString("job"));
        
        userEducateTv.setText(userObj.optString("school") + userObj.optString("education"));
        JSONArray languageArray = userObj.optJSONArray("languages");
        if (languageArray != null && languageArray.length() != 0)
        {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < languageArray.length(); i++)
            {
                sb.append(languageArray.optString(i));
                sb.append(" · ");
                
            }
            sb.deleteCharAt(sb.length() - 2);
            userLanguageTv.setText(sb.toString());
        }
        
        JSONArray experiencesArray = userObj.optJSONArray("experiences");
        if (experiencesArray != null && experiencesArray.length() != 0)
        {
            for (int i = 0; i < experiencesArray.length(); i++)
            {
                JSONObject experienceObj = experiencesArray.optJSONObject(i);
                Experiences experience = new Experiences();
                experience.setExperienceId(experienceObj.optString("id")); // 设置过往id标识
                experience.setRank(experienceObj.optString("rank")); // 设置 过往排序
                JSONArray imageArray = experienceObj.optJSONArray("images");
                if (imageArray != null)
                {
                    for (int j = 0; j < imageArray.length(); j++)
                    {
                        experience.getImagesUrls().add(imageArray.optString(j)); // 设置过往图片
                        
                    }
                }
                
                experience.setDescription(experienceObj.optString("description")); // 设置过往描述
                experienceList.add(experience);
            }
            
            createExperienceWidget(experienceList);
        }
        
    }
    
    private void createExperienceWidget(ArrayList<Experiences> experienceList)
    {
        if (experienceList != null && experienceList.size() != 0)
        {
            
            for (int i = 0; i < experienceList.size(); i++)
            {
                View convertView =
                    LayoutInflater.from(this).inflate(R.layout.item_personal_more_experience_layout, null);
                
                TextView experienceContentTv =
                    (TextView)convertView.findViewById(R.id.personal_more_experience_content_tv);
                
                Experiences experienceItem = experienceList.get(i);// 获得指定position处的过往经历
                
                String experienceStr = experienceItem.getDescription();
                ArrayList<String> photoList = (ArrayList<String>)experienceItem.getImagesUrls();
                experienceContentTv.setText(experienceStr);
                LinearLayout photoLayout =
                    (LinearLayout)convertView.findViewById(R.id.layout_personal_more_experience_photo_container);
                for (int j = 0; j < photoList.size(); j++)
                {
                    View photoView =
                        LayoutInflater.from(this).inflate(R.layout.item_personal_more_experience_photo, null);
                    photoView.setTag(R.id.tag_experience_photo_list, photoList);// 设置
                                                                                // 照片集合
                    photoView.setTag(R.id.tag_experience_photo_index, j);// 设置
                                                                         // 点击的照片的索引index
                    photoView.setOnClickListener(mClickListener);
                    ImageView photoIv = (ImageView)photoView.findViewById(R.id.personal_more_record_photo_iv);
                    if (!StringUtils.isEmpty(photoList.get(j)))
                    {
                        ImageLoader.getInstance().displayImage(Urls.imgHost + photoList.get(j),
                            photoIv,
                            AppConfig.options(R.drawable.ic_launcher));
                        photoLayout.addView(photoView);// 将一个照片布局添加到总的照片布局中
                    }
                    
                }
                if (i != 0)
                {
                    View spaceView =
                        LayoutInflater.from(this).inflate(R.layout.item_personal_more_view_split_container, null);
                    experienceViewContainer.addView(spaceView);
                    
                }
                
                experienceViewContainer.addView(convertView);
                
            }
        }
    }
    
    private OnClickListener mClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            ArrayList<String> photoList = (ArrayList<String>)v.getTag(R.id.tag_experience_photo_list);// 获得选中的照片所处的集合
            int index = (Integer)v.getTag(R.id.tag_experience_photo_index);// 获得选中的照片的index
            Intent intentForView = new Intent(PersonalInfoMoreActivity.this, ImageViewerActivity.class);
            intentForView.putExtra("image_index", index);
            intentForView.putStringArrayListExtra("image_urls", (ArrayList<String>)photoList);
            startActivity(intentForView);
            
        }
    };
}
