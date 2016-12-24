package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.AffirmDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 确认详情确认列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class AffirmListAdapter extends BaseAdapter
{
    private static final String TAG = "AffirmListAdapter";
    
    private Context mContext;
    
    private ArrayList<UserInfo> mItemList;
    
    public AffirmListAdapter(Context context, ArrayList<UserInfo> list)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.product_affirm_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.operationLayout = (LinearLayout)convertView.findViewById(R.id.operation_layout);
        holder.aceptButton = (ImageView)convertView.findViewById(R.id.acept_button);
        holder.refuseButton = (ImageView)convertView.findViewById(R.id.refuse_button);
        holder.itemLeftLayout = (LinearLayout)convertView.findViewById(R.id.affirm_item_left_layout);
        holder.userPhoto = (RoundImageView)convertView.findViewById(R.id.affirm_user_photo);
        holder.userName = (TextView)convertView.findViewById(R.id.affirm_user_name);
        holder.affirmState = (TextView)convertView.findViewById(R.id.affirm_state);
        holder.leaveWords = (TextView)convertView.findViewById(R.id.leave_words);
        holder.aceptButton.setOnClickListener(mOnClickListener);
        holder.refuseButton.setOnClickListener(mOnClickListener);
        holder.aceptButton.setClickable(false);
        holder.refuseButton.setClickable(false);
        if (!StringUtils.isEmpty(mItemList.get(position).getAvatar()))
        {
            ImageLoader.getInstance()
                .displayImage(Urls.imgHost + mItemList.get(position).getAvatar(), holder.userPhoto);
        }
        
        holder.userName.setText(mItemList.get(position).getNickname());
        String state = "";
        if (state != null)
        {
            holder.affirmState.setText(state);
            holder.affirmState.setVisibility(View.VISIBLE);
            if (state.equals("已同意"))
            {
                holder.affirmState.setBackgroundResource(R.drawable.user_agree_tag_bg);
            }
            else if (state.equals("已拒绝"))
            {
                holder.affirmState.setBackgroundResource(R.drawable.user_refuse_tag_bg);
            }
            else
            {
                holder.affirmState.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.affirmState.setVisibility(View.GONE);
        }
        
        holder.leaveWords.setText("");
        return convertView;
    }
    
    OnClickListener mOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            Intent intent = new Intent(mContext, AffirmDialog.class);
            if (v.getId() == R.id.acept_button)
            {
                intent.putExtra("isAgree", true);
            }
            else if (v.getId() == R.id.refuse_button)
            {
                intent.putExtra("isAgree", false);
            }
            // intent.putExtra("userInfo", mItemList.get(0));
            mContext.startActivity(intent);
        }
    };
    
    public class ViewHolder
    {
        /**
         * 同意拒绝布局
         */
        public LinearLayout operationLayout;
        
        /**
         * 同意
         */
        public ImageView aceptButton;
        
        /**
         * 拒绝
         */
        public ImageView refuseButton;
        
        public LinearLayout itemLeftLayout;
        
        /**
         * 用户头像
         */
        public RoundImageView userPhoto;
        
        /**
         * 确认状态
         */
        public TextView affirmState;
        
        /**
         * 用户名字
         */
        public TextView userName;
        
        /**
         * 用户留言
         */
        public TextView leaveWords;
    }
}
