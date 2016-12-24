package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;

import com.bcinfo.tripaway.activity.UserMicroCommentActivity;
import com.bcinfo.tripaway.adapter.UserCommentSecListAdapter.ReplyInterface;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
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
public class UserCommentListAdapter extends BaseAdapter
{
    private Activity mActivity;
    
    private ArrayList<Comments> commentList;
    
    private UserReplyListener userReplyListener;
    
    private List<Replys> replys = new ArrayList<Replys>();
    
    
    public UserCommentListAdapter(Activity mActivity, ArrayList<Comments> list)
    {
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
        this.commentList = list;
        // bitmapManager =
        // new BitmapManager(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher));
    }
    
    
    public UserCommentListAdapter(Activity mActivity, ArrayList<Comments> list,UserReplyListener userReplyListener)
    {
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
        this.commentList = list;
        this.userReplyListener = userReplyListener;
        // bitmapManager =
        // new BitmapManager(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher));
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return commentList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return commentList.get(position);
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
        if(commentList.get(position).getReplys() != null){
//        	 replys.clear();
//        	 replys .addAll(commentList.get(position).getReplys());
        }
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.comments_detail_item, null);
            holder = new ViewHolder();
            holder.userStarLayout = (LinearLayout)convertView.findViewById(R.id.comments_user_star);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
    	// 回复列表
    	holder.replyListView = (ListView)convertView.findViewById(R.id.comments_replys_all);
    	final String commentId = commentList.get(position).getId();
    	if(commentList.get(position).getReplys() != null){
    		UserCommentSecListAdapter adapter = new UserCommentSecListAdapter(mActivity,new ReplyInterface() {
				@Override
				public void replyToPublisher(String replyId, String content) {
					userReplyListener.replyUserComment(content, commentId, replyId);
				}
			});
    		adapter.addAll(commentList.get(position).getReplys());
    		holder.replyListView.setAdapter(adapter);
    	}
        holder.userName = (TextView)convertView.findViewById(R.id.comments_user_name);
        holder.userPhoto = (RoundImageView)convertView.findViewById(R.id.comments_user_photo);
        holder.commentDate = (TextView)convertView.findViewById(R.id.comments_user_date);
        holder.commentContent = (TextView)convertView.findViewById(R.id.comments_user_content);
        holder.userReplyTv = (TextView)convertView.findViewById(R.id.comments_user_reply);
        holder.userReplyTv.setTag(R.id.tag_comment_first, commentList.get(position).getId());
        String name="";
		if(StringUtils.verifyIsPhone(commentList.get(position).getUser().getNickname())){
			name = StringUtils.getSecretStr(commentList.get(position).getUser().getNickname());
			holder.userName.setText(name);
		}else{
			holder.userName.setText(commentList.get(position).getUser().getNickname());
		}
//        holder.userName.setText(commentList.get(position).getUser().getNickname());
		if (PreferenceUtil.getUserId().equals(commentList.get(position).getUser().getUserId())){
			  holder.userReplyTv.setVisibility(View.GONE);
		}else{
			holder.userReplyTv.setVisibility(View.VISIBLE);
		}
        String starts = commentList.get(position).getAverScore();
        if(starts != null && !StringUtils.isEmpty(starts)){
        	if(starts.contains(".")){
        		starts = starts.substring(0,starts.indexOf("."));
        	}
        	int num = Integer.parseInt(starts);
        	
        	if(holder.userStarLayout != null){
        		holder.userStarLayout.removeAllViews();
        	}
        	
        	for (int i = 0; i < num; i++) {
        		ImageView view = new ImageView(mActivity);
        		LayoutParams params = new LayoutParams(20,20);
        		params.setMargins(5,0,0,0);
        		view.setLayoutParams(params);
        		view.setImageResource(R.drawable.yellow_star);
        		holder.userStarLayout.addView(view);
			}
        	
        }
        //时间
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
			long time = (date.getTime() -format.parse(commentList.get(position).getCreateTime()).getTime())/1000;
			long day1=time/(24*3600);
			long hour1=time%(24*3600)/3600;
		    long minute1=time%3600/60;
		    if(day1 >0){
		    	if(day1<0)
		    	{
		    		day1=0;
		    	}
		    	holder.commentDate.setText(day1+"天前");
		    }else if(hour1 > 0){
		    	if(hour1<0)
		    	{
		    		hour1=0;
		    	}
		    	holder.commentDate.setText(hour1+"小时前");
		    }else if(minute1 > 0){
		    	if(minute1<0)
		    	{
		    		minute1=0;
		    	}
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
        holder.commentContent.setText(StringUtils.unicodeRevertString(commentList.get(position).getContent()));
//        if (!StringUtils.isEmpty(commentList.get(position).getUser().getAvatar()))
//        {
//        }else{
//        	holder.userPhoto.setImageResource(R.drawable.user_defult_photo);
//        }
        holder.userPhoto.setImageResource(R.drawable.user_defult_photo);
        if(!StringUtils.isEmpty(commentList.get(position).getUser().getAvatar())){
        	
        	ImageLoader.getInstance().displayImage(Urls.imgHost + commentList.get(position).getUser().getAvatar(),
        			holder.userPhoto,AppConfig.options(R.drawable.user_defult_photo));
        }
//        holder.userPhoto.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(mActivity, PersonalInfoNewActivity.class);
//                intent.putExtra("user", "");
//                mActivity.startActivity(intent);
//                mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
//            }
//        });
        holder.userReplyTv.setTag(position);
        holder.userReplyTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position=(Integer)v.getTag();
				String name ="";
				if(StringUtils.verifyIsPhone(commentList.get(position).getUser().getNickname())){
					name = StringUtils.getSecretStr(commentList.get(position).getUser().getNickname());
//					holder.userName.setText(name);
				}else{
					name = commentList.get(position).getUser().getNickname();
				}
				
				final String commentId =v.getTag(R.id.tag_comment_first).toString();
				new ReplyDialog(mActivity, new OnSendReplyListener() {
					@Override
					public void sendReply(String comment) {
						userReplyListener.replyUserComment(comment,commentId,"");
					}
				},"@" + name + ": ").show();
			}
		});
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户名
         */
        public TextView userName;
        
        /**
         * 用户头像
         */
        public RoundImageView userPhoto;
        
        /**
         * 评论日期
         */
        public TextView commentDate;
        
        /**
         * 评论内容
         */
        public TextView commentContent;
        
        /**
         * 回复listview
         */
        private ListView replyListView;
        
        private TextView userReplyTv;
        
        private LinearLayout userStarLayout;
    }
    
    public interface UserReplyListener {
    	void replyUserComment(String content,String commentId,String replyId);
    	
    }
    
}
