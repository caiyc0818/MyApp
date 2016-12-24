package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Cust;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Cust> custList;
	
	public CustAdapter(){
		
	}
	
	public CustAdapter( Context mContext,List<Cust> custList){
		this.mContext = mContext;
		this.custList = custList;
	}
	@Override
	public int getCount() {
		return custList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return custList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Cust cust = custList.get(position);
		if(null == convertView){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.cust_item, null);
			holder = new ViewHolder();
			holder.imageBackground = (ImageView) convertView.findViewById(R.id.cust_background);
			holder.mCustTitle = (TextView) convertView.findViewById(R.id.cust_title);
			holder.mCustLines = (TextView) convertView.findViewById(R.id.cust_lineNum);
			holder.mCustProName = (TextView) convertView.findViewById(R.id.cust_product_title);
			holder.mCustProImg = (ImageView) convertView.findViewById(R.id.cust_product_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.imageBackground.setImageResource(R.drawable.summer);
		if(!StringUtils.isEmpty(cust.getCover())){
			ImageLoader.getInstance().displayImage(Urls.imgHost+cust.getCover(), holder.imageBackground,AppConfig.options(R.drawable.summer));
		}
		holder.mCustTitle.setText(cust.getCustTitle());
		if(StringUtils.isEmpty(cust.getLineNum())||Double.parseDouble(cust.getLineNum()) == 0){
			holder.mCustLines.setText("");
		}else{
			holder.mCustLines.setText(cust.getLineNum()+"条线路");
		}
		holder.mCustProName.setText(cust.getProduct().getTitle());
		holder.mCustProImg.setImageResource(R.drawable.summer);
		if(!StringUtils.isEmpty(cust.getProduct().getTitleImg())){
			ImageLoader.getInstance().displayImage(Urls.imgHost+cust.getProduct().getTitleImg(), holder.mCustProImg,AppConfig.options(R.drawable.summer));
		}
		((View)(holder.mCustProName.getParent())).setTag(position);
		((View)(holder.mCustProName.getParent())).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position=(Integer)v.getTag();
				Cust cust = custList.get(position);
				cust.getProduct().setProductType("team");
				ActivityUtil.toProductHomePage(cust.getProduct(), mContext);
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		
		private ImageView imageBackground;
		
		private TextView mCustTitle;
		
		private TextView mCustLines;
		
		private TextView mCustProName;
		
		private ImageView mCustProImg;
	}

}
