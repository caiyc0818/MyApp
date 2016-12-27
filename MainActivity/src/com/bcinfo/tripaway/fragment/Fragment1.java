package com.bcinfo.tripaway.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.FixedSpeedScroller;
import com.bcinfo.tripaway.adapter.DragAdapter;
import com.bcinfo.tripaway.adapter.SquareFragmentPagerAdapter;
import com.bcinfo.tripaway.bean.Data;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.BaseTools;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.ColumnHorizontalScrollView;
import com.bcinfo.tripaway.view.MyGridView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 广场列表 fragment
 */
public class Fragment1 extends BaseFragment implements OnTabSelectListener {
    DragAdapter userAdapter;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private ImageView button_more_columns;
    private ViewPager mViewPager;
    private int mScreenWidth = 0;
    private int mItemWidth = 0;
    private int mScreenHigh = 0;
    private int columnSelectIndex = 0;
    private ArrayList<Data> userChannelList = new ArrayList<Data>();
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    int tag = 0;
    private ArrayList<TopicOrTag> tags = new ArrayList<>();
    private String tab1;
    private PopupWindow pw;
    private SquareFragmentPagerAdapter mAdapetr;
    RelativeLayout tab;
    private String[] mTitles  = new String[30];
    private SlidingTabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.squrelayout, null);
        mScreenWidth = BaseTools.getWindowsWidth(getActivity());
        mItemWidth = mScreenWidth / 5;
        mScreenHigh = BaseTools.getWindowsHigh(getActivity());
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);
        tab = (RelativeLayout) view.findViewById(R.id.tab);
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tl_4);
        tabLayout.setOnTabSelectListener(this);
        // 设置标题
        ((View) button_more_columns.getParent()).setOnClickListener(new OnClickListener() {
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw);
            Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw2);

            @Override
            public void onClick(View v) {
                if (tag % 2 == 0) {
                   pw.showAsDropDown(tabLayout);
                    anim.setFillAfter(true);
                    button_more_columns.startAnimation(anim);
                }
                if (tag % 2 == 1) {
                    pw.dismiss();
                }
                tag++;
            }
        });
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        initView(view);
        setChangelView();
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        statisticsTitle = "广场";
    }

    /**
     * 初始化布局
     */
    private void initView(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.square_table_item_popuwindow, null);
        pw = new PopupWindow(contentView, mScreenWidth, (mScreenHigh - DensityUtil.dip2px(getActivity(), 102)));
        pw.setTouchable(true);
        pw.setFocusable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw2);
                // TODO Auto-generated method stub
                anim2.setFillAfter(true);
                button_more_columns.startAnimation(anim2);
                backgroundAlpha(1f);

            }

            public void backgroundAlpha(float bgAlpha) {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = bgAlpha; // 0.0-1.0
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pw.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }

        });
        pw.getContentView().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                pw.setFocusable(false);
                pw.dismiss();
                tag = 0;
                return true;
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        pw.setAnimationStyle(R.style.pw_anim_style);
        // 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
        MyGridView userGridView = (MyGridView) contentView.findViewById(R.id.userGridView);
        userAdapter = new DragAdapter(getActivity(), userChannelList);
        userGridView.setAdapter(userAdapter);
        // 设置GRIDVIEW的ITEM的点击监听
        userGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                if (!StringUtils.isEmpty(userChannelList.get(position).tab)) {
//                    selectTab(position);
                    tabLayout.setCurrentTab(position);
                    mViewPager.setCurrentItem(position);
                    userChannelList.get(position).isCheck = true;
                    for (int i = 0; i < userChannelList.size(); i++) {
                        if (i != position)
                            userChannelList.get(i).isCheck = false;
                    }
                    userAdapter.notifyDataSetChanged();
                    pw.dismiss();
                    tag = 0;
                } else {
                    pw.dismiss();
                }

            }

        });
    }

    private void setChangelView() {
        initColumnData();

    }

    private void initColumnData() {
        /*
		 * 标题
		 */
        tab1 = "全部";
        RequestParams params = new RequestParams();
        params.put("mcode", "sale.square.tag");

        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String code = response.optString("code");
                if ("00000".equals(code)) {
                    JSONArray dataArray = response.optJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject object = dataArray.optJSONObject(i);
                            JSONObject tagobject = object.optJSONObject("object");
                            if (tagobject == null)
                                continue;
                            TopicOrTag tag = new TopicOrTag();
                            if (!StringUtils.isEmpty(tagobject.optString("tagsId"))) {
                                tag.setId(tagobject.optString("tagsId"));
                                tag.setName(tagobject.optString("tagsName"));
                                tags.add(tag);
                            }
                        }
                    }
                    for (int i = 0; i < tags.size() + 1; i++) {
                        Data data = new Data();
                        if (i == 0) {
                            data.tab = tab1;
                            data.isCheck = true;
                            data.id = "all";
                        } else {
                            data.tab = tags.get(i - 1).getName();
                            data.id = tags.get(i - 1).getId();
                        }
                        userChannelList.add(data);
                    }
                    // 增加线条长度
                    if (userChannelList.size() % 3 == 1) {
                        Data data1 = new Data();
                        data1.id = "";
                        data1.tab = "";
                        Data data2 = new Data();
                        data2.id = "";
                        data2.tab = "";
                        userChannelList.add(data1);
                        userChannelList.add(data2);
                    } else if (userChannelList.size() % 3 == 2) {
                        Data data1 = new Data();
                        data1.id = "";
                        data1.tab = "";
                        userChannelList.add(data1);
                    }
                    for (int i = 0; i < userChannelList.size(); i++) {
                        mTitles[i] = userChannelList.get(i).tab;
                    }
                    initFragment();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void initFragment() {
        fragments.clear();//
        // 添加Fragment
        for (int i = 0; i < userChannelList.size(); i++) {
            if (!StringUtils.isEmpty(userChannelList.get(i).tab)) {
                Bundle data = new Bundle();
                data.putInt("type", i);
                data.putString("tabId", userChannelList.get(i).id);
                data.putSerializable("tabList", (Serializable) userChannelList);
                SqureFragment squarefragment = new SqureFragment();
                squarefragment.setArguments(data);
                fragments.add(squarefragment);
            }
        }
        mAdapetr = new SquareFragmentPagerAdapter(getChildFragmentManager(), fragments, mTitles);
        // 去除过度动画
        setViewPagerScrollSpeed();
        // 控制加载的页数
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0, true);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.addOnPageChangeListener(pageListener);
        mViewPager.setCurrentItem(0);
        tab.setVisibility(View.VISIBLE);
        tabLayout.setViewPager(mViewPager);
    }

    // 解决viewpager切换动画时间
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    /**
     * ViewPager
     */
    public OnPageChangeListener pageListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
