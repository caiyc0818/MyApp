package com.bcinfo.tripaway.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.CustomerHomePageActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.PhotoActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.TouristHomepageActivity;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.getui.util.TimeUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author hanweipeng
 * @date 2015-7-21 下午8:53:36
 */
public class MeaasgeListAdapter extends BaseAdapter {
	private Context mContext;

	// private String imageUrl;

	private LayoutInflater inflater;

	private List<MessageInfo> messageList;

	public MeaasgeListAdapter(Context mContext, List<MessageInfo> messageList) {
		this.mContext = mContext;
		this.messageList = messageList;
		inflater = LayoutInflater.from(mContext);
	}

	public void setMessageList(List<MessageInfo> messageList) {
		this.messageList = messageList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		String senderId = messageList.get(position).getSender().getUserId();
		if (senderId.equals(PreferenceUtil.getUserId())) {
			// 自己发的消息
			return 0;
		} else {
			if (StringUtils.isEmpty(messageList.get(position).getProductInfo().getId())) {
				// 别人发的非产品消息
				return 1;
			} else {
				// 别人发的产品消息
				return 2;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			String senderId = messageList.get(position).getSender().getUserId();
			if (getItemViewType(position) == 1) {// 别人发的非产品消息
				convertView = inflater.inflate(R.layout.left_chat_item, null);
				viewHolder.messageContent = (TextView) convertView.findViewById(R.id.message_content);
				viewHolder.failStatus = (ImageView) convertView.findViewById(R.id.fail_icon);
				viewHolder.sendName = (TextView) convertView.findViewById(R.id.send_name);
			} else if (getItemViewType(position) == 0) {
				// 自己发的消息
				convertView = inflater.inflate(R.layout.right_chat_item, null);
				viewHolder.messageContent = (TextView) convertView.findViewById(R.id.message_content);
				viewHolder.failStatus = (ImageView) convertView.findViewById(R.id.fail_icon);
				viewHolder.sendName = (TextView) convertView.findViewById(R.id.send_name);
			} else {
				// 别人发的产品消息
				convertView = inflater.inflate(R.layout.product_chat_item, null);
				viewHolder.productPost = (ImageView) convertView.findViewById(R.id.product_post);
				viewHolder.productTitle = (TextView) convertView.findViewById(R.id.product_title);
				viewHolder.productTags = (TextView) convertView.findViewById(R.id.product_tag);
				viewHolder.productDays = (TextView) convertView.findViewById(R.id.product_days);
				viewHolder.productDistance = (TextView) convertView.findViewById(R.id.product_distance);
				viewHolder.productLayout = (RelativeLayout) convertView.findViewById(R.id.product_layout);
				viewHolder.sendName = (TextView) convertView.findViewById(R.id.send_name);
			}
			viewHolder.messageTime = (TextView) convertView.findViewById(R.id.message_time);
			viewHolder.senderIcon = (ImageView) convertView.findViewById(R.id.send_icon);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MessageInfo messageInfo = messageList.get(position);

		viewHolder.senderIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				
				
//				 Intent it=new Intent();
//				 it.putExtra("identifyId",
//				 messageInfo.getSender().getUserId());
//				 it.setClass(mContext,ClubMebHomepageActivity.class);
//				 if("司机".equals(messageInfo.getSender().getOrgRole().getRoleName()))
//				 {
//				 it.putExtra("isDriverHomePage", true);
//				
//				 }else
//				 if("admin".equals(messageInfo.getSender().getOrgRole().getRoleCode())){
//				 it.putExtra("userInfo", messageInfo.getSender());
//				 it.setClass(mContext,ClubFirstPageActivity.class);
//				
//				 }else{
//				 it.putExtra("userInfo", messageInfo.getSender());
//				// it.setClass(mContext,CustomerHomePageActivity.class);
//				 it.setClass(mContext,TouristHomepageActivity.class);
//				 }
//				 mContext.startActivity(it);
				ActivityUtil.toPersonHomePage(messageInfo.getSender(), mContext);
			}

		});
		viewHolder.sendName.setText(StringUtils.isEmpty(messageInfo.getSender().getNickname()) ? ""
				: messageInfo.getSender().getNickname());
		if (viewHolder.sendName.getText().length() > 5) {

			viewHolder.sendName.setText((viewHolder.sendName.getText() + "").substring(0, 4) + "...");
		}
		// viewHolder.messageTime.setText(DateUtil.getFormateDate1(messageInfo.getCreatetime()));
		String pattern = messageList.get(position).getPattern();
		if (pattern != null && pattern.equals("notice")) {
			String formateStr = "yyyy-MM-dd";
			viewHolder.messageTime.setText(DateUtil.formateDateToTime1(messageInfo.getCreatetime(), formateStr));
		} else {
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				long time = (date.getTime() - format.parse(messageList.get(position).getCreatetime()).getTime()) / 1000;
				long day1 = time / (24 * 3600);
				long hour1 = time % (24 * 3600) / 3600;
				long minute1 = time % 3600 / 60;
				if (day1 > 0) {
					viewHolder.messageTime.setText(DateUtil.getFormateDate1(messageInfo.getCreatetime()));
				} else if (hour1 > 0) {
					viewHolder.messageTime.setText(hour1 + "小时前");
				} else if (minute1 > 0) {
					viewHolder.messageTime.setText(minute1 + "分钟前");
				} else {
					if (time <= 0) {
						viewHolder.messageTime.setText("刚刚");
					} else {
						viewHolder.messageTime.setText(time + "秒前");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (!StringUtils.isEmpty(messageInfo.getSender().getAvatar())) {
			if (messageInfo.getSender().getAvatar().equals("-1"))
				viewHolder.senderIcon.setVisibility(View.GONE);
			else {
				viewHolder.senderIcon.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(Urls.imgHost + messageInfo.getSender().getAvatar(),
						viewHolder.senderIcon, AppConfig.options(R.drawable.user_defult_photo));
			}
		}

		if (getItemViewType(position) == 0 || getItemViewType(position) == 1) {
			viewHolder.failStatus.setVisibility(View.GONE);
			if (messageInfo.getSendStatus().equals("1")) {
				viewHolder.failStatus.setVisibility(View.VISIBLE);
				viewHolder.failStatus.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((ChatActivity) mContext).reSend(position);

					}
				});
			}
		}
		if (!StringUtils.isEmpty(messageInfo.getImage()) || !StringUtils.isEmpty(messageInfo.getLocalImage())) {
			// imageUrl =messageInfo.getImage();
			Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_photo);
			SpannableString spannableString = addPicture(bitmap, "i");
			viewHolder.messageContent.setText(spannableString);
			ImageSize imageSize = new ImageSize(200, 200);
			if (!StringUtils.isEmpty(messageInfo.getLocalImage())) {
				setImg("file:///" + messageInfo.getLocalImage(), imageSize, viewHolder.messageContent);
			} else {
				if (!StringUtils.isEmpty(messageInfo.getImage())) {
					setImg(Urls.imgHost + messageInfo.getImage(), imageSize, viewHolder.messageContent);
				}
			}
		}
		if (getItemViewType(position) == 2) {
			if (!StringUtils.isEmpty(messageInfo.getProductInfo().getTitleImg())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + messageInfo.getProductInfo().getTitleImg(),
						viewHolder.productPost, AppConfig.options(R.drawable.ic_launcher));
			}

			viewHolder.productTitle.setText(messageInfo.getProductInfo().getTitle());
			String tagStr = "";
			for (int i = 0; i < messageInfo.getProductInfo().getTopics().size(); i++) {
				if (i != messageInfo.getProductInfo().getTopics().size() - 1) {
					tagStr = tagStr + messageInfo.getProductInfo().getTopics().get(i) + ".";
				} else {
					tagStr = tagStr + messageInfo.getProductInfo().getTopics().get(i);
				}
			}
			viewHolder.productTags.setText(tagStr);
			viewHolder.productDays.setText(messageInfo.getProductInfo().getDays());
			viewHolder.productDistance.setText(messageInfo.getProductInfo().getDistance());
			viewHolder.productLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = null;
					ProductNewInfo productNewInfo = messageInfo.getProductInfo();
					if (productNewInfo.getProductType().equals("single")) {
						if (productNewInfo.getServices().equals("traffic")) {
							intent = new Intent(mContext, CarProductDetailActivity.class);
							intent.putExtra("productId", productNewInfo.getId());
						} else if (productNewInfo.getServices().equals("stay")) {
							intent = new Intent(mContext, HouseProductDetailActivity.class);
							intent.putExtra("productId", productNewInfo.getId());
						} else {
							intent = new Intent(mContext, GrouponProductNewDetailActivity.class);
							intent.putExtra("productId", productNewInfo.getId());
						}
					} else if (productNewInfo.getProductType().equals("base")
							|| productNewInfo.getProductType().equals("customization")) {
						intent = new Intent(mContext, GrouponProductNewDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					} else if (productNewInfo.getProductType().equals("team")) {

						// GrouponProductNewDetailActivity
						intent = new Intent(mContext, GrouponProductNewDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					}
					mContext.startActivity(intent);
					((BaseActivity) mContext).activityAnimationOpen();
				}
			});
		}
		if (StringUtils.isEmpty(messageInfo.getLocalImage()) && StringUtils.isEmpty(messageInfo.getImage())
				&& getItemViewType(position) != 2) {
			viewHolder.messageContent.setText("");
			viewHolder.messageContent.setText(messageInfo.getContent());
		}
		return convertView;
	}

	private void setImg(String url, ImageSize imageSize, final TextView textView) {

		ImageLoader.getInstance().loadImage(url, imageSize, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				// Bitmap bitmap =
				// ImageLoader.getInstance().loadImageSync(messageInfo.getImgUrl(),
				// imageSize);
				Bitmap newBitmap = null;
				if (imageUri.contains("file:///")) {
					int angle = BitmapUtil.getBitmapDegree(imageUri.substring("file:///".length()));
					newBitmap = BitmapUtil.rotaingImageView(angle, loadedImage);
				} else {
					newBitmap = loadedImage;
				}
				SpannableString spannableString = addPicture(newBitmap, "i");
				textView.setText(spannableString);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ArrayList<String> photoUrls = new ArrayList<String>();
						photoUrls.add(imageUri);
						// TODO Auto-generated method stub
						Intent intent = new Intent(mContext, PhotoActivity.class);
						intent.putExtra("photos", photoUrls);
						mContext.startActivity(intent);
					}
				});
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 添加图片
	 * 
	 * @param context
	 * @param bmpUrl
	 *            图片路径
	 * @param spannableString
	 * @return
	 */
	private SpannableString addPicture(Bitmap bitmap, String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}

		ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	static class ViewHolder {
		public TextView messageTime;

		public TextView messageContent;

		public ImageView senderIcon;

		public ImageView failStatus;

		public ImageView productPost;

		public TextView productTitle;

		public TextView productTags;

		public TextView productDays;

		public TextView productDistance;

		public TextView sendName;
		public RelativeLayout productLayout;
	}

	public interface ReSendMessageInterface {
		public void reSend(int position);
	}
}
