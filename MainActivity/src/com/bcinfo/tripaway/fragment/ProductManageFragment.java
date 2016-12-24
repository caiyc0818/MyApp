package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ProductAffirmActivity;
import com.bcinfo.tripaway.adapter.ProductListAdapter;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.ComListViewFooter;

/**
 * 我的产品
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月4日 上午10:55:30
 */
public class ProductManageFragment extends BaseFragment implements OnItemClickListener
{
    private static final String TAG = "ProductManageFragment";
    
    private ArrayList<ProductInfo> myProductArrList = new ArrayList<ProductInfo>();
    
    private ProductListAdapter myProductListAdapter = null;
    
    private ListView myProductListView;
    
    private ComListViewFooter mComListViewFooter;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View product = inflater.inflate(R.layout.product_manage_fragment, container, false);
        myProductListView = (ListView)product.findViewById(R.id.my_product_listview);
        mComListViewFooter = new ComListViewFooter(getActivity());
        myProductListView.addFooterView(mComListViewFooter);
        // Toast.makeText(getActivity(), "productListView", Toast.LENGTH_LONG).show();
        testListview();
        return product;
    }
    
    private void testListview()
    {
        for (int i = 0; i < 6; i++)
        {
            ProductInfo productInfo = new ProductInfo();
            if (i == 0)
            {
                productInfo.setName("澳洲西海岸八日游");
                productInfo.setAddress("澳大利亚悉尼");
            }
            if (i == 1)
            {
                productInfo.setName("撒哈拉大沙漠穿越五日游");
                productInfo.setAddress("撒哈拉大沙漠东北角");
            }
            if (i == 2)
            {
                productInfo.setName("海边小镇一日游");
                productInfo.setAddress("中国福建");
            }
            if (i == 3)
            {
                productInfo.setName("百莫大三角潜水五日游");
                productInfo.setAddress("百莫大三角");
            }
            if (i == 4)
            {
                productInfo.setName("喜马拉雅山看日落三日游");
                productInfo.setAddress("百莫大三角");
            }
            if (i == 5)
            {
                productInfo.setName("澳洲西海岸八日游捉鲨鱼");
            }
            productInfo.setOrderNumber("2" + i);
            productInfo.setService("SUV·六位旅客·行程规划·代订机票");
            productInfo.setDescription("大家好，我叫大锤，是个活泼开朗的西北大汉，来美国当总统已经5年了,对这里的人文，美食有独特的研究");
            productInfo.setBuyDate("2014-12-12");
            productInfo.setPrice("￥234,5");
            productInfo.setEvaluate("3.6");
            productInfo.setOfferExplain("1、自由活动的餐费，车费，景点门票费");
            productInfo.setTotalPople("整团12人");
            productInfo.setRecruitDate("11.03-11.08");
            productInfo.setExperienceDate("12.09-12.12");
            productInfo.setEvaluateNum("22条评论");
            productInfo.setAuthorName("steady");
            productInfo.setOrderState((i % 4 + 1) + "");
            productInfo.setAuthorPhotoUrl("http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1501/07/c5/1597923_1597923_1420634132548_mthumb.jpg");
            ArrayList<String> logoUrls = new ArrayList<String>();
            if (i == 0)
                logoUrls.add("http://pic24.nipic.com/20120925/4744012_220314990000_2.jpg");
            if (i == 1)
                logoUrls.add("http://www.loveq.cn/store/photo/423/796/423796/328954/1235287844929077825.jpg");
            if (i == 2)
                logoUrls.add("http://img3.3lian.com/2014/c1/51/d/34.jpg");
            if (i == 3)
                logoUrls.add("http://d.hiphotos.baidu.com/image/w%3D2048/sign=43b9711cd62a283443a6310b6f8dc8ea/adaf2edda3cc7cd9aef055d83b01213fb90e91d5.jpg");
            if (i == 4)
                logoUrls.add("http://img1.3lian.com/img2008/14/06/0281.jpg");
            logoUrls.add("http://pic25.nipic.com/20121119/6835836_115116793000_2.jpg");
            logoUrls.add("http://img3.redocn.com/20140604/Redocn_2014060222115144.jpg");
            productInfo.setLogoUrls(logoUrls);
            if (i % 3 == 0)
            {
                productInfo.setType("1");
                productInfo.setEnable(true);
            }
            else if (i % 3 == 1)
            {
                productInfo.setType("2");
                productInfo.setEnable(true);
            }
            else if (i % 3 == 2)
            {
                productInfo.setType("3");
                productInfo.setEnable(false);
            }
            myProductArrList.add(productInfo);
        }
        myProductListAdapter = new ProductListAdapter(getActivity(), myProductArrList);
        myProductListView.setAdapter(myProductListAdapter);
        myProductListView.setOnItemClickListener(this);
    }
    
    /**
     * 加载更多handler
     */
    private Handler loadMoreHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                Toast.makeText(getActivity(), "加载结束", Toast.LENGTH_LONG).show();
            }
        }
    };
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == parent.getAdapter().getCount() - 1)
        {
            return;
        }
        LogUtil.d(TAG, "onItemClick", "position=" + position);
        // TODO Auto-generated method stub
        ProductInfo product = (ProductInfo)parent.getAdapter().getItem(position);
        if (!product.isEnable())
        {
            // return true;
        }
        Intent intent = new Intent(getActivity(), ProductAffirmActivity.class);
        intent.putExtra("product", product);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
}
