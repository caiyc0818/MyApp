package com.bcinfo.tripaway.utils;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.LocationCountryDetailActivity;
import com.bcinfo.tripaway.activity.LoginActivity;
import com.bcinfo.tripaway.activity.SubjectDetailActivity;
import com.bcinfo.tripaway.activity.ThemeDetailActivity;
import com.bcinfo.tripaway.activity.TouristHomepageActivity;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ActivityUtil {
	public static void toPersonHomePage(UserInfo userInfo, Context context) {
		OrgRole orgRole = userInfo.getOrgRole();

		if (orgRole != null) {
			if ("admin".equals(orgRole.getRoleCode())) {
				Intent intent = new Intent(context, ClubFirstPageActivity.class);
				intent.putExtra("userInfo", userInfo);
				context.startActivity(intent);
				return;
			} else if ("leader".equals(orgRole.getRoleCode()) || "guide".equals(orgRole.getRoleCode())
					|| "provider".equals(orgRole.getRoleCode())||"customer_service".equals(orgRole.getRoleCode())) {
				Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
				intentForUserInfo.putExtra("isDriverHomePage", false);
				intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
				intentForUserInfo.putExtra("userInfo", userInfo);
				context.startActivity(intentForUserInfo);
				return;

			} else if ("driver".equals(orgRole.getRoleCode())) {
				Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
				intentForUserInfo.putExtra("isDriverHomePage", true);
				intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
				intentForUserInfo.putExtra("userInfo", userInfo);
				context.startActivity(intentForUserInfo);
				return;
			} else if ("customer_service".equals(orgRole.getRoleCode())) {
				Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
				intentForUserInfo.putExtra("isDriverHomePage", false);
				intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
				intentForUserInfo.putExtra("userInfo", userInfo);
				context.startActivity(intentForUserInfo);
				return;
			}
		}
		// if (orgRole != null) {
		// if (orgRole.getRoleCode().equals("driver"))
		String profession = userInfo.getPermission();
		Intent intentForUserInfo = new Intent(context, TouristHomepageActivity.class);
		if (profession != null && profession.contains("专业司机")) {
			intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
			intentForUserInfo.putExtra("isDriverHomePage", true);
		} else if (profession != null && (profession.contains("资深领队") || profession.contains("达人导游")))

		{
			intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
			intentForUserInfo.putExtra("isDriverHomePage", false);
		}

		// }

		intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
		intentForUserInfo.putExtra("userId", userInfo.getUserId());
		intentForUserInfo.putExtra("userInfo", userInfo);
		context.startActivity(intentForUserInfo);
	}

	public static void squareToPersonHomePage(UserInfo userInfo, Context context) {
		if (userInfo != null) {
			if ("customer".equals(userInfo.getUserType())) {
				Intent intent = new Intent(context, TouristHomepageActivity.class);
				intent.putExtra("userInfo", userInfo);
				context.startActivity(intent);
				return;
			} else if ("person".equals(userInfo.getUserType())) {
				Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
				intentForUserInfo.putExtra("isDriverHomePage", false);
				intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
				intentForUserInfo.putExtra("userInfo", userInfo);
				context.startActivity(intentForUserInfo);
				return;
			} else if (userInfo.getOrgRole() != null && "organization".equals(userInfo.getUserType())) {
				OrgRole orgRole = userInfo.getOrgRole();
				if ("admin".equals(orgRole.getRoleCode())) {
					Intent intent = new Intent(context, ClubFirstPageActivity.class);
					intent.putExtra("userInfo", userInfo);
					context.startActivity(intent);
					return;
				} else if ("leader".equals(orgRole.getRoleCode()) || "guide".equals(orgRole.getRoleCode())
						|| "provider".equals(orgRole.getRoleCode())|| "customer_service".equals(orgRole.getRoleCode())) {
					Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", false);
					intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
					intentForUserInfo.putExtra("userInfo", userInfo);
					context.startActivity(intentForUserInfo);
					return;

				} else if ("driver".equals(orgRole.getRoleCode())) {
					Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", true);
					intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
					intentForUserInfo.putExtra("userInfo", userInfo);
					context.startActivity(intentForUserInfo);
					return;
				} else if ("customer_service".equals(orgRole.getRoleCode())) {
					Intent intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", false);
					intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
					intentForUserInfo.putExtra("userInfo", userInfo);
					context.startActivity(intentForUserInfo);
					return;
				}
			}
		}
		String profession = userInfo.getPermission();
		Intent intentForUserInfo = new Intent(context, TouristHomepageActivity.class);
		if (profession != null && profession.contains("专业司机")) {
			intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
			intentForUserInfo.putExtra("isDriverHomePage", true);
		} else if (profession != null && (profession.contains("资深领队") || profession.contains("达人导游")))

		{
			intentForUserInfo = new Intent(context, ClubMebHomepageActivity.class);
			intentForUserInfo.putExtra("isDriverHomePage", false);
		}
		intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
		intentForUserInfo.putExtra("userId", userInfo.getUserId());
		intentForUserInfo.putExtra("userInfo", userInfo);
		context.startActivity(intentForUserInfo);
	}

	public static void toProductHomePage(ProductNewInfo productNewInfo2, Context context) {
		Intent intentForProductDetail2;
		if (productNewInfo2.getProductType().equals("base")
				|| productNewInfo2.getProductType().equals("customization")) {
			intentForProductDetail2 = new Intent(context, GrouponProductNewDetailActivity.class);
			intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
			// intentForProductDetail.putExtra("product",
			// productNewInfo);
		} else if (productNewInfo2.getProductType().equals("single")
				&& (productNewInfo2.getServices().equals("traffic"))) {
			intentForProductDetail2 = new Intent(context, CarProductDetailActivity.class);
			intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
			// intentForProductDetail.putExtra("product",
			// productNewInfo);
		} else if (productNewInfo2.getProductType().equals("single")
				&& (productNewInfo2.getServices().equals("stay"))) {
			intentForProductDetail2 = new Intent(context, HouseProductDetailActivity.class);
			intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
		}
		// 团产品
		else if (productNewInfo2.getProductType().equals("team")) {

			intentForProductDetail2 = new Intent(context, GrouponProductNewDetailActivity.class);
			intentForProductDetail2.putExtra("productTitle", productNewInfo2.getTitle());
			intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
			// intentForProductDetail.putExtra("product",
			// productNewInfo);
		} else {
			intentForProductDetail2 = new Intent(context, CarProductDetailActivity.class);
			intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
			// intentForProductDetail.putExtra("product",
			// productNewInfo);
		}
		if (intentForProductDetail2 != null)
			context.startActivity(intentForProductDetail2);
	}

	public static void joinPrivateChat(final Context context, final UserInfo user) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(context, LoginActivity.class);
			context.startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "user");
		params.put("referId", user.getUserId());
		HttpUtil.get(Urls.join_private_chat, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (!"00000".equals(code)) {
					// Intent intent = new
					// Intent(GrouponProductNewDetailActivity.this,
					// LoginActivity.class);
					// startActivity(intent);
					return;
				}
				String queueId = response.optString("data");
				// if (!StringUtils.isEmpty(queueId)) {
				// // 查询队列详情并跳转
				// queryPrivateChatInfo(queueId);
				Intent talkIntent = new Intent(context, ChatActivity.class);
				talkIntent.putExtra("queueId", queueId);
				talkIntent.putExtra("receiveId", user.getUserId());
				talkIntent.putExtra("title", user.getNickname());
				talkIntent.putExtra("isTeam", false);
				talkIntent.putExtra("fromQueue", true);
				context.startActivity(talkIntent);
				// }

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	public static void toDetailPage(PushResource pushResource, Context context, boolean extraArg) {
		String objectType = pushResource.getObjectType();
		if (!StringUtils.isEmpty(objectType) && pushResource.getObject() != null) {
			if ("product".equals(objectType)) {
				ProductNewInfo productNewInfo2 = (ProductNewInfo) pushResource.getObject();
				toProductHomePage(productNewInfo2, context);

			} else if ("topic".equals(objectType)) {
				TopicInfo topicInfo = (TopicInfo) pushResource.getObject();
				String topicId = pushResource.getObjectId();
				Intent intentForTopicProductList = new Intent(context, ThemeDetailActivity.class);
				intentForTopicProductList.putExtra("themeId", topicId);
				intentForTopicProductList.putExtra("themeName", topicInfo.getTitle());
				intentForTopicProductList.putExtra("description", topicInfo.getDescription());
				context.startActivity(intentForTopicProductList);
			} else if ("user".equals(objectType)) {
				UserInfo userInfo2 = (UserInfo) pushResource.getObject();
				// Intent intentClubFirstPage2 = new Intent(this,
				// ClubFirstPageActivity.class);
				// intentClubFirstPage2.putExtra("userInfo", userInfo2);
				// startActivity(intentClubFirstPage2);
				ActivityUtil.toPersonHomePage(userInfo2, context);
			} else if ("destination".equals(objectType)) {
				String destId2 = pushResource.getObjectId();// 目的地标识
				Intent intentForLocateCountry2 = new Intent(context, LocationCountryDetailActivity.class);
				intentForLocateCountry2.putExtra("destId", destId2);
				intentForLocateCountry2.putExtra("destinationTitle", pushResource.getResTitle());
				context.startActivity(intentForLocateCountry2);

			} else if ("softtext".equals(objectType) || "story".equals(objectType)) {
				ArticleInfo articleInfo = (ArticleInfo) pushResource.getObject();
				String path = Urls.content_host + articleInfo.getArticleId();
				Intent intent = new Intent(context, ContentActivity.class);
				intent.putExtra("path", path);
				context.startActivity(intent);

			} else if ("link".equals(objectType)) {
				String url = (String) pushResource.getObject();
				Intent intent = new Intent(context, ContentActivity.class);
				intent.putExtra("title", "活动");
				intent.putExtra("path", url);
				intent.putExtra("resTitle", pushResource.getResTitle());
				intent.putExtra("desc", pushResource.getDescription());
				intent.putExtra("titleImage", pushResource.getTitleImage());
				context.startActivity(intent);
			} else if ("subject".equals(objectType)) {
				String entityId = pushResource.getObjectId();
				Intent intentSubjectDetail = new Intent(context, SubjectDetailActivity.class);
				intentSubjectDetail.putExtra("entityId", entityId);
				intentSubjectDetail.putExtra("subjectTitle", pushResource.getResTitle());
				intentSubjectDetail.putExtra("show", extraArg);
				context.startActivity(intentSubjectDetail);
			}
		}
	}

}
