package com.bcinfo.tripaway.utils;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * 加密助手
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年3月16日 下午2:38:37
 */
public class MCryptUtil
{
    private String iv = "fedcba9876543210";//虚拟的 iv (秘钥偏移量)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String SecretKey = "bcinfo2015!@#$%^";//虚拟的 密钥 (需更改)    
//    public static String APPCLIENT_KEY ="bcinfo_appclient_key";
    public static String APPCLIENT_KEY ="bcinfo_appclient_key";
    
    public MCryptUtil()
    {
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        try
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String Encrypt(String sSrc) throws Exception
    {
        byte[] encryptResult = encrypt(sSrc);
        return new String(Base64.encodeBase64(encryptResult)).trim();//用base64转码
    }

    public String Decrypt(String sSrc) throws Exception
    {
        byte[] decryptFrom = android.util.Base64.decode(sSrc, android.util.Base64.DEFAULT);//先用base64转码
        byte[] decryptResult = decrypt(bytesToHex(decryptFrom));//解码  
        return new String(decryptResult, "UTF-8");
    }

    public byte[] encrypt(String text) throws Exception
    {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        byte[] encrypted = null;
        try
        {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        }
        catch (Exception e)
        {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception
    {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");
        byte[] decrypted = null;
        try
        {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(hexToBytes(code));
        }
        catch (Exception e)
        {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data)
    {
        if (data == null)
        {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++)
        {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }
        return str;
    }

    public static byte[] hexToBytes(String str)
    {
        if (str == null)
        {
            return null;
        }
        else if (str.length() < 2)
        {
            return null;
        }
        else
        {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++)
            {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

//    private static String padString(String source)
//    {
//        char paddingChar = ' ';
//        int size = 16;
//        int x = source.length() % size;
//        int padLength = size - x;
//        for (int i = 0; i < padLength; i++)
//        {
//            source += paddingChar;
//        }
//        return source;
//    }
}