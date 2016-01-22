package bw.com.app.network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Volley 回调接口
 */
public abstract class VolleyInterface {

    public Context mContext;

    public static Response.Listener<String> mListener;

    public static Response.ErrorListener mErrorListener;

    public VolleyInterface(Context Context, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        this.mContext = Context;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    //对外开放接口
    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);

    //请求成功接口
    public Response.Listener<String> loadListener() {
        this.mListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                onMySuccess(arg0);
            }
        };
        return mListener;
    }

    //请求失败接口
    public Response.ErrorListener errorListener() {
        this.mErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                onMyError(arg0);
            }
        };
        return mErrorListener;
    }
}