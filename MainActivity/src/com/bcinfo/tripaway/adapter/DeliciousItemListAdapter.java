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
 * 美食 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月20日 15:42:11
 * 
 */
public class DeliciousItemListAdapter extends BaseAdapter
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
    
    public DeliciousItemListAdapter(Context context, List<Map<String, Object>> itemList)
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
            convertView = inflator.inflate(R.layout.item_food_service_layout, null);
            holder.deliciousIv = (ImageView)convertView.findViewById(R.id.food_item_ImageView);
            holder.deliciousNameTv = (TextView)convertView.findViewById(R.id.food_item_name);
            holder.foodLocationTv = (TextView)convertView.findViewById(R.id.food_item_first);
            holder.foodCategoryTv = (TextView)convertView.findViewById(R.id.food_item_second);
            holder.foodPriceRangeTv = (TextView)convertView.findViewById(R.id.food_item_third);
            holder.currentStateTv = (TextView)convertView.findViewById(R.id.food_item_fourth);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        if (!StringUtils.isEmpty((String)mapItem.get("foodUrl")))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("foodUrl"), holder.deliciousIv);
        }
        
        holder.deliciousNameTv.setText("Okolabama Joes 奥克红");
        holder.foodLocationTv.setText("堪萨斯州");
        holder.foodCategoryTv.setText("烤肉");
        holder.foodPriceRangeTv.setText("价格适中");
        holder.currentStateTv.setText("需排队");
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView deliciousIv;// 美食图片imageview
        
        private TextView deliciousNameTv;// 美食name
        
        private TextView foodLocationTv;// 美食地区
        
        private TextView foodCategoryTv;// 美食种类
        
        private TextView foodPriceRangeTv;// 美食价格
        
        private TextView currentStateTv;// 当前状态
        
    }
    
}
