package bw.com.app.network;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import bw.com.app.config.VatApplication;

public class VolleyRequest {

     public static StringRequest stringRequest;
     public static Context context;

     public static void requestGet(Context mContext, String url, String tag,
               VolleyInterface vif) {
          // 取消其他请求
          VatApplication.getHttpQueues().cancelAll(tag);

          stringRequest = new StringRequest(Method.GET, url, vif.loadListener(),
                    vif.errorListener());
          //设置请求标签
          stringRequest.setTag(tag);

          //添加并启动请求队列
          VatApplication.getHttpQueues().add(stringRequest);
          VatApplication.getHttpQueues().start();
     }

     public static void requestPost(Context mContext, String url, String tag,
               final Map<String, String> param, VolleyInterface vif) {
          // 取消其他请求
          VatApplication.getHttpQueues().cancelAll(tag);

          stringRequest = new StringRequest(url, vif.loadListener(),
                    vif.errorListener()) {
               @Override
               protected Map<String, String> getParams() {
                    return param;
               }
          };
          //设置请求标签
          stringRequest.setTag(tag);

          //添加并启动请求队列
          VatApplication.getHttpQueues().add(stringRequest);
          VatApplication.getHttpQueues().start();
     }

}