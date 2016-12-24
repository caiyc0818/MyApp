package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 新个人资料-游记adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月30日 10:51:11
 */
public class BlogPagerAdapter extends BaseAdapter
{
    private Context context;
    
    private List<String> blogUrlList;
    
    public List<String> getBlogUrlList()
    {
        return blogUrlList;
    }
    
    public void setBlogUrlList(List<String> blogUrlList)
    {
        this.blogUrlList = blogUrlList;
    }
    
    public BlogPagerAdapter(Context context, List<String> blogUrlList)
    {
        this.context = context;
        this.blogUrlList = blogUrlList;
        System.out.println("list集合长度:" + blogUrlList.size());
    }
    
    @Override
    public int getCount()
    {
        
        return blogUrlList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return blogUrlList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder holder;
        LayoutInflater inflator;
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.layout_item_blog_individual, null);
            holder.blogIv = (ImageView)convertView.findViewById(R.id.layout_item_blog_iv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(blogUrlList.get(position)))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + blogUrlList.get(position),
                holder.blogIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView blogIv;
    }
    
}
