package com.bcinfo.tripaway.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Cats;
import com.bcinfo.tripaway.bean.Cust;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CatsFragment extends BaseFragment {

	private Context mContext;
	
	private OperationListener operationListener;

	private Cats cat;

	private RoundImageView mCatsIv;

	private TextView mCatsName;

	public CatsFragment() {

	}

	public CatsFragment(Context mContext, Cats cat,OperationListener operationListener) {
		this.mContext = mContext;
		this.cat = cat;
		this.operationListener = operationListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.club_cats_item, null);
		mCatsName = (TextView) view.findViewById(R.id.cats_name);
		mCatsIv = (RoundImageView) view.findViewById(R.id.cats_photo);
		mCatsIv.setImageResource(R.drawable.ic_launcher);
		if (!StringUtils.isEmpty(cat.getCover())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + cat.getCover(), mCatsIv,
					AppConfig.options(R.drawable.ic_launcher));
		}
		mCatsName.setText(cat.getCustTitle());
		mCatsIv.setTag(cat);
		
		mCatsIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCatsIv.getParent().requestDisallowInterceptTouchEvent(true);
				Cats id = (Cats) v.getTag();
				operationListener.queryProductById(id);
			}
		});
		return view;
	}
	public interface OperationListener {
		void queryProductById(Cats cust);
	}
}
