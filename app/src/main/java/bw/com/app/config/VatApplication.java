package bw.com.app.config;

import android.app.Application;


import org.netutils.NetUtils;
import org.viewutils.ViewUtil;


/**
 * Created by zhaoyechao on 2016/1/19.
 */
public class VatApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        ViewUtil.init(this);
        NetUtils.init(this);
    }
}
