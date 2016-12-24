package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.AddScheduleEventActivity;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.adapter.EventListAdapter;
import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.calendar.CalendarPagerAdapter;
import com.bcinfo.tripaway.view.calendar.CalendarPagerFragment;
import com.bcinfo.tripaway.view.calendar.LunarCalendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

/**
 * 日程
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月5日 上午10:26:55
 */
public class TripManageFragment extends BaseFragment implements OnDateSetListener, OnMenuItemClickListener,
        OnFocusChangeListener, OnClickListener, OnItemClickListener
{
    private static final String TAG = "TripManageFragment";
    //日历显示ViewPager
    private ViewPager mPager;
    //日历显示ViewPager适配器
    private CalendarPagerAdapter mPagerAdapter;
    private ImageView imgPreviousMonth;
    private ImageView imgNextMonth;
    //公历日期
    private TextView txtTitleGregorian;
    //日程事件标题
    private TextView scheduleEventTitle;
    //事件列表
    private ListView eventListView;
    private ArrayList<ScheduleEvent> scheduleEventList = new ArrayList<ScheduleEvent>();
    //事件列表适配器
    private EventListAdapter eventListAdapter;
    //事件存储数据库
    private ScheduleEventDatabase mDatabase;

    private int getTodayMonthIndex()
    {
        Calendar today = Calendar.getInstance();
        int offset = (today.get(Calendar.YEAR) - LunarCalendar.getMinYear()) * 12 + today.get(Calendar.MONTH);
        return offset;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase = ScheduleEventDatabase.getInstance(getActivity());
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        if (mPagerAdapter != null)
        {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View tripManage = inflater.inflate(R.layout.trip_manage_fragment, container, false);
        imgPreviousMonth = (ImageView) tripManage.findViewById(R.id.imgPreviousMonth);// 上一个月
        imgNextMonth = (ImageView) tripManage.findViewById(R.id.imgNextMonth);// 下一个月
        txtTitleGregorian = (TextView) tripManage.findViewById(R.id.txtTitleGregorian);// 当前的日期
        scheduleEventTitle = (TextView) tripManage.findViewById(R.id.schedule_event_title);
        mPager = (ViewPager) tripManage.findViewById(R.id.pager);
        eventListView = (ListView) tripManage.findViewById(R.id.event_listview);
        imgPreviousMonth.setOnClickListener(this);
        imgNextMonth.setOnClickListener(this);
        mPagerAdapter = new CalendarPagerAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager(), this,
                onCellClickListener);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new simplePageChangeListener());
        
        mPager.setCurrentItem(getTodayMonthIndex());
        if (BaseActivity.screenHeight == 960)
        {
            mPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(
                    R.dimen.calendar_540p_height)));
        }
        else if (BaseActivity.screenHeight == 1280)
        {
            mPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(
                    R.dimen.calendar_720p_height)));
        }
        else
        {
            mPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(
                    R.dimen.calendar_540p_height)));
        }
        initEventListView();
        return tripManage;
    }

    /**
     * 更新日历
     */
    public void updateCalendar()
    {
        ArrayList<View> cellViewList = null;
        ArrayList<HashMap<Integer, ArrayList<View>>> prepareMonthList = CalendarPagerFragment.getPrepareMonthList();
        //        LogUtil.i(TAG, "updateCalendar", "cellViewList.size=" + prepareMonthList.size());
        if (prepareMonthList.size() > 0)
        {
            for (int i = 0; i < prepareMonthList.size(); i++)
            {
                HashMap<Integer, ArrayList<View>> map = prepareMonthList.get(i);
                Iterator<Entry<Integer, ArrayList<View>>> iter = map.entrySet().iterator();
                while (iter.hasNext())
                {
                    Map.Entry<Integer, ArrayList<View>> entry = iter.next();
                    Integer key = entry.getKey();
                    if (Math.abs(CalendarPagerFragment.getMonthIndex() - key) == 1)
                    {
                        cellViewList = entry.getValue();
                    }
                }
            }
            if (cellViewList == null)
            {
                return;
            }
            for (int i = 0; i < cellViewList.size(); i++)
            {
                View cellView = cellViewList.get(i);
                ImageView eventMark = (ImageView) cellView.findViewById(R.id.event_remark);
                LunarCalendar lc = (LunarCalendar) cellView.getTag();
                int year = lc.getGregorianDate(Calendar.YEAR);
                int month = lc.getGregorianDate(Calendar.MONTH) + 1;
                int dayOfMonth = lc.getGregorianDate(Calendar.DAY_OF_MONTH);
                String date = DateUtil.getFormateDate(year, month, dayOfMonth);
                //                LogUtil.i(TAG, "updateCalendar", "date=" + date);
                ArrayList<ScheduleEvent> eventList = mDatabase.queryEventByDate(date);
                setEventColor(eventMark, eventList);
            }
        }
    }

    /**
     * 设置日历事件颜色
     * @param eventMark
     * @param eventList
     */
    private void setEventColor(ImageView eventMark, ArrayList<ScheduleEvent> eventList)
    {
        //        LogUtil.i(TAG, "setEventColor", "eventMark=" + eventMark);
        //        LogUtil.i(TAG, "setEventColor", "eventList.size=" + eventList.size());
        if (eventList != null && eventList.size() > 0)
        {
            for (int i = 0; i < eventList.size(); i++)
            {
                ScheduleEvent event = eventList.get(i);
                if (event.getColor().equals("green"))
                {
                    eventMark.setBackgroundColor(getResources().getColor(R.color.event_green));
                    eventMark.setVisibility(View.VISIBLE);
                    return;
                }
            }
            for (int i = 0; i < eventList.size(); i++)
            {
                ScheduleEvent event = eventList.get(i);
                if (event.getColor().equals("gray"))
                {
                    eventMark.setBackgroundColor(getResources().getColor(R.color.event_gray));
                    eventMark.setVisibility(View.VISIBLE);
                    return;
                }
            }
            eventMark.setBackgroundColor(getResources().getColor(R.color.event_red));
            eventMark.setVisibility(View.VISIBLE);
        }
        else
        {
            eventMark.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 日期单元格点击事件
     */
    OnClickListener onCellClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if (v.getTag() == null)
            {
                return;
            }
            LogUtil.i(TAG, "initEventListView", "v.getTag=" + v.getTag().toString());
        }
    };

    /**
     * 日期对话框选择完成事件
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        int offset = (year - LunarCalendar.getMinYear()) * 12 + monthOfYear;
        mPager.setCurrentItem(offset);
    }

    /**
     * 日期单元格焦点变化事件
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            LunarCalendar lc = (LunarCalendar) v.getTag();
            //            CharSequence[] info = formatter.getFullDateInfo(lc);
            int year = lc.getGregorianDate(Calendar.YEAR);
            int month = lc.getGregorianDate(Calendar.MONTH) + 1;
            int dayOfMonth = lc.getGregorianDate(Calendar.DAY_OF_MONTH);
            String event = month + "月" + dayOfMonth + "日" + "日程活动";
            scheduleEventTitle.setText(event);
            LinearLayout markToday = (LinearLayout) v.findViewById(R.id.mark_today);
            TextView txtCellGregorian = (TextView) v.findViewById(R.id.txtCellGregorian);
            TextView txtCellLunar = (TextView) v.findViewById(R.id.txtCellLunar);
            markToday.setBackgroundResource(R.drawable.date_selected);
            txtCellGregorian.setTextColor(getActivity().getResources().getColor(R.color.white));
            txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.white));
            String date = DateUtil.getFormateDate(year, month, dayOfMonth);
            ArrayList<ScheduleEvent> eventList = mDatabase.queryEventByDate(date);
            updateEventListView(eventList);
        }
        else
        {
            LinearLayout markToday = (LinearLayout) v.findViewById(R.id.mark_today);
            TextView txtCellGregorian = (TextView) v.findViewById(R.id.txtCellGregorian);
            TextView txtCellLunar = (TextView) v.findViewById(R.id.txtCellLunar);
            if (markToday.getTag() == null)
            {
                markToday.setBackgroundResource(R.drawable.transparent);
                if(txtCellGregorian.getTag() == null)
                {
                    txtCellGregorian.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
                    if(txtCellLunar.getTag() == null)
                    {
                        txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.gray));
                    }
                    else
                    {
                        if(txtCellLunar.getTag().equals("isFestival"))
                        {
                            txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.color_calendar_festival));
                        }
                        else
                        {
                            txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.color_calendar_solarterm));
                        }
                    }
                }
                else
                {
                    txtCellGregorian.setTextColor(getActivity().getResources().getColor(R.color.gray_little_d));
                    txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.gray_little_d));
                }
            }
            else
            {
                markToday.setBackgroundResource(R.drawable.img_hint_today);
                txtCellGregorian.setTextColor(getActivity().getResources().getColor(R.color.white));
                txtCellLunar.setTextColor(getActivity().getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.imgPreviousMonth:
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                break;
            case R.id.imgNextMonth:
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                break;
        }
    }
    /**
     * 月份显示切换事件
     * @function
     * @author     JiangCS  
     * @version   1.0, 2014年12月16日 下午5:44:36
     */
    private class simplePageChangeListener extends ViewPager.SimpleOnPageChangeListener
    {
        @Override
        public void onPageSelected(int position)
        {
            // set title year month
            StringBuilder title = new StringBuilder();
            title.append(LunarCalendar.getMinYear() + (position / 12));
            title.append('-');
            int month = (position % 12) + 1;
            if (month < 10)
            {
                title.append('0');
            }
            title.append(month);
            txtTitleGregorian.setText(title);
            // set related button's state
            if (position < mPagerAdapter.getCount() - 1 && !imgNextMonth.isEnabled())
            {
                imgNextMonth.setEnabled(true);
            }
            if (position > 0 && !imgPreviousMonth.isEnabled())
            {
                imgPreviousMonth.setEnabled(true);
            }
        }
    }

    /**
     * 初始化事件列表
     * @param eventList
     */
    private void initEventListView()
    {
        Calendar calendar = Calendar.getInstance();
        ArrayList<ScheduleEvent> eventList = mDatabase.queryEventByDate(DateUtil.getFormateDate(calendar));
        //        LogUtil.i(TAG, "initEventListView", "eventList.size=" + eventList.size());
        scheduleEventList.addAll(eventList);
        eventListAdapter = new EventListAdapter(getActivity(), scheduleEventList, mDatabase);
        eventListView.setAdapter(eventListAdapter);
        int listViewHeigt = getResources().getDimensionPixelSize(R.dimen.calendar_event_item_height)
                * scheduleEventList.size();
        eventListView.setOnItemClickListener(this);
        eventListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, listViewHeigt));
        if (scheduleEventList.size() == 0)
        {
            scheduleEventTitle.setVisibility(View.GONE);
        }
        else
        {
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String event = month + "月" + day + "日" + "日程活动";
            scheduleEventTitle.setText(event);
            scheduleEventTitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新事件列表
     * @param eventList
     */
    private void updateEventListView(ArrayList<ScheduleEvent> eventList)
    {
        //        LogUtil.i(TAG, "updateEventListView", "eventList.size=" + eventList.size());
        scheduleEventList.clear();
        scheduleEventList.addAll(eventList);
        int listViewHeigt = getResources().getDimensionPixelSize(R.dimen.calendar_event_item_height)
                * scheduleEventList.size();
        eventListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, listViewHeigt));
        eventListAdapter.notifyDataSetChanged();
        if (scheduleEventList.size() == 0)
        {
            scheduleEventTitle.setVisibility(View.GONE);
        }
        else
        {
            scheduleEventTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        ScheduleEvent event = scheduleEventList.get(position);
        Intent intent = new Intent(getActivity(), AddScheduleEventActivity.class);
        intent.putExtra("isUpdate", true);
        intent.putExtra("scheduleEvent", event);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
