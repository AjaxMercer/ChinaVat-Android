package org.utils.parser;

import com.squareup.okhttp.Response;

public interface Parser<T> {
    T parse(Response response);
}
