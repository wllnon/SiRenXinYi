package com.test.wllnon.sirenxinyi.utils.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public class GsonRequest<T> extends Request<T> {
    private static final int TIME_OUT = 10000;

    private final Response.Listener<T> mListener;
    private static Gson mGson = GsonUtils.newInstance().getGson();
    private Class<T> mClass;
    private Map<String, String> mParams;
    private Type mType;


    public GsonRequest(int method, Map<String, String> params, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
        mParams = params;
        setMyRetryPolicy();
    }

    public GsonRequest(int method, Map<String, String> params, String url, Type type, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mType = type;
        mListener = listener;
        mParams = params;
        setMyRetryPolicy();
    }

    private void setMyRetryPolicy() {
        setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, null, url, clazz, listener, errorListener);
    }

    public GsonRequest(String url, Type type, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, null, url, type, listener, errorListener);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (mType == null)
                return Response.success(mGson.fromJson(jsonString, mClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            else
                return (Response<T>) Response.success(mGson.fromJson(jsonString, mType),
                        HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}