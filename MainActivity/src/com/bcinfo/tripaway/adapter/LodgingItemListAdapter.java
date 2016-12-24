package com.bcinfo.tripaway.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 住宿 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月20日 15:42:11
 * 
 */
public class LodgingItemListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Map<String, Object>> itemList;
    
    private LayoutInflater inflator;
    
    public List<Map<String, Object>> getItemList()
    {
        return itemList;
    }
    
    public void setItemList(List<Map<String, Object>> itemList)
    {
        this.itemList = itemList;
    }
    
    public LodgingItemListAdapter(Context context, List<Map<String, Object>> itemList)
    {
        super();
        this.context = context;
        this.itemList = itemList;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return itemList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return itemList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        Map<String, Object> mapItem = itemList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_lodging_service_layout, null);
            holder.lodgeIv = (ImageView)convertView.findViewById(R.id.lodging_item_ImageView);
            holder.lodgeNameTv = (TextView)convertView.findViewById(R.id.lodging_item_name);
            holder.lodgeStyleTv = (TextView)convertView.findViewById(R.id.lodging_item_first);
            holder.lodgeSecondTv = (TextView)convertView.findViewById(R.id.lodging_item_second);
            holder.lodgeVisitorCountTv = (TextView)convertView.findViewById(R.id.lodging_item_third);
            holder.lodgeBedCountTv = (TextView)convertView.findViewById(R.id.lodging_item_fourth);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        if (!StringUtils.isEmpty((String)mapItem.get("accommadtedUrl")))
        {
            ImageLoader.getInstance()
                .displayImage(Urls.imgHost + (String)mapItem.get("accommadtedUrl"), holder.lodgeIv);
        }
        
        holder.lodgeNameTv.setText("Ocean View MaliBu Hideaway");
        holder.lodgeStyleTv.setText("整套/公寓");
        holder.lodgeSecondTv.setText("1间卧室");
        holder.lodgeVisitorCountTv.setText("5位房客");
        holder.lodgeBedCountTv.setText("5张床");
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView lodgeIv;// 住宿图片imageview
        
        private TextView lodgeNameTv;// 住宿name
        
        private TextView lodgeStyleTv;// 住宿样式
        
        private TextView lodgeSecondTv;// 住宿规格
        
        private TextView lodgeVisitorCountTv;// 房客数量
        
        private TextView lodgeBedCountTv;// 床位数量
        
    }
    
}
