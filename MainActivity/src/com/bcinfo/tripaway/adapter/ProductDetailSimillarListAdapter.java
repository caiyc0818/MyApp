package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品详情相似产品列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class ProductDetailSimillarListAdapter extends BaseAdapter
{
    private static final String TAG = "ProductDetailSimillarListAdapter";
    
    private Context mContext;
    
    private List<ProductInfo> mItemList;
    
    public ProductDetailSimillarListAdapter(Context context, List<ProductInfo> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mItemList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mItemList.get(position);
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
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.product_detail_similar_listitem, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.productLogo = (ImageView)convertView.findViewById(R.id.similar_product_logo);
        holder.productName = (TextView)convertView.findViewById(R.id.similar_product_name);
        holder.productService = (TextView)convertView.findViewById(R.id.similar_product_service);
        holder.productPrice = (TextView)convertView.findViewById(R.id.similar_product_price);
        holder.productEndDate = (TextView)convertView.findViewById(R.id.product_end_date);
        holder.productEndDateLayout = (LinearLayout)convertView.findViewById(R.id.product_end_date_layout);
        ArrayList<String> logoUrls = mItemList.get(position).getLogoUrls();
        if (logoUrls != null && logoUrls.size() > 0)
        {
            if (!StringUtils.isEmpty(logoUrls.get(0)))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + logoUrls.get(0), holder.productLogo);
            }
        }
        holder.productName.setText(mItemList.get(position).getName());
        holder.productService.setText(mItemList.get(position).getService());
        holder.productPrice.setText(mItemList.get(position).getPrice());
        String type = mItemList.get(position).getType();
        if (type != null)
        {
            if (type.equals("2"))
            {
                holder.productEndDateLayout.setVisibility(View.VISIBLE);
                holder.productEndDate.setVisibility(View.VISIBLE);
                holder.productService.setVisibility(View.GONE);
            }
            else
            {
                holder.productEndDateLayout.setVisibility(View.GONE);
                holder.productEndDate.setVisibility(View.GONE);
                holder.productService.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 产品图片
         */
        public ImageView productLogo;
        
        /**
         * 产品名称
         */
        public TextView productName;
        
        /**
         * 产品描述
         */
        public TextView productService;
        
        /**
         * 产品价格
         */
        public TextView productPrice;
        
        /**
         * 团购产品结束日期
         */
        public TextView productEndDate;
        
        /**
         * 团购产品结束日期布局
         */
        public LinearLayout productEndDateLayout;
    }
}
