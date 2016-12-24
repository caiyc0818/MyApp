package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.LocationAreaAdapter;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.utils.HanziToPinyin;
import com.bcinfo.tripaway.view.SideBar;
import com.bcinfo.tripaway.view.SideBar.OnTouchingLetterChangedListener;

/**
 * @author hanweipeng
 * @date 2015-7-18 上午9:58:37
 */
public class LocationListActivity extends BaseActivity
{
    private ListView areaLst;
    
    private LocationAreaAdapter adapter;
    
    private ArrayList<AreaInfo> areaList;
    
    private String selectCity = "";

	private SideBar sideBar;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.location_activity);
        setSecondTitle("更多城市");
        sideBar = (SideBar) findViewById(R.id.sidrbar); 
//        sideBar.setVisibility(View.INVISIBLE);
        //设置右侧触摸监听  
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {  
              
            @Override  
            public void onTouchingLetterChanged(String s) {  
                //该字母首次出现的位置  
                int position = adapter.getPositionForSection(s.charAt(0));  
                if(position != -1){  
                	areaLst.setSelection(position);  
                }  
                  
            }  
        });
        areaList = getIntent().getParcelableArrayListExtra("areaList");
        if (areaList == null)
        {
            areaList = new ArrayList<AreaInfo>();
        }else{
        	for( AreaInfo areaInfo:areaList  ){
        		areaInfo.setSortLetter(HanziToPinyin.getFirstPinYin(areaInfo.getAreaName()));
        	}
        }
        areaLst = (ListView)findViewById(R.id.area_listview);
        adapter = new LocationAreaAdapter(this, areaList);
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
                    Intent intent = new Intent(LocationListActivity.this, LocationListActivity.class);
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
