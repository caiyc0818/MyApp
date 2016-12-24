package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.TripPlanDetailListAdapter;
import com.bcinfo.tripaway.bean.ScenicInfo;

import android.os.Bundle;
import android.widget.ListView;
/**
 * 行程规划详情
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月27日 下午4:45:35
 */
public class TripPlanDetailActivity extends BaseActivity
{
    private TripPlanDetailListAdapter scenicListAdapter;
    private ArrayList<ScenicInfo> scenicArrayList=new ArrayList<ScenicInfo>();
  private   ListView scenicListView;
  
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.trip_plan_detail_activity);
        setSecondTitle("行程规划");
        scenicListView=(ListView)findViewById(R.id.trip_plan_detail_listview);
        for (int i = 0; i < 6; i++)
        {
            ScenicInfo info=new ScenicInfo();
//            info.setName("洛杉矶环球影城，Universal Studios Welcome");
//            info.setPhotoUrls(getCarUrls());
//            info.setComment("刚刚从洛杉矶环球影城，Universal Studios Welcome 回来");
//            info.setTicketPrice("门票：￥544");
//            info.setIndex((i+1)+"");
//            info.setNextDistance("距离下一景点44公里，大约30分钟到达");
            scenicArrayList.add(info);
        }
        scenicListAdapter=new TripPlanDetailListAdapter(this, scenicArrayList);
        scenicListView.setAdapter(scenicListAdapter);
    }
    
    private ArrayList<String> getCarUrls()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg");
        list.add("http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg");
        return list;
    }
}
