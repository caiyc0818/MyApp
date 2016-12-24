package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.TipsInfo;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.view.MyGridView;

/**
 * @author hanweipeng
 * @date 2015-6-19 下午2:33:46
 */
public class TipsAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<TipsInfo> tipContentInfos;
    
    private LayoutInflater inflater;
    
    public TipsAdapter(Context context, List<TipsInfo> tipContentInfos)
    {
        this.mContext = context;
        this.tipContentInfos = tipContentInfos;
        inflater = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return tipContentInfos.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return tipContentInfos.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_tips_layout, null);
            holder.title = (TextView)convertView.findViewById(R.id.tip_title);
            holder.hybirdTip = (MyGridView)convertView.findViewById(R.id.tip_gridview);
            holder.commonTip = (WebView)convertView.findViewById(R.id.tip_common);
            // holder.content = (TextView)convertView.findViewById(R.id.tip_content);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(tipContentInfos.get(position).getTitle());
        // holder.content.setText(tipContentInfos.get(position).getContent());
        holder.hybirdTip.setVisibility(View.GONE);
        holder.commonTip.setVisibility(View.GONE);
        TipsInfo tipsInfo = tipContentInfos.get(position);
        if (tipsInfo.getType().equals("common"))
        {
            holder.commonTip.setVisibility(View.VISIBLE);
            holder.commonTip.loadDataWithBaseURL(null, tipsInfo.getContent(), "text/html", "utf-8", null);
        }
        else
        {
            holder.hybirdTip.setVisibility(View.VISIBLE);
            List<Facility> facilities = JsonUtil.getCommonTipList(tipsInfo.getContent());
            CarInfoGridViewAdapter adapter = new CarInfoGridViewAdapter(mContext, facilities);
            holder.hybirdTip.setAdapter(adapter);
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        
        public TextView title;
        
        public MyGridView hybirdTip;
        
        public WebView commonTip;
    }
}
