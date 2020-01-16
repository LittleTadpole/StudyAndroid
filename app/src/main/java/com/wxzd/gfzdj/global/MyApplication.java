package com.wxzd.gfzdj.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.example.gfzdj.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2017/12/27.
 * 全局application ,app入口, 进行一些初始化工作
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static AppComponent appComponent;

    /**
     * 获取AppComponent实例
     *
     * @return AppComponent实例
     */
    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            Log.d("----", "getAppComponent: 为空");
        }
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        initPlatform();
        initData();
        // 以下用来捕获程序崩溃异常, 因为google内置机制,app崩溃后会自动重启.导致开发过程中看不到崩溃日志
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程
            registerActivityListener();//activity管理类
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initData() {
//        String sysID = AesUtil.encrypt(Constant.SYSID, Constant.PASSWORD, Constant.IV);
//        SPUtils.getInstance().put(Constant.KEY_SYSID, sysID);

    }


    /**
     * 第三方sdk初始化
     */
    private void initPlatform() {
        Utils.init(this);   //初始化utils工具
        LogUtils.getConfig().setLogSwitch(AppUtils.isAppDebug());//控制日志开关


    }

    //初始化Component
    private void initComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

    }

    List<Activity> activities = new ArrayList<>();
    public Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //下面为调试用的代码，发布时可注释
            Writer info = new StringWriter();
            PrintWriter printWriter = new PrintWriter(info);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = info.toString();
            Log.i("sss", result);
            for (int i = 0; i < activities.size(); i++) {
                Log.i("sss", activities.get(i).getLocalClassName());
                if (activities.get(i) != null)
                    activities.get(i).finish();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };


    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    activities.add(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null == activities && activities.isEmpty()) {
                        return;
                    }
                    if (activities.contains(activity)) {
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        activities.remove(activity);
                    }
                }
            });
        }
    }


}
