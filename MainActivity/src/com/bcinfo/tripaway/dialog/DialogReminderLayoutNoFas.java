package com.bcinfo.tripaway.dialog;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.view.MBProgressHUD.rotateProgressHUD;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogReminderLayoutNoFas extends AlertDialog implements android.view.View.OnClickListener
{

    private Context mContext;
    private AccountNoFasInterface accountUnbindInterface;
    private LinearLayout cancelLayout;// 取消
    private LinearLayout confirmLayout;// 确认
    private rotateProgressHUD rotateLoading;
    private boolean showingRotate;
    private String text;

    public DialogReminderLayoutNoFas(Context context, AccountNoFasInterface accountUnbindInterface)
    {
        super(context);
        this.mContext = context;
        this.accountUnbindInterface = accountUnbindInterface;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reminder_layout_fas_cashno);
        cancelLayout = (LinearLayout) findViewById(R.id.dialog_reminder_cancel); // 取消按钮
        confirmLayout = (LinearLayout) findViewById(R.id.dialog_reminder_confirm); // 确定按钮       
//        rotateLoading = (rotateProgressHUD) findViewById(R.id.rotate_loading_icon);
//        TextView dialogReminderText = (TextView)this.findViewById(R.id.dialog_reminder_text);
//        dialogReminderText.setText(text);
        cancelLayout.setOnClickListener(this);
        confirmLayout.setOnClickListener(this);
        showingRotate=false;

    }

    public interface AccountNoFasInterface
    {
        // 解绑操作的抽象方法 operateCode:0 取消 ;1 确认

        public void noOperate(int operateCode);
    }

//    public void setDailogText(String text)
//    {
//        TextView dialogReminderText = (TextView)this.findViewById(R.id.dialog_reminder_text);
//        if(dialogReminderText!=null){
//            dialogReminderText.setText(text);
//        }
//        this.text=text;
//    }

    public void showRotate(boolean isshow)
    {
        showingRotate = isshow;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.dialog_reminder_cancel: // 取消操作
            cancelLayout.setEnabled(false);
            confirmLayout.setEnabled(false);
            this.cancel();
            accountUnbindInterface.noOperate(0);
            break;
        case R.id.dialog_reminder_confirm: // 确认操作
//            if (showingRotate)
//            {
//                rotateLoading.setVisibility(View.VISIBLE);
//                rotateLoading.startAnimation();
//            }
            cancelLayout.setEnabled(false);
            confirmLayout.setEnabled(false);
            accountUnbindInterface.noOperate(1);
            break;
        default:
            break;
        }

    }
}
