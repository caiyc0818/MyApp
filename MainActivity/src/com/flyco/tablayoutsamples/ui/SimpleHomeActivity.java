package com.flyco.tablayoutsamples.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ant.liao.GifView;
import com.bcinfo.tripaway.R;
import com.flyco.tablayoutsamples.adapter.SimpleHomeAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class SimpleHomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private Context mContext = this;
    private final String[] mItems = {"SlidingTabLayout", "CommonTabLayout", "SegmentTabLayout"};
    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class,
            SegmentTabActivity.class};
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView lv;
    private GifView gf1;
    private SimpleHomeAdapter adapter;
    private View view;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    lv.removeHeaderView(view);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        lv = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swif);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//            }
//        });
        swipeRefreshLayout.setOnRefreshListener(this);
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setFadingEdgeLength(0);
        adapter = new SimpleHomeAdapter(mContext, mItems);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, mClasses[position]);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onRefresh() {

        lv.removeHeaderView(view);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.header_layout, null);
            GifView gf1 = (GifView) view.findViewById(R.id.gif2);
            // 设置Gif图片源
            gf1.setGifImage(R.drawable.zealeranim);
// 添加监听器
            gf1.setOnClickListener(this);
// 设置显示的大小，拉伸或者压缩
//            gf1.setShowDimension(setShowDimension);
// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
            gf1.setGifImageType(GifView.GifImageType.COVER);

        }
        if (lv.getHeaderViewsCount() == 0) {
            lv.addHeaderView(view);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handle.sendEmptyMessage(1);
                    }
                }).start();
            }
        };
        Timer time = new Timer();
        time.schedule(task, 3000);

    }

    @Override
    public void onClick(View v) {
        final Snackbar snackBar = Snackbar.make(lv, "是否撤销删除？", Snackbar.LENGTH_LONG);
        snackBar.setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
                lv.removeAllViews();
            }
        });
        snackBar.show();

    }
}
