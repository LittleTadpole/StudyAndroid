package com.wxzd.gfzdj.global.base;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gfzdj.R;
import com.wxzd.gfzdj.global.AppComponent;
import com.wxzd.gfzdj.global.MyApplication;
import com.wxzd.gfzdj.global.utils.CommonUtils;
import com.wxzd.gfzdj.global.views.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment implements BaseView<T>, IBaseView {
    protected final String TAG = this.getClass().getSimpleName();
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected View mContentView;
    LoadingDialog mLoadingDialog;
    protected T presenter;
    protected Unbinder unBinder;
    private long clickTime;
    public boolean isInnerFragment;


    public BaseFragment() {
        injectComponent();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(bindLayoutID(), container, false);
        unBinder = ButterKnife.bind(this, mContentView);
        initView(savedInstanceState);
        return mContentView;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData(savedInstanceState);
        initListener();
        doBusiness();
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtils.isFastClick())
            onWidgetClick(view);
    }


    /**
     * 注入DaggerComponent
     */
    protected abstract void injectComponent();


    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }


    /**
     * 获取全局的组件
     *
     * @return AppComponent的实例
     */
    protected AppComponent getAppComponent() {
        return MyApplication.getAppComponent();
    }


    public String getResString(int resid) {
        return getResources().getString(resid);
    }

    public int getResColor(int resid) {
        return getResources().getColor(resid);
    }

    /**
     * [页面跳转]
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
        }
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onDestroyView() {
        dismissLoadingDialog();
        if (presenter != null) {
            presenter.cancelRequest();
        }
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
        super.onDestroyView();
    }


    public <T extends View> T findViewById(@IdRes int id) {
        if (mContentView == null)
            throw new NullPointerException("ContentView is null.");
        return mContentView.findViewById(id);
    }


    public View getContentView() {
        return mContentView;
    }


}
