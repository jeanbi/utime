package com.ubbcn.android.utime.view;

import com.ubbcn.android.utime.R;
import com.ubbcn.android.utime.Singleton;
import com.ubbcn.android.utime.utils.DbProvider;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class SplashView extends Activity {
	private Context ctx;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        
        ctx = this;
        MobclickAgent.onError(ctx);
        MobclickAgent.updateOnlineConfig(ctx);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					DbProvider db = new DbProvider(ctx);
					Singleton.instance.setDb(db);
					Thread.sleep(1000);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
    }
    
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			startActivity(new Intent().setClass(ctx, GetStarted.class));
			finish();
		}
    	
    };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
    
    
}