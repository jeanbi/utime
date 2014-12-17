package com.ubbcn.android.utime.Widgets;

import com.ubbcn.android.utime.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class RecordProgress extends ImageView {
	private Context ctx;
	private ImageView imageView;
	private int delay = 1000;
	public RecordProgress(Context ctx){
		super(ctx);
		init(ctx);
	}
	
	public RecordProgress(Context ctx, AttributeSet attrs){
		super(ctx, attrs);
		init(ctx);
	}
	
	public RecordProgress(Context ctx, AttributeSet attrs, int defStyle){
		super(ctx, attrs, defStyle);
		init(ctx);
	}
	
	private void init(Context ctx){
		this.ctx = ctx;
		handler.postDelayed(runnable, delay);
		imageView = this;
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.recording_anim);
			LinearInterpolator interpolator = new LinearInterpolator();
			anim.setInterpolator(interpolator);
			imageView.startAnimation(anim);
			handler.postDelayed(this, delay);
		}
	};
	
	private Handler handler = new Handler();
}
