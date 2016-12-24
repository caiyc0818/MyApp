package com.bcinfo.tripaway.view.pop;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.wefika.flowlayout.FlowLayout;

/**
 * 订单操作弹出框
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月25日 上午10:33:15
 */
@SuppressLint("WrongViewCast")
public class GoldFilterPopWindow extends PopupWindow implements OnClickListener
{
    private static final String TAG = "OrderPopWindow.java";
    
    private Context mContext;
    
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    
    private OperationListener mOperationListener;
    
    private String type;
    
    private String isGold;
    
	private CompoundButton currentView ;
	
	private RadioButton radioBtn;
	
	private FlowLayout flayout;
	
	private TextView submitLy;
    
    @SuppressWarnings("deprecation")
    public GoldFilterPopWindow(Context context, OperationListener listener,String isGold,String type)
    {
        this.mContext = context;
        this.type = type;
        this.isGold = isGold;
        mOperationListener = listener;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        // 设置弹窗的宽度和高度
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(mContext).inflate(R.layout.gold_filter, null);
        setContentView(view);
        flayout = (FlowLayout) view.findViewById(R.id.destination);
        radioBtn = (RadioButton) view.findViewById(R.id.gold_radio);
        submitLy = (TextView) view.findViewById(R.id.submit);
        submitLy.setOnClickListener(this);
        addPriceFlowView(flayout);
        initParams();
    }
    
    private void initParams(){
    	RadioButton btn = null;
    	if("all".equals(type)){
    		btn = (RadioButton)flayout.getChildAt(0);
    		btn.setChecked(true);
    	}else if("guide".equals(type)){
    		btn = (RadioButton)flayout.getChildAt(1);
    		btn.setChecked(true);
    	}else if("leader".equals(type)){
    		btn = (RadioButton)flayout.getChildAt(2);
    		btn.setChecked(true);
    	}else if("driver".equals(type)){
    		btn = (RadioButton)flayout.getChildAt(3);
    		btn.setChecked(true);
    	}
    }
    
    private void addPriceFlowView(FlowLayout flowLayout) {
		for (int i = 0; i < 4; i++) {
			RadioButton newView = new RadioButton(((Activity) mContext));
			newView.setBackgroundResource(R.drawable.square);
			if(i == 0){
				newView.setText("全部");
				newView.setTag("all");
			}else if(i == 1){
				newView.setText("导游");
				newView.setTag("guide");
			}else if(i == 2){
				newView.setText("领队");
				newView.setTag("leader");
			}else {
				newView.setText("司机");
				newView.setTag("driver");
			}
			
			newView.setButtonDrawable(android.R.color.transparent);
			
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.BLACK);
			
			newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				

				@Override
				public void onCheckedChanged(CompoundButton mCompoundButton, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						mCompoundButton.setTextColor(Color.WHITE);
//						type = (String) mCompoundButton.getTag();
						if(currentView==null){
							
						}else {
							currentView.setTextColor(Color.BLACK);
							currentView.setChecked(false);
						}
						currentView=mCompoundButton;
					}
				}
			});
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(((Activity) mContext), 80),
					DensityUtil.dip2px(((Activity) mContext), 29));
			params.rightMargin = DensityUtil.dip2px(((Activity) mContext), 5);
			params.bottomMargin = DensityUtil.dip2px(((Activity) mContext), 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}
    /**
     * 显示弹窗列表界面
     */
    public void show(final View view)
    {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        int x = 0;
        int y = mLocation[1] + view.getHeight()+ 20;
        // 显示弹窗的位置
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
        	case R.id.submit:
        		mOperationListener.queryMeb(type, isGold);
        		dismiss();
        		break;
            default:
                break;
        }
    }
    
    public interface OperationListener
    {
        public void queryMeb(String type,String isGold);
        
    }
}
