package com.bcinfo.tripaway.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.ReplyDialog;
import com.bcinfo.tripaway.view.dialog.ReplyDialog.OnSendReplyListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MicroBlogsTravelNewInfoListenerAdapter extends BaseAdapter {

	private Context context;

	private List<Replys> replysList;

	private int postion;

	private ReplyInterface repInterface;

	// private BitmapManager bitmapManager;
	public MicroBlogsTravelNewInfoListenerAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MicroBlogsTravelNewInfoListenerAdapter(Context context, List<Replys> replysList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.replysList = replysList;
	}

	public MicroBlogsTravelNewInfoListenerAdapter(Context context, ReplyInterface repInterface) {
		this.context = context;
		this.repInterface = repInterface;
	}

	public MicroBlogsTravelNewInfoListenerAdapter(Context context, List<Replys> replysList,
			ReplyInterface repInterface) {
		this.context = context;
		this.replysList = replysList;
		this.repInterface = repInterface;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return replysList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return replysList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Replys replys = replysList.get(position);
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.micro_blogs_listener_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.product_servicer_icon_iv = (RoundImageView) convertView.findViewById(R.id.product_servicer_icon_iv);

		// if()
		holder.replyName = (TextView) convertView.findViewById(R.id.tvName);
		// holder.replyToName =
		// (TextView)convertView.findViewById(R.id.comments_reply_toname);
		holder.commentDate = (TextView) convertView.findViewById(R.id.tvTime);
		holder.commentContent = (TextView) convertView.findViewById(R.id.tvContent);
		// holder.replyBtn =
		// (TextView)convertView.findViewById(R.id.comments_reply_btn);
		// 时间
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			long time = (date.getTime() - format.parse(replysList.get(position).getCreateTime()).getTime()) / 1000;
			long day1 = time / (24 * 3600);
			long hour1 = time % (24 * 3600) / 3600;
			long minute1 = time % 3600 / 60;
			if(time<0){
				time=0;
			}
			if (day1 >= 0) {
				holder.commentDate.setText(day1 + "天前");
			} else if (hour1 > 0) {
				holder.commentDate.setText(hour1 + "小时前");
			} else if (minute1 > 0) {
				holder.commentDate.setText(minute1 + "分前");
			} else {
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
		holder.product_servicer_icon_iv = (RoundImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
		if (!StringUtils.isEmpty(replys.getPublisher().getAvatar())) {
			if (holder.product_servicer_icon_iv.getTag() != null) {
				int i = (Integer) holder.product_servicer_icon_iv.getTag();
				if (i != position) {
					holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
				}
			} else {
				holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			ImageLoader.getInstance().displayImage(Urls.imgHost + replys.getPublisher().getAvatar(),
					holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
		} else {
			holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
		}
		holder.product_servicer_icon_iv.setTag(position);
		String name = "";
		if (StringUtils.verifyIsPhone(replys.getPublisher().getNickname())) {
			name = StringUtils.getSecretStr(replys.getPublisher().getNickname());
			holder.replyName.setText(name);
		} else {
			holder.replyName.setText(replys.getPublisher().getNickname());
		}
		if (StringUtils.verifyIsPhone(replys.getReplyTo().getNickname())) {
			name = StringUtils.getSecretStr(replys.getReplyTo().getNickname());
			holder.commentContent.setText("回复@" + name + ": " + StringUtils.unicodeRevertString(replys.getContent()));
		} else {
			holder.commentContent.setText("回复@" + replys.getReplyTo().getNickname() + ": "
					+ StringUtils.unicodeRevertString(replys.getContent()));
		}
		// holder.replyName.setText(replys.getPublisher().getNickname());
		// holder.replyToName.setText(replys.getReplyTo().getNickname());
		// holder.commentContent.setText("回复@" +
		// replys.getReplyTo().getNickname() + ": "+
		// StringUtils.unicodeRevertString(replys.getContent()));
		// holder.replyBtn.setTag(replys);
		// holder.replyBtn.setOnClickListener(replyClickListener);
		holder.replyName.setTag(position);
		convertView.setOnClickListener(replyClickListener);
		return convertView;
	}

	private OnClickListener replyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.replayView:
				TextView tvName = (TextView) v.findViewById(R.id.tvName);
				final int position = (Integer) tvName.getTag();
				if (PreferenceUtil.getUserId().equals(replysList.get(position).getPublisher().getUserId())) {
					ToastUtil.showToast(context, "不能对自己的回复进行回复");
					return;
				}
				String name = "";
				if (StringUtils.verifyIsPhone(replysList.get(position).getReplyTo().getNickname())) {
					name = StringUtils.getSecretStr(replysList.get(position).getReplyTo().getNickname());
				} else {
					name = replysList.get(position).getReplyTo().getNickname();
				}
				new ReplyDialog(context, new OnSendReplyListener() {
					@Override
					public void sendReply(String comment) {
						repInterface.replyToPublisher(replysList.get(position).getId(), comment);
					}
				}, "@" + name + ": ").show();
				break;
			default:
				break;
			}
		}
	};

	public interface ReplyInterface {
		void replyToPublisher(String replyId, String content);
	}

	public class ViewHolder {
		public RoundImageView product_servicer_icon_iv;

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

	public void clearAll() {
		this.replysList.clear();
		notifyDataSetChanged();
	}

	public void addAll(List<Replys> arrayList) {
		// TODO Auto-generated method stub
		this.replysList = arrayList;
		notifyDataSetChanged();
	}
}
