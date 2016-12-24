package com.bcinfo.tripaway.camera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.camera.ImageGridAdapter.TextCallback;

public class ImageGridActivity extends Activity implements OnClickListener
{
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    
    private List<ImageItem> dataList;
    
    private GridView gridView;
    
    private ImageGridAdapter adapter;
    
    private Button bt;
    
    private TextView backBtn;
    
    private LinearLayout saveLayout;
    
    private int openType;
    
    private int count = 0;
    
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择30张图片", 400).show();
                    break;
                
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_image_grid);
        dataList = (List<ImageItem>)getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
        openType = getIntent().getIntExtra("opentype", 0);
        count = getIntent().getIntExtra("count", 0);
        initView();
    }
    
    private void initView()
    {
        saveLayout = (LinearLayout)findViewById(R.id.save);
        if (openType == 0)
        {
            saveLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            saveLayout.setVisibility(View.GONE);
        }
        
        bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(this);
        backBtn = (TextView)findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler, count);
        adapter.setType(openType);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new TextCallback()
        {
            public void onListen(int count)
            {
                bt.setText("完成" + "(" + count + ")");
            }
        });
        
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                adapter.notifyDataSetChanged();
            }
        });
        
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.bt:
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.getMap().values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();)
                {
                    list.add(it.next());
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("pathlist", list);
                setResult(101, intent);
                finish();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
