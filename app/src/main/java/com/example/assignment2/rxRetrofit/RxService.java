package com.example.assignment2.rxRetrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface RxService {
    @GET("users/{name}/{password}")
    Observable<Response<ResponseBody>> login(@Path("name") String name,@Path("password") String password);

    @POST("users/{name}/{password}")
    Observable<retrofit2.Response<ResponseBody>> register(@Path("name") String name,@Path("password") String password);
}
