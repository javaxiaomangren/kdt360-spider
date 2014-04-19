package com.amap.task.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;


public class HttpUtils {
    static String UTF8 = "UTF-8";
    static int DEFAULT_SO_TIMEOUT = 10;
    static int DEFAULT_READ_TIMEOUT = 3;


    public static String postXml(String url, String param, int timeout) throws IOException {
        if (timeout == 0) {
            timeout = DEFAULT_READ_TIMEOUT;
        }
        return post(url, param, "text/xml", UTF8, DEFAULT_SO_TIMEOUT, timeout);
    }

    public static String postXml(String url, String param) throws IOException {
        return post(url, param, "text/xml", UTF8, DEFAULT_SO_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public static String post(String url, String param, String contextType, String coder, int soTimeout, int timeout) throws IOException {
        HttpClientParams httpParams = new HttpClientParams();
        httpParams.setSoTimeout(soTimeout * 1000);
        httpParams.setConnectionManagerTimeout(timeout * 1000);

        PostMethod post = new PostMethod(url);
        RequestEntity entity = new StringRequestEntity(param, contextType, coder);
        post.setRequestEntity(entity);

        HttpClient httpclient = new HttpClient();
//        httpclient.getHostConfiguration().setProxy("127.0.0.1", 8087);
        httpclient.setParams(httpParams);
        httpclient.executeMethod(post);
        return post.getResponseBodyAsString();
    }

}
