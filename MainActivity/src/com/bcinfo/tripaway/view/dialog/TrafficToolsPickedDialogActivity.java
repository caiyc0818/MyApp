package com.bcinfo.tripaway.view.dialog;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;

import android.os.Bundle;

/**
 * 产品-申请预订-选择汽车
 * @function
 * @author caihelin
 * @version 1.0 2015年4月2日  14:18:11
 */
public class TrafficToolsPickedDialogActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.dialog_layout_car_picked);
        float dialogHeight=this.getResources().getDimension(R.dimen.dialog_product_car_picked_window_height);
        this.getWindow().getAttributes().height=(int)dialogHeight;// 设置对话框样式的高度为指定的高度 480dp
        
    }

}
