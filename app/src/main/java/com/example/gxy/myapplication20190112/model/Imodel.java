package com.example.gxy.myapplication20190112.model;

import java.util.Map;

public interface Imodel {
    void getRequest(String url, Class clazz, MyCallBack myCallBack);
    void postRequest(String url, Map<String, String> map, Class clazz, MyCallBack myCallBack);
}
