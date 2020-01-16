package com.wxzd.gfzdj.views.activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.gfzdj.R;
import com.wxzd.gfzdj.global.base.BaseActivity;

public class LoginActivity extends BaseActivity {
    /**
     * 请输入账号
     */
    private EditText mEtName;
    /**
     * 请输入密码
     */
    private EditText mEtPwd;
    /**
     * 登录
     */
    private TextView mTvLogin;
    private String mphone;


    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initAppBar("登录");
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mTvLogin = (TextView) findViewById(R.id.tv_login);
//        final LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
//        lottieAnimationView.setAnimation("13682-heart.json");
//        lottieAnimationView.playAnimation();
//        lottieAnimationView.setRepeatCount(100000);
//        LottieCompositionFactory.fromUrl(this,"url")
//                .addListener(new LottieListener<LottieComposition>() {
//            @Override
//            public void onResult(LottieComposition result) {
//                lottieAnimationView.setComposition(result);
//            }
//        }).addFailureListener(new LottieListener<Throwable>() {
//            @Override
//            public void onResult(Throwable result) {
//
//            }
//        });


    }

    @Override
    public void initListener() {
        mTvLogin.setOnClickListener(this);
    }


    private void login() {
        mphone = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(mphone)) {
            ;
            return;
        }
        if (!RegexUtils.isMobileExact(mphone)) {

            return;
        }
        String pwd = mEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {

            return;
        }
//        if (!RegexUtils.isMatch(Constants.REGEX_PASSWORD, pwd)) {
//            ToastUtil.showToast(getString(R.string.tips_for_pwd));
//            return;
//        }
//        String encryptPwd = AesUtil.encrypt(pwd, Constants.PASSWORD, Constants.IV);
//        try {
//            if (httpBody == null) {
//                httpBody = new HttpBody();
//            }
//            RequestBody body = httpBody.addParams("mobile", phone)
//                    .addParams("password", encryptPwd)
//                    .addParams("appVersion", AppUtils.getAppVersionName())
//                    .addParams("appDevice", "02")
//                    .addParams("phoneDevice", Build.MODEL)
//                    .addParams("phoneVersion", Build.VERSION.RELEASE)
//                    .addParams("deviceNo", SPUtils.getInstance().getString(Constants.KEY_UUID))
//                    .addParams("ip", NetworkUtils.getIPAddress(true))
//                    .addParams("sysId", SPUtils.getInstance().getString(Constants.KEY_SYSID))
//                    .toBody();
//            LogUtils.e(TAG, "phoneDevice: " + Build.MODEL + "  phoneVersion: " + Build.VERSION.RELEASE);
//            showLoadingDialog();
//            presenter.login(body);
//        } catch (JSONException e) {
//            LogUtils.e(TAG, e.getMessage(), e);
//            ToastUtil.showToast("登录失败");
//        }
    }


    @Override
    public void onWidgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
//                login();
                ToastUtils.showShort("登录");
                break;
        }
    }


    @Override
    public void onBackPressedSupport() {
        exit();
    }

    //退出时的时间
    private long mExitTime;

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showShort("再按一次退出" + getString(R.string.app_name));
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            ActivityUtils.finishAllActivities();
            System.exit(0);
        }
    }
}
