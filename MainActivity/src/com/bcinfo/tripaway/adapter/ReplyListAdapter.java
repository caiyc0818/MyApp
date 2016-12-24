package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;

import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.PhotoActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ReplayActivity;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class ReplyListAdapter extends BaseAdapter
{
    private Context mContext;
    
//    private String  imageUrl;
    
    private LayoutInflater inflater;
    
    private List<MessageInfo> messageList;
    
    public ReplyListAdapter(Context mContext, List<MessageInfo> messageList)
    {
        this.mContext = mContext;
        this.messageList = messageList;
        inflater = LayoutInflater.from(mContext);
    }
    
    public void setMessageList(List<MessageInfo> messageList)
    {
        this.messageList = messageList;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return messageList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return messageList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        // TODO Auto-generated method stub
        String senderId = messageList.get(position).getSender().getUserId();
        if (senderId.equals(PreferenceUtil.getUserId()))
        {
            // 自己发的消息
            return 0;
        }
        else
        {
        	 // 别人发的非产品消息
            return 1;
        }
    }
    
    @Override
    public int getViewTypeCount()
    {
        // TODO Auto-generated method stub
        return 2;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            String senderId = messageList.get(position).getSender().getUserId();
            if (getItemViewType(position) == 1)
            {// 别人发的非产品消息
                convertView = inflater.inflate(R.layout.left_chat_item, null);
                viewHolder.messageContent = (TextView)convertView.findViewById(R.id.message_content);
                viewHolder.failStatus = (ImageView)convertView.findViewById(R.id.fail_icon);
            }
            else if (getItemViewType(position) == 0)
            {
                // 自己发的消息
                convertView = inflater.inflate(R.layout.right_chat_item, null);
                viewHolder.messageContent = (TextView)convertView.findViewById(R.id.message_content);
                viewHolder.failStatus = (ImageView)convertView.findViewById(R.id.fail_icon);
            }
            else
            {
            }
            viewHolder.messageTime = (TextView)convertView.findViewById(R.id.message_time);
            viewHolder.senderIcon = (ImageView)convertView.findViewById(R.id.send_icon);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final MessageInfo messageInfo = messageList.get(position);
        viewHolder.messageTime.setText(DateUtil.getFormateDate(messageInfo.getCreatetime()));
       
        if (getItemViewType(position) == 1)
        {// 别人发的非产品消息
        	viewHolder.senderIcon.setImageResource(R.drawable.user_defult_photo);
        }else{
        	 if (!StringUtils.isEmpty(PreferenceUtil.getString("avatar")))
             {
                 ImageLoader.getInstance().displayImage(Urls.imgHost + PreferenceUtil.getString("avatar"),
                     viewHolder.senderIcon,
                     AppConfig.getCircleOption());
             }
        	
        }
        
        if (getItemViewType(position) == 0 || getItemViewType(position) == 1)
        {
            viewHolder.failStatus.setVisibility(View.GONE);
            if (messageInfo.getSendStatus().equals("1"))
            {
                viewHolder.failStatus.setVisibility(View.VISIBLE);
                viewHolder.failStatus.setOnClickListener(new OnClickListener()
                {
                    
                    @Override
                    public void onClick(View v)
                    {
                        // TODO Auto-generated method stub
                        ((ReplayActivity)mContext).reSend(position);
                        
                    }
                });
            }
        }
        if (!StringUtils.isEmpty(messageInfo.getImage()) || !StringUtils.isEmpty(messageInfo.getLocalImage()))
        {
//        	imageUrl =messageInfo.getImage();
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_photo);
            SpannableString spannableString = addPicture(bitmap, "i");
            viewHolder.messageContent.setText(spannableString);
            ImageSize imageSize = new ImageSize(200, 200);
            if (!StringUtils.isEmpty(messageInfo.getLocalImage()))
            {
                setImg("file:///" + messageInfo.getLocalImage(), imageSize, viewHolder.messageContent);
            }
            else
            {
                if (!StringUtils.isEmpty(messageInfo.getImage()))
                {
                    setImg(Urls.imgHost + messageInfo.getImage(), imageSize, viewHolder.messageContent);
                }
            }
        }
        if (StringUtils.isEmpty(messageInfo.getLocalImage()) && StringUtils.isEmpty(messageInfo.getImage())
            && getItemViewType(position) != 2)
        {
            viewHolder.messageContent.setText("");
            viewHolder.messageContent.setText(messageInfo.getContent());
        }
        return convertView;
    }
    
    private void setImg(String url, ImageSize imageSize, final TextView textView)
    {
        
        ImageLoader.getInstance().loadImage(url, imageSize, new ImageLoadingListener()
        {
            
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage)
            {
                // TODO Auto-generated method stub
                // Bitmap bitmap = ImageLoader.getInstance().loadImageSync(messageInfo.getImgUrl(), imageSize);
                Bitmap newBitmap = null;
                if (imageUri.contains("file:///"))
                {
                    int angle = BitmapUtil.getBitmapDegree(imageUri.substring("file:///".length()));
                    newBitmap = BitmapUtil.rotaingImageView(angle, loadedImage);
                }
                else
                {
                    newBitmap = loadedImage;
                }
                SpannableString spannableString = addPicture(newBitmap, "i");
                textView.setText(spannableString);
                textView.setOnClickListener(new OnClickListener()
                {
                    
                    @Override
                    public void onClick(View v)
                    {
                    	ArrayList<String> photoUrls=new ArrayList<String>();
                    	photoUrls.add(imageUri);
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext, PhotoActivity.class);
                        intent.putExtra("photos", photoUrls);
                        mContext.startActivity(intent);
                    }
                });
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {
                // TODO Auto-generated method stub
                
            }
        });
        
    }
    
    /**
     * 添加图片
     * 
     * @param context
     * @param bmpUrl 图片路径
     * @param spannableString
     * @return
     */
    private SpannableString addPicture(Bitmap bitmap, String spannableString)
    {
        if (TextUtils.isEmpty(spannableString))
        {
            return null;
        }
        
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
    
    static class ViewHolder
    {
        public TextView messageTime;
        
        public TextView messageContent;
        
        public ImageView senderIcon;
        
        public ImageView failStatus;
        
        public ImageView productPost;
        
        public TextView productTitle;
        
        public TextView productTags;
        
        public TextView productDays;
        
        public TextView productDistance;
        
        public RelativeLayout productLayout;
    }
    
    public interface ReSendMessageInterface
    {
        public void reSend(int position);
    }
}
