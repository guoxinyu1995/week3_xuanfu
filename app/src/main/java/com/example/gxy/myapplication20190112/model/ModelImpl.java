package com.example.gxy.myapplication20190112.model;

import com.example.gxy.myapplication20190112.network.RetrofitMessage;
import com.google.gson.Gson;

import java.util.Map;

public class ModelImpl implements Imodel {
    @Override
    public void getRequest(String url, final Class clazz, final MyCallBack myCallBack) {
        RetrofitMessage.getInstance().get(url, new RetrofitMessage.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack!=null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(myCallBack!=null){
                        myCallBack.onError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String reeor) {
                if(myCallBack!=null){
                    myCallBack.onError(reeor);
                }
            }
        });
    }

    @Override
    public void postRequest(String url, Map<String, String> map, Class clazz, MyCallBack myCallBack) {

    }
}
