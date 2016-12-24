package com.bcinfo.tripaway.view.dialog;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.MyMicroBlogActivity;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyBlogDeleteDialog extends AlertDialog implements android.view.View.OnClickListener
{
    private LinearLayout deleteConfirmTv;// 确定按钮
    private LinearLayout deleteCancelTv;// 确定取消按钮
    private OnBlogDeleteListener onBlogDeleteListenr;
    private Context mContext;
    private String photoId;
    private Handler mHandler = new Handler();

    public MyBlogDeleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);

    }

    public MyBlogDeleteDialog(Context context, int theme)
    {
        super(context, theme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_blog_deleted_dialog);
        deleteConfirmTv = (LinearLayout) findViewById(R.id.my_blog_delDialog_confirm_layout);
        deleteCancelTv = (LinearLayout) findViewById(R.id.my_blog_delDialog_cancel_layout);
        this.getWindow().setGravity(Gravity.CENTER);
        deleteConfirmTv.setOnClickListener(this);
        deleteCancelTv.setOnClickListener(this);
    }

    public MyBlogDeleteDialog(Context context, OnBlogDeleteListener onBlogDeleteListenr, String photoId)
    {
        super(context);
        this.mContext = context;
        this.onBlogDeleteListenr = onBlogDeleteListenr;
        this.photoId = photoId;
    }

    public interface OnBlogDeleteListener
    {
        public void onDelete(String photoId); // 参数表示欲删除的微游记标识
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.my_blog_delDialog_confirm_layout:
            onBlogDeleteListenr.onDelete(photoId);
            dismiss();
            break;
        case R.id.my_blog_delDialog_cancel_layout:
            dismiss();
            break;
        default:
            break;
        }

    }
}
