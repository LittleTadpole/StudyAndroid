package com.wxzd.gfzdj.global.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.ProgressBar;

import com.example.gfzdj.R;
import com.wxzd.gfzdj.global.views.drawable.Circle;


public class LoadingDialog extends Dialog {

	private Context mContext;
    private ProgressBar progressBar;
	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialogTheme);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater .inflate(R.layout.loading_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
		Circle circle = new Circle();
		circle.setColor(mContext.getResources().getColor(R.color.white));
		progressBar.setIndeterminateDrawable(circle);
		setContentView(view);
		Window window = getWindow();
		WindowManager windowManager = window.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = display.getWidth() * 2 / 5; // 设置dialog宽度为屏幕的2/5
		lp.height = display.getWidth() * 2 / 5; // 设置dialog宽度为屏幕的2/5
		window.setAttributes(lp);
	}

}