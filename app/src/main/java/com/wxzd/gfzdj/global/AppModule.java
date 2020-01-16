package com.wxzd.gfzdj.global;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.gfzdj.BuildConfig;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context mContext) {
        this.mContext = mContext;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mContext;
    }

    @Singleton
    @Provides
    HttpManager provideHttpHelper() {
        return new HttpManager();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    /**
     * 获取Retrofit的实例
     *
     * @param baseUrl 公共的url
     * @param client  OkHttpClient的实例
     * @return Retrofit的实例 --- 单例的
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(@Named("base_url") String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
//                .addConverterFactory(new NullOnEmptyConverterFactory()) //解决后台返回空响应体的问题
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Named("base_url")
    @Provides
    String provideBaseUrl() {
        String ip = SPUtils.getInstance().getString("ip");
        LogUtils.e("ip",ip);
        return BuildConfig.baseUrl;

    }

    /**
     * 获取OkHttpClient的实例
     *
     * @param loggingInterceptor   日志拦截 debug模式打印日志 release模式不打印日志
     * @param cache                缓存的路径和大小
     * @param cacheInterceptor     缓存有网络的时候取最新数据 没有网络的时候取缓存数据
     * @param parameterInterceptor 公共参数
     * @return OkHttpClient的实例
     */
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache, @Named("cache_interceptor") Interceptor cacheInterceptor, @Named("parameter_interceptor") Interceptor parameterInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        //获取请求头里面的token
                        String authorization = response.header("Authorization");
                        if (!TextUtils.isEmpty(authorization)) {
                            SPUtils.getInstance().put(Constant.KEY_TOKEN, authorization);
                            LogUtils.e("token: ", authorization);
                        }
                        return response;
                    }
                })
                .cache(cache)
                .addInterceptor(cacheInterceptor)
                .addInterceptor(parameterInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
//                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    /**
     * OKHttp3日志拦截 debug版本打印日志 release版本不打印日志
     *
     * @return HttpLoggingInterceptor的实例
     */
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

    /**
     * 获取缓存实例---緩存路径和大小 50M
     *
     * @return Cache实例
     */
    @Provides
    Cache provideCache() {
        File cacheFile = new File(mContext.getExternalCacheDir(), "cache_network");
        return new Cache(cacheFile, 50 * 1024 * 1024);
    }


    /**
     * 获取网络缓存的拦截器---有网络的时候取最新数据, 没网络的时候取缓存中的数据---网络缓存只适用于GET请求
     *
     * @return Interceptor的实例
     */
    @Named("cache_interceptor")
    @Provides
    Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.isConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtils.isConnected()) {
                    CacheControl cacheControl = request.cacheControl();
                    String value = cacheControl.toString();
                    String noCache = "public, max-age=" + 0;
                    response = response.newBuilder()
                            .header("Cache-Control", (value == null || value.trim().length() == 0) ? noCache : value)
                            .build();
                } else {
                    response = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + 24 * 60 * 60 * 7)
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 获取Interceptor实例---添加公共的参数---给参数加密
     *
     * @return Interceptor对象
     */
    @Named("parameter_interceptor")
    @Provides
    Interceptor provideParameterInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String token = SPUtils.getInstance().getString(Constant.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    return chain.proceed(originalRequest);
                }
                Request authorised = originalRequest.newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(authorised);
//                String method = originalRequest.method();
//                String token = DbHelper.getAppToken(context);
//                String userId = DbHelper.getUserId(context);
//                if (method != null && method.equals("GET")) {
//                    HttpUrl.Builder builder = originalRequest.url().newBuilder();
//                    HttpUrl newHttpUrl = builder.build();
//                    int size = newHttpUrl.querySize();
//                    for (int i = 0; i < size; i++) {
//                        String name = newHttpUrl.queryParameterName(i);
//                        String value = newHttpUrl.queryParameterValue(i);
//                        builder.setQueryParameter(name, "");
//                    }
//                    newHttpUrl = builder
//                            .addQueryParameter("username", "")
//                            .addQueryParameter("strKey", "")
//                            .addQueryParameter("apptoken", "")
//                            .build();
//                    request = originalRequest.newBuilder().url(newHttpUrl).build();
//                    return chain.proceed(request);
//                } else if (method != null && method.equals("POST")) {
//                    RequestBody body = originalRequest.body();
//                    if (body != null && body instanceof FormBody) {
//                        FormBody.Builder formBuilder = new FormBody.Builder();
//                        FormBody formBody = (FormBody) body;
//                        int size = formBody.size();
//                        for (int i = 0; i < size; i++) {
//                            String name = formBody.name(i);
//                            String value = formBody.value(i);
//                            formBuilder.add(name, "");
//                        }
//                        formBody = formBuilder
////                                .add("username", "")
////                                .add("strKey", "")
//                                .add("Authorization", SPUtils.getInstance().getString(Constants.KEY_TOKEN))
//                                .build();
//                        request = originalRequest.newBuilder().post(formBody).build();
//                        return chain.proceed(request);
//                    }
//                }
//                return chain.proceed(originalRequest);
            }
        };
    }


}
