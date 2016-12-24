package com.bcinfo.tripaway.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacSha1加密算法
 * @author  zhangbingkang
 * @version V1.0  创建时间：2013-12-25 下午2:14:29 
 */
public class HmacSha1
{
    public static String hmacSha1(String datas)
    {
        //加密的key值
        String key = "onecard";
        try
        {
            byte[] data = key.getBytes("UTF-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);
            byte[] text = datas.getBytes("UTF-8");
            //完成 Mac 操作
            byte[] text1 = mac.doFinal(text);
            StringBuilder sb = new StringBuilder(10);
            for (int i = 0; i < text1.length; i++)
            {
                sb.append(String.format("%02x", text1[i]));
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return null;
    }
}
