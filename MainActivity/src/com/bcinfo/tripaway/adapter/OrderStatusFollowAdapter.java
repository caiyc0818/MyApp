package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.DispatchInfo;
import com.bcinfo.tripaway.bean.TraceInfo;
import com.bcinfo.tripaway.enums.OrderStatusEnum;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderStatusFollowAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<TraceInfo> mTraceList;
	private ArrayList<DispatchInfo> mdispatchInfos;

	public OrderStatusFollowAdapter() {

	}

	public OrderStatusFollowAdapter(Context context,
			ArrayList<TraceInfo> traceList,
			ArrayList<DispatchInfo> dispatchInfos) {
		mContext = context;
		mTraceList = traceList;
		mdispatchInfos = dispatchInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mdispatchInfos == null || mdispatchInfos.size() == 0) {
			return mTraceList.size();
		}
		return mTraceList.size() + mdispatchInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mdispatchInfos == null || mdispatchInfos.size() == 0) {
			return mTraceList.get(position);
		} else {
			if (position < mdispatchInfos.size()) {
				return mdispatchInfos.get(position);
			} else {
				return mTraceList.get(position - mdispatchInfos.size());
			}
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.order_status_item, null);
			holder = new ViewHolder();
			holder.mPointView = (ImageView) convertView
					.findViewById(R.id.kongxin_yuan);
			holder.mTraceStatus = (TextView) convertView
					.findViewById(R.id.trace_status_tv);
			holder.mTraceDesc = (TextView) convertView
					.findViewById(R.id.trace_desc_tv);
			holder.mTraceTime = (TextView) convertView
					.findViewById(R.id.trace_time_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mdispatchInfos == null || mdispatchInfos.size() == 0) {
			TraceInfo info = mTraceList.get(position);

			if (position == 0) {
				holder.mPointView.setImageResource(R.drawable.kongxinyuan);
			} else {
				holder.mPointView.setImageResource(R.drawable.kongxinyuan_gray);
			}

			holder.mTraceStatus.setText(OrderStatusEnum.getFromStatus(info
					.getStatus()));
			holder.mTraceTime.setText(DateUtil.getFormateDate(info
					.getTraceTime()));
			holder.mTraceDesc.setText(info.getDesc());
		} else {
			if (position < mdispatchInfos.size()) {
				DispatchInfo dispatchInfo = mdispatchInfos.get(position);
				if (position == 0) {
					holder.mPointView.setImageResource(R.drawable.kongxinyuan);
				} else {
					holder.mPointView
							.setImageResource(R.drawable.kongxinyuan_gray);
				}
				holder.mTraceDesc.setText("联系电话："
						+ (StringUtils.isEmpty(dispatchInfo.getDispatchTo()
								.getMobile()) ? "" : dispatchInfo
								.getDispatchTo().getMobile()));

				String sex = "";
				if (dispatchInfo.getDispatchTo().getGender().equals("0")) {
					sex = "女";
				} else {
					sex = "男";
				}
				String dis = "";
				if (dispatchInfo.getDispatchTo().getOrgRole().getRoleName()
						.equals("客服")) {
					dis = "已受理";
				} else if (dispatchInfo.getDispatchTo().getOrgRole()
						.getRoleName().equals("导游")
						|| dispatchInfo.getDispatchTo().getOrgRole()
								.getRoleName().equals("司机")
						|| dispatchInfo.getDispatchTo().getOrgRole()
								.getRoleName().equals("领队")) {
					dis = "已接单";
				}
				holder.mTraceStatus.setText(dispatchInfo.getDispatchTo()
						.getRealName()
						+ "("
						+ sex
						+ dispatchInfo.getDispatchTo().getOrgRole()
								.getRoleName() + ")" + dis);
				holder.mTraceTime.setText(DateUtil.getFormateDate(dispatchInfo
						.getCreateTime()));
			} else {

				TraceInfo info = mTraceList.get(position
						- mdispatchInfos.size());

				holder.mPointView.setImageResource(R.drawable.kongxinyuan_gray);

				holder.mTraceStatus.setText(OrderStatusEnum.getFromStatus(info
						.getStatus()));
				holder.mTraceTime.setText(DateUtil.getFormateDate(info
						.getTraceTime()));
				holder.mTraceDesc.setText(info.getDesc());
			}
		}
		return convertView;
	}

	class ViewHolder {

		private ImageView mPointView;

		private TextView mTraceStatus;

		private TextView mTraceDesc;

		private TextView mTraceTime;
	}
}
