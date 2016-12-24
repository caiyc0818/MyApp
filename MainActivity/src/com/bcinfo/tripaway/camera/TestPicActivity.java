package com.bcinfo.tripaway.camera;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 
 * @classname TestPicActivity
 * 
 * 
 * @description 手机本地相册选择页面
 */
public class TestPicActivity extends BaseActivity implements OnClickListener
{
    private List<ImageBucket> dataList;
    
    private GridView gridView;
    
    private ImageBucketAdapter adapter;// 自定义的适配器
    
    private AlbumHelper helper;
    
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    
    // public static Bitmap bimap;
    
    private TextView backBtn;
    
    private int openType = 0;
    
    private int count = 0;
   
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bucket);
        helper = new AlbumHelper();
        helper.init(getApplicationContext());
        initData();
        initView();
    }
    
    /**
     * 初始化数据
     */
    private void initData()
    {
        openType = getIntent().getIntExtra("type", 0);
        count = getIntent().getIntExtra("count", 0);
        Log.i("TestPicActivity", "openType-->" + openType);
        dataList = helper.getImagesBucketList(true);
        // bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
    }
    
    /**
     * 初始化view视图
     */
    protected void initView()
    {
        backBtn = (TextView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        gridView = (GridView)findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                // adapter.notifyDataSetChanged();
                Intent intent = new Intent(TestPicActivity.this, ImageGridActivity.class);
                intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST, (Serializable)dataList.get(position).getImageList());
                intent.putExtra("opentype", openType);
                intent.putExtra("count", count);
                startActivityForResult(intent, 1);
            }
        });
    }
    
    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        //super.onClick(view);
        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case 101:
                setResult(RESULT_OK, data);
                System.out.println("图片地址>>>>"+data);
                ArrayList<String>  lists = new ArrayList<String>();
               
                lists=data.getExtras().getStringArrayList("pathlist");
                System.out.println(lists);
                
                Intent intents = new Intent();
                intents.putStringArrayListExtra("qwer", lists);
                setResult(222, intents);
                
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
