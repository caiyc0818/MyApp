package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AirListViewAdapter;
import com.bcinfo.tripaway.adapter.AirListViewAdapter.OnFlightOperationListener;
import com.bcinfo.tripaway.adapter.ComfireOrderAdapter;
import com.bcinfo.tripaway.adapter.ComfireOrderAdapter.OnComfireOperationListener;
import com.bcinfo.tripaway.bean.ComfireInfo;
import com.bcinfo.tripaway.bean.FlightInfo;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.ServiceInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.date.DayPicker;
import com.bcinfo.tripaway.view.dialog.DelectChoiseTrafficDialog;
import com.bcinfo.tripaway.view.dialog.DelectChoiseTrafficDialog.OperationListener;
import com.bcinfo.tripaway.view.dialog.FlightPickedDialogActivity;
import com.bcinfo.tripaway.view.dialog.SelectFlightDialog;
import com.bcinfo.tripaway.view.dialog.SelectFlightDialog.SelectFlightListener;
import com.bcinfo.tripaway.view.dialog.TrafficToolsPickedDialog;
import com.bcinfo.tripaway.view.dialog.TrafficToolsPickedDialog.ChoseTimeListener;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Function：详情界面
 * 
 * @author： tcyangpeng
 * @Time：2015年3月31日下午4:32:46
 * @Version： 1.0
 */
