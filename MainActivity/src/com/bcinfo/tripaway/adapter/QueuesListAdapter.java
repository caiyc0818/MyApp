package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.getui.util.TimeUtil;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.emoji.EmojiconTextView;
import com.bcinfo.tripaway.view.image.MoreCircleImageView;
import com.bcinfo.tripaway.view.swipe.BaseSwipeAdapter;
import com.bcinfo.tripaway.view.swipe.SwipeLayout;
import com.bcinfo.tripaway.view.swipe.SwipeLayout.Status;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 消息列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class QueuesListAdapter extends BaseSwipeAdapter
{
    private static final String TAG = "MessageListAdapter";
    
    private Context mContext;
    
    private List<QueuesInfo> queuesInfoList = new ArrayList<QueuesInfo>();
    
    // private DelectListener mDelectListener;
    
    public QueuesListAdapter(Context context, List<QueuesInfo> queuesInfoList)
    {
        this.mContext = context;
        this.queuesInfoList = queuesInfoList;
    }
    
    // public MessageListAdapter(Context context, LinkedList<RecentItem> list, DelectListener listener)
    // {
    // // TODO Auto-generated constructor stub
    // this.mContext = context;
    // this.mItemList = list;
    // this.mDelectListener = listener;
    // mMessageDB = Constant.getInstance().getMessageDB();
    // mRecentDB = Constant.getInstance().getRecentDB();
    // }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return queuesInfoList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return queuesInfoList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    // @Override
    // public View getView(final int position, View convertView, ViewGroup parent)
    // {
    // // TODO Auto-generated method stub
    // final RecentItem item = mItemList.get(position);
    // ViewHolder holder = null;
    // LayoutInflater inflater = LayoutInflater.from(mContext);
    // if (convertView == null)
    // {
    // convertView = inflater.inflate(R.layout.message_list_item, null);
    // holder = new ViewHolder();
    // convertView.setTag(holder);
    // }
    // else
    // {
    // holder = (ViewHolder)convertView.getTag();
    // }
    // // holder.delectButton = (ImageView)convertView.findViewById(R.id.delect_button);
    // holder.itemLeftLayout = (LinearLayout)convertView.findViewById(R.id.message_item_left_layout);
    // holder.userPhoto = (MoreCircleImageView)convertView.findViewById(R.id.message_user_photo);
    // holder.userName = (TextView)convertView.findViewById(R.id.message_user_name);
    // holder.sendTime = (TextView)convertView.findViewById(R.id.message_send_time);
    // holder.messageNumber = (TextView)convertView.findViewById(R.id.message_number);
    // holder.lastWords = (TextView)convertView.findViewById(R.id.last_words);
    // holder.delectButton.setOnClickListener(new OnClickListener()
    // {
    // @Override
    // public void onClick(View v)
    // {
    // mDelectListener.delect(position);
    // }
    // });
    // holder.delectButton.setClickable(false);
    // ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    // for (int i = 0; i < 5; i++)
    // {
    // Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), Constant.heads[i]);
    // bitmapList.add(photo);
    // }
    // holder.userPhoto.setImageBitmaps(bitmapList);
    // // ImageLoader.getInstance().displayImage(mItemList.get(position).getUserPhotoUrl(), holder.userPhoto);
    // holder.userName.setText(item.getName());
    // holder.lastWords.setText(convertNormalStringToSpannableString(item.getMessage()), BufferType.SPANNABLE);
    // // holder.itemLeftLayout.setScrollX(0);
    // holder.sendTime.setText(TimeUtil.getChatTime(item.getTime()));
    // int num = mMessageDB.getNewCount(item.getUserId());
    // if (num > 0)
    // {
    // holder.messageNumber.setVisibility(View.VISIBLE);
    // holder.messageNumber.setText(num + "");
    // }
    // else
    // {
    // holder.messageNumber.setVisibility(View.GONE);
    // }
    // return convertView;
    // }
    
    public class ViewHolder
    {
        /**
         * 删除按钮
         */
        public ImageView delectButton;
        
        /**
         * 左边布局
         */
        public LinearLayout itemLeftLayout;
        
        /**
         * 用户头像
         */
        public MoreCircleImageView userPhoto;
        
        /**
         * 消息发送时间
         */
        public TextView sendTime;
        
        /**
         * 消息条数
         */
        public TextView messageNumber;
        
        /**
         * 用户名字
         */
        public TextView userName;
        
        /**
         * 用户最后留言
         */
        public EmojiconTextView lastWords;
    }
    
    // public interface DelectListener
    // {
    // public void delect(int position);
    // }
    
    @Override
    public int getSwipeLayoutResourceId(int position)
    {
        // TODO Auto-generated method stub
        return R.id.swipe;
    }
    
    @Override
    public View generateView(final int position, ViewGroup parent)
    {
    	Log.v(TAG, Integer.toString(position));
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
//        v.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                // 点击完成之后，关闭删除menu
//            	
//            	Log.v(TAG, Integer.toString(position));
//            	Log.v(TAG, Integer.toString(queuesInfoList.size()));
//                if (swipeLayout.getOpenStatus().equals(Status.Open))
//                {
//                    // LogUtil.i(TAG, "关闭item，隐藏删除按钮", position + "");
//                    swipeLayout.close();
//                }
//                else
//                {
//                    LogUtil.i(TAG, "跳转至聊天页面", position + "");
//                    Intent intent = new Intent(mContext, ChatActivity.class);
//                    intent.putExtra("queueId", queuesInfoList.get(position).getQueueId());
//                    intent.putExtra("title", queuesInfoList.get(position).getQueueTitle());
//                    intent.putExtra("pattern", queuesInfoList.get(position).getType());
//                    intent.putExtra("fromQueue", true);
//                    if (queuesInfoList.get(position).getType().equals("team"))
//                    {
//                        intent.putExtra("isTeam", true);
//                    }
//                    else
//                    {
//                        intent.putExtra("isTeam", false);
//                    }
//                    mContext.startActivity(intent);
//                    ((Activity)mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
//                }
//            }
//        });
        // 添加删除布局的点击事件
//        v.findViewById(R.id.ll_menu).setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View arg0)
//            {
//                // 点击完成之后，关闭删除menu
//                if (swipeLayout.getOpenStatus().equals(Status.Open))
//                {
//                	Log.v(TAG, Integer.toString(position));
//                	Log.v(TAG, Integer.toString(queuesInfoList.size()));
//                    PreferenceUtil.setHaveDeleteId(queuesInfoList.get(position).getMessageInfo().getId());
//                    QueuesListDB.getInstance().deleteQueuesById(queuesInfoList.get(position).getQueueId());
////                    QueuesInfo queuesInfo=   queuesInfoList.remove(position);
////                    if(queuesInfo!=null){
//                    setQueueRead(queuesInfoList.get(position).getQueueId(),position);
//                    queuesInfoList.remove(position);
//                    notifyDataSetChanged();
//                   
////                    }
//                    swipeLayout.close();
//                }
//            }
//        });
        return v;
    }
    
    @Override
    public void fillValues(final int position, View convertView)
    {
        // TODO Auto-generated method stub
        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // 点击完成之后，关闭删除menu
            	
            	Log.v(TAG, Integer.toString(position));
            	Log.v(TAG, Integer.toString(queuesInfoList.size()));
                if (swipeLayout.getOpenStatus().equals(Status.Open))
                {
                    // LogUtil.i(TAG, "关闭item，隐藏删除按钮", position + "");
                    swipeLayout.close();
                }
                else
                {
                    LogUtil.i(TAG, "跳转至聊天页面", position + "");
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    
                    intent.putExtra("queueId", queuesInfoList.get(position).getQueueId());
                    intent.putExtra("title", queuesInfoList.get(position).getQueueTitle());
                    intent.putExtra("pattern", queuesInfoList.get(position).getType());
                    intent.putExtra("fromQueue", true);
                    if (queuesInfoList.get(position).getType().equals("team"))
                    {
                        intent.putExtra("isTeam", true);
                        intent.putExtra("ok", queuesInfoList.get(position).getMessageInfo().getProductInfo());
                       
                    }
                    else
                    {
                        intent.putExtra("isTeam", false);
                    }
                    mContext.startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                }
            }
        });
      View view=  convertView.findViewById(R.id.ll_menu);
      view.setTag(position);
      view.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
            	int position=(Integer)arg0.getTag();
                // 点击完成之后，关闭删除menu
                if (swipeLayout.getOpenStatus().equals(Status.Open))
                {
                	Log.v(TAG, Integer.toString(position));
                	Log.v(TAG, Integer.toString(queuesInfoList.size()));
                    PreferenceUtil.setHaveDeleteId(queuesInfoList.get(position).getMessageInfo().getId());
                    QueuesListDB.getInstance().deleteQueuesById(queuesInfoList.get(position).getQueueId());
//                    QueuesInfo queuesInfo=   queuesInfoList.remove(position);
//                    if(queuesInfo!=null){
                    setQueueRead(queuesInfoList.get(position).getQueueId(),position);
                    queuesInfoList.remove(position);
                 
                    Intent intent1 = new Intent("com.bcinfo.refreshUnread1");
//                    intent1.putExtra(num, value)
                    mContext.sendBroadcast(intent1);
                    notifyDataSetChanged();
                   
//                    }
                    swipeLayout.close();
                }
            }
        });
        LinearLayout itemLeftLayout = (LinearLayout)convertView.findViewById(R.id.message_item_left_layout);
        final MoreCircleImageView userPhoto = (MoreCircleImageView)convertView.findViewById(R.id.message_user_photo);
        TextView userName = (TextView)convertView.findViewById(R.id.message_user_name);
        TextView sendTime = (TextView)convertView.findViewById(R.id.message_send_time);
        TextView messageNumber = (TextView)convertView.findViewById(R.id.message_number);
        EmojiconTextView lastWords = (EmojiconTextView)convertView.findViewById(R.id.last_words);
        final ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
        if (queuesInfoList.get(position).getType().equals("team"))
        {
            final List<String> avatarsList = queuesInfoList.get(position).getAvatarsList();
            if (avatarsList.size() > 5)
            {
                for (int i = 0; i < 5; i++)
                {
                    if (!StringUtils.isEmpty(avatarsList.get(i)))
                    {
                        ImageLoader.getInstance().loadImage(Urls.imgHost + avatarsList.get(i),
                            new ImageLoadingListener()
                            {
                                
                                @Override
                                public void onLoadingStarted(String arg0, View arg1)
                                {
                                    // TODO Auto-generated method stub
                                    
                                }
                                
                                @Override
                                public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
                                {
                                    // TODO Auto-generated method stub
                                    bitmapList.add(BitmapFactory.decodeResource(mContext.getResources(),
                                        R.drawable.user_defult_photo));
                                    if (bitmapList.size() == 5)
                                    {
                                        userPhoto.setImageBitmaps(bitmapList);
                                    }
                                }
                                
                                @Override
                                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
                                {
                                    // TODO Auto-generated method stub
                                    bitmapList.add(arg2);
                                    if (bitmapList.size() == 5)
                                    {
                                        userPhoto.setImageBitmaps(bitmapList);
                                    }
                                }
                                
                                @Override
                                public void onLoadingCancelled(String arg0, View arg1)
                                {
                                    // TODO Auto-generated method stub
                                    
                                }
                            });
                    }
//                    else{
//                    	ImageLoader.getInstance().displayImage(Urls.imgHost + avatarsList.get(i),userPhoto,R.drawable.user_defult_photo);
//                    }
                }
            }
            else
            {
                for (int i = 0; i < avatarsList.size(); i++)
                {
                    if (!StringUtils.isEmpty(avatarsList.get(i).replace(" ", "")))
                    {
                        ImageLoader.getInstance().loadImage(Urls.imgHost + avatarsList.get(i).replace(" ", ""),
                            new ImageLoadingListener()
                            {
                                
                                @Override
                                public void onLoadingStarted(String arg0, View arg1)
                                {
                                    // TODO Auto-generated method stub
                                    
                                }
                                
                                @Override
                                public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
                                {
                                    // TODO Auto-generated method stub
                                    bitmapList.add(BitmapFactory.decodeResource(mContext.getResources(),
                                        R.drawable.user_defult_photo));
                                    if (bitmapList.size() == avatarsList.size())
                                    {
                                        userPhoto.setImageBitmaps(bitmapList);
                                    }
                                }
                                
                                @Override
                                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
                                {
                                    // TODO Auto-generated method stub
                                    bitmapList.add(arg2);
                                    if (bitmapList.size() == avatarsList.size())
                                    {
                                        userPhoto.setImageBitmaps(bitmapList);
                                    }
                                }
                                
                                @Override
                                public void onLoadingCancelled(String arg0, View arg1)
                                {
                                    // TODO Auto-generated method stub
                                    
                                }
                            });
                    }
                }
            }
        }
        else
        {
            if (!StringUtils.isEmpty(queuesInfoList.get(position).getQueueLogo()))
            {
                ImageLoader.getInstance().loadImage(Urls.imgHost + queuesInfoList.get(position).getQueueLogo(),
                    new ImageLoadingListener()
                    {
                        
                        @Override
                        public void onLoadingStarted(String arg0, View arg1)
                        {
                            // TODO Auto-generated method stub
                            
                        }
                        
                        @Override
                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
                        {
                            // TODO Auto-generated method stub
                            bitmapList.add(BitmapFactory.decodeResource(mContext.getResources(),
                                R.drawable.user_defult_photo));
                            if (bitmapList.size() == 5)
                            {
                                userPhoto.setImageBitmaps(bitmapList);
                            }
                        }
                        
                        @Override
                        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
                        {
                            // TODO Auto-generated method stub
                            if (arg2 != null)
                            {
                                bitmapList.add(arg2);
                                userPhoto.setImageBitmaps(bitmapList);
                            }
                        }
                        
                        @Override
                        public void onLoadingCancelled(String arg0, View arg1)
                        {
                            // TODO Auto-generated method stub
                            
                        }
                    });
            }
        }
        
        // for (int i = 0; i < 5; i++)
        // {
        // Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), Constant.heads[i]);
        // bitmapList.add(photo);
        // }
        // userPhoto.setImageBitmaps(bitmapList);
        MessageInfo messageInfo = queuesInfoList.get(position).getMessageInfo();
        userName.setText(queuesInfoList.get(position).getQueueTitle());
        sendTime.setText(TimeUtil.getDayTime1(DateUtil.formatStrtoLong(messageInfo.getCreatetime())));
        if (queuesInfoList.get(position).getUnread().equals("0"))
        {
            messageNumber.setVisibility(View.GONE);
        }
        else
        {
            if (Integer.parseInt(queuesInfoList.get(position).getUnread()) > 99)
            {
                messageNumber.setText("99+");
            }
            else
            {
                messageNumber.setText(queuesInfoList.get(position).getUnread());
            }
            messageNumber.setVisibility(View.VISIBLE);
        }
        if (queuesInfoList.get(position).getType().equals("team"))
        {
            if (StringUtils.isEmpty(messageInfo.getImage()))
            {
                lastWords.setText(/* messageInfo.getSender().getNickname() + ":" + */messageInfo.getContent());
            }
            else
            {
                lastWords.setText(/* messageInfo.getSender().getNickname() + ":" + */"[图片]");
            }
            
        }
        else
        {
            if (StringUtils.isEmpty(messageInfo.getImage()))
            {
                lastWords.setText(messageInfo.getContent());
            }
            else
            {
                lastWords.setText(/* messageInfo.getSender().getNickname() + ":" + */"[图片]");
            }
        }
    }
    
    private void setQueueRead(String queueId,final int position)
    {
//        RequestParams params = new RequestParams();
//        params.put("queueId", queueId);
        JSONObject jsonObject = new JSONObject();
        try {
			jsonObject.put("queueId", queueId);
		
        StringEntity stringEntity = new StringEntity(jsonObject.toString(),HTTP.UTF_8);
        HttpUtil.post(Urls.queue_read_url, stringEntity, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "onFailure--setQueueRead", throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtil.i(TAG, "onFailure--setQueueRead", throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i(TAG, "setQueueRead", response.toString());
                Intent intent = new Intent("com.bcinfo.refreshUnread");
                mContext.sendBroadcast(intent);
               
            }
            
        });} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
