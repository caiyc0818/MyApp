package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 发现-目的地产品GridAdapter
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2015年2月5日 20:53:56
 * 
 * 
 */
public class DiscoveryLocationProductGridAdapter extends BaseAdapter implements OnClickListener
{
    private List<ProductInfo> productList;
    
    private Context context;
    
    private ImageLoader locationUrlLoader;
    
    private LayoutInflater inflator;
    
    public List<ProductInfo> getProductList()
    {
        return productList;
    }
    
    public void setProductList(List<ProductInfo> productList)
    {
        this.productList = productList;
    }
    
    public DiscoveryLocationProductGridAdapter()
    {
    }
    
    /**
     * 自定义参数构造器
     * 
     */
    public DiscoveryLocationProductGridAdapter(List<ProductInfo> productList, Context context)
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
            convertView = (View)inflator.inflate(R.layout.item_discovery_location_products, null);
            holder.locationProductIcon = (ImageView)convertView.findViewById(R.id.img_discovery_location_products_item);
            int imgHeight = BaseActivity.screenWidth * 9 / 16;
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, imgHeight);
            holder.locationProductIcon.setLayoutParams(lp);
            holder.locationProductName =
                (TextView)convertView.findViewById(R.id.title_item_discovery_location_products);
            holder.locationProductCount =
                (TextView)convertView.findViewById(R.id.items_discovery_location_products_comments);
            holder.locationProductServicerName =
                (TextView)convertView.findViewById(R.id.items_discovery_location_products_servicerName);
            holder.locationProductPrice =
                (TextView)convertView.findViewById(R.id.items_discovery_location_products_price);
            holder.locationProductAuthorIcon =
                (ImageView)convertView.findViewById(R.id.items_discovery_location_products_servicerIcon);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        ProductInfo product = (ProductInfo)productList.get(position);
        // ImageLoader.getInstance().displayImage(product.getLogoUrls().get(0), holder.locationProductIcon);
        // ImageListener locationHeadUriListener=ImageLoader.getImageListener(holder.locationProductAuthorIcon,
        // R.drawable.ic_launcher, R.drawable.ic_launcher);
        // locationUrlLoader.get(product.getAuthorPhotoUrl(), locationHeadUriListener);
        if (!StringUtils.isEmpty(product.getAuthorPhotoUrl()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + product.getAuthorPhotoUrl(),
                holder.locationProductAuthorIcon);
        }
        
        holder.locationProductName.setText(product.getName());
        holder.locationProductCount.setText(product.getEvaluateNum()
            + context.getResources().getString(R.string.trip_comments));
        holder.locationProductServicerName.setText(product.getAuthorName());
        holder.locationProductPrice.setText(product.getPrice());
        // ImageLoader.getInstance().displayImage(product.getAuthorPhotoUrl(), holder.locationProductAuthorIcon);
        holder.locationProductAuthorIcon.setOnClickListener(this);
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
        
        // 产品发布者名字
        private ImageView locationProductAuthorIcon;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.items_discovery_location_products_servicerIcon:
                Intent personalInfoIntent = new Intent(context, PersonalInfoNewActivity.class);
                context.startActivity(personalInfoIntent);
                break;
            default:
                break;
        }
    }
}
