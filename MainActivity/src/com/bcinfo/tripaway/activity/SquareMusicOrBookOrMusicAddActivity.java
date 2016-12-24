package com.bcinfo.tripaway.activity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.ui.PhotoPreviewActivity;
import com.bcinfo.photoselector.ui.PhotoSelectorActivity;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.RichText.Type;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.TextStyleUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

public class SquareMusicOrBookOrMusicAddActivity extends BaseActivity implements
		OnClickListener {
	private final static String TAG = "SquareMusicPublishedActivity";
	private TextView squareTitle;
	private TextView squarePublish;
	private TextView name1;
	private TextView name2;
	private TextView blogNavBack;
	private TextView addPic;
	private RelativeLayout picLayout;
	private ImageView pic;
	private TextView modify;
	
	private String uri;
	private int type;
	private EditText name1Text;
	private TextView name2Text;
	private EditText desc;
	private TextView blogPublish;
	private boolean isEdit=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_published_music);
		AppManager.getAppManager().addActivity(this);
		initViews();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
		Intent it=getIntent();
		isEdit = it.getBooleanExtra("isEdit", false);
		type=it.getIntExtra("type", 1);
		if(type==1)
		{
			squareTitle.setText("推荐音乐");
			statisticsTitle="推荐音乐";
		}
		if(type==2)
		{
			squareTitle.setText("推荐书籍");
			name1.setText("书籍名称:");
			name2.setText("作者姓名:");
			statisticsTitle="推荐书籍";
		}
		else
			if(type==3)
			{
				squareTitle.setText("推荐电影");
				name1.setText("电影名称:");
				name2.setText("导演姓名:");
				statisticsTitle="推荐电影";
			}
		squarePublish.setText("确定");
		if(!StringUtils.isEmpty(it.getStringExtra("name1"))){
			name1Text.setText(it.getStringExtra("name1"));
		}
		if(!StringUtils.isEmpty(it.getStringExtra("name2"))){
			name2Text.setText(it.getStringExtra("name2"));
		}
		if(!StringUtils.isEmpty(it.getStringExtra("desc"))){
			desc.setText(it.getStringExtra("desc"));
		}
		uri=it.getStringExtra("uri");
			if(!StringUtils.isEmpty(it.getStringExtra("uri"))){
				addPic.setVisibility(View.GONE);
				picLayout.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						"file://" + it.getStringExtra("uri"), pic);
		}
			findViewById(R.id.the_microblog_title).getBackground().setAlpha(255);
	}

	private void initViews() {
		squareTitle = (TextView) findViewById(R.id.blog_title);
		squarePublish = (TextView) findViewById(R.id.blog_publish);
		blogNavBack = (TextView) findViewById(R.id.blog_navBack);
		addPic = (TextView) findViewById(R.id.add_pic);
		picLayout = (RelativeLayout) findViewById(R.id.pic_layout);
		pic = (ImageView) findViewById(R.id.pic);
		modify = (TextView) findViewById(R.id.modify);
		name1Text = (EditText) findViewById(R.id.name1_text);
		name2Text = (TextView) findViewById(R.id.name2_text);
		desc = (EditText) findViewById(R.id.desc);
		blogNavBack.setOnClickListener(this);
		addPic.setOnClickListener(this);
		modify.setOnClickListener(this);
		name1 = (TextView) findViewById(R.id.name1);
		name2 = (TextView) findViewById(R.id.name2);
		blogPublish = (TextView) findViewById(R.id.blog_publish);
		blogPublish.setOnClickListener(this);
	}	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
switch (v.getId()) {
case R.id.blog_navBack:
	finish();
	activityAnimationClose();
	break;
case R.id.add_pic:
case R.id.modify:
	List<PhotoModel> choosed = new ArrayList<PhotoModel>();
	Intent intent = new Intent(this,
			PhotoSelectorActivity.class);
	intent.putExtra(PhotoSelectorActivity.KEY_MAX, 50);
	Bundle picBundle = new Bundle();
	picBundle.putParcelableArrayList("selected",
			(ArrayList<? extends Parcelable>) choosed);
	picBundle.putBoolean("isMultiChoice", false);
	intent.putExtras(picBundle);
	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	startActivityForResult(intent, 1001);
	break;
case R.id.blog_publish:
	String name1Str=name1Text.getText().toString();
	String name2Str=name2Text.getText().toString();
	if(StringUtils.isEmpty(name1Str)||StringUtils.isEmpty(uri)){
	ToastUtil.showToast(this, name1.getText().toString().substring(0, 4)+"或"+ name1.getText().toString().substring(0, 2)+"封面不能为空");
	return;
}
//	if(StringUtils.isEmpty(name1Str)||StringUtils.isEmpty(name2Str)){
//		ToastUtil.showToast(this, name1.getText().toString().substring(0, 4)+"或"+name2.getText().toString().substring(0, 4)+"不能为空");
//		return;
//	}
//	if(StringUtils.isEmpty(uri)){
//		ToastUtil.showToast(this, "封面不能为空");
//		return;
//	}
//	if(StringUtils.isEmpty(desc.getText().toString())){
//		ToastUtil.showToast(this, "描述不能为空");
//		return;
//	}
	Intent data = new Intent();
	Bundle bundle = new Bundle();
	bundle.putSerializable("uri",uri);
	bundle.putInt("type", type);
	bundle.putBoolean("isEdit", isEdit);
	bundle.putString("desc", desc.getText().toString());
	bundle.putString("name1",name1Text.getText().toString());
	bundle.putString("name2", name2Text.getText().toString());
	data.putExtras(bundle);
	setResult(RESULT_OK, data);
	finish();
	activityAnimationClose();
	break;
default:
	break;
}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1001:
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				if(photos.size()>0){
					addPic.setVisibility(View.GONE);
					picLayout.setVisibility(View.VISIBLE);
					PhotoModel info=photos.get(0);
					uri=info.getOriginalPath();
					ImageLoader.getInstance().displayImage(
							"file://" + info.getOriginalPath(), pic);
				}
				break;

			default:
				break;
			}
		}
	}
}
