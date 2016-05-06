package com.test.wllnon.sirenxinyi.utils.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.wllnon.sirenxinyi.application.Application;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/4/19.
 */
public class NetworkUtils {
    private RequestQueue requestQueue;

    private static class NetworkUtilsHolder {
        private static final NetworkUtils instance = new NetworkUtils(Application.getInstance());
    }

    public static NetworkUtils newInstance() {
        return NetworkUtilsHolder.instance;
    }

    private NetworkUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context, new OkHttp3StackUtils(new OkHttpClient()));
    }

    /**
     * @param tag This tag will be useful when you need to cancel requests
     * @param url Url which you want
     * @param listener Do something after the request has succeed
     * @param errorListener Do something while the request has been failure
     * @return
     */
    public StringRequest request(Object tag, String url, Response.Listener<String> listener,
                                    Response.ErrorListener errorListener) {
        return requestWithMethod(tag, Request.Method.GET, url, listener, errorListener);
    }

    /**
     * @param tag This tag will be useful when you need to cancel requests
     * @param method Request method
     * @param url Url which you want
     * @param listener Do something after the request has succeed
     * @param errorListener Do something while the request has been failure
     * @return
     */
    public StringRequest requestWithMethod(Object tag, int method, String url, Response.Listener<String> listener,
                                    Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(method, url, listener, errorListener);
        request.setTag(tag);
        requestQueue.add(request);
        return request;
    }

    /**
     * Get方法
     *
     * @param tag
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> gsonRequest(Object tag, String url, Class<T> clazz,
                                             Response.Listener<T> listener, Response.ErrorListener errorListener) {
        return gsonRequestWithMethod(tag, Request.Method.GET, null, url, clazz, listener, errorListener);
    }

    /**
     * Get方法
     *
     * @param tag
     * @param url
     * @param type
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> gsonRequest(Object tag, String url, Type type,
                                          Response.Listener<T> listener, Response.ErrorListener errorListener) {
        return gsonRequestWithMethod(tag, Request.Method.GET, null, url, type, listener, errorListener);
    }

    /**
     *
     * @param tag
     * @param params
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> gsonRequestWithMethod(Object tag, int method, Map<String, String> params, String url, Class<T> clazz,
                                              Response.Listener<T> listener, Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<>(method, params, url, clazz, listener, errorListener);
        request.setTag(tag);
        requestQueue.add(request);
        return request;
    }

    /**
     *
     * @param tag
     * @param params
     * @param url
     * @param type
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> gsonRequestWithMethod(Object tag, int method, Map<String, String> params, String url, Type type,
                                                    Response.Listener<T> listener, Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<>(method, params, url, type, listener, errorListener);
        request.setTag(tag);
        requestQueue.add(request);
        return request;
    }

    /**
     * Post方式2：json字符串
     *
     * @param url
     * @param jsonObject
     * @param listener
     * @param errorListener
     */
    public void jsonRequest(Object tag, String url, JSONObject jsonObject,
                                Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        jsonRequestWithMethod(tag, Request.Method.GET, url, jsonObject, listener, errorListener);
    }

    /**
     * Post方式2：json字符串
     *
     * @param url
     * @param jsonObject
     * @param listener
     * @param errorListener
     */
    public void jsonRequestWithMethod(Object tag, int method, String url, JSONObject jsonObject,
                                Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(method, url, jsonObject, listener, errorListener);
        jsonObjectRequest.setTag(tag);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * cancel requests with the tag
     *
     * @param tag
     */
    public void cancel(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
