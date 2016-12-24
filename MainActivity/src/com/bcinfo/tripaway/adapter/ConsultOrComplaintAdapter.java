package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.activity.ReplayActivity;
import com.bcinfo.tripaway.bean.FeedBack;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.fragment.CustomizationFragment;
import com.bcinfo.tripaway.getui.util.TimeUtil;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
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
public class ConsultOrComplaintAdapter extends BaseSwipeAdapter {
	private static final String TAG = "MessageListAdapter";

	private Context mContext;

	private List<FeedBack> feedBackList;
	
	private String type;

	// private DelectListener mDelectListener;

	public ConsultOrComplaintAdapter(Context context,
			List<FeedBack> feedBackList,String type) {
		this.mContext = context;
		this.feedBackList = feedBackList;
		this.type=type;
	}

	// public MessageListAdapter(Context context, LinkedList<RecentItem> list,
	// DelectListener listener)
	// {
	// // TODO Auto-generated constructor stub
	// this.mContext = context;
	// this.mItemList = list;
	// this.mDelectListener = listener;
	// mMessageDB = Constant.getInstance().getMessageDB();
	// mRecentDB = Constant.getInstance().getRecentDB();
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feedBackList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feedBackList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// @Override
	// public View getView(final int position, View convertView, ViewGroup
	// parent)
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
	// // holder.delectButton =
	// (ImageView)convertView.findViewById(R.id.delect_button);
	// holder.itemLeftLayout =
	// (LinearLayout)convertView.findViewById(R.id.message_item_left_layout);
	// holder.userPhoto =
	// (MoreCircleImageView)convertView.findViewById(R.id.message_user_photo);
	// holder.userName =
	// (TextView)convertView.findViewById(R.id.message_user_name);
	// holder.sendTime =
	// (TextView)convertView.findViewById(R.id.message_send_time);
	// holder.messageNumber =
	// (TextView)convertView.findViewById(R.id.message_number);
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
	// Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(),
	// Constant.heads[i]);
	// bitmapList.add(photo);
	// }
	// holder.userPhoto.setImageBitmaps(bitmapList);
	// //
	// ImageLoader.getInstance().displayImage(mItemList.get(position).getUserPhotoUrl(),
	// holder.userPhoto);
	// holder.userName.setText(item.getName());
	// holder.lastWords.setText(convertNormalStringToSpannableString(item.getMessage()),
	// BufferType.SPANNABLE);
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

	public class ViewHolder {
		/**
		 * 删除按钮
		 */
		public ImageView delectButton;

		/**
		 * 左边布局
		 */
		public LinearLayout itemLeftLayout;

		public TextView content;

		public TextView createTime;
		public TextView status;
	}

	// public interface DelectListener
	// {
	// public void delect(int position);
	// }

	@Override
	public int getSwipeLayoutResourceId(int position) {
		// TODO Auto-generated method stub
		return R.id.swipe;
	}

	@Override
	public View generateView(final int position, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.consult_or_complaint_list_item, null);
		final SwipeLayout swipeLayout = (SwipeLayout) v
				.findViewById(getSwipeLayoutResourceId(position));
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 点击完成之后，关闭删除menu
				if (swipeLayout.getOpenStatus().equals(Status.Open)) {
					// LogUtil.i(TAG, "关闭item，隐藏删除按钮", position + "");
					swipeLayout.close();
				} else {
					LogUtil.i(TAG, "跳转至聊天页面", position + "");
					Intent intent = new Intent(mContext, ReplayActivity.class);
					intent.putExtra("queueId", feedBackList.get(position)
							.getFeedBackId());
					intent.putExtra("type", type);
					mContext.startActivity(intent);
					((Activity) mContext).overridePendingTransition(
							R.anim.activity_new, R.anim.activity_out);
				}
			}
		});
		// 添加删除布局的点击事件
		v.findViewById(R.id.ll_menu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 点击完成之后，关闭删除menu
				if (swipeLayout.getOpenStatus().equals(Status.Open)) {
//					PreferenceUtil.setHaveDeleteId(feedBackList.get(position)
//							.getFeedBackId());
					// QueuesListDB.getInstance().deleteQueuesById(feedBackList.get(position).getFeedBackId());
					delFeedBack(position);

					swipeLayout.close();
				}
			}
		});
		return v;
	}

	@Override
	public void fillValues(int position, View convertView) {
		// TODO Auto-generated method stub
		LinearLayout itemLeftLayout = (LinearLayout) convertView
				.findViewById(R.id.message_item_left_layout);
		TextView createTime = (TextView) convertView
				.findViewById(R.id.create_time);
		TextView status = (TextView) convertView.findViewById(R.id.status);
		TextView content = (TextView) convertView.findViewById(R.id.content);

		// for (int i = 0; i < 5; i++)
		// {
		// Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(),
		// Constant.heads[i]);
		// bitmapList.add(photo);
		// }
		// userPhoto.setImageBitmaps(bitmapList);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date;
		try {
			date = format.parse(feedBackList.get(position).getCreateTime());
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
			createTime.setText(format1.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statusType = feedBackList.get(position).getStatus();
		;
		if (statusType.equals("init"))
			status.setText("初始化状态");
		else if (statusType.equals("process"))
			status.setText("进行中");
		if (statusType.equals("finish"))
			status.setText("已完成");
		content.setText(feedBackList.get(position).getContent());

	}

	private void delFeedBack(final int position

	) {
		HttpUtil.delete(Urls.del_consultation
				+ feedBackList.get(position).getFeedBackId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						if ("00000".equals(response.optString("code"))) {
							ToastUtil.showToast(mContext, "删除成功");
							feedBackList.remove(position);
							if (feedBackList.size() > 0) {
								// setQueueRead(feedBackList.get(position).getFeedBackId());
								notifyDataSetChanged();
							}

						} else {
							ToastUtil.showToast(mContext, "删除失败");
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i("CustomizationFragment",
								"CustomizationFragment", responseString);
					}
				});

	}
}
