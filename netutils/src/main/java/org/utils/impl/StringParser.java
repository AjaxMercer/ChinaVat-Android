package org.utils.impl;

import com.squareup.okhttp.Response;

import org.utils.parser.Parser;

import java.io.IOException;


public class StringParser implements Parser<String> {
    @Override
    public String parse(Response response) {
        String result=null;
        try {
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
