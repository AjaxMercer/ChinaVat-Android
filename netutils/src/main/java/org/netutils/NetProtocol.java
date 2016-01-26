package org.netutils;


import org.netutils.callback.Callback;
import org.netutils.impl.HttpMethod;
import org.netutils.impl.RequestParams;

/**
 * 网络请求类接口
 */
public interface NetProtocol {

    /**
     * 异步GET请求
     *
     * @param entity
     * @param callback
     * @param <T>
     * @return
     */
    <T> Callback.Cancelable get(RequestParams entity, Callback.CommonCallback<T> callback);

    /**
     * 异步POST请求
     *
     * @param entity
     * @param callback
     * @param <T>
     * @return
     */
    <T> Callback.Cancelable post(RequestParams entity, Callback.CommonCallback<T> callback);

    /**
     * 异步请求
     *
     * @param method
     * @param entity
     * @param callback
     * @param <T>
     * @return
     */
    <T> Callback.Cancelable request(HttpMethod method, RequestParams entity, Callback.CommonCallback<T> callback);


    /**
     * 同步GET请求
     *
     * @param entity
     * @param resultType
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T getSync(RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步POST请求
     *
     * @param entity
     * @param resultType
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T postSync(RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步请求
     *
     * @param method
     * @param entity
     * @param resultType
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T requestSync(HttpMethod method, RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步请求
     *
     * @param method
     * @param entity
     * @param callback
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T requestSync(HttpMethod method, RequestParams entity, Callback.TypedCallback<T> callback) throws Throwable;
}
