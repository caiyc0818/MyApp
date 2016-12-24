package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 付款成功
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月29日 下午8:05:45
 */
public class PaidSuccessActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.paid_success_activity);
        setSecondTitle("确认详情");
        initProductInfo();
        initApplyExplain();
    }
    
    private void initProductInfo()
    {
        ProductInfo mProductInfo = (ProductInfo)getIntent().getSerializableExtra("product");
        ImageView productLogo = (ImageView)findViewById(R.id.product_logo);
        TextView productName = (TextView)findViewById(R.id.product_name);
        TextView productService = (TextView)findViewById(R.id.product_service_item);
        TextView productDate = (TextView)findViewById(R.id.product_end_date);
        LinearLayout productDateLayout = (LinearLayout)findViewById(R.id.product_end_date_layout);
        if (mProductInfo != null)
        {
            ArrayList<String> logoUrls = mProductInfo.getLogoUrls();
            if (logoUrls != null && logoUrls.size() > 0)
            {
                if (!StringUtils.isEmpty(logoUrls.get(0)))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + logoUrls.get(0), productLogo);
                }
            }
            productName.setText(mProductInfo.getName());
            productService.setText(mProductInfo.getService());
            productDate.setText(mProductInfo.getBuyDate());
            if (mProductInfo.getType() != null && mProductInfo.getType().equals("2"))
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
    }
    
    /**
     * 初始化产品订购说明
     */
    private void initApplyExplain()
    {
        TextView applyProductExplain = (TextView)findViewById(R.id.apply_explain);
        /********** 服务条款 *********/
        String explain1 = "可在“我的预订”查看预订情况，请保持关注";
        SpannableString spStr1 = new SpannableString(explain1);
        NoLineClickSpan clickSpan1 = new NoLineClickSpan(getResources().getColor(R.color.title_bg))
        {
            @Override
            public void onClick(View widget)
            {
                // TODO Auto-generated method stub
                // LogUtil.i(TAG, "onClick", "onClick");
                // Intent intent = new Intent(PaidSuccessActivity.this, DescriptionActivity.class);
                // intent.putExtra("title", "服务条款");
                // intent.putExtra("description", "服务条款");
                // startActivity(intent);
            }
        };
        spStr1.setSpan(clickSpan1, 3, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        applyProductExplain.append(spStr1);
        
        applyProductExplain.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
}
