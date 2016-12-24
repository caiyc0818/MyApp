package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AliyrTripPlanListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.bcinfo.tripaway.view.MyListView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyTripPlanDetailParentAdapter extends BaseAdapter {
	
	private static final String TAG = "MyTripPlanDetailParentAdapter";

    private Context mContext;
    
    private List<Object> childList=new ArrayList<Object>();
    private List<Object> list = null;
    
    private ArrayList<Journey> mItemList = new ArrayList<Journey>();
    //private ArrayList<HashMap<String, Vector<ProductServiceResource>>> allAttractionsList;
    public MyTripPlanDetailParentAdapter(Context context, ArrayList<Journey> list)
    {
        this.mContext = context;
        this.mItemList = list;
        //this.allAttractionsList = allAttractionsList;
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
	public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Journey journey = mItemList.get(position);
        
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
        	//trip_detail_item
            convertView = inflater.inflate(R.layout.trip_plan_detail_parent_listview_item, null);
            holder = new ViewHolder();
            // 详细行程标题
            holder.scenicIndex = (TextView)convertView.findViewById(R.id.scenic_index);
            holder.scenicName1 = (TextView)convertView.findViewById(R.id.scenic_name1);
            holder.textabstract = (TextView)convertView.findViewById(R.id.text_abstract);
            holder.scenic_detail_parent_listview = (MyListView) convertView
					.findViewById(R.id.trip_plan_detail_parent_listview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        //holder.trip_item_index.setText("D" + (position + 1));
        holder.scenicName1.setText(journey.getTitle());// 标题

      
        String desc=journey.getDescription();
        SpannableStringBuilder style=new SpannableStringBuilder(desc); 
        if(!StringUtils.isEmpty(desc)){
        	int start=desc.indexOf("住宿：");
        	if(start!=-1){
        		 style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA105")),start,start+3,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
        		 style.setSpan(new StyleSpan(Typeface.BOLD),start,start+3,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 	
        	}
        	start=desc.indexOf("用餐：");
        	if(start!=-1){
        		 style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA105")),start,start+3,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
        		 style.setSpan(new StyleSpan(Typeface.BOLD),start,start+3,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 	
        	}
        }
        holder.textabstract.setText(style);
        holder.scenicIndex.setText((position + 1)+"");
        
       
		/**
		 * 引入子listview的adapter
		 */
        //System.out.println("******************************"+allAttractionsList.size());
    	  if(mItemList.get(position).getAttractionAllInfoList()!=null){
         TripPlanDetailNewListChildAdapter daAdapter = new TripPlanDetailNewListChildAdapter(mContext);
        //System.out.println("******************************"+allAttractionsList.toString());
       
        daAdapter.addAll(mItemList.get(position).getAttractionAllInfoList());
       
        holder.scenic_detail_parent_listview.setAdapter(daAdapter);
	}
        return convertView;
    }
	
	public class ViewHolder
    {
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
         * 景点下标，标题，描述 子listview
         */
        public MyListView scenic_detail_parent_listview;
    }

}
