package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.utils.StringUtils;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AssociateAdapter extends BaseAdapter implements OnClickListener {

	Context mContext;
	List<PartnerInfo> partnerInfos;
	private OnDelPartnerItemListener onDelPartnerItemListener;
	
	public AssociateAdapter(Context mContext,List<PartnerInfo> partnerInfos,OnDelPartnerItemListener onDelPartnerItemListener) {
		this.mContext = mContext;
		this.partnerInfos = partnerInfos;
		this.onDelPartnerItemListener = onDelPartnerItemListener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partnerInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partnerInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PartnerInfo partnerInfo = partnerInfos.get(position);
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.associate_item, null);
			holder.associate_name = (TextView) convertView.findViewById(R.id.associate_name);
			holder.associate_id_num = (TextView) convertView.findViewById(R.id.associate_id_num);
			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			holder.papers_num = (TextView) convertView.findViewById(R.id.papers_num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.associate_name.setText(StringUtils.isEmpty(partnerInfo.getRealName())?"":partnerInfo.getRealName());
		holder.associate_id_num.setText(StringUtils.isEmpty(partnerInfo.getIdNo())?"暂未填写":StringUtils.getIdNo(partnerInfo.getIdNo()));
		holder.papers_num.setText(StringUtils.isEmpty(partnerInfo.getPassportNo())?"暂未填写":StringUtils.getPassportNo(partnerInfo.getPassportNo()));
		holder.delete.setTag(position);
		convertView.setOnClickListener(this);
		holder.delete.setOnClickListener(this);
		return convertView;
	}

	class ViewHolder{
		TextView associate_name;
		TextView papers_num;
		ImageView delete;
		TextView associate_id_num;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete:
			int position = (Integer) v.getTag();
			onDelPartnerItemListener.deletePartner(position);
			break;
		default:
			break;
		}
	}
	
	public interface OnDelPartnerItemListener{
    	void deletePartner(int position);
    }
}
