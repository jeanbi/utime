package com.ubbcn.android.utime.view;

import com.ubbcn.android.utime.GlobalEnv;
import com.ubbcn.android.utime.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ViewReport extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		TabHost tabhost = getTabHost();
		tabhost.addTab(tabhost.newTabSpec(GlobalEnv.TAB_TODAY).setIndicator(getString(R.string.today)).setContent(new Intent(this, ItemList.class).putExtra(GlobalEnv.TAB_TAG, GlobalEnv.TAB_TODAY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		tabhost.addTab(tabhost.newTabSpec(GlobalEnv.TAB_YESTERDAY).setIndicator(getString(R.string.yesterday)).setContent(new Intent(this, ItemList.class).putExtra(GlobalEnv.TAB_TAG, GlobalEnv.TAB_YESTERDAY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		tabhost.addTab(tabhost.newTabSpec(GlobalEnv.TAB_UNDEFINED).setIndicator(getString(R.string.defined)).setContent(new Intent(this, ItemList.class).putExtra(GlobalEnv.TAB_TAG, GlobalEnv.TAB_UNDEFINED).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
	}

}
