package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ServiceContentListAdapter extends BaseAdapter
{
    private static final String TAG = "ServiceContentListAdapter";
    
    private Context mContext;
    
    private List<ProductServiceResource> productServiceResources;
    
    public ServiceContentListAdapter(Context context, List<ProductServiceResource> productServiceResources)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.productServiceResources = productServiceResources;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return productServiceResources.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return productServiceResources.get(position);
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
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.service_content_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.photo = (ImageView)convertView.findViewById(R.id.content_photo);
        holder.name = (TextView)convertView.findViewById(R.id.content_name);
        holder.keywords = (TextView)convertView.findViewById(R.id.content_keyword);
        if (!StringUtils.isEmpty(productServiceResources.get(position).getTitleImage()))
        {
            ImageLoader.getInstance()
                .displayImage(Urls.imgHost + productServiceResources.get(position).getTitleImage(),
                    holder.photo,
                    AppConfig.options(R.drawable.default_photo));
        }
        
        holder.name.setText(productServiceResources.get(position).getServName());
        holder.keywords.setVisibility(View.GONE);
        //holder.keywords.setText(productServiceResources.get(position).getServDesc());
        return convertView;
    }
    
    public class ViewHolder
    {
        public ImageView photo;
        
        public TextView name;
        
        public TextView keywords;
    }
}
