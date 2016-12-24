package com.bcinfo.tripaway.adapter;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Friend;
import com.bcinfo.tripaway.net.HttpDeleteWithBody;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.ApplyExitDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 好友列表 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月25日 11：38：22
 */
public class FriendShipListAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<Friend> friendList;
    
    private LayoutInflater inflator;
    
    private ApplyExitDialog cancelDialog;
    
    public FriendShipListAdapter(Context mContext, List<Friend> friendList)
    {
        super();
        this.mContext = mContext;
        this.friendList = friendList;
        inflator = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        
        return friendList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return friendList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup groupView)
    {
        ViewHolder holder;
        Friend friendItem = friendList.get(position);
        if (convertView == null)
        {
            convertView = inflator.inflate(R.layout.item_my_good_friendship_layout, null);
            holder = new ViewHolder();
            holder.friendRoundIv = (RoundImageView)convertView.findViewById(R.id.my_good_friendship_head_icon_iv);
            holder.friendNameTv = (TextView)convertView.findViewById(R.id.my_good_friendship_info_name_tv);
            holder.friendOriginTv = (TextView)convertView.findViewById(R.id.my_good_friendship_info_place_of_origin_tv);
            holder.friendStatusTv1 = (TextView)convertView.findViewById(R.id.my_good_friendship_status_tv1);
            holder.friendStatusTv2 = (TextView)convertView.findViewById(R.id.my_good_friendship_status_tv2);
            convertView.setTag(holder);// 设置tag标签
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (StringUtils.isEmpty(friendItem.getAvatar()))
        {
            
        }
        else
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + friendItem.getAvatar(),
                holder.friendRoundIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        holder.friendNameTv.setText(friendItem.getNickName());
        holder.friendOriginTv.setText(friendItem.getArea());
        if (friendItem.getStatus().equals("0"))// 已关注
        {
            holder.friendStatusTv1.setVisibility(View.VISIBLE);
            holder.friendStatusTv1.setTag(position);
            holder.friendStatusTv2.setVisibility(View.GONE);
            holder.friendStatusTv1.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    final int posArg = (Integer)v.getTag();
                    cancelDialog = ApplyExitDialog.createDialog(mContext, R.layout.dialog_cancel_attention_layout);
                    LinearLayout cancelLayout =
                        (LinearLayout)cancelDialog.findViewById(R.id.my_friendship_cancel_layout);
                    LinearLayout confirmLayout =
                        (LinearLayout)cancelDialog.findViewById(R.id.my_friendship_confirm_layout);
                    
                    confirmLayout.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            friendList.remove(posArg);
                            FriendShipListAdapter.this.notifyDataSetChanged();
                            System.out.println("firendList:" + friendList);
                            cancelDialog.dismiss();
                            
                        }
                    });
                    cancelLayout.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            
                            cancelDialog.dismiss();
                        }
                    });
                    cancelDialog.show();
                    
                }
            });
        }
        else if (friendItem.getStatus().equals("1"))// 相互关注
        {
            holder.friendStatusTv1.setVisibility(View.GONE);// 隐藏
            holder.friendStatusTv2.setVisibility(View.VISIBLE);// 可见
            holder.friendStatusTv2.setTag(position);
            holder.friendStatusTv2.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    final int posArg = (Integer)v.getTag();
                    cancelDialog = ApplyExitDialog.createDialog(mContext, R.layout.dialog_cancel_attention_layout);
                    LinearLayout cancelLayout =
                        (LinearLayout)cancelDialog.findViewById(R.id.my_friendship_cancel_layout);
                    LinearLayout confirmLayout =
                        (LinearLayout)cancelDialog.findViewById(R.id.my_friendship_confirm_layout);
                    confirmLayout.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            friendList.remove(posArg);
                            FriendShipListAdapter.this.notifyDataSetChanged();
                            cancelDialog.dismiss();
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    testFriendunFocusUrl("abc");
                                }
                            }).start();
                            
                        }
                    });
                    cancelLayout.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            
                            cancelDialog.dismiss();
                        }
                    });
                    cancelDialog.show();
                }
            });
        }
        
        return convertView;
    }
    
    private void testFriendunFocusUrl(String userId)
    {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(Urls.friend_focus_off_url + userId);
        httpDelete.addHeader("session", PreferenceUtil.getSession());
        httpDelete.addHeader("token", PreferenceUtil.getToken());
        
        HttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                System.out.println(EntityUtils.toString(httpResponse.getEntity()));
            }
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }
        
    }
    
    private class ViewHolder
    {
        RoundImageView friendRoundIv;// 好友照片
        
        TextView friendNameTv;// 好友姓名
        
        TextView friendOriginTv;// 好友籍贯
        
        TextView friendStatusTv1;// 好友关注状态(已关注)
        
        TextView friendStatusTv2;// 好友关注状态 (相互关注)
    }
    
}
