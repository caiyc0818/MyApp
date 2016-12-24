package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.TypedValue;
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
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 推荐目的地 adapter适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:11:11
 */
public class NewProductPushDestinationsGridAdapter extends BaseAdapter
{
    private List<PushResource> locationList;
    public Context context;
    public NewProductPushDestinationsGridAdapter(Context context, List<PushResource> locationList)
    {
        this.context = context;
        this.locationList = locationList;
        System.out.println("locationList==" + locationList);
    }
    
    @Override
    public int getCount()
    {
        return locationList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return locationList.get(position);
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
        PushResource locationItem = (PushResource)locationList.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflator.inflate(R.layout.new_item_discovery_locations, null);
            holder = new ViewHolder();
            holder.locationFlagIv = (ImageView)convertView.findViewById(R.id.item_location_flag_iv);
            holder.locationenNameTv = (TextView)convertView.findViewById(R.id.item_location_enUSname_tv);
            holder.locationzhNameTv = (TextView)convertView.findViewById(R.id.item_location_zhCNname_tv);
            holder.locationKeywordsTv=(TextView)convertView.findViewById(R.id.item_location_keywords_tv);
            holder.subjectIcon = (ImageView) convertView
					.findViewById(R.id.subject_icon);
           /* AssetManager mgr = context.getAssets();// 得到AssetManager
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
            //holder.locationenNameTv.setTypeface(tf);// 设置字体
            holder.locationzhNameTv.setTypeface(tf);// 设置字体*/            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
      
        	
        	 //finalBitmap.display(holder.locationFlagIv,locationItem.getTitleImage());
      
       /* ImageLoader.getInstance().displayImage(locationItem.getTitleImage(),
            holder.locationFlagIv,
            AppConfig.options(R.drawable.default_photo));*/
        	 if(!locationItem.getSubTitle().equals("null")||locationItem.getSubTitle()!=null||locationItem.getSubTitle().equals("")){
        if (!StringUtils.isEmpty(locationItem.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + locationItem.getTitleImage(),
                holder.locationFlagIv,
                AppConfig.options(R.drawable.default_photo));
        }
        
        holder.locationenNameTv.setText(locationItem.getSubTitle());
        	 }
        holder.locationzhNameTv.setText(locationItem.getResTitle());
        if(locationItem.getKeywords()!=null&&!locationItem.getKeywords().equals("null")&&!locationItem.getKeywords().equals("")){
        String keywords=locationItem.getKeywords().toString();
        if(keywords.contains(",")){
        	keywords=keywords.replace(",","·" );
        	
        }else if(keywords.contains("，")){
        	keywords=keywords.replace("，","·" );
        }
        else if(keywords.contains("·")){
        	keywords=keywords.replace("·"," · " );
        }
        holder.locationKeywordsTv.setText(keywords);
        }
        if (locationItem.getObjectType().equals("destination")) {
        	holder.locationzhNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			holder.subjectIcon.setVisibility(View.GONE);
			holder.locationenNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			holder.locationKeywordsTv.setVisibility(View.VISIBLE);
		} else if (locationItem.getObjectType().equals("subject")) {
			holder.locationzhNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			holder.locationKeywordsTv.setVisibility(View.GONE);
			holder.locationenNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			holder.subjectIcon.setVisibility(View.VISIBLE);
		}      
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView locationFlagIv;// 目的地国旗iv
        
        private TextView locationKeywordsTv;//目的地关键字
        
        private TextView locationzhNameTv;// 目的地中文名tv
        
        private TextView locationenNameTv;// 目的地英文名tv
        
        ImageView subjectIcon;
    }
}
