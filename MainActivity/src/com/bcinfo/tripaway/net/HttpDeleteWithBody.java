package com.bcinfo.tripaway.net;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import android.net.Uri;

/**
 * 继承HttpEntityEnclosingRequestBase 基本的http请求来实现httpDelete传递请求参数的功能
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月26日 15:39:22
 * 
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase
{
    public static final String METHOD_NAME = "DELETE";

    /*
     * 返回http请求的方法 例如get post patch 或者delete等
     */
    @Override
    public String getMethod()
    {

        return METHOD_NAME;
    }

    public HttpDeleteWithBody(final String uri)
    {
        super();
        this.setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri)
    {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody()
    {
        super();

    }

}
