package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.bean.GoWithNew;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.TripZone;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月27日 15:30:22
 * 
 */
public class ZoneListAdapter extends BaseAdapter
{
    private List<TripZone> zoneList;
    
    private Activity mActivity;
    
    private HashMap<Integer, Boolean> checkMap = new HashMap<Integer,Boolean>();// 定义一个存储指定position上的boolean类型的map集合变量
    
    private LayoutInflater inflator;
    
    public ZoneListAdapter(List<TripZone> zoneList, Activity mActivity)
    {
        super();
        this.zoneList = zoneList;
        this.mActivity = mActivity;
        initMap();
        inflator = LayoutInflater.from(mActivity);
    }
    
    private void initMap()
    {
        for (int i = 0; i < zoneList.size(); i++)
        {
            checkMap.put(i, false);
        }
    }
    
    @Override
    public int getCount()
    {
        
        return zoneList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return zoneList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parentView)
    {
        final ViewHolder holder;
        final int pos = position;
        final TripZone tripZoneItem = zoneList.get(pos);
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_content_zone_list_layout, null);
            holder.authorRoundIv = (RoundImageView)convertView.findViewById(R.id.zone_user_head_iv);
            holder.authorNameTv = (TextView)convertView.findViewById(R.id.zone_content_username_tv);
            holder.contentCategoryTv = (TextView)convertView.findViewById(R.id.zone_content_category_tv);// 发布内容的种类
            holder.authorCheckBox = (CheckBox)convertView.findViewById(R.id.zone_content_love_cb);
            holder.authorCheckBox.setTag(pos);// 为checkbox 设置tag position
            holder.loveCountTv = (TextView)convertView.findViewById(R.id.zone_content_love_count_tv);
            holder.tripStoryContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_zone_content_middle_container);
            holder.tripProductContainer =
                (RelativeLayout)convertView.findViewById(R.id.layout_zone_content_middle_product_container2);
            holder.tripGoWithContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_zone_content_middle_container3);
            holder.goWithTitleTv = (TextView)convertView.findViewById(R.id.zone_content_go_with_title_text);
            holder.goWithContentTv = (TextView)convertView.findViewById(R.id.zone_content_go_with_content_text);
            holder.storyContentTv = (TextView)convertView.findViewById(R.id.zone_content_middle_tv);
            holder.photoLayoutContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_zone_content_middle_photo_container);
            holder.publishLocationTv = (TextView)convertView.findViewById(R.id.zone_content_published_location_tv);
            holder.publishTimeTv = (TextView)convertView.findViewById(R.id.zone_content_published_time_tv);
            holder.productPhotoIv = (ImageView)convertView.findViewById(R.id.zone_content_product_background_iv);
            holder.productInfoContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_zone_content_middle_product_container);
            holder.productNameTv = (TextView)convertView.findViewById(R.id.zone_content_middle_product_name_tv);
            holder.productLabelTv = (TextView)convertView.findViewById(R.id.zone_content_middle_product_label_tv);
            
            convertView.setTag(holder);// 设置tag标签
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            holder.authorCheckBox.setTag(pos);// 对重用的convertView使用新的Position来设置tag
            
        }
        // 设置checked监听器
        holder.authorCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            /*
             * 初始生成checkbox的时候 是不会调用onCheckedChanged()方法的
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox cb = (CheckBox)buttonView;
                if (isChecked)
                {
                    checkMap.put((Integer)cb.getTag(), true);
                    cb.setChecked(checkMap.get((Integer)cb.getTag()));// 设置为选中状态
                }
                else
                {
                    checkMap.put((Integer)cb.getTag(), false);
                    cb.setChecked(checkMap.get((Integer)cb.getTag()));// 设置为未选中状态
                }
            }
        });
        
        holder.authorCheckBox.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (checkMap.get((Integer)v.getTag()))// true
                {
                    // mapItem.put("loveCount", ((Integer)
                    // mapItem.get("loveCount")) + 1);
                    tripZoneItem.setLikes(String.valueOf(Integer.parseInt(tripZoneItem.getLikes()) + 1));
                    holder.loveCountTv.setText(tripZoneItem.getLikes());// 点赞人数
                    testAppericateUrl((String)v.getTag(R.id.item_zone_object_id),
                        (String)v.getTag(R.id.item_zone_object_type),
                        "1");
                    // Toast.makeText(mContext, "加点击了", 0).show(); // 加1 // +1
                }
                else
                {
                    // mapItem.put("loveCount", ((Integer)
                    // mapItem.get("loveCount")) - 1);
                    tripZoneItem.setLikes(String.valueOf(Integer.parseInt(tripZoneItem.getLikes()) - 1));
                    holder.loveCountTv.setText(tripZoneItem.getLikes());// 点赞人数
                    testAppericateUrl((String)v.getTag(R.id.item_zone_object_id),
                        (String)v.getTag(R.id.item_zone_object_type),
                        "0");
                    // Toast.makeText(mContext, "减点击了", 0).show(); // 减1 // -1
                }
            }
        });
        if ("1".equals(tripZoneItem.getIsLike())) // 已赞
        {
            checkMap.put((Integer)holder.authorCheckBox.getTag(), true);
        }
        else if ("0".equals(tripZoneItem.getIsLike())) // 未赞
        {
            checkMap.put((Integer)holder.authorCheckBox.getTag(), false);
        }
        holder.authorRoundIv.setImageResource(R.drawable.ic_launcher);
        if (!StringUtils.isEmpty(tripZoneItem.getPublisher().getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + tripZoneItem.getPublisher().getAvatar(),
                holder.authorRoundIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        holder.authorNameTv.setText(tripZoneItem.getPublisher().getNickname());
        if ("tripstory".equals(tripZoneItem.getObjectType()) && tripZoneItem.getObject() instanceof TripArticle)// 旅行故事
        {
            final TripArticle tripArticle = (TripArticle)tripZoneItem.getObject();
            holder.contentCategoryTv.setText(R.string.zone_content_category_tripStory);
            holder.tripStoryContainer.setVisibility(View.VISIBLE);
            holder.tripProductContainer.setVisibility(View.GONE);
            holder.tripGoWithContainer.setVisibility(View.GONE);
            holder.storyContentTv.setText(tripArticle.getDescription());
            holder.publishLocationTv.setText(tripZoneItem.getLocation());
            holder.publishTimeTv.setText(calculateTimeLength(tripZoneItem.getPublishTime()));
            holder.authorCheckBox.setChecked(checkMap.get((Integer)holder.authorCheckBox.getTag()));// 设置点赞
            holder.authorCheckBox.setTag(R.id.item_zone_object_id, tripArticle.getPhotoId()); // 圈子item的id
            holder.authorCheckBox.setTag(R.id.item_zone_object_type, tripZoneItem.getObjectType());// 圈子item的type
            
            // false
            // holder.authorCheckBox.setTag(position);
            holder.loveCountTv.setText(tripZoneItem.getLikes());
            // ArrayList<String> photoList = (ArrayList<String>)
            // mapItem.get("photoList");
            ArrayList<ImageInfo> picList = tripArticle.getPictureList();
            ArrayList<String> pictureList = new ArrayList<String>();
            holder.photoLayoutContainer.removeAllViews();// 删除container中所有的childView
            if (picList != null && picList.size() != 0)
            {
                for (int i = 0; i < picList.size(); i++)
                {
                    ImageInfo resPicture = picList.get(i);
                    pictureList.add(resPicture.getUrl());
                    View photoViewItem = inflator.inflate(R.layout.item_zone_photo_layout, null);
                    ImageView photoIv = (ImageView)photoViewItem.findViewById(R.id.zone_layout_photo_iv);
                    if (!StringUtils.isEmpty(resPicture.getUrl()))
                    {
                        ImageLoader.getInstance().displayImage(Urls.imgHost + resPicture.getUrl(),
                            photoIv,
                            AppConfig.options(R.drawable.ic_launcher));
                    }
                    
                    holder.photoLayoutContainer.addView(photoViewItem);
                    // 设置每一个追加的photoViewItem的tag 设置指定position 和集合的tag标签
                    photoViewItem.setTag(R.id.tag_experience_photo_list, pictureList);// 设置
                    // 照片集合
                    photoViewItem.setTag(R.id.tag_experience_photo_index, i);// 设置点击照片的索引i
                    photoViewItem.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            ArrayList<String> photoList = (ArrayList<String>)v.getTag(R.id.tag_experience_photo_list);// 获得选中的照片所处的集合
                            int index = (Integer)v.getTag(R.id.tag_experience_photo_index);// 获得选中的照片的index
                            Intent intentForView = new Intent(mActivity, ImageViewerActivity.class);
                            intentForView.putExtra("image_index", index);
                            intentForView.putStringArrayListExtra("image_urls", photoList);
                            mActivity.startActivity(intentForView);
                            
                        }
                    });
                }
            }
            
        }
        else if ("product".equals(tripZoneItem.getObjectType()) && tripZoneItem.getObject() instanceof ProductNewInfo)// 具体的服务产品
        {
            final ProductNewInfo proNewInfo = (ProductNewInfo)tripZoneItem.getObject();
            holder.tripStoryContainer.setVisibility(View.GONE);// 隐藏
            holder.tripProductContainer.setVisibility(View.VISIBLE);// 可见
            holder.tripGoWithContainer.setVisibility(View.GONE);
            holder.contentCategoryTv.setText(R.string.zone_content_category_product);
            holder.publishLocationTv.setText(tripZoneItem.getLocation());
            holder.publishTimeTv.setText(calculateTimeLength(tripZoneItem.getPublishTime()));
            
            holder.authorCheckBox.setChecked(checkMap.get((Integer)holder.authorCheckBox.getTag()));// 设置点赞
            holder.authorCheckBox.setTag(R.id.item_zone_object_id, proNewInfo.getId()); // 圈子item的id
            holder.authorCheckBox.setTag(R.id.item_zone_object_type, tripZoneItem.getObjectType());// 圈子item的type
            // false
            // holder.authorCheckBox.setTag(position);
            holder.loveCountTv.setText(tripZoneItem.getLikes());
            if (!StringUtils.isEmpty(proNewInfo.getTitleImg()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + proNewInfo.getTitleImg(), holder.productPhotoIv);
            }
            
            holder.productNameTv.setText(proNewInfo.getTitle());
            List<String> topicArray = proNewInfo.getTopics();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < topicArray.size(); i++)
            {
                
                sb.append(topicArray.get(i));
                sb.append(" · ");
                
            }
            sb.deleteCharAt(sb.length() - 2);
            holder.productLabelTv.setText(sb.toString()); // 产品 主题
            holder.tripProductContainer.setTag(proNewInfo);
            holder.tripProductContainer.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    Intent intent = null;
                    ProductNewInfo productNewInfo = (ProductNewInfo)v.getTag();
                    if (productNewInfo.getProductType().equals("single"))
                    {
                        if (productNewInfo.getServices().equals("traffic"))
                        {
                            intent = new Intent(mActivity, CarProductDetailActivity.class);
                            intent.putExtra("productId", productNewInfo.getId());
                        }
                        else if (productNewInfo.getServices().equals("stay"))
                        {
                            intent = new Intent(mActivity, HouseProductDetailActivity.class);
                            intent.putExtra("productId", productNewInfo.getId());
                        }
                        else
                        {
                            intent = new Intent(mActivity, GrouponProductNewDetailActivity.class);
                            intent.putExtra("productId", productNewInfo.getId());
                        }
                    }
                    else if (productNewInfo.getProductType().equals("base")
                        || productNewInfo.getProductType().equals("customization"))
                    {
                        intent = new Intent(mActivity, GrouponProductNewDetailActivity.class);
                        intent.putExtra("productId", productNewInfo.getId());
                    }
                    else if (productNewInfo.getProductType().equals("team"))
                    {
                        intent = new Intent(mActivity, GrouponProductNewDetailActivity.class);
                        intent.putExtra("productId", productNewInfo.getId());
                    }
                    mActivity.startActivity(intent);
                    ((BaseActivity)mActivity).activityAnimationOpen();
                    // Intent intentForProductDetail = new Intent(mActivity, ProductDetailNewActivity.class);
                    // ProductInfo info = new ProductInfo();
                    // info.setName(((ProductNewInfo)v.getTag()).getTitle());
                    // info.setService("服务");
                    // info.setPrice("4500");
                    // intentForProductDetail.putExtra("product", info);
                    // mActivity.startActivity(intentForProductDetail);
                }
            });
        }
        else if ("together".equals(tripZoneItem.getObjectType()) && tripZoneItem.getObject() instanceof GoWithNew) // 结伴信息
        {
            final GoWithNew goWithNew = (GoWithNew)tripZoneItem.getObject();
            // ImageLoader.getInstance().displayImage(goWithNew.getUser().getAvartar(),
            // holder.authorRoundIv,
            // AppConfig.options(R.drawable.ic_launcher));
            // holder.authorRoundIv.setOnClickListener(new OnClickListener()
            // {
            //
            // @Override
            // public void onClick(View v)
            // {
            // Intent intentForPersonal = new Intent(mActivity, PersonalInfoNewActivity.class);
            // intentForPersonal.putExtra("identifyId", goWithNew.getId());
            // mActivity.startActivity(intentForPersonal);
            // }
            // });
            // holder.authorNameTv.setText(goWithNew.getUser().getNickname());
            holder.tripStoryContainer.setVisibility(View.GONE);// 隐藏
            holder.tripProductContainer.setVisibility(View.GONE);// 隐藏
            holder.tripGoWithContainer.setVisibility(View.VISIBLE);// 可见
            holder.contentCategoryTv.setText(R.string.zone_content_category_goWith);
            holder.publishLocationTv.setText(tripZoneItem.getLocation());
            holder.publishTimeTv.setText(calculateTimeLength(tripZoneItem.getPublishTime()));
            holder.goWithTitleTv.setText(goWithNew.getTitle());
            holder.goWithContentTv.setText(goWithNew.getDescription());
            holder.authorCheckBox.setChecked(checkMap.get((Integer)holder.authorCheckBox.getTag()));// 设置点赞
            holder.authorCheckBox.setTag(R.id.item_zone_object_id, goWithNew.getId()); // 圈子item的id
            holder.authorCheckBox.setTag(R.id.item_zone_object_type, tripZoneItem.getObjectType());// 圈子item的type
            // false
            // holder.authorCheckBox.setTag(position);
            holder.loveCountTv.setText(tripZoneItem.getLikes());
            
        }
        
        return convertView;
    }
    
    /*
     * 测试 点赞/取消点赞 url
     */
    private void testAppericateUrl(final String objectId, final String objectType, final String opType)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                HttpPost httpPost = new HttpPost(Urls.appericate_url);
                
                Map<String, String> paramItem = new HashMap<String, String>();
                paramItem.put("objectId", objectId);
                paramItem.put("objectType", objectType);
                paramItem.put("opType", opType);
                JSONObject paramObj = new JSONObject(paramItem);
                try
                {
                    StringEntity strEntity = new StringEntity(paramObj.toString(), "utf-8");
                    strEntity.setContentEncoding("UTF-8");
                    strEntity.setContentType("application/json");
                    httpPost.setEntity(strEntity);
                    Map<String, String> mapParams = HttpUtil.getHeadersMap(Urls.appericate_url);
                    for (Map.Entry<String, String> entry : mapParams.entrySet())
                    {
                        // 添加请求头信息
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                        
                    }
                    httpPost.addHeader("token", PreferenceUtil.getToken());
                    httpPost.addHeader("session", PreferenceUtil.getSession());
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                    {
                        System.out.println("响应内容=" + EntityUtils.toString(httpResponse.getEntity()));
                    }
                }
                catch (Exception e)
                {
                    
                    e.printStackTrace();
                }
            }
        }).start();
        
        // RequestParams params=new RequestParams();
        // params.put("objectId", objectId);
        // params.put("objectType", objectType);
        // params.put("opType", opType);
        // HttpUtil.post(Urls.appericate_url, params, new
        // JsonHttpResponseHandler(){
        // @Override
        // public void onSuccess(int statusCode, Header[] headers, JSONObject
        // response)
        // {
        //
        // super.onSuccess(statusCode, headers, response);
        // System.out.println(response);
        // }
        // @Override
        // public void onFailure(int statusCode, Header[] headers, String
        // responseString, Throwable throwable)
        // {
        //
        // super.onFailure(statusCode, headers, responseString, throwable);
        // System.out.println(responseString);
        //
        // }
        // });
    }
    
    /**
     * 计算 从发布时间 到 现在为止所经过的时间长度 (单位: 分 ； 小时 ； 天数)
     * 
     * @author caihelin
     * 
     */
    private String calculateTimeLength(String publishTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        try
        {
            Date specifiedDate = sdf.parse(publishTime);
            long specifiedMillions = specifiedDate.getTime(); // 返回 发布时间 指定的毫秒数
            Date currentDate = new Date();
            long currentMillions = currentDate.getTime();
            
            double value = (currentMillions - specifiedMillions) / (3600 * 1000); // 获得的是小时数
            // System.out.println("小时数:"+value);
            if (value > 24)
            {
                long daysValue = (long)value / 24;// 获得天数 估值
                System.out.println("天数:" + daysValue);
                
                return String.valueOf(daysValue + "天之前");
                
            }
            else
            { // 小于24小时 (一天的范围内)
                if (value >= 1)
                {
                    
                    System.out.println("小时数:" + value);
                    
                    return String.valueOf(value + "小时之前");
                }
                else
                {
                    int minutesValue = (int)(value * 60);
                    if (minutesValue >= 1 && minutesValue < 60)
                    {
                        System.out.println("分钟:" + minutesValue);
                        
                        return String.valueOf(minutesValue + "分钟之前");
                    }
                    else
                    {
                        return String.valueOf("1分钟之前");
                        
                    }
                }
            }
        }
        catch (ParseException e)
        {
            
            e.printStackTrace();
        }
        
        return null;
    }
    
    private class ViewHolder
    {
        RoundImageView authorRoundIv;// 作者 头像
        
        TextView authorNameTv;// 作者 姓名
        
        CheckBox authorCheckBox;// 赞美 checkbox
        
        TextView contentCategoryTv;// 发布内容的种类 (包括:微游记 结伴 新产品)
        
        TextView loveCountTv;// 点赞人数 textview
        
        TextView goWithTitleTv;// 结伴标题
        
        TextView goWithContentTv;// 结伴内容
        
        LinearLayout tripStoryContainer;// 旅行故事 LinearLayout
        
        RelativeLayout tripProductContainer;// 服务产品 relativeLayout
        
        LinearLayout tripGoWithContainer;// 结伴LinearLayout
        
        TextView storyContentTv;// 故事内容 textview
        
        LinearLayout photoLayoutContainer;// 故事照片集
        
        TextView publishLocationTv;// 发布的地点
        
        TextView publishTimeTv;// 发布的时间
        
        ImageView productPhotoIv;// 产品照片
        
        TextView productNameTv;// 产品名字
        
        TextView productLabelTv;// 产品标签
        
        LinearLayout productInfoContainer;// 产品具体信息 layout
        
    }
    
}
