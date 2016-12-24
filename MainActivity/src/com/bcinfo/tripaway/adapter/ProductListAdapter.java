package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的产品列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月9日 下午3:55:10
 */
public class ProductListAdapter extends BaseAdapter
{
    private static final String TAG = "ProductListAdapter";
    
    private Context mContext;
    
    private ArrayList<ProductInfo> mItemList;
    
    public ProductListAdapter(Context context, ArrayList<ProductInfo> list)
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
        final ViewHolder holder = new ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.product_list_item_group, null);
        holder.grayLine = (RelativeLayout)convertView.findViewById(R.id.gray_line);
        holder.groupProductLogo = (ImageView)convertView.findViewById(R.id.product_logo);
        holder.orderedForUser = (TextView)convertView.findViewById(R.id.product_order_for_user);
        holder.groupProductName = (TextView)convertView.findViewById(R.id.product_name);
        holder.userPhoto = (RoundImageView)convertView.findViewById(R.id.customize_user_photo);
        holder.groupOrderedNumber = (TextView)convertView.findViewById(R.id.ordered_number);
        holder.groupProductService = (TextView)convertView.findViewById(R.id.product_service_item);
        holder.groupEndDate = (TextView)convertView.findViewById(R.id.product_end_date);
        holder.groupEndDateLayout = (LinearLayout)convertView.findViewById(R.id.product_end_date_layout);
        if (position == 0)
        {
            holder.grayLine.setVisibility(View.GONE);
        }
        
        holder.groupProductName.setText(mItemList.get(position).getName());
        holder.groupOrderedNumber.setText(mItemList.get(position).getOrderNumber());
        holder.groupProductService.setText(mItemList.get(position).getService());
        holder.groupEndDate.setText(mItemList.get(position).getBuyDate());
        ArrayList<String> logoUrls = mItemList.get(position).getLogoUrls();
        if (logoUrls != null && logoUrls.size() > 0)
        {
            if (!StringUtils.isEmpty(logoUrls.get(0)))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + logoUrls.get(0), holder.groupProductLogo);
            }
        }
        if (mItemList.get(position).isEnable())
        {
            int color = mContext.getResources().getColor(R.color.dark_gray);
            holder.groupProductName.setTextColor(color);
        }
        else
        {
            int color = mContext.getResources().getColor(R.color.gray_border);
            holder.groupProductName.setTextColor(color);
        }
        String type = mItemList.get(position).getType();
        if (type != null)
        {
            if (type.equals("2"))
            {
                holder.orderedForUser.setVisibility(View.GONE);
                holder.userPhoto.setVisibility(View.GONE);
                holder.groupEndDateLayout.setVisibility(View.VISIBLE);
                holder.groupEndDate.setVisibility(View.VISIBLE);
                holder.groupProductService.setVisibility(View.GONE);
            }
            else if (type.equals("1"))
            {
                holder.orderedForUser.setVisibility(View.GONE);
                holder.userPhoto.setVisibility(View.GONE);
                holder.groupEndDateLayout.setVisibility(View.GONE);
                holder.groupEndDate.setVisibility(View.GONE);
                holder.groupProductService.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.orderedForUser.setText("");
                holder.orderedForUser.append("为“");
                holder.orderedForUser.append(StringUtils.setStringColor("姚明",
                    mContext.getResources().getColor(R.color.blue_more)));
                holder.orderedForUser.append("”定制");
                holder.orderedForUser.setVisibility(View.VISIBLE);
                holder.userPhoto.setVisibility(View.VISIBLE);
                if (!StringUtils.isEmpty(mItemList.get(position).getAuthorPhotoUrl()))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + mItemList.get(position).getAuthorPhotoUrl(),
                        holder.userPhoto);
                }
                
                holder.groupEndDateLayout.setVisibility(View.GONE);
                holder.groupEndDate.setVisibility(View.GONE);
                holder.groupProductService.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        public RelativeLayout grayLine;
        
        public ImageView groupProductLogo;
        
        public TextView orderedForUser;
        
        public TextView groupProductName;
        
        public TextView groupOrderedNumber;
        
        public TextView groupProductService;
        
        public TextView groupEndDate;
        
        public LinearLayout groupEndDateLayout;
        
        public RoundImageView userPhoto;
    }
}
