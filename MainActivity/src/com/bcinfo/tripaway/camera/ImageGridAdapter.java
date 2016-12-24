package com.bcinfo.tripaway.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGridAdapter extends BaseAdapter
{
    private TextCallback textcallback = null;
    
    final String TAG = getClass().getSimpleName();
    
    private Activity act;
    
    private List<ImageItem> dataList;
    
    private Map<String, String> map = new TreeMap<String, String>();
    
    // private BitmapManager cache;
    
    private Handler mHandler;
    
    private int selectTotal = 0;
    
    private int type = 0;
    
    private int count = 0;
    
    public static interface TextCallback
    {
        public void onListen(int count);
    }
    
    public void setTextCallback(TextCallback listener)
    {
        textcallback = listener;
    }
    
    public Map<String, String> getMap()
    {
        return map;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler, int count)
    {
        this.act = act;
        dataList = list;
        this.mHandler = mHandler;
        this.count = count;
    }
    
    @Override
    public int getCount()
    {
        int count = 0;
        if (dataList != null)
        {
            count = dataList.size();
        }
        return count;
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    class Holder
    {
        private ImageView iv;
        
        private ImageView selected;
        
        private TextView text;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Holder holder;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = (ImageView)convertView.findViewById(R.id.image);
            holder.selected = (ImageView)convertView.findViewById(R.id.isselected);
            holder.text = (TextView)convertView.findViewById(R.id.item_image_grid_text);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        final ImageItem item = dataList.get(position);
        holder.iv.setTag(item.getImagePath());
        // cache.displayBmp(holder.iv, item.getThumbnailPath(), item.getImagePath(), callback);
        ImageLoader.getInstance().displayImage("file:///" + item.getImagePath(), holder.iv);
        holder.selected.setImageResource(0);
        if (item.isSelected())
        {
            holder.selected.setImageResource(R.drawable.icon_data_select);
            holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
        }
        else
        {
            holder.selected.setImageResource(0);
            holder.text.setBackgroundColor(0x00000000);
        }
        holder.iv.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String path = dataList.get(position).getImagePath();
                if ((count + selectTotal) < 30)
                {
                    item.setSelected(!item.isSelected());
                    if (item.isSelected())
                    {
                        holder.selected.setImageResource(R.drawable.icon_data_select);
                        holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
                        if (type == 1)
                        {
                            ArrayList<String> pathList = new ArrayList<String>();
                            pathList.add(path);
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("pathlist", pathList);
                            act.setResult(101, intent);
                            act.finish();
                        }
                        selectTotal++;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        Log.e("ImageGridAdapter", "path-->" + path);
                        map.put(path, path);
                    }
                    else if (!item.isSelected())
                    {
                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.remove(path);
                    }
                }
                else if ((count + selectTotal) >= 30)
                {
                    if (item.isSelected() == true)
                    {
                        item.setSelected(!item.isSelected());
                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        map.remove(path);
                    }
                    else
                    {
                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    }
                }
            }
        });
        return convertView;
    }
}
