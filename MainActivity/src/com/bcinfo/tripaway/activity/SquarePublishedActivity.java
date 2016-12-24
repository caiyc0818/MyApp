package com.bcinfo.tripaway.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextPaint;
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
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

public class SquarePublishedActivity extends BaseActivity implements
		OnClickListener {
	private final static String TAG = "SquarePublishedActivity";
	private ViewPager viewPager;
	private LinearLayout dotsLayout;
	private List<View> imageViews;
	private ArrayList<View> dots;

	private int count;
	private int currentItem = 0;

	private List<String> topicStrs;

	private int gap;

	private List<List<String>> strsList = new ArrayList<List<String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_published);
		AppManager.getAppManager().addActivity(this);

		initViews();

		gap = DensityUtil.dip2px(this, 13);
		topicStrs = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			if(i%2==0)
			topicStrs.add("#自定义" + (i + 1) + "#");
			else {
				topicStrs.add("#你说这就是爱" + (i + 1) + "#");	
			}
		}
		getFlowText(topicStrs);
		addCarousel();
		
	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) findViewById(R.id.carousel_dots);
		EditText	editText = (EditText) findViewById(R.id.trip_edit_photoDescript);
		editText.setText("12345666");
		Editable text=editText.getEditableText();
		int textSize=(int)editText.getTextSize();
		text.setSpan(new EmojiconSpan(this, R.drawable.square_insert_movie, textSize, textSize),
                0,
                0 + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		text.setSpan(new EmojiconSpan(this, R.drawable.square_insert_music, textSize, textSize),
                2,
                0 + 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void addCarousel() {
		// count = tempList.size();
		imageViews = new ArrayList<View>();
		if (strsList == null || strsList.size() == 0) {
			View imageView = LayoutInflater.from(this).inflate(
					R.layout.square_published_item, null);
			imageViews.add(imageView);
			// handler.postDelayed(runnable, 2000);
		} else {
			count = strsList.size();
			dots = new ArrayList<View>();
			if (dotsLayout != null) {
				dotsLayout.removeAllViews();
			}
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			for (int i = 0; i < count; i++) {
				List<String> strs = strsList.get(i);
				View imageView = LayoutInflater.from(this).inflate(
						R.layout.square_published_item, null);
				FlowLayout tags = (FlowLayout) imageView.findViewById(R.id.tags);
				tags.setTag(i);

				// TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
				ImageView dotsView = new ImageView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				if (i != count - 1)
					params.rightMargin = 30;
				dotsView.setLayoutParams(params);
				dotsView.setBackgroundResource(R.drawable.shape_less_gray_oval);
				if (0 == i) {
					dotsView.setBackgroundResource(R.drawable.shape_green_oval);
				}
				dotsLayout.addView(dotsView);
				dots.add(dotsView);
				imageViews.add(imageView);
				addFlowView(strs, tags);
			}
		}

		// TODO 设置填充ViewPager页面的适配器********当滚动主题有时再放开下面代码******
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				if (arg1 == strsList.size()) {

					return;
				}
				((ViewPager) arg0).removeView(imageViews.get(arg1));
			}

			@Override
			public void finishUpdate(View arg0) {
			}

			@Override
			public int getCount() {
				return imageViews.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == (arg1);
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
			}

			@Override
			public Object instantiateItem(View arg0, final int arg1) {
				// mListViews.get(arg1).setBackgroundResource(resId[arg1]);
				if (arg1 == imageViews.size()) {

					return null;
				}
				((ViewPager) arg0).addView(imageViews.get(arg1), 0);
				return imageViews.get(arg1);
			}

		});
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			// if (position == pushResourceList.size() ) {
			// Intent mainIntent = new Intent(LoadingActivity.this,
			// MainActivity.class);
			// startActivity(mainIntent);
			// activityAnimationOpen();
			// finish();
			// return;
			// }

			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.shape_less_gray_oval);
			dots.get(position).setBackgroundResource(
					R.drawable.shape_green_oval);
			oldPosition = position;

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	}

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = gap;
			params.topMargin = gap;
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void getFlowText(List<String> strs) {
		int w = screenWidth - gap;
		TextView newView = new TextView(this);
		newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		newView.setTypeface(TripawayApplication.normalTf);
		newView.setGravity(Gravity.CENTER);
		newView.setTextColor(Color.parseColor("#666666"));
		TextPaint textPaint = newView.getPaint();
		int lineWidth = 0;
		int lineNum = 0;
		List<String> strList = new ArrayList<String>();
		strsList.add(strList);
		for (int i = 0; i < strs.size(); i++) {
			String str = strs.get(i);
			int length = (int) textPaint.measureText(strs.get(i));
			if (lineWidth + gap + length > w) {

				if (lineNum == 3) {
					strList = new ArrayList<String>();
					strsList.add(strList);
					lineNum = 0;
				} else {
					++lineNum;
				}
				strList.add(str);
				lineWidth = gap + length;
			} else {
				strList.add(str);
				lineWidth += lineWidth + length + gap;
			}
		}
		System.out.print(22222);
	}
}
