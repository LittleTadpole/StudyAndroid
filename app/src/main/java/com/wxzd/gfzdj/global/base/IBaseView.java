package com.wxzd.gfzdj.global.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/06/27
 *     desc  :
 * </pre>
 */
interface IBaseView extends View.OnClickListener {

    /**
     * 初始化数据
     *
     * @param bundle 传递过来的 bundle
     */
    void initData(@Nullable final Bundle bundle);

    /**
     * 绑定布局
     *
     * @return 布局 Id
     */
    int bindLayoutID();

    /**
     * 初始化 view
     */
    void initView(final Bundle savedInstanceState);

    /**
     * 初始化点击事件
     */
    void initListener();

    /**
     * 业务操作
     */
    void doBusiness();

    /**
     * 视图点击事件
     *
     * @param view 视图
     */
    void onWidgetClick(final View view);
}
