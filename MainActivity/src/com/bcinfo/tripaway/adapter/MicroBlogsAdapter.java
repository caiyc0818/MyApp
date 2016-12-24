package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.R.id;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.LoginActivity;
import com.bcinfo.tripaway.activity.MainActivity;
import com.bcinfo.tripaway.activity.MicroBlogsNewInfoActivity;
import com.bcinfo.tripaway.activity.SquarePicDetailActivity;
import com.bcinfo.tripaway.activity.SquareSerialDetailActivity;
import com.bcinfo.tripaway.activity.UserMicroCommentActivity;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.MessageInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MicroBlogsAdapter extends BaseAdapter implements OnClickListener {

	private ArrayList<TripArticle> tripArticleList = new ArrayList<>();
	private Context mContext;

	private boolean isComplete = true;

	MicroBlogsCommentsAdapter microBlogsCommentsAdapter;

	// private ArrayList<Comments> commentsListAll = new ArrayList<>();
	private DisplayMetrics dm;

	public MicroBlogsAdapter(Context mContext, ArrayList<TripArticle> tripArticleList) {
		isComplete = true;
		this.mContext = mContext;
		this.tripArticleList = tripArticleList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tripArticleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tripArticleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		TripArticle tripArticle = tripArticleList.get(position);
		if (convertView == null) {
			// microBlogsCommentsAdapter = new
			// MicroBlogsCommentsAdapter(mContext, commentsList);
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.micro_blogs_travel, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.product_servicer_icon_iv = (RoundImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
		holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
		holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
		holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
		holder.tv_picnum = (TextView) convertView.findViewById(R.id.tv_picnum);
		holder.map_location_text = (TextView) convertView.findViewById(R.id.map_location_text);
		holder.map_location = (ImageView) convertView.findViewById(R.id.map_location);
		holder.image1 = (ImageView) convertView.findViewById(R.id.image1);
		holder.imageall = (ImageView) convertView.findViewById(R.id.imageall);
		holder.tv_zannum = (TextView) convertView.findViewById(R.id.tv_zannum);
		holder.tv_reviewnum = (TextView) convertView.findViewById(R.id.tv_reviewnum);
		holder.review_listview = (MyListView) convertView.findViewById(R.id.review_listview);
		// holder.review_listview.setAdapter(microBlogsCommentsAdapter);
		holder.imageRelative = (RelativeLayout) convertView.findViewById(R.id.imageRelative);
		holder.zanRelative = (RelativeLayout) convertView.findViewById(R.id.zanrelative);
		holder.reviewRelative = (RelativeLayout) convertView.findViewById(R.id.reviewRelative);
		holder.shareRelative = (RelativeLayout) convertView.findViewById(R.id.shareRelative);
		holder.addressRelative = (LinearLayout) convertView.findViewById(R.id.addressRelative);
		holder.line = (View) convertView.findViewById(R.id.line);
		holder.zanRelative.setTag(position);
		holder.reviewRelative.setTag(position);
		holder.shareRelative.setTag(position);
		holder.imageRelative.setTag(position);
		holder.imageRelative.setOnClickListener(this);
		holder.zanRelative.setOnClickListener(this);
		holder.reviewRelative.setOnClickListener(this);
		holder.shareRelative.setOnClickListener(this);
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getAvatar())) {
			if (holder.product_servicer_icon_iv.getTag() != null) {
				int i = (Integer) holder.product_servicer_icon_iv.getTag();
				if (i != position) {
					holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
				}
			} else {
				holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPublisher().getAvatar(),
					holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
		} else {
			holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
		}
		final UserInfo user = tripArticle.getPublisher();
		holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent blogIntentForPersonal = new Intent(v.getContext(),
				// VistorHomepageActivity.class);
				// blogIntentForPersonal.putExtra("userId", userId);
				ActivityUtil.toPersonHomePage(user, mContext);
				// v.getContext().startActivity(blogIntentForPersonal);
				((Activity) mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
			}
		});
		String name = "";
		if(StringUtils.isEmpty(tripArticle.getPublisher().getNickname()))
			holder.tvName.setText("");
		else
		if (tripArticle.getPublisher().getNickname().equals(tripArticle.getPublisher().getMobile())) {
			name = StringUtils.getSecretStr(tripArticle.getPublisher().getNickname());
			holder.tvName.setText(name);
		} else {
			holder.tvName.setText(tripArticle.getPublisher().getNickname());
		}
		holder.tvName.setTag(position);
		if (tripArticle.getPublisher().getOrgRole() != null
				&& !StringUtils.isEmpty(tripArticle.getPublisher().getOrgRole().getRoleName())) {
			holder.tvPost.setVisibility(View.VISIBLE);
			holder.tvPost.setText("(" + tripArticle.getPublisher().getOrgRole().getRoleName() + ")");
		} else {
			holder.tvPost.setVisibility(View.GONE);
		}
		String time = DateUtil.setTime(tripArticle.getPublishTime());
		holder.tvTime.setText(StringUtils.isEmpty(time) ? "" : time);
		holder.tvDescription.setText(tripArticle.getDescription());
		int num = tripArticle.getPictureList().size();
		if (num > 0) {

			holder.tv_picnum.setText(String.valueOf(num));
		} else {
			holder.tv_picnum.setText("0");
		}
		if (!StringUtils.isEmpty(tripArticle.getLocation()) && !tripArticle.getLocation().equals("不显示")) {
			holder.addressRelative.setVisibility(View.VISIBLE);
			holder.map_location_text.setText(tripArticle.getLocation());
			// holder.map_location.setVisibility(View.VISIBLE);
			// holder.map_location_text.setVisibility(View.VISIBLE);
		} else {
			// holder.map_location.setVisibility(View.GONE);
			// holder.map_location_text.setVisibility(View.GONE);
			holder.addressRelative.setVisibility(View.GONE);
		}
		// 赞的数目
		holder.tv_zannum.setText(StringUtils.isEmpty(tripArticle.getLikes()) || tripArticle.getLikes().equals("0") ? ""
				: tripArticle.getLikes());
		if (tripArticle.getIsLike().equals("0")) {
			holder.image1.setBackgroundResource(R.drawable.nozan);
		} else if (tripArticle.getIsLike().equals("1")) {
			holder.image1.setBackgroundResource(R.drawable.zan2x);
		}
		// 评论数目
		holder.tv_reviewnum
				.setText(StringUtils.isEmpty(tripArticle.getComments()) || tripArticle.getComments().equals("0") ? ""
						: tripArticle.getComments());
		if (StringUtils.isEmpty(tripArticle.getComments()) || tripArticle.getComments().equals("0")) {
			holder.line.setVisibility(View.GONE);
		} else {
			holder.line.setVisibility(View.VISIBLE);

		}
		//
		MicroBlogsCommentsAdapter adapter1 = (MicroBlogsCommentsAdapter) holder.review_listview.getAdapter();
		int count = 0;
		if (adapter1 == null) {
			ArrayList<Comments> commentsList = new ArrayList<Comments>();
			for (Comments comments : tripArticle.getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			holder.review_listview.setAdapter(new MicroBlogsCommentsAdapter(mContext, commentsList));
		} else {
			count = 0;
			MicroBlogsCommentsAdapter adapter = (MicroBlogsCommentsAdapter) adapter1;
			ArrayList<Comments> commentsList = adapter.getCommentsList();
			commentsList.clear();
			for (Comments comments : tripArticle.getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			adapter.notifyDataSetChanged();
		}
		// holder.review_listview.requestLayout();
		// microBlogsCommentsAdapter.notifyDataSetChanged();
		if (tripArticle.getPictureList() == null || tripArticle.getPictureList().size() == 0) {
			((View) holder.imageall.getParent()).setVisibility(View.GONE);
		} else {
			((View) holder.imageall.getParent()).setVisibility(View.VISIBLE);
			if (holder.imageall.getTag() != null) {
				int i = (Integer) holder.imageall.getTag();
				if (i != position) {
					holder.imageall.setImageResource(R.drawable.ic_launcher);
				}
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			// if (tripArticle.getPictureList() != null &&
			// tripArticle.getPictureList().size() > 0) {
			// RelativeLayout.LayoutParams ls = new
			// RelativeLayout.LayoutParams(dm.widthPixels - 14,
			// (dm.widthPixels - 14) *
			// Integer.parseInt(tripArticle.getPictureList().get(0).getHeight())
			// /
			// Integer.parseInt(tripArticle.getPictureList().get(0).getWidth()));
			// holder.imageall.setScaleType(ScaleType.CENTER_CROP);
			// holder.imageall.setLayoutParams(ls);
			WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			float width = wm.getDefaultDisplay().getWidth();
			ViewGroup.LayoutParams lp = holder.imageall.getLayoutParams();
			lp.width = (int) width;
			lp.height = (int) (width / 620 * 348);
			holder.imageall.setLayoutParams(lp);
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPictureList().get(0).getUrl(),
					holder.imageall, AppConfig.options(R.drawable.ic_launcher));
			// }
		}
		holder.imageall.setTag(position);
		holder.product_servicer_icon_iv.setTag(position);
		convertView.setOnClickListener(this);
		return convertView;
	}

	class ViewHolder {
		RoundImageView product_servicer_icon_iv;
		TextView tvName;
		TextView tvPost;
		TextView tvTime;
		TextView tvDescription;
		TextView tv_picnum;
		TextView map_location_text;
		TextView tv_zannum;
		TextView tv_reviewnum;
		ImageView image1;// 赞
		ImageView imageall;
		ImageView map_location;
		MyListView review_listview;
		RelativeLayout imageRelative;// 图片点击
		RelativeLayout zanRelative;
		RelativeLayout reviewRelative;
		RelativeLayout shareRelative;
		LinearLayout addressRelative;
		View line;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zanrelative:
			if (!isComplete) {
				return;
			}
			isComplete = false;
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(mContext, LoginActivity.class);
				((Activity) mContext).startActivity(it);
				isComplete = true;
				return;
			}

			if ((tripArticleList.get((Integer) v.getTag())).getIsLike().equals("0")
					|| StringUtils.isEmpty((tripArticleList.get((Integer) v.getTag())).getIsLike())) {
				setZanInfo((Integer) v.getTag(), 1);
			} else if ((tripArticleList.get((Integer) v.getTag())).getIsLike().equals("1")) {
				setZanInfo((Integer) v.getTag(), 0);
			}
			break;
		case R.id.reviewRelative:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(mContext, LoginActivity.class);
				((Activity) mContext).startActivity(it);
				return;
			}
			TripArticle tripArticle = (tripArticleList.get((Integer) v.getTag()));
			Intent reviewintent = new Intent(mContext, UserMicroCommentActivity.class);
			reviewintent.putExtra("objectType", "0");
			reviewintent.putExtra("microId", tripArticle.getPhotoId());
			String counts = StringUtils.isEmpty(tripArticle.getComments()) ? "0" : tripArticle.getComments();
			if (counts.equals("0")) {
				reviewintent.putExtra("count", 0);
			} else {
				reviewintent.putExtra("count", Integer.parseInt(counts));
			}

			((Activity) mContext).startActivityForResult(reviewintent, 3);
			((Activity) mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
			break;
		case R.id.shareRelative:
			tripArticle = (tripArticleList.get((Integer) v.getTag()));
			if (tripArticle.getPictureList() != null && tripArticle.getPictureList().size() > 0) {
				ShareSelectDialog shareSelectDialog = new ShareSelectDialog(mContext,
						tripArticle.getPictureList().get(0).getUrl(), "发表微游记", tripArticle.getPublisher().getNickname(),
						tripArticle.getPhotoId(), Urls.ShareUrlOfPhoto, tripArticle.getPublisher().getNickname());
				shareSelectDialog.show();
			}
			break;
		case R.id.linearlayout:
			TextView tvname = (TextView) v.findViewById(R.id.tvName);
			int position = (Integer) tvname.getTag();
			// Intent microBlogsNewInfoIntents = new Intent(mContext,
			// SquareSerialDetailActivity.class);
//			Intent microBlogsNewInfoIntents = new Intent(mContext, SquarePicDetailActivity.class);
			Intent microBlogsNewInfoIntents = new Intent(mContext, MicroBlogsNewInfoActivity.class);
			microBlogsNewInfoIntents.putExtra("tripArticle", tripArticleList.get(position));
			((Activity) mContext).startActivityForResult(microBlogsNewInfoIntents, 0);
			((Activity) mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
			break;
		case R.id.imageRelative:
			Intent microBlogsNewInfoIntent = new Intent(mContext, MicroBlogsNewInfoActivity.class);
			microBlogsNewInfoIntent.putExtra("tripArticle", tripArticleList.get((Integer) v.getTag()));
			((Activity) mContext).startActivityForResult(microBlogsNewInfoIntent, 0);
			((Activity) mContext).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
			break;
		default:
			break;
		}

	}

	private void setZanInfo(final int position, final int like) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("objectId", tripArticleList.get(position).getPhotoId());
			jsonObject.put("objectType", "tripstory");
			jsonObject.put("opType", like);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.set_like, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					isComplete = true;
					MicroBlogsAdapter.this.notifyDataSetChanged();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					isComplete = true;
					MicroBlogsAdapter.this.notifyDataSetChanged();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {

						ArrayList<TripArticle> temp = new ArrayList<>();
						tripArticleList.get(position).setIsLike(String.valueOf(like));
						String likes = tripArticleList.get(position).getLikes();
						int num = StringUtils.isEmpty(likes) ? 0 : Integer.parseInt(likes);
						if (like == 0 && num != 0) {
							tripArticleList.get(position).setLikes((num - 1) + "");
						} else if (like == 1) {
							tripArticleList.get(position).setLikes((num + 1) + "");
						}
						if (like == 1) {
							Toast.makeText(mContext, "点赞 +1", Toast.LENGTH_SHORT).show();
						}
						temp.addAll(tripArticleList);
						tripArticleList.clear();
						tripArticleList.addAll(temp);
					}
					isComplete = true;
					MicroBlogsAdapter.this.notifyDataSetChanged();
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
