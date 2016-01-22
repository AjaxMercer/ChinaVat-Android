package org.viewutils;

import android.app.Application;
import android.content.Context;

import org.viewutils.model.ViewInjector;
import org.viewutils.model.ViewInjectorImpl;

import java.lang.reflect.Method;

/**
 * Created by zhaoyechao on 2016/1/22.
 */
public class ViewUtil {

    private static boolean debug;
    private static Application app;
    private static ViewInjector injector;

    private ViewUtil() {
    }

    /**
     * @param application
     */
    public static void init(Application application) {
        if (app == null) {
            app = application;
        }
    }

    /**
     * @param viewInjector
     */
    public static void setViewInjector(ViewInjector viewInjector) {
        injector = viewInjector;
    }


    /**
     * @return
     */
    public static Application app() {
        if (app == null) {
            try {
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate()"
                        + " and register your Application in manifest.");
            }
        }
        return app;
    }

    public static ViewInjector view() {
        if (injector == null) {
            ViewInjectorImpl.registerInstance();
        }
        return injector;
    }

    public static void setDebug(boolean bool) {
        debug = bool;
    }

    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }
}
