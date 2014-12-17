package com.ubbcn.android.utime.view;

import java.util.Calendar;
import java.util.List;

import com.ubbcn.android.utime.GlobalEnv;
import com.ubbcn.android.utime.R;
import com.ubbcn.android.utime.Singleton;
import com.ubbcn.android.utime.model.ActionModel;
import com.ubbcn.android.utime.utils.DbProvider;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class GetStarted extends Activity {

	private Context ctx;
	private boolean iscounting = false;
	private int hour = 0, minute = 0, second = 0;
	private long counter = hour * 3600 + minute * 60 + second;
	private String action_name = "";
	private long actionId = 0, timeInMillions = 0;
	private ImageView h_hour, l_hour, h_minute, l_minute, h_second, l_second;
	private ActionAdapter actionAdapter;
	private EditText edit;
	private Button quick_btn;
	private TextView action_label;
	private GridView actionGrid;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.started_layout);
				
		h_hour = (ImageView)findViewById(R.id.h_hour);
		l_hour = (ImageView)findViewById(R.id.l_hour);
		h_minute = (ImageView)findViewById(R.id.h_minute);
		l_minute = (ImageView)findViewById(R.id.l_minute);
		h_second = (ImageView)findViewById(R.id.h_second);
		l_second = (ImageView)findViewById(R.id.l_second);
				
		edit = (EditText)findViewById(R.id.action_name);
		action_label = (TextView)findViewById(R.id.action_lable);
		
		ctx = this;
		
		db = Singleton.instance.getDb();
		actionGrid = (GridView)findViewById(R.id.action_list);
		
		ReloadActionGrid();

		top_layout = (RelativeLayout)findViewById(R.id.top_layout);
		
		quick_btn = (Button)findViewById(R.id.count_btn);
		quick_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionId = 0;
				if(iscounting){
					quick_btn.setBackgroundResource(R.drawable.start_selector);
					stop();
					iscounting = false;
					saveAction();
				}else{
					quick_btn.setBackgroundResource(R.drawable.stop_selector);
					start();
					iscounting = true;
				}
				actionAdapter.setBackgroundIndex(-1);
			}
		});
		
		setHour(hour);
		setMinute(minute);
		setSecond(second);
		
		LinearLayout adLayout = (LinearLayout)findViewById(R.id.ad);
		if(GlobalEnv.ISDEBUG)
			adLayout.setVisibility(View.GONE);
	}
	
	private void ReloadActionGrid(){
		Calendar c = Calendar.getInstance();
		actionList = db.ActionTimeList(c.getTimeInMillis());
		actionAdapter = new ActionAdapter();
		actionGrid.setAdapter(actionAdapter);
		
		actionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ActionModel am = actionList.get(arg2);
				actionId = am.getId();
				action_name = am.getName();
				if(iscounting){
					action_label.setText(null);
					stop();
					DoSave(action_name, timeInMillions);
					quick_btn.setEnabled(true);
					iscounting = false;
				}else{
					start();
					quick_btn.setEnabled(false);
					iscounting = true;
				}
				actionAdapter.setBackgroundIndex(arg2);
			}
			
		});
	}
	
	private void resetCounter(){
		hour = 0; minute = 0; second = 0;
	}
	
	private PendingIntent pending;
	private BroadcastReceiver receiver;	
	private int[] num = new int[]{ R.drawable.t_0, R.drawable.t_1, R.drawable.t_2, R.drawable.t_3, R.drawable.t_4, R.drawable.t_5, R.drawable.t_6, R.drawable.t_7, R.drawable.t_8, R.drawable.t_9 };
	private AlarmManager am;
	private RelativeLayout top_layout;
	
	private void start(){
		resetCounter();
		counter = 0;
		top_layout.setBackgroundResource(R.drawable.recording_top);
		Intent intent = new Intent(GlobalEnv.COUNTING_ACTION);
		
		receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				iscounting = true;				
				if(second >= 60){
					minute ++;
					second = 0;
				}
				
				if(minute >= 60){
					hour ++;
					minute = 0;
				}
				
				second ++;
				counter ++;
				
				setHour(hour);
				setMinute(minute);
				setSecond(second);
			}
		};
		
		registerReceiver(receiver, new IntentFilter(GlobalEnv.COUNTING_ACTION));
		am = (AlarmManager)getSystemService(ALARM_SERVICE);
		pending = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 1000, pending);
		sendBroadcast(intent);
	}
	
	private void stop(){
		top_layout.setBackgroundResource(R.drawable.start_top);
		if(actionId == 0)
			quick_btn.setBackgroundResource(R.drawable.start_selector);
		edit.setText(null);
		unregisterReceiver(receiver);
		am.cancel(pending);
		timeInMillions = counter * 1000;
		iscounting = false;
	}
	
	private void saveAction(){
		final SlidingDrawer sliding = (SlidingDrawer)findViewById(R.id.sliding);
		sliding.open();
		
		Button save, discard;
		save = (Button)findViewById(R.id.positive_btn);
		discard = (Button)findViewById(R.id.negative_btn);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				action_name = edit.getText().toString();
				DoSave(action_name, timeInMillions);
				if(exit)
					finish();
				else{
					actionAdapter.notifyDataSetChanged();
					ReloadActionGrid();
					sliding.close();
					Toast.makeText(ctx, String.format(ctx.getResources().getString(R.string.save_success), action_name), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		discard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit.setText(null);
				sliding.close();
			}
		});
	}
	
	private void DoSave(String name, long timeInMillions){
		Calendar c = Calendar.getInstance();
		ActionModel am = new ActionModel();
		am.setDate(c.getTimeInMillis());
		am.setName(name);
		am.setElapsed(timeInMillions);
		db.save(am);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(iscounting){
				showDialog(GlobalEnv.EXIT_DIALOG);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean exit = false;
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case GlobalEnv.EXIT_DIALOG:
			return new AlertDialog.Builder(ctx).setTitle(R.string.alert_title).setMessage(R.string.alert_msg)
					.setPositiveButton(R.string.exit_but_save, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							stop();
							exit = true;
							if(actionId == 0)
								saveAction();
							
						}
					})
					.setNeutralButton(R.string.exit_without_save, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							stop();
							finish();
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).show();
		case GlobalEnv.ABOUT_DIALOG:
			return new AlertDialog.Builder(ctx).setTitle(R.string.about_title).setMessage(R.string.about_text)
						.setPositiveButton(R.string.close_dialog, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						})
						.setNeutralButton(R.string.mail_author, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.setType("plain/text");
								intent.putExtra(Intent.EXTRA_EMAIL, "jeanbi@gmail.com");
								startActivity(Intent.createChooser(intent, getString(R.string.mail_chooser)));
							}
						}).show();
		default:
			return null;
		}
	}

	private DbProvider db;
	private List<ActionModel> actionList;
	class ActionAdapter extends BaseAdapter{

		int current;
		public void setBackgroundIndex(int current){
			this.current = current;
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return actionList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View pv = convertView;
			GridItem gi = null;
			if(pv == null){
				pv = getLayoutInflater().inflate(R.layout.action_grid_row, null);
				gi = new GridItem(pv);
				pv.setTag(gi);
			}else{
				gi = (GridItem)pv.getTag();
			}
			
			ActionModel am = actionList.get(position);
			gi.ItemButton().setText(am.getName());
			
			if(iscounting){
				if(current == position)
					gi.GridRow().setBackgroundResource(R.drawable.grid_enable_selector);
				else
					gi.GridRow().setBackgroundResource(R.drawable.grid_selector);
			}else{
				gi.GridRow().setBackgroundResource(R.drawable.grid_selector);
			}
			return pv;
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menus, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_report:
			Intent intent = new Intent(this, ViewReport.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.menu_about:
			showDialog(GlobalEnv.ABOUT_DIALOG);
			break;
		case R.id.menu_exit:
			if(iscounting){
				showDialog(GlobalEnv.EXIT_DIALOG);
			}else{
				finish();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class GridItem{
		private View view;
		private TextView itemBtn = null;
		public GridItem(View view) {
			// TODO Auto-generated constructor stub
			this.view = view;
		}
		
		public TextView ItemButton(){
			if(itemBtn == null)
				itemBtn = (TextView)view.findViewById(R.id.act_label);
			return itemBtn;
		}
		
		private LinearLayout layout = null;
		public LinearLayout GridRow(){
			if(layout == null)
				layout = (LinearLayout)view.findViewById(R.id.grid_id);
			return layout;
		}
	}
	
	private int[] getPoint(int num){
		int[] cons = new int[2];
		int d = (int)Math.floor(num % 10);
		cons[0] = (num - d) / 10;
		cons[1] = d;
		return cons;
	}
	
	private void setHour(int hour){
		h_hour.setImageResource(num[getPoint(hour)[0]]);
		l_hour.setImageResource(num[getPoint(hour)[1]]);
	}
	
	private void setMinute(int minute){		
		h_minute.setImageResource(num[getPoint(minute)[0]]);
		l_minute.setImageResource(num[getPoint(minute)[1]]);
	}
	
	private void setSecond(int second){		
		h_second.setImageResource(num[getPoint(second)[0]]);
		l_second.setImageResource(num[getPoint(second)[1]]);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(ctx);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(ctx);
	}
	
	
}
