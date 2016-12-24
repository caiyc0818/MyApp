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

import com.bcinfo.tripaway.AppConfig;
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
public class ComfireOrderAdapter
{
    private static final String TAG = "AirListViewAdapter";
    
    private Context mContext;
    
    private List<ServResrouce> mAirDataList;
    
    private OnComfireOperationListener mListener;
    
    private LinearLayout mListView;
    
    public ComfireOrderAdapter(Context context, List<ServResrouce> airDataList, LinearLayout listView)
    {
        mContext = context;
        mAirDataList = airDataList;
        mListView = listView;
        updateListView();
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
    
    private void updateListView()
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
    
    private void setItemView(View view, int position)
    {
        ServResrouce info = mAirDataList.get(position);
        LogUtil.i(TAG, "setItemView", "info=" + info);
        ViewHolder holder = null;
        if (view == null)
        {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.comfireorder_item, null);
            holder.isSelect = (CheckBox)view.findViewById(R.id.comfire_checkbox);
            holder.title = (TextView)view.findViewById(R.id.comfire_item_title);
            holder.content = (TextView)view.findViewById(R.id.comfire_item_content);
            holder.photo = (ImageView)view.findViewById(R.id.comfire_item_imageview);
            holder.typeTV = (TextView)view.findViewById(R.id.comfire_item_flag);
            holder.layoutUseTime = (LinearLayout)view.findViewById(R.id.comfire_selected_Lin);
            holder.useTime = (TextView)view.findViewById(R.id.comfire_item_time);
            holder.deleteIv = (ImageView)view.findViewById(R.id.comfire_del);
            holder.bootom_line = (ImageView)view.findViewById(R.id.bootom_line);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.deleteIv.setOnClickListener(new DelectComfireListener(info));
        holder.isSelect.setChecked(info.isChecked());
        holder.title.setText(info.getServName());
        holder.content.setText(info.getServDesc());
        holder.photo.setImageResource(R.drawable.ic_launcher);
        if (!StringUtils.isEmpty(info.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + info.getTitleImage(),
                holder.photo,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        if (info.getServType().equals("car"))
        {
            holder.typeTV.setText("汽车");
            holder.typeTV.setBackgroundResource(R.drawable.bg_car);
        }
        else if (info.getServType().equals("boat"))
        {
            holder.typeTV.setText("游轮");
            holder.typeTV.setBackgroundResource(R.drawable.bg_ship);
        }
        else if (info.getServType().equals("hotel"))
        {
            holder.typeTV.setText("酒店");
            holder.typeTV.setBackgroundResource(R.drawable.bg_hotel);
        }
        else if (info.getServType().equals("house"))
        {
            holder.typeTV.setText("民宿");
            holder.typeTV.setBackgroundResource(R.drawable.bg_sushe);
        }
        if (info.isChecked())
        {
            holder.layoutUseTime.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.layoutUseTime.setVisibility(View.GONE);
        }
        if (mAirDataList.size() > 1 && position != mAirDataList.size() - 1)
        {
            holder.bootom_line.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.bootom_line.setVisibility(View.GONE);
        }
        if (mListView != null)
        {
            view.setOnClickListener(new ComfireItemClickListener(info, position));
            mListView.addView(view);
        }
    }
    
    public void setOnComfireOperationListener(OnComfireOperationListener listener)
    {
        mListener = listener;
    }
    
    public class ViewHolder
    {
        ImageView photo;
        
        CheckBox isSelect;
        
        TextView title;
        
        TextView content;
        
        TextView typeTV;
        
        LinearLayout layoutUseTime;
        
        TextView useTime;// 使用时间
        
        ImageView deleteIv;// 清除信息按钮
        
        ImageView bootom_line;
    }
    
    /**
     * 删除监听
     * 
     * @function
     */
    class DelectComfireListener implements OnClickListener
    {
        ServResrouce info;
        
        public DelectComfireListener(ServResrouce info)
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
    class ComfireItemClickListener implements OnClickListener
    {
        private int position = 0;
        
        private ServResrouce comfireInfo;
        
        public ComfireItemClickListener(ServResrouce info, int position)
        {
            this.position = position;
            comfireInfo = info;
        }
        
        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onItemClick(v, comfireInfo, position);
            }
        }
    }
    
    public interface OnComfireOperationListener
    {
        public void onItemClick(View itemView, ServResrouce info, int position);
        
        public void onDelectChoise(ServResrouce info);
    }
}
