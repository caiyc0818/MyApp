package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.CustomerHomePageActivity;
import com.bcinfo.tripaway.activity.SquareSerialDetailActivity;
import com.bcinfo.tripaway.adapter.MicroBlogsTravelNewInfoListenerAdapter.ReplyInterface;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.dialog.ReplyDialog;
import com.bcinfo.tripaway.view.dialog.ReplyDialog.OnSendReplyListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MicroBlogsCommentNewInfoAdapter extends BaseAdapter {

	private ArrayList<Comments> commentsListView = new ArrayList<>();
	private Context mContext;
	private UserReplyListener userReplyListener;

	public MicroBlogsCommentNewInfoAdapter(Context mContext, ArrayList<Comments> commentsListView,UserReplyListener userReplyListener) {
		this.mContext = mContext;
		this.commentsListView = commentsListView;
        this.userReplyListener = userReplyListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentsListView.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentsListView.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	Comments commentss=new Comments();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Comments comments = commentsListView.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.review_item, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.product_servicer_icon_iv = (RoundImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
		holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
		holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
		holder.comments_replys_all = (MyListView) convertView.findViewById(R.id.comments_replys_all);
		final String commentId = comments.getId();
		if (comments.getReplys() != null) {
			MicroBlogsTravelNewInfoListenerAdapter adapter = new MicroBlogsTravelNewInfoListenerAdapter(mContext,new ReplyInterface() {
				@Override
				public void replyToPublisher(String replyId, String content) {
					userReplyListener.replyUserComment(content, commentId, replyId);
				}
			});
			adapter.addAll(comments.getReplys());
			holder.comments_replys_all.setAdapter(adapter);
		}
		if (!StringUtils.isEmpty(comments.getUser().getAvatar())) {
//			ImageLoader.getInstance().displayImage(Urls.imgHost + comments.getUser().getAvatar(),
//					holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			if (holder.product_servicer_icon_iv.getTag() != null) {
				int i = (Integer) holder.product_servicer_icon_iv.getTag();
				if (i != position) {
					holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
				}
			} else {
				holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			ImageLoader.getInstance().displayImage(Urls.imgHost + comments.getUser().getAvatar(),
					holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
		} else {
			holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
		}
		holder.product_servicer_icon_iv.setTag(position);
		holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RoundImageView rs		= (RoundImageView) v.findViewById(R.id.product_servicer_icon_iv);
				int position=(Integer)rs.getTag();
//				ActivityUtil.squareToPersonHomePage(commentsListView.get(position).getUser(),mContext);
				//Toast.makeText(mContext,commentsListView.get(position).getUser().getOrgRole().getRoleName()+"---"+commentsListView.get(position).getUser().getOrgRole().getRoleCode(),Toast.LENGTH_SHORT ).show();
				Intent it=new Intent();
				
				it.putExtra("identifyId", commentsListView.get(position).getUser().getUserId());
				
				it.setClass(mContext,ClubMebHomepageActivity.class);
				if(commentsListView.get(position).getUser().getOrgRole()==null)
				{
					it.putExtra("userInfo", commentsListView.get(position).getUser());

					it.setClass(mContext,CustomerHomePageActivity.class);
				}else if("司机".equals(commentsListView.get(position).getUser().getOrgRole().getRoleName()))
				{
					it.putExtra("isDriverHomePage", true);
					
				}else if("管理员".equals(commentsListView.get(position).getUser().getOrgRole().getRoleName())){
					it.putExtra("userInfo", commentsListView.get(position).getUser());
					it.setClass(mContext,ClubFirstPageActivity.class);
					
				}else{
					it.putExtra("userInfo", commentsListView.get(position).getUser());

					it.setClass(mContext,CustomerHomePageActivity.class);
				}
				mContext.startActivity(it);
//				Intent it=new Intent();
//				
//
//					
//				
//					it.putExtra("userInfo", commentsListView.get(position).getUser());
//
//					it.setClass(mContext,CustomerHomePageActivity.class);
				
//				mContext.startActivity(it);
				
			}
		});
		String name = "";
		if(StringUtils.verifyIsPhone(comments.getUser().getNickname())){
			name = StringUtils.getSecretStr(comments.getUser().getNickname());
			holder.tvName.setText(name);
		}else{
			holder.tvName.setText(comments.getUser().getNickname());
		}
//		holder.tvName.setText(StringUtils.isEmpty(comments.getUser().getNickname()) ? "" : comments.getUser().getNickname());
		String time = DateUtil.setTime(comments.getCreateTime());
		holder.tvTime.setText(StringUtils.isEmpty(time) ? "" : time);
		holder.tvContent.setText(StringUtils.isEmpty(comments.getContent()) ? ""
				: StringUtils.unicodeRevertString(comments.getContent()));
		
		final String userId=commentsListView.get(position).getUser().getUserId();
		holder.tvName.setTag(position);
		convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView tvName = (TextView) v.findViewById(R.id.tvName);
					int position=(Integer)tvName.getTag();
					
					if (PreferenceUtil.getUserId().equals(commentsListView.get(position).getUser().getUserId())){
						ToastUtil.showToast(mContext, "不能对自己的评论进行回复");
						return;
					}
					final String commentId=commentsListView.get(position).getId();
					new ReplyDialog(mContext, new OnSendReplyListener() {
						@Override
						public void sendReply(String comment) {
							userReplyListener.replyUserComment(comment,commentId,"");
						}
					},"@" + commentsListView.get(position).getUser().getNickname() + ": ").show();
				}
			});
		return convertView;
	}

	public interface UserReplyListener {
    	void replyUserComment(String content,String commentId,String replyId);
    	
    }
	
	class ViewHolder {
		RoundImageView product_servicer_icon_iv;
		TextView tvName;
		TextView tvTime;
		TextView tvContent;
		MyListView comments_replys_all;
	}
}
