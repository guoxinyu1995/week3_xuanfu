package com.example.gxy.myapplication20190112.presenter;

import java.util.Map;

public interface Ipresenter {
    void requestGet(String url, Class clazz);
    void requestPost(String url, Map<String, String> map, Class clazz);
}
