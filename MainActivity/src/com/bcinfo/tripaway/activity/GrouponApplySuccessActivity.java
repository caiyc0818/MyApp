package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.view.dialog.GrouponApplaySendDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 团购产品申请成功
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月8日 下午6:27:39
 */
public class GrouponApplySuccessActivity extends BaseActivity implements OnClickListener
{
    private RoundImageView anthorPhoto;

    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.groupon_apply_success_activity);
        setSecondTitle("报名成功");
        anthorPhoto = (RoundImageView) findViewById(R.id.product_author_photo);
        TextView   affirmButton = (TextView) findViewById(R.id.apply_product_affirm_button);
        affirmButton.setOnClickListener(this);
        String url = "http://c.hiphotos.baidu.com/image/w%3D2048/sign=744a86ae0d3387449cc5287c6537d8f9/ac345982b2b7d0a28e9adc63caef76094a369af9.jpg";
        ImageLoader.getInstance().displayImage(url, anthorPhoto);
        initApplyGroupProductExplain();
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.apply_product_affirm_button:
                Intent intent=new Intent(this, GrouponApplaySendDialog.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    /**
     * 初始化产品预订说明
     */
    private void initApplyGroupProductExplain()
    {
        TextView applyProductExplain = (TextView) findViewById(R.id.apply_product_explain);
        /********** 服务条款 *********/
        String explain1 = "预订本房源，您即同意了支付右侧显示的总金额（此金额包含了小费），并同意服务条款、";
        
        SpannableString spStr1 = new SpannableString(explain1);
        NoLineClickSpan clickSpan1 = new NoLineClickSpan(getResources().getColor(R.color.event_red))
        {
            @Override
            public void onClick(View widget)
            {
                // TODO Auto-generated method stub
                // LogUtil.i(TAG, "onClick", "onClick");
                Intent intent = new Intent(GrouponApplySuccessActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "服务条款");
                intent.putExtra("description", "服务条款");
                startActivity(intent);
            }
        };
        spStr1.setSpan(clickSpan1, explain1.length() - 5, explain1.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        applyProductExplain.append(spStr1);
        /********** 退订政策 *********/
        String explain2 = "退订政策及";
        SpannableString spStr2 = new SpannableString(explain2);
        NoLineClickSpan clickSpan2 = new NoLineClickSpan(getResources().getColor(R.color.event_red))
        {
            @Override
            public void onClick(View widget)
            {
                // TODO Auto-generated method stub
                // LogUtil.i(TAG, "onClick", "onClick");
                Intent intent = new Intent(GrouponApplySuccessActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "退订政策");
                intent.putExtra("description", "退订政策");
                startActivity(intent);
            }
        };
        spStr2.setSpan(clickSpan2, 0, explain2.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        applyProductExplain.append(spStr2);
        /********** 游客退款政策 *********/
        String explain3 = "游客退款政策";
        SpannableString spStr3 = new SpannableString(explain3);
        NoLineClickSpan clickSpan3 = new NoLineClickSpan(getResources().getColor(R.color.event_red))
        {
            @Override
            public void onClick(View widget)
            {
                // TODO Auto-generated method stub
                // LogUtil.i(TAG, "onClick", "onClick");
                Intent intent = new Intent(GrouponApplySuccessActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "游客退款政策");
                intent.putExtra("description", "游客退款政策");
                startActivity(intent);
            }
        };
        spStr3.setSpan(clickSpan3, 0, explain3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        applyProductExplain.append(spStr3);
        applyProductExplain.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
