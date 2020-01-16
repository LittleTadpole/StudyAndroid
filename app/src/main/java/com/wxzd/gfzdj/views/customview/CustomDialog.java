package com.wxzd.gfzdj.views.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.blankj.utilcode.util.LogUtils;
import com.example.gfzdj.R;

/**
 * Created by admin on 2018/1/4.
 * description:  自定义的弹窗, 使用时候动态设置布局, 实现点击事件
 */

public class CustomDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CustomDialog";
  private Context context;


    public CustomDialog(@NonNull Context context) {
        super(context, R.style.dialog_custom);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }


    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }



    private OnCustomItemClickListener listener;

    public interface OnCustomItemClickListener {
        void OnCustomItemClick(CustomDialog dialog, View view);
    }

    public void setOnCustomItemClickListener(OnCustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.OnCustomItemClick(this, view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
//        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置为居中
//        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth(); // 设置dialog宽度为屏幕宽度
        LogUtils.e(TAG,"屏幕宽: "+display.getWidth());
        window.setAttributes(lp);
//        setCanceledOnTouchOutside(false);// 点击Dialog外部不消失
    }
}
