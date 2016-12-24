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
 * 购物 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月20日 15:42:11
 * 
 */
public class ShoppingItemListAdapter extends BaseAdapter
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
    
    public ShoppingItemListAdapter(Context context, List<Map<String, Object>> itemList)
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
            convertView = inflator.inflate(R.layout.item_shop_service_layout, null);
            holder.shoppingIv = (ImageView)convertView.findViewById(R.id.shop_item_ImageView);
            holder.shoppingThemeTv = (TextView)convertView.findViewById(R.id.shop_item_name);
            holder.shoppingLocationTv = (TextView)convertView.findViewById(R.id.shop_item_first);
            holder.shoppingCategoryTv = (TextView)convertView.findViewById(R.id.shop_item_second);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        if (!StringUtils.isEmpty((String)mapItem.get("shoppingUrl")))
        {
            ImageLoader.getInstance()
                .displayImage(Urls.imgHost + (String)mapItem.get("shoppingUrl"), holder.shoppingIv);
        }
        
        holder.shoppingThemeTv.setText("帕沙迪纳老城购物区 ");
        
        holder.shoppingLocationTv.setText("洛杉矶");
        holder.shoppingCategoryTv.setText("古典艺术");
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView shoppingIv;// 购物imageview
        
        private TextView shoppingThemeTv;// 购物主题
        
        private TextView shoppingLocationTv;// 购物地点
        
        private TextView shoppingCategoryTv;// 购物种类
        
    }
    
}
