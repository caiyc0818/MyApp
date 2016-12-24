package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.DiscoveryLocationProductGridAdapter;
import com.bcinfo.tripaway.adapter.PickedLocationProductGridAdapter;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.view.editText.DiscoveryEditText;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 精选-目的地产品结果Activity
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月27日 13:58:33
 */
public class PickedDetailActivity extends BaseActivity implements OnClickListener, TextWatcher
{
    /**
     * 存放目的地全部ProductInfo产品的list集合
     */
    private ArrayList<ProductInfo> pickedLocationList;
    /**
     * map集合
     */
    private Map<Integer, ArrayList<String>> logoMap;
    /**
     * 精选-目的地产品结果标题栏  返回按钮
     */
    private ImageView pickedBackBtn;
    /**
        * 精选-目的地产品结果标题栏  自定义搜索框editText
        */
    private DiscoveryEditText pickedEditText;

    public DiscoveryEditText getPickedEditText()
    {
        return pickedEditText;
    }

    public void setPickedEditText(DiscoveryEditText pickedEditText)
    {
        this.pickedEditText = pickedEditText;
    }

    public ImageView getPickedBackBtn()
    {
        return pickedBackBtn;
    }

    public void setPickedBackBtn(ImageView pickedBackBtn)
    {
        this.pickedBackBtn = pickedBackBtn;
    }
    private ListView locationListView;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.picked_details_layout);
        String locationName = this.getIntent().getStringExtra("location");
        initLocationInfo(locationName);
        pickedBackBtn = (ImageView) findViewById(R.id.picked_back_button);
        pickedEditText = (DiscoveryEditText) findViewById(R.id.picked_discover_keywords);
        locationListView = (ListView) findViewById(R.id.picked_detail_listview);
        PickedLocationProductGridAdapter listAdapter = new PickedLocationProductGridAdapter(pickedLocationList, this);
        pickedEditText.setText(locationName);
        pickedEditText.setSelection(locationName.length());
        // 为搜索框添加text监听方法
        pickedEditText.addTextChangedListener(this);
        locationListView.setAdapter(listAdapter);
        pickedBackBtn.setOnClickListener(mOnClickListener);
    }

    /**
     * 获取目的地产品数据
     */
    private void initLocationInfo(String locationName)
    {
        pickedLocationList = new ArrayList<ProductInfo>();
        initLogoList(locationName);
        if ("福建".equals(locationName))
        {
            /*
             * 福建的全部产品   5个
             */
            ProductInfo info1 = new ProductInfo();
            info1.setId("1");
            info1.setName("福建九寨沟八日游");
            // 产品评价数量
            info1.setEvaluateNum("45");
            info1.setPrice("3888");
            info1.setAuthorName("Kitty");
            info1.setLogoUrls(logoMap.get(1));
            pickedLocationList.add(info1);
            ProductInfo info2 = new ProductInfo();
            info2.setId("2");
            info2.setName("武夷山风景游");
            info2.setEvaluateNum("55");
            info2.setPrice("1299");
            info2.setAuthorName("Jaswe");
            info2.setLogoUrls(logoMap.get(2));
            pickedLocationList.add(info2);
            ProductInfo info3 = new ProductInfo();
            info3.setId("3");
            info3.setName("福建土家楼无限光景");
            info3.setEvaluateNum("89");
            info3.setPrice("5666");
            info3.setAuthorName("Savite");
            info3.setLogoUrls(logoMap.get(3));
            pickedLocationList.add(info3);
            ProductInfo info4 = new ProductInfo();
            info4.setId("4");
            info4.setName("福建阿里山游玩");
            info4.setEvaluateNum("340");
            info4.setPrice("7765");
            info4.setAuthorName("LasTis");
            info4.setLogoUrls(logoMap.get(4));
            pickedLocationList.add(info4);
            ProductInfo info5 = new ProductInfo();
            info5.setId("5");
            info5.setName("福建长岛十日游");
            info5.setEvaluateNum("444");
            info5.setPrice("33");
            info5.setAuthorName("YouTuBe");
            info5.setLogoUrls(logoMap.get(5));
            pickedLocationList.add(info5);
        }
        else if ("云南".equals(locationName))
        {
            /*
             * 云南的全部产品  5个
             */
            ProductInfo info1 = new ProductInfo();
            info1.setId("1");
            info1.setName("云南昆明旅游");
            // 产品评价数量
            info1.setEvaluateNum("66");
            info1.setPrice("4433");
            info1.setAuthorName("Jimmy");
            info1.setLogoUrls(logoMap.get(1));
            pickedLocationList.add(info1);
            ProductInfo info2 = new ProductInfo();
            info2.setId("2");
            info2.setName("云南大理八日游");
            info2.setEvaluateNum("12");
            info2.setPrice("12901");
            info2.setAuthorName("Erica");
            info2.setLogoUrls(logoMap.get(2));
            pickedLocationList.add(info2);
            ProductInfo info3 = new ProductInfo();
            info3.setId("3");
            info3.setName("云南西双版纳风景区");
            info3.setEvaluateNum("9998");
            info3.setPrice("12001");
            info3.setAuthorName("WarTree");
            info3.setLogoUrls(logoMap.get(3));
            pickedLocationList.add(info3);
            ProductInfo info4 = new ProductInfo();
            info4.setId("4");
            info4.setName("云南傣家两日游");
            info4.setEvaluateNum("110");
            info4.setPrice("788");
            info4.setAuthorName("Typhone");
            info4.setLogoUrls(logoMap.get(4));
            pickedLocationList.add(info4);
            ProductInfo info5 = new ProductInfo();
            info5.setId("5");
            info5.setName("云南丽江历险");
            info5.setEvaluateNum("888");
            info5.setPrice("65535");
            info5.setAuthorName("TuDou");
            info5.setLogoUrls(logoMap.get(5));
            pickedLocationList.add(info5);
        }
        else if ("西安".equals(locationName))
        {
            /*
             * 西安的全部产品  5个
             */
            ProductInfo info1 = new ProductInfo();
            info1.setId("1");
            info1.setName("西安七日游");
            // 产品评价数量
            info1.setEvaluateNum("66");
            info1.setPrice("4433");
            info1.setAuthorName("Jimmy");
            info1.setLogoUrls(logoMap.get(1));
            pickedLocationList.add(info1);
            ProductInfo info2 = new ProductInfo();
            info2.setId("2");
            info2.setName("西安兵马俑游");
            info2.setEvaluateNum("12");
            info2.setPrice("12901");
            info2.setAuthorName("Erica");
            info2.setLogoUrls(logoMap.get(2));
            pickedLocationList.add(info2);
            ProductInfo info3 = new ProductInfo();
            info3.setId("3");
            info3.setName("西安酒楼住宿");
            info3.setEvaluateNum("9998");
            info3.setPrice("12001");
            info3.setAuthorName("WarTree");
            info3.setLogoUrls(logoMap.get(3));
            pickedLocationList.add(info3);
            ProductInfo info4 = new ProductInfo();
            info4.setId("4");
            info4.setName("西安人家探险");
            info4.setEvaluateNum("110");
            info4.setPrice("788");
            info4.setAuthorName("Typhone");
            info4.setLogoUrls(logoMap.get(4));
            pickedLocationList.add(info4);
            ProductInfo info5 = new ProductInfo();
            info5.setId("5");
            info5.setName("西安崖谷赶尸历险");
            info5.setEvaluateNum("888");
            info5.setPrice("65535");
            info5.setAuthorName("TuDou");
            info5.setLogoUrls(logoMap.get(5));
            pickedLocationList.add(info5);
        }
    }

    /**
     * 获取网络图片
     */
    private void initLogoList(String locationName)
    {
        logoMap = new HashMap<Integer, ArrayList<String>>();
        if ("福建".equals(locationName))
        {
            ArrayList<String> logo1 = new ArrayList<String>();
            logo1.add("http://img.uutuu.com/data3/a/ph/large/080112/1acad9cc501178a0195e5b3981b92ae5.jpg");
            logo1.add("http://img.uutuu.com/data3/a/ph/large/080112/1acad9cc501178a0195e5b3981b92ae5.jpg");
            logoMap.put(1, logo1);
            ArrayList<String> logo2 = new ArrayList<String>();
            logo2.add("http://www.fansimg.com/uploads2012/02/userid219491time20120201134815.jpg");
            logo2.add("http://www.fansimg.com/uploads2012/02/userid219491time20120201134815.jpg");
            logoMap.put(2, logo2);
            ArrayList<String> logo3 = new ArrayList<String>();
            logo3.add("http://www.fj.xinhuanet.com/ntp/2008-08/28/xinsrc_1530805281636328268248.jpg");
            logo3.add("http://www.fj.xinhuanet.com/ntp/2008-08/28/xinsrc_1530805281636328268248.jpg");
            logoMap.put(3, logo3);
            ArrayList<String> logo4 = new ArrayList<String>();
            logo4.add("http://img7.uutuu.com/data7/a/ph/large/091110/cf7d23564a47f12c39b4494c2fbf900d.jpg");
            logo4.add("http://img7.uutuu.com/data7/a/ph/large/091110/cf7d23564a47f12c39b4494c2fbf900d.jpg");
            logoMap.put(4, logo4);
            ArrayList<String> logo5 = new ArrayList<String>();
            logo5.add("http://y1.ifengimg.com/weather_spider/dci_2013/02/20bfb49e8e39e4b9c3068095c6e344fc.jpg");
            logo5.add("http://y1.ifengimg.com/weather_spider/dci_2013/02/20bfb49e8e39e4b9c3068095c6e344fc.jpg");
            logoMap.put(5, logo5);
        }
        else if ("云南".equals(locationName))
        {
            ArrayList<String> logo1 = new ArrayList<String>();
            logo1.add("http://www.51766.com/img/shjinhua/1275457284285.jpg");
            logo1.add("http://www.51766.com/img/shjinhua/1275457284285.jpg");
            logoMap.put(1, logo1);
            ArrayList<String> logo2 = new ArrayList<String>();
            logo2.add("http://pic23.nipic.com/20120826/10698203_201354687000_2.jpg");
            logo2.add("http://pic23.nipic.com/20120826/10698203_201354687000_2.jpg");
            logoMap.put(2, logo2);
            ArrayList<String> logo3 = new ArrayList<String>();
            logo3.add("http://pic21.nipic.com/20120510/9434618_202948332108_2.jpg");
            logo3.add("http://pic21.nipic.com/20120510/9434618_202948332108_2.jpg");
            logoMap.put(3, logo3);
            ArrayList<String> logo4 = new ArrayList<String>();
            logo4.add("http://www.luobou.com/zhuti/UploadPic/2013-8/201383132110237.jpg");
            logo4.add("http://www.luobou.com/zhuti/UploadPic/2013-8/201383132110237.jpg");
            logoMap.put(4, logo4);
            ArrayList<String> logo5 = new ArrayList<String>();
            logo5.add("http://pic15.nipic.com/20110726/7083362_172749259132_2.jpg");
            logo5.add("http://pic15.nipic.com/20110726/7083362_172749259132_2.jpg");
            logoMap.put(5, logo5);
        }
        else if ("西安".equals(locationName))
        {
            ArrayList<String> logo1 = new ArrayList<String>();
            logo1.add("http://article.sh51766.com/img_travel/201241317446265.jpg");
            logo1.add("http://article.sh51766.com/img_travel/201241317446265.jpg");
            logoMap.put(1, logo1);
            ArrayList<String> logo2 = new ArrayList<String>();
            logo2.add("http://pic23.nipic.com/20120917/5682333_203830871000_2.jpg");
            logo2.add("http://pic23.nipic.com/20120917/5682333_203830871000_2.jpg");
            logoMap.put(2, logo2);
            ArrayList<String> logo3 = new ArrayList<String>();
            logo3.add("http://a3.att.hudong.com/07/58/01300000239313123039585345404.jpg");
            logo3.add("http://a3.att.hudong.com/07/58/01300000239313123039585345404.jpg");
            logoMap.put(3, logo3);
            ArrayList<String> logo4 = new ArrayList<String>();
            logo4.add("http://hanxu1001.cl16.xunbiz.net/uploadfile/20120411/20124111611571114.jpg");
            logo4.add("http://hanxu1001.cl16.xunbiz.net/uploadfile/20120411/20124111611571114.jpg");
            logoMap.put(4, logo4);
            ArrayList<String> logo5 = new ArrayList<String>();
            logo5.add("http://www.itravelqq.com/uploadfile/2009/1116/20091116092558354.jpg");
            logo5.add("http://www.itravelqq.com/uploadfile/2009/1116/20091116092558354.jpg");
            logoMap.put(5, logo5);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (pickedEditText.hasFocus)
        {
            if (s.length() > 0)
            {
                pickedEditText.setCompoundDrawables(pickedEditText.getCompoundDrawables()[0],
                        pickedEditText.getCompoundDrawables()[1], pickedEditText.mdrawable,
                        pickedEditText.getCompoundDrawables()[3]);
            }
            else
            {
                pickedEditText.setCompoundDrawables(pickedEditText.getCompoundDrawables()[0],
                        pickedEditText.getCompoundDrawables()[1], null, pickedEditText.getCompoundDrawables()[3]);
            }
        }
        else
        {
            pickedEditText.setCompoundDrawables(pickedEditText.getCompoundDrawables()[0],
                    pickedEditText.getCompoundDrawables()[1], null, pickedEditText.getCompoundDrawables()[3]);
        }
    }
}
