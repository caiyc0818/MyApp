package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;

import android.os.Bundle;
/**
 * 汽车信息详情
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月27日 下午4:46:17
 */
public class CarInfoActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.car_info_activity);
        setSecondTitle("车辆信息");
    }
}
