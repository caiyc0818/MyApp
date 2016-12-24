package com.bcinfo.tripaway.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.StringUtils;

public class ReplyDialog extends Dialog implements android.view.View.OnClickListener{

	private Context mContext;

	private LayoutInflater mInflater;

	private LayoutParams mLp;

	private OnSendReplyListener mSendReplyListener;

	private Button mSendBtn;
	
	private EditText mEditText;
	private TextView mTextView;

	public ReplyDialog(Context context, OnSendReplyListener cancelListener) {
		super(context, R.style.Dialog);
		this.mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = mInflater.inflate(R.layout.reply_dialog, null);
		setContentView(layout);
		// 设置window属性
		mLp = getWindow().getAttributes();
		mLp.gravity = Gravity.BOTTOM;
//		mLp.dimAmount = (float) 0.75; // 去背景遮盖
		mLp.alpha = 1.0f;
		mLp.width = LayoutParams.MATCH_PARENT;
		mSendReplyListener = cancelListener;
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(mLp);
		mTextView = (TextView) findViewById(R.id.relpaytoname);
		mEditText = (EditText) findViewById(R.id.comment_reply);
		mSendBtn = (Button) findViewById(R.id.reply_send);
		mEditText.setFocusable(true);
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus){
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});
		mSendBtn.setOnClickListener(this);
	}
	
	public ReplyDialog(Context context, OnSendReplyListener cancelListener,String content) {
		super(context, R.style.Dialog);
		this.mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = mInflater.inflate(R.layout.reply_dialog, null);
		setContentView(layout);
		// 设置window属性
		mLp = getWindow().getAttributes();
		mLp.gravity = Gravity.BOTTOM;
//		mLp.dimAmount = (float) 0.75; // 去背景遮盖
		mLp.alpha = 1.0f;
		mLp.width = LayoutParams.MATCH_PARENT;
		mSendReplyListener = cancelListener;
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(mLp);
		if(!StringUtils.isEmpty(content)){
			mTextView = (TextView) findViewById(R.id.relpaytoname);
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setText(content);
		}
		mEditText = (EditText) findViewById(R.id.comment_reply);
		mSendBtn = (Button) findViewById(R.id.reply_send);
		mEditText.setFocusable(true);
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus){
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});
		mSendBtn.setOnClickListener(this);
	}
		
	public interface OnSendReplyListener {
		public void sendReply(String comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reply_send:
			String comment = mEditText.getText().toString();
			mSendReplyListener.sendReply(comment);
			this.dismiss();
			break;
		default:
			break;
		}
	}

}
