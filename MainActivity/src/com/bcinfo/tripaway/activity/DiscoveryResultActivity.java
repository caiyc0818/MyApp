package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.view.editText.DiscoveryEditText;

/**
 * 发现-结果Activity
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年2月6日 11:10:23
 */
public class DiscoveryResultActivity extends BaseActivity implements TextWatcher, OnClickListener
{
    private DiscoveryEditText discoverEditText;
    
    private ListView discoverResultListView;
    
    private ArrayList<ArrayList<String>> productLogoList;
    
    private ArrayList<ProductInfo> resultList;
    
    // 发现结果下的筛选条件按钮
    private LinearLayout filterConditionLayout;
    
    // 发现 筛选结果下的筛选条件按钮
    private LinearLayout filterResultConditionLayout;
    
    // 搜索关键词
    private String keyWord;
    
    // private DiscoveryProductResultAdapter discoveryResultAdapter;
    public Handler discoveryHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0x1344:
                    resultList.remove(0);
                    resultList.remove(1);
                    // discoveryResultAdapter.notifyDataSetChanged();
                    filterConditionLayout.setVisibility(View.GONE);
                    filterResultConditionLayout.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.discovery_results);
        LayoutInflater inflator = LayoutInflater.from(this);
        initProductLogoList();
        initDiscoveryResult();
        keyWord = this.getIntent().getStringExtra("keyWord");
        discoverThemeIcon = (ImageView)findViewById(R.id.discovery_discover_button);
        navBack = (ImageView)findViewById(R.id.discovery_back_button);
        discoverEditText = (DiscoveryEditText)findViewById(R.id.discovery_discover_keywords);
        discoverResultListView = (ListView)findViewById(R.id.discovery_resultData_listView);
        filterConditionLayout = (LinearLayout)findViewById(R.id.discovery_results_bottomBtn);
        filterResultConditionLayout = (LinearLayout)findViewById(R.id.discovery_filter_results_bottomBtn);
        // View footView = inflator.inflate(R.layout.trip_product_footview, null);
        // discoverResultListView.addFooterView(footView);
        navBack.setOnClickListener(mOnClickListener);
        filterConditionLayout.setOnClickListener(this);
        discoverEditText.addTextChangedListener(this);
        if (!TextUtils.isEmpty(keyWord))
        {
            discoverEditText.setText(keyWord);
            discoverEditText.setSelection(keyWord.length());
        }
        discoverThemeIcon.setVisibility(View.GONE);
        // discoveryResultAdapter = new DiscoveryProductResultAdapter(resultList, this);
        // discoverResultListView.setAdapter(discoveryResultAdapter);
    }
    
    /**
     * init 搜索结果 搜索结果中包含普通产品和目的地产品两种 其中普通产品的type为1;目的地产品为4
     */
    private void initDiscoveryResult()
    {
        resultList = new ArrayList<ProductInfo>();
        /*
         * 搜索结果产品1 type: 1 表示标准产品 4表示国家，城市，或行程产品
         */
        ProductInfo proInfo1 = new ProductInfo();
        proInfo1.setId("1");
        // 产品名称
        proInfo1.setName("美国夏威夷海岛之旅");
        // 评价条数
        proInfo1.setEvaluateNum("355");
        proInfo1.setAuthorName("Caihelin");
        proInfo1.setPrice("9882");
        proInfo1.setAuthorPhotoUrl("http://99touxiang.com/public/upload/nvsheng/33/16-083608_342.jpg");
        proInfo1.setLogoUrls(productLogoList.get(0));
        proInfo1.setType("1");
        resultList.add(proInfo1);
        /*
         * 搜索结果产品2
         */
        ProductInfo proInfo2 = new ProductInfo();
        proInfo2.setId("2");
        proInfo2.setAddress("美国");
        proInfo2.setLogoUrls(productLogoList.get(1));
        proInfo2.setType("4");
        resultList.add(proInfo2);
        /*
         * 搜索结果产品3
         */
        ProductInfo proInfo3 = new ProductInfo();
        proInfo1.setId("3");
        // 产品名称
        proInfo3.setName("美国纽约行");
        // 评价条数
        proInfo3.setEvaluateNum("555");
        proInfo3.setAuthorName("Smitch Tom");
        proInfo3.setPrice("7754");
        proInfo3.setAuthorPhotoUrl("http://www.qqtouxiang888.com/uploads/allimg/110422/422-2.jpg");
        proInfo3.setLogoUrls(productLogoList.get(2));
        proInfo3.setType("1");
        resultList.add(proInfo3);
        /*
         * 搜索结果产品4
         */
        ProductInfo proInfo4 = new ProductInfo();
        proInfo4.setId("4");
        proInfo4.setAddress("西雅图");
        proInfo4.setLogoUrls(productLogoList.get(3));
        proInfo4.setType("4");
        resultList.add(proInfo4);
        /*
         * 搜索结果产品5
         */
        ProductInfo proInfo5 = new ProductInfo();
        proInfo5.setId("5");
        // 产品名5
        proInfo5.setName("国王山风景区之旅");
        // 评价条数
        proInfo5.setEvaluateNum("10000");
        proInfo5.setAuthorName("Jerry");
        proInfo5.setPrice("100223");
        proInfo5.setAuthorPhotoUrl("http://img02.store.sogou.com/app/a/10010016/370fbccef1effa77a15562fcfe4a76cd");
        proInfo5.setLogoUrls(productLogoList.get(4));
        proInfo5.setType("1");
        resultList.add(proInfo5);
        /*
         * 搜索产品6
         */
        ProductInfo proInfo6 = new ProductInfo();
        proInfo6.setId("6");
        proInfo6.setAddress("旧金山");
        proInfo6.setLogoUrls(productLogoList.get(5));
        proInfo6.setType("4");
        resultList.add(proInfo6);
        
    }
    
    /**
     * 获得网络图片
     */
    private void initProductLogoList()
    {
        productLogoList = new ArrayList<ArrayList<String>>();
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("http://pic29.nipic.com/20130531/12106509_170957350147_2.jpg");
        productLogoList.add(list1);
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("http://www.canachieve.com.cn/UserFiles/Image/meiguo03.gif");
        productLogoList.add(list2);
        ArrayList<String> list3 = new ArrayList<String>();
        list3.add("http://www.ccnuedu.net/uploads/allimg/130627/24-13062G5563II.png");
        productLogoList.add(list3);
        ArrayList<String> list4 = new ArrayList<String>();
        list4.add("http://www.lvxingqu.com/files/2012-12/20121219165028160266.jpg");
        productLogoList.add(list4);
        ArrayList<String> list5 = new ArrayList<String>();
        list5.add("http://pic17.nipic.com/20111006/8488195_162440259000_2.jpg");
        productLogoList.add(list5);
        ArrayList<String> list6 = new ArrayList<String>();
        list6.add("http://www.ixueyi.com/GetImges/39001-42000/41366/201501061506206250.png");
        productLogoList.add(list6);
    }
    
    @Override
    public void afterTextChanged(Editable s)
    {
        if (discoverEditText.hasFocus)
        {
            if (s.length() > 0)
            {
                discoverEditText.setCompoundDrawables(discoverEditText.getCompoundDrawables()[0],
                    discoverEditText.getCompoundDrawables()[1],
                    discoverEditText.mdrawable,
                    discoverEditText.getCompoundDrawables()[3]);
            }
            else
            {
                discoverEditText.setCompoundDrawables(discoverEditText.getCompoundDrawables()[0],
                    discoverEditText.getCompoundDrawables()[1],
                    null,
                    discoverEditText.getCompoundDrawables()[3]);
            }
        }
        else
        {
            discoverEditText.setCompoundDrawables(discoverEditText.getCompoundDrawables()[0],
                discoverEditText.getCompoundDrawables()[1],
                null,
                discoverEditText.getCompoundDrawables()[3]);
        }
        
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.discovery_results_bottomBtn:
                Intent intentForFilter = new Intent(this, TripFilterConditionActivity.class);
                // 类名标识
                intentForFilter.putExtra("flagClassName", "DiscoveryResultActivity");
                // 携带的数据源list
                intentForFilter.putExtra("flagListName", resultList);
                startActivityForResult(intentForFilter, 101);
                
                break;
            
            default:
                break;
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 101 && resultCode == RESULT_OK)
        {
            Message msg = discoveryHandler.obtainMessage();
            msg.what = 0x1344;
            discoveryHandler.sendMessage(msg);
        }
    }
    
}
