package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 申请预订
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月29日 下午4:17:43
 */
public class ApplyProductActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "ApplyProductActivity";
    
    private ProductInfo mProductInfo;
    
    private TextView beginDateText;
    
    private TextView endDateText;
    
    private ImageView subtractTouristButton;
    
    private ImageView addTouristButton;
    
    private TextView applyTouristNumber;
    
    private int touristNumber = 2;
    
    private int mBeginYear = 0;
    
    private int mBeginMonth = 0;
    
    private int mBeginDay = 0;
    
    private int mEndYear = 0;
    
    private int mEndMonth = 0;
    
    private int mEndDay = 0;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.apply_product_activity);
        setSecondTitle("申请预订");
        beginDateText = (TextView)findViewById(R.id.apply_date_begin);
        endDateText = (TextView)findViewById(R.id.apply_date_end);
        LinearLayout layoutBeginDate = (LinearLayout)findViewById(R.id.layout_apply_date_begin);
        LinearLayout layoutEndDate = (LinearLayout)findViewById(R.id.layout_apply_date_end);
        subtractTouristButton = (ImageView)findViewById(R.id.apply_tourist_subtract_button);
        addTouristButton = (ImageView)findViewById(R.id.apply_tourist_add_button);
        applyTouristNumber = (TextView)findViewById(R.id.apply_tourist_number_text);
        TextView applyButton = (TextView)findViewById(R.id.apply_product_affirm_button);
        layoutBeginDate.setOnClickListener(this);
        layoutEndDate.setOnClickListener(this);
        addTouristButton.setOnClickListener(this);
        subtractTouristButton.setOnClickListener(this);
        applyButton.setOnClickListener(this);
        applyTouristNumber.setText(touristNumber + "位游客");
        initProductInfo();
        initDateTime();
        initApplyProductExplain();
    }
    
    private void initProductInfo()
    {
        mProductInfo = (ProductInfo)getIntent().getSerializableExtra("product");
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
                    ImageLoader.getInstance().displayImage(logoUrls.get(0), productLogo);
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
     * 初始化产品预订说明
     */
    private void initApplyProductExplain()
    {
        TextView applyProductExplain = (TextView)findViewById(R.id.apply_product_explain);
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
                Intent intent = new Intent(ApplyProductActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "服务条款");
                intent.putExtra("description", "服务条款");
                startActivity(intent);
                activityAnimationOpen();
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
                Intent intent = new Intent(ApplyProductActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "退订政策");
                intent.putExtra("description", "退订政策");
                startActivity(intent);
                activityAnimationOpen();
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
                Intent intent = new Intent(ApplyProductActivity.this, DescriptionActivity.class);
                intent.putExtra("title", "游客退款政策");
                intent.putExtra("description", "游客退款政策");
                startActivity(intent);
                activityAnimationOpen();
            }
        };
        spStr3.setSpan(clickSpan3, 0, explain3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        applyProductExplain.append(spStr3);
        applyProductExplain.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.apply_tourist_add_button:
                if (touristNumber < 5)
                {
                    touristNumber++;
                    applyTouristNumber.setText(touristNumber + "位游客");
                    subtractTouristButton.setImageResource(R.drawable.search_choosepeople_subtract);
                    if (touristNumber == 5)
                    {
                        addTouristButton.setImageResource(R.drawable.search_choosepeople_addover);
                    }
                }
                break;
            case R.id.apply_tourist_subtract_button:
                if (touristNumber > 1)
                {
                    touristNumber--;
                    applyTouristNumber.setText(touristNumber + "位游客");
                    addTouristButton.setImageResource(R.drawable.search_choosepeople_add);
                    if (touristNumber == 1)
                    {
                        subtractTouristButton.setImageResource(R.drawable.search_choosepeople_subtractover);
                    }
                }
                break;
            case R.id.layout_apply_date_begin:
                DatePickerDialog beginDatePickerDialog =
                    new DatePickerDialog(this, mBeginDateSetListener, mBeginYear, mBeginMonth, mBeginDay);
                beginDatePickerDialog.updateDate(mBeginYear, mBeginMonth, mBeginDay);
                beginDatePickerDialog.show();
                break;
            case R.id.layout_apply_date_end:
                DatePickerDialog endDatePickerDialog =
                    new DatePickerDialog(this, mEndDateSetListener, mEndYear, mEndMonth, mEndDay);
                endDatePickerDialog.updateDate(mEndYear, mEndMonth, mEndDay);
                endDatePickerDialog.show();
                break;
            case R.id.apply_product_affirm_button:
                intent = new Intent(this, PaidSuccessActivity.class);
                intent.putExtra("product", mProductInfo);
                startActivity(intent);
                activityAnimationOpen();
                break;
            default:
                break;
        }
    }
    
    /**
     * 初始化日期
     */
    private void initDateTime()
    {
        final Calendar c = Calendar.getInstance();
        mBeginYear = c.get(Calendar.YEAR);
        mBeginMonth = c.get(Calendar.MONTH);
        mBeginDay = c.get(Calendar.DAY_OF_MONTH);
        mEndYear = c.get(Calendar.YEAR);
        mEndMonth = c.get(Calendar.MONTH);
        mEndDay = c.get(Calendar.DAY_OF_MONTH);
        updateBeginDateDisplay();
        updateEndDateDisplay();
    }
    
    /**
     * 更新开始日期显示
     */
    private void updateBeginDateDisplay()
    {
        StringBuilder beginBuilder = new StringBuilder();
        beginBuilder.append(mBeginYear);
        beginBuilder.append("-");
        beginBuilder.append((mBeginMonth + 1) < 10 ? "0" + (mBeginMonth + 1) : (mBeginMonth + 1));
        beginBuilder.append("-");
        beginBuilder.append((mBeginDay < 10) ? "0" + mBeginDay : mBeginDay);
        beginDateText.setText(beginBuilder);
    }
    
    /**
     * 更新结束日期显示
     */
    private void updateEndDateDisplay()
    {
        StringBuilder endBuilder = new StringBuilder();
        endBuilder.append(mEndYear);
        endBuilder.append("-");
        endBuilder.append((mEndMonth + 1) < 10 ? "0" + (mEndMonth + 1) : (mEndMonth + 1));
        endBuilder.append("-");
        endBuilder.append((mEndDay < 10) ? "0" + mEndDay : mEndDay);
        endDateText.setText(endBuilder);
    }
    
    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mBeginDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            mBeginYear = year;
            mBeginMonth = monthOfYear;
            mBeginDay = dayOfMonth;
            updateBeginDateDisplay();
        }
    };
    
    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            mEndYear = year;
            mEndMonth = monthOfYear;
            mEndDay = dayOfMonth;
            updateEndDateDisplay();
        }
    };
}
