package org.netutils.request;

import org.netutils.NetUtils;
import org.netutils.common.LogUtil;
import org.netutils.impl.ProgressHandler;
import org.netutils.impl.RequestParams;
import org.netutils.loader.Loader;
import org.netutils.loader.LoaderFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Uri请求发送和数据接收
 */
public abstract class UriRequest implements Closeable {

    protected final String queryUrl;
    protected final RequestParams params;
    protected final Loader<?> loader;

    protected ClassLoader callingClassLoader = null;
    protected ProgressHandler progressHandler = null;

    /*package*/ UriRequest(RequestParams params, Type loadType) throws Throwable {
        this.params = params;
        this.queryUrl = buildQueryUrl(params);
        this.loader = LoaderFactory.getLoader(loadType, params);
    }

    // build query
    protected String buildQueryUrl(RequestParams params) {
        return params.getUri();
    }

    public void setProgressHandler(ProgressHandler progressHandler) {
        this.progressHandler = progressHandler;
        this.loader.setProgressHandler(progressHandler);
    }

    public void setCallingClassLoader(ClassLoader callingClassLoader) {
        this.callingClassLoader = callingClassLoader;
    }

    public RequestParams getParams() {
        return params;
    }

    public String getRequestUri() {
        return queryUrl;
    }

    /**
     * invoke via Loader
     *
     * @throws IOException
     */
    public abstract void sendRequest() throws IOException;

    public abstract boolean isLoading();

    public abstract String getCacheKey();

    /**
     * 由loader发起请求, 拿到结果.
     *
     * @return
     * @throws Throwable
     */
    public Object loadResult() throws Throwable {
        return this.loader.load(this);
    }

    /**
     * 尝试从缓存获取结果, 并为请求头加入缓存控制参数.
     *
     * @return
     * @throws Throwable
     */
    public abstract Object loadResultFromCache() throws Throwable;

    public abstract void clearCacheHeader();

    public void save2Cache() {
        NetUtils.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    loader.save2Cache(UriRequest.this);
                } catch (Throwable ex) {
                    LogUtil.e(ex.getMessage(), ex);
                }
            }
        });
    }

    public abstract InputStream getInputStream() throws IOException;

    @Override
    public abstract void close() throws IOException;

    public abstract long getContentLength();

    public abstract int getResponseCode() throws IOException;

    public abstract String getResponseMessage() throws IOException;

    public abstract long getExpiration();

    public abstract long getLastModified();

    public abstract String getETag();

    public abstract String getResponseHeader(String name);

    public abstract Map<String, List<String>> getResponseHeaders();

    public abstract long getHeaderFieldDate(String name, long defaultValue);

    @Override
    public String toString() {
        return getRequestUri();
    }
}
