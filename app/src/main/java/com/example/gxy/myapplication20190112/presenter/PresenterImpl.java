package com.example.gxy.myapplication20190112.presenter;



import com.example.gxy.myapplication20190112.model.ModelImpl;
import com.example.gxy.myapplication20190112.model.MyCallBack;
import com.example.gxy.myapplication20190112.view.Iview;

import java.util.Map;

public class PresenterImpl implements Ipresenter {
    private Iview mIview;
    private final ModelImpl model;

    public PresenterImpl(Iview iview) {
        mIview = iview;
        model = new ModelImpl();
    }


    @Override
    public void requestGet(String url, Class clazz) {
        model.getRequest(url, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIview.requestData(data);
            }

            @Override
            public void onError(String erroe) {
                mIview.requestFail(erroe);
            }
        });
    }

    @Override
    public void requestPost(String url, Map<String, String> map, Class clazz) {

    }
}
