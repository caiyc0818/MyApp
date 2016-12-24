package com.bcinfo.tripaway.net;

public class Urls {

	public static String imgHost = "http://img.tripaway.cn/";

	public static String policy = "http://www.tripaway.cn/download/policy.html";
	
	/*
	 * 接口地址
	 */
//	 public static String host = "http://125.71.209.90:8099/tripaway-api/"; //外网测试环境url
	public static String host = "http://inf.tripaway.cn/"; // 正式环境url
//	 public static String host = "http://app.tripaway.cn/"; // 正式环境url
//	 public static String host = "http://it.tripaway.cn/"; // 仿真环境url
	 
		//访问地址
//	public static String host1="http://125.71.209.90:8098/tripawayWap/";
//	public static String host1="http://mt.tripaway.cn/";
	public static String host1="http://m.tripaway.cn/";
	
	/*
	 * 首付游Wap地址
	 */
//	public static String  pay = "http://pt.tripaway.cn/sfy/create.do?orderNo=" ;// 仿真环境
	public static String  pay = "http://pay.tripaway.cn/sfy/create.do?orderNo=";// 正式环境
	
	// // 正式环境分享
	public static String ShareUrlOfProduct = host1+"base_prod-";
	public static String ShareUrlOfUser = host1+"club-";
	public static String ShareUrlOfPhoto = host1+"tripstory-";
	
//		订单详情wap
	public static String UrlOfOrderDetail = host1+"order_sales-";
		
//		失败订单详情wap
	public static String	e_UrlOfOrderDetail=host1+"order-";
	
	// 发现-俱乐部聚合
	public static String club = "http://m.tripaway.cn/clubs.html"; // 正式环境url

	public static String huodong = host1+"activity-";

	// 我的同行人 add by guc
	public static final String get_consultation = host + "api/feedback";

	// 我的投诉咨询详情add by shangwx
	public static final String get_consultation_reply = host + "api/feedback/reply";

	// 回复咨询投诉add by shangwx
	public static final String send_consultation_reply = host + "api/feedback/reply";

	// 回复咨询投诉add by shangwx
	public static final String del_consultation = host + "api/feedback/";

	// 回复咨询投诉add by shangwx
	public static final String article_list = host + "api/article";

	public static final String goldmedal_list = host + "api/gold";

	public static final String trends_list = host + "api/feed";
	/**
	 * 游记查询
	 */
	public static final String travels_list = host + "api/feed/story";

	public static final String dest_list = host + "api/userInfo/dest";

	public static final String cust_list = host + "api/product/cust";

	// 个人产品
	public static final String get_plist = host + "api/userInfo/plist";

	// 个人目的地
	public static final String get_dest = host + "api/userInfo/dest";

	// 个人游记
	public static final String get_trip_info = host + "api/tripstory/list";

	// 资源列表by shangwx
	public static final String get_bymcode = host + "api/push/bymcode";

	// 查询俱乐部列表by shangwx
	public static final String get_club = host + "api/club";

	// 查询俱乐部标签列表by shangwx
	public static final String get_club_tags = host + "api/club/tags";

	// 查询旅行经历by shangwx
	public static final String get_experience = host + "api/experience";

	// 主题关联产品by shangwx
	public static final String get_theme_product = host + "api/topic/product/";

	// 检查账号是否注册add by shangwx
	public static final String checkAccount = host + "api/user/checkAccount";

	// 查询实体详情add by shangwx
	public static final String get_entity = host + "api/entity";

	public static final String BAIDUURL = "http://api.map.baidu.com/geocoder/v2/?";

	/*
	 * 文章地址
	 */
	public static String content_host = "http://www.tripaway.cn/site/article/";// 正式环境文章地址
	// public static String content_host=
	// "http://wt.tripaway.cn/site/article/";//仿真环境文章地址
	// public static String content_host =
	// "http://125.71.209.90:8098/tripaway-site/site/article/";// 测试环境文章地址

	// 分享
	public static String ShareUrl = "http://www.tripaway.cn/site/wap/gotoShowProduct?productId=";

	// 搜索
	public static String solrUrl = host + "api/search";

	// 搜索热词
	public static String solrHotWordsUrl = host + "api/hotwords";

	// 注册-普通注册
	public static String registerUrl = host + "api/user/register";

	// 登录-普通登录
	public static String loginUrl = host + "api/user/login";

	// 详情个人主页
	public static String detail_personal_url = host + "api/product/userInfo/";

	// 重置密码
	public static String resetPwdUrl = host + "api/user/resetPassword";

	// 发送短信验证码
	public static String smsValidateUrl = host + "api/user/sms";

	// 退出登录
	public static String logoutUrl = host + "api/user/logout";

	// 未读消息
	public static String unreadMessage_url = host + "api/message/unread";

	// 会话验证
	public static String sessionVerifyUrl = host + "api/user/sessionCheck";

	// 产品详情
	public static String product_detail = host + "api/product/";

	// 产品服务
	public static String product_service = host + "api/product/serv/";

