package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.PartnerMemberAdapter;
import com.bcinfo.tripaway.adapter.PartnerMemberAdapter.OnDelPartnerItemListener;
import com.bcinfo.tripaway.adapter.ProductDateAdapter;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.alipay.AlipayUtile;
import com.bcinfo.tripaway.utils.alipay.Result;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.date.DayPicker;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @author hanweipeng
 * @date 2015-7-7 下午8:08:39
 */
public class ConfirmPayActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
    /**
     * 单产品选择时间布局
     */
    private LinearLayout timeLayout;
    
    /**
     * 开始时间
     */
    private TextView beginTimeTxt;
    
    /**
     * 结束时间
     */
    private TextView endTimeTxt;
    
    /**
     * 团产品选择时间布局
     */
    private MyListView timeLst;
    
    /**
     * 自己的姓名
     */
    private EditText userNameEdt;
    
    /**
     * 自己的证件号
     */
    private EditText userNumberEdt;
    
    // private TextView memberCount;
    
    /**
     * 同行人列表
     */
    private MyListView memberLst;
    
    /**
     * 添加同行人按钮
     */
    private TextView addMemberBtn;
    
    /**
     * 产品价格
     */
    private TextView productPriceTxt;
    
    /**
     * 总价格
     */
    private TextView totalPriceTxt;
    
    /**
     * 留言标题
     */
    private TextView leaveTitleTxt;
    
    /**
     * 留言输入框
     */
    private EditText leaveEdt;
    
    private ProductNewInfo productInfo;
    
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
     * 随行人列表
     */
    private List<PartnerInfo> partnerInfos = new ArrayList<PartnerInfo>();
    
    /**
     * 随行人适配器
     */
    private PartnerMemberAdapter memberAdapter;
    
    private ProductDateAdapter dateAdapter;
    
    private TextView countTxt;
    
    private int selectPosition = -1;
    
    /**
     * 开始时间
     */
    private String beginDate = "";
    
    /**
     * 结束时间
     */
    private String endDate = "";
    
    private final static String TAG = "ConfirmPayActivity";
    
    private String orderNo;
    
    private String total;
    
    /**
     * 订购按钮
     */
    private Button confirmPayBtn;
    
    private TextView levelTxt;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.confirm_pay_layout);
        setSecondTitle("订购详情");
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
        titleLayout.setAlpha(1);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
        initView();
        getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
    }
    
    protected void initView()
    {
        productInfo = getIntent().getParcelableExtra("productInfo");
        timeLayout = (LinearLayout)findViewById(R.id.comfire_time_lin);
        beginTimeTxt = (TextView)findViewById(R.id.comfire_date_begin);
        endTimeTxt = (TextView)findViewById(R.id.comfire_date_end);
        timeLst = (MyListView)findViewById(R.id.time_listview);
        userNameEdt = (EditText)findViewById(R.id.user_name);
        userNumberEdt = (EditText)findViewById(R.id.user_number);
        memberLst = (MyListView)findViewById(R.id.member_listview);
        addMemberBtn = (TextView)findViewById(R.id.add_member);
        productPriceTxt = (TextView)findViewById(R.id.product_price);
        totalPriceTxt = (TextView)findViewById(R.id.total_price);
        leaveTitleTxt = (TextView)findViewById(R.id.leave_title);
        levelTxt = (TextView)findViewById(R.id.level);
        leaveEdt = (EditText)findViewById(R.id.leave_edt);
        countTxt = (TextView)findViewById(R.id.member_count);
        confirmPayBtn = (Button)findViewById(R.id.comfir_pay_button);
        confirmPayBtn.setOnClickListener(this);
        beginTimeTxt.setOnClickListener(this);
        endTimeTxt.setOnClickListener(this);
        timeLst.setOnItemClickListener(this);
        memberLst.setOnItemClickListener(this);
        addMemberBtn.setOnClickListener(this);
        if (productInfo.getProductType().equals("team"))
        {
            timeLayout.setVisibility(View.GONE);
            timeLst.setVisibility(View.VISIBLE);
            dateAdapter =
                new ProductDateAdapter(ConfirmPayActivity.this, productInfo.getExpPeriodList(),
                    Integer.parseInt(productInfo.getDays()));
            timeLst.setAdapter(dateAdapter);
        }
        else
        {
            beginDate = DateUtil.getCurrentDateStr().replaceAll("/", "");
            beginTimeTxt.setText(DateUtil.getCurrentDateStr());
            if (productInfo.getProductType().equals("base"))
            {
                endDate =
                    DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays())).replaceAll("/",
                        "");
                endTimeTxt.setText(DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays())));
            }
            timeLayout.setVisibility(View.VISIBLE);
            timeLst.setVisibility(View.GONE);
        }
        UserInfo getUserInfo = UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
        if (getUserInfo != null)
        {
            userNameEdt.setText(getUserInfo.getNickname());
            // userNumberEdt.setText(getUserInfo.getUsersIdentity());
        }
        if (productInfo.getLevel().equals("super"))
        {
            levelTxt.setText("非常严格");
        }
        else if (productInfo.getLevel().equals("high"))
        {
            levelTxt.setText("严格");
        }
        else if (productInfo.getLevel().equals("middle"))
        {
            levelTxt.setText("适中");
        }
        else if (productInfo.getLevel().equals("low"))
        {
            levelTxt.setText("灵活");
        }
        memberAdapter = new PartnerMemberAdapter(ConfirmPayActivity.this, partnerInfos,new OnDelPartnerItemListener() {
			
			@Override
			public void deletePartner(int position) {
				// TODO Auto-generated method stub
				
			}
		});
        memberLst.setAdapter(memberAdapter);
        countTxt.setText("(" + productInfo.getMaxMember() + "/" + (partnerInfos.size() + 1) + ")");
        leaveTitleTxt.setText("向" + productInfo.getUser().getNickname() + "打个招呼吧");
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.comfir_pay_button:
                // if (productInfo.getProductType().equals("team"))
                // {
                // // getOrederInfo(productInfo.getId(), productInfo.getExpPeriodList().get(selectPosition), endDate,
                // // leaveEdt.getText().toString());
                // }
                // else
                // {
                if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "请填写完整的开始时间和结束时间");
                    return;
                }
                else if (StringUtils.isEmpty(userNameEdt.getText().toString())
                    || StringUtils.isEmpty(userNumberEdt.getText().toString()))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "请将自己的信息填写完整");
                    return;
                }
                getOrederInfo(productInfo.getId(), beginDate, endDate, leaveEdt.getText().toString());
                // }
                break;
            case R.id.comfire_date_begin:
                dayPickerDialog();
                break;
            case R.id.comfire_date_end:
                dayEndPickerDialog();
                break;
            case R.id.add_member:
                if (Integer.parseInt(productInfo.getMaxMember()) <= (partnerInfos.size() + 1))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "随行人已达到最大上限数");
                    return;
                }
                Intent intent = new Intent(ConfirmPayActivity.this, AddMemberActivity.class);
                intent.putExtra("isAdd", true);
                startActivityForResult(intent, 101);
                break;
            case R.id.ok_button_date:
            {
                Calendar calendar = mdayPicker.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String monthStr = (month + 1) + "";
                String dayStr = day + "";
                if (month < 9)
                {
                    monthStr = "0" + (month + 1);
                }
                if (day < 10)
                {
                    dayStr = "0" + day;
                }
                if (DateUtil.getcurrentTimeMillis() > DateUtil.getTimeInMillis(year + "" + monthStr + "" + dayStr))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "选择的开始时间不能早于当前时间");
                    return;
                }
                if (!productInfo.getProductType().equals("base"))
                {
                    if (StringUtils.isEmpty(endDate)
                        && !DateUtil.compareTime(year + "" + monthStr + "" + dayStr, endDate))
                    {
                        ToastUtil.showToast(ConfirmPayActivity.this, "选择的开始时间不能晚于结束时间");
                        return;
                    }
                }
                // if (!StringUtils.isEmpty(endDate))
                // {
                // // 标准产品受时间限制 单产品是根据天数计价的
                // if (productInfo.getProductType().equals("base")
                // && DateUtil.isBeyondMaxDay(year + "" + monthStr + "" + dayStr,
                // endDate,
                // Integer.parseInt(productInfo.getDays())))
                // {
                // ToastUtil.showToast(ConfirmPayActivity.this, "选择的天数不符合产品的周期");
                // return;
                // }
                // }
                beginDate = year + "" + monthStr + "" + dayStr;
                if (productInfo.getProductType().equals("base"))
                {
                    endDate =
                        DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays()))
                            .replaceAll("/", "");
                    endTimeTxt.setText(DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays())));
                }
                String notify = year + "/" + (month + 1) + "/" + day;
                beginTimeTxt.setText(notify);
                beginTimeTxt.setTextColor(getResources().getColor(R.color.dark_gray));
                begindialog.cancel();
                if (partnerInfos.size() == 0)
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
                }
                else
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, partnerInfos.size() + 1);
                }
            }
                break;
            case R.id.ok_button_enddate:
            {
                Calendar calendar = mdayPicker.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String monthStr = (month + 1) + "";
                String dayStr = day + "";
                if (month < 9)
                {
                    monthStr = "0" + (month + 1);
                }
                if (day < 10)
                {
                    dayStr = "0" + day;
                }
                if (DateUtil.getcurrentTimeMillis() > DateUtil.getTimeInMillis(year + "" + monthStr + "" + dayStr))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "选择的结束时间不能早于当前时间");
                    return;
                }
                // 标准产品受时间限制 单产品是根据天数计价的
                if (productInfo.getProductType().equals("base")
                    && DateUtil.isBeyondMaxDay(beginDate,
                        year + "" + monthStr + "" + dayStr,
                        Integer.parseInt(productInfo.getDays())))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "选择的天数不符合产品的周期");
                    return;
                }
                if (!productInfo.getProductType().equals("base"))
                {
                    if (!DateUtil.compareTime(beginDate, year + "" + monthStr + "" + dayStr))
                    {
                        ToastUtil.showToast(ConfirmPayActivity.this, "选择的结束时间不能早于开始时间");
                        return;
                    }
                }
                endDate = year + "" + monthStr + "" + dayStr;
                String notify = year + "/" + (month + 1) + "/" + day;
                endTimeTxt.setText(notify);
                endTimeTxt.setTextColor(getResources().getColor(R.color.dark_gray));
                endDialog.cancel();
                if (partnerInfos.size() == 0)
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
                }
                else
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, partnerInfos.size() + 1);
                }
                
            }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg0)
        {
            case 101:
                if (arg2 != null)
                {
                    int index = 0;
                    if (arg2.getStringExtra("action").equals("del"))
                    {
                        index = arg2.getIntExtra("index", 0);
                        for (int i = 0; i < partnerInfos.size(); i++)
                        {
                            if (i == index)
                            {
                                partnerInfos.remove(i);
                            }
                        }
                    }
                    else if (arg2.getStringExtra("action").equals("add"))
                    {
                        PartnerInfo partnerInfo = arg2.getParcelableExtra("partner");
                        partnerInfos.add(partnerInfo);
                    }
                    else if (arg2.getStringExtra("action").equals("modify"))
                    {
                        index = arg2.getIntExtra("index", 0);
                        PartnerInfo partnerInfo = arg2.getParcelableExtra("partner");
                        for (int i = 0; i < partnerInfos.size(); i++)
                        {
                            if (i == index)
                            {
                                partnerInfos.get(i).setRealName(partnerInfo.getRealName());
                                partnerInfos.get(i).setIdNo(partnerInfo.getIdNo());
                            }
                        }
                    }
                    if (partnerInfos.size() > 0)
                    {
                        memberLst.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        memberLst.setVisibility(View.GONE);
                    }
                    
                    memberAdapter.notifyDataSetChanged();
                    countTxt.setText("(" + productInfo.getMaxMember() + "/" + partnerInfos.size() + ")");
                }
                if (partnerInfos.size() == 0)
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
                }
                else
                {
                    getApplyPrice(productInfo.getId(), beginDate, endDate, partnerInfos.size() + 1);
                }
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
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        switch (parent.getId())
        {
            case R.id.time_listview:
                if (DateUtil.getTimeInMillis(productInfo.getExpPeriodList().get(position).getBeginDate()) <= DateUtil.getcurrentTimeMillis())
                {
                    return;
                }
                else
                {
                    dateAdapter.setSelectPosition(position);
                }
                selectPosition = position;
                beginDate = productInfo.getExpPeriodList().get(position).getBeginDate();
                endDate = productInfo.getExpPeriodList().get(position).getEndDate();
                break;
            case R.id.member_listview:
                Intent intent = new Intent(ConfirmPayActivity.this, AddMemberActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("index", position);
                intent.putExtra("partner", partnerInfos.get(position));
                startActivityForResult(intent, 101);
                break;
            default:
                break;
        }
    }
    
    // 获取价格接口
    private void getApplyPrice(String productId, String beginTime, String endTime, int partnerNum)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productId", productId);
            jsonObject.put("beginTime", beginTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("partnerNum", partnerNum);
            System.out.println("jsonObject=" + jsonObject.toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
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
                    if (!response.optString("code").equals("00000"))
                    {
//                        ToastUtil.showToast(ConfirmPayActivity.this, "error=" + response.optString("code"));
                        return;
                    }
                    JSONObject dataJson;
                    try
                    {
                        dataJson = response.getJSONObject("data");
                        if (dataJson != null && !dataJson.equals("") && !dataJson.equals("null"))
                        {
                            String toatlPrice = dataJson.optString("totalPrice");
                            String price = dataJson.optString("price");
                            String amount = dataJson.optString("amount");
                            String unit = dataJson.optString("unit");
                            productPriceTxt.setText("¥" + price);
                            totalPriceTxt.setText("¥" + toatlPrice);
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
    
    // 生成订单获取订单号接口
    private void getOrederInfo(String productId, String beginTime, String endTime, String leaveWord)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("productId", productId);
            jsonObject.put("beginTime", beginTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("leaveWord", leaveWord);
            JSONArray partnerArray = new JSONArray();
            for (int i = 0; i < partnerInfos.size() + 1; i++)
            {
                JSONObject partnerJson = new JSONObject();
                if (i == 0)
                {
                    partnerJson.put("name", userNameEdt.getText().toString());
                    partnerJson.put("idNo", userNumberEdt.getText().toString());
                    partnerJson.put("myself", "yes");
                }
                else
                {
                    partnerJson.put("name", partnerInfos.get(i - 1).getRealName());
                    partnerJson.put("idNo", partnerInfos.get(i - 1).getIdNo());
                    partnerJson.put("myself", "no");
                }
                partnerArray.put(partnerJson);
            }
            jsonObject.put("partner", partnerArray);
            System.out.println("生成订单jsonObject=" + jsonObject.toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            HttpUtil.post(Urls.apply_orderInfo_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtil.i(TAG, "getOrederInfo", "statusCode=" + statusCode);
                    throwable.printStackTrace();
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("生成订单接口=" + response);
                    if (!response.optString("code").equals("00000"))
                    {
//                        ToastUtil.showToast(ConfirmPayActivity.this, "error=" + response.optString("code"));
                        return;
                    }
                    else
                    {
                        try
                        {
                            JSONObject dataJson = response.getJSONObject("data");
                            if (dataJson != null && !dataJson.equals("") && !dataJson.equals("null"))
                            {
                                orderNo = dataJson.optString("orderNo");
                                total = dataJson.optString("total");
                                // 去支付
                                new Thread()
                                {
                                    public void run()
                                    {
                                        String orderInfoStr =
                                            AlipayUtile.getNewOrderInfo(orderNo, productInfo.getTitle(), total);
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(ConfirmPayActivity.this);
                                        // 调用支付接口
                                        String result = alipay.pay(orderInfoStr);
                                        
                                        Log.i("MyOrderDetailActivity", "result = " + result);
                                        Message msg = new Message();
                                        msg.what = 0;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                }.start();
                            }
                        }
                        catch (JSONException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
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
    
    private void Topaysuccess(String result)
    {
        Intent newIntent = new Intent(this, PaySuccessAcivity.class);
        newIntent.putExtra("orderNo", orderNo);
        newIntent.putExtra("total", total);
        newIntent.putExtra("result", result);
        newIntent.putExtra("beginDate", beginDate);
        newIntent.putExtra("endDate", endDate);
        newIntent.putExtra("leaveEdt", leaveEdt.getText().toString());
        newIntent.putExtra("name", productInfo.getTitle());
        newIntent.putExtra("num", String.valueOf(partnerInfos.size() + 1));
        startActivity(newIntent);
        activityAnimationOpen();
        finish();
    }
    
    Handler mHandler = new Handler()
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Result result = new Result((String)msg.obj);
            if (msg.what == 0)
            {
                if (result.getResult().equals("9000"))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "支付成功");
                    finish();
                    activityAnimationClose();
                }
                else if (result.getResult().equals("4000"))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "系统异常");
                }
                else if (result.getResult().equals("4001"))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "订单参数错误");
                }
                else if (result.getResult().equals("6001"))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "用户取消支付");
                }
                else if (result.getResult().equals("6002"))
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "网络连接异常");
                }
                else
                {
                    ToastUtil.showToast(ConfirmPayActivity.this, "其他錯誤");
                }
                Topaysuccess(result.getResult());
            }
        }
        
    };
}
