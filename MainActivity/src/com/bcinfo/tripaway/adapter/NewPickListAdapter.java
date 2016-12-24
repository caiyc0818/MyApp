package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.activity.ThemeProductListActivity;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 旅游产品精选adapter
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2014年12月18日 19:18:09
 * 
 */
public class NewPickListAdapter extends BaseAdapter implements OnClickListener {
	private List<Map<String, Object>> resultList;

	private Activity mActivity;

	private LayoutInflater inflator;

	private OperationListener listener;

	public NewPickListAdapter(List<Map<String, Object>> resultList,
			Activity mActivity, OperationListener listener) {
		this.resultList = resultList;
		this.mActivity = mActivity;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		if(resultList.size()>5){
			return 5;
		}
		return resultList.size();
	}

	@Override
	public Object getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		inflator = LayoutInflater.from(mActivity);

		ViewHolder holder;
		Map<String, Object> mapItem = resultList.get(position);
		if (convertView == null) {
			convertView = (View) inflator.inflate(R.layout.new_trip_picked_item,
					null);
			holder = new ViewHolder();
			// 精选-普通产品
			holder.pickedProductLayoutContainer = (RelativeLayout) convertView
					.findViewById(R.id.layout_trip_picked_product_container);
			holder.pickedProductUnit = (TextView) convertView
					.findViewById(R.id.trip_picked_product_info_unit_tv);
			holder.pickedProductGroupLayout = (LinearLayout) convertView
					.findViewById(R.id.trip_picked_product_group_layout);
			holder.pickedProductIv = (ImageView) convertView
					.findViewById(R.id.trip_picked_product_iv);
			holder.pickedProductPriceTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_info_price_tv);
			holder.pickedProductNameTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_info_name_tv);
			// AssetManager mgr = mActivity.getAssets();// 得到AssetManager
			// Typeface tf = Typeface.createFromAsset(mgr,
			// "fonts/cuyuan.ttf");// 根据路径得到Typeface
			// holder.pickedProductNameTv.setTypeface(TripawayApplication.tf);//
			// 设置字体
			// holder.pickedProductPriceTv.setTypeface(TripawayApplication.tf);//
			// 设置字体
			holder.storeImg = (ImageView) convertView
					.findViewById(R.id.if_stored);
			holder.pickedProductTimeSchedualTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_timeSchedual_tv);
			holder.pickedProductDistanceTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_distance_tv);
			holder.pickedProductLableTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_lable);
			// add by lij 2015/09/25 start
			holder.pickedProductPeoIv = (ImageView) convertView
					.findViewById(R.id.trip_picked_product_peo_iv); // 参与人数图片
			// holder.pickedProductPeoTv = (TextView)
			// convertView.findViewById(R.id.trip_picked_product_peo_join_tv);//
			// 参与人数
			// add by lij 2015/09/25 end
			// holder.pickedProductLabelTv1 =
			// (TextView)convertView.findViewById(R.id.trip_picked_product_label_tv1);
			// holder.pickedProductLabelTv2 =
			// (TextView)convertView.findViewById(R.id.trip_picked_product_label_tv2);
			// holder.pickedProductAreaTv =
			// (TextView)convertView.findViewById(R.id.trip_picked_product_area_tv);
			// 精选-服务者
			holder.pickedServicerLayoutContainer = (FrameLayout) convertView
					.findViewById(R.id.layout_trip_picked_product_servicer_container);
			holder.pickedServicerIv = (ImageView) convertView
					.findViewById(R.id.trip_picked_product_servicer_iv);
			holder.pickedServicerHeadIconIv = (RoundImageView) convertView
					.findViewById(R.id.trip_picked_product_servicer_headIcon_iv);
			holder.pickedServicerNameTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_servicer_name_tv);
			holder.pickedServicerBriefTv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_servicer_brief_tv);
			// 精选-主题
			holder.pickedProductThemeLayoutContainer = (LinearLayout) convertView
					.findViewById(R.id.layout_trip_picked_product_theme_container);
			holder.themeProductLayout1 = (FrameLayout) convertView
					.findViewById(R.id.theme_product1);
			// holder.themeProductLayout2 =
			// (FrameLayout)convertView.findViewById(R.id.theme_product2);
			holder.pickedProductThemeIv1 = (ImageView) convertView
					.findViewById(R.id.trip_picked_product_theme_item1_iv);
			// holder.pickedProductThemeIv2 =
			// (ImageView)convertView.findViewById(R.id.trip_picked_product_theme_item2_iv);
			holder.pickedProductThemeNameTv1 = (TextView) convertView
					.findViewById(R.id.trip_picked_product_theme_item_name_tv1);
			// holder.pickedProductThemeNameTv2 =
			// (TextView)convertView.findViewById(R.id.trip_picked_product_theme_item_name_tv2);
			holder.pickedTag1 = (TextView) convertView
					.findViewById(R.id.trip_picked_tag1_lable);
			holder.pickedTag2 = (TextView) convertView
					.findViewById(R.id.trip_picked_tag2_lable);
			holder.pickedTag3 = (TextView) convertView
					.findViewById(R.id.trip_picked_tag3_lable);
			holder.pickedTag4 = (TextView) convertView
					.findViewById(R.id.trip_picked_tag4_lable);
			holder.layout_originalPrice = (RelativeLayout) convertView
					.findViewById(R.id.layout_originalPrice);
			holder.originalPrice = (TextView) convertView
					.findViewById(R.id.originalPrice);
			holder.pv = (TextView) convertView
					.findViewById(R.id.trip_picked_product_peo_join_tv);
			convertView.setTag(holder);
			Log.e("SubjectGridAdapter,convertView为null", Integer.toString(position));
		} else {
			holder = (ViewHolder) convertView.getTag();
			Log.e("SubjectGridAdapter,convertView不为null", Integer.toString(position));
		}
		// if判断 mapItem的type属性 其中 1 表示精选普通产品 2 表示精选服务者 3 表示 精选 主题
		if (((String) mapItem.get("pickedItemType")).equals("product")) // 精选 产品
		{

			ProductNewInfo product = (ProductNewInfo) mapItem
					.get("objectValue");
			// 设置原价

			float price = 0;
			if (!StringUtils.isEmpty(product.getPrice())) {
				price = Float.parseFloat(product.getPrice());
			}
			float price2 = 0;
			if (!StringUtils.isEmpty(product.getOriginalPrice())) {
				price2 = Float.parseFloat(product.getOriginalPrice());
			}
			if (!StringUtils.isEmpty(product.getOriginalPrice())
					&& "0".equals(product.getOriginalPrice()) == false
					&& (price < price2)) {
				holder.layout_originalPrice.setVisibility(View.VISIBLE);
				holder.originalPrice.setText("¥" + product.getOriginalPrice());
			} else {
				holder.layout_originalPrice.setVisibility(View.GONE);
			}

			// 设置浏览人数
			if (!StringUtils.isEmpty(product.getPv())) {
				holder.pv.setText(product.getPv());
			}
			HashMap<String, String> exts = product.getExts();
			String tag = null;
			if (exts != null && exts.size() > 0) {
				tag = exts.get("refer_tags");
				String bigTag = exts.get("big_refer_tags");
				if (bigTag != null) {
					if (bigTag.contains("小团游")) {
						holder.pickedTag1.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag1.setVisibility(View.GONE);
					}
					if (bigTag.contains("深度游")) {
						holder.pickedTag2.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag2.setVisibility(View.GONE);
					}
					if (bigTag.contains("可定制")) {
						holder.pickedTag3.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag3.setVisibility(View.GONE);
					}
					if (bigTag.contains("跟团游")) {
						holder.pickedTag4.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag4.setVisibility(View.GONE);
					}
				} else {
					holder.pickedTag4.setVisibility(View.GONE);
					holder.pickedTag1.setVisibility(View.GONE);
					holder.pickedTag2.setVisibility(View.GONE);
					holder.pickedTag3.setVisibility(View.GONE);
				}
			} else {
				holder.pickedTag4.setVisibility(View.GONE);
				holder.pickedTag1.setVisibility(View.GONE);
				holder.pickedTag2.setVisibility(View.GONE);
				holder.pickedTag3.setVisibility(View.GONE);
			}

			holder.pickedProductLayoutContainer.setVisibility(View.VISIBLE);
			holder.pickedServicerLayoutContainer.setVisibility(View.GONE);
			holder.pickedProductThemeLayoutContainer.setVisibility(View.GONE);

			if ((((ProductNewInfo) mapItem.get("objectValue")).getProductType())
					.equals("team")) {
				holder.pickedProductUnit.setVisibility(View.VISIBLE);
				// holder.pickedProductGroupLayout.setVisibility(View.VISIBLE);
			} else {
				holder.pickedProductUnit.setVisibility(View.GONE);
				holder.pickedProductGroupLayout.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + (String) mapItem.get("pickedItemImg"),
						holder.pickedProductIv,
						AppConfig.options(R.drawable.ic_launcher));
			}

			if ((String) (((ProductNewInfo) mapItem.get("objectValue"))
					.getPrice()) != null) {
				holder.pickedProductPriceTv.setText("￥"
						+ (String) (((ProductNewInfo) mapItem
								.get("objectValue")).getPrice()));
				/*
				 * if ((boolean) (((ProductNewInfo) mapItem.get("objectValue"))
				 * .getProductType()).equals("team")) {
				 * holder.pickedProductUnit.setVisibility(View.VISIBLE);
				 * holder.pickedProductGroupLayout.setVisibility(View.VISIBLE);
				 * }
				 */
				// 价格字体加粗
				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			} else {
				holder.pickedProductPriceTv.setText("¥888");

				// 价格字体加粗
				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);

			}
			String productName = (String) mapItem.get("pickedItemTitle");

			// 标题字体加粗
			holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			holder.pickedProductTimeSchedualTv
					.setText((String) (((ProductNewInfo) mapItem
							.get("objectValue")).getDays()) + "天");
			holder.pickedProductDistanceTv
					.setText((String) (((ProductNewInfo) mapItem
							.get("objectValue")).getDistance()) + "km");
			if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + (String) mapItem.get("pickedItemImg"),
						holder.pickedProductIv,
						AppConfig.options(R.drawable.default_photo));
			}
			holder.pickedProductNameTv.setText(productName);
			if ((String) (((ProductNewInfo) mapItem.get("objectValue"))
					.getPrice()) != null) {
				holder.pickedProductPriceTv.setText("￥"
						+ (String) (((ProductNewInfo) mapItem
								.get("objectValue")).getPrice()));

				// 价格字体加粗
				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			} else {
				holder.pickedProductPriceTv.setText("¥888");

				// 价格字体加粗
				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			}
			// 标题字体加粗
			holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			holder.pickedProductTimeSchedualTv
					.setText((String) (((ProductNewInfo) mapItem
							.get("objectValue")).getDays()) + "天");
			holder.pickedProductDistanceTv
					.setText((String) (((ProductNewInfo) mapItem
							.get("objectValue")).getDistance()) + "km");
			// add by lij 2015/09/25 start
			// if ("0".equals(((ProductNewInfo)
			// mapItem.get("objectValue")).getMemberJoinCount())) {
			// } else {
			// holder.pickedProductPeoTv
			// .setText(((ProductNewInfo)
			// mapItem.get("objectValue")).getMemberJoinCount() + "人浏览");
			// }

			if (!StringUtils.isEmpty(tag)) {
				holder.pickedProductLableTv.setText(tag.replaceAll(";", " · "));
			} else {
				// add by lij 2015/09/25 end
				List<String> topicNameTvArray = ((ProductNewInfo) mapItem
						.get("objectValue")).getTopics();
				if (topicNameTvArray != null && topicNameTvArray.size() != 0) {
					String lable = "";
					for (int i = 0; i < topicNameTvArray.size(); i++) {
						if (i != topicNameTvArray.size() - 1) {
							lable = lable + topicNameTvArray.get(i) + " · ";
						} else {
							lable = lable + topicNameTvArray.get(i);
						}
					}
					holder.pickedProductLableTv.setText(lable);
				}
			}
			if ("yes".equals(product.getIsFav())) {
				holder.storeImg.setImageResource(R.drawable.yes_store);
				holder.storeImg.setTag(R.id.tag_second, true);
			} else {
				holder.storeImg.setImageResource(R.drawable.no_store);
				holder.storeImg.setTag(R.id.tag_second, false);
			}
			holder.storeImg.setTag(R.id.tag_first, product.getId());
			holder.storeImg.setTag(R.id.tag_third, position);
			holder.storeImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String productId = (String) v.getTag(R.id.tag_first);
					boolean falg = (Boolean) v.getTag(R.id.tag_second);
					int position = (Integer) v.getTag(R.id.tag_third);

					if (falg) {
						ToastUtil.showToast(mActivity, "收藏已取消");
						((ImageView) v).setImageResource(R.drawable.no_store);
					} else {
						ToastUtil.showToast(mActivity, "已收藏");
						((ImageView) v).setImageResource(R.drawable.yes_store);
					}
					listener.addOrCancelStored(productId, falg, position);
				}
			});
			holder.pickedProductLayoutContainer.setTag(mapItem);
			holder.pickedProductLayoutContainer
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Map<String, Object> mapItem = (Map<String, Object>) v
									.getTag();
							Intent intentForProductDetail = null;
							ProductNewInfo productNewInfo = (ProductNewInfo) mapItem
									.get("objectValue");
							// 单产品
							if (productNewInfo.getProductType().equals("base")
									|| productNewInfo.getProductType().equals(
											"customization")) {
								intentForProductDetail = new Intent(mActivity,
										GrouponProductNewDetailActivity.class);
								intentForProductDetail.putExtra("productId",
										productNewInfo.getId());
								// intentForProductDetail.putExtra("product",
								// productNewInfo);
							} else if (productNewInfo.getProductType().equals(
									"single")
									&& (((ProductNewInfo) mapItem
											.get("objectValue")).getServices()
											.equals("traffic"))) {
								intentForProductDetail = new Intent(mActivity,
										CarProductDetailActivity.class);
								intentForProductDetail.putExtra("productId",
										productNewInfo.getId());
								// intentForProductDetail.putExtra("product",
								// productNewInfo);
							} else if (productNewInfo.getProductType().equals(
									"single")
									&& (((ProductNewInfo) mapItem
											.get("objectValue")).getServices()
											.equals("stay"))) {
								intentForProductDetail = new Intent(mActivity,
										HouseProductDetailActivity.class);
								intentForProductDetail.putExtra("productId",
										productNewInfo.getId());
							}
							// 团产品
							else if (productNewInfo.getProductType().equals(
									"team")) {

								intentForProductDetail = new Intent(mActivity,
										GrouponProductNewDetailActivity.class);
								intentForProductDetail.putExtra("productId",
										productNewInfo.getId());
								// intentForProductDetail.putExtra("product",
								// productNewInfo);
							} else {
								intentForProductDetail = new Intent(mActivity,
										CarProductDetailActivity.class);
								intentForProductDetail.putExtra("productId",
										productNewInfo.getId());
								// intentForProductDetail.putExtra("product",
								// productNewInfo);
							}
							intentForProductDetail.putExtra("productTitle",
									productNewInfo.getTitle());
							mActivity.startActivity(intentForProductDetail);
						}
					});

		} else if (((String) mapItem.get("pickedItemType")).equals("user")) // 服务者
		{
			holder.pickedTag1.setVisibility(View.GONE);
			holder.pickedTag2.setVisibility(View.GONE);
			holder.pickedTag3.setVisibility(View.GONE);
			holder.pickedProductLayoutContainer.setVisibility(View.GONE);
			holder.pickedServicerLayoutContainer.setVisibility(View.VISIBLE);
			holder.pickedProductThemeLayoutContainer.setVisibility(View.GONE);
			if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + (String) mapItem.get("pickedItemImg"),
						holder.pickedServicerIv,
						AppConfig.options(R.drawable.ic_launcher));// 服务者
			}

			holder.pickedServicerHeadIconIv
					.setImageResource(R.drawable.user_defult_photo);
			if (!StringUtils.isEmpty(((UserInfo) mapItem.get("objectValue"))
					.getAvatar())) {
				ImageLoader
						.getInstance()
						.displayImage(
								Urls.imgHost
										+ (String) (((UserInfo) mapItem
												.get("objectValue"))
												.getAvatar()),
								holder.pickedServicerHeadIconIv,
								AppConfig.options(R.drawable.user_defult_photo));// 服务者圆形头像
			}

			holder.pickedServicerNameTv.setText((String) mapItem
					.get("pickedItemTitle"));// 服务者姓名
			// holder.pickedServicerBriefTv.setText(((UserInfo)mapItem.get("objectValue")).getIntroduction());
			// 改成显示服务标签
			UserInfo userInfo = (UserInfo) mapItem.get("objectValue");
			Tags tagsArray = userInfo.getTag();
			if (tagsArray != null) {
				List<String> list = new ArrayList<String>();
				if (tagsArray.getInterests() != null
						&& tagsArray.getInterests().size() > 0) {
					for (int i = 0; i < tagsArray.getInterests().size(); i++) {
						if (list.contains(tagsArray.getInterests().get(i))) {
							continue;
						} else {
							list.add(tagsArray.getInterests().get(i));
						}
					}
				}
				if (tagsArray.getSpheres() != null) {
					for (int i = 0; i < tagsArray.getSpheres().size(); i++) {
						if (list.contains(tagsArray.getSpheres().get(i))) {
							continue;
						} else {
							list.add(tagsArray.getSpheres().get(i));
						}
					}
				}
				if (tagsArray.getFootprints() != null) {
					for (int i = 0; i < 2; i++) {

						try {
							if (list.contains(tagsArray.getFootprints().get(i))) {
								continue;
							} else {
								list.add(tagsArray.getFootprints().get(i));
							}
						} catch (Exception e) {

						}
					}
				}
				if (tagsArray.getClubTypes() != null) {
					for (int i = 0; i < 2; i++) {
						try {
							if (list.contains(tagsArray.getClubTypes().get(i))) {
								continue;
							} else {
								list.add(tagsArray.getClubTypes().get(i));
							}
						} catch (Exception e) {

						}
					}
				}
				if (tagsArray.getServAreas() != null) {
					for (int i = 0; i < 2; i++) {
						try {
							if (list.contains(tagsArray.getServAreas().get(i))) {
								continue;
							} else {
								list.add(tagsArray.getServAreas().get(i));
							}
						} catch (Exception e) {

						}
					}
				}
				String lable = "";
				for (int i = 0; i < list.size(); i++) {
					if (i != list.size() - 1) {
						lable += list.get(i) + " · ";
					} else {
						lable += list.get(i);
					}
				}
				holder.pickedServicerBriefTv.setText(lable);
			}
			holder.pickedServicerLayoutContainer.setTag(((UserInfo) mapItem
					.get("objectValue")));

			holder.pickedServicerLayoutContainer
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							UserInfo userInfo = (UserInfo) v.getTag();
							OrgRole orgRole = userInfo.getOrgRole();

							if (orgRole != null) {
								if ("admin".equals(orgRole.getRoleCode())) {
									Intent intent = new Intent(mActivity,
											ClubFirstPageActivity.class);
									intent.putExtra("userInfo", userInfo);
									mActivity.startActivity(intent);
									return;
								}
								if ("leader".equals(orgRole.getRoleCode())
										|| "guide".equals(orgRole.getRoleCode())) {
									Intent intentForUserInfo = new Intent(
											mActivity,
											ClubMebHomepageActivity.class);
									intentForUserInfo.putExtra(
											"isDriverHomePage", false);
									intentForUserInfo.putExtra("identifyId",
											userInfo.getUserId());
									intentForUserInfo.putExtra("userInfo",
											userInfo);
									mActivity.startActivity(intentForUserInfo);
									return;

								} else if ("driver".equals(orgRole
										.getRoleCode())) {
									Intent intentForUserInfo = new Intent(
											mActivity,
											ClubMebHomepageActivity.class);
									intentForUserInfo.putExtra(
											"isDriverHomePage", true);
									intentForUserInfo.putExtra("identifyId",
											userInfo.getUserId());
									intentForUserInfo.putExtra("userInfo",
											userInfo);
									mActivity.startActivity(intentForUserInfo);
									return;
								}
							}
							// if (orgRole != null) {
							// if (orgRole.getRoleCode().equals("driver"))
							String profession = userInfo.getPermission();
							Intent intentForUserInfo = new Intent(mActivity,
									PersonalInfoNewActivity.class);
							if (profession != null
									&& profession.contains("专业司机")) {
								intentForUserInfo = new Intent(mActivity,
										ClubMebHomepageActivity.class);
								intentForUserInfo.putExtra("isDriverHomePage",
										true);
							} else if (profession != null
									&& (profession.contains("资深领队") || profession
											.contains("达人导游")))

							{
								intentForUserInfo = new Intent(mActivity,
										ClubMebHomepageActivity.class);
								intentForUserInfo.putExtra("isDriverHomePage",
										false);
							}

							// }

							intentForUserInfo.putExtra("identifyId",
									userInfo.getUserId());
							intentForUserInfo.putExtra("userInfo", userInfo);
							mActivity.startActivity(intentForUserInfo);
						}
					});

		} else if (((String) mapItem.get("pickedItemType")).equals("column")
				|| ((String) mapItem.get("pickedItemType")).equals("topic")) // 精选主题
		{
			holder.pickedTag1.setVisibility(View.GONE);
			holder.pickedTag2.setVisibility(View.GONE);
			holder.pickedTag3.setVisibility(View.GONE);
			holder.pickedProductLayoutContainer.setVisibility(View.GONE);
			holder.pickedServicerLayoutContainer.setVisibility(View.GONE);
			holder.pickedProductThemeLayoutContainer
					.setVisibility(View.VISIBLE);
			if (!StringUtils.isEmpty(((TopicInfo) mapItem
					.get("pickedThemeItem1")).getBackground())) {
				ImageLoader
						.getInstance()
						.displayImage(
								Urls.imgHost
										+ ((TopicInfo) mapItem
												.get("pickedThemeItem1"))
												.getBackground(),
								holder.pickedProductThemeIv1);
			}
			// if (null != (TopicInfo)mapItem.get("pickedThemeItem2")
			// &&!StringUtils.isEmpty(((TopicInfo)mapItem.get("pickedThemeItem2")).getBackground()))
			// {
			// ImageLoader.getInstance().displayImage(Urls.imgHost
			// + ((TopicInfo)mapItem.get("pickedThemeItem2")).getBackground(),
			// holder.pickedProductThemeIv2);
			// }

			holder.themeProductLayout1.setTag(((TopicInfo) mapItem
					.get("pickedThemeItem1")).getId());
			// if(null != (TopicInfo)mapItem.get("pickedThemeItem2")){
			// holder.themeProductLayout2.setTag(((TopicInfo)mapItem.get("pickedThemeItem2")).getId());
			// holder.themeProductLayout2.setOnClickListener(new
			// OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View v)
			// {
			// // TODO Auto-generated method stub
			// String themeId = (String)v.getTag();
			// Intent intentForTopicProductList = new Intent(mActivity,
			// ThemeProductListActivity.class);
			// intentForTopicProductList.putExtra("themeId", themeId);
			// mActivity.startActivity(intentForTopicProductList);
			// }
			// });
			// }
			holder.pickedProductThemeNameTv1.setText(((TopicInfo) mapItem
					.get("pickedThemeItem1")).getTitle());
			// if((TopicInfo)mapItem.get("pickedThemeItem2")!=
			// null&&!StringUtils.isEmpty(((TopicInfo)mapItem.get("pickedThemeItem2")).getTitle())){
			// holder.pickedProductThemeNameTv2.setText(((TopicInfo)mapItem.get("pickedThemeItem2")).getTitle());
			// }else{
			// holder.pickedProductThemeNameTv2.setText("");
			// }
			holder.themeProductLayout1
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String themeId = (String) v.getTag();
							Intent intentForTopicProductList = new Intent(
									mActivity, ThemeProductListActivity.class);
							intentForTopicProductList.putExtra("themeId",
									themeId);
							mActivity.startActivity(intentForTopicProductList);
						}
					});
		} /*
		 * else if (((String) mapItem.get("pickedItemType")).equals( "team")){
		 * holder.pickedProductUnit.setVisibility(View.VISIBLE);
		 * holder.pickedProductGroupLayout.setVisibility(View.VISIBLE); }
		 */

		convertView.setTag(R.id.tag_picked_first, holder);
		convertView.setTag(R.id.tag_picked_second,
				(String) mapItem.get("pickedItemType"));
		convertView.setTag(R.id.tag_picked_thrid, mapItem);
		return convertView;
	}

	/**
	 * ViewHolder 内部类
	 * 
	 */
	class ViewHolder {
		public TextView pickedProductUnit;

		private LinearLayout pickedProductGroupLayout;

		private ImageView storeImg;
		private RelativeLayout pickedProductLayoutContainer;// 精选-普通产品 layout

		private ImageView pickedProductIv;// 精选-普通产品照片 imageview

		private TextView pickedProductPriceTv;// 精选-普通产品价格 textview

		private TextView pickedProductNameTv;// 精选-普通产品名字 textview

		private TextView pickedProductTimeSchedualTv;// 精选-普通产品天数 textview

		private TextView pickedProductDistanceTv;// 精选-普通产品里程距离 textview

		private TextView pickedProductLableTv;// 精选-普通产品 地点 国家 textview

		// add by lij 2015/09/25 start
		private ImageView pickedProductPeoIv;// 精选-普通产品参与人数图片 imageview

		// private TextView pickedProductPeoTv;// 精选-普通产品浏览数量 textview
		// add by lij 2015/09/25 end

		// private TextView pickedProductLabelTv1;// 精选-普通产品 标签1 textview
		//
		// private TextView pickedProductLabelTv2;// 精选-普通产品 标签2 textview
		//
		// private TextView pickedProductAreaTv;// 精选-普通产品 地点 地区 textview

		private FrameLayout pickedServicerLayoutContainer;// 精选-服务者 layout

		private ImageView pickedServicerIv;// 精选-服务者 照片

		private RoundImageView pickedServicerHeadIconIv;// 精选-服务者头像iv

		private TextView pickedServicerNameTv;// 精选-服务者姓名 tv

		private TextView pickedServicerBriefTv;// 精选-服务者简介tv

		private LinearLayout pickedProductThemeLayoutContainer;// 精选-产品主题 layout

		private ImageView pickedProductThemeIv1;// 精选-产品主题1 iv

		private TextView pickedProductThemeNameTv1;// 精选-产品主题名字 textview

		// private ImageView pickedProductThemeIv2;// 精选-产品主题2 iv

		// private TextView pickedProductThemeNameTv2;// 精选-产品主题名字 textview

		private FrameLayout themeProductLayout1;

		// private FrameLayout themeProductLayout2;

		private TextView pickedTag1;

		private TextView pickedTag2;

		private TextView pickedTag3;
		private TextView pickedTag4;

		private RelativeLayout layout_originalPrice;
		private TextView originalPrice;
		private TextView pv; // 浏览人数

	}

	@Override
	public void onClick(View view) {

	}

	public interface OperationListener {
		void addOrCancelStored(String productId, boolean flag, int position);
	}
}
