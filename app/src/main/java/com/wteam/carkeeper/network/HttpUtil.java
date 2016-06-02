package com.wteam.carkeeper.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * Created by lhb on 2016/5/21.
 */
public class HttpUtil {

    /**
     * 实例化cookie对象
     */
    public static PersistentCookieStore myCookieStore = new PersistentCookieStore(CarkeeperApplication.getContext());

    /**
     * 私有化构造方法
     */
    private HttpUtil(){}
    /**
     * 实例话请求客户端对象
     */
    public static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(10000); // 设置链接超时，如果不设置，默认为10s
        client.setCookieStore(myCookieStore);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }
}
