package com.bcinfo.tripaway.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.adapter.SubjectGridAdapter;
import com.bcinfo.tripaway.bean.AirplaneInfo;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.BoatInfo;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.CarImageCategory;
import com.bcinfo.tripaway.bean.CarServCategory;
import com.bcinfo.tripaway.bean.Cate;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.HotelInfo;
import com.bcinfo.tripaway.bean.HouseInfo;
import com.bcinfo.tripaway.bean.Iairplane;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.MetroInfo;
import com.bcinfo.tripaway.bean.OrgInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PoiInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.ShoppingInfo;
import com.bcinfo.tripaway.bean.SubjectInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.TrainInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.MyAccFas;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午8:30:24
 */
public class JsonUtil
{
    private final static String TAG = "JsonUtil";
    
    public static BoatInfo getBoatInfo(String resourceExt)
    {
        BoatInfo boatInfo = new BoatInfo();
        JSONObject boatJsonObject;
        try
        {
            boatJsonObject = new JSONObject(resourceExt);
            boatInfo.setBoatdetail(boatJsonObject.optString("boat_detail"));
            boatInfo.setBoatType(boatJsonObject.optString("boat_type"));
            boatInfo.setStartingPoint(boatJsonObject.optString("starting_point"));
            boatInfo.setDestPoint(boatJsonObject.optString("dest_point"));
            boatInfo.setElapsedTime(boatJsonObject.optString("elapsed_time"));
            boatInfo.setImageInfoList(getImageInfo(boatJsonObject.optJSONArray("image")));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return boatInfo;
    }
    
    public static AirplaneInfo getAirplaneInfo(String resourceExt)
    {
        AirplaneInfo airplaneInfo = new AirplaneInfo();
        JSONObject airplaneJsonObject;
        try
        {
            airplaneJsonObject = new JSONObject(resourceExt);
            airplaneInfo.setPlaneType(airplaneJsonObject.optString("plane_type"));
            airplaneInfo.setPlaneDetail(airplaneJsonObject.optString("plane_detail"));
            airplaneInfo.setStartingPoint(airplaneJsonObject.optString("starting_point"));
            airplaneInfo.setDescPoint(airplaneJsonObject.optString("dest_point"));
            airplaneInfo.setDepartureTime(airplaneJsonObject.optString("departure_time"));
            airplaneInfo.setArriveTime(airplaneJsonObject.optString("arrive_time"));
            airplaneInfo.setDescPointAlias(airplaneJsonObject.optString("dest_point_alias"));
            airplaneInfo.setStartingPointAlias(airplaneJsonObject.optString("starting_point_alias"));
            airplaneInfo.setElapsedTime(airplaneJsonObject.optString("elapsed_time"));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return airplaneInfo;
    }
    
    public static TrainInfo getTrainInfo(String resourceExt)
    {
        TrainInfo trainInfo = new TrainInfo();
        JSONObject trainJsonObject;
        try
        {
            trainJsonObject = new JSONObject(resourceExt);
            trainInfo.setDestPoint(trainJsonObject.optString("dest_point"));
            trainInfo.setDistance(trainJsonObject.optString("distance"));
            trainInfo.setElapsedTime(trainJsonObject.optString("elapsed_time"));
            trainInfo.setStartingPoint(trainJsonObject.optString("starting_point"));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return trainInfo;
    }
    
    public static Iairplane getIairplane(String resourceExt)
    {
        Iairplane iairplane = new Iairplane();
        JSONObject iairplaneJsonObject;
        try
        {
            iairplaneJsonObject = new JSONObject(resourceExt);
            iairplane.setAirline(iairplaneJsonObject.optString("airline"));
            iairplane.setStartingpoint(iairplaneJsonObject.optString("starting_point"));
            iairplane.setDescPoint(iairplaneJsonObject.optString("dest_point"));
            iairplane.setFlightnumber(iairplaneJsonObject.optString("flight_number"));
            iairplane.setAirLineLogo(iairplaneJsonObject.optString("airline_logo"));
            iairplane.setStartingPointAlias(iairplaneJsonObject.optString("starting_point_alias"));
            iairplane.setDescPointAlias(iairplaneJsonObject.optString("dest_point_alias"));
            iairplane.setStandard(iairplaneJsonObject.optString("standard"));
            iairplane.setDistance(iairplaneJsonObject.optString("distance"));
            iairplane.setDepartureTime(iairplaneJsonObject.optString("departure_time"));
            iairplane.setArriveTime(iairplaneJsonObject.optString("arrive_time"));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return iairplane;
    }
    
    public static MetroInfo getMetroInfo(String resourceExt)
    {
        MetroInfo metroInfo = new MetroInfo();
        JSONObject metroJsonObject;
        try
        {
            metroJsonObject = new JSONObject(resourceExt);
            metroInfo.setDestPoint(metroJsonObject.optString("dest_point"));
            metroInfo.setStartingPoint(metroJsonObject.optString("starting_point"));
            metroInfo.setElapsedTime(metroJsonObject.optString("elapsed_time"));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return metroInfo;
    }
    
    public static PoiInfo getPoiInfo(String resourceExt)
    {
        PoiInfo poiInfo = new PoiInfo();
        JSONObject poiJsonObject;
        try
        {
            poiJsonObject = new JSONObject(resourceExt);
            poiInfo.setScenery_alias(poiJsonObject.optString("scenery_alias"));
            poiInfo.setAddress(poiJsonObject.optString("address"));
            poiInfo.setBusinessHours(poiJsonObject.optString("business_hours"));
            poiInfo.setImageInfoList(getImageInfo(poiJsonObject.optJSONArray("image")));
            poiInfo.setTicket(poiJsonObject.optString("ticket"));
            
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return poiInfo;
    }
    
    public static HotelInfo getHotelInfo(String resourceExt)
    {
        HotelInfo hotelInfo = new HotelInfo();
        JSONObject hotelJsonObject;
        try
        {
            hotelJsonObject = new JSONObject(resourceExt);
            hotelInfo.setAddress(hotelJsonObject.optString("address"));
            hotelInfo.setBedType(hotelJsonObject.optString("bed_type"));
            hotelInfo.setBusinessHours(hotelJsonObject.optString("business_hours"));
            // hotelInfo.setGrogshopAlias(grogshopAlias);
            hotelInfo.setHoldNum(hotelJsonObject.optString("holdnum"));
            hotelInfo.setStarLevel(hotelJsonObject.optString("star_level"));
            hotelInfo.setCheckTime(hotelJsonObject.optString("check_time"));
            hotelInfo.setHouseServs(getSerCategory(hotelJsonObject.optJSONArray("categoryServ")));
            hotelInfo.setImages(getImageInfo(hotelJsonObject.optJSONArray("image")));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hotelInfo;
    }
    
    public static HouseInfo getHouseInfo(String resourceExt)
    {
        HouseInfo houseInfo = new HouseInfo();
        JSONObject houseJsonObject;
        try
        {
            houseJsonObject = new JSONObject(resourceExt);
            houseInfo.setAddress(houseJsonObject.optString("address"));
            houseInfo.setBed(houseJsonObject.optString("bed"));
            houseInfo.setBedType(houseJsonObject.optString("bed_type"));
            houseInfo.setCountry(houseJsonObject.optString("country"));
            houseInfo.setHoldnum(houseJsonObject.optString("holdnum"));
            // houseInfo.setHouseServ(houseServ);
            houseInfo.setHouseType(houseJsonObject.optString("house_type"));
            houseInfo.setToilet(houseJsonObject.optString("toilet"));
            houseInfo.setUnit(houseJsonObject.optString("unit"));
            houseInfo.setCheckTime(houseJsonObject.optString("check_time"));
            houseInfo.setHouseServs(getSerCategory(houseJsonObject.optJSONArray("categoryServ")));
            houseInfo.setImages(getImageInfo(houseJsonObject.optJSONArray("image")));
            
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return houseInfo;
    }
    
    public static Cate getCate(String resourceExt)
    {
        Cate cateInfo = new Cate();
        JSONObject cateJsonObject;
        try
        {
            cateJsonObject = new JSONObject(resourceExt);
            cateInfo.setAddress(cateJsonObject.optString("address"));
            cateInfo.setBusinessHours(cateJsonObject.optString("business_hours"));
            cateInfo.setFeature(cateJsonObject.optString("feature"));
            cateInfo.setFoodAlias(cateJsonObject.optString("food_alias"));
            cateInfo.setPriceLevel(cateJsonObject.optString("price_level"));
            cateInfo.setImageInfoList(getImageInfo(cateJsonObject.optJSONArray("image")));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cateInfo;
    }
    
    public static ShoppingInfo getShoppingInfo(String resourceExt)
    {
        ShoppingInfo shoppingInfo = new ShoppingInfo();
        JSONObject shoppingJsonObject;
        try
        {
            shoppingJsonObject = new JSONObject(resourceExt);
            shoppingInfo.setAddress(shoppingJsonObject.optString("address"));
            shoppingInfo.setBusinessHours(shoppingJsonObject.optString("business_hours"));
            shoppingInfo.setFeature(shoppingJsonObject.optString("feature"));
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return shoppingInfo;
    }
    
    public static CarExt getCarInfo(String resourceExt)
    {
        CarExt carInfo = new CarExt();
        try
        {
            JSONObject carJsonObject = new JSONObject(resourceExt);
            if (carJsonObject != null && !carJsonObject.equals(""))
            {
                carInfo.setCarName(carJsonObject.optString("car_name"));
                carInfo.setLicense(carJsonObject.optString("license"));
                carInfo.setDistance(carJsonObject.optString("distance"));
                carInfo.setShoppingTime(carJsonObject.optString("shoppingtime"));
                carInfo.setSeatNum(carJsonObject.optString("seatnum"));
                carInfo.setCarType(carJsonObject.optString("car_type"));
                carInfo.setCapacity(carJsonObject.optString("capacity"));
                carInfo.setCarPlace(carJsonObject.optString("car_place"));
                JSONArray carImageJsonArray = carJsonObject.optJSONArray("categoryImage");
                if (carImageJsonArray != null && carImageJsonArray.length() > 0)
                {
                    LogUtil.d(TAG, "carImageJsonArray", carImageJsonArray.toString());
                    List<CarImageCategory> carImages = new ArrayList<CarImageCategory>();
                    for (int i = 0; i < carImageJsonArray.length(); i++)
                    {
                        CarImageCategory carImage = new CarImageCategory();
                        JSONObject carImageJsonObject = carImageJsonArray.getJSONObject(i);
                        carImage.setName(carImageJsonObject.optString("name"));
                        carImage.setCateCode(carImageJsonObject.optString("cateCode"));
                        JSONArray imageJsonArray = carImageJsonObject.optJSONArray("img");
                        carImage.setCarImageList(getImageInfo(imageJsonArray));
                        carImages.add(carImage);
                    }
                    carInfo.setCarImages(carImages);
                }
                JSONArray carServJsonArray = carJsonObject.optJSONArray("categoryServ");
                carInfo.setCarServers(getSerCategory(carServJsonArray));
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return carInfo;
    }
    
    public static List<ImageInfo> getImageInfo(JSONArray imageArray)
    {
        List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
        if (imageArray != null && imageArray.length() > 0)
        {
            for (int i = 0; i < imageArray.length(); i++)
            {
                JSONObject imageInfoJson = imageArray.optJSONObject(i);
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setUrl(imageInfoJson.optString("url"));
                imageInfo.setDesc(imageInfoJson.optString("desc"));
                imageInfos.add(imageInfo);
            }
        }
        return imageInfos;
    }
    
    public static List<CarServCategory> getSerCategory(JSONArray categoryArray)
    {
        List<CarServCategory> categoryList = new ArrayList<CarServCategory>();
        if (categoryArray != null && categoryArray.length() > 0)
        {
            for (int i = 0; i < categoryArray.length(); i++)
            {
                JSONObject categoryJson = categoryArray.optJSONObject(i);
                CarServCategory carServCategory = new CarServCategory();
                carServCategory.setName(categoryJson.optString("name"));
                carServCategory.setCateCode(categoryJson.optString("code"));
                JSONArray facilityArray = categoryJson.optJSONArray("facility");
                if (facilityArray != null && facilityArray.length() > 0)
                {
                    List<Facility> facilities = new ArrayList<Facility>();
                    for (int j = 0; j < facilityArray.length(); j++)
                    {
                        JSONObject facilityJson = facilityArray.optJSONObject(j);
                        Facility facility = new Facility();
                        facility.setId(facilityJson.optString("id"));
                        facility.setFacilityCode(facilityJson.optString("facilityCode"));
                        facility.setFacilityName(facilityJson.optString("facilityName"));
                        facility.setStatus(facilityJson.optString("status"));
                        facilities.add(facility);
                    }
                    carServCategory.setFacilities(facilities);
                }
                categoryList.add(carServCategory);
            }
        }
        return categoryList;
    }
    
    public static List<Facility> getCommonTipList(String content)
    {
        List<Facility> facilities = new ArrayList<Facility>();
        try
        {
            JSONArray contentArray = new JSONArray(content);
            if (contentArray != null && contentArray.length() > 0)
            {
                for (int i = 0; i < contentArray.length(); i++)
                {
                    JSONObject contentJson = contentArray.optJSONObject(i);
                    if (contentJson != null && contentJson.length() > 0)
                    {
                        Facility facility = new Facility();
                        facility.setFacilityCode(contentJson.optString("catCode"));
                        facility.setFacilityName(contentJson.optString("catName"));
                        if (contentJson.optString("checked").equals("true"))
                        {
                            facility.setStatus("1");
                        }
                        else
                        {
                            facility.setStatus("0");
                        }
                        facilities.add(facility);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return facilities;
    }
    
    public static ProductNewInfo getProductNewInfo(JSONObject productJson)
    {
    	ProductNewInfo productNewInfo = new ProductNewInfo();
		productNewInfo.setId(productJson
				.optString("id"));
		productNewInfo.setExpStart(productJson
				.optString("expStart"));
		productNewInfo.setExpend(productJson
				.optString("expend"));
		productNewInfo.setProductType(productJson
				.optString("productType"));
		productNewInfo.setDistance(productJson
				.optString("distance"));
		productNewInfo.setTitle(productJson
				.optString("title"));
		productNewInfo.setPoiCount(productJson
				.optString("poiCount"));
		productNewInfo.setPrice(productJson
				.optString("price"));
		productNewInfo.setDays(productJson
				.optString("days"));
		productNewInfo.setDescription(productJson
				.optString("description"));
		productNewInfo.setPriceMax(productJson
				.optString("priceMax"));
		productNewInfo.setTitleImg(productJson
				.optString("titleImg"));
		productNewInfo.setProductCode(productJson
				.optString("productCode"));
		productNewInfo.setMaxMember(productJson
				.optString("maxMember"));
		productNewInfo.setServices(productJson
				.optString("serviceCodes"));
		productNewInfo.setQuickPayment(productJson
				.optString("quickPayment"));
		productNewInfo.setOriginalPrice(productJson
				.optString("originalPrice"));
		productNewInfo.setPv(productJson
				.optString("pv"));
		HashMap<String, String> exts=new HashMap<String, String>();	
		JSONObject jsonObject = productJson.optJSONObject("exts");
		if(jsonObject!=null&&jsonObject.length()>0){
			if(jsonObject.optString("refer_tags")!=null){
				
				exts.put("refer_tags", productJson.optJSONObject("exts").optString("refer_tags"));
			}
			if(jsonObject.optString("big_refer_tags")!=null){
				
				exts.put("big_refer_tags", productJson.optJSONObject("exts").optString("big_refer_tags"));
			}
			productNewInfo.setExts(exts);
		}
		
		
		ArrayList<String> topics = new ArrayList<String>();
		JSONArray topicJsonArray = productJson
				.optJSONArray("topics");
		if (topicJsonArray != null
				&& topicJsonArray.length() > 0) {
			for (int j = 0; j < topicJsonArray
					.length(); j++) {
				LogUtil.i(
						"LocationCityDetailActivity",
						"topicJsonArray",
						topicJsonArray
								.optString(j));
				topics.add(topicJsonArray
						.optString(j));
			}
			productNewInfo.setTopics(topics);
		}
		
		
		
        return productNewInfo;
    }
    
    public static TopicInfo getTopicInfo(JSONObject jsonObject)
    {
    	 TopicInfo topic = new TopicInfo();
         
    	 topic.setId(jsonObject.optString("id"));
    	 topic.setTitle(jsonObject.optString("title"));
    	 topic.setBackground(jsonObject.optString("background"));
    	 topic.setDescription(jsonObject.optString("description"));
        return topic;
    }
    
    public static UserInfo getUserInfo(JSONObject userObj)
    {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setGender(userObj.optString("sex"));
		userInfo.setStatus(userObj.optString("status"));
		userInfo.setAvatar(userObj.optString("avatar"));
		userInfo.setEmail(userObj.optString("email"));
		userInfo.setNickname(userObj
				.optString("nickName"));
		userInfo.setRealName(userObj
				.optString("realName"));
		userInfo.setUserId(userObj.optString("userId"));
		userInfo.setIsFocus(userObj.optString("isFocus"));
		userInfo.setBackground(userObj
				.optString("background"));
		userInfo.setBrief(userObj.optString("brief"));
		userInfo.setProductCount(userObj.optString("productCount"));
		userInfo.setFansCount(userObj.optString("fansCount"));
		userInfo.setFocus(userObj.optString("focus"));
		userInfo.setIntroduction(userObj
				.optString("introduction"));
		userInfo.setIsTalent(userObj
				.optString("isTalent"));
		userInfo.setMobile(userObj.optString("mobile"));
		userInfo.setUserType(userObj.optString("userType"));
		JSONArray professionArray = userObj
				.optJSONArray("profession");
		JSONObject tagObj = userObj.optJSONObject("tags");
		if (null != tagObj) {
			Tags tag = new Tags();
			JSONArray interest = tagObj
					.optJSONArray("interest");
			if (interest != null) {
				List<String> list = tag.getInterests();
				for (int i = 0; i < interest.length(); i++) {
					list.add(interest.optString(i));
				}
				tag.setInterests(list);
			}

			JSONArray sphere = tagObj
					.optJSONArray("sphere");
			if (sphere != null) {
				List<String> list = tag.getSpheres();
				for (int i = 0; i < sphere.length(); i++) {
					list.add(sphere.optString(i));
				}
				tag.setSpheres(list);
			}

			JSONArray footprint = tagObj
					.optJSONArray("footprint");
			if (footprint != null) {
				List<String> list = tag.getFootprints();
				for (int i = 0; i < footprint.length(); i++) {
					list.add(footprint.optString(i));
				}
				tag.setFootprints(list);
			}

			JSONArray clubtype = tagObj
					.optJSONArray("club_type");
			if (clubtype != null) {
				List<String> list = tag.getClubTypes();
				for (int i = 0; i < clubtype.length(); i++) {
					list.add(clubtype.optString(i));
				}
				tag.setClubTypes(list);
			}

			JSONArray servarea = tagObj
					.optJSONArray("serv_area");
			if (servarea != null) {
				List<String> list = new ArrayList<>();
				for (int i = 0; i < servarea.length(); i++) {
					list.add(servarea.optString(i));
				}
				tag.setServAreas(list);
			}
			userInfo.setTag(tag);
		}
		String profession = "";
		if (professionArray != null) {
			for (int j = 0; j < professionArray
					.length(); j++) {
				if (j == 0) {
					profession += professionArray
							.optString(j);
				} else {
					profession += ","
							+ professionArray
									.optString(j);
				}
			}
		}
		userInfo.setPermission(profession);
		JSONObject orgRoleObj = userObj
				.optJSONObject("orgRole");
		if (orgRoleObj != null) {
			OrgRole orgRole = new OrgRole();
			orgRole.setRoleType(orgRoleObj
					.optString("roleType"));
			orgRole.setPermission(orgRoleObj
					.optString("permission"));
			orgRole.setRoleCode(orgRoleObj
					.optString("roleCode"));
			if (orgRole.getRoleCode().equals("driver")) {
				System.out.print(orgRole.getRoleCode()
						.equals("driver"));
			}
			orgRole.setRoleName(orgRoleObj
					.optString("roleName"));
			userInfo.setOrgRole(orgRole);
		}
		userInfo.setServerPolicy(userObj.optString("serverPolicy"));
        return userInfo;
    }
//    广场接口 文档 User_Schema userInfo简化   其中role替代orgRole
    public static UserInfo getSquareUser(JSONObject userObj)
    {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setGender(userObj.optString("sex"));
		userInfo.setStatus(userObj.optString("status"));
		userInfo.setAvatar(userObj.optString("avatar"));
		userInfo.setEmail(userObj.optString("email"));
		userInfo.setNickname(userObj
				.optString("nickName"));
		userInfo.setRealName(userObj
				.optString("realName"));
		userInfo.setUserId(userObj.optString("userId"));
		userInfo.setIsFocus(userObj.optString("isFocus"));
		userInfo.setBackground(userObj
				.optString("background"));
		userInfo.setBrief(userObj.optString("brief"));
		userInfo.setProductCount(userObj.optString("productCount"));
		userInfo.setFansCount(userObj.optString("fansCount"));
		userInfo.setFocus(userObj.optString("focus"));
		userInfo.setIntroduction(userObj
				.optString("introduction"));
		userInfo.setIsTalent(userObj
				.optString("isTalent"));
		userInfo.setMobile(userObj.optString("mobile"));
		userInfo.setUserType(userObj.optString("userType"));
		JSONArray professionArray = userObj
				.optJSONArray("profession");
		JSONObject tagObj = userObj.optJSONObject("tags");
		if (null != tagObj) {
			Tags tag = new Tags();
			JSONArray interest = tagObj
					.optJSONArray("interest");
			if (interest != null) {
				List<String> list = tag.getInterests();
				for (int i = 0; i < interest.length(); i++) {
					list.add(interest.optString(i));
				}
				tag.setInterests(list);
			}

			JSONArray sphere = tagObj
					.optJSONArray("sphere");
			if (sphere != null) {
				List<String> list = tag.getSpheres();
				for (int i = 0; i < sphere.length(); i++) {
					list.add(sphere.optString(i));
				}
				tag.setSpheres(list);
			}

			JSONArray footprint = tagObj
					.optJSONArray("footprint");
			if (footprint != null) {
				List<String> list = tag.getFootprints();
				for (int i = 0; i < footprint.length(); i++) {
					list.add(footprint.optString(i));
				}
				tag.setFootprints(list);
			}

			JSONArray clubtype = tagObj
					.optJSONArray("club_type");
			if (clubtype != null) {
				List<String> list = tag.getClubTypes();
				for (int i = 0; i < clubtype.length(); i++) {
					list.add(clubtype.optString(i));
				}
				tag.setClubTypes(list);
			}

			JSONArray servarea = tagObj
					.optJSONArray("serv_area");
			if (servarea != null) {
				List<String> list = new ArrayList<>();
				for (int i = 0; i < servarea.length(); i++) {
					list.add(servarea.optString(i));
				}
				tag.setServAreas(list);
			}
			userInfo.setTag(tag);
		}
		String profession = "";
		if (professionArray != null) {
			for (int j = 0; j < professionArray
					.length(); j++) {
				if (j == 0) {
					profession += professionArray
							.optString(j);
				} else {
					profession += ","
							+ professionArray
									.optString(j);
				}
			}
		}
		userInfo.setPermission(profession);
		JSONObject orgRoleObj = userObj
				.optJSONObject("role");
		if (orgRoleObj != null) {
			OrgRole orgRole = new OrgRole();
			orgRole.setRoleType(orgRoleObj
					.optString("roleType"));
			orgRole.setPermission(orgRoleObj
					.optString("permission"));
			orgRole.setRoleCode(orgRoleObj
					.optString("roleCode"));
			if (orgRole.getRoleCode().equals("driver")) {
				System.out.print(orgRole.getRoleCode()
						.equals("driver"));
			}
			orgRole.setRoleName(orgRoleObj
					.optString("roleName"));
			userInfo.setOrgRole(orgRole);
		}
		userInfo.setServerPolicy(userObj.optString("serverPolicy"));
        return userInfo;
    }
    public static UserInfo getSimpleUserInfo(JSONObject userObj)
    {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setGender(userObj.optString("sex"));
		userInfo.setStatus(userObj.optString("dispatchStatus"));
		userInfo.setAvatar(userObj.optString("avatar"));
		userInfo.setNickname(userObj
				.optString("nickName"));
		userInfo.setRealName(userObj
				.optString("realName"));
		userInfo.setUserType(userObj.optString("userType"));
		userInfo.setUserId(userObj.optString("userId"));
		userInfo.setMobile(userObj.optString("tel"));
		JSONObject orgRoleObj = userObj
				.optJSONObject("role");
		if (orgRoleObj != null) {
			OrgRole orgRole = new OrgRole();
			orgRole.setRoleType(orgRoleObj
					.optString("roleType"));
			orgRole.setPermission(orgRoleObj
					.optString("permission"));
			orgRole.setRoleCode(orgRoleObj
					.optString("roleCode"));
			orgRole.setRoleName(orgRoleObj
					.optString("roleName"));
			userInfo.setOrgRole(orgRole);
		}
        return userInfo;
    }
    
    public static TripDestination getDestinationInfo(JSONObject jsonDestObj)
    {
    	TripDestination tripDestination = new TripDestination();
         
    	 tripDestination.setDestProNum(jsonDestObj.optString("pNum"));
         tripDestination.setDestId(jsonDestObj.optString("id"));
         tripDestination.setDestSupNum(jsonDestObj.optString("sNum"));
         tripDestination.setDestLogoKey(jsonDestObj.optString("logo"));
         tripDestination.setDestName(jsonDestObj.optString("destName"));
         tripDestination.setDestNameEn(jsonDestObj.optString("destNameEn"));
         tripDestination.setDestDescription(jsonDestObj.optString("description"));
         tripDestination.setDestKeyWords(jsonDestObj.optString("keywords"));
        return tripDestination;
    }
    
    
    public static OrgInfo getOrgInfo(JSONObject jsonObj)
    {
    	OrgInfo orgInfo = new OrgInfo();
         
    	orgInfo.setArticleNum(jsonObj.optString("articleNum"));
    	orgInfo.setFansCount(jsonObj.optString("fansCount"));
    	orgInfo.setFoundDay(jsonObj.optString("foundDay"));
    	orgInfo.setId(jsonObj.optString("id"));
    	orgInfo.setIntroduce(jsonObj.optString("introduce"));
    	orgInfo.setIsFocus(jsonObj.optString("isFocus"));
    	orgInfo.setMembers(jsonObj.optString("members"));
    	orgInfo.setOrgBackground(jsonObj.optString("orgBackground"));
    	orgInfo.setOrgName(jsonObj.optString("orgName"));
    	orgInfo.setProductCount(jsonObj.optString("productCount"));
			JSONArray tagArray= jsonObj.optJSONArray("tags");
			if (tagArray != null&&tagArray.length()>0) {
				for (int j = 0; j < tagArray.length();j++) {
					orgInfo.getTags().add(tagArray.optString(j));
				}
			}
        return orgInfo;
    }
    
    
    public static ArticleInfo getArticleInfo(JSONObject jsonObj)
    {
    	ArticleInfo articleInfo = new ArticleInfo();
    	articleInfo.setContent(jsonObj.optString("content"));
    	JSONObject orgJsonObject=jsonObj.optJSONObject("organization");
    	if(orgJsonObject!=null&&orgJsonObject.length()>0){
    		articleInfo.setOrgInfo(getOrgInfo(orgJsonObject));
    	}
    	JSONObject userObject=jsonObj.optJSONObject("publisher");
    	if(userObject!=null&&userObject.length()>0){
    		articleInfo.setPublisher(getUserInfo(userObject));
    	}
    	articleInfo.setPublishTime(jsonObj.optString("pushlishTime"));
    	articleInfo.setArticleId(jsonObj.optString("articleId"));
    	articleInfo.setSource(jsonObj.optString("source"));
    	articleInfo.setSubTitle(jsonObj.optString("subTitle"));
    	articleInfo.setTitle(jsonObj.optString("title"));
    	articleInfo.setType(jsonObj.optString("type"));
    	articleInfo.setComments(jsonObj.optString("comments"));
    	articleInfo.setCover(jsonObj.optString("cover"));
    	articleInfo.setAbstracts(jsonObj.optString("abstracts"));
        return articleInfo;
    }
    
    public static MyAccFas getMyAccFas(JSONObject jsonObject)
    {
        MyAccFas myAccFas = new MyAccFas();  
        myAccFas.setActionType("actionType");
        myAccFas.setAmount("amount");
        myAccFas.setBuyer("buyer");
        myAccFas.setDirection("direction");
        myAccFas.setFocusNum("focusNum");
        myAccFas.setOrderNo("orderNo");
        myAccFas.setPayType("payType");
        myAccFas.setRecordTime("recordTime");
        myAccFas.setStatus("status");
        myAccFas.setTitle("title");
        
        return myAccFas;
    }
    public static SubjectInfo getSubjectInfo(JSONObject jsonObj)
    {
    	SubjectInfo subjectInfo = new SubjectInfo();
    	subjectInfo.setCover(jsonObj.optString("cover"));
    	subjectInfo.setDescription(jsonObj.optString("description"));
    	subjectInfo.setSubjectId(jsonObj.optString("subjectId"));
    	subjectInfo.setSubTitle(jsonObj.optString("subTitle"));
    	subjectInfo.setTitle(jsonObj.optString("title"));
    	subjectInfo.setType(jsonObj.optString("type"));
    	subjectInfo.setUrl(jsonObj.optString("url"));
    	subjectInfo.setCover(jsonObj.optString("cover"));
        return subjectInfo;
    }
    
    public static void getPushResources(JSONArray jsonDestArray ,List<PushResource> pushResourceList) {
			for (int i = 0; i < jsonDestArray.length(); i++) {
				PushResource pushResource = new PushResource();
				// if (i < 5) {
				JSONObject jsonObj = jsonDestArray.optJSONObject(i);

				pushResource.setId(jsonObj.optString("id"));
				pushResource.setResTitle(jsonObj.optString("resTitle"));
				pushResource.setObjectType(jsonObj.optString("objectType"));
				pushResource.setObjectId(jsonObj.optString("objectId"));
				if (jsonObj.optString("keywords").contains("，")) {
					pushResource.setKeywords(jsonObj.optString("keywords")
							.replace("，", "·"));
				} else {
					pushResource.setKeywords(jsonObj.optString("keywords"));
				}
				pushResource.setSubTitle(jsonObj.optString("subTitle"));
				pushResource.setTitleImage(jsonObj.optString("titleImage"));
				if ("link".equals(jsonObj.optString("objectType"))) {
					pushResource.setObject(jsonObj.optString("object"));

				} else {
					JSONObject jSONObject = jsonObj.optJSONObject("object");
					if (jSONObject != null && jSONObject.length() > 0) {
						if ("product".equals(jsonObj.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getProductNewInfo(jSONObject));

						} else if ("topic".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getTopicInfo(jSONObject));
						} else if ("activity".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getTopicInfo(jSONObject));
						}  else if ("user".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getUserInfo(jSONObject));
						} else if ("destination".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getDestinationInfo(jSONObject));

						} else if ("softtext".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getArticleInfo(jSONObject));

						} else if ("story".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getArticleInfo(jSONObject));
						} else {
							JSONArray relatedResourcesJson = jSONObject
									.optJSONArray("relatedResources");
							String objectType = pushResource.getObjectType();
							if (jSONObject.optString("cover") != null
									&& "subject".equals(objectType))
								pushResource.setTitleImage(jSONObject
										.optString("cover"));
							UserInfo userInfo = new UserInfo();
							if (relatedResourcesJson != null
									&& relatedResourcesJson.length() > 0) {
								for (int m = 0; m < relatedResourcesJson
										.length(); m++) {
									JSONObject relatedResourceJson = relatedResourcesJson
											.optJSONObject(m);
									if (relatedResourceJson
											.optString("objectType") != null
											&& !"user"
													.equals(relatedResourceJson
															.optString("objectType"))) {
										JSONObject productObj = relatedResourceJson
												.optJSONObject("object");
										if (productObj != null
												&& userInfo.getReferPics()
														.size() < 3) {
											if (productObj
													.optString("titleImg") != null)
												userInfo.getReferPics()
														.add(productObj
																.optString("titleImg"));
										}
										continue;
									}
									if (!StringUtils.isEmpty(userInfo
											.getUserId()))
										continue;
									JSONObject objectParamObj = relatedResourceJson
											.optJSONObject("objectParams");
									if (objectParamObj != null
											&& objectParamObj.length() > 0) {
										pushResource.getObjectParam().put(
												"nickname",
												objectParamObj
														.optString("nickname"));
										pushResource.getObjectParam().put(
												"tags",
												objectParamObj
														.optString("tags"));
										pushResource.getObjectParam().put(
												"avartar",
												objectParamObj
														.optString("avartar"));
									}
									JSONObject userObj = relatedResourceJson
											.optJSONObject("object");
									userInfo.setGender(userObj.optString("sex"));
									userInfo.setStatus(userObj
											.optString("status"));
									userInfo.setAvatar(userObj
											.optString("avatar"));
									userInfo.setEmail(userObj
											.optString("email"));
									userInfo.setNickname(userObj
											.optString("nickName"));
									userInfo.setUserId(userObj
											.optString("userId"));
									userInfo.setBackground(userObj
											.optString("background"));
									userInfo.setBrief(userObj
											.optString("brief"));
									userInfo.setIntroduction(userObj
											.optString("introduction"));
									userInfo.setMobile(userObj
											.optString("mobile"));
									JSONArray professionArray = userObj
											.optJSONArray("profession");
									JSONObject tagObj = userObj
											.optJSONObject("tags");
									if (null != tagObj) {
										Tags tag = new Tags();
										JSONArray interest = tagObj
												.optJSONArray("interest");
										if (interest != null) {
											List<String> list = new ArrayList<>();
											for (int j = 0; j < interest
													.length(); j++) {
												list.add(interest.optString(j));
											}
											tag.setInterests(list);
											userInfo.setTag(tag);
										}
									}
									String profession = "";
									if (professionArray != null) {
										for (int j = 0; j < professionArray
												.length(); j++) {
											if (j == 0) {
												profession += professionArray
														.optString(j);
											} else {
												profession += ","
														+ professionArray
																.optString(j);
											}
										}
									}
									userInfo.setPermission(profession);
									JSONObject orgRoleObj = userObj
											.optJSONObject("orgRole");
									if (orgRoleObj != null) {
										OrgRole orgRole = new OrgRole();
										orgRole.setRoleType(orgRoleObj
												.optString("roleType"));
										orgRole.setPermission(orgRoleObj
												.optString("permission"));
										orgRole.setRoleCode(orgRoleObj
												.optString("roleCode"));
										if (orgRole.getRoleCode().equals(
												"driver")) {
											System.out.print(orgRole
													.getRoleCode().equals(
															"driver"));
										}
										orgRole.setRoleName(orgRoleObj
												.optString("roleName"));
										userInfo.setOrgRole(orgRole);
									}
									pushResource.setObject(userInfo);
								}
							}
						}
					}
				}
				// } else {
				// if (i == 5) {
				// pushResource.setResTitle("更多");
				// orgList.add(pushResource);
				// break;
				// }
				// }
				pushResourceList.add(pushResource);

			}

	}
    public static void getPushResourceList(JSONObject jsonResponse,List<PushResource> pushResourceList) {
		String code = jsonResponse.optString("code");
		if ("00000".equals(code)) {
			JSONArray jsonDestArray = jsonResponse.optJSONArray("data");
			getPushResources(jsonDestArray, pushResourceList);
		}

	}
    
    public static TripArticle getTripArticle(JSONObject dataObj) {
    	TripArticle articleObj=null;
		if (dataObj != null && dataObj.length() != 0) {
			 articleObj = new TripArticle();
			 
			 if (!StringUtils.isEmpty(dataObj.optString("viewNum"))) {
					try {
						articleObj.setViewNum(Integer.parseInt(dataObj
								.optString("views")));
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
			 
			// 微游记 发布时间
			articleObj.setPublishTime(dataObj.optString("publishTime"));
			// 位置
			articleObj.setLocation(dataObj.optString("location"));
			// 是否已赞
			articleObj.setIsLike(dataObj.optString("isLike"));
			// 点赞数
			articleObj.setLikes(dataObj.optString("likes"));
			// 微游记 标识id
			articleObj.setPhotoId(dataObj.optString("photoId"));
			// 微游记 评论数
			articleObj.setComments(dataObj.optString("comments"));
			// 微游记 描述
			articleObj.setDescription(dataObj.optString("description"));
			JSONArray picArray = dataObj.optJSONArray("pictures");
			if (null != picArray) {
				for (int j = 0; j < picArray.length(); j++) {
					JSONObject picObj = picArray.optJSONObject(j);
					ImageInfo picRes = new ImageInfo();
					if(StringUtils.isEmpty(picObj.optString("url")))
						continue;
					picRes.setDesc(picObj.optString("desc"));
					picRes.setUrl(picObj.optString("url"));
					picRes.setHeight(picObj.optString("height"));
					picRes.setWidth(picObj.optString("width"));
					articleObj.getPictureList().add(picRes);

				}
			}
			JSONArray tagArray = dataObj.optJSONArray("tagNames");
			if (null != tagArray&&tagArray.length()>0) {
				for (int j = 0; j < tagArray.length(); j++) {
					String tag = tagArray.optString(j);
					articleObj.getTagList().add(tag);
				}
			}
			JSONObject userObj = dataObj.optJSONObject("publisher");
			if (null != userObj&&userObj.length()>0) {

				articleObj.setPublisher(JsonUtil.getUserInfo(userObj));
			}
			JSONArray jsonArray = dataObj.optJSONArray("commentList");
			if (jsonArray != null && jsonArray.length() > 0) {
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jsonObject = jsonArray.optJSONObject(j);
					Comments comment = new Comments();
					comment.setContent(jsonObject.optString("content"));
					comment.setCreateTime(jsonObject.optString("createTime"));
					comment.setId(jsonObject.optString("id"));
					JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
					if (jsonUserInfo != null) {
						UserInfo userInfo = new UserInfo();
						userInfo.setAvatar(jsonUserInfo.optString("avatar"));
						userInfo.setNickname(jsonUserInfo.optString("nickName"));
						userInfo.setUserType(jsonUserInfo.optString("userType"));
						comment.setUser(userInfo);
					}
					articleObj.getCommentList().add(comment);
				}
		}
	}
		return articleObj;
    }
    
    public static TripArticle getSerialTripArticle(JSONObject dataObj) {
    	TripArticle articleObj=null;
		if (dataObj != null && dataObj.length() != 0) {
			 articleObj = new TripArticle();
			 articleObj.setType((byte) 1);
			 
			 articleObj.setCover(dataObj.optString("cover"));
			 articleObj.setTitle(dataObj.optString("title"));
			if (!StringUtils.isEmpty(dataObj.optString("viewNum"))) {
				try {
					articleObj.setViewNum(Integer.parseInt(dataObj
							.optString("views")));
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
			articleObj.setAccess(dataObj.optString("access"));
			// 微游记 发布时间
			articleObj.setPublishTime(dataObj.optString("releaseTime"));
			// 位置
			articleObj.setLocation(dataObj.optString("position"));
			// 是否已赞
			articleObj.setIsLike(dataObj.optString("isLike"));
			// 点赞数
			articleObj.setLikes(dataObj.optString("likes"));
			
			articleObj.setIsFocus(dataObj.optString("isFocus"));
			
			// 微游记 标识id
			articleObj.setPhotoId(dataObj.optString("seriesId"));
			// 微游记 评论数
			articleObj.setComments(dataObj.optString("comments"));
			// 微游记 描述
			articleObj.setDescription(dataObj.optString("desc"));
			JSONArray picArray = dataObj.optJSONArray("pictures");
			if (null != picArray) {
				for (int j = 0; j < picArray.length(); j++) {
					JSONObject picObj = picArray.optJSONObject(j);
					ImageInfo picRes = new ImageInfo();
					if(StringUtils.isEmpty(picObj.optString("url")))
						continue;
					picRes.setDesc(picObj.optString("desc"));
					picRes.setUrl(picObj.optString("url"));
					picRes.setHeight(picObj.optString("height"));
					picRes.setWidth(picObj.optString("width"));
					articleObj.getPictureList().add(picRes);

				}
			}
			JSONArray tagArray = dataObj.optJSONArray("tagNames");
			if (null != tagArray&&tagArray.length()>0) {
				for (int j = 0; j < tagArray.length(); j++) {
					String tag = tagArray.optString(j);
					articleObj.getTagList().add(tag);
				}
			}
			JSONObject userObj = dataObj.optJSONObject("publisher");
			if (null != userObj&&userObj.length()>0) {

				articleObj.setPublisher(JsonUtil.getUserInfo(userObj));
			}
			JSONArray jsonArray = dataObj.optJSONArray("commentList");
			if (jsonArray != null && jsonArray.length() > 0) {
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jsonObject = jsonArray.optJSONObject(j);
					Comments comment = new Comments();
					comment.setContent(jsonObject.optString("content"));
					comment.setCreateTime(jsonObject.optString("createTime"));
					comment.setId(jsonObject.optString("id"));
					JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
					if (jsonUserInfo != null) {
						UserInfo userInfo = new UserInfo();
						userInfo.setAvatar(jsonUserInfo.optString("avatar"));
						userInfo.setNickname(jsonUserInfo.optString("nickName"));
						comment.setUser(userInfo);
					}
					articleObj.getCommentList().add(comment);
				}
		}
	}
		return articleObj;
    }

}
