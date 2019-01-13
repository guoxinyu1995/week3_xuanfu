package com.example.gxy.myapplication20190112.network;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitMessage {
    private final String BASE_URL = "http://www.zhaoapi.cn/product/";
    private static RetrofitMessage instance;
    private final BaseApis baseApis;

    private RetrofitMessage() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(10,TimeUnit.SECONDS);
        builder.readTimeout(10,TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        builder.retryOnConnectionFailure(true);
        OkHttpClient build = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(build)
                .build();
        baseApis = retrofit.create(BaseApis.class);
    }

    public static RetrofitMessage getInstance() {
        if(instance == null ){
            synchronized (RetrofitMessage.class){
                instance = new RetrofitMessage();
            }
        }
        return instance;
    }
    //创建观察者
    private Observer getObserver(final HttpListener listener){
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(listener!=null){
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if(listener!=null){
                        listener.onSuccess(data);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(listener!=null){
                        listener.onFail(e.getMessage());
                    }
                }
            }

        };
        return observer;
    }
    /**
     * get
     * */
    public void get(String url,HttpListener listener){
        baseApis.get(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getObserver(listener));
    }
    /**
     * post
     * */
    public void post(String url,Map<String,String> map,HttpListener listener){
        baseApis.post(url,map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getObserver(listener));
    }
    //定义接口
    public interface HttpListener{
        void onSuccess(String data);
        void onFail(String reeor);
    }
}
