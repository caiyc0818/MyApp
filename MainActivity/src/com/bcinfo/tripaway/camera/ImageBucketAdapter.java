package com.bcinfo.tripaway.camera;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageBucketAdapter extends BaseAdapter
{
    final String TAG = getClass().getSimpleName();
    
    private Activity act;
    
    /**
     * 图片集列表
     */
    private List<ImageBucket> dataList;
    
    public ImageBucketAdapter(Activity act, List<ImageBucket> list)
    {
        this.act = act;
        dataList = list;
        // cache = BitmapManager.getBitmapManager();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        int count = 0;
        if (dataList != null)
        {
            count = dataList.size();
        }
        return count;
    }
    
    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return arg0;
    }
    
    class Holder
    {
        private ImageView iv;
        
        private ImageView selected;
        
        private TextView name;
        
        private TextView count;
    }
    
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2)
    {
        // TODO Auto-generated method stub
        Holder holder;
        if (arg1 == null)
        {
            holder = new Holder();
            arg1 = View.inflate(act, R.layout.item_image_bucket, null);
            holder.iv = (ImageView)arg1.findViewById(R.id.image);
            holder.selected = (ImageView)arg1.findViewById(R.id.isselected);
            holder.name = (TextView)arg1.findViewById(R.id.name);
            holder.count = (TextView)arg1.findViewById(R.id.count);
            arg1.setTag(holder);
        }
        else
        {
            holder = (Holder)arg1.getTag();
        }
        ImageBucket item = dataList.get(arg0);
        holder.count.setText("" + item.getCount());
        holder.name.setText(item.getBucketName());
        holder.selected.setVisibility(View.GONE);
        if (item.getImageList() != null && item.getImageList().size() > 0)
        {
            String thumbPath = item.getImageList().get(0).getThumbnailPath();
            String sourcePath = item.getImageList().get(0).getImagePath();
            holder.iv.setTag(sourcePath);
            // cache.displayBmp(holder.iv, thumbPath, sourcePath, callback);
            ImageLoader.getInstance().displayImage("file:///" + sourcePath, holder.iv);
        }
        else
        {
            holder.iv.setImageBitmap(null);
        }
        return arg1;
    }
}
