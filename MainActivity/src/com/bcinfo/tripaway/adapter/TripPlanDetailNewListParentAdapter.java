package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.TripPlanDetailNewListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.view.MyListView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripPlanDetailNewListParentAdapter extends BaseAdapter {
	
	
	
	private static final String TAG = "TripPlanDetailNewListAdapter";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private Context mContext;
    private Journey  jouser;
    
    private List<Object> mItemList;
	
	public TripPlanDetailNewListParentAdapter(Context context, List<Object> list,Journey mJourney )
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
        this.jouser=mJourney;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Object resource = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null){
        	convertView = inflater.inflate(R.layout.trip_plan_detail_parent_listview_item, null);
            holder = new ViewHolder();
            holder.scenicIndex = (TextView)convertView.findViewById(R.id.scenic_index);
            holder.scenicName1 = (TextView)convertView.findViewById(R.id.scenic_name1);  //toubu 
            holder.textabstract = (TextView)convertView.findViewById(R.id.text_abstract);
            holder.scenic_detail_parent_listview = (MyListView) convertView
					.findViewById(R.id.trip_plan_detail_parent_listview);
            convertView.setTag(holder);
            
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.scenicIndex.setText(((ProductServiceResource) resource).getRank());
        holder.scenicName1.setText(jouser.getTitle());//  //biaoti
        holder.textabstract.setText(((ProductServiceResource) resource).getServDesc());// miaosu 
		/**
		 * 引入子listview的adapter
		 */
       // final TripPlanDetailNewListChildAdapter daAdapter = new TripPlanDetailNewListChildAdapter(mContext, mItemList);
        //daAdapter.addAll( mItemList);
        //holder.scenic_detail_parent_listview.setAdapter(daAdapter);
		
		
		
		
		
		return convertView;
	}

	public class ViewHolder {
		/**
         * 行程描述
         */
        public TextView textabstract;
        /**
         * 行程下标
         */
        public TextView scenicIndex;
        /**
         * 行程标题
         */
		public TextView scenicName1;
		/**
         * 景点下标，标题，描述
         */
        public MyListView scenic_detail_parent_listview;
	}
}
