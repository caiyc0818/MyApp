package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AffirmListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TripStorySchema;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OldPicAdapter extends BaseAdapter {
	public List<TripStorySchema> list;
	    public TripStorySchema tempGridViewItem;
	    public LayoutInflater layoutInflater;
	    public Context contexts;
	    
	    private OnSelectPicListener onSelectPicListener;
		
		public void setOnSelectPicListener(OnSelectPicListener onSelectPicListener) {
			this.onSelectPicListener = onSelectPicListener;
		}
		public Context getContexts() {
			return contexts;
		}
		public void setContexts(Context contexts) {
			this.contexts = contexts;
		}
		public List<TripStorySchema> getList() {
			return list;
		}
		public void setList(List<TripStorySchema> list) {
			this.list = list;
		}
		public TripStorySchema getTempGridViewItem() {
			return tempGridViewItem;
		}
		public void setTempGridViewItem(TripStorySchema tempGridViewItem) {
			this.tempGridViewItem = tempGridViewItem;
		}
		public LayoutInflater getLayoutInflater() {
			return layoutInflater;
		}
		public void setLayoutInflater(LayoutInflater layoutInflater) {
			this.layoutInflater = layoutInflater;
		}
		public OldPicAdapter(Context context,List<TripStorySchema> list)
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
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    ViewHolder holder = null;
	        if (convertView == null)
	        {
	            convertView = LayoutInflater.from(contexts).inflate(R.layout.squeroldshaitu, null);
	            holder = new ViewHolder();
	            convertView.setTag(holder);
	        }
	        else
	        {
	            holder = (ViewHolder)convertView.getTag();
	        }
			try {
				
				 
				 
				holder.title=(TextView) convertView.findViewById(R.id.oldTitle);
				holder.beginTime=(TextView) convertView.findViewById(R.id.oldTime);
				holder.oldPic=(ImageView) convertView.findViewById(R.id.oldPic);
				holder.check=(CheckBox) convertView.findViewById(R.id.check);
				holder.picNum=(TextView) convertView.findViewById(R.id.tv_picnum);
				holder.check.setTag(position);
				TripStorySchema sy=list.get(position);
				if(sy!=null)
				{
					if(!StringUtils.isEmpty(sy.getDescription())){
						List<RichText> richTexts = StringUtils.xmlToRichText(sy
								.getDescription());
						StringUtils.initRichTextView1(holder.title, richTexts);
					}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");//小写的mm表示的是分钟  
			
				java.util.Date date=null;
				try {
					date = sdf.parse(sy.getPublishTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				sdf=new SimpleDateFormat("yyyy/MM/dd");
				if(date!=null)
				{
					holder.beginTime.setText("发布于"+sdf.format(date));	
				}
				if(sy.getPicture()!=null&&sy.getPicture().size()>0){
					ImageInfo imageInfo=sy.getPicture().get(0);
						if(!StringUtils.isEmpty(imageInfo.getUrl())){
							ImageLoader.getInstance().displayImage(
									Urls.imgHost + imageInfo.getUrl(),
									holder.oldPic,
									AppConfig.options(R.drawable.ic_launcher));
						}
				}	
				holder.check.setChecked(sy.isCheck());
				holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						int position=(int)buttonView.getTag();
					 String id=	list.get(position).getPhotoId();
						if(isChecked){
						onSelectPicListener.addPic(id);	
						}else{
							onSelectPicListener.removePic(id);	
						}
						list.get(position).setCheck(isChecked);
					}
				});	
				if(sy.getPicCount()<2){
					((View)holder.picNum.getParent()).setVisibility(View.GONE);
				}
				else{
					((View)holder.picNum.getParent()).setVisibility(View.VISIBLE);
				holder.picNum.setText(Integer.toString(sy.getPicCount()));
				}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        return convertView;
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
		  
		  public class ViewHolder
		    {
			  TextView title;
			  TextView beginTime;
			  ImageView oldPic;
			  CheckBox check;
			  TextView picNum;
		    }
		  
		  public interface OnSelectPicListener{
			  public void addPic(String id);
			  
			  public void removePic(String id);
		  }

}