	// 贴士详情
	public static String tips_detail = host + "api/tips/detail";

	// 相似产品
	public static String simillar_product = host + "api/product/others";

	// TA的产品
	public static String ta_product = host + "api/userInfo/product/list";

	// 行程规划
	public static String product_journey = host + "api/product/journey/";

	// 行程规划详情
	public static String journey_detail = host + "api/journey/";

	// 点赞/取消点赞
	public static String appericate_url = host + "api/like";

	// 产品评论
	public static String product_comment = host + "api/product/comment/";

	// 产品评论
	public static String product_detail_comment = host + "api/comment";

	// 产品评论
	public static String micro_comment = host + "api/comment";

	// 更多评论
	public static String product_all_comments = host + "api/product/comments";

	// 结伴
	public static String go_with = host + "api/together";

	// 我的结伴
	public static String my_go_with = host + "api/together/apply";

	// 回应
	public static String my_answer = host + "api/together/answer";

	// 圈子
	public static String zone_url = host + "api/circle";
	// 现金券
	public static String cashs = host + "api/coupon/self";
	public static String validCoupon = host + "api/coupon/validCoupon";

	// 侧边栏-好友列表
	public static String friend_list_url = host + "api/friends";

	// 微游记-照片列表
	public static String tripStory_url = host + "api/tripstory/list";

	// 获取 上传凭证
	public static String getUploadToken_url = host + "api/image/token";

	// 微游记-删除游记
	public static String tripStory_delete_url = host + "api/tripstory/self/"; // delete请求方式

	// 资源列表
	public static String travel_time_resources_url = host + "api/push/resource/";

	// 微游记列表
	public static String tripStory_self_url = host + "api/tripstory/self";

	// 连载
	public static String series_self_url = host + "api/series/self";
	// 我的旅程
	public static String mytrip_url = host + "api/mytrip";

	// 旅程概况(游客)
	public static String mytrip_detail_url = host + "api/mytrip/";

	// 旅程概况(游客)
	public static String mytrip_cancel_url = host + "api/mytrip/cancel";

	// 旅程规划(游客)
	public static String mytrip_detail_plan_url = host + "api/mytrip/journey/";

	// 旅程评论
	public static String mytrip_detail_comment_url = host + "api/mytrip/comment/";

	// 旅程评论 (更多)
	public static String mytrip_detail_comments_url = host + "api/mytrip/comments/";

	// 行程单列表(游客)
	public static String myItinerary_url = host + "api/itinerary";

	// 行程单详情(游客)
	public static String myItinerary_detail_url = host + "api/itinerary/";

	// 关注好友
	public static String friend_focus_on_url = host + "api/friends";

	// 取消关注好友
	public static String friend_focus_off_url = host + "api/friends/";

	// 个人资料-个人主页 (自己)
	public static String individual_url = host + "api/userinfo/self";

	// 个人主页
	public static String personal_url = host + "api/userInfo/";

	// 个人资料-更多
	public static String personal_more_url = host + "api/userinfo/";

	// 个人关注
	public static String personal_focus_url = host + "api/userinfo/focus";

	// 个人粉丝
	public static String personal_fans_url = host + "api/userinfo/fans";

	// 精选及目的地-目的地资源推荐
	public static String location_res_recommend_url = host + "api/push/dest";

	// 精选及目的地-目的地资源推荐（修改后端口）（假d）
	// public static String location_res_recommend_url = host +
	// "api/push/choice.do";
	// public static String location_res_recommend_url = host + "api/push/dest";
	// 精选及目的地-目的地资源推荐（修改后端口）
	// public static String location_res_recommend_url = host +
	// "api/push/choice.do";

	// 目的地列表(全部)
	public static String destinations_all_url = host + "api/destination";

	// 主题列表(全部)
	public static String topics_all_url = host + "api/topic/";

	// 主题关联产品
	public static String single_topic_product_url = host + "api/topic/product/";

	// 目的地详情
	public static String destinations_detail_url = host + "api/destination/";

	// 精选及目的地-主题及时间栏目推荐
	public static String topics_time_recommend_url = host + "api/push/topicAndTime";

	// 精选推荐
	public static String picked_url = host + "api/push/choice";

	// 结伴-结伴详情
	public static String together_url = host + "api/together/";

	// 结伴-结伴编辑
	public static String addtogether_url = host + "api/together/edit";

	// 系统设置-查询最新版本
	public static String version_update_url = host + "api/system/version";

	// 产品可预订资源
	public static String serv_book_url = host + "api/product/servBook/";

	// 预订价格
	public static String apply_price_url = host + "api/apply/price";

	// 预订申请接口
	public static String apply_book_url = host + "api/apply/book";

	// 生成订单获取订单号接口
	public static String apply_orderInfo_url = host + "api/apply/orderInfo";

	// 查询订单详情
	public static String query_orderdetail_url = host + "api/mytrip/detail/";

	// 订单退订
	public static String apply_chargeback_url = host + "api/apply/chargeback";

	// 申诉
	public static String order_staff_url = host + "api/order/staff";

	// 修改用户信息
	public static String userinfo_edit_url = host + "api/userInfo/edit";

	// 查询用户信息
	public static String userinfo_detail_url = host + "api/userInfo/detail";

	// 查询消息队列
	public static String messsage_queues_url = host + "api/message/queues";

	// 将删除的消息队列中的所有消息置为已读
	public static String queue_read_url = host + "api/message/queue/read";

	// 查询群聊成员接口
	public static String message_member_url = host + "api/message/members";

	// 退出群聊
	public static String drop_group_url = host + "api/message/queues/";

	// 查询消息队列详情
	public static String message_queue_url = host + "api/message/queue";

	// 发送消息
	public static String send_message_url = host + "api/message";

	// 精选轮播图(首页轮播图)
	public static String push_pics_url = host + "api/push/pics";

	// 获取闪屏数据
	public static String push_flash_url = host + "api/push/flash";

	// 获取产品图片
	public static String product_pic_url = host + "api/product/pics/";

	// add by lij 2015/09/28 start
	// 产品详情
	public static String product_buyers = host + "api/product/buyers";

	// 产品评论
	public static String product_detail_starcomment = host + "api/appraise";

	// 产品评论
	public static String product_detail_reply = host + "api/comment/reply";

	// 删除订单
	public static String order_delete = host + "api/mytrip/";

	// 进入群聊
	public static String join_team_talk = host + "api/groupchat";

	// 进入私聊
	public static String join_private_chat = host + "api/chat";

	// 添加收藏
	public static String add_store = host + "api/favorite";

	// 取消收藏
	public static String cancel_store = host + "api/favorite";

	// 我的
	public static String userinfo_subject = host + "api/userInfo/subject";
	// add by lij 2015/09/28 end

	// 发送验证码 add by shangwx
	public static final String verify_code = host + "api/user/sms";

	// 定制 add by shangwx
	public static final String submit_customization = host + "api/desire";

	// 定制 add by shangwx
	public static final String get_customization = host + "api/desire";

	// 定制 add by shangwx
	public static final String del_customization = host + "api/desire/";

	// 我的同行人 add by guc
	public static final String my_partner = host + "api/userInfo/partner";

	// 我的同行人 add by guc
	public static final String my_invoice = host + "api/invoice";

	public static final String my_collection = host + "api/favorite";

	// 点赞/取消点赞add by guyl
	public static final String set_like = host + "api/like";

	// 出行单 add by shangwx
	public static final String get_itinerary = host + "api/itinerary/cust";

	// 晒图 /api/ tripstory/myselfList

	public static final String oldPic = host + "api/tripstory/olderList";

	// 出行单详情 add by shangwx
	public static final String get_itinerary_detail = host + "api/itinerary/";

	// 查询文章活动
	public static final String search_act = host + "api/article";

	// 目的地关联产品by shangwx
	public static final String get_location_product = host + "api/destination/p";
	// 俱乐部
	public static final String get_location_club = host + "api/club/wap";
	// 广场标签列表
	public static final String get_square_tags = host + "api/square/tags";

	// 广场话题列表
	public static final String get_square_topics = host + "api/square/theme";

	// 晒图
	public static final String square_pic = host + "api/tripstory/";

	// 连载
	public static final String square_serial = host + "api/series/";
	// 我的账户
	public static String my_account_subject = host + "api/userInfo/subject";
	// 账户明细
	public static String my_account_fas = host + "api/fas";
	// 账户提现
	public static String my__fas = host + "api/fas/cash";
	public static String trip_story_url = host + "api/tripstory/self";
	// 账号流水
	public static String payment_account_url = host + "api/paymentaccount/flow";
	// 账户类型
	public static String payment_account_category_url = host + "api/paymentaccount/type";
	// 添加账户
	public static String payment_account_add_url = host + "api/paymentaccount";
	// 账户列表
	public static String payment_account_list_url = host + "api/paymentaccount";
	// 编辑账户
	public static String payment_account_edit_url = host + "api/paymentaccount/edit";
	// 删除账户
	public static String payment_account_delete_url = host + "api/paymentaccount/";
	// 默认账户url
	public static String payment_account_isDefault_url = host + "api/paymentaccount/save";
	// 默认广场话题
	public static String get_square_theme = host + "api/square/theme";

	// 默认广场
	public static String get_square_list = host + "api/square/list";

	// 默认账户url
	public static String square_pic_to_serial = host + "api/series/ref";
	// 我的连载列表 /api/series/title

	public static final String series = host + "api/series/title";

	public static final String series_edit = host + "api/series/edit";

	// 查询消息通知详情
	public static String message_notice_url = host + "api/push/message";

	// 批量添加我的足迹
	public static String add_footprint = host + "api/userInfo/btchmyFP";

	// 我的足迹
	public static String footprint = host + "api/userInfo/myFP";
	public static String del_series = host + "api/series/self/";
	// 粉丝
	public static String fans = host + "api/userInfo/fans";
	// 关注
	public static String focus = host + "api/userInfo/focus";
}
