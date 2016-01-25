package org.utils.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.utils.parser.Parser;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class Callback<T> implements com.squareup.okhttp.Callback {
    private Parser<T> mParser;
    private static final int CALLBACK_SUCCESSFUL=0x01;
    private static final int CALLBACK_FAILED=0x02;
    static class UIHandler<T> extends Handler{
        private WeakReference mWeakReference;
        public UIHandler(org.utils.callback.Callback<T> callback){
            super(Looper.getMainLooper());
            mWeakReference=new WeakReference(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CALLBACK_SUCCESSFUL: {
                    T t = (T) msg.obj;
                    org.utils.callback.Callback callback = (org.utils.callback.Callback) mWeakReference.get();
                    if (callback != null) {
                        callback.onResponse(t);
                    }
                    break;
                }
                case CALLBACK_FAILED: {
                    IOException e = (IOException) msg.obj;
                    org.utils.callback.Callback callback = (org.utils.callback.Callback) mWeakReference.get();
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    private Handler mHandler=new UIHandler(this);

    public Callback(Parser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        Message message=Message.obtain();
        message.what=CALLBACK_FAILED;
        message.obj=e;
        mHandler.sendMessage(message);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            T parseResult = mParser.parse(response);
            Message message=Message.obtain();
            message.what=CALLBACK_SUCCESSFUL;
            message.obj=parseResult;
            mHandler.sendMessage(message);
        } else {
            Message message=Message.obtain();
            message.what=CALLBACK_FAILED;
            mHandler.sendMessage(message);
        }
    }

    public void onResponse(T t){

    }
    public  void onFailure(IOException e){

    }
}
