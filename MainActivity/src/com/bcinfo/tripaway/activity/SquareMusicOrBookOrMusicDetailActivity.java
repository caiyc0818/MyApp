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
import com.bcinfo.tripaway.AppConfig;
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

public class SquareMusicOrBookOrMusicDetailActivity extends BaseActivity
		implements OnClickListener {
	private final static String TAG = "SquareMusicPublishedActivity";
	private TextView squareTitle;
	private TextView squarePublish;
	private TextView name1;
	private TextView name2;
	private TextView blogNavBack;

	private String uri;
	private int type;
	private TextView desc;
	private TextView blogPublish;
	private ImageView pic;
	private boolean isEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_music_detail);
		AppManager.getAppManager().addActivity(this);
		Intent it = getIntent();
		isEdit = it.getBooleanExtra("isEdit", true);
		initViews();
		type = it.getIntExtra("type", 1);
		if (type == 1) {
			squareTitle.setText("推荐音乐");
		}
		if (type == 2) {
			squareTitle.setText("推荐书籍");
			name1.setText("书籍名称");
			name2.setText("作者姓名");
		} else if (type == 3) {
			squareTitle.setText("推荐电影");
			name1.setText("电影名称");
			name2.setText("导演姓名");
		}
		if (!StringUtils.isEmpty(it.getStringExtra("name1"))) {
			name1.setText(it.getStringExtra("name1"));
		}
		if (!StringUtils.isEmpty(it.getStringExtra("name2"))) {
			name2.setText(it.getStringExtra("name2"));
		}
		if (!StringUtils.isEmpty(it.getStringExtra("desc"))) {
			desc.setText(it.getStringExtra("desc"));
		}
		if (!StringUtils.isEmpty(it.getStringExtra("img"))) {
			uri = it.getStringExtra("img");
			ImageLoader.getInstance().displayImage(Urls.imgHost + uri, pic,
					AppConfig.options(R.drawable.ic_launcher));
		} else if (!StringUtils.isEmpty(it.getStringExtra("uri"))) {
			uri = it.getStringExtra("uri");
			ImageLoader.getInstance().displayImage(
					"file://" + it.getStringExtra("uri"), pic);
		}

	}

	private void initViews() {
		squareTitle = (TextView) findViewById(R.id.blog_title);
		squarePublish = (TextView) findViewById(R.id.blog_publish);
		squarePublish.setText("编辑");
		if (!isEdit) {
			squarePublish.setVisibility(View.GONE);
		}
		blogNavBack = (TextView) findViewById(R.id.blog_navBack);
		desc = (TextView) findViewById(R.id.desc);
		blogNavBack.setOnClickListener(this);
		name1 = (TextView) findViewById(R.id.name1);
		name2 = (TextView) findViewById(R.id.name2);
		pic = (ImageView) findViewById(R.id.pic);
		squarePublish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.blog_navBack:
			finish();
			activityAnimationClose();
			break;
		case R.id.blog_publish:
			Intent data = new Intent(this,
					SquareMusicOrBookOrMusicAddActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("uri", uri);
			bundle.putBoolean("isEdit", isEdit);
			bundle.putInt("type", type);
			bundle.putString("desc", desc.getText().toString());
			bundle.putString("name1", name1.getText().toString());
			bundle.putString("name2", name2.getText().toString());
			data.putExtras(bundle);
			startActivityForResult(data, 1002);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				setResult(RESULT_OK, data);
				finish();
				activityAnimationClose();
				break;
			default:
				break;
			}
		}
	}

}
