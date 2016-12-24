package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.db.HistoryRecordDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.view.editText.DiscoveryEditText;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 搜索默认页面
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月18日 14:31:11
 * 
 */
public class DiscoveryDefaultActivity extends BaseActivity implements TextWatcher, OnClickListener, OnItemClickListener
{
    private DiscoveryEditText discoveryEditText;// 搜索-默认页面 搜索输入框
    
    private static final String DEFAULT_URL = "2015-05-30_09:52:04_rbBBYctZ.jpg";
    
    private static final String SOU_COUNT_TEXT = "  共"; // 共...
    
    private static final String SOU_PIECE_TEXT = "条"; // 条
    
    /**
     * 热搜词 gridview
     */
    private GridView hotKeyWordsGridView;
    
    /**
     * 搜索历史 listview
     */
    private ListView discoveryHistoryList;
    
    /**
     * 自动匹配 listview
     */
    private ListView resultListView;
    
    /**
     * 发现-搜索框中的放大镜图标
     */
    protected TextView discoveryZoomOutIcon;
    
    private ArrayList<Map<String, String>> dataList;// 搜索提示信息 集合
    
    /**
     * 搜索可能结果的list集合 (下拉搜索框)
     * 
     */
    private ArrayList<Map<String, String>> dataResultList;
    
    /**
     * 搜索历史记录集合
     */
    private ArrayList<Map<String, String>> historyDataList = new ArrayList<Map<String, String>>();
    
    /**
     * 热搜词集合
     */
    private List<Map<String, Object>> souKeyListItem;
    
    // 搜索提示信息适配器
    private SimpleAdapter adapter1;
    
    // 搜索历史记录适配器
    private SimpleAdapter adapter2;
    
    // 搜索-热搜词 适配器
    private SimpleAdapter adapter3;
    
    // 筛选条件
    // private LinearLayout filterLayout;
    // 要搜索的文字
    private String finalkeyWords = "";
    
    private HistoryRecordDB myhistoryrecord;
    
    private InputMethodManager inputManager;
    
    public ArrayList<Map<String, String>> getDataList()
    {
        return dataList;
    }
    
    public void setDataList(ArrayList<Map<String, String>> dataList)
    {
        this.dataList = dataList;
    }
    
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0x1233:
                    // historyListView.setVisibility(View.VISIBLE);
                    dataResultList.clear();
                    adapter1.notifyDataSetChanged();
                    
