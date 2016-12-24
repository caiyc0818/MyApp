package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;

/**
 * @author hanweipeng
 * @date 2015-7-9 下午5:11:29
 */
public class PartnerMemberAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<PartnerInfo> partnerInfos;
    
    private LayoutInflater inflator;
    
    private OnDelPartnerItemListener onDelPartnerItemListener;
    
    public PartnerMemberAdapter(Context mContext, List<PartnerInfo> partnerInfos,OnDelPartnerItemListener onDelPartnerItemListener)
    {
        this.mContext = mContext;
        this.partnerInfos = partnerInfos;
        inflator = LayoutInflater.from(mContext);
        this.onDelPartnerItemListener = onDelPartnerItemListener;
    }
    
    private void setPartnerInfoList(List<PartnerInfo> partnerInfos)
    {
        this.partnerInfos = partnerInfos;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return partnerInfos.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return partnerInfos.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.member_item, null);
            holder.memberName = (TextView)convertView.findViewById(R.id.member_name);
            holder.memberNumber = (TextView)convertView.findViewById(R.id.member_number);
            holder.memberDel = (ImageView) convertView.findViewById(R.id.del_member);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.memberName.setText(partnerInfos.get(position).getRealName());
        holder.memberNumber.setText(partnerInfos.get(position).getIdNo());
        final int pos = position;
        holder.memberDel.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDelPartnerItemListener.deletePartner(pos);
			}
		});
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView memberName;
        
        private TextView memberNumber;
        
        private ImageView memberDel;
        
    }
    
    public interface OnDelPartnerItemListener{
    	void deletePartner(int position);
    }
}
