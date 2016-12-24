package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;

/**
 * 同伴列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月28日 下午5:42:59
 */
public class CompanionListAdapter extends BaseAdapter
{
    private static final String TAG = "MyOrderFeeAdapter";
    
    private Context mContext;
    
    private List<PartnerInfo> mItemList;
    
    public CompanionListAdapter(Context context, List<PartnerInfo> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
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
        PartnerInfo partnerInfo = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.companion_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.companion_name = (TextView)convertView.findViewById(R.id.companion_name);
        holder.companion_ID_card = (TextView)convertView.findViewById(R.id.companion_ID_card);
        holder.companion_name.setText(partnerInfo.getRealName());
        holder.companion_ID_card.setText(partnerInfo.getIdNo());
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 同伴名字
         */
        public TextView companion_name;
        
        /**
         * 身份证号码
         */
        public TextView companion_ID_card;
    }
}
