package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * 景点图文列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class ScenicDetailListAdapter extends BaseAdapter
{
    private static final String TAG = "ScenicDetailListAdapter";
    
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    
    private Context mContext;
    
    
    /**
     * key: 图片 value: 图片描述
     */
    private List<ImageInfo> mItemList = new ArrayList<ImageInfo>();
    
    public ScenicDetailListAdapter(Context context, List<ImageInfo> list)
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ImageInfo imageInfo = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scenic_detail_listitem, null);
            holder.scenic_detail_photo = (ImageView)convertView.findViewById(R.id.scenic_detail_photo);
            holder.scenic_detail_description = (TextView)convertView.findViewById(R.id.scenic_detail_description);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.scenic_detail_photo.setImageResource(R.drawable.ic_launcher);
        ImageSize imageSize = new ImageSize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ImageLoader.getInstance().displayImage(Urls.imgHost + imageInfo.getUrl(),holder.scenic_detail_photo );
        
        /*
         * ImageLoader.getInstance().displayImage(imageInfo.getUrl(), holder.scenic_detail_photo,
         * AppConfig.options(R.drawable.ic_launcher));
         */
        if (!StringUtils.isEmpty(imageInfo.getDesc()))
        {
            holder.scenic_detail_description.setVisibility(View.VISIBLE);
            holder.scenic_detail_description.setText(imageInfo.getDesc());
        }
        else
        {
            holder.scenic_detail_description.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 景点照片说明
         */
        public TextView scenic_detail_description;
        
        /**
         * 景点照片
         */
        public ImageView scenic_detail_photo;
    }
}
