package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.SeekBar.RangeSeekBar;
import com.bcinfo.tripaway.view.SeekBar.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.bcinfo.tripaway.view.SeekBar.RangeSeekBar.OnRightRangeSeekBarChangeListener;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 筛选条件界面
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2014年12月12日 14:00:23
 * 
 * 
 */
public class TripFilterConditionActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "TripFilterConditionActivity";
    /**
     * 筛选条件  标题栏 右边的 "完成" 字样  textview
     */
    private TextView filterTitleRightTv;
    /**
     * 标题栏右边的  放大镜 图标
     */
    private ImageView discoveryRightIconIv;
    /**
     * 行程开始时间
     */
    private LinearLayout startTimeLayout;
    /**
     * 开始时间  文本框
     */
    private TextView startTimeTv;
    /**
     * 行程结束时间
     */
    private LinearLayout endTimeLayout;
    /**
     * 结束时间  文本框
     */
    private TextView endTimeTv;
    /**
     * 开始时间-年
     */
    private int startYear;
    /**
     * 开始时间-月
     */
    private int startMonthOfYear;
    /**
     * 开始时间-日
     */
    private int startDayOfMonth;
    /**
     * 结束时间-年
     */
    private int endYear;
    /**
     * 结束时间-月
     */
    private int endMonthOfYear;
    /**
     * 结束时间-日
     */
    private int endDayOfMonth;
    /**
     * 日期选择对话框
     */
    private DatePickerDialog dateDialog;
    /**
     * 同行游客  减号
     */
    private Button subTractMark;
    /**
     * 同行游客  加号
     */
    private Button addMark;
    /**
     * 同行游客  个数
     */
    private int  uNum=2;
    /**
     * 同行游客 textview
     */
    private TextView visitorCountTv;
    /**
     * 定义 "价格范围"二级拖动条
     */
    private LinearLayout seekBarLayout;
    /**
     * 定义 "价格范围"二级拖动条
     * jiangchangshan 
     */
    private RangeSeekBar<Integer> mRangeSeekBar;
    /**
     * 上次拖动条左边值
     * jiangchangshan 
     */
    private int lastMinValue = 25;
    /**
     * 上次拖动条右值
     * jiangchangshan 
     */
    private int lastMaxValue = 975;
    /**
     * checkbox数组
     */
    private CheckBox[] cb;
	// 删选的参数
	private String[] filterpar =new String[5];
	//价格数组
	private String[] mypricearr;
    public LinearLayout getSeekBarLayout()
    {
        return seekBarLayout;
    }

    public void setSeekBarLayout(LinearLayout seekBarLayout)
    {
        this.seekBarLayout = seekBarLayout;
    }
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            int minValue = mRangeSeekBar.getSelectedMinValue(); // 获得移动到的小值
            int maxValue = mRangeSeekBar.getSelectedMaxValue(); // 获得移动到的大值
            int valueStep = 20; // valueStep 变量代表的是一次进度增加的幅度值
            int timeStep = 3;
            switch (msg.what)
            {
                case 1:
                    if (minValue > msg.arg1) // 将之前移动到的值和规定的值进行比较 (如果大于，代表了移动的值在进度规定值的右边)
                    {
                        if (minValue - valueStep > msg.arg1)
                        {
                            mRangeSeekBar.setSelectedMinValue(minValue - valueStep);
                            Message minValueMsg = handler.obtainMessage();
                            minValueMsg.what = 1;
                            minValueMsg.arg1 = msg.arg1;
                            handler.sendMessageDelayed(minValueMsg, timeStep);
                        }
                        else
                        {
                            mRangeSeekBar.setSelectedMinValue(msg.arg1);
                            if (lastMinValue != lastMaxValue)
                            {
                                cb[getCBIndex(lastMinValue)].setChecked(false);
                            }
                            cb[getCBIndex(msg.arg1)].setChecked(true);
                            lastMinValue = msg.arg1;
                        }
                    }
                    else // 同理 (小于，代表了移动的值在进度规定值的左边)
                    {
                        if (minValue + valueStep < msg.arg1) // 如果之前移动到的值加上偏移量小于特定的进度值
                        {
                            mRangeSeekBar.setSelectedMinValue(minValue + valueStep);
                            Message minValueMsg = handler.obtainMessage();
                            minValueMsg.what = 1;
                            minValueMsg.arg1 = msg.arg1;
                            handler.sendMessageDelayed(minValueMsg, timeStep);
                        }
                        else
                        {
                            mRangeSeekBar.setSelectedMinValue(msg.arg1);
                            if (lastMinValue != lastMaxValue)
                            {
                                cb[getCBIndex(lastMinValue)].setChecked(false);
                            }
                            cb[getCBIndex(msg.arg1)].setChecked(true);
                            lastMinValue = msg.arg1;
                        }
                    }
                    break;
                case 2:
                    if (maxValue > msg.arg1)
                    {
                        if (maxValue - valueStep > msg.arg1)
                        {
                            mRangeSeekBar.setSelectedMaxValue(maxValue - valueStep);
                            Message maxValueMsg = handler.obtainMessage();
                            maxValueMsg.what = 2;
                            maxValueMsg.arg1 = msg.arg1;
                            handler.sendMessageDelayed(maxValueMsg, timeStep);
                        }
                        else
                        {
                            mRangeSeekBar.setSelectedMaxValue(msg.arg1);
                            if (lastMinValue != lastMaxValue)
                            {
                                cb[getCBIndex(lastMaxValue)].setChecked(false);
                            }
                            cb[getCBIndex(msg.arg1)].setChecked(true);
                            lastMaxValue = msg.arg1;
                        }
                    }
                    else
                    {
                        if (maxValue + valueStep < msg.arg1)
                        {
                            mRangeSeekBar.setSelectedMaxValue(maxValue + valueStep);
                            Message maxValueMsg = handler.obtainMessage();
                            maxValueMsg.what = 2;
                            maxValueMsg.arg1 = msg.arg1;
                            handler.sendMessageDelayed(maxValueMsg, timeStep);
                        }
                        else
                        {
                            mRangeSeekBar.setSelectedMaxValue(msg.arg1);
                            if (lastMinValue != lastMaxValue)
                            {
                                cb[getCBIndex(lastMaxValue)].setChecked(false);
                            }
                            cb[getCBIndex(msg.arg1)].setChecked(true);
                            lastMaxValue = msg.arg1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 根据进度条的值获取CheckBox位置
     * @param value
     */
    private int getCBIndex(int value)
    {
        int position = 0;
        switch (value)
        {
            case 25:
                position = 0;
                break;
            case 215:
                position = 1;
                break;
            case 405:
                position = 2;
                break;
            case 595:
                position = 3;
                break;
            case 785:
                position = 4;
                break;
            case 975:
                position = 5;
                break;
            default:
                break;
        }
        return position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_condition);
        startTimeLayout = (LinearLayout) findViewById(R.id.trip_start_time_layout);
        endTimeLayout = (LinearLayout) findViewById(R.id.trip_end_time_layout);
        startTimeTv = (TextView) findViewById(R.id.trip_start_time);
        endTimeTv = (TextView) findViewById(R.id.trip_end_time);
        subTractMark = (Button) findViewById(R.id.substract_icon);
        addMark = (Button) findViewById(R.id.add_icon);
        visitorCountTv = (TextView) findViewById(R.id.trip_visitor_counts);
        seekBarLayout = (LinearLayout) this.findViewById(R.id.seekBar_layout);
        cb = new CheckBox[6];
        cb[0] = (CheckBox) findViewById(R.id.zerobox1);
        cb[1] = (CheckBox) findViewById(R.id.onebox2);
        cb[2] = (CheckBox) findViewById(R.id.fivebox3);
        cb[3] = (CheckBox) findViewById(R.id.onebox4);
        cb[4] = (CheckBox) findViewById(R.id.dotfivebox5);
        cb[5] = (CheckBox) findViewById(R.id.twobox6);
        navBack = (ImageView) this.findViewById(R.id.discovery_back_button);
        discoverLayout = (LinearLayout) findViewById(R.id.discovery_discover_container);
        filterTitleRightTv = (TextView) findViewById(R.id.discovery_right_text);
        filterTitleRightTv.setVisibility(View.VISIBLE);// 筛选条件界面标题栏右边的"完成"字样变为可见
        filterTitleRightTv.setOnClickListener(this);
        discoveryRightIconIv=(ImageView)findViewById(R.id.discovery_discover_button);
//        discoverCancelTv = (TextView) findViewById(R.id.discovery_discover_cancel_tv);
        discoverTitle = (TextView) this.findViewById(R.id.discovery_title_text);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        subTractMark.setOnClickListener(this);
        addMark.setOnClickListener(this);
        navBack.setVisibility(View.VISIBLE);
//        discoverCancelTv.setVisibility(View.GONE);
        discoverLayout.setVisibility(View.GONE);
       discoveryRightIconIv.setVisibility(View.GONE);
      
        discoverTitle.setText(R.string.choice_title);
        discoverTitle.setVisibility(View.VISIBLE);
        navBack.setOnClickListener(mOnClickListener);
        mypricearr=getResources().getStringArray(R.array.pricearr);//得到价格数组
        // 创建RangeSeekBar自定义对象 第一个参数表示二级拖动条的最小值；第二个参数表示拖动条的最大值；第三个参数表示context上下文对象
        mRangeSeekBar = new RangeSeekBar<Integer>(0, 1000, this);
        mRangeSeekBar.setSelectedMinValue(25); // 设置初始的最小值
        mRangeSeekBar.setSelectedMaxValue(975); // 设置初始最大值
        // 参数表示创建一个OnRangeSeekBarChangeListener接口的实现类 并实现接口中的方法
        mRangeSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue)
            {
//                Log.i(TAG, "方法执行了");
                Message msg = handler.obtainMessage();
                if (lastMinValue != minValue) // 判断如果这次移动的值不等于初始的最小值的话,那么
                {
                    LogUtil.i(TAG, "SeekBarValues", "minValue=" + minValue);
                    handler.removeMessages(2); // 这里的removeMessage()的作用是当有人为干预时，删除之前移动滑块时延迟发送的消息
                    handler.removeMessages(1);
                   
                    msg.what = 1;
                    if (minValue <= 110) // 110 : 0-1K之间的中间点
                    {
                        msg.arg1 = 25; // 25 即  0处:最靠近minValue的值
                        handler.sendMessage(msg);
                    }
                    else if (minValue <= 300) // 300: 1K-5K之间的中间点
                    {
                        msg.arg1 = 215; // 215 即 1K处 :最靠近minValue的值
                        handler.sendMessage(msg);
                    }
                    else if (minValue <= 500) // 500: 5K-1W之间的中间点
                    {
                        msg.arg1 = 405;
                        handler.sendMessage(msg);
                    }
                    else if (minValue <= 700) // 700 : 1W-1.5W之间的中间点
                    {
                        msg.arg1 = 595;
                        handler.sendMessage(msg);
                    }
                    else if (minValue <= 890) // 890 : 1.5W-2W之间的中间点 
                    {
                        msg.arg1 = 785;
                        handler.sendMessage(msg);
                    }
                    else
                    {
                        msg.arg1 = 975;
                        handler.sendMessage(msg);
                    }
                }
                if (lastMaxValue != maxValue)
                {
                    LogUtil.i(TAG, "SeekBarValues", "maxValue=" + maxValue);
                    handler.removeMessages(2);
                    handler.removeMessages(1);
                    msg.what = 2;
                    if (maxValue >= 890)
                    {
                        msg.arg1 = 975;
                        handler.sendMessage(msg);
                    }
                    else if (maxValue >= 700)
                    {
                        msg.arg1 = 785;
                        handler.sendMessage(msg);
                    }
                    else if (maxValue >= 500)
                    {
                        msg.arg1 = 595;
                        handler.sendMessage(msg);
                    }
                    else if (maxValue >= 300)
                    {
                        msg.arg1 = 405;
                        handler.sendMessage(msg);
                    }
                    else if (maxValue >= 110)
                    {
                        msg.arg1 = 215;
                        handler.sendMessage(msg);
                    }
                    else
                    {
                        msg.arg1 = 25;
                        handler.sendMessage(msg);
                    }
                }

            }
        });
       
        //        seekBar.setNotifyWhileDragging(true);
        // 将自定义的seekBar添加到线性布局中
        seekBarLayout.addView(mRangeSeekBar); 
        initIfAlreadyFilter(this.getIntent().getBooleanExtra("isfilter",false));
    }
    /**
     *初始化参数。
     */
    private void initIfAlreadyFilter(Boolean AlreadyFilter)
    {
    	if(AlreadyFilter){
    		filterpar=this.getIntent().getStringArrayExtra("filterpar");
    		uNum=Integer.valueOf(filterpar[0]);
    		visitorCountTv.setText(uNum + "位");
    		String minprice=filterpar[1].substring(1, filterpar[1].split("T")[0].length()-1);
    		String maxprice=filterpar[1].substring(filterpar[1].split("O")[0].length()+2,filterpar[1].split("]")[0].length());
    		for(int i=0;i<6;i++){
    			if(minprice.equals(mypricearr[i])){
    				cb[i].setChecked(true);
    				mRangeSeekBar.setSelectedMinValue(getposition(i)); // 设置初始的最小值
    				lastMinValue=getposition(i);
    			}
    			if(maxprice.equals(mypricearr[i])){
    				cb[i].setChecked(true);
    				mRangeSeekBar.setSelectedMaxValue(getposition(i)); // 设置初始最大值
    				lastMaxValue=getposition(i);
    			}
    		}
    		if(filterpar[2].contains("交通"))
    			((CheckBox) findViewById(R.id.trip_car_service)).setChecked(true);
    		if(filterpar[2].contains("行程"))
    			((CheckBox) findViewById(R.id.trip_route_plan)).setChecked(true);
    		if(filterpar[2].contains("房屋"))
    			((CheckBox) findViewById(R.id.trip_rent)).setChecked(true);
            startYear = Integer.valueOf(filterpar[3].substring(0,4));
            startMonthOfYear=Integer.valueOf(filterpar[3].substring(5,7))-1;
            startDayOfMonth =Integer.valueOf(filterpar[3].substring(8,10));
            endYear =Integer.valueOf(filterpar[4].substring(0,4));
            endMonthOfYear =Integer.valueOf(filterpar[4].substring(5,7))-1;
            endDayOfMonth = Integer.valueOf(filterpar[4].substring(8,10));
            startTimeTv.setText(filterpar[3]);
            endTimeTv.setText(filterpar[4]);
    	}else{
            Calendar calendar = Calendar.getInstance();
            startYear = calendar.get(Calendar.YEAR);
            startMonthOfYear = calendar.get(Calendar.MONTH);
            startDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            endYear = calendar.get(Calendar.YEAR);
            endMonthOfYear = calendar.get(Calendar.MONTH);
            endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            cb[0].setChecked(true);
            cb[5].setChecked(true);
    	}
    } 
    /**
     * 得到标准位置
     */
    private int getposition(int num){
    	int num1=0;
    	switch(num){
    	case 0:
    		num1=25;
    		break;
    	case 1:
    		num1=215;
    		break;
    	case 2:
    		num1=405;
    		break;
    	case 3:
    		num1=595;
    		break;
    	case 4:
    		num1=785;
    		break;
    	case 5:
    		num1=975;
    		break;
    	}
    	return num1;
    }
    /**
     * 日期转换成字符串
     */
    private String getDatetoString(int year, int monthOfYear, int dayOfMonth,String interval)
    {
        String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
        String day = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        return year + interval + month + interval + day;
    }
    
    /**
     * 比较日期
     */
    private boolean compareDate()
    {
    	if(startYear>endYear)
    		return false;
    	if(startYear==endYear&&startMonthOfYear>endMonthOfYear)
    		return false;
    	if(startYear==endYear&&startMonthOfYear==endMonthOfYear&&startDayOfMonth>endDayOfMonth)
    		return false;
    	return true;
    }
    private OnDateSetListener onDateSetListener1 = new OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            TripFilterConditionActivity.this.startYear = year;
            TripFilterConditionActivity.this.startMonthOfYear = monthOfYear;
            TripFilterConditionActivity.this.startDayOfMonth = dayOfMonth;
            // 更新日期
            startTimeTv.setText(getDatetoString(year, monthOfYear, dayOfMonth,"/"));
            }
    };
    private OnDateSetListener onDateSetListener2 = new OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            TripFilterConditionActivity.this.endYear = year;
            TripFilterConditionActivity.this.endMonthOfYear = monthOfYear;
            TripFilterConditionActivity.this.endDayOfMonth = dayOfMonth;
            // 更新日期
            endTimeTv.setText(getDatetoString(year, monthOfYear, dayOfMonth,"/"));
            }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.discovery_right_text: // 文本框  显示  "完成"字样
            	if(compareDate()){
	                Intent intentForBack=new Intent(this, DiscoveryDefaultsolresultActivity.class);
	                String servType=((CheckBox) findViewById(R.id.trip_car_service)).isChecked()? "交通 ":"";
	                servType+=((CheckBox) findViewById(R.id.trip_route_plan)).isChecked()? "行程 ":"";
	                servType+=((CheckBox) findViewById(R.id.trip_rent)).isChecked()? "房屋":"";
	                filterpar[0]=String.valueOf(uNum);//人数
	                filterpar[1]=getthepriceRange();//价格范围
	                filterpar[2]=servType;//提供的服务
	                filterpar[3]=getDatetoString(startYear, startMonthOfYear, startDayOfMonth,"/");//开始时间
	                filterpar[4]=getDatetoString(endYear, endMonthOfYear, endDayOfMonth,"/");//结束时间
	                intentForBack.putExtra("filterpar",filterpar);
	                setResult(RESULT_OK, intentForBack);
	                finish();
	                activityAnimationClose(this);
            	}else{
            		Toast.makeText(getBaseContext(),"结束时间不能小于开始时间哦!",Toast.LENGTH_SHORT).show();
            	}
            break;
            case R.id.add_icon: // 点击 加号  图标
                if (!subTractMark.isEnabled())
                {
                    subTractMark.setEnabled(true);
                }
                
                visitorCountTv.setText(++uNum + "位");
                if (uNum == 10)
                    // 设置加号不可用  图标变成灰色
                    addMark.setEnabled(false);
                if (!subTractMark.isEnabled())
                	subTractMark.setEnabled(true);
                break;
            case R.id.substract_icon: // 点击 减号  图标
                // if 判断 加号图标是否可用
                if (!addMark.isEnabled())
                    addMark.setEnabled(true);
                visitorCountTv.setText(--uNum + "位");
                if (uNum == 1)
                    subTractMark.setEnabled(false);
                break;
            case R.id.trip_start_time_layout:// 点击  开始时间
                dateDialog = new DatePickerDialog(this, onDateSetListener1, startYear, startMonthOfYear,
                        startDayOfMonth);
                dateDialog.updateDate(startYear, startMonthOfYear, startDayOfMonth);
                dateDialog.show();
                break;
            case R.id.trip_end_time_layout:// 点击  结束时间
                dateDialog = new DatePickerDialog(this, onDateSetListener2, endYear, endMonthOfYear, endDayOfMonth);
                dateDialog.updateDate(endYear, endMonthOfYear, endDayOfMonth);
                dateDialog.show();
                break;
            default:
                break;
        }
    }
    //得到价格范围
    private String getthepriceRange(){
    	String Minprice = null,Maxprice=null;
    	for(int i=0;i<6;i++){
    		if(cb[i].isChecked()){
    			if(null==Minprice)
    				Minprice=mypricearr[i];
    			else
    				Maxprice=mypricearr[i];
    		}
    	}
    	return "["+Minprice+" TO "+Maxprice+"]";
    } 
    
}
