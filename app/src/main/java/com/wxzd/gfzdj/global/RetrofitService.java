package com.wxzd.gfzdj.global;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitService {
//        String BASEURL="http://gank.io/api/";




    //获取短信验证码
    //获取短信验证码
    @POST("api/user/sendcode")
    Observable<Response<Object>> getSmsCode(@Body RequestBody body);


}
