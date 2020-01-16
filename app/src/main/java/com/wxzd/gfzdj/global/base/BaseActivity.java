package com.wxzd.gfzdj.global.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.gfzdj.R;
import com.wxzd.gfzdj.global.AppComponent;
import com.wxzd.gfzdj.global.MyApplication;
import com.wxzd.gfzdj.global.utils.CommonUtils;
import com.wxzd.gfzdj.global.views.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;


/**
 * description:  activity基类
 */

public abstract class BaseActivity<T extends BasePresenter> extends SupportActivity implements BaseView<T>, IBaseView {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();
    protected T presenter;
    protected Activity mActivity;
    protected LoadingDialog mLoadingDialog;
    private Unbinder mUnbinder;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "BaseActivity-->onCreate()");
        if (mContentView == null) {
            mContentView = LayoutInflater.from(this).inflate(bindLayoutID(), null);
        }
        setContentView(mContentView);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        ScreenUtils.adaptScreen4VerticalSlide(this, 750);   //屏幕适配
        BarUtils
                .setStatusBarColor(this, Color.TRANSPARENT)
                .setBackgroundResource(R.drawable.shape_theme);//设置状态栏背景色
        BarUtils.addMarginTopEqualStatusBarHeight(mContentView);
        BarUtils.setStatusBarLightMode(this, false);  //设置状态栏模式 true: 黑色
        mActivity = this;
        mUnbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        injectComponent();
        initListener();
        initData(savedInstanceState);
        doBusiness();
    }

    public void onResume() {
        super.onResume();
        dismissLoadingDialog();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        if (presenter != null) {
            presenter.cancelRequest();
        }
        super.onDestroy();
    }


    /**
     * 获取全局的组件
     *
     * @return AppComponent的实例
     */
    protected AppComponent getAppComponent() {
        return MyApplication.getAppComponent();
    }


    /**
     * 注入视图
     */
    protected void injectComponent() {

    }

    /**
     * 子类选择性实现
     */
    public void doBusiness() {

    }


    /**
     * @param title 标题
     *              显示默认返回箭头和中间标题
     */
    public void initAppBar(@NonNull String title) {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        View status = findViewById(R.id.status);
//        setStatusBarHeight(status);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 如果是自定义标题栏的情况，需要动态设置FirstChildView的高度
     **/
    public void setStatusBarHeight(View FirstChildView) {
        ViewGroup.LayoutParams params = FirstChildView.getLayoutParams();
        params.height = BarUtils.getStatusBarHeight();
    }

    /**
     * [页面跳转]
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtils.isFastClick())
            onWidgetClick(view);
    }


    public String getResString(int resid) {
        return getResources().getString(resid);
    }

    public int getResColor(int resid) {
        return getResources().getColor(resid);
    }


    /**
     * 解决点击输入框之外的地方,让软键盘消失,并取消editText的焦点
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        return false;
    }

    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }


//    protected void startH5Activity(String url, String title) {
//        Intent intent = new Intent(this, H5Activity.class);
//        intent.putExtra("url", url);
//        intent.putExtra("title", title);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//        startActivity(intent);
//    }


//    protected View showEmptyView(int res, String tips) {
//        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
//        TextView tv_empty = (TextView) emptyView.findViewById(R.id.emptyView);
//        tv_empty.setText(tips);
//        ImageView iv_empty = (ImageView) emptyView.findViewById(R.id.iv_empty);
//        iv_empty.setImageResource(res);
//        return emptyView;
//    }


    protected void showLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
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
}
