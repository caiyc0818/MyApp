package com.bcinfo.tripaway.view.emoji;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bcinfo.tripaway.R;

public class FaceAdapter extends BaseAdapter
{
    
    private List<Emojicon> data;
    
    private LayoutInflater inflater;
    
    private int size = 0;
    
    public FaceAdapter(Context context, List<Emojicon> list)
    {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.size = list.size();
    }
    
    @Override
    public int getCount()
    {
        return this.size;
    }
    
    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Emojicon emoji = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.face_item, null);
            viewHolder.iv_face = (EmojiconTextView)convertView.findViewById(R.id.face_iv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.iv_face.setText(emoji.getEmoji());
        return convertView;
    }
    
    class ViewHolder
    {
        
        public EmojiconTextView iv_face;
    }
}