@Deprecated
public class ConfirmOrderActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
    private static final String TAG = "ConfirmOrderActivity";
    
    // 开始时间
    private TextView beginDate;
    
    // 结束时间
    private TextView endDate;
    
    /**
     * 日期选择时间（开始）对话框
     */
    private AlertDialog begindialog;
    
    /**
     * 日期选择时间（结束）对话框
     * 
     */
    private AlertDialog endDialog;
    
    /**
     * 日期选择时间
     */
    private DayPicker mdayPicker;
    
    /**
     * 飞机列表adapter
     */
    private AirListViewAdapter mAirListViewAdapter;
    
    // 飞机listview
    private LinearLayout mAirListView;
    
    private List<FlightInfo> mAirDataList = new ArrayList<FlightInfo>();
    
    /**
     * 汽车或轮船adapter
     */
    private ComfireOrderAdapter mNoAirAdapter;
    
    // 汽车或轮船listview
    private LinearLayout mNoAirListView;
    
    private List<ComfireInfo> mNoAirDatalist = new ArrayList<ComfireInfo>();
    
    /**
     * 住宿adapter
     */
    private ComfireOrderAdapter mHotelAdapter;
    
    private LinearLayout mHotelListView;
    
    private List<ComfireInfo> mHotelDataList = new ArrayList<ComfireInfo>();
    
    private String productId;
    
    private List<ServResrouce> airPlaneList = new ArrayList<ServResrouce>();
    
    private List<ServResrouce> carOrBoaticList = new ArrayList<ServResrouce>();
    
    private List<ServResrouce> stayList = new ArrayList<ServResrouce>();
    
    private TextView productPriceTxt;
    
    private TextView stayPriceTxt;
    
    private TextView trafficPriceTxt;
    
    private TextView totalPriceTxt;
    
    private List<String> resourceIdList = new ArrayList<String>();;
    
    private Button paybutton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_comfire_order);
        setSecondTitle("确认详情");
        beginDate = (TextView)findViewById(R.id.comfire_date_begin);
        endDate = (TextView)findViewById(R.id.comfire_date_end);
        mAirListView = (LinearLayout)findViewById(R.id.comfire_plane);
        mNoAirListView = (LinearLayout)findViewById(R.id.comfire_othertraffic);
        mHotelListView = (LinearLayout)findViewById(R.id.comfire_hotel);
        paybutton = (Button)findViewById(R.id.comfir_pay_button);
        beginDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        paybutton.setOnClickListener(this);
        productId = getIntent().getStringExtra("productId");
        initFlightListView();
        initNoAirView();
        initHotelView();
        initPriceView();
        getservBookResource(productId);
        getApplyPrice(productId, null);
    }
    
    private void initPriceView()
    {
        productPriceTxt = (TextView)findViewById(R.id.product_price);
        stayPriceTxt = (TextView)findViewById(R.id.stay_price);
        trafficPriceTxt = (TextView)findViewById(R.id.traffic_price);
        totalPriceTxt = (TextView)findViewById(R.id.price_total);
    }
    
    private void initPriceInfoData(String productPrice, String totalPrice, List<ServiceInfo> serviceInfos)
    {
        productPriceTxt.setText(productPrice);
        totalPriceTxt.setText(totalPrice);
        for (int i = 0; i < serviceInfos.size(); i++)
        {
            if (serviceInfos.get(i).getCode().equals("traffic"))
            {
                trafficPriceTxt.setText(serviceInfos.get(i).getPrice());
            }
            else if (serviceInfos.get(i).getCode().equals("stay"))
            {
                stayPriceTxt.setText(serviceInfos.get(i).getPrice());
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.comfire_date_begin:
                dayPickerDialog();
                break;
            case R.id.comfire_date_end:
                dayEndPickerDialog();
                break;
            case R.id.ok_button_date:
                Calendar calendar = mdayPicker.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String notify = year + "年" + (month + 1) + "月" + day + "日";
                beginDate.setText(notify);
                beginDate.setTextColor(getResources().getColor(R.color.dark_gray));
                begindialog.cancel();
                break;
            case R.id.cancel_button_date:
                if (begindialog.isShowing())
                {
                    begindialog.cancel();
                }
                else
                {
                    endDialog.cancel();
                }
                break;
            case R.id.comfir_pay_button:
                applyBook();
                break;
            default:
                break;
        }
    }
    
    /**
     * 起始日期选择方法
     */
    private void dayPickerDialog()
    {
        begindialog = new AlertDialog.Builder(this).create();
        begindialog.show();
        Window window = begindialog.getWindow();
        window.setContentView(R.layout.day_picker_dialog);
        mdayPicker = (DayPicker)window.findViewById(R.id.alarm_date_picker);
        Button ok = (Button)window.findViewById(R.id.ok_button_date);
        Button cancel = (Button)window.findViewById(R.id.cancel_button_date);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                begindialog.cancel();
            }
        });
    }
    
    /**
     * 终止日期选择方法
     */
    private void dayEndPickerDialog()
    {
        endDialog = new AlertDialog.Builder(this).create();
        endDialog.show();
        Window window = endDialog.getWindow();
        window.setContentView(R.layout.day_picker_enddialog);
        mdayPicker = (DayPicker)window.findViewById(R.id.alarm_date_endpicker);
        Button ok = (Button)window.findViewById(R.id.ok_button_enddate);
        Button cancel = (Button)window.findViewById(R.id.cancel_button_enddate);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endDialog.cancel();
            }
        });
    }
    
    /**
     * 飞机listview的充填
     */
    private void initFlightListView()
    {
        mAirListViewAdapter = new AirListViewAdapter(this, airPlaneList, mAirListView);
        mAirListViewAdapter.setOnFlightOperationListener(new OnFlightOperationListener()
        {
            @Override
            public void onItemClick(View itemView, ServResrouce info, int position)
            {
                // TODO Auto-generated method stub
                SelectFlightDialog dialog =
                    new SelectFlightDialog(ConfirmOrderActivity.this, mSelectFlightListener, itemView, position);
                dialog.show();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.height = (int)(screenHeight - statusBarHeight - 120);
                dialog.getWindow().setAttributes(lp);
            }
            
            @Override
            public void onDelectChoise(ServResrouce info)
            {
                // TODO Auto-generated method stub
                DelectChoiseTrafficDialog dialog =
                    new DelectChoiseTrafficDialog(ConfirmOrderActivity.this, new OperationListener()
                    {
                        @Override
                        public void onDelectFlightInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                            info.setChecked(false);
                            mAirListViewAdapter.notifyDataSetChanged();
                        }
                        
                        @Override
                        public void onDelectComfireInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
                dialog.setFlightInfo(info);
                dialog.show();
            }
        });
    }
    
    SelectFlightListener mSelectFlightListener = new SelectFlightListener()
    {
        @Override
        public void selectFlight(FlightInfo info, View itemView, int position)
        {
            FlightInfo temp = mAirDataList.get(0);
            temp.setHaveFlight(true);
            mAirListViewAdapter.notifyDataSetChanged();
        }
    };
    
    /**
     * 汽车或轮船的填充
     */
    private void initNoAirView()
    {
        mNoAirAdapter = new ComfireOrderAdapter(this, carOrBoaticList, mNoAirListView);
        mNoAirAdapter.setOnComfireOperationListener(new OnComfireOperationListener()
        {
            @Override
            public void onItemClick(final View itemView, final ServResrouce info, int position)
            {
                // TODO Auto-generated method stub
                TrafficToolsPickedDialog dialog =
                    new TrafficToolsPickedDialog(ConfirmOrderActivity.this, info, new ChoseTimeListener()
                    {
                        @Override
                        public void onResult(String startTime, String endTime)
                        {
                            // TODO Auto-generated method stub
                            TextView useTime = (TextView)itemView.findViewById(R.id.comfire_item_time);
                            useTime.setText("预订时间：" + startTime + "-" + endTime);
                            addResourceIdPrice(info.getServId());
                            mNoAirAdapter.notifyDataSetChanged();
                        }
                    });
                dialog.show();
            }
            
            @Override
            public void onDelectChoise(ServResrouce info)
            {
                // TODO Auto-generated method stub
                DelectChoiseTrafficDialog dialog =
                    new DelectChoiseTrafficDialog(ConfirmOrderActivity.this, new OperationListener()
                    {
                        @Override
                        public void onDelectComfireInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                            info.setChecked(false);
                            removeResourceIdPrice(info.getServId());
                            mNoAirAdapter.notifyDataSetChanged();
                        }
                        
                        @Override
                        public void onDelectFlightInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
                dialog.setComfireInfo(info);
                dialog.show();
            }
        });
    }
    
    /**
     * 酒店住宿的充填
     */
    private void initHotelView()
    {
        mHotelAdapter = new ComfireOrderAdapter(this, stayList, mHotelListView);
        mHotelAdapter.setOnComfireOperationListener(new OnComfireOperationListener()
        {
            @Override
            public void onItemClick(final View itemView, final ServResrouce info, int position)
            {
                // TODO Auto-generated method stub
                TrafficToolsPickedDialog dialog =
                    new TrafficToolsPickedDialog(ConfirmOrderActivity.this, info, new ChoseTimeListener()
                    {
                        @Override
                        public void onResult(String startTime, String endTime)
                        {
                            // TODO Auto-generated method stub
                            TextView useTime = (TextView)itemView.findViewById(R.id.comfire_item_time);
                            useTime.setText("预订时间：" + startTime + "-" + endTime);
                            addResourceIdPrice(info.getServId());
                            mHotelAdapter.notifyDataSetChanged();
                        }
                    });
                dialog.show();
            }
            
            @Override
            public void onDelectChoise(ServResrouce info)
            {
                // TODO Auto-generated method stub
                DelectChoiseTrafficDialog dialog =
                    new DelectChoiseTrafficDialog(ConfirmOrderActivity.this, new OperationListener()
                    {
                        @Override
                        public void onDelectComfireInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                            info.setChecked(false);
                            removeResourceIdPrice(info.getServId());
                            mHotelAdapter.notifyDataSetChanged();
                        }
                        
                        @Override
                        public void onDelectFlightInfo(ServResrouce info)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
                dialog.setComfireInfo(info);
                dialog.show();
            }
        });
    }
    
    private void getApplyPrice(String productId, String[] resource)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productId", productId);
            JSONArray resourceArray = new JSONArray();
            if (resource != null)
            {
                for (int i = 0; i < resource.length; i++)
                {
                    resourceArray.put(resource[i]);
                }
                jsonObject.put("resource", resourceArray);
            }
            else
            {
                jsonObject.put("resource", "");
            }
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.apply_price_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtil.i(TAG, "getApplyPrice", "statusCode=" + statusCode);
                    LogUtil.i(TAG, "getApplyPrice", "responseString=" + responseString);
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    // LogUtil.i(TAG, "getApplyPrice", "responseString=" + response.toString());
                    System.out.println("预订价格接口=" + response);
                    JSONObject dataJson;
                    try
                    {
                        dataJson = response.getJSONObject("data");
                        if (dataJson != null && !dataJson.equals(""))
                        {
                            String toatlPrice = dataJson.optString("totalPrice");
                            String price = dataJson.optString("price");
                            JSONArray serviceArray = dataJson.optJSONArray("service");
                            List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
                            if (serviceArray != null && serviceArray.length() > 0)
                            {
                                for (int i = 0; i < serviceArray.length(); i++)
                                {
                                    ServiceInfo serviceInfo = new ServiceInfo();
                                    JSONObject serviceJson = serviceArray.optJSONObject(i);
                                    serviceInfo.setCode(serviceJson.optString("code"));
                                    serviceInfo.setName(serviceJson.optString("name"));
                                    serviceInfo.setPrice(serviceJson.optString("price"));
                                }
                            }
                            initPriceInfoData(price, toatlPrice, serviceInfos);
                        }
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void getservBookResource(String id)
    {
        HttpUtil.get(Urls.serv_book_url + id, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i(TAG, "getservBookResource", response.toString());
                if (response.opt("code").equals("00000"))
                {
                    JSONObject dataJsonObject = response.optJSONObject("data");
                    JSONArray trafficJSONList = dataJsonObject.optJSONArray("traffic");
                    if (trafficJSONList != null && trafficJSONList.length() > 0)
                    {
                        for (int j = 0; j < trafficJSONList.length(); j++)
                        {
                            JSONObject serviceJSON = trafficJSONList.optJSONObject(j);
                            ServResrouce service = new ServResrouce();
                            service.setServId(serviceJSON.optString("servId"));
                            service.setServName(serviceJSON.optString("servName"));
                            service.setServAlias(serviceJSON.optString("servAlias"));
                            service.setServType(serviceJSON.optString("servType"));
                            service.setRank(serviceJSON.optString("rank"));
                            service.setTitleImage(serviceJSON.optString("titleImg"));
                            service.setServDesc(serviceJSON.optString("servDesc"));
                            if (service.getServType().equals("airplane") || service.getServType().equals("Iairplane"))
                            {
                                airPlaneList.add(service);
                            }
                            else
                            {
                                carOrBoaticList.add(service);
                            }
                        }
                    }
                    JSONArray stayJSONList = dataJsonObject.optJSONArray("stay");
                    if (stayJSONList != null && stayJSONList.length() > 0)
                    {
                        for (int j = 0; j < stayJSONList.length(); j++)
                        {
                            JSONObject serviceJSON = stayJSONList.optJSONObject(j);
                            ServResrouce service = new ServResrouce();
                            service.setServId(serviceJSON.optString("servId"));
                            service.setServName(serviceJSON.optString("servName"));
                            service.setServAlias(serviceJSON.optString("servAlias"));
                            service.setServType(serviceJSON.optString("servType"));
                            service.setRank(serviceJSON.optString("rank"));
                            service.setTitleImage(serviceJSON.optString("titleImg"));
                            service.setServDesc(serviceJSON.optString("servDesc"));
                            stayList.add(service);
                        }
                    }
                    mAirListViewAdapter.notifyDataSetChanged();
                    mNoAirAdapter.notifyDataSetChanged();
                    mHotelAdapter.notifyDataSetChanged();
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "getservBookResource", "statusCode=" + statusCode);
                LogUtil.i(TAG, "getservBookResource", "responseString=" + responseString);
            }
        });
    }
    
    private void applyBook()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productId", productId);
            jsonObject.put("beginTime", "21050627");
            jsonObject.put("endTime", "21050628");
            jsonObject.put("leaveWord", "");
            JSONArray partentsArray = new JSONArray();
            partentsArray.put("1");
            partentsArray.put("2");
            jsonObject.put("partnerIds", partentsArray);
            JSONArray resourceArray = new JSONArray();
            for (int i = 0; i < resourceIdList.size(); i++)
            {
                JSONObject resourceJson = new JSONObject();
                resourceJson.put("id", resourceIdList.get(i));
                JSONArray availableTimeArray = new JSONArray();
                JSONObject availableTimeJson = new JSONObject();
                availableTimeJson.put("beginDate", "21050627");
                availableTimeJson.put("endDate", "21050628");
                availableTimeArray.put(availableTimeJson);
                resourceJson.put("availableTime", availableTimeArray);
                resourceArray.put(resourceJson);
            }
            jsonObject.put("resource", resourceArray);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.apply_book_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                    System.out.println("applyBook-->onFailure" + statusCode);
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("申请预订接口=" + response);
                }
                
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        switch (adapterView.getId())
        {
            case R.id.comfire_plane:// 航班
                Intent flightPickedIntent = new Intent(this, FlightPickedDialogActivity.class);
                startActivity(flightPickedIntent);
                break;
            case R.id.comfire_othertraffic: // 交通工具
                break;
            default:
                break;
        }
    }
    
    private void addResourceIdPrice(String id)
    {
        resourceIdList.add(id);
        int size = resourceIdList.size();
        String[] array = (String[])resourceIdList.toArray(new String[size]);
        getApplyPrice(productId, array);
    }
    
    private void removeResourceIdPrice(String id)
    {
        for (int i = 0; i < resourceIdList.size(); i++)
        {
            if (resourceIdList.get(i).equals(id))
            {
                resourceIdList.remove(i);
            }
        }
        int size = resourceIdList.size();
        String[] array = (String[])resourceIdList.toArray(new String[size]);
        getApplyPrice(productId, array);
    }
}
