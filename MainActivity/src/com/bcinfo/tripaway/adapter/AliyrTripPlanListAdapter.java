package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品详情的行程规划预览侧边栏列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class AliyrTripPlanListAdapter extends BaseAdapter
{
    private static final String TAG = "ProductTripPlanListAdapter";

	private  ImageView[] imageViews = null;
    
    private Context mContext;
    private BitmapManager bitmapManager;
    
    private ArrayList<Journey> mItemList = new ArrayList<Journey>();
    
    public AliyrTripPlanListAdapter(Context context, ArrayList<Journey> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
       // bitmapManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
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
        Journey journey = mItemList.get(position);
        
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
        	//trip_detail_item
            convertView = inflater.inflate(R.layout.a_ne_list_item, null);
            holder = new ViewHolder();
            // 详细行程标题
            holder.trip_item_index = (TextView)convertView.findViewById(R.id.trip_item_index);
            holder.trip_item_title = (TextView)convertView.findViewById(R.id.trip_item_title);
            
            if (position==mItemList.size()) {
            	holder.hui = (ImageView)convertView.findViewById(R.id.lin_grey);
            	holder.hui.setVisibility(View.GONE);
            	holder.lan = (ImageView)convertView.findViewById(R.id.lin_grenn);
            	holder.lan.setVisibility(View.GONE);
        	 
		}
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        //holder.trip_item_index.setText("D" + (position + 1));
        holder.trip_item_title.setText(journey.getTitle());// 标题

        
        holder.trip_item_index.setText("第" + (position + 1)+"天");
        
        return convertView;
    }
    
    private ImageView findViewById(int scenicSpotsPhoto) {
		// TODO Auto-generated method stub
		return null;
	}

	public class ViewHolder
    {
        TextView trip_item_index;
        
        TextView trip_item_title;
        
        ImageView  hui;
        
        ImageView  lan;
    }

	public static void main(String[] args) {
		
	}
}
