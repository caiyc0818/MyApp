package com.bcinfo.tripaway.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;

/**
 * 【出发日历】的界面
 * 
 * @author cuimingmin
 *
 */
public class InGroupDataDetail extends BaseActivity implements OnClickListener {
	private TextView text;

	private List<String> lists;

	/**
	 * 返回键按钮
	 */
	private LinearLayout date_back_button;
	/**
	 * 解析的开始时间的数据集合
	 */
	// private String[] s;

	private RelativeLayout time_title;

	/**
	 * 一天的毫秒数
	 */
	static long nd = 1000 * 24L * 60L * 60L;

	/**
	 * 当前的天数值 大于10或小于10号 例如：d=09或10
	 */
	private String d1;
	/**
	 * 选择的日期，例如：8
	 */
	private String[] qq;
	/**
	 * 结束时间的数据集合
	 */
	private List<String> lists2;

	private LinearLayout containLayout;
	// View dateView;
	// private List<String> lists3;
	String[] ss;

	private Map<String, String> dateToPrice = new HashMap<String, String>();

	private ArrayList<String> priceList = new ArrayList<String>();

	private Set<String> dateTypes = new TreeSet<String>();

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.ingroup_data_detail_activity);
		time_title = (RelativeLayout) findViewById(R.id.time_title);
		time_title.getBackground().setAlpha(255);
		initView();

	}

	@Override
	protected void initView() {
		containLayout = (LinearLayout) findViewById(R.id.contain_ll);
		date_back_button = (LinearLayout) findViewById(R.id.date_back_button);
		date_back_button.setOnClickListener(this);

		Intent intent = getIntent();
		lists = intent.getExtras().getStringArrayList("tour_dates");
		priceList = intent.getStringArrayListExtra("priceList");
		if (lists.get(0).equals("没有开始时间数据")) {
			System.out.println(lists.get(0));
			return;
		}

		// lists3 = new ArrayList<String>();
		// s = new String[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			// s[i] = lists.get(i);
			// lists3.add(s[i].substring(0, 6));
			dateToPrice.put(lists.get(i), priceList.get(i));
			dateTypes.add(lists.get(i).substring(0, 6));
		}

		Iterator<String> it = dateTypes.iterator();

		while (it.hasNext()) {
			String date = it.next();
			View dateView = LayoutInflater.from(this).inflate(R.layout.date_layout, null);

			init(dateView, date);
			containLayout.addView(dateView);

		}

	}

	private void init(View view, String date) {
		List<String> gvList = new ArrayList<String>();// 存放天数
		TextView tv_month = (TextView) view.findViewById(R.id.tv_month);
		TextView tv_year = (TextView) view.findViewById(R.id.tv_year);

		final Calendar cal = Calendar.getInstance();// 获取日历实例
		DateFormat format = new SimpleDateFormat("yyyyMM");
		try {
			cal.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		} // cal设置为当天的
		cal.set(Calendar.DATE, 1);// cal设置当前day为当前月第一天

		// 当月多少天
		int s = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 当月第一天星期几
		int q = cal.get(Calendar.DAY_OF_WEEK) - 1;

		// 给gridview 设置 adapter 数据
		setGvListData(q, s, date, gvList);

		tv_year.setText(date.substring(0, 4));
		tv_month.setText(date.substring(4, 6));

		MyGridView gv = (MyGridView) view.findViewById(R.id.gv_calendar);
		calendarGridViewAdapter gridViewAdapter = new calendarGridViewAdapter(InGroupDataDetail.this, gvList);

		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));

		gv.setAdapter(gridViewAdapter);
		gv.setOnItemClickListener(new OnItemClickListener(

		) {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {

				String choiceDay = (String) adapterView.getAdapter().getItem(position);
				if (choiceDay.equals(" , ")) {
					return;
				}

				// 月
				String[] ymd = choiceDay.split(",");

				String dday = ymd[1];

				if (Integer.parseInt(dday) < 10) {
					dday = "0" + ymd[1];
				}
				String checkDay = ymd[0] + dday;
				boolean flag = false;
				for (int i = 0; i < lists.size(); i++) {
					if (checkDay.equals(lists.get(i))) {
						flag = true;
						break;
					}
				}
				if (flag) {
					Intent it = new Intent(InGroupDataDetail.this, ConfirmPayActivity3.class);
					it.putExtra("result", ymd[0].substring(0, 4) + "-" + ymd[0].substring(4, 6) + "-" + dday);
					it.putExtra("priceByDate", dateToPrice.get(checkDay));
					setResult(2015, it);
					finish();
					activityAnimationClose();
				}

			}
		});

	}

	/**
	 * 为gridview中添加需要展示的数据
	 * 
	 * @param tempSum
	 * @param dayNumInMonth
	 */
	private void setGvListData(int tempSum, int dayNumInMonth, String YM, List<String> gvList) {
		for (int i = 0; i < tempSum; i++) {
			gvList.add(" , ");
		}
		for (int j = 1; j <= dayNumInMonth; j++) {
			gvList.add(YM + "," + String.valueOf(j));
		}
	}

	/**
	 * gridview中adapter的viewholder
	 * 
	 * @author Administrator
	 * 
	 */
	static class GrideViewHolder {
		TextView tvDay;

		TextView tvDay2;

		// ImageView tv;

		LinearLayout ww;
	}

	/**
	 * gridview的adapter
	 * 
	 * @author Administrator
	 * 
	 */
	class calendarGridViewAdapter extends BaseAdapter {

		List<String> gvList;// 存放天

		private final Context mcontext;

		public calendarGridViewAdapter(Context context, List<String> gvList) {
			super();
			this.gvList = gvList;
			this.mcontext = context;
		}

		@Override
		public int getCount() {
			return gvList.size();
		}

		@Override
		public String getItem(int position) {
			return gvList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GrideViewHolder holder;
			if (convertView == null) {
				holder = new GrideViewHolder();
				convertView = LayoutInflater.from(mcontext).inflate(R.layout.common_calendar_gridview_item2, null);
				// holder.tv =
				// (ImageView)convertView.findViewById(R.id.tv_calendar_day);
				holder.tvDay = (TextView) convertView.findViewById(R.id.tv_calendar);
				holder.tvDay2 = (TextView) convertView.findViewById(R.id.tv_calendar2);
				holder.ww = (LinearLayout) convertView.findViewById(R.id.ww);

				convertView.setTag(holder);
			} else {
				holder = (GrideViewHolder) convertView.getTag();
			}
			String[] date = getItem(position).split(",");
			holder.tvDay.setText(date[1]);

			if (!StringUtils.isEmpty(date[1])) {
				String day = date[1];
				String yearMont = date[0];
				if (Integer.parseInt(date[1]) < 10) {
					day = "0" + date[1];
				}

				for (int t = 0; t < lists.size(); t++) {
					String date1 = lists.get(t);
					String yearS = yearMont + day;
					if (date1.equals(yearS)) {
						holder.tvDay2.setText("￥" + dateToPrice.get(date1));
						// holder.tv.setBackgroundResource(R.drawable.cmm);
						holder.ww.setBackgroundColor(Color.WHITE);
					}
				}

			}

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_back_button:
			finish();
			activityAnimationClose();
			break;

		default:
			break;
		}

	}

}
