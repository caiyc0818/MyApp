package com.bcinfo.tripaway.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ContentActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.LinkDetailActivity;
import com.bcinfo.tripaway.activity.LocationCountryDetailActivity;
import com.bcinfo.tripaway.activity.OrgListActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ThemeProductListActivity;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * @author zhangbingkang
 * @version V1.0 创建时间：2014-2-18 上午11:08:48
 */
public class MyAdsPagerAdapter extends PagerAdapter
{
    public List<View> mListViews;
    
    private Context context;
    
    // private List<ActivityInfo> tempList;
    private List<PushResource> pushResourceList;
    
    // private int [] resId={R.drawable.ad1,R.drawable.ad2};
    private int type = 0;
    
    public MyAdsPagerAdapter(Context context, List<View> mListViews, List<PushResource> pushResourceList)
    {
        this.mListViews = mListViews;
        this.context = context;
        this.pushResourceList = pushResourceList;
    }
    
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2)
    {
        ((ViewPager)arg0).removeView(mListViews.get(arg1));
    }
    
    @Override
    public void finishUpdate(View arg0)
    {
    }
    
    @Override
    public int getCount()
    {
        return mListViews.size();
    }
    
    @Override
    public Object instantiateItem(View arg0, final int arg1)
    {
        // mListViews.get(arg1).setBackgroundResource(resId[arg1]);
        ((ViewPager)arg0).addView(mListViews.get(arg1), 0);
        final PushResource resource = pushResourceList.get(arg1);
        mListViews.get(arg1).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (pushResourceList != null)
                {
                    if (resource.getObjectType().equals("product"))
                    {
                        JSONObject jsonObject;
                        try
                        {
                            jsonObject = new JSONObject(resource.getObject().toString());
                            String productType = jsonObject.optString("productType");
                            String serviceCodes = jsonObject.optString("serviceCodes");
                            if (productType.equals("team"))
                            {
                                Intent intent = new Intent(context, GrouponProductNewDetailActivity.class);
                                intent.putExtra("productId", resource.getObjectId());
                                context.startActivity(intent);
                            }
                            else if (productType.equals("base") || productType.equals("customization"))
                            {
                                Intent intent = new Intent(context, GrouponProductNewDetailActivity.class);
                                intent.putExtra("productId", resource.getObjectId());
                                context.startActivity(intent);
                            }
                            else if (productType.equals("single"))
                            {
                                if (serviceCodes.equals("traffic"))
                                {
                                    Intent intent = new Intent(context, CarProductDetailActivity.class);
                                    intent.putExtra("productId", resource.getObjectId());
                                    context.startActivity(intent);
                                }
                                else if (serviceCodes.equals("stay"))
                                {
                                    Intent intent = new Intent(context, HouseProductDetailActivity.class);
                                    intent.putExtra("productId", resource.getObjectId());
                                    context.startActivity(intent);
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else if (resource.getObjectType().equals("column") || resource.getObjectType().equals("topic"))
                    {
                        Intent intentForTopicProductList = new Intent(context, ThemeProductListActivity.class);
                        intentForTopicProductList.putExtra("themeId", resource.getObjectId());
                        context.startActivity(intentForTopicProductList);
                    }
                    else if (resource.getObjectType().equals("destination"))
                    {
                        Intent intentForLocateCountry = new Intent(context, LocationCountryDetailActivity.class);
                        intentForLocateCountry.putExtra("destId", resource.getObjectId());
                        context.startActivity(intentForLocateCountry);
                    }
                    else if (resource.getObjectType().equals("link"))
                    {
                        Intent intentForLocateCountry = new Intent(context, ContentActivity.class);
                        intentForLocateCountry.putExtra("path", resource.getObject().toString());
                        intentForLocateCountry.putExtra("title", "专题活动");
                        intentForLocateCountry.putExtra("resTitle", resource.getResTitle());
                        intentForLocateCountry.putExtra("desc",resource.getDescription());
                        intentForLocateCountry.putExtra("titleImage", resource.getTitleImage());
                        context.startActivity(intentForLocateCountry);
                    }else if(resource.getObjectType()!=null&&resource.getObjectType().equals("user")){
						UserInfo userInfo = (UserInfo) resource
								.getObject();
						Intent intentClubFirstPage = new Intent(context,
								ClubFirstPageActivity.class);
						intentClubFirstPage.putExtra("userInfo", userInfo);
						context.startActivity(intentClubFirstPage);
					}
                }
            }
        });
        return mListViews.get(arg1);
    }
    
    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == (arg1);
    }
    
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }
    
    @Override
    public Parcelable saveState()
    {
        return null;
    }
    
    @Override
    public void startUpdate(View arg0)
    {
    }
}
