package com.bcinfo.tripaway.fragment;

import im.yixin.sdk.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.sax2.Driver;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity.OnLoadMoreListener;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ProductJoinMebActivity;
import com.bcinfo.tripaway.adapter.ExpAdapter;
import com.bcinfo.tripaway.adapter.ExperienceListAdapter;
import com.bcinfo.tripaway.adapter.FamousCommentAdapter;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductInfoAdapter;
import com.bcinfo.tripaway.adapter.TravelAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.Experience;
import com.bcinfo.tripaway.bean.ExperienceDetail;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.FamousComment;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.MyGridView;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.R.bool;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ExpericeInfoFragment extends Fragment implements OnLoadMoreListener, OnClickListener {

	// ArrayList<Experiences> mItemList ;
	// private ListView expericeDetailList;
	//
	// private ExperienceListAdapter expericesAdapter;

	private FlowLayout serviceArieaFlow;
	private FlowLayout serviceContentFlow;
	private FlowLayout languageFlow;

	private LinearLayout otherInfoLayout;
	private LinearLayout serviceArieaLayout;
	private LinearLayout serviceContentLayout;
	private LinearLayout languageLayout;
	private LinearLayout educationLayout;
	private LinearLayout schoolLayout;
	private LinearLayout addressLayout;
	private LinearLayout showAllReferLayout;
	private LinearLayout famousCommentLayout;

	private TextView education;
	private TextView school;
	private TextView address;
	private TextView showAllInfo;
	private TextView showAllRefer;
	private TextView placeNum;
	private TextView footprintNum;

	private ListView referListview;
	private ListView explistview;

	private boolean isShowALLInfo = false;
	private boolean isShowALLRefer = false;

	private UserInfo user;
	// 判断空界面的数据
	private UserInfo user1=new UserInfo();
	private FamousCommentAdapter famousCommentAdapter;
	private ExpAdapter expAdapter;

	private List<FamousComment> famousCommentList = new ArrayList<FamousComment>();
	private List<Experience> experienceList = new ArrayList<Experience>();
	private int pageSize = 10;
	private int pageNum = 1;
	private ImageView noImageView;
	private LinearLayout linearLayout;

	public ExpericeInfoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mytravel_info, null);// 注意不要指定父视图
		initView(view);
		queryExp();
		// expericesAdapter = new ExperienceListAdapter(getActivity(),
		// mItemList);
		// expericeDetailList.setAdapter(expericesAdapter);
		if (user1.getTag() == null && user1.getExts() == null && user1.getLanguagesList().size() == 0
				&& user1.getAddress() == null && user1.getFamousCommentsList().size() == 0
				&& experienceList.size() == 0) {
			noImageView.setVisibility(View.VISIBLE);
			linearLayout.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initView(View view) {
	
		// expericeDetailList = (ListView) view
		// .findViewById(R.id.experice_detail_listview);
		noImageView = (ImageView) view.findViewById(R.id.noimage);
		linearLayout = (LinearLayout) view.findViewById(R.id.ll);
		serviceArieaFlow = (FlowLayout) view.findViewById(R.id.serviceArieaFlow);
		serviceContentFlow = (FlowLayout) view.findViewById(R.id.serviceContentFlow);
		languageFlow = (FlowLayout) view.findViewById(R.id.languageFlow);

		otherInfoLayout = (LinearLayout) view.findViewById(R.id.other_info_layout);
		serviceArieaLayout = (LinearLayout) view.findViewById(R.id.serviceArieaLayout);
		serviceContentLayout = (LinearLayout) view.findViewById(R.id.serviceContentLayout);
		languageLayout = (LinearLayout) view.findViewById(R.id.languageLayout);
		educationLayout = (LinearLayout) view.findViewById(R.id.educationLayout);
		schoolLayout = (LinearLayout) view.findViewById(R.id.schoolLayout);
		addressLayout = (LinearLayout) view.findViewById(R.id.addressLayout);
		showAllReferLayout = (LinearLayout) view.findViewById(R.id.show_all_refer_layout);
		famousCommentLayout = (LinearLayout) view.findViewById(R.id.famousCommentLayout);

		education = (TextView) view.findViewById(R.id.education);
		school = (TextView) view.findViewById(R.id.school);
		address = (TextView) view.findViewById(R.id.address);
		showAllInfo = (TextView) view.findViewById(R.id.show_all_info);
		showAllRefer = (TextView) view.findViewById(R.id.show_all_refer);
		placeNum = (TextView) view.findViewById(R.id.place_num);
		footprintNum = (TextView) view.findViewById(R.id.footprint_num);
		showAllInfo.setOnClickListener(this);
		showAllRefer.setOnClickListener(this);

		referListview = (ListView) view.findViewById(R.id.refer_listview);
		referListview.setFocusable(false);
		explistview = (ListView) view.findViewById(R.id.exp_listview);
		explistview.setFocusable(false);
		if (user == null)
			return;
		Tags tag = user.getTag();
		boolean check=false;
		if (tag != null && tag.getFootprints() != null && tag.getFootprints().size() > 0) {
			user1.setTag(user.getTag());
			addFlowView(tag.getFootprints(), serviceArieaFlow);
			if (showAllInfo.getVisibility() == View.GONE)
				showAllInfo.setVisibility(View.VISIBLE);
			check=true;
		} else {
			serviceArieaLayout.setVisibility(View.GONE);
		}
		if (tag != null && tag.getServAreas() != null && tag.getServAreas().size() > 0) {
			addFlowView(tag.getServAreas(), serviceContentFlow);
			if (showAllInfo.getVisibility() == View.GONE)
				showAllInfo.setVisibility(View.VISIBLE);
			check=true;
		} else {
			serviceContentLayout.setVisibility(View.GONE);
		}
		if (user.getLanguagesList().size() > 0) {
			user1.setLanguagesList(user.getLanguagesList());
			addFlowView(user.getLanguagesList(), languageFlow);
			if (showAllInfo.getVisibility() == View.GONE)
				showAllInfo.setVisibility(View.VISIBLE);
			check=true;
		} else {
			languageLayout.setVisibility(View.GONE);
		}
		
		HashMap<String, String> exts = user.getExts();
		if (exts != null) {
			user1.setExts(user.getExts());
			if (!StringUtils.isEmpty(exts.get("education"))) {
				education.setText(exts.get("education"));
				if (showAllInfo.getVisibility() == View.GONE)
					showAllInfo.setVisibility(View.VISIBLE);
			} else
				educationLayout.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(exts.get("school"))) {
				school.setText(exts.get("school"));
				if (showAllInfo.getVisibility() == View.GONE)
					showAllInfo.setVisibility(View.VISIBLE);
			} else
				schoolLayout.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(user.getAddress())) {
			user1.setAddress(user.getAddress());
			address.setText(user.getAddress());
			if (showAllInfo.getVisibility() == View.GONE)
				showAllInfo.setVisibility(View.VISIBLE);
		} else
			addressLayout.setVisibility(View.GONE);
		if(!check){
			showAllInfo.setVisibility(View.GONE);
			otherInfoLayout.setVisibility(View.VISIBLE);
		}
//		if (showAllInfo.getVisibility() == View.GONE) {
//			((View) showAllInfo.getParent()).setVisibility(View.GONE);
//		}
		if (user.getFamousCommentsList().size() > 0) {
			user1.setFamousCommentsList(user.getFamousCommentsList());
			for (int i = 0; i < user.getFamousCommentsList().size(); i++) {
				famousCommentList.add(user.getFamousCommentsList().get(i));
				if (i == 1)
					break;
			}
			if (user.getFamousCommentsList().size() <= 2) {
				showAllReferLayout.setVisibility(View.GONE);
			}
			famousCommentAdapter = new FamousCommentAdapter(getActivity(), famousCommentList);
			referListview.setAdapter(famousCommentAdapter);
		} else {
			famousCommentLayout.setVisibility(View.GONE);
		}
	}

	public void setData(UserInfo user) {
		this.user = user;
		// this.mItemList = mItemList;
		// expericesAdapter = new ExperienceListAdapter(getActivity(),
		// mItemList);
		// expericeDetailList.setAdapter(expericesAdapter);
	}

	public void refeshData() {
		// expericesAdapter.notifyDataSetChanged();
		((ClubMebHomepageActivity) getActivity()).resetViewPagerHeight(1);
	}

	@Override
	public int isNeedLoad() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (experienceList.size() == 0) {
			return 0;
		} else if (experienceList.size() % pageSize == 0) {
			pageNum = pageNum + 1;
			// pagesize = pagesize + 10;
			queryExp();
			return 1;
		}
		return 2;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_all_info:
			isShowALLInfo = !isShowALLInfo;
			if (isShowALLInfo) {
				showAllInfo.setText("收起");
				otherInfoLayout.setVisibility(View.VISIBLE);
			} else {
				showAllInfo.setText("显示全部");
				otherInfoLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.show_all_refer:
			isShowALLRefer = !isShowALLRefer;
			if (isShowALLRefer) {
				showAllRefer.setText("收起");
				famousCommentList.clear();
				for (int i = 0; i < user.getFamousCommentsList().size(); i++) {
					famousCommentList.add(user.getFamousCommentsList().get(i));
				}
			} else {
				showAllRefer.setText("查看全部");
				famousCommentList.clear();
				for (int i = 0; i < user.getFamousCommentsList().size(); i++) {
					famousCommentList.add(user.getFamousCommentsList().get(i));
					if (i == 1)
						break;
				}
			}
			famousCommentAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(getActivity());
			newView.setBackgroundResource(R.drawable.shape_person_info);
			;
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(getActivity(), 5);
			params.bottomMargin = DensityUtil.dip2px(getActivity(), 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void queryExp() {
		RequestParams params = new RequestParams();
		params.put("pagesize", pageSize);
		params.put("pagenum", pageNum);
		params.put("userId", user.getUserId());
		HttpUtil.get(Urls.get_experience, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response.optJSONObject("data");
					if (data != null && data.length() > 0) {
						JSONArray expArray = data.optJSONArray("exp");

						if (expArray == null || expArray.length() == 0) {
							((ClubMebHomepageActivity) getActivity()).noMoreFinish();
							--pageNum;
						}
						String total = data.optString("total");
						String detailTotal = data.optString("detailTotal");
						if (StringUtils.isEmpty(total) && StringUtils.isEmpty(detailTotal) || (total != null
								&& total.equals("0") && detailTotal != null && detailTotal.equals("0"))) {
							return;
						} else {
							((View) placeNum.getParent()).setVisibility(View.VISIBLE);
						}
						if (!StringUtils.isEmpty(total)) {
							placeNum.setText(total);
						}
						if (!StringUtils.isEmpty(detailTotal)) {
							footprintNum.setText(detailTotal);
						}

						if (expArray != null && expArray.length() > 0) {
							for (int i = 0; i < expArray.length(); i++) {
								Experience exp = new Experience();
								exp.setExperienceId(expArray.optJSONObject(i).optString("id"));
								exp.setDestName(expArray.optJSONObject(i).optString("destName"));
								exp.setExpTimes(expArray.optJSONObject(i).optString("expTimes"));
								exp.setDescription(expArray.optJSONObject(i).optString("desc"));
								JSONArray expDetailArray = expArray.optJSONObject(i).optJSONArray("details");
								if (expDetailArray != null && expDetailArray.length() > 0) {
									for (int j = 0; j < expDetailArray.length(); j++) {
										ExperienceDetail expDetail = new ExperienceDetail();
										expDetail.setId(expDetailArray.optJSONObject(j).optString("id"));
										expDetail.setScale(expDetailArray.optJSONObject(j).optString("scale"));
										expDetail
												.setTravelTime(expDetailArray.optJSONObject(j).optString("travelTime"));
										JSONArray appraiseAarray = expDetailArray.optJSONObject(j)
												.optJSONArray("appraise");
										if (appraiseAarray != null && appraiseAarray.length() > 0) {
											for (int m = 0; m < appraiseAarray.length(); m++) {
												HashMap<String, String> map = new HashMap<String, String>();
												map.put("name", appraiseAarray.optJSONObject(m).optString("name"));
												map.put("content",
														appraiseAarray.optJSONObject(m).optString("content"));
												expDetail.getAppraise().add(map);
											}
										}
										JSONArray picAarray = expDetailArray.optJSONObject(j).optJSONArray("pics");
										if (picAarray != null && picAarray.length() > 0) {
											for (int m = 0; m < picAarray.length(); m++) {
												ImageInfo pic = new ImageInfo();
												pic.setDesc(picAarray.optJSONObject(m).optString("desc"));
												pic.setWidth(picAarray.optJSONObject(m).optString("width"));
												pic.setHeight(picAarray.optJSONObject(m).optString("height"));
												pic.setUrl(picAarray.optJSONObject(m).optString("url"));
												expDetail.getImages().add(pic);
											}
										}
										exp.getExpDetail().add(expDetail);
									}
								}
								experienceList.add(exp);
							}
							if (experienceList.size() > 0) {
								expAdapter = new ExpAdapter(getActivity(), experienceList);
								explistview.setAdapter(expAdapter);
							}
						}
					}
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}
	

}
