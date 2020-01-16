package com.wxzd.gfzdj.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.wxzd.gfzdj.global.base.BasePresenter;
import com.wxzd.gfzdj.global.base.BaseView;
import com.wxzd.gfzdj.global.base.CallBackListener;
import com.wxzd.gfzdj.views.activities.LoginActivity;

import org.json.JSONException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * 网络请求的工具类
 */
public class HttpManager {

    private static final String TAG = "HttpManager";
    private Gson gson = new Gson();
    ;

    public HttpManager() {
    }

    /**
     * 統一的网络请求
     *
     * @param observable          被观察者
     * @param <T>                 网络返回的数据
     * @param compositeDisposable 用于取消网络请求
     */
    public <T, K extends BasePresenter> void request(Observable<Response<T>> observable,
                                                     final CompositeDisposable compositeDisposable, final BaseView<K> view,
                                                     final CallBackListener<T> listener) {

        if (observable == null || compositeDisposable == null || view == null || listener == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<T> response) {

                        if (response == null) {//!view.isActive()
                            LogUtils.e(TAG, "response == null");
                            return;
                        }
                        if (response.getResults() != null) {
                            LogUtils.e(TAG, response.toString());
                        }
                        int code = response.getStatus();
                        LogUtils.e(TAG, "返回码: " + code);
//                        T data = response.getResults();
//                        listener.onSuccess(response.getResults());
                        switch (code) {
                            case 200:
                                listener.onSuccess(response.getResults());
                                break;
                            case 401:
                                listener.onError(response.getMsg());
                                break;
                            case 403:   //token过期或者错误
                                SPUtils.getInstance().put(Constant.KEY_TOKEN, "");
                                listener.onError(response.getMsg());
                                Activity context = ActivityUtils.getTopActivity();
                                if (context == null) {
                                    return;
                                }
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                break;
//
//                        /*
//                         * 成功
//                         */
//                            case 1:
//                                listener.onSuccess(response.getResults());
//                                break;
//
//
//                            // TODO: 2017/12/29  token error---跳转到登录界面
//                            case 2:
//                                Context context = MyApplication.getAppComponent().getContext();
//                                if (context == null) {
//                                    return;
//                                }
//                                break;
//
                            default:
                                listener.onError(response.getMsg());
//                                try {
//                                    String s = gson.toJson(response);
//                                    ErrorListResponse errorListResponse = gson.fromJson(s, ErrorListResponse.class);
//                                    List<ErrorListResponse.ErrorBean> errorBeen = errorListResponse.messageList;
//                                    listener.onError(errorBeen.get(0).messageBody);
//                                } catch (Exception e) {
//                                    listener.onError(Constant.REQUEST_FAILURE);
//                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e == null) {//!view.isActive() ||
                            return;
                        }
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            LogUtils.e(TAG, "onError: 错误码" + httpException.code());
                            switch (httpException.code()) {
                                /*
                                 * 没有网络
                                 */
                                case 504:
                                    listener.onError(Constant.NET_WORK_ERROR);
                                    break;
                                case 403:   //token过期或者错误
                                    Context context = MyApplication.getAppComponent().getContext();
                                    if (context == null) {
                                        return;
                                    }
                                    SPUtils.getInstance().put(Constant.KEY_TOKEN, "");
                                    // TODO: 2018/8/31 去登录
//                                    Intent intent = new Intent(context, LoginOrRegisterActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(intent);
                                    break;
                                case 404:
                                    listener.onError(Constant.PAGE_NOT_EXIST);
                                    break;
                                case 500:
                                    listener.onError(Constant.SERVER_ERROR);
                                    break;
                                default:
//                                    listener.onError(Constant.REQUEST_FAILURE);
                                    listener.onError(e.getMessage());
                                    break;
                            }
                            return;
                        }
                        if (e instanceof ConnectException) {
                            listener.onError(Constant.CAN_NOT_CONNECT_TO_SERVER);
                            return;
                        }
                        if (e instanceof SocketException) {
                            listener.onError(Constant.NET_WORK_ERROR);
                            return;
                        }
                        if (e instanceof SocketTimeoutException) {
                            listener.onError(Constant.time_out);
                            return;
                        }
                        if (e instanceof SecurityException) {
                            listener.onError(Constant.net_permission);
                            return;
                        }
                        if (e instanceof JsonParseException || e instanceof JSONException || e instanceof MalformedJsonException) {
                            listener.onError(Constant.DATA_PARSE_EXCEPTION);
//                            listener.onError(Constant.REQUEST_FAILURE);
                            return;
                        }
//                        listener.onError(Constant.REQUEST_FAILURE);
                        listener.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
