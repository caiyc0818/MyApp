package com.bcinfo.tripaway.view.dialog;

import android.os.Bundle;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;

/**
 * 产品-申请预订-选择酒店
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月7日 9:55:22
 */
public class HotelPickedDialogActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);

        setContentView(R.layout.dialog_layout_hotel_picked);
        this.getWindow().getAttributes().height = (int) this.getResources().getDimension(
                R.dimen.dialog_product_car_picked_window_height);

    }

}
