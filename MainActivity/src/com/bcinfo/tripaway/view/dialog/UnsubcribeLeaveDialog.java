package com.bcinfo.tripaway.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;

/**
 * @author hanweipeng
 * @date 2015-7-11 下午12:09:46
 */
public class UnsubcribeLeaveDialog extends Dialog implements android.view.View.OnClickListener
{
    private LayoutParams lp;
    
    private EditText leaveEdt;
    
    private Button unsubcribeBtn;
    
    private ImageView closeBtn;
    
    private Context mContext;
    
    private ApplyUnsubcribeListener listener;
    
    public UnsubcribeLeaveDialog(Context context, ApplyUnsubcribeListener listener)
    {
        super(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.unsubscribe_leave_layout);
        mContext = context;
        this.listener = listener;
        lp = getWindow().getAttributes();
        lp.dimAmount = 0; // 去背景遮盖
        lp.alpha = 0.9f;
        getWindow().setAttributes(lp);
        leaveEdt = (EditText)findViewById(R.id.leave_txt);
        unsubcribeBtn = (Button)findViewById(R.id.confirm_unsubcribe);
        closeBtn = (ImageView)findViewById(R.id.close_btn);
        unsubcribeBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.close_btn:
                cancel();
                break;
            case R.id.confirm_unsubcribe:
                if (StringUtils.isEmpty(leaveEdt.getText().toString()))
                {
                    ToastUtil.showToast(mContext, "请填写退订理由");
                    return;
                }
                listener.onUnsubcribe(leaveEdt.getText().toString());
                cancel();
                break;
            default:
                break;
        }
    }
    
    public interface ApplyUnsubcribeListener
    {
        public void onUnsubcribe(String leaveWord);
    }
}
