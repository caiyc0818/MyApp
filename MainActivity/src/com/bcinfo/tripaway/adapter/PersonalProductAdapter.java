package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.bean.ProductNewInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalProductAdapter extends BaseAdapter {
	
private Context mcontext;
    
    private List<ProductNewInfo> productList;
    
    public PersonalProductAdapter(Context mContext, List<ProductNewInfo> productList)
    {
        this.mcontext = mContext;
        this.productList = productList;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator;
        ViewHolder holder = null;
		
		
		
		
		
		
		return convertView;
	}

	 private class ViewHolder
	    {
	        ImageView productIv;// 产品icon
	        
	        TextView productTitleTv;// 产品标题title
	        
	        TextView productTopicTv;// 产品主题
	        
	        TextView productPriceTv;// 产品价格
	        
	       // TextView productDaySchedualTv;// 产品 天数
	        
	       // TextView productDistanceTv;// 产品 里程距离
	        
	        TextView productIntroduceTv;// 产品 介绍 说明
	    }
}
