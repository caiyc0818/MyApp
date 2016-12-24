package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DateUtil;

public class InGroupDataDetail2 extends BaseActivity implements OnClickListener
{
    private TextView text;
    
    private List<String> lists;
    
    /**
     * 返回键按钮
     */
    private LinearLayout date_back_button2;
    
    String[] s_day;
    
    /**
     * 标题的月份
     */
    TextView tv_month;
    
    /**
     * 标题的年份
     */
    TextView tv_year;
    
    /**
     * 存放当前月的天数集合
     */
    private List<String> gvList;
    
    /**
     * Date 实例化
     */
    private Date theInDay;
    
    /**
     * 一天的毫秒数
     */
    static long nd = 1000 * 24L * 60L * 60L;
    
    /**
     * 当前的年份值 例如：y=2015
     */
    String y;
    
    /**
     * 当前的月份值 例如：m=9
     */
    String m;
    
    /**
     * 当前的天数值 大于10或小于10号 例如：d=9或10
     */
    String d;
    
    /**
     * 当前的天数值 大于10或小于10号 例如：d=09或10
     */
    String d1;
    
    String[] qq;
    private List<String> lists2;
    
    private ArrayList<String> priceList = new ArrayList<String>();
    
    String time_choose;
    
    private LinearLayout containLayout;
    View dateView;
    private RelativeLayout time_title;
    String aa;
    @Override
    public void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.ingroup_data_detail_activity2);
        time_title=(RelativeLayout) findViewById(R.id.time_title2);
        time_title.getBackground().setAlpha(255);
        initView();
        
        
    }
    
    @Override
    protected void initView()
    {
    	
    	 containLayout=(LinearLayout) findViewById(R.id.contain_ll2);
    	 date_back_button2 = (LinearLayout)findViewById(R.id.date_back_button2);
         date_back_button2.setOnClickListener(this);
    	 
         for (int k = 0; k < 1; k++) {
         	
         	 dateView=LayoutInflater.from(this).inflate(R.layout.date_layout, null);
         	
         	 Intent intent = getIntent();
             lists = intent.getExtras().getStringArrayList("ww");
             priceList = intent.getStringArrayListExtra("priceList");
             if (lists.get(0).equals("没有开始时间数据")) {
     			return;
     		}
             
             s_day = new String[lists.size()];
             for (int i = 0; i < lists.size(); i++)
             {
            	 s_day[i] = lists.get(i);
                 
            	 System.out.println("===开始时间==="+lists.get(i));
             }
            
             
            setTime(s_day[0]);
 	        setTheDay(new Date(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d)));
 	        containLayout.addView(dateView);
        }
        
    }
    
    public void setTime(String date_statr)
    {
        y = date_statr.substring(0, 4).toString();
        
        String m_zero = date_statr.substring(4, 5);
        String d_zero = date_statr.substring(6, 7);
        // 截取月份
        if (m_zero.equals("0"))
        {
            m = date_statr.substring(5, 6).toString();
        }
        else
        {
            m = date_statr.substring(4, 6).toString();
        }
        d1 = date_statr.substring(6, 8).toString();
        // // 截取日
        if (d_zero.equals("0"))
        {
            d = date_statr.substring(7, 8).toString();
        }
        else
        {
            d = date_statr.substring(6, 8).toString();
        }
        
    }
    
    public void setTheDay(Date dateIn)
    {
        this.theInDay = dateIn;
        init();
    }
    
    private void init()
    {
        gvList = new ArrayList<String>();// 存放天数
        tv_month = (TextView)dateView.findViewById(R.id.tv_month);
        tv_year = (TextView)dateView.findViewById(R.id.tv_year);
        
        gvList = new ArrayList<String>();// 存放天
        final Calendar cal = Calendar.getInstance();// 获取日历实例
        cal.setTime(theInDay);// cal设置为当天的
        cal.set(Calendar.DATE, 1);// cal设置当前day为当前月第一天
        
        int s = SpecialCalendar.getDaysOfMonth(SpecialCalendar.isLeapYear(Integer.parseInt(y)), Integer.parseInt(m)); // 获取当前月有多少天;
        int q = SpecialCalendar.getWeekdayOfMonth(Integer.parseInt(y), Integer.parseInt(m));// 获取当前月第一天为星期几
        
        setGvListData(q, s, Integer.parseInt(y) + "-" + Integer.parseInt(m));
        
        tv_year.setText(y);
        tv_month.setText(m);
        
        MyGridView gv = (MyGridView)dateView.findViewById(R.id.gv_calendar);
        calendarGridViewAdapter gridViewAdapter = new calendarGridViewAdapter(InGroupDataDetail2.this, gvList);
        
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        
        gv.setAdapter(gridViewAdapter);
        gv.setOnItemClickListener(new OnItemClickListener(
        
        )
        {
            
            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3)
            {
            	
               String choiceDay = (String)adapterView.getAdapter().getItem(position);
               System.out.println("原始的日期----"+choiceDay);
                if (choiceDay.equals(" , ")) {
					return;
				}
                
              //月
                String choiceDay2 = choiceDay.replaceAll("-", ",");
                String[] ymd = choiceDay2.split(",");
                
                String yday = ymd[0];
                String mday = ymd[1];
                String dday = ymd[2];
                
                if (Integer.parseInt(mday) < 10)
                {
                    mday = "0" + ymd[1];
                }
               
                
                if (Integer.parseInt(dday) < 10)
                {
                	dday = "0" + ymd[2];
                }
                
                System.out.println("/////mday月==="+yday);
                System.out.println("/////mday月==="+mday);
                System.out.println("/////mday日==="+dday);
                
                
                choiceDay=yday+ "-"+mday+ "-"+dday;
                String checkDay = yday+mday+dday;
                String  week = DateUtil.getWeek(choiceDay);
                time_choose =choiceDay+" "+week;
                
                System.out.println(choiceDay);
                System.out.println(time_choose);
 
       boolean flag = false;
       int num = -1;
              for (int i = 0; i < lists.size(); i++) {
            	  if (checkDay.equals(lists.get(i))) {
            		  flag = true;
            		  num = i;
            		  break;
				}
			}
              if(flag){
            		Intent intent = new Intent();
	            	intent.putExtra("result",time_choose);
	            	intent.putExtra("price", priceList.get(num));
	                InGroupDataDetail2.this.setResult(2015, intent);
	                finish();
	                activityAnimationClose();
              }
                	 
//					Intent intent = new Intent();
//	            	intent.putExtra("result",time_choose);
//	                InGroupDataDetail2.this.setResult(2015, intent);
//	                finish();
//	                activityAnimationClose();
				
    					
            }
        });
        
    }
    
    /**
     * 为gridview中添加需要展示的数据
     * 
     * @param tempSum
     * @param dayNumInMonth
     */
    private void setGvListData(int tempSum, int dayNumInMonth, String YM)
    {
        gvList.clear();
        for (int i = 0; i < tempSum; i++)
        {
            gvList.add(" , ");
        }
        for (int j = 1; j <= dayNumInMonth; j++)
        {
            gvList.add(YM + "," + String.valueOf(j));
        }
    }
    
    /**
     * gridview中adapter的viewholder
     * 
     * @author Administrator
     * 
     */
    static class GrideViewHolder
    {
        TextView tvDay;
        
        TextView tvDay2;
        
//        ImageView tv;
        
        LinearLayout ww;
    }
   
    /**
     * gridview的adapter
     * 
     * @author Administrator
     * 
     */
    class calendarGridViewAdapter extends BaseAdapter
    {
        
        List<String> gvList;// 存放天
        
        private final Context mcontext;
        
        public calendarGridViewAdapter(Context context, List<String> gvList)
        {
            super();
            this.gvList = gvList;
            this.mcontext = context;
        }
        
        @Override
        public int getCount()
        {
            return gvList.size();
        }
        
        @Override
        public String getItem(int position)
        {
            return gvList.get(position);
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            GrideViewHolder holder;
            if (convertView == null)
            {
                holder = new GrideViewHolder();
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.common_calendar_gridview_item2, null);
//                holder.tv = (ImageView)convertView.findViewById(R.id.tv_calendar_day);
                holder.tvDay = (TextView)convertView.findViewById(R.id.tv_calendar);
                holder.tvDay2 = (TextView)convertView.findViewById(R.id.tv_calendar2);
                holder.ww = (LinearLayout)convertView.findViewById(R.id.ww);
                
                convertView.setTag(holder);
            }
            else
            {
                holder = (GrideViewHolder)convertView.getTag();
            }
            String[] date = getItem(position).split(",");
            holder.tvDay.setText(date[1]);
            
            if (!date[1].equals(" "))
            {
                String day = date[1];
                String yearMont = date[0];
                if (Integer.parseInt(date[1]) < 10)
                {
                    day = "0" + date[1];
                }
            
            
            
                qq = new String[lists.size()];
                for (int t = 0; t < lists.size(); t++)
                {
                    qq[t] = s_day[t].substring(6, 8);
                    String yearS = s_day[t].substring(0,4)+"-"+Integer.parseInt(s_day[t].substring(4,6));
                    if ((day.equals(d1) || day.equals(qq[t].toString()))&&yearMont.equals(yearS))
                    {
                        holder.tvDay2.setText(priceList.get(t));
//                        holder.tv.setBackgroundResource(R.drawable.cmm);
                        holder.ww.setBackgroundColor(Color.WHITE);
                    }
                }
        
            }
            
            return convertView;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.date_back_button2:
            	
//            	Intent intent = new Intent();
//            	intent.putExtra("result","请选择出发日期");
//                setResult(2015, intent);
                finish();
                activityAnimationClose();
                break;
            
            default:
                break;
        }
        
    }
    
    //物理返回键监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {
   	 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
            //do something...
        	Intent intent = new Intent();
        	intent.putExtra("result","请选择出发日期");
            setResult(2015, intent);
            finish();
            activityAnimationClose();
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
   
}
