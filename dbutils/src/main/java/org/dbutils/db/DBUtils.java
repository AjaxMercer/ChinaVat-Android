package org.dbutils.db;

import android.app.Application;

/**
 * Created by zhaoyechao on 2016/1/26.
 */
public class DBUtils {

    private static boolean isDebug;
    private static Application app;
    private static DbManager dbManager;

    private DBUtils(){}

    /**
     * 传入app对象
     * @param application
     */
    public static void init(Application application){
        if(app == null){
            app = application;
        }
    }
    public static Application getApp(){
        return app;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        DBUtils.isDebug = isDebug;
    }

    public static DbManager getDb(DbManager.DaoConfig daoConfig) {
        return DbManagerImpl.getInstance(daoConfig);
    }
}
