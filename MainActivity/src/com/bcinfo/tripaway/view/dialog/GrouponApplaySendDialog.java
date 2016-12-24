package com.bcinfo.tripaway.view.dialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;

/**
 * 报名请求发送成功
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月8日 下午7:40:27
 */
public class GrouponApplaySendDialog extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "GrouponApplaySendDialog";
    private TextView chenkApplyStateText;

    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.groupon_applay_send_dialog);
        chenkApplyStateText = (TextView) findViewById(R.id.chenk_apply_state);
        TextView cancelButton = (TextView) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        SpannableString spStr = new SpannableString("可在“我的预订”查看报名情况，请保持关注");
        NoLineClickSpan clickSpan = new NoLineClickSpan(getResources().getColor(R.color.title_bg))
        {
            @Override
            public void onClick(View widget)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "onClick", "onClick");
            }
        };
        spStr.setSpan(clickSpan, 3, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        chenkApplyStateText.append(spStr);
        chenkApplyStateText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.cancel_button:
                finish();
                break;
            default:
                break;
        }
    }
}
