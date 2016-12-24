package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品详情景点照片列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class ScenicSpotsPhotoAdapter extends BaseAdapter
{
    private Context mContext;
    
    private ArrayList<String> mPhotoUrlList;
    
    public ScenicSpotsPhotoAdapter(Context context, ArrayList<String> scenicList)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mPhotoUrlList = scenicList;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mPhotoUrlList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mPhotoUrlList.get(position);
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
            convertView = inflater.inflate(R.layout.scenic_spots_gridview_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.scenic_spots_photo = (ImageView)convertView.findViewById(R.id.scenic_spots_photo);
        if (!StringUtils.isEmpty(mPhotoUrlList.get(position)))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + mPhotoUrlList.get(position),
                holder.scenic_spots_photo);
        }
        
        return convertView;
    }
    
    public class ViewHolder
    {
        public ImageView scenic_spots_photo;
    }
}
