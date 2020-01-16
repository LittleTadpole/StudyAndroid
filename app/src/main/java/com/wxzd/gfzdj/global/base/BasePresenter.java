package com.wxzd.gfzdj.global.base;



import com.wxzd.gfzdj.global.HttpManager;
import com.wxzd.gfzdj.global.RetrofitService;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ckw
 * on 2017/11/30.
 */
public abstract class BasePresenter {
    protected RetrofitService mRetrofitService;
    protected CompositeDisposable mCompositeDisposable;
    protected HttpManager mHttpManager;


    public BasePresenter(RetrofitService mRetrofitService, HttpManager mHttpManager) {
        this.mRetrofitService = mRetrofitService;
        this.mHttpManager = mHttpManager;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        mCompositeDisposable.clear();
    }
}
