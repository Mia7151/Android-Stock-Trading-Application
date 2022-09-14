package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }
    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/searchutil/" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void companydesp(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/companydesp/" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void latestprice(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/latestprice/" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void histdata(Context ctx, String query, Long from_unix, Long to_unix, Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/histdata/" + query +"/" + from_unix +"/"+to_unix;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void companypeers(Context ctx, String query,Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/companypeersdata/" + query ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void socialsentiment(Context ctx, String query,Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/socialsentimentdata/" + query ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void recommendation(Context ctx, String query,Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/recommendationdata/" + query ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
    public static void news(Context ctx, String query,Response.Listener<String>
            listener, Response.ErrorListener errorListener){
        String url = "https://homework88.wl.r.appspot.com/api/v1.0.0/news/" + query ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }


}
