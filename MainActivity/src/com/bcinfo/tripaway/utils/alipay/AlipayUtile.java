package com.bcinfo.tripaway.utils.alipay;

import java.net.URLEncoder;

/**
 * @author hanweipeng
 * @date 2014年11月26日 下午4:06:25
 */
public class AlipayUtile
{
    public static String getNewOrderInfo(String orderId, String title, String orderPrice)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(Keys.DEFAULT_PARTNER);
        sb.append("\"&out_trade_no=\"");
        sb.append(orderId);
        sb.append("\"&subject=\"");
        sb.append(title);
        sb.append("\"&body=\"");
        sb.append("aa");
        sb.append("\"&total_fee=\"");
//        sb.append(0.01);
        sb.append(orderPrice);
        sb.append("\"&notify_url=\"");
        
        // 网址需要做URL编码
//       sb.append("http://app.tripaway.cn/alipay/pay/notify.do");// 正式环境地址
       
       sb.append("http://inf.tripaway.cn/alipay/pay/notify.do");// 正式环境地址
      
//       sb.append("http://it.tripaway.cn/alipay/pay/notify.do");// 仿真环境地址
//         sb.append("http://125.71.209.90:8099/tripaway-api/pay/notify_url.jsp");// 测试环境地址
        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");
        sb.append("\"&return_url=\"");
        sb.append("http://m.alipay.com");
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");
        sb.append(Keys.DEFAULT_SELLER);
        
        // 如果show_url值为空，可不传
        // sb.append("\"&show_url=\"");
        // sb.append("\"&it_b_pay=\"");
        sb.append("\"");
        String info = sb.toString();
        String sign = Rsa.sign(info, Keys.PRIVATE);
        sign = URLEncoder.encode(sign);
        info += "&sign=\"" + sign + "\"&" + getSignType();
        
        return info;
    }
    
    private static String getSignType()
    {
        return "sign_type=\"RSA\"";
    }
}
