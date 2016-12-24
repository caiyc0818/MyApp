package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ScenicInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 行程规划详情列表适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年11月29日 上午10:04:40
 */
public class TripPlanDetailListAdapter extends BaseAdapter
{
    private static final String TAG = "TripPlanDetailListAdapter";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private Context mContext;
    private ArrayList<ScenicInfo> mItemList;

    public TripPlanDetailListAdapter(Context context, ArrayList<ScenicInfo> list)
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.trip_plan_detail_listitem, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.scenicIndex = (TextView) convertView.findViewById(R.id.scenic_index);
        holder.scenicName = (TextView) convertView.findViewById(R.id.scenic_name);
        holder.scenicComment = (TextView) convertView.findViewById(R.id.scenic_comment);
        holder.scenicTicketPrice = (TextView) convertView.findViewById(R.id.scenic_ticket_price);
        holder.nextScenicDistance = (TextView) convertView.findViewById(R.id.next_scenic_distance);
        holder.scenicPhoto = (ImageView) convertView.findViewById(R.id.scenic_photo);
//        holder.scenicIndex.setText(mItemList.get(position).getIndex());
//        holder.scenicName.setText(mItemList.get(position).getName());
//        holder.scenicComment.setText(mItemList.get(position).getComment());
//        holder.scenicTicketPrice.setText(mItemList.get(position).getTicketPrice());
//        holder.nextScenicDistance.setText(mItemList.get(position).getNextDistance());
        
//        final ArrayList<String> urls = mItemList.get(position).getPhotoUrls();
//        if (urls != null && urls.size() > 0)
//        {
//            ImageLoader.getInstance().displayImage(urls.get(0),  holder.scenicPhoto);
//            holder.scenicPhoto.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent(mContext, ImageViewerActivity.class);
//                    intent.putExtra(EXTRA_IMAGE_URLS, mItemList.get(position).getPhotoUrls());
//                    intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, urls);
//                    mContext.startActivity(intent);
//                }
//            });
//        }
        return convertView;
    }
    public class ViewHolder
    {
        /**
         * 景点序号
         */
        public TextView scenicIndex;
        /**
         * 景点名称
         */
        public TextView scenicName;
        /**
         *景点照片ViewPager
         */
        public ImageView scenicPhoto;
        /**
         *  景点点评
         */
        public TextView scenicComment;
        /**
         *  景点门票
         */
        public TextView scenicTicketPrice;
        /**
         *  下个景点距离
         */
        public TextView nextScenicDistance;
    }
}
