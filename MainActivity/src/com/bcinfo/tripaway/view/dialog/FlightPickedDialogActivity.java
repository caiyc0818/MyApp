package com.bcinfo.tripaway.view.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.adapter.FlightPickedAdapter;

/**
 * 产品-申请预订-选择航班
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月2日 14:10:11
 */
public class FlightPickedDialogActivity extends BaseActivity implements OnItemClickListener
{
    private ListView flightListView;
    
    private List<Map<String, Object>> mapList;
    
    private ScrollView sc;
    
    public List<Map<String, Object>> getMapList()
    {
        return mapList;
    }
    
    public void setMapList(List<Map<String, Object>> mapList)
    {
        this.mapList = mapList;
    }
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.dialog_layout_flight_picked);
        flightListView = (ListView)findViewById(R.id.flight_picked_listview);
        sc = (ScrollView)findViewById(R.id.scrollView_flight_container);
        // sc.requestChildFocus(flightListView, flightListView);
        float windowsHeight = this.getResources().getDimension(R.dimen.dialog_product_car_picked_window_height);
        this.getWindow().getAttributes().height = (int)windowsHeight;
        initList();
        FlightPickedAdapter flightAdapter = new FlightPickedAdapter(this, mapList);
        flightListView.setAdapter(flightAdapter);
        flightListView.setOnItemClickListener(this);
        setInitialHeight(flightListView);
        sc.post(new Runnable()
        {
            
            @Override
            public void run()
            {
                sc.scrollTo(0, 0);
                
            }
        });
    }
    
    private void initList()
    {
        mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapItem1 = new HashMap<String, Object>();
        mapItem1.put("flightUrl",
            "http://pic1.ooopic.com/uploadfilepic/shiliang/2009-06-06/OOOPIC_gaojie181818_200906066934816f04063f95.jpg");// 航空公司图标
        mapItem1.put("flightName", "美国联合航空CA1848");// flight name
        mapItem1.put("schedualTime", "约4个小时");
        mapItem1.put("flightPrice", "3756");
        mapItem1.put("airPortDeparturezhName", "南京禄口机场T2");
        mapItem1.put("airPortDepartureEnName", "NKG");
        mapItem1.put("departureTime", "14:00");
        mapItem1.put("airPortDestinatezhName", "洛杉矶机场T2");
        mapItem1.put("airPortDestinateEnName", "PEK");
        mapItem1.put("destinateTime", "18:00");
        mapItem1.put("flightIsDay", false);// 航程时间是否超过1天
        mapList.add(mapItem1);
        Map<String, Object> mapItem2 = new HashMap<String, Object>();
        mapItem2.put("flightUrl", "http://sucai.bhcode.net/UploadFile/sucaiku_image/2009-03-19/pic10.jpg");// 航空公司图标
        mapItem2.put("flightName", "中国国际航空CA1504");// flight name
        mapItem2.put("schedualTime", "约29个小时");
        mapItem2.put("flightPrice", "4380");
        mapItem2.put("airPortDeparturezhName", "南京禄口机场T1");
        mapItem2.put("airPortDepartureEnName", "NKG");
        mapItem2.put("departureTime", "20:40");
        mapItem2.put("airPortDestinatezhName", "杜勒斯国际机场");
        mapItem2.put("airPortDestinateEnName", "PEK");
        mapItem2.put("destinateTime", "14:30");
        mapItem2.put("flightIsDay", true);// 航程时间是否超过1天
        mapList.add(mapItem2);
        Map<String, Object> mapItem3 = new HashMap<String, Object>();
        mapItem3.put("flightUrl", "http://sucai.bhcode.net/UploadFile/sucaiku_image/2009-03-19/pic10.jpg");// 航空公司图标
        mapItem3.put("flightName", "中国国际航空CA1504");// flight name
        mapItem3.put("schedualTime", "约29个小时");
        mapItem3.put("flightPrice", "4380");
        mapItem3.put("airPortDeparturezhName", "南京禄口机场T1");
        mapItem3.put("airPortDepartureEnName", "NKG");
        mapItem3.put("departureTime", "20:40");
        mapItem3.put("airPortDestinatezhName", "杜勒斯国际机场");
        mapItem3.put("airPortDestinateEnName", "PEK");
        mapItem3.put("destinateTime", "14:30");
        mapItem3.put("flightIsDay", true);// 航程时间是否超过1天
        mapList.add(mapItem3);
        Map<String, Object> mapItem4 = new HashMap<String, Object>();
        mapItem4.put("flightUrl", "http://sucai.bhcode.net/UploadFile/sucaiku_image/2009-03-19/pic10.jpg");// 航空公司图标
        mapItem4.put("flightName", "中国国际航空CA1504");// flight name
        mapItem4.put("schedualTime", "约29个小时");
        mapItem4.put("flightPrice", "4380");
        mapItem4.put("airPortDeparturezhName", "南京禄口机场T1");
        mapItem4.put("airPortDepartureEnName", "NKG");
        mapItem4.put("departureTime", "20:40");
        mapItem4.put("airPortDestinatezhName", "杜勒斯国际机场");
        mapItem4.put("airPortDestinateEnName", "PEK");
        mapItem4.put("destinateTime", "14:30");
        mapItem4.put("flightIsDay", true);// 航程时间是否超过1天
        mapList.add(mapItem4);
        
    }
    
    /**
     * 航班列表的真实高度
     */
    private void setInitialHeight(ListView listView)
    {
        if (listView != null)
        {
            ListAdapter adapter = listView.getAdapter();
            int count = adapter.getCount();
            int totalHeight = 0;
            for (int i = 0; i < count; i++)
            {
                View viewItem = adapter.getView(i, null, listView);
                viewItem.measure(0, 0);
                totalHeight = totalHeight + viewItem.getMeasuredHeight();
            }
            totalHeight = totalHeight + listView.getDividerHeight() * (count - 1);
            listView.getLayoutParams().height = totalHeight;
            
        }
        else
        {
            return;
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        Toast.makeText(this, "" + position, 0).show();
        
    }
}