                    break;
                case 0x1235:
                    // historyListView.setVisibility(View.VISIBLE);
                    getmHistoryData();
                    Log.d("", "-------,historyDataList=" + historyDataList.toString());
                    adapter2.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.discovery_default);
        LayoutInflater inflator = LayoutInflater.from(this);
        hotKeyWordsGridView = (GridView)findViewById(R.id.discovery_hot_keywords_gridview);
        discoveryHistoryList = (ListView)findViewById(R.id.discovery_history_listview);
        resultListView = (ListView)findViewById(R.id.discovery_result_ListView);
        hotKeyWordsGridView.setOnItemClickListener(this);
        discoveryHistoryList.setOnItemClickListener(this);
        QuerySouKeyWords();
        dataList = getmData();
        myhistoryrecord = new HistoryRecordDB();
        getmHistoryData();
        dataResultList = new ArrayList<Map<String, String>>();
        // resultList = new ArrayList<ProductInfo>();
        navBack = (ImageView)findViewById(R.id.discovery_back_button);
        discoverThemeIcon = (ImageView)findViewById(R.id.discovery_discover_button);
        // discoverCancelTv = (TextView)
        // findViewById(R.id.discovery_discover_cancel_tv);
        discoveryZoomOutIcon = (TextView)findViewById(R.id.discovery_discover_icon);
        TextView discoveryHistoryRecordClearTv = (TextView)findViewById(R.id.discovery_history_record_clear_tv);
        discoveryHistoryRecordClearTv.setOnClickListener(this);
        discoveryEditText = (DiscoveryEditText)findViewById(R.id.discovery_discover_keywords);
        adapter1 =
            new SimpleAdapter(this, dataResultList, R.layout.item_discover_hint, new String[] {"itemValue",
                "countValue"}, new int[] {R.id.discovery_items_result, R.id.discovery_items_counts});
        adapter2 =
            new SimpleAdapter(this, historyDataList, R.layout.item_discovery_history_data, new String[] {"itemName"},
                new int[] {R.id.discovery_history_item_name_tv});
        resultListView.setAdapter(adapter1);
        resultListView.setOnItemClickListener(this);
        // historyListView.addHeaderView(headerView);
        // historyListView.addFooterView(footerView);
        discoveryHistoryList.setAdapter(adapter2);
        // discoverCancelTv.setOnClickListener(mOnClickListener);
        // discoveryEditText.setFocusable(true);
        // discoveryEditText.setFocusableInTouchMode(true);
        discoveryEditText.requestFocus();
        // 设置discoveryEditText的text change事件
        discoveryEditText.addTextChangedListener(this);
        // 隐藏搜索默认二级页面的放大镜图标
        discoverThemeIcon.setVisibility(View.GONE);
        navBack.setOnClickListener(mOnClickListener);
        discoveryZoomOutIcon.setOnClickListener(this);
        // discoverCancelTv.setOnClickListener(mOnClickListener);
        Timer timer = new Timer();
        // timer指定延迟时间之后执行run()方法 自动弹出虚拟键盘
        timer.schedule(new TimerTask()
        {
            
            @Override
            public void run()
            {
                inputManager =
                    (InputMethodManager)discoveryEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                
                inputManager.showSoftInput(discoveryEditText, 0); // 显示输入法
                
            }
        },
            300);
        discoveryEditText.setOnTouchListener(new DiscoveryEditText.OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                if (inputManager == null || !inputManager.isActive())
                    inputManager =
                        (InputMethodManager)discoveryEditText.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(discoveryEditText, 0); // 显示输入法
                return false;
            }
        });
        discoveryEditText.setOnEditorActionListener(new OnEditorActionListener()
        {
            
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                // TODO Auto-generated method stub
                
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {// 搜索键的响应action
                    testsolresponse();
                }
                return false;
            }
        });
        
    }
    
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessage(0x1235);
        super.onStart();
    }
    
    /**
     * 查询 热门搜索关键词
     */
    public void QuerySouKeyWords()
    {
        
        HttpUtil.get(Urls.solrHotWordsUrl, null, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                
                if (statusCode == 200)
                {
                    souKeyListItem = getSouKeyList(response);
                    System.out.println("response=" + response);
                    if (souKeyListItem.size() != 0)
                    {
                        adapter3 =
                            new SimpleAdapter(DiscoveryDefaultActivity.this, souKeyListItem,
                                R.layout.item_discovery_hot_keywords_gridview, new String[] {"keyword"},
                                new int[] {R.id.discovery_hot_keywords_tv});
                        hotKeyWordsGridView.setAdapter(adapter3); // 设置 热搜词
                        
                    }
                }
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("json get failed statusCode=" + statusCode);
                System.out.println("responseString=" + responseString);
            }
        });
        
    }
    
    private List<Map<String, Object>> getSouKeyList(JSONObject response)
    {
        List<Map<String, Object>> souKeyList = new ArrayList<Map<String, Object>>();
        if ("00000".equals(response.optString("code")))
        {
            JSONArray souKeyWordsArray = response.optJSONObject("data").optJSONArray("hotwords");
            if (souKeyWordsArray != null && souKeyWordsArray.length() != 0)
            {
                for (int i = 0; i < souKeyWordsArray.length(); i++)
                {
                    Map<String, Object> souKeyWordItem = new HashMap<String, Object>();
                    souKeyWordItem.put("keyword", souKeyWordsArray.optString(i));
                    souKeyList.add(souKeyWordItem);
                    
                }
                
            }
        }
        
        return souKeyList;
    }
    
    /**
     * init 搜索提示框数据集
     */
    private ArrayList<Map<String, String>> getmData()
    {
        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        
        Map<String, String> mapItem1 = new HashMap<String, String>();
        mapItem1.put("itemValue", "洛杉矶影城豪华五日游");
        mapItem1.put("countValue", "约5个结果");
        
        dataList.add(mapItem1);
        Map<String, String> mapItem2 = new HashMap<String, String>();
        mapItem2.put("itemValue", "洛杉矶国王山一日游");
        mapItem2.put("countValue", "约2个结果");
        dataList.add(mapItem2);
        Map<String, String> mapItem3 = new HashMap<String, String>();
        mapItem3.put("itemValue", "中国长城半日游");
        mapItem3.put("countValue", "约17个结果");
        dataList.add(mapItem3);
        Map<String, String> mapItem4 = new HashMap<String, String>();
        mapItem4.put("itemValue", "美国纽约酒吧会所狂欢");
        mapItem4.put("countValue", "约22个结果");
        dataList.add(mapItem4);
        Map<String, String> mapItem5 = new HashMap<String, String>();
        mapItem5.put("itemValue", "伦敦大本钟秋游");
        mapItem5.put("countValue", "约12个结果");
        dataList.add(mapItem5);
        Map<String, String> mapItem6 = new HashMap<String, String>();
        mapItem6.put("itemValue", "澳门赌场赌博大战");
        mapItem6.put("countValue", "约65个结果");
        dataList.add(mapItem6);
        Map<String, String> mapItem7 = new HashMap<String, String>();
        mapItem7.put("itemValue", "阿里山无限风光");
        mapItem7.put("countValue", "约8个结果");
        dataList.add(mapItem7);
        Map<String, String> mapItem8 = new HashMap<String, String>();
        mapItem8.put("itemValue", "云南大理云游仙境");
        mapItem8.put("countValue", "约33个结果");
        dataList.add(mapItem8);
        Map<String, String> mapItem9 = new HashMap<String, String>();
        mapItem9.put("itemValue", "上方寺烧香拜佛");
        mapItem9.put("countValue", "约91个结果");
        dataList.add(mapItem9);
        Map<String, String> mapItem10 = new HashMap<String, String>();
        mapItem10.put("itemValue", "西安兵马俑皇陵游");
        mapItem10.put("countValue", "约13个结果");
        dataList.add(mapItem10);
        return dataList;
    }
    
    /**
     * init 搜索历史记录数据集
     */
    private void getmHistoryData()
    {
        if (null != historyDataList)
        {
            historyDataList.clear();
        }
        historyDataList.addAll(myhistoryrecord.readRecord(10));
    }
    
    /*
     * 当文本框中的文本输入后 回调afterTextChanged方法
     */
    @Override
    public void afterTextChanged(Editable s)
    {
        if (discoveryEditText.hasFocus)
        {
            // if判断 text文本内容的长度是否大于0 如果大于0 那么就显示右边的删除图标
            if (s.length() > 0)
            {
                // showDrawableVisible(true);
                discoveryEditText.setCompoundDrawables(discoveryEditText.getCompoundDrawables()[0],
                    discoveryEditText.getCompoundDrawables()[1],
                    discoveryEditText.mdrawable,
                    discoveryEditText.getCompoundDrawables()[3]);
                
                mHandler.post(mRunnable);
            }
            else
            {
                discoveryEditText.setCompoundDrawables(discoveryEditText.getCompoundDrawables()[0],
                    discoveryEditText.getCompoundDrawables()[1],
                    null,
                    discoveryEditText.getCompoundDrawables()[3]);
                mHandler.sendEmptyMessage(0x1233);
            }
        }
        else
        {
            discoveryEditText.setCompoundDrawables(discoveryEditText.getCompoundDrawables()[0],
                discoveryEditText.getCompoundDrawables()[1],
                null,
                discoveryEditText.getCompoundDrawables()[3]);
        }
        
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // TODO 自动生成的方法存根
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // TODO 自动生成的方法存根
        
    }
    
    /**
     * Runnable
     */
    private Runnable mRunnable = new Runnable()
    {
        
        @Override
        public void run()
        {
            // historyListView.setVisibility(View.INVISIBLE);
            // resultListView.setVisibility(View.VISIBLE);
            String str = discoveryEditText.getText().toString();
            getMetaData(dataResultList, str);
            // Log.d("","-----mRunnable,dataResultList="+dataResultList.toString());
            adapter1.notifyDataSetChanged();
        }
    };
    
    private void getMetaData(ArrayList<Map<String, String>> dataResultList, String str)
    {
        dataResultList.clear();
        for (int i = 0; i < dataList.size(); i++)
        {
            Map<String, String> mapValue = dataList.get(i);
            if (mapValue.get("itemValue").contains(str))
            {
                dataResultList.add(mapValue);
                
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        
            case R.id.discovery_discover_icon:// 搜索框 图标
                testsolresponse();
                break;
            case R.id.discovery_discover_button:
                Intent intentForDefault = new Intent(v.getContext(), DiscoveryDefaultActivity.class);
                startActivity(intentForDefault);
                finish();
                break;
            case R.id.discovery_history_record_clear_tv:
                new AlertDialog.Builder(this).setTitle("")
                    .setMessage("确定删除历史记录吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            myhistoryrecord.deleteRecord("");
                            mHandler.sendEmptyMessage(0x1235);
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
                
                break;
            default:
                break;
        }
        
    }
    
    /*
     * testsolresponse 搜索按键反应
     */
    private void testsolresponse()
    {
        finalkeyWords = discoveryEditText.getText().toString().trim();
        if (TextUtils.isEmpty(finalkeyWords))
        {
            Toast.makeText(this, "搜索不能为空哦!", 0).show();
        }
        else
        {
            if (inputManager == null || !inputManager.isActive())
                inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(discoveryEditText.getWindowToken(), 0);// 隐藏软键盘
            Intent intentForDefault =
                new Intent(DiscoveryDefaultActivity.this, DiscoveryDefaultsolresultActivity.class);
            intentForDefault.putExtra("keyWords", finalkeyWords);
            startActivity(intentForDefault);
            myhistoryrecord.saveRHistoryRecord(finalkeyWords);
            activityAnimationOpen(this);
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        
        switch (parent.getId())
        {
            case R.id.discovery_hot_keywords_gridview: // 热搜词
                finalkeyWords = souKeyListItem.get(position).get("keyword").toString();
                discoveryEditText.setText(finalkeyWords);
                Intent intentForhotkeyProductList =
                    new Intent(DiscoveryDefaultActivity.this, DiscoveryDefaultsolresultActivity.class);
                intentForhotkeyProductList.putExtra("keyWords", finalkeyWords);
                startActivity(intentForhotkeyProductList);
                break;
            case R.id.discovery_history_listview: // 历史记录
                
                finalkeyWords = historyDataList.get(position).get("itemName");
                discoveryEditText.setText(finalkeyWords);
                Intent intentForhistorydataProductList =
                    new Intent(DiscoveryDefaultActivity.this, DiscoveryDefaultsolresultActivity.class);
                intentForhistorydataProductList.putExtra("keyWords", finalkeyWords);
                startActivity(intentForhistorydataProductList);
                break;
            case R.id.discovery_result_ListView:
                finalkeyWords = dataResultList.get(position).get("itemValue");
                discoveryEditText.setText(finalkeyWords);
                Intent intentForresultdataProductList =
                    new Intent(DiscoveryDefaultActivity.this, DiscoveryDefaultsolresultActivity.class);
                intentForresultdataProductList.putExtra("keyWords", finalkeyWords);
                startActivity(intentForresultdataProductList);
                break;
            default:
                break;
        // TODO Auto-generated method stub
        
        }
        myhistoryrecord.saveRHistoryRecord(finalkeyWords);
        activityAnimationOpen(this);
    }
    
}
