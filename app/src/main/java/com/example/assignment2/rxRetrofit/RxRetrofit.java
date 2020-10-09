package com.example.assignment2.rxRetrofit;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxRetrofit {
    private static String baseUrl = "https://api.boyang.me/";
    private static RxService rxService;
    private RxRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        rxService = retrofit.create(RxService.class);
    }
    private static class SingletonHolder{
        private static final RxRetrofit INSTANCE = new RxRetrofit();
    }

    public static RxRetrofit getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void requestUser(Subscriber<Response<ResponseBody>> subscriber,String type,String name,String password) {
        Observable<Response<ResponseBody>> observable = null;
        switch(type){
            case "login":
                observable = rxService.login(name, password);
                break;
            case "register":
                observable = rxService.register(name,password);
                break;
        }
        if(observable != null) {toSubscribe(observable,subscriber);}
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
