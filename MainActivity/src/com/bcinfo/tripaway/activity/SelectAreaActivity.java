package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaAdapter;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;

/**
 * @author hanweipeng
 * @date 2015-7-14 上午10:00:17
 */
public class SelectAreaActivity extends BaseActivity implements OnItemClickListener
{
    private TextView currentAreaTxt;
    
    private TextView countryTxt;
    
    private TextView cityTxt;
    
    private MyListView areaLst;
    
    private List<AreaInfo> areaList = new ArrayList<AreaInfo>();
    
    private AreaAdapter adapter;
    
    private String selectCity = "";
    
    private String haveSelectArea;
    
    private RelativeLayout havaSelectLayout;
    
    private LocationClient mLocationClient = null;
    
    private LinearLayout locationLayout;
    
    private BDLocationListener myListener = new MyLocationListener();
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.select_area_activity);
        initLocation();
        initView();
    }
    
    private String countryStr = "";
    
    private String cityStr = "";
    
    protected void initView()
    {
        setSecondTitle("选择城市");
        haveSelectArea = getIntent().getStringExtra("areaAddress");
        if (!StringUtils.isEmpty(haveSelectArea))
        {
            if (haveSelectArea.indexOf(" ") > 0)
            {
                countryStr = haveSelectArea.substring(0, haveSelectArea.indexOf(" "));
                cityStr = haveSelectArea.substring(haveSelectArea.indexOf(" "));
            }
            else
            {
                countryStr = haveSelectArea;
                cityStr = "已选地区";
            }
        }
        locationLayout = (LinearLayout)findViewById(R.id.location_layout);
        havaSelectLayout = (RelativeLayout)findViewById(R.id.hava_select);
        currentAreaTxt = (TextView)findViewById(R.id.current_area_txt);
        countryTxt = (TextView)findViewById(R.id.country_txt);
        countryTxt.setText(countryStr);
        cityTxt = (TextView)findViewById(R.id.city_txt);
        cityTxt.setText(cityStr);
        areaLst = (MyListView)findViewById(R.id.area_listview);
        areaList = SqliteDBHelper.getHelper().getAreaListByPid("0");
        adapter = new AreaAdapter(SelectAreaActivity.this, areaList);
        areaLst.setAdapter(adapter);
        areaLst.setOnItemClickListener(this);
        if (StringUtils.isEmpty(countryStr) && StringUtils.isEmpty(cityStr))
        {
            havaSelectLayout.setVisibility(View.GONE);
        }
        havaSelectLayout.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (StringUtils.isEmpty(countryStr))
                {
                    return;
                }
                else
                {
                    AreaInfo info = SqliteDBHelper.getHelper().getAreaInfoByName(countryStr);
                    ArrayList<AreaInfo> newAreaList = SqliteDBHelper.getHelper().getAreaListByPid(info.getAreaId());
                    if (newAreaList == null || newAreaList.size() == 0)
                    {
                        Intent intent = new Intent();
                        intent.putExtra("area", info.getAreaName());
                        setResult(RESULT_OK, intent);
                        finish();
                        activityAnimationClose();
                    }
                    else
                    {
                        selectCity = info.getAreaName();
                        Intent intent = new Intent(SelectAreaActivity.this, AreaListActivity.class);
                        intent.putParcelableArrayListExtra("areaList", newAreaList);
                        startActivityForResult(intent, 101);
                        activityAnimationOpen();
                    }
                }
            }
        });
        areaLst.setFocusable(false);
        areaLst.setFocusableInTouchMode(false);
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 101 && arg2 != null)
        {
            String areaString = selectCity + " " + arg2.getStringExtra("area");
            Intent intent = new Intent();
            intent.putExtra("area", areaString);
            setResult(RESULT_OK, intent);
            finish();
            activityAnimationClose();
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        ArrayList<AreaInfo> newAreaList =
            SqliteDBHelper.getHelper().getAreaListByPid(areaList.get(position).getAreaId());
        if (newAreaList == null || newAreaList.size() == 0)
        {
            Intent intent = new Intent();
            intent.putExtra("area", areaList.get(position).getAreaName());
            setResult(RESULT_OK, intent);
            finish();
            activityAnimationClose();
        }
        else
        {
            selectCity = areaList.get(position).getAreaName();
            Intent intent = new Intent(SelectAreaActivity.this, AreaListActivity.class);
            intent.putParcelableArrayListExtra("areaList", newAreaList);
            startActivityForResult(intent, 101);
            activityAnimationOpen();
        }
    }
    
    private void initLocation()
    {
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    
    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            // 定位结束，关闭定位
            String province = "";
            if (location.getProvince().contains("省"))
            {
                province = location.getProvince().substring(0, location.getProvince().length() - 1);
            }
            else
            {
                province = location.getProvince();
            }
            String city = "";
            if (location.getCity().contains("市"))
            {
                city = location.getCity().substring(0, location.getCity().length() - 1);
            }
            else
            {
                city = location.getCity();
            }
            currentAreaTxt.setText(location.getCountry() + " " + province + " " + city);
            mLocationClient.stop();
            locationLayout.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    String locationArea = currentAreaTxt.getText().toString();
                    if (!StringUtils.isEmpty(locationArea))
                    {
                        if (locationArea.indexOf(" ") > 0)
                        {
                            countryStr = locationArea.substring(0, locationArea.indexOf(" "));
                            cityStr = locationArea.substring(locationArea.indexOf(" "));
                        }
                        else
                        {
                            countryStr = locationArea;
                            cityStr = "已选地区";
                        }
                        countryTxt.setText(countryStr);
                        cityTxt.setText(cityStr);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("area", locationArea);
                    setResult(RESULT_OK, intent);
                    finish();
                    activityAnimationClose();
                }
            });
        }
    }
}
