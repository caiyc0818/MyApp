package com.bcinfo.tripaway.view.MBProgressHUD;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MyMBProgressHUD extends LinearLayout {
	/**
	 * animation of the MBProgressHUD
	 */
	private ProgressBar FooterBar;
	/**
	 * text of the MBProgressHUD
	 */
	private TextView TextOfFooterBar;

	public MyMBProgressHUD(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyMBProgressHUD(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		LinearLayout mContainer = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.trip_product_listview_footer, null);
		addView(mContainer, lp);

		FooterBar = (ProgressBar) findViewById(R.id.footerBar); // 旋转动画
		TextOfFooterBar = (TextView) findViewById(R.id.text_of_footerBar); // 选转动画后面的文字
	}

	/**
	 * 开始只有动画
	 */
	public void setStart() {
		FooterBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 开始动画和文字
	 */
	public void setStartWithText() {
		FooterBar.setVisibility(View.VISIBLE);
		TextOfFooterBar.setVisibility(View.VISIBLE);
	}

	public void setLoadSuccess() {
		FooterBar.setVisibility(View.GONE);
		TextOfFooterBar.setVisibility(View.GONE);
	}

	public void setLoadEmptyInfo() {
		FooterBar.setVisibility(View.GONE);
		TextOfFooterBar.setText("没有相关数据!");
	}

	public void setLoadEmptyInfoToast(boolean show) {
		FooterBar.setVisibility(View.GONE);
		if (show) {
			Toast.makeText(getContext(), "没有相关数据!", Toast.LENGTH_SHORT).show();
		}
	}

	public void setLoadFailed() {
		FooterBar.setVisibility(View.GONE);
		TextOfFooterBar.setText("加载失败!");
	}

	public void setLoadFailedToast(boolean show) {
		FooterBar.setVisibility(View.GONE);
		if (show) {
			Toast.makeText(getContext(), "加载失败!", Toast.LENGTH_SHORT).show();
		}
	}

	public void setTextVisible() {
		TextOfFooterBar.setVisibility(View.VISIBLE);
	}

	public void setTextGone() {
		TextOfFooterBar.setVisibility(View.GONE);
	}
}
