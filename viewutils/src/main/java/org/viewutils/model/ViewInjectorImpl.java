package org.viewutils.model;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.viewutils.ViewUtil;
import org.viewutils.annotation.ContentView;
import org.viewutils.annotation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;


public final class ViewInjectorImpl implements ViewInjector {

    private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();
    private static final Object lock = new Object();
    private static ViewInjectorImpl instance;

    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.Fragment.class);
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable ignored) {

        }
    }

    private ViewInjectorImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ViewInjectorImpl();
                }
            }
        }
        ViewUtil.setViewInjector(instance);
    }

    /**
     * 查找并返回Activity"注解"的具体方法
     */
    private static ContentView findContentView(Class<?> thisCls) {
        //注解不为空,并且已经被添加至容器队列时返回null
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        ContentView contentView = thisCls.getAnnotation(ContentView.class);
        //当前子类没有注解信息时找父类
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    @SuppressWarnings("ConstantConditions")
    private static void injectObject(Object handler, Class<?> handlerType, ViewFinder finder) {

        if (handlerType == null || IGNORED.contains(handlerType)) {
            return;
        }

        // inject view
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {

                Class<?> fieldType = field.getType();
                if (
                /* 不注入静态字段 */     Modifier.isStatic(field.getModifiers()) ||
                /* 不注入final字段 */    Modifier.isFinal(field.getModifiers()) ||
                /* 不注入基本类型字段 */  fieldType.isPrimitive() ||
                /* 不注入数组类型字段 */  fieldType.isArray()) {
                    continue;
                }

                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if (viewInject != null) {
                    try {
                        View view = finder.findViewById(viewInject.value(), viewInject.parentId());
                        if (view != null) {
                            field.setAccessible(true);
                            //
                            field.set(handler, view);
                        } else {
                            throw new RuntimeException("Invalid @ViewInject for " + handlerType.getSimpleName() + "." + field.getName());
                        }
                    } catch (Throwable ex) {
                        Log.e(ex.getMessage(), ex.getMessage().toString());
                    }
                }
            }
        }
        injectObject(handler, handlerType.getSuperclass(), finder);
    }

    @Override
    public void inject(View view) {
        injectObject(view, view.getClass(), new ViewFinder(view));
    }

    @Override
    public void inject(Activity activity) {
        //获取Activity的ContentView的注解
        Class<?> handlerType = activity.getClass();
        try {
            ContentView contentView = findContentView(handlerType);
            if (contentView != null) {

                int viewId = contentView.value();
                if (viewId > 0) {
                    Method setContentViewMethod = handlerType.getMethod("setContentView", int.class);
                    setContentViewMethod.invoke(activity, viewId);
                }
            }
        } catch (Throwable ex) {
            Log.e(ex.getMessage(), ex.getMessage().toString());
        }

        injectObject(activity, handlerType, new ViewFinder(activity));
    }

    @Override
    public void inject(Object handler, View view) {
        injectObject(handler, handler.getClass(), new ViewFinder(view));
    }

    @Override
    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
        // inject ContentView
        View view = null;
        Class<?> handlerType = fragment.getClass();
        try {
            ContentView contentView = findContentView(handlerType);
            if (contentView != null) {
                int viewId = contentView.value();
                if (viewId > 0) {
                    view = inflater.inflate(viewId, container, false);
                }
            }
        } catch (Throwable ex) {
            Log.e(ex.getMessage(), ex.getMessage().toString());
        }

        // inject res & event
        injectObject(fragment, handlerType, new ViewFinder(view));

        return view;
    }

}
