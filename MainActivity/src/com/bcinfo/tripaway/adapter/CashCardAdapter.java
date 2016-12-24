package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.baidu.mobstat.GetReverse;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AffirmListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Cash;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.Cash;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CashCardAdapter extends BaseAdapter {
	public List<Cash> list;
	public Cash tempGridViewItem;
	public LayoutInflater layoutInflater;
	public Context contexts;

	private OnSelectPicListener onSelectPicListener;

	public void setOnSelectPicListener(OnSelectPicListener onSelectPicListener) {
		this.onSelectPicListener = onSelectPicListener;
	}

	public Context getContexts() {
		return contexts;
	}

	public void setContexts(Context contexts) {
		this.contexts = contexts;
	}

	public List<Cash> getList() {
		return list;
	}

	public void setList(List<Cash> list) {
		this.list = list;
	}

	public Cash getTempGridViewItem() {
		return tempGridViewItem;
	}

	public void setTempGridViewItem(Cash tempGridViewItem) {
		this.tempGridViewItem = tempGridViewItem;
	}

	public LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	public void setLayoutInflater(LayoutInflater layoutInflater) {
		this.layoutInflater = layoutInflater;
	}

	public CashCardAdapter(Context context, List<Cash> list) {
		this.list = list;
		contexts = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(contexts).inflate(R.layout.cashcarditem, null);
			holder = new ViewHolder();
			holder.cashMoney = (TextView) convertView.findViewById(R.id.cashMoney);
			holder.cashType = (TextView) convertView.findViewById(R.id.cashType);
			holder.cashClub = (TextView) convertView.findViewById(R.id.cashClub);
			holder.cashCon = (TextView) convertView.findViewById(R.id.cashCon);
			holder.cashDate = (TextView) convertView.findViewById(R.id.cashDate);
			holder.cashPic = (ImageView) convertView.findViewById(R.id.cashPic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		try {

			Cash sy = list.get(position);
			if (sy != null) {

				if ("现金抵用券".equals(sy.getCashType())) {
					holder.cashMoney.setText("￥" + sy.getCashMoney());
					holder.cashType.setText("现金抵用券");
				} else if ("折扣券".equals(sy.getCashType())) {
					holder.cashType.setText("折扣券");
					holder.cashMoney.setText(((Double.parseDouble(sy.getDiscount()) * 10 + "")) + "折");
				}
				holder.cashClub.setText(sy.getCashClub());
				holder.cashCon.setText(sy.getCashCon());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 小写的mm表示的是分钟

				java.util.Date date = null;
				try {
					date = sdf.parse(sy.getCashDate());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				holder.cashDate.setText("有效期至:" + sdf.format(date));
				if (sy.getCashStatus() != null) {
					if ("used".equals(sy.getCashStatus())) {
						holder.cashPic.setImageResource(R.drawable.ysy);
						holder.cashMoney.setTextColor(contexts.getResources().getColor(R.color.red));
					} else if (("expired".equals(sy.getCashStatus()))) {
						holder.cashPic.setImageResource(R.drawable.ygq);
						holder.cashMoney.setTextColor(contexts.getResources().getColor(R.color.gray));
					} else {
						holder.cashPic.setVisibility(View.INVISIBLE);
						holder.cashMoney.setTextColor(contexts.getResources().getColor(R.color.red));
					}
					if (!"valid".equals(sy.getCashStatus())) {
						holder.cashPic.setVisibility(View.VISIBLE);
					} else {
						final Cash ca = sy;
						convertView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// ToastUtil.showToast(contexts,
								// ca.getCouponCode()+"");
							}
						});
					}

				}
			}

		} catch (Exception ce) {

		}
		return convertView;
	}

	public static String getWeekOfDate(Date dt) {
		if (dt == null) {
			return null;
		}
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public class ViewHolder {
		TextView cashMoney;
		TextView cashType;
		TextView cashClub;
		TextView cashCon;
		TextView cashDate;
		ImageView cashPic;
	}

	public interface OnSelectPicListener {
		public void addPic(String id);

		public void removePic(String id);
	}

}
