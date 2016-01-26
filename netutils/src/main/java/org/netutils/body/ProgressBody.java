package org.netutils.body;


import org.netutils.impl.ProgressHandler;

/**
 * Created by wyouflf on 15/8/13.
 */
public interface ProgressBody extends RequestBody {
    void setProgressHandler(ProgressHandler progressHandler);
}
