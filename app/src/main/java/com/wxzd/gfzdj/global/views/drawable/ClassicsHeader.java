package com.wxzd.gfzdj.global.views.drawable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.gfzdj.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;


/**
 * Description:
 * User: zhangzhongzhi
 * Date: 2018-04-17
 */
public class ClassicsHeader  extends LinearLayout implements RefreshHeader {


    private  Circle mProgressDrawable=new Circle();
    private ImageView mProgressView;//刷新动画视图


    public ClassicsHeader(Context context) {
        this(context, null);
    }
    public ClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER);

        mProgressView = new ImageView(context);
        mProgressView.setImageDrawable(mProgressDrawable);
        mProgressDrawable.setColor(context.getResources().getColor(R.color.white));
        addView(mProgressView, ConvertUtils.dp2px(32), ConvertUtils.dp2px(32));
//        addView(new Space(context), DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        setMinimumHeight(ConvertUtils.dp2px(60));
    }
    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        //开始动画
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        mProgressDrawable.stop();//停止动画
        mProgressView.setVisibility(VISIBLE);//隐藏动画

        return 0;//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mProgressDrawable.start();
                mProgressView.setVisibility(VISIBLE);//隐藏动画
                break;
            case Refreshing:
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToRefresh:
                break;
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

//        @Override
//        public void onPulling(float percent, int offset, int height, int maxDragHeight) {
//
//        }
//        @Override
//        public void onReleasing(float percent, int offset, int height, int maxDragHeight) {
//
//        }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}