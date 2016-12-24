package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 已选择航班列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月17日 下午2:56:53
 */
public class AirListViewAdapter
{
    private static final String TAG = "AirListViewAdapter";
    
    private Context mContext;
    
    private List<ServResrouce> mAirDataList;
    
    private OnFlightOperationListener mListener;
    
    private LinearLayout mListView;
    
    public AirListViewAdapter(Context context, List<ServResrouce> airDataList, LinearLayout listView)
    {
        mContext = context;
        mAirDataList = airDataList;
        mListView = listView;
        showListView();
    }
    
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mAirDataList.size();
    }
    
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mAirDataList.get(position);
    }
    
    private void showListView()
    {
        if (mListView != null)
        {
            mListView.removeAllViews();
            for (int i = 0; i < mAirDataList.size(); i++)
            {
                setItemView(null, i);
            }
        }
    }
    
    public void notifyDataSetChanged()
    {
        LogUtil.i(TAG, "notifyDataSetChanged", " notifyDataSetChanged");
        if (mListView != null)
        {
            LogUtil.i(TAG, "notifyDataSetChanged", " mListView.getChildCount()=" + mListView.getChildCount());
            ArrayList<View> itemViewList = new ArrayList<View>();
            for (int i = 0; i < mListView.getChildCount(); i++)
            {
                View view = mListView.getChildAt(i);
                itemViewList.add(view);
            }
            mListView.removeAllViews();
            LogUtil.i(TAG, "notifyDataSetChanged", " itemViewList.size()=" + itemViewList.size());
            for (int i = 0; i < mAirDataList.size(); i++)
            {
                setItemView(null, i);
            }
        }
    }
    
    private void setItemView(View flightView, int position)
    {
        ServResrouce info = mAirDataList.get(position);
        ViewHolder holder = null;
        if (flightView == null)
        {
            holder = new ViewHolder();
            flightView = LayoutInflater.from(mContext).inflate(R.layout.flight_listview_item, null);
            // 未选中航班
            holder.layout_common_traffic = (LinearLayout)flightView.findViewById(R.id.layout_common_traffic);
            holder.ck = (CheckBox)flightView.findViewById(R.id.comfire_checkbox);
            holder.iv = (ImageView)flightView.findViewById(R.id.comfire_item_imageview);
            holder.title = (TextView)flightView.findViewById(R.id.comfire_item_title);
            holder.content = (TextView)flightView.findViewById(R.id.comfire_item_content);
            // 选择航班
            holder.layout_plane_traffic = (LinearLayout)flightView.findViewById(R.id.layout_plane_traffic);
            holder.airlinesLogo = (ImageView)flightView.findViewById(R.id.flight_agent_icon_iv);
            holder.airlinesName = (TextView)flightView.findViewById(R.id.flight_agent_name_tv);
            holder.flight_price_tv = (TextView)flightView.findViewById(R.id.flight_price_tv);
            holder.flight_time_tv = (TextView)flightView.findViewById(R.id.flight_time_tv);
            holder.startAirportNameCN = (TextView)flightView.findViewById(R.id.flight_airport_departure_zhCNName);
            holder.endAirportNameCN = (TextView)flightView.findViewById(R.id.flight_airport_destination_zhCNName);
            holder.startAirportNameEN = (TextView)flightView.findViewById(R.id.flight_airport_departure_enUSName);
            holder.endAirportNameEN = (TextView)flightView.findViewById(R.id.flight_airport_destination_enUSName);
            holder.flightStartTime = (TextView)flightView.findViewById(R.id.flight_departure_startTime);
            holder.flightEndTime = (TextView)flightView.findViewById(R.id.flight_destination_endTime);
            holder.deleteOrderFlight = (ImageView)flightView.findViewById(R.id.dellect_order_plane);
            flightView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)flightView.getTag();
        }
        holder.ck.setChecked(info.isChecked());
        holder.airlinesLogo.setImageResource(R.drawable.ic_launcher);
        if (info.isChecked())
        {
            holder.layout_common_traffic.setVisibility(View.VISIBLE);
            holder.deleteOrderFlight.setVisibility(View.VISIBLE);
            holder.layout_plane_traffic.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(info.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + info.getTitleImage(), holder.airlinesLogo);
            }
            
            // holder.airlinesName.setText(info.getAirlinesName());
            // holder.flight_price_tv.setText(info.getFlightPrice());
            // holder.flight_time_tv.setText(info.getStartTime());
            // holder.startAirportNameCN.setText(info.getStartAirportCN());
            // holder.endAirportNameCN.setText(info.getEndAirportCN());
            // holder.startAirportNameEN.setText(info.getStartAirportEN());
            // holder.endAirportNameEN.setText(info.getEndAirportEN());
            // holder.flightStartTime.setText(info.getStartTime());
            // holder.flightEndTime.setText(info.getEndTime());
            holder.deleteOrderFlight.setOnClickListener(new DelectFlightListener(info));
        }
        else
        {
            holder.deleteOrderFlight.setVisibility(View.GONE);
            holder.layout_plane_traffic.setVisibility(View.GONE);
            holder.layout_common_traffic.setVisibility(View.VISIBLE);
            holder.title.setText(info.getServName());
            holder.content.setText(info.getServDesc());
            if (!StringUtils.isEmpty(info.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + info.getTitleImage(), holder.iv);
            }
            
        }
        if (mListView != null)
        {
            flightView.setOnClickListener(new FlightItemClickListener(info, position));
            mListView.addView(flightView);
        }
    }
    
    public void setOnFlightOperationListener(OnFlightOperationListener listener)
    {
        mListener = listener;
    }
    
    public class ViewHolder
    {
        LinearLayout layout_common_traffic;
        
        ImageView iv;
        
        LinearLayout comfireLayout;
        
        CheckBox ck;
        
        TextView title;
        
        TextView content;
        
        TextView typeTV;
        
        TextView useTime;// 使用时间
        
        ImageView deleteIv;// 清除信息按钮
        
        // 航班部分
        LinearLayout layout_plane_traffic;
        
        ImageView airlinesLogo;
        
        TextView airlinesName;
        
        TextView flight_price_tv;
        
        TextView flight_time_tv;
        
        TextView startAirportNameCN;
        
        TextView endAirportNameCN;
        
        TextView startAirportNameEN;
        
        TextView endAirportNameEN;
        
        TextView flightStartTime;
        
        TextView flightEndTime;
        
        ImageView deleteOrderFlight;
    }
    
    /**
     * 删除航班监听
     * 
     * @function
     */
    class DelectFlightListener implements OnClickListener
    {
        ServResrouce info;
        
        public DelectFlightListener(ServResrouce info)
        {
            this.info = info;
        }
        
        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onDelectChoise(info);
            }
        }
    }
    
    /**
     * 点击列表Item监听
     * 
     * @function
     */
    class FlightItemClickListener implements OnClickListener
    {
        private int position = 0;
        
        private ServResrouce flightInfo;
        
        public FlightItemClickListener(ServResrouce info, int position)
        {
            this.position = position;
            flightInfo = info;
        }
        
        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onItemClick(v, flightInfo, position);
            }
        }
    }
    
    public interface OnFlightOperationListener
    {
        public void onItemClick(View itemView, ServResrouce info, int position);
        
        public void onDelectChoise(ServResrouce info);
    }
}
