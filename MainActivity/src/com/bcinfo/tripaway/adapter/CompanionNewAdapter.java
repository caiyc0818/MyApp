package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompanionNewAdapter extends BaseAdapter {

private static final String TAG = "MyOrderFeeAdapter";
    
    private Context mContext;
    
    private List<PartnerInfo> mItemList;
    
    public CompanionNewAdapter(Context context, List<PartnerInfo> list)
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
	public View getView(int position, View convertView, ViewGroup parent) {
		PartnerInfo partnerInfo = mItemList.get(position);
		ViewHolder holder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.companion_new, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
			holder.tvpassportNum = (TextView) convertView.findViewById(R.id.tvpassportNum);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(StringUtils.isEmpty(partnerInfo.getRealName())?"":partnerInfo.getRealName());
		holder.tvNum.setText(StringUtils.isEmpty(partnerInfo.getIdNo())?"未填写":StringUtils.getIdNo(partnerInfo.getIdNo()));
		holder.tvpassportNum.setText(StringUtils.isEmpty(partnerInfo.getPassportNo())?"未填写":StringUtils.getPassportNo(partnerInfo.getPassportNo()));
		return convertView;
	}

	class ViewHolder{
		TextView tvName;
		TextView tvNum;
		TextView tvpassportNum;
	}
}
