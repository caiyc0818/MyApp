package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.annotation.SuppressLint;
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
 * 精选-目的地产品GridAdapter
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2015年2月5日 20:53:56
 * 
 * 
 */
public class PickedLocationProductGridAdapter extends BaseAdapter
{
    private List<ProductInfo> productList;
    
    private Context context;
    
    private LayoutInflater inflator;
    
    public List<ProductInfo> getProductList()
    {
        return productList;
    }
    
    public void setProductList(List<ProductInfo> productList)
    {
        this.productList = productList;
    }
    
    public PickedLocationProductGridAdapter()
    {
        
    }
    
    /**
     * 自定义参数构造器
     * 
     */
    public PickedLocationProductGridAdapter(List<ProductInfo> productList, Context context)
    {
        
        this.productList = productList;
        
        this.context = context;
        
    }
    
    /**
     * 获取adapter中数据的个数
     */
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
    
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Layoutinflator加载列表项布局文件
        inflator = LayoutInflater.from(context);
        
        ViewHolder holder = null;
        
        if (convertView == null)
        {
            holder = new ViewHolder();
            
            convertView = (View)inflator.inflate(R.layout.items_picked_location_products, null);
            
            holder.locationProductIcon = (ImageView)convertView.findViewById(R.id.picked_location_product_icon);
            holder.locationProductName = (TextView)convertView.findViewById(R.id.picked_location_product_title);
            holder.locationProductCount = (TextView)convertView.findViewById(R.id.picked_location_product_comments);
            holder.locationProductServicerName =
                (TextView)convertView.findViewById(R.id.picked_location_product_servicerName);
            holder.locationProductPrice = (TextView)convertView.findViewById(R.id.picked_location_product_price);
            
            convertView.setTag(holder);
            
        }
        else
        {
            
            holder = (ViewHolder)convertView.getTag();
            
        }
        
        ProductInfo product = (ProductInfo)productList.get(position);
        if (!StringUtils.isEmpty(product.getLogoUrls().get(0)))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + product.getLogoUrls().get(0),
                holder.locationProductIcon);
        }
        
        holder.locationProductName.setText(product.getName());
        holder.locationProductCount.setText(product.getEvaluateNum()
            + context.getResources().getString(R.string.trip_comments));
        holder.locationProductServicerName.setText(product.getAuthorName());
        holder.locationProductPrice.setText(product.getPrice());
        
        return convertView;
    }
    
    /**
     * 定义ViewHolder类 该类中的变量是列表项中的控件
     * 
     */
    class ViewHolder
    {
        // 产品 icon
        private ImageView locationProductIcon;
        
        // 产品名称
        private TextView locationProductName;
        
        // 产品评价数量
        private TextView locationProductCount;
        
        // 产品服务者名字
        private TextView locationProductServicerName;
        
        // 产品价格
        private TextView locationProductPrice;
        
    }
}
