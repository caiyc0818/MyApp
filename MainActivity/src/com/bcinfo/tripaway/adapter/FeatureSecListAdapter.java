package com.bcinfo.tripaway.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.UserCommentSecListAdapter.ReplyInterface;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.FeatureInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.dialog.ReplyDialog;
import com.bcinfo.tripaway.view.dialog.ReplyDialog.OnSendReplyListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户评论列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月30日 下午4:20:49
 */
public class FeatureSecListAdapter extends BaseAdapter
{
	private List<ImageInfo> imageList;
	
    private Context mActivity;
    
    
    public FeatureSecListAdapter(Context mActivity,List<ImageInfo> imageList)
    {
        this.mActivity = mActivity;
        this.imageList = imageList;
    }
    
    
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return imageList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return imageList.get(position);
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
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        ImageInfo info = imageList.get(position);
        if(convertView == null){
        	holder = new ViewHolder();
        	convertView = inflater.inflate(R.layout.feature_list_item2, null);
        	holder.mImgUrl = (ImageView) convertView.findViewById(R.id.feature_img_url);
        	holder.mImgDesc = (TextView) convertView.findViewById(R.id.feature_img_desc);
        	convertView.setTag(holder);
        }else{
        	holder = (ViewHolder) convertView.getTag();
        }
        
        WindowManager wm = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		ViewGroup.LayoutParams lp = holder.mImgUrl.getLayoutParams();
		lp.width=(int) width;
		lp.height=(int) (width/620*348);
		holder.mImgUrl.setLayoutParams(lp);
        if(!StringUtils.isEmpty(info.getUrl())){
        	ImageLoader.getInstance().displayImage(Urls.imgHost+info.getUrl(),  holder.mImgUrl);
        }
        if(!StringUtils.isEmpty(info.getDesc())&&!info.getDesc().equals("null"))
//        holder.mImgDesc.setText("\u3000\u3000"+info.getDesc());
        {	holder.mImgDesc.setText(info.getDesc());
        holder.mImgDesc.setVisibility(View.VISIBLE);
        }else {
        	 holder.mImgDesc.setVisibility(View.GONE);	
        }
        
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户名
         */
        public ImageView mImgUrl;
        
        /**
         * 用户头像
         */
        public TextView mImgDesc;
        
        
    }
    
    public void notifys()
    {
        notifyDataSetChanged();
    }
}
