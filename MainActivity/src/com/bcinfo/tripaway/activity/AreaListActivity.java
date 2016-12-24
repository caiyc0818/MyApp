package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaAdapter;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;

/**
 * @author hanweipeng
 * @date 2015-7-18 上午9:58:37
 */
public class AreaListActivity extends BaseActivity
{
    private ListView areaLst;
    
    private AreaAdapter adapter;
    
    private ArrayList<AreaInfo> areaList;
    
    private String selectCity = "";
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.area_activity);
        setSecondTitle("地区");
        areaList = getIntent().getParcelableArrayListExtra("areaList");
        if (areaList == null)
        {
            areaList = new ArrayList<AreaInfo>();
        }
        areaLst = (ListView)findViewById(R.id.area_listview);
        adapter = new AreaAdapter(this, areaList);
        areaLst.setAdapter(adapter);
        areaLst.setOnItemClickListener(new OnItemClickListener()
        {
            
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
                    Intent intent = new Intent(AreaListActivity.this, AreaListActivity.class);
                    intent.putParcelableArrayListExtra("areaList", newAreaList);
                    startActivityForResult(intent, 102);
                    activityAnimationOpen();
                }
                
            }
        });
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 102 && arg2 != null)
        {
            String areaString = selectCity + " " + arg2.getStringExtra("area");
            Intent intent = new Intent();
            intent.putExtra("area", areaString);
            setResult(RESULT_OK, intent);
            finish();
            activityAnimationClose();
        }
    }
    
}
