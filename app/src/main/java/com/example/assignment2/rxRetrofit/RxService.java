package com.example.assignment2.rxRetrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface RxService {
    //login
    @GET("users/{name}/{password}")
    Observable<Response<ResponseBody>> login(@Path("name") String name,@Path("password") String password);
    //sign up
    @POST("users/{name}/{password}")
    Observable<retrofit2.Response<ResponseBody>> register(@Path("name") String name,@Path("password") String password);
    //state
    @GET("state/{token}")
    Observable<Response<ResponseBody>> getState(@Path("token") String token);

    @PUT("state/{newstate}/{token}")
    Observable<Response<ResponseBody>> updateState(@Path("newstate") String newState,@Path("token") String token);

    //memo
    @GET("memo/{token}")
    Observable<Response<ResponseBody>> getMemo(@Path("token") String token);

    @POST("memo/{title}/{content}/{token}")
    Observable<Response<ResponseBody>> postMemo(@Path("title") String title,@Path("content") String content,@Path("token") String token);

    @PUT("memo/{memoid}/{title}/{content}/{token}")
    Observable<Response<ResponseBody>> putMemo(@Path("memoid") String memoId,@Path("title") String title,@Path("content") String content,@Path("token") String token);

    @DELETE("memo/{memoid}/{token}")
    Observable<Response<ResponseBody>> deleteMemo(@Path("memoid") String memoId,@Path("token") String token);

    //todolist
    @GET("todolist/{token}")
    Observable<Response<ResponseBody>> getToDoList(@Path("token") String token);

    @POST("todolist/{content}//{state}{token}")
    Observable<Response<ResponseBody>> postToDoList(@Path("content") String content,@Path("state") String state,@Path("token") String token);

    @PUT("todolist/{todolistid}/{content}//{state}{token}")
    Observable<Response<ResponseBody>> putToDoList(@Path("memoid") String memoId,@Path("content") String content,@Path("state") String state,@Path("token") String token);

    @DELETE("todolist/{todolistid}/{token}")
    Observable<Response<ResponseBody>> deleteToDoList(@Path("memoid") String memoId,@Path("token") String token);
}
