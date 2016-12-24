package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 单一主题产品列表适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年2月5日 16:00:11
 */
public class ThemeProductGridAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ProductInfo> productList;
    
    public ThemeProductGridAdapter(Context context, List<ProductInfo> productList)
    {
        this.context = context;
        this.productList = productList;
    }
    
    @Override
    public int getCount()
    {
        return productList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return productList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        LayoutInflater inflator = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(R.layout.item_discovery_theme_product_list, parent, false);
            holder.themeProductIcon = (ImageView)convertView.findViewById(R.id.discovery_theme_product_header_icon);
            holder.themeProductPrice = (TextView)convertView.findViewById(R.id.discovery_theme_product_header_price);
            holder.themeProductName = (TextView)convertView.findViewById(R.id.discovery_theme_product_name);
            holder.themeProductLocation = (TextView)convertView.findViewById(R.id.discovery_theme_product_location);
            holder.themeProductAuthorName = (TextView)convertView.findViewById(R.id.discovery_theme_product_sponsor);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        // ImageLoader.getInstance().displayImage(productList.get(position).getLogoUrls().get(0),
        // holder.themeProductIcon);
        if (!StringUtils.isEmpty(productList.get(position).getLogoUrls().get(0)))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + productList.get(position).getLogoUrls().get(0),
                holder.themeProductIcon);
        }
        
        holder.themeProductPrice.setText(productList.get(position).getPrice());
        holder.themeProductName.setText(productList.get(position).getName());
        holder.themeProductLocation.setText(productList.get(position).getAddress());
        holder.themeProductAuthorName.setText(productList.get(position).getAuthorName());
        return convertView;
    }
    
    class ViewHolder
    {
        // 产品icon
        private ImageView themeProductIcon;
        
        // 产品标价
        private TextView themeProductPrice;
        
        // 产品名称
        private TextView themeProductName;
        
        // 产品地点
        private TextView themeProductLocation;
        
        // 产品发布者名字
        private TextView themeProductAuthorName;
    }
}
