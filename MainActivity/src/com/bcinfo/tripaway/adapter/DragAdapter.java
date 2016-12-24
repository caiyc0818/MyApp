package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.DiscoveryProductResultAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Data;
import com.umeng.socialize.net.v;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 列表数据是否改变 */
	private boolean isListChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 可以拖动的列表（即用户选择的频道列表） */
	public List<Data> channelList;
	/** TextView 频道内容 */
	private TextView item_text;
	/** 要删除的position */
	public int remove_position = -1;
	private int selectedPosition = 0;

	public DragAdapter(Context context, List<Data> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
			holder.item_text = (TextView) convertView.findViewById(R.id.text_item);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
//		String channel = getItem(position);
//		holder.item_text.setText(channel);
//		if ((position == 0) || (position == 1)) {
//			// item_text.setTextColor(context.getResources().getColor(R.color.black));
//			holder.item_text.setEnabled(false);
//		}
//		if (isChanged && (position == holdPosition) && !isItemShow) {
//			holder.item_text.setText("");
//			holder.item_text.setSelected(true);
//			holder.item_text.setEnabled(true);
//			isChanged = false;
//		}
//		if (!isVisible && (position == -1 + channelList.size())) {
//			holder.item_text.setText("");
//			holder.item_text.setSelected(true);
//			holder.item_text.setEnabled(true);
//		}
//		if (remove_position == position) {
//			holder.item_text.setText("");
//		}
//		
		String tab=channelList.get(position).tab;
		boolean isCheck=channelList.get(position).isCheck;
		holder.item_text.setText(tab);
		if(isCheck){
			holder.item_text.setTextColor(context.getResources().getColor(R.color.title_bg));	
		}else {
			holder.item_text.setTextColor(context.getResources().getColor(R.color.black_gray));
		}
		
		return convertView;
	}

//	/** 添加频道列表 */
//	public void addItem(String channel) {
//		channelList.add(channel);
//		isListChanged = true;
//		notifyDataSetChanged();
//	}

	// /** 拖动变更频道排序 */
	// public void exchange(int dragPostion, int dropPostion) {
	// holdPosition = dropPostion;
	// ChannelItem dragItem = getItem(dragPostion);
	// Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" +
	// dropPostion);
	// if (dragPostion < dropPostion) {
	// channelList.add(dropPostion + 1, dragItem);
	// channelList.remove(dragPostion);
	// } else {
	// channelList.add(dropPostion, dragItem);
	// channelList.remove(dragPostion + 1);
	// }
	// isChanged = true;
	// isListChanged = true;
	// notifyDataSetChanged();
	// }

	class viewHolder {
		TextView item_text;
	}

//	/** 获取频道列表 */
//	public List<String> getChannnelLst() {
//		return channelList;
//	}
//
//	/** 设置删除的position */
//	public void setRemove(int position) {
//		remove_position = position;
//		notifyDataSetChanged();
//	}
//
//	/** 删除频道列表 */
//	public void remove() {
//		channelList.remove(remove_position);
//		remove_position = -1;
//		isListChanged = true;
//		notifyDataSetChanged();
//	}
//
//	/** 设置频道列表 */
//	public void setListDate(List<String> list) {
//		channelList = list;
//	}

	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}

	/** 排序是否发生改变 */
	public boolean isListChanged() {
		return isListChanged;
	}

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}