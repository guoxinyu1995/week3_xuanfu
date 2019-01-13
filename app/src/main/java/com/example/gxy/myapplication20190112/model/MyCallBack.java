package com.example.gxy.myapplication20190112.model;

public interface MyCallBack<E> {
        void onSuccess(E data);
        void onError(String erroe);
}
