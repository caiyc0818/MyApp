package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AttractionAllInfo;
import com.bcinfo.tripaway.bean.Cate;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.HotelInfo;
import com.bcinfo.tripaway.bean.HouseInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.PoiInfo;
import com.bcinfo.tripaway.bean.ShoppingInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 行程规划详情列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class TripPlanDetailNewListChildAdapter extends BaseAdapter
{
    private static final String TAG = "TripPlanDetailNewListChildAdapter";
    
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    
    private Context mContext;
    
    
    private List<AttractionAllInfo> childList;
    
    public TripPlanDetailNewListChildAdapter(Context context)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        // this.childList = mItemList;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return childList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return childList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        AttractionAllInfo resource = childList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.trip_plan_detail_child_listview_item, null);
            holder = new ViewHolder();
            holder.scenicName = (TextView)convertView.findViewById(R.id.scenic_name);
            
            //
            holder.alis = (TextView)convertView.findViewById(R.id.scenic_spot_englishname);
            
            holder.scenic_flag_layout = (LinearLayout)convertView.findViewById(R.id.scenic_flag_layout);
            holder.scenicPhoto = (ImageView)convertView.findViewById(R.id.scenic_photo);
            holder.scenicDescription = (TextView)convertView.findViewById(R.id.scenic_description);
            holder.layout1 = (LinearLayout)convertView.findViewById(R.id.layout1);
            holder.layout2 = (LinearLayout)convertView.findViewById(R.id.layout2);
            holder.layout3 = (LinearLayout)convertView.findViewById(R.id.layout3);
            holder.layout4 = (LinearLayout)convertView.findViewById(R.id.layout4);
            holder.layout5 = (LinearLayout)convertView.findViewById(R.id.layout5);
            holder.layout6 = (LinearLayout)convertView.findViewById(R.id.layout6);
            holder.layout7 = (LinearLayout)convertView.findViewById(R.id.layout7);
            
            holder.title1 = (TextView)convertView.findViewById(R.id.title1);
            holder.title2 = (TextView)convertView.findViewById(R.id.title2);
            holder.title3 = (TextView)convertView.findViewById(R.id.title3);
            holder.title4 = (TextView)convertView.findViewById(R.id.title4);
            holder.title5 = (TextView)convertView.findViewById(R.id.title5);
            holder.title6 = (TextView)convertView.findViewById(R.id.title6);
            holder.title7 = (TextView)convertView.findViewById(R.id.title7);
            
            holder.logo1 = (ImageView)convertView.findViewById(R.id.logo1);
            holder.logo2 = (ImageView)convertView.findViewById(R.id.logo2);
            holder.logo3 = (ImageView)convertView.findViewById(R.id.logo3);
            holder.logo4 = (ImageView)convertView.findViewById(R.id.logo4);
            holder.logo5 = (ImageView)convertView.findViewById(R.id.logo5);
            holder.logo6 = (ImageView)convertView.findViewById(R.id.logo6);
            holder.logo7 = (ImageView)convertView.findViewById(R.id.logo7);
            
            holder.text1 = (TextView)convertView.findViewById(R.id.text1);
            holder.text2 = (TextView)convertView.findViewById(R.id.text2);
            holder.text3 = (TextView)convertView.findViewById(R.id.text3);
            holder.text4 = (TextView)convertView.findViewById(R.id.text4);
            holder.text5 = (TextView)convertView.findViewById(R.id.text5);
            holder.text6 = (TextView)convertView.findViewById(R.id.text6);
            holder.text7 = (TextView)convertView.findViewById(R.id.text7);
            holder.gridTitle1 = (TextView)convertView.findViewById(R.id.grid_title1);
            holder.gridTitle2 = (TextView)convertView.findViewById(R.id.grid_title2);
            holder.gridView1 = (GridView)convertView.findViewById(R.id.grid1);
            holder.gridView2 = (GridView)convertView.findViewById(R.id.grid2);
            holder.scenic_detail_listview = (MyListView)convertView.findViewById(R.id.scenic_detail_listview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.scenicName.setText((resource).getServName());
        holder.alis.setText((resource).getServAlias());// daxie
        
        holder.scenic_flag_layout.removeAllViews();
        holder.scenic_flag_layout.addView(getLabelLogo());
        if ((resource).getTags().size() == 0)
        {
            holder.scenic_flag_layout.setVisibility(View.GONE);
        }
        else
        {
            for (int i = 0; i < (resource).getTags().size(); i++)
            {
                holder.scenic_flag_layout.addView(getLable((resource).getTags().get(i)));
            }
        }
        // finalBitmap.display(holder.scenicPhoto, resource.getTitleImage());
        if (!StringUtils.isEmpty(resource.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), holder.scenicPhoto);
        }
        
        //ImageLoader.getInstance().displayImage(Urls.imgHost +resource.getTitleImage(), holder.scenicPhoto);
        holder.scenicDescription.setText(( resource).getServDesc());
        
        holder.layout1.setVisibility(View.GONE);
        holder.layout2.setVisibility(View.GONE);
        holder.layout3.setVisibility(View.GONE);
        holder.layout4.setVisibility(View.GONE);
        holder.layout5.setVisibility(View.GONE);
        holder.layout6.setVisibility(View.GONE);
        holder.layout7.setVisibility(View.GONE);
        holder.gridTitle1.setVisibility(View.GONE);
        holder.gridTitle2.setVisibility(View.GONE);
        holder.gridView1.setVisibility(View.GONE);
        holder.gridView2.setVisibility(View.GONE);
        
        List<ImageInfo> imageInfos = null;
        if ((resource).getServType().equals("cate"))
        {
            Cate cate = JsonUtil.getCate((resource).getResourceExt());
            holder.layout1.setVisibility(View.VISIBLE);
            holder.layout2.setVisibility(View.VISIBLE);
            holder.layout3.setVisibility(View.VISIBLE);
            holder.title1.setText("人均消费");
            holder.title2.setText("地址");
            holder.title3.setText("时间");
            if(StringUtils.isEmpty(resource.getFee()))
                holder.layout1.setVisibility(View.GONE);
            else 
            	holder.layout1.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(cate.getAddress()))
                holder.layout2.setVisibility(View.GONE);
            else 
            	holder.layout2.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(cate.getBusinessHours()))
                holder.layout3.setVisibility(View.GONE);
            else 
            	holder.layout3.setVisibility(View.VISIBLE);
            if ((resource).getFee() == null || (resource).getFee().equals(" "))
            {
                holder.text1.setText("-");
            }
            else
            {
                holder.text1.setText("￥" + (resource).getFee());
            }
            holder.text2.setText(cate.getAddress());
            holder.text3.setText(cate.getBusinessHours());
            imageInfos = cate.getImageInfoList();
        }
        else if ((resource).getServType().equals("shopping"))
        {
            holder.layout2.setVisibility(View.VISIBLE);
            holder.layout3.setVisibility(View.VISIBLE);
            
            holder.title2.setText("地址");
            holder.title3.setText("营业时间");
            ShoppingInfo info = JsonUtil.getShoppingInfo((resource).getResourceExt());
            if(StringUtils.isEmpty(info.getAddress()))
                holder.layout2.setVisibility(View.GONE);
            else 
            	holder.layout2.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getBusinessHours()))
                holder.layout3.setVisibility(View.GONE);
            else 
            	holder.layout3.setVisibility(View.VISIBLE);
            holder.text2.setText(info.getAddress());
            holder.text3.setText(info.getBusinessHours());
            imageInfos = info.getImageInfoList();
        }
        else if ((resource).getServType().equals("poi"))
        {
        	
            // holder.layout4.setVisibility(View.VISIBLE);
            holder.title1.setText("门票");
            holder.title2.setText("时间");
            holder.title3.setText("地址");
            // holder.title4.setText("门票");
            PoiInfo info = JsonUtil.getPoiInfo((resource).getResourceExt());
            if(StringUtils.isEmpty(info.getTicket()))
                holder.layout1.setVisibility(View.GONE);
            else 
            	holder.layout1.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getBusinessHours()))
                holder.layout2.setVisibility(View.GONE);
            else 
            	holder.layout2.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getAddress()))
                holder.layout3.setVisibility(View.GONE);
            else 
            	holder.layout3.setVisibility(View.VISIBLE);
            if (info.getTicket() == null || info.getTicket().equals(""))
            {
                holder.text1.setText("-");
            }
            else
            {
                holder.text1.setText(info.getTicket());
            }
            
            if (info.getBusinessHours() != null)
            {
                holder.text2.setText(info.getBusinessHours());
            }
            else
            {
                holder.text2.setText("-");
            }
            if (info.getAddress() != null)
            {
                holder.text3.setText(info.getAddress());
            }
            else
            {
                holder.text3.setText("-");
            }
            /*
             * if(info.getTicket()!=null){ holder.text4.setText(info.getTicket()); }else{ holder.text4.setText("-"); }
             */
            /*
             * if(info.getScenery_alias()==null||info.getScenery_alias().equals("")){ holder.text4.setText("-"); }else{
             * holder.text4.setText(info.getScenery_alias()); }
             */
            
            /* holder.text3.setText(info.getBusinessHours()); */
            
            imageInfos = info.getImageInfoList();
        }
        else if ((resource).getServType().equals("hotel"))
        {
            holder.layout2.setVisibility(View.VISIBLE);
            holder.layout3.setVisibility(View.VISIBLE);
            holder.layout4.setVisibility(View.VISIBLE);
            holder.layout5.setVisibility(View.VISIBLE);
            // holder.layout6.setVisibility(View.VISIBLE);
            holder.title2.setText("酒店星级");
            holder.title3.setText("房间类型");
            holder.title4.setText("地址");
            holder.title5.setText("入住退房时间");
            // holder.title6.setText("退房时间");
            HotelInfo info = JsonUtil.getHotelInfo((resource).getResourceExt());
            if(StringUtils.isEmpty(info.getStarLevel()))
                holder.layout2.setVisibility(View.GONE);
            else 
            	holder.layout2.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getBedType()))
                holder.layout3.setVisibility(View.GONE);
            else 
            	holder.layout3.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getAddress()))
                holder.layout4.setVisibility(View.GONE);
            else 
            	holder.layout4.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getCheckTime()))
                holder.layout5.setVisibility(View.GONE);
            else 
            	holder.layout5.setVisibility(View.VISIBLE);
            holder.text2.setText(info.getStarLevel());
            holder.text3.setText(info.getBedType());
            holder.text4.setText(info.getAddress());
            holder.text5.setText(info.getCheckTime());
            // holder.text6.setText("");
            
            imageInfos = info.getImages();
        }
        else if ((resource).getServType().equals("house"))
        {
            holder.layout2.setVisibility(View.VISIBLE);
            holder.layout3.setVisibility(View.VISIBLE);
            holder.layout4.setVisibility(View.VISIBLE);
            // holder.layout5.setVisibility(View.VISIBLE);
            holder.layout6.setVisibility(View.VISIBLE);
            holder.layout7.setVisibility(View.VISIBLE);
            holder.title2.setText("地址");
            holder.title3.setText("房间类型");
            holder.title4.setText("入住退房时间");
            // holder.title5.setText("退房时间");
            holder.title6.setText("可住人数");
            holder.title7.setText("卫生间数");
            
            HouseInfo info = JsonUtil.getHouseInfo((resource).getResourceExt());
            if(StringUtils.isEmpty(info.getAddress()))
                holder.layout2.setVisibility(View.GONE);
            else 
            	holder.layout2.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getBedType()))
                holder.layout3.setVisibility(View.GONE);
            else 
            	holder.layout3.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getCheckTime()))
                holder.layout4.setVisibility(View.GONE);
            else 
            	holder.layout4.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getHoldnum()))
                holder.layout6.setVisibility(View.GONE);
            else 
            	holder.layout6.setVisibility(View.VISIBLE);
            if(StringUtils.isEmpty(info.getToilet()))
                holder.layout7.setVisibility(View.GONE);
            else 
            	holder.layout7.setVisibility(View.VISIBLE);
            holder.text2.setText(info.getAddress());
            holder.text3.setText(info.getBedType());
            holder.text4.setText(info.getCheckTime());
            // holder.text5.setText("");
            holder.text6.setText(info.getHoldnum());
            holder.text7.setText(info.getToilet());
            holder.gridTitle1.setVisibility(View.VISIBLE);
            holder.gridTitle2.setVisibility(View.VISIBLE);
            holder.gridView1.setVisibility(View.VISIBLE);
            holder.gridView2.setVisibility(View.VISIBLE);
            
            holder.gridTitle1.setText("房内配套");
            holder.gridTitle2.setText("其他");
            for (int j = 0; j < info.getHouseServs().size(); j++)
            {
                if (info.getHouseServs().get(j).getCateCode().equals("facility_rooms"))
                {
                    List<Facility> facilityRoomsList = new ArrayList<Facility>();
                    facilityRoomsList.addAll(info.getHouseServs().get(j).getFacilities());
                    CarInfoGridViewAdapter insideAdapter = new CarInfoGridViewAdapter(mContext, facilityRoomsList);
                    holder.gridView1.setAdapter(insideAdapter);
                }
                else
                {
                    List<Facility> facilityOtherList = new ArrayList<Facility>();
                    facilityOtherList.addAll(info.getHouseServs().get(j).getFacilities());
                    CarInfoGridViewAdapter feeAdapter = new CarInfoGridViewAdapter(mContext, facilityOtherList);
                    holder.gridView2.setAdapter(feeAdapter);
                }
            }
            imageInfos = info.getImages();
        }
        if (imageInfos != null)
        {
            initScenicListView(holder.scenic_detail_listview, imageInfos);
        }
        return convertView;
    }
    
    private ImageView getLabelLogo()
    {
        ImageView img = new ImageView(mContext);
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        p.rightMargin = 4;
        img.setLayoutParams(p);
        img.setImageResource(R.drawable.label);
        return img;
    }
    
    private void initScenicListView(MyListView listView, List<ImageInfo> mItemList)
    {
        ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(mContext, mItemList);
        listView.setAdapter(adapter);
    }
    
    private LinearLayout getLable(String lable)
    {
        LinearLayout lableLayout = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.lable_layout, null);
        TextView lableText = (TextView)lableLayout.findViewById(R.id.lable_text);
        lableText.setText(lable);
        return lableLayout;
    }
    
    public class ViewHolder
    {
        
        //
        
        public TextView alis;
        
        /**
         * 景点名称
         */
        public TextView scenicName;
        
        /**
         * 景点标签列表
         */
        public LinearLayout scenic_flag_layout;
        
        /**
         * 景点照片ViewPager
         */
        public ImageView scenicPhoto;
        
        /**
         * 景点说明
         */
        public TextView scenicDescription;
        
        public LinearLayout layout1;
        
        public LinearLayout layout2;
        
        public LinearLayout layout3;
        
        public LinearLayout layout4;
        
        public LinearLayout layout5;
        
        public LinearLayout layout6;
        
        public LinearLayout layout7;
        
        public ImageView logo1;
        
        public ImageView logo2;
        
        public ImageView logo3;
        
        public ImageView logo4;
        
        public ImageView logo5;
        
        public ImageView logo6;
        
        public ImageView logo7;
        
        public TextView title1;
        
        public TextView title2;
        
        public TextView title3;
        
        public TextView title4;
        
        public TextView title5;
        
        public TextView title6;
        
        public TextView title7;
        
        public TextView text1;
        
        public TextView text2;
        
        public TextView text3;
        
        public TextView text4;
        
        public TextView text5;
        
        public TextView text6;
        
        public TextView text7;
        
        public TextView gridTitle1;
        
        public TextView gridTitle2;
        
        public GridView gridView1;
        
        public GridView gridView2;
        
        /**
         * 景点图文列表
         */
        public MyListView scenic_detail_listview;
    }
    
    /*
     * public void addAll(List<ProductServiceResource> list) { this.childList=list; notifyDataSetChanged(); }
     */
    
    public void clearAll()
    {
        this.childList.clear();
        notifyDataSetChanged();
    }
    
    public void addAll(ArrayList<AttractionAllInfo> arrayList)
    {
        // TODO Auto-generated method stub
        this.childList = arrayList;
        notifyDataSetChanged();
    }
}
