package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.LogUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 *结伴列表适配
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年11月29日 上午10:04:40
 */
public class ThemeListAdapter extends BaseAdapter
{
    private static final String TAG = "ThemeListAdapter";
    private Context mContext;
    private ArrayList<String> mItemList;
    private ArrayList<String> selectThemeList = new ArrayList<String>();
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();

    public ThemeListAdapter(Context context, ArrayList<String> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mItemList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    public ArrayList<String> getSelectThemeList()
    {
        return selectThemeList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewMap.get(position) == null) 
        {
            convertView = inflater.inflate(R.layout.theme_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            viewMap.put(position, convertView);
        }
        else
        {
            convertView=viewMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.themeName = (CheckBox) convertView.findViewById(R.id.theme_name);
        holder.themeName.setText(mItemList.get(position));

        holder.themeName.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // TODO Auto-generated method stub
                int color = Color.GRAY;
                if (isChecked)
                {
                    color = mContext.getResources().getColor(R.color.title_bg);
                    selectThemeList.add(buttonView.getText().toString());
                }
                else
                {
                    color = mContext.getResources().getColor(R.color.dark_gray);
                    selectThemeList.remove(buttonView.getText().toString());
                }
                buttonView.setTextColor(color);
            }
        });
        return convertView;
    }
    public class ViewHolder
    {
        public CheckBox themeName;
    }
}
