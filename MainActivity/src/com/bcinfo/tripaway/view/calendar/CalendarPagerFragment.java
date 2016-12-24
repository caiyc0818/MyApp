package com.bcinfo.tripaway.view.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.LogUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * 日历页
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月5日 上午10:14:21
 */
public class CalendarPagerFragment extends Fragment
{
    public static final String ARG_PAGE = "page";
    private static final String TAG = "CalendarPagerFragment";
    private static int mMonthIndex;
    private static OnFocusChangeListener mOnFocusChangeListener;
    private static OnClickListener mOnClickListener;
    /**
     * ViewPage预加载月份所包含的日期View
     */
    private static ArrayList<HashMap<Integer, ArrayList<View>>> prepareMonthList = new ArrayList<HashMap<Integer, ArrayList<View>>>();

    public static CalendarPagerFragment create(int monthIndex, OnFocusChangeListener focusListener,
            OnClickListener clickListener)
    {
        CalendarPagerFragment fragment = new CalendarPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, monthIndex);
        fragment.setArguments(args);
        mOnFocusChangeListener = focusListener;
        mOnClickListener = clickListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mMonthIndex = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LogUtil.d(TAG, "onCreateView", "mMonthIndex=" + mMonthIndex);
        ArrayList<View> cellViewList = new ArrayList<View>();
        TableRow tableRow;
        View cellView;
        TableLayout tableView = (TableLayout) inflater.inflate(R.layout.view_calendar_table, container, false);
        ScheduleEventDatabase database = ScheduleEventDatabase.getInstance(tableView.getContext());
        CalendarTableCellProvider adpt = new CalendarTableCellProvider(getResources(), mMonthIndex);
        for (int row = 0; row < 6; row++)
        {
            tableRow = new TableRow(tableView.getContext());
            for (int column = 0; column < 7; column++)
            {
                cellView = adpt.getView(row * 7 + column, inflater, tableRow, database);
                //                cellView.setOnFocusChangeListener((View.OnFocusChangeListener) container.getContext());
                cellView.setOnFocusChangeListener(mOnFocusChangeListener);
                cellView.setOnClickListener(mOnClickListener);
                tableRow.addView(cellView);
                cellViewList.add(cellView);
            }
            tableView.addView(tableRow);
        }
        int size = prepareMonthList.size();
        if (size >= 3)
        {
            for (int i = 0; i < prepareMonthList.size(); i++)
            {
                HashMap<Integer, ArrayList<View>> map = prepareMonthList.get(i);
                Iterator<Entry<Integer, ArrayList<View>>> iter = map.entrySet().iterator();
                while (iter.hasNext())
                {
                    Map.Entry<Integer, ArrayList<View>> entry = iter.next();
                    Integer key = entry.getKey();
                    if (Math.abs(mMonthIndex - key) > 2)
                    {
                        prepareMonthList.remove(i);
                        break;
                    }
                }
            }
        }
        HashMap<Integer, ArrayList<View>> map = new HashMap<Integer, ArrayList<View>>();
        map.put(mMonthIndex, cellViewList);
        prepareMonthList.add(map);
        return tableView;
    }

    /**
     * ViewPage预加载月份所包含的日期View
     * @return
     */
    public static ArrayList<HashMap<Integer, ArrayList<View>>> getPrepareMonthList()
    {
        return prepareMonthList;
    }

    public static int getMonthIndex()
    {
        return mMonthIndex;
    }
}
