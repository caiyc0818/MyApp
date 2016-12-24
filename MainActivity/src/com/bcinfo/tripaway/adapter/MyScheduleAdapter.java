package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.MySchedule;
import com.bcinfo.tripaway.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyScheduleAdapter extends BaseAdapter {
	public List<MySchedule> list;
	    public MySchedule tempGridViewItem;
	    public LayoutInflater layoutInflater;
	    public Context contexts;
		
		public Context getContexts() {
			return contexts;
		}
		public void setContexts(Context contexts) {
			this.contexts = contexts;
		}
		public List<MySchedule> getList() {
			return list;
		}
		public void setList(List<MySchedule> list) {
			this.list = list;
		}
		public MySchedule getTempGridViewItem() {
			return tempGridViewItem;
		}
		public void setTempGridViewItem(MySchedule tempGridViewItem) {
			this.tempGridViewItem = tempGridViewItem;
		}
		public LayoutInflater getLayoutInflater() {
			return layoutInflater;
		}
		public void setLayoutInflater(LayoutInflater layoutInflater) {
			this.layoutInflater = layoutInflater;
		}
		public MyScheduleAdapter(Context context,List<MySchedule> list)
		{
			this.list = list;
			contexts=context;
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
			 View view = null;
			 view=LayoutInflater.from(contexts).inflate(R.layout.my_schedulet_item, null);
			 RelativeLayout la=(RelativeLayout) view.findViewById(R.id.relly);
			TextView title=(TextView) view.findViewById(R.id.title);
			TextView beginTime=(TextView) view.findViewById(R.id.beginTime);
			ImageView type=(ImageView) view.findViewById(R.id.type);
			MySchedule sy=list.get(position);
			if(sy!=null)
			{
				title.setText(StringUtils.isEmptyReturn(sy.getTitle()));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟  
			
				java.util.Date date=null;
				try {
					date = sdf.parse(sy.getBeginDate());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				sdf=new SimpleDateFormat("yyyy/MM/dd");
				if(date!=null)
				{
					beginTime.setText("出行时间:"+sdf.format(date)+" "+getWeekOfDate(date));	
				}
				
				if("end".equals(sy.getType()))
				{
					type.setBackgroundResource(R.drawable.ycx);
				}else if ("wait".equals(sy.getType()))
				{

					type.setBackgroundResource(R.drawable.wcx);
				}else if("process".equals(sy.getType()))
				{

					type.setBackgroundResource(R.drawable.cx);
					
				}
				if(position==list.size()-1)
				{
					la.setBackgroundResource(R.drawable.really2);
				}
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

}
