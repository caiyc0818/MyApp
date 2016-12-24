package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;

public class MyInvoiceAdapter extends BaseAdapter implements OnClickListener{
	
	private Context mContext;
	
	private ArrayList<PartnerInfo> partnerInfoList;
	
	private OperationListener operationListener;
	
	public MyInvoiceAdapter(){
		
	}
	
	public MyInvoiceAdapter(Context context,ArrayList<PartnerInfo> partnerInfoList,OperationListener operationListener){
		mContext = context;
		this.partnerInfoList = partnerInfoList;
		this.operationListener = operationListener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null ;
		PartnerInfo info = partnerInfoList.get(position);
		if(convertView==null){
			convertView= LayoutInflater.from(mContext).inflate(R.layout.invoice_adapter,null);
			viewHolder = new ViewHolder();
			viewHolder.userName=(TextView) convertView.findViewById(R.id.user_name);
			viewHolder.idNumber=(TextView) convertView.findViewById(R.id.id_number);
			viewHolder.passport=(TextView) convertView.findViewById(R.id.passport);
			viewHolder.deleteBtn =(TextView) convertView.findViewById(R.id.button_delete);
			viewHolder.modifyBtn = (TextView) convertView.findViewById(R.id.button_modify);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.userName.setText(info.getRealName());
		viewHolder.idNumber.setText(info.getIdNo());
		viewHolder.passport.setText(info.getPassportNo());
		viewHolder.deleteBtn.setTag(R.id.tag_first,position);
		viewHolder.deleteBtn.setTag(R.id.tag_second,info.getId());
		viewHolder.deleteBtn.setOnClickListener(this);
		viewHolder.modifyBtn.setTag(R.id.tag_second,info);
		viewHolder.modifyBtn.setTag(R.id.tag_first,position);
		viewHolder.modifyBtn.setOnClickListener(this);
		return convertView;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partnerInfoList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partnerInfoList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	class ViewHolder{
		private TextView userName;
		private TextView idNumber;
		private TextView passport;
		private TextView deleteBtn;
		private TextView modifyBtn;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_delete:
			String id = v.getTag(R.id.tag_second).toString();
			int pos = (Integer)v.getTag(R.id.tag_first);
			operationListener.delInfoById(id,pos);
			break;
		case R.id.button_modify:
			int poss = (Integer)v.getTag(R.id.tag_first);
		    PartnerInfo info = (PartnerInfo)v.getTag(R.id.tag_second);
			operationListener.modifyInfo(info,poss);
			break;
		default:
			break;
		}
	}
	
	public interface OperationListener{
		void modifyInfo(PartnerInfo info,int position);
		
		void delInfoById(String id,int position);
	}
}
