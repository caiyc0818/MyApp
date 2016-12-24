package com.bcinfo.tripaway.net;

import org.apache.http.client.methods.HttpPut;

/**
 * 继承HttpPut的HttpPatch HttpPatch用于部分更新; HttpPut用于全部更新
 * 
 * @function
 * @author asus-pc
 * @version 1.0 2015年5月20日 10:54:33
 */
public class HttpPatch extends HttpPut
{

    public HttpPatch(String uri)
    {
        super(uri);

    }

    @Override
    public String getMethod()
    {

        return "PATCH";
    }

}