//			selectTab(position);
            tabLayout.setCurrentTab(position);
            for (int i = 0; i < userChannelList.size(); i++) {
                if (i != position) {
                    userChannelList.get(i).isCheck = false;
                } else {
                    userChannelList.get(i).isCheck = true;
                }
            }
            userAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        // getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        pw.dismiss();
        tag = 0;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        pw.dismiss();
        tag = 0;
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {
        mViewPager.setCurrentItem(position);
        for (int i = 0; i < userChannelList.size(); i++) {
            if (i != position) {
                userChannelList.get(i).isCheck = false;
            } else {
                userChannelList.get(i).isCheck = true;
            }
        }
        userAdapter.notifyDataSetChanged();
    }
}

//	private void initTabColumn() {
//		tabLin.setVisibility(View.VISIBLE);
//		mRadioGroup_content.removeAllViews();
//		int count = userChannelList.size();
//		mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content);
//		for (int i = 0; i < count; i++) {
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.WRAP_CONTENT);
//			params.leftMargin = DensityUtil.dip2px(getActivity(), 17);
//			params.rightMargin = DensityUtil.dip2px(getActivity(), 13);
//			params.topMargin = DensityUtil.dip2px(getActivity(), 15);
//			params.bottomMargin = DensityUtil.dip2px(getActivity(), 15);
//			// TextView localTextView = (TextView)
//			// mInflater.inflate(R.layout.column_radio_item, null);
//			TextView columnTextView = new TextView(getActivity());
//			columnTextView.setTextAppearance(getActivity(), R.style.top_category_scroll_view_item_text);
//			// localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
//			// columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
//			columnTextView.setGravity(Gravity.CENTER);
//			// columnTextView.setMaxWidth(mItemWidth);
//			columnTextView.setSingleLine();
//			columnTextView.setId(i);
//			columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//			// columnTextView.setWidth(mItemWidth);
//			columnTextView.setTypeface(TripawayApplication.boldTf);
//			columnTextView.setText(userChannelList.get(i).tab);
//			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
//			if (columnSelectIndex == i) {
//				columnTextView.setSelected(true);
//			}
//			columnTextView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
//						View localView = mRadioGroup_content.getChildAt(i);
//						if (localView != v) {
//							localView.setSelected(false);
//							userChannelList.get(i).isCheck = false;
//						} else {
//							localView.setSelected(true);
//							mViewPager.setCurrentItem(i);
//							// userGridView.requestFocus();
//							// userGridView.setSelection(i);
//							userChannelList.get(i).isCheck = true;
//							if (tag % 2 == 1) {
//								pw.dismiss();
//							}
//							tag = 0;
//						}
//					}
//					userAdapter.notifyDataSetChanged();
//				}
//			});
//			mRadioGroup_content.addView(columnTextView, i, params);
//		}
//		initFragment();
//	}
//
//	/**
//	 * 选择的Column里面的Tab
//	 */
//	private void selectTab(int tab_postion) {
//		columnSelectIndex = tab_postion;
//		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
//			View checkView = mRadioGroup_content.getChildAt(tab_postion);
//			int k = checkView.getMeasuredWidth();
//			int l = checkView.getLeft();
//			int i2 = l + k / 2 - mScreenWidth / 2;
//			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
//		}
//		// 判断是否选中
//		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
//			View checkView = mRadioGroup_content.getChildAt(j);
//			boolean ischeck;
//			if (j == tab_postion) {
//				ischeck = true;
//			} else {
//				ischeck = false;
//			}
//			checkView.setSelected(ischeck);
//		}
//
//	}