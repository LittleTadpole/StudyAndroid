package com.wxzd.gfzdj.global.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.gfzdj.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.wxzd.gfzdj.views.activities.H5Activity;

/**
 * activity,fragment常用的公共方法
 */
public class CommonUtils {
    private static long lastClick = 0;

    public static boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 1000) {
            lastClick = now;
            return false;
        }
        return true;
    }

    protected static void startH5Activity(Activity context, String title, String url) {
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);
    }

    /**
     * Show message
     *
     * @param activity Activity
     * @param msg message
     */
    public static void showSnackMessage(Activity activity, String msg) {

        //去掉虚拟按键
        activity.getWindow().getDecorView().setSystemUiVisibility(
                //隐藏虚拟按键栏 | 防止点击屏幕时,隐藏虚拟按键栏又弹了出来
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        final Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(activity, R.color.white));
        snackbar.setAction("知道了", v -> {
            snackbar.dismiss();
            //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,很难看
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }).show();
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }
}
