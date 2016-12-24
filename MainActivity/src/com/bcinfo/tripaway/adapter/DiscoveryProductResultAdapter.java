package com.bcinfo.tripaway.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 搜索产品结果的Adapter
 * 
 * 该adapter可作为搜索结果显示数据的适配器，也可作为筛选结果数据适配器
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2015年4月20日 11:32:22
 * 
 */
public class DiscoveryProductResultAdapter extends BaseAdapter implements OnClickListener
{
    // 搜索结果 筛选结果的list集合
    private List<Map<String, Object>> resultList;
    
    private Context context;
    
    private LayoutInflater inflator;
    
    public DiscoveryProductResultAdapter()
    {
    }
    
    public DiscoveryProductResultAdapter(List<Map<String, Object>> resultList, Context context)
    {
        this.resultList = resultList;
        this.context = context;
    }
    
    @Override
    public int getCount()
    {
        return resultList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return resultList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        inflator = LayoutInflater.from(context);
        ViewHolder holder = null;
        Map<String, Object> mapItem = resultList.get(position);
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = (View)inflator.inflate(R.layout.item_discovery_results, null);
            holder.discoveryStandardProductContainer = (LinearLayout)convertView // 搜索结果-标准产品
            .findViewById(R.id.layout_discovery_standard_product_container);
            holder.standardProductPhotoIv =
                (ImageView)convertView.findViewById(R.id.discovery_standard_product_photo_iv); // 标准产品图片
            holder.standardProductNameTv = (TextView)convertView.findViewById(R.id.discovery_standard_product_name_tv);// 标准产品名称
            holder.standardProductExtraInfoTv =
                (TextView)convertView.findViewById(R.id.discovery_standard_product_extras_tv);// 标准产品额外信息
            holder.standardProductTimeTv =
                (TextView)convertView.findViewById(R.id.discovery_standard_product__timeSchedual_tv);// 标准产品时间
            holder.standardProductDistanceTv =
                (TextView)convertView.findViewById(R.id.discovery_standard_product_distance_tv);// 标准产品路程距离
            holder.discoveryTeamProductContainer = (LinearLayout)convertView // 搜索结果-团购产品
            .findViewById(R.id.layout_discovery_team_product_container);
            holder.teamProductPhotoIv = (ImageView)convertView.findViewById(R.id.discovery_team_product_photo_iv);// 团购产品照片
            holder.teamProductNameTv = (TextView)convertView.findViewById(R.id.discovery_team_product_name_tv); // 团购产品名字
            holder.teamProductExtraInfoTv = (TextView)convertView.findViewById(R.id.discovery_team_product_extras_tv);// 团购产品额外信息
            holder.teamProductDayTv = (TextView)convertView.findViewById(R.id.discovery_team_product_timeSchedual_tv);// 团购产品天数
            holder.teamProductTimeSpaceTv =
                (TextView)convertView.findViewById(R.id.discovery_team_product_time_distance_tv);// 团购产品时间
            holder.discoverySingleProductContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_discovery_single_product_container);// 搜索结果-单产品
            holder.singleProductPhotoIv = (ImageView)convertView.findViewById(R.id.discovery_single_product_photo_iv);// 单产品logo
            holder.singleProductNameTv = (TextView)convertView.findViewById(R.id.discovery_single_product_name_tv);// 单产品名字
            holder.singleProductExtraInfoTv =
                (TextView)convertView.findViewById(R.id.discovery_single_product_extras_tv);// 单产品主题
            holder.singleProductDayTv =
                (TextView)convertView.findViewById(R.id.discovery_single_product__timeSchedual_tv);// 单产品
                                                                                                   // 天数
            holder.singleProductDistanceTv =
                (TextView)convertView.findViewById(R.id.discovery_single_product_distance_tv);// 单产品
                                                                                              // 里程
            holder.discoveryServicerContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_discovery_servicer_container);// 服务者
            holder.servicerIconIv = (RoundImageView)convertView.findViewById(R.id.discovery_servicer_icon_item_iv); // 服务者头像
            holder.servicerNameTv = (TextView)convertView.findViewById(R.id.discovery_servicer_name_tv);// 服务者名字
            holder.servicerTitleTv = (TextView)convertView.findViewById(R.id.discovery_servicer_titles_tv);// 服务者头衔
            holder.discoveryDestinationContainer =
                (LinearLayout)convertView.findViewById(R.id.layout_discovery_destination_container);// 目的地
            holder.destinationHeadView = (ImageView)convertView.findViewById(R.id.discovery_destination_head_iv);// 目的地
                                                                                                                 // imageview
            holder.destinationNameTv = (TextView)convertView.findViewById(R.id.discovery_destination_name_tv); // 目的地
                                                                                                               // 名称
            holder.destinationAchivementTv =
                (TextView)convertView.findViewById(R.id.discovery_destination_achivement_count_tv);// 目的地
                                                                                                   // 达人数量
            holder.destinationProductCountTv =
                (TextView)convertView.findViewById(R.id.discovery_destination_product_count_tv);// 目的地产品数量
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        if ("base".equals((String)mapItem.get("itemType")))// 标准产品
        {
            holder.discoveryStandardProductContainer.setVisibility(View.VISIBLE); // 标准产品布局可见
            holder.discoveryTeamProductContainer.setVisibility(View.GONE);// 组团产品布局隐藏
            holder.discoveryServicerContainer.setVisibility(View.GONE); // 服务者布局隐藏
            holder.discoveryDestinationContainer.setVisibility(View.GONE); // 目的地布局隐藏
            holder.discoverySingleProductContainer.setVisibility(View.GONE); // 单产品布局隐藏
            if (!StringUtils.isEmpty((String)mapItem.get("baseProductLogoUrl")))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("baseProductLogoUrl"),
                    holder.standardProductPhotoIv,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            holder.standardProductNameTv.setText((String)mapItem.get("productNameStr"));
            holder.standardProductTimeTv.setText((String)mapItem.get("productDaysStr") + "天");
            holder.standardProductDistanceTv.setText((String)mapItem.get("productDistanceStr") + "km");
            holder.standardProductExtraInfoTv.setText((String)mapItem.get("productTopicStr"));
        }
        else if ("single".equals((String)mapItem.get("itemType")))// 单产品
        {
            holder.discoverySingleProductContainer.setVisibility(View.VISIBLE);
            holder.discoveryStandardProductContainer.setVisibility(View.GONE);
            holder.discoveryTeamProductContainer.setVisibility(View.GONE);
            holder.discoveryServicerContainer.setVisibility(View.GONE);
            holder.discoveryDestinationContainer.setVisibility(View.GONE);
            if (!StringUtils.isEmpty((String)mapItem.get("singleProductLogoUrl")))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("singleProductLogoUrl"),
                    holder.singleProductPhotoIv,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            holder.singleProductNameTv.setText((String)mapItem.get("productNameStr"));
            holder.singleProductDayTv.setText((String)mapItem.get("productDaysStr") + "天");
            holder.singleProductDistanceTv.setText((String)mapItem.get("productDistanceStr") + "km");
            holder.singleProductExtraInfoTv.setText((String)mapItem.get("productTopicStr"));
            
        }
        else if ("team".equals((String)mapItem.get("itemType")))
        { // 组团产品
            holder.discoveryTeamProductContainer.setVisibility(View.VISIBLE);
            holder.discoverySingleProductContainer.setVisibility(View.GONE);
            holder.discoveryStandardProductContainer.setVisibility(View.GONE);
            holder.discoveryServicerContainer.setVisibility(View.GONE);
            holder.discoveryDestinationContainer.setVisibility(View.GONE);
            if (!StringUtils.isEmpty((String)mapItem.get("teamProductLogoUrl")))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("teamProductLogoUrl"),
                    holder.teamProductPhotoIv,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            holder.teamProductNameTv.setText((String)mapItem.get("productNameStr"));
            holder.teamProductExtraInfoTv.setText((String)mapItem.get("productTopicStr"));
            holder.teamProductDayTv.setText((String)mapItem.get("productDaysStr") + "天");
            holder.singleProductDistanceTv.setText((String)mapItem.get("productDistanceStr") + "km");
            
        }
        else if ("user".equals((String)mapItem.get("itemType")))
        { // 服务者
            holder.discoveryServicerContainer.setVisibility(View.VISIBLE);
            holder.discoveryTeamProductContainer.setVisibility(View.GONE);
            holder.discoverySingleProductContainer.setVisibility(View.GONE);
            holder.discoveryStandardProductContainer.setVisibility(View.GONE);
            holder.discoveryDestinationContainer.setVisibility(View.GONE);
            if (StringUtils.isEmpty((String)mapItem.get("userLogoUrl")))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("userLogoUrl"),
                    holder.servicerIconIv,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            holder.servicerNameTv.setText((String)mapItem.get("userNameStr"));
            holder.servicerTitleTv.setText((String)mapItem.get("userTagStr"));
            
        }
        else if ("destination".equals((String)mapItem.get("itemType")))
        { // 目的地
            holder.discoveryDestinationContainer.setVisibility(View.VISIBLE);
            holder.discoveryServicerContainer.setVisibility(View.GONE);
            holder.discoveryTeamProductContainer.setVisibility(View.GONE);
            holder.discoverySingleProductContainer.setVisibility(View.GONE);
            holder.discoveryStandardProductContainer.setVisibility(View.GONE);
            if (!StringUtils.isEmpty((String)mapItem.get("destLogoUrl")))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("destLogoUrl"),
                    holder.destinationHeadView,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            holder.destinationNameTv.setText((String)mapItem.get("destNameStr"));
            holder.destinationAchivementTv.setText((String)mapItem.get("destsNum") + "名达人"); // 目的地达人数量
            holder.destinationProductCountTv.setText((String)mapItem.get("destpNum") + "个旅游产品"); // 目的地产品数量
            
        }
        // 设置第一个tag标签 holder
        // convertView.setTag(R.id.tag_search_first, holder);
        // 设置第二个tag标签 type
        // convertView.setTag(R.id.tag_search_second, Integer.parseInt((String)
        // mapItem.get("productType")));
        return convertView;
    }
    
    /**
     * ViewHolder 内部类
     * 
     */
    class ViewHolder
    {
        LinearLayout discoveryStandardProductContainer;// 搜索结果-标准产品layout
        
        LinearLayout discoveryTeamProductContainer;// 搜索结果-跟团购产品layout
        
        LinearLayout discoverySingleProductContainer;// 搜索结果-单产品layout
        
        LinearLayout discoveryServicerContainer; // 搜索结果-服务者layout
        
        LinearLayout discoveryDestinationContainer;// 搜索结果-目的地layout
        
        /* 搜索结果-标准产品 */
        ImageView standardProductPhotoIv;// 搜索结果-标准产品-照片imageview
        
        TextView standardProductNameTv;// 搜索结果-标准产品-产品名字tv
        
        TextView standardProductExtraInfoTv;// 搜索结果-标准产品-额外的信息(国家 地区 标签)
        
        TextView standardProductTimeTv;// 搜索结果-标准产品-产品 天数
        
        TextView standardProductDistanceTv;// 搜索结果-标准产品 -产品行驶里程
        
        /* 搜索结果-团购产品 */
        ImageView teamProductPhotoIv;// 搜索结果-团购产品-团购产品照片imageview
        
        TextView teamProductNameTv;// 搜索结果-团购产品名字-团购产品名字tv
        
        TextView teamProductExtraInfoTv;// 搜索结果-团购产品-团购产品的额外信息(国家 地区 标签)
        
        TextView teamProductDayTv;// 搜索结果-团购产品 -团购产品天数
        
        TextView teamProductTimeSpaceTv;// 搜索结果-团购产品 -开始时间和 结束时间
        
        /* 搜索结果-单产品 */
        ImageView singleProductPhotoIv;// 单产品-照片imageview
        
        TextView singleProductNameTv;// 单产品名字tv
        
        TextView singleProductExtraInfoTv;// 单产品主题
        
        TextView singleProductDayTv;// 单产品天数
        
        TextView singleProductDistanceTv;// 单产品-里程
        
        /* 搜索结果-服务者 */
        RoundImageView servicerIconIv; // 搜索结果-服务者头像
        
        TextView servicerNameTv; // 搜索结果-服务者姓名
        
        TextView servicerTitleTv; // 搜索结果-服务者头衔
        
        /* 搜索结果-目的地 */
        ImageView destinationHeadView; // 搜索结果-目的地imageview
        
        TextView destinationNameTv;// 搜索结果-目的地名字textview
        
        TextView destinationAchivementTv;// 搜索结果-达人数量 textview
        
        TextView destinationProductCountTv;// 搜索结果-目的地产品数量 textview
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }
}
