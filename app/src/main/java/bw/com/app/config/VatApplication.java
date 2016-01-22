package bw.com.app.config;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.viewutils.ViewUtil;


/**
 * Created by zhaoyechao on 2016/1/19.
 */
public class VatApplication extends Application{

    public static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        ViewUtil.init(this);
        queues = Volley.newRequestQueue(getApplicationContext());

    }
    public static RequestQueue getHttpQueues()
    {
        return queues;
    }
}
