package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 展示主题 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:23:22
 */
public class ProductThemesGridAdapter extends BaseAdapter
{
    private List<PushResource> topicList;
    
    public Context context;
    
    public ProductThemesGridAdapter(Context context, List<PushResource> topicList)
    {
        this.context = context;
        this.topicList = topicList;
    }
    
    @Override
    public int getCount()
    {
        
        return topicList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return topicList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflator = LayoutInflater.from(context);
        PushResource topicItem = topicList.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_discovery_theme_list, null);
            holder.themeIconIv = (ImageView)convertView.findViewById(R.id.item_discovery_theme_iv);
            holder.themeNameTv = (TextView)convertView.findViewById(R.id.item_discovery_theme_tv);
            holder.item_discovery_theme_desctv= (TextView)convertView.findViewById(R.id.item_discovery_theme_desctv);
            holder.subjectIcon = (ImageView) convertView
					.findViewById(R.id.subject_icon);
            /*AssetManager mgr = context.getAssets();// 得到AssetManager
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
            holder.themeNameTv.setTypeface(tf);// 设置字体
*/            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.themeIconIv.setImageResource(R.drawable.ic_launcher);
        if (!StringUtils.isEmpty(topicItem.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + topicItem.getTitleImage(),
                holder.themeIconIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
       if (topicItem.getObjectType().equals("subject")) {
			holder.themeNameTv.setText(topicItem.getResTitle());
			holder.themeNameTv.setGravity(Gravity.CENTER);
			holder.item_discovery_theme_desctv.setGravity(Gravity.CENTER);
			holder.subjectIcon.setVisibility(View.VISIBLE);
		}else{
			holder.themeNameTv.setText(topicItem.getResTitle());
			holder.subjectIcon.setVisibility(View.GONE);
			holder.themeNameTv.setGravity(Gravity.LEFT);
			holder.item_discovery_theme_desctv.setGravity(Gravity.LEFT);
		}
		if(!StringUtils.isEmpty(topicItem.getSubTitle())&&!topicItem.getSubTitle().equals("null"))
		holder.item_discovery_theme_desctv.setText(topicItem.getSubTitle());
        
        return convertView;
    }
    
    class ViewHolder
    {
        ImageView themeIconIv;// 主题iv
        
        TextView themeNameTv;// 主题名称tv
        
        TextView item_discovery_theme_desctv;// 主题desctv
        
        ImageView subjectIcon;
        
    }
    
}
