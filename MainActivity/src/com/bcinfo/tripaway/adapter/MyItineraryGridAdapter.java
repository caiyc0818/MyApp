package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的行程单列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月23日 下午2:20:09
 */
public class MyItineraryGridAdapter extends BaseAdapter
{
    protected static final String TAG = "MyItineraryGridAdapter";
    
    private Context mContext;
    
    private ArrayList<HashMap<String, String>> mItemList;
    
    public MyItineraryGridAdapter(Context context, ArrayList<HashMap<String, String>> list)
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
        HashMap<String, String> map = mItemList.get(position);
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.my_itinerary_lv_item, null);
            holder.product_photo = (ImageView)convertView.findViewById(R.id.product_photo);
            holder.leave_time = (TextView)convertView.findViewById(R.id.leave_time);
            holder.product_title = (TextView)convertView.findViewById(R.id.product_title);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        String photoUrl = map.get("product_photo");
        if (!StringUtils.isEmpty(photoUrl))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + photoUrl, holder.product_photo);
        }
        
        holder.leave_time.setText(DateUtil.formateDateToTimeStr(map.get("leave_time")) + "出发");
        holder.product_title.setText(map.get("product_title"));
        return convertView;
    }
    
    public class ViewHolder
    {
        public ImageView product_photo;
        
        public TextView leave_time;
        
        public TextView product_title;
    }
}
