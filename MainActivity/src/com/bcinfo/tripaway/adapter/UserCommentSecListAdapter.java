package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AttractionAllInfo;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.ReplyDialog;
import com.bcinfo.tripaway.view.dialog.ReplyDialog.OnSendReplyListener;

/**
 * 用户评论列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月30日 下午4:20:49
 */
public class UserCommentSecListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Replys> replysList;
    
    private int postion;
    
    private ReplyInterface repInterface;
    
    // private BitmapManager bitmapManager;
    public UserCommentSecListAdapter( Context context)
    {
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    
    public UserCommentSecListAdapter( Context context, List<Replys> replysList)
    {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.replysList = replysList;
    }
    
    public UserCommentSecListAdapter( Context context,ReplyInterface repInterface){
    	this.context = context;
        this.repInterface =repInterface;
   }
    public UserCommentSecListAdapter( Context context, List<Replys> replysList,ReplyInterface repInterface){
    	 this.context = context;
         this.replysList = replysList;
         this.repInterface =repInterface;
    }
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return replysList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return replysList.get(position);
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
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.comments_detail_second_item, null);
            holder = new ViewHolder();
            holder.replyName = (TextView)convertView.findViewById(R.id.comments_reply_name);
            holder.replyToName = (TextView)convertView.findViewById(R.id.comments_reply_toname);
            holder.commentDate = (TextView)convertView.findViewById(R.id.comments_reply_date);
            holder.commentContent = (TextView)convertView.findViewById(R.id.comments_reply_content);
            holder.replyBtn = (TextView)convertView.findViewById(R.id.comments_reply_btn);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
      
        //时间
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
			long time = (date.getTime() -format.parse(replysList.get(position).getCreateTime()).getTime())/1000;
			long day1=time/(24*3600);
			long hour1=time%(24*3600)/3600;
		    long minute1=time%3600/60;
		    if(day1 >0){
		    	holder.commentDate.setText(day1+"天前");
		    }else if(hour1 > 0){
		    	holder.commentDate.setText(hour1+"小时前");
		    }else if(minute1 > 0){
		    	holder.commentDate.setText(minute1+"分前");
		    }else{
		    	if(time<=0)
		    	{
		    		holder.commentDate.setText("刚刚");
		    	}else{		    		
		    		holder.commentDate.setText(time+"秒前");
		    	}
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        String name = replysList.get(position).getPublisher().getNickname();
        if(StringUtils.verifyIsPhone(name)){
        	name = StringUtils.getSecretStr(name);
        }
        holder.replyName.setText(name);
        
        if(StringUtils.verifyIsPhone(replysList.get(position).getReplyTo().getNickname())){
        	name = StringUtils.getSecretStr(replysList.get(position).getReplyTo().getNickname());
        }
        else{
        	name = replysList.get(position).getReplyTo().getNickname();
        }
        holder.replyToName.setText(name);
        
        holder.commentContent.setText(StringUtils.unicodeRevertString(replysList.get(position).getContent()));
//        holder.replyBtn.setTag(replysList.get(position));
        if (!(PreferenceUtil.getUserId().equals(
        		replysList.get(position).getPublisher().getUserId()))) {
			holder.replyBtn.setVisibility(View.VISIBLE);
		}
        holder.replyBtn.setOnClickListener(replyClickListener);
        holder.replyBtn.setTag(position);
        return convertView;
    }
    
    private OnClickListener replyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comments_reply_btn:
				int position = (Integer) v.getTag();
				final Replys reply = replysList.get(position);
				String name = reply.getPublisher().getNickname();
				if(StringUtils.verifyIsPhone(name)){
					name = StringUtils.getSecretStr(name);
				}
				new ReplyDialog(context, new OnSendReplyListener() {
					@Override
					public void sendReply(String comment) {
						repInterface.replyToPublisher(reply.getId(),comment);
					}
				},"@" + name + ": ").show();
				break;
			default:
				break;
			}
		}
	};
	public interface ReplyInterface{
		void replyToPublisher(String replyId,String content);
	}
    public class ViewHolder
    {
        /**
         * 回复人
         */
        public TextView replyName;
        
        /**
         * 回复对象
         */
        public TextView replyToName;
        
        /**
         * 评论日期
         */
        public TextView commentDate;
        
        /**
         * 评论内容
         */
        public TextView commentContent;
        
        /**
         * 回复
         */
        public TextView replyBtn;
    }
    
    public void clearAll()
    {
        this.replysList.clear();
        notifyDataSetChanged();
    }
    
    public void addAll(List<Replys> arrayList)
    {
        // TODO Auto-generated method stub
        this.replysList = arrayList;
        notifyDataSetChanged();
    }
}

