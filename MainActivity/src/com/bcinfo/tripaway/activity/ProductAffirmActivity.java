package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AffirmListAdapter;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.AffirmListView;
import com.bcinfo.tripaway.view.dialog.ApplyDetailDialog;
import com.bcinfo.tripaway.view.dialog.ChangeProductStateDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 标准产品确认详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月19日 下午4:57:53
 */
public class ProductAffirmActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
    private static final String TAG = "ProductAffirmActivity";
    
    private AffirmListView mAffirmListView;
    
    private ArrayList<UserInfo> mAffirmDataList = new ArrayList<UserInfo>();
    
    private AffirmListAdapter mAffirmListAdapter;
    
    private TextView productStatus;
    
    private RelativeLayout productLayout;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.product_affirm_activity);
        setSecondTitle("确认详情");
        productLayout = (RelativeLayout)findViewById(R.id.product_layout);
        mAffirmListView = (AffirmListView)findViewById(R.id.product_listview);
        productStatus = (TextView)findViewById(R.id.product_status);
        productStatus.setOnClickListener(this);
        productLayout.setOnClickListener(this);
        initData();
    }
    
    private void initData()
    {
        Intent intent = getIntent();
        ProductInfo product = (ProductInfo)intent.getSerializableExtra("product");
        ImageView productLogo = (ImageView)findViewById(R.id.product_logo);
        TextView productName = (TextView)findViewById(R.id.product_name);
        TextView productService = (TextView)findViewById(R.id.product_service_item);
        TextView productDate = (TextView)findViewById(R.id.product_end_date);
        LinearLayout productDateLayout = (LinearLayout)findViewById(R.id.product_end_date_layout);
        if (product != null)
        {
            ArrayList<String> logoUrls = product.getLogoUrls();
            if (logoUrls != null && logoUrls.size() > 0)
            {
                if (!StringUtils.isEmpty(logoUrls.get(0)))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + logoUrls.get(0), productLogo);
                }
            }
            productName.setText(product.getName());
            productService.setText(product.getService());
            productDate.setText(product.getBuyDate());
            if (product.getType() != null && product.getType().equals("2"))
            {
                productDateLayout.setVisibility(View.VISIBLE);
                productService.setVisibility(View.GONE);
            }
            else
            {
                productDateLayout.setVisibility(View.GONE);
                productService.setVisibility(View.VISIBLE);
            }
        }
        initListView();
        LogUtil.i(TAG, "initData", "product=" + product);
    }
    
    private void initListView()
    {
        ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
        for (int i = 0; i < 6; i++)
        {
            UserInfo user = new UserInfo();
            // user.setName("奥巴马john");
            // user.setPhoto("http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1412/22/c3/985445_1419251530621_mthumb.jpg");
            // user.setLeaveWords("国防生轻轻巧巧");
            // if (i % 2 == 0)
            // {
            // user.setResponseState("已同意");
            // }
            // else
            // {
            // user.setResponseState("已拒绝");
            // }
            userList.add(user);
        }
        mAffirmDataList.addAll(userList);
        mAffirmListAdapter = new AffirmListAdapter(this, mAffirmDataList);
        mAffirmListView.setAdapter(mAffirmListAdapter);
        mAffirmListView.setOnItemClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.product_status:
                intent = new Intent(this, ChangeProductStateDialog.class);
                intent.putExtra("state", "issuet");
                startActivity(intent);
                break;
            case R.id.product_layout:
                ProductInfo product = (ProductInfo)getIntent().getSerializableExtra("product");
                if (!StringUtils.isEmpty(product.getType()))
                {
                    if (product.getType().endsWith("1"))
                    {
                        // intent = new Intent(this, ProductDetailActivity.class);
                        intent = new Intent(this, GrouponProductNewDetailActivity.class);
                    }
                    else
                    {
                        intent = new Intent(this, GrouponProductNewDetailActivity.class);
                    }
                    intent.putExtra("product", product);
                    intent.putExtra("productId", product.getId());
                    startActivity(intent);
                    activityAnimationOpen();
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        LogUtil.d(TAG, "onItemClick", "position=" + position);
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ApplyDetailDialog.class);
        startActivity(intent);
    }
}
