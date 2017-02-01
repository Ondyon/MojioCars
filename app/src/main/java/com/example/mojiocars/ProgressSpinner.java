package com.example.mojiocars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class ProgressSpinner extends FrameLayout {
	private Context context;
	private View base;
	private ImageView button;
	private ImageView progress;
	private Animation anima;

	public ProgressSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}


	public ProgressSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ProgressSpinner(Context context) {
		super(context);
		init(context);
	}

	private void init(Context c) {
		context = c;

		anima = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate);

		LayoutInflater inflater = LayoutInflater.from(context);
		base = inflater.inflate(R.layout.view_progress_spinner, this);

		button = (ImageView)base.findViewById(R.id.progress_button_refresh);
		progress = (ImageView)base.findViewById(R.id.progress_button_progress);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		button.setOnClickListener(l);
	}

	public void setProgress(boolean on) {
		if(on) {
			setVisibility(View.VISIBLE);
			progress.startAnimation(anima);
		} else {
			progress.clearAnimation();
			setVisibility(View.GONE);
		}
	}


}
