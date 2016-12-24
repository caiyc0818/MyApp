package com.bcinfo.tripaway.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.MyAccFas;
import com.bcinfo.tripaway.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的账户-账户列表 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月30日 10:05:23
 */
public class MyAccountfasAdapter extends BaseAdapter {
	private List<MyAccFas> AccountList;
	private Context mContext;
	private LayoutInflater inflator;
	private String data;
	private String time;
	private String time1;
	private String time2;

	public MyAccountfasAdapter(List<MyAccFas> AccountList, Context mContext)

	{
		super();
		this.AccountList = AccountList;
		this.mContext = mContext;
		inflator = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {

		return AccountList.size();
	}

	@Override
	public Object getItem(int position) {

		return AccountList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.cash_item, null);
			holder.cash_type = (TextView) convertView.findViewById(R.id.cash_type);
			holder.all_cash = (TextView) convertView.findViewById(R.id.all_cash);
			holder.buyer = (TextView) convertView.findViewById(R.id.buyer);
			holder.older = (TextView) convertView.findViewById(R.id.older);
			holder.data = (TextView) convertView.findViewById(R.id.data);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyAccFas MyAccFas = AccountList.get(position);
		if (!StringUtils.isEmpty(MyAccFas.getActionType())) {
			if ("0".equals(MyAccFas.getActionType())) {// 定金
				holder.cash_type.setText("订单订金");
			}
			if ("1".equals(MyAccFas.getActionType())) {// 全（尾）款从用户
				holder.cash_type.setText("订单全（尾）款");
			}
			if ("2".equals(MyAccFas.getActionType())) {// 转账
				holder.cash_type.setText("转账");
			}
			if ("3".equals(MyAccFas.getActionType())) {// 退款
				holder.cash_type.setText("退款");
			}
			if ("4".equals(MyAccFas.getActionType())) {// 提现
				holder.cash_type.setText("提现");
			}
			if ("5".equals(MyAccFas.getActionType())) {// 充值
				holder.cash_type.setText("充值");
			}
			if ("6".equals(MyAccFas.getActionType())) {// 充值
				holder.cash_type.setText("交易佣金");
			}

			if (StringUtils.isEmpty(MyAccFas.getBuyer())) {
				holder.buyer.setVisibility(View.GONE);
			} else {
				holder.buyer.setVisibility(View.VISIBLE);
				holder.buyer.setText(MyAccFas.getBuyer());
			}
			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			if ("0".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.red2));
				holder.all_cash.setText("-" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
			}
			if ("1".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.red2));
				holder.all_cash.setText("-" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
			}
			if ("2".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				if ("1".equals(MyAccFas.getDirection())) {
					holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.green2));
					holder.all_cash.setText("+" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
				} else if ("2".equals(MyAccFas.getDirection())) {
					holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.red2));
					holder.all_cash.setText("-" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
				}
			}
			if ("3".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.green2));
				holder.all_cash.setText("+" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
			}
			if ("4".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.red2));
				holder.all_cash.setText("-" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
			}
			if ("5".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.green2));
				holder.all_cash.setText("+" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
			}
			if ("6".equals(MyAccFas.getActionType()) && !StringUtils.isEmpty(MyAccFas.getAmount() + "")) {
				if ("1".equals(MyAccFas.getDirection())) {
					holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.green2));
					holder.all_cash.setText("+" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
				} else if ("2".equals(MyAccFas.getDirection())) {
					holder.all_cash.setTextColor(mContext.getResources().getColor(R.color.red2));
					holder.all_cash.setText("-" + decimalFormat.format(Double.parseDouble(MyAccFas.getAmount())));
				}
			}

			if (StringUtils.isEmpty(MyAccFas.getOrderNo())) {
				holder.older.setText("");
			} else {
				holder.older.setText(MyAccFas.getOrderNo());
			}
			if (!StringUtils.isEmpty(MyAccFas.getRecordTime())) {

				data = MyAccFas.getRecordTime().substring(0, 8);
				time = MyAccFas.getRecordTime().substring(8, 12);
				time1 = time.substring(0, 2);
				time2 = time.substring(2, 4);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 小写的mm表示的是分钟
			java.util.Date date = null;
			try {
				date = sdf.parse(data);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sdf = new SimpleDateFormat("yyyy/MM/dd");
			if (date != null) {
				holder.data.setText(sdf.format(date));
			}
			holder.time.setText(time1 + ":" + time2);

		}
		return convertView;
	}

	private class ViewHolder {
		TextView cash_type;// 类型
		TextView all_cash;// 所有的钱
		TextView buyer;// 购买人
		TextView older;// 订单号
		TextView data;// 添加账号
		TextView time; // 默认显示账号信息
	}
}
