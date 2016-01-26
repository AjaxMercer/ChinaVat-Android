package org.netutils;

import android.app.Application;

import org.netutils.impl.NetUtilsImpl;
import org.netutils.task.TaskController;
import org.netutils.task.TaskControllerImpl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by zhaoyechao on 2016/1/26.
 * <p/>
 * 网络任务管理类
 * 封装网络请求与相关异步管理
 *
 */
public class NetUtils {


    private static boolean debug;
    private static Application app;
    private static NetProtocol protocol;
    private static TaskController controller;

    static {
        TaskControllerImpl.registerInstance();
        // 默认信任所有https域名
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    private NetUtils() {
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean bool) {
        debug = bool;
    }

    /**
     * 传入application对象
     * @param application
     */
    public static void init(Application application) {
        if (app == null) {
            app = application;
        }
    }

    public static Application getApp() {
        return app;
    }

    /**
     * @param netImpl
     */
    public static void setNetProtocol(NetProtocol netImpl) {
        protocol = netImpl;
    }

    /**
     * 实例化方法
     * @return
     */
    public static NetProtocol protocol() {
        if (protocol == null) {
            NetUtilsImpl.registerInstance();
        }
        return protocol;
    }

    /**
     * 传入任务管理器
     * @param controllerImpl
     */
    public static void setTaskController(TaskControllerImpl controllerImpl) {
        if (controllerImpl == null) {
            controller = controllerImpl;
        }
    }


    public static TaskController task() {
        return controller;
    }


}
