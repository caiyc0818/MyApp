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
import com.bcinfo.tripaway.bean.Invoice;
import com.bcinfo.tripaway.bean.PartnerInfo;

public class MyInvoice2Adapter extends BaseAdapter implements OnClickListener{
	
	private Context mContext;
	
	private ArrayList<Invoice> invoiceList;
	
	private OperationListener operationListener;
	
	public MyInvoice2Adapter(){
		
	}
	
	public MyInvoice2Adapter(Context context,ArrayList<Invoice> invoiceList,OperationListener operationListener){
		mContext = context;
		this.invoiceList = invoiceList;
		this.operationListener = operationListener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null ;
		Invoice info = invoiceList.get(position);
		if(convertView==null){
			convertView= LayoutInflater.from(mContext).inflate(R.layout.invoice_adapter2,null);
			viewHolder = new ViewHolder();
			viewHolder.invoiceTitle=(TextView) convertView.findViewById(R.id.invoice_title);
			viewHolder.invoiceAddress=(TextView) convertView.findViewById(R.id.invoice_address);
			viewHolder.invoiceTel=(TextView) convertView.findViewById(R.id.invoice_tel);
			viewHolder.deleteBtn =(TextView) convertView.findViewById(R.id.button_delete);
			viewHolder.modifyBtn = (TextView) convertView.findViewById(R.id.button_modify);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.invoiceTitle.setText(info.getInvoiceTitle());
		viewHolder.invoiceAddress.setText(info.getAddress());
		viewHolder.invoiceTel.setText(info.getTel());
		viewHolder.deleteBtn.setTag(R.id.tag_first,position);
		viewHolder.deleteBtn.setTag(R.id.tag_second,info.getInvoiceId());
		viewHolder.deleteBtn.setOnClickListener(this);
		viewHolder.modifyBtn.setTag(R.id.tag_second,info);
		viewHolder.modifyBtn.setTag(R.id.tag_first,position);
		viewHolder.modifyBtn.setOnClickListener(this);
		return convertView;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return invoiceList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return invoiceList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	class ViewHolder{
		private TextView invoiceTitle;
		private TextView invoiceAddress;
		private TextView invoiceTel;
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
			Invoice info = (Invoice)v.getTag(R.id.tag_second);
			operationListener.modifyInfo(info,poss);
			break;
		default:
			break;
		}
	}
	
	public interface OperationListener{
		void modifyInfo(Invoice info,int position);
		
		void delInfoById(String id,int position);
	}
}
