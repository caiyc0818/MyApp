package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MicroBlogsCommentsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Comments> commentsList = new ArrayList<>();

	public ArrayList<Comments> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(ArrayList<Comments> commentsList) {
		this.commentsList = commentsList;
	}

	public MicroBlogsCommentsAdapter(Context mContext, ArrayList<Comments> commentsList) {
		this.mContext = mContext;
		this.commentsList = commentsList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Comments comments = commentsList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.review_linearlayout, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvContent.setText(StringUtils.unicodeRevertString(comments.getContent()));
		if (StringUtils.verifyIsPhone(comments.getUser().getNickname())) {
			holder.tvName.setText(StringUtils.getSecretStr(comments.getUser().getNickname()));
		} else {
			holder.tvName.setText(comments.getUser().getNickname());
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvContent;

	}
}
