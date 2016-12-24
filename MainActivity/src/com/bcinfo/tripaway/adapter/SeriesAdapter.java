package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Series;
import com.bcinfo.tripaway.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("NewApi")
public class SeriesAdapter extends BaseAdapter {
	public List<Series> list;
	    public Series tempGridViewItem;
	    public LayoutInflater layoutInflater;
	    public Context contexts;
	    private OnSelectSerialListener onSelectSerialListener;
		
		public Context getContexts() {
			return contexts;
		}
		public void setContexts(Context contexts) {
			this.contexts = contexts;
		}
		public List<Series> getList() {
			return list;
		}
		public void setList(List<Series> list) {
			this.list = list;
		}
		public Series getTempGridViewItem() {
			return tempGridViewItem;
		}
		public void setTempGridViewItem(Series tempGridViewItem) {
			this.tempGridViewItem = tempGridViewItem;
		}
		public LayoutInflater getLayoutInflater() {
			return layoutInflater;
		}
		public void setLayoutInflater(LayoutInflater layoutInflater) {
			this.layoutInflater = layoutInflater;
		}
		public SeriesAdapter(Context context,List<Series> list,OnSelectSerialListener onSelectSerialListener)
		{
			this.list = list;
			contexts=context;
			this.onSelectSerialListener=onSelectSerialListener;
	        layoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 View view=LayoutInflater.from(contexts).inflate(R.layout.squeradditem, null);
			try {
				
				 
				
				TextView title=(TextView) view.findViewById(R.id.seriesTitle);
				RadioButton check=(RadioButton) view.findViewById(R.id.check);
				//ImageView type=(ImageView) view.findViewById(R.id.oldPic);
				Series sy=list.get(position);
				check.setTag(position);
				if(sy!=null)
				{
					title.setText(StringUtils.isEmptyReturn(sy.getTitle()));
				
					check.setChecked(sy.isCheck());
					check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							int position=(int)buttonView.getTag();
						 String id=	list.get(position).getId();
							if(isChecked){
								onSelectSerialListener.setSerial(id);	
								list.get(position).setCheck(isChecked);
								for(Series sy:list ){
									if(!sy.getId().equals(id)&&sy.isCheck()){
										sy.setCheck(false);
									}
								}
								notifyDataSetChanged();
							}
						}
					});	
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        return view;
		}
		
		  public static String getWeekOfDate(Date dt) {
			  if(dt==null)
			  {
				  return null;
			  }
		        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		        Calendar cal = Calendar.getInstance();
		        cal.setTime(dt);

		        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		        if (w < 0)
		            w = 0;

		        return weekDays[w];
		    }
		  
		  public interface OnSelectSerialListener{
			  public void setSerial(String id);
			  
		  }

}
