package com.bcinfo.tripaway.view.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.FlightPickedAdapter;
import com.bcinfo.tripaway.adapter.SelectFlightAdapter;
import com.bcinfo.tripaway.bean.FlightInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 航班对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年3月28日 下午5:22:28
 */
public class SelectFlightDialog extends AlertDialog implements OnItemClickListener
{
    private Context mContext;
    private ListView flightListView;
    private List<FlightInfo> mAirDataList = new ArrayList<FlightInfo>();
    private SelectFlightAdapter flightAdapter;
    private SelectFlightListener mSelectFlightListener;
    private View mItemView;
    private int mItemPosition;

    public SelectFlightDialog(Context context, SelectFlightListener listener, View itemView, int position)
    {
        super(context);
        // TODO Auto-generated constructor stub
        //select_flight_list_item  
        mContext = context;
        mSelectFlightListener = listener;
        mItemView = itemView;
        mItemPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_flight_dialog);
        flightListView = (ListView) findViewById(R.id.select_flight_listview);
        flightListView.addHeaderView(getListViewHeader());
        initFlightListView();
    }

    private LinearLayout getListViewHeader()
    {
        LinearLayout header = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.select_flight_list_header,
                null);
        return header;
    }

    private void initFlightListView()
    {
        for (int i = 0; i < 5; i++)
        {
            FlightInfo info = new FlightInfo();
            info.setFlightPicture("http://img.luxtarget.com/cms/www/201405/22141718vswx.gif");
            info.setFlightName("国际航班：中国-美国");
            info.setFlightContent("中国直飞美国，提供多个出发地城市的选择，抵达美国洛杉矶机场");
            info.setAirlinesLogo("http://pic50.nipic.com/file/20141011/2531170_173017019000_2.jpg");
            info.setAirlinesName("美国联合航空CA1848");
            info.setFlightPrice("￥3756");
            info.setFlightTotalTime("约4个小时");
            info.setStartAirportCN("南京禄口机场T2");
            info.setEndAirportCN("洛杉矶机场T2");
            info.setStartAirportEN("NKG");
            info.setEndAirportEN("PEK");
            info.setStartTime("14:00");
            info.setEndTime("18:00");
            mAirDataList.add(info);
        }
        flightAdapter = new SelectFlightAdapter(mContext, mAirDataList);
        flightListView.setAdapter(flightAdapter);
        flightListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        //        Toast.makeText(mContext, "" + position, 0).show();
        this.cancel();
        mSelectFlightListener.selectFlight(mAirDataList.get(position), mItemView, mItemPosition);
    }
    public interface SelectFlightListener
    {
        public void selectFlight(FlightInfo info, View itemView, int position);
    }
}
