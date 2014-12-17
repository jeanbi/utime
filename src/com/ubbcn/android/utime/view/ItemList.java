package com.ubbcn.android.utime.view;

import java.util.Calendar;
import java.util.List;

import com.ubbcn.android.utime.GlobalEnv;
import com.ubbcn.android.utime.R;
import com.ubbcn.android.utime.Singleton;
import com.ubbcn.android.utime.model.ActionModel;
import com.ubbcn.android.utime.utils.DbProvider;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ItemList extends ListActivity {

	private Calendar sd = Calendar.getInstance(), ed = Calendar.getInstance();
	private Context ctx;
	private ListView itemList;
	private String day_tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list_layout);
		
		ctx = this;
		String tag = getIntent().getStringExtra(GlobalEnv.TAB_TAG);
	
		View header = getLayoutInflater().inflate(R.layout.item_list_header, null);
		header_title = (TextView)header.findViewById(R.id.header_title);
		itemList = (ListView)findViewById(android.R.id.list);
		itemList.addHeaderView(header);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		
		if(GlobalEnv.TAB_TODAY.equals(tag)){
			sd.set(year, month, day, 0, 0, 0);
			ed.set(year, month, day, 23, 59, 59);
			day_tag = getString(R.string.today);
		}else if(GlobalEnv.TAB_YESTERDAY.equals(tag)){
			c.add(Calendar.DATE, -1);
			sd.set(year, month, c.get(Calendar.DATE), 0, 0, 0);
			ed.set(year, month, c.get(Calendar.DATE), 23, 59, 59);
			day_tag = getString(R.string.yesterday);
		}else{
			new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					sd.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
					ed.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
					day_tag = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
					header_title.setText(String.format(getString(R.string.report_title), day_tag));
					LoadDataByDate(sd.getTimeInMillis(), ed.getTimeInMillis());
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
		}
		
		LoadDataByDate(sd.getTimeInMillis(), ed.getTimeInMillis());
		header_title.setText(String.format(getString(R.string.report_title), day_tag));
	}
	
	private int listCount = 0;
	private List<ActionModel> list;
	private TextView header_title;
	private void LoadDataByDate(long startTimeMillions, long endTimeMillions){
		DbProvider db = Singleton.instance.getDb();
		list = db.QueryForReport(startTimeMillions, endTimeMillions);
		listCount = list.size();
		itemList.setAdapter(new ItemAdapter());
	}
	
	class ItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listCount;
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
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			// TODO Auto-generated method stub
			View pv = convertView;
			ItemView iv = null;
			if(pv == null){
				pv = getLayoutInflater().inflate(R.layout.item_list_row, null);
				iv = new ItemView(pv);
				pv.setTag(iv);
			}else{
				iv = (ItemView)pv.getTag();
			}
			
			ActionModel am = list.get(position);
			iv.ItemName().setText(am.getName());
			long current_elapsed = am.getElapsed();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(current_elapsed);
			String elapsed_time = "";
			if(c.get(Calendar.MINUTE) > 0)
				elapsed_time += c.get(Calendar.MINUTE) + "分";
			elapsed_time += c.get(Calendar.SECOND) + "秒";
			iv.ItemProgress().setText(String.format(getString(R.string.elapsed_string), elapsed_time));
			long day_times = 3600 * 24;
			int percent_dimen = (int)(current_elapsed * 4.8 / day_times);
			
			ImageView percent = iv.ItemPercent();
			ViewGroup.LayoutParams params = percent.getLayoutParams();
			params.width = percent_dimen;
			percent.setLayoutParams(params);
			return pv;
		}
		
	}
	
	class ItemView{
		private View base;
		public ItemView(View base){
			this.base = base;
		}
		
		private TextView itemName = null, itemProgress = null;
		private ImageView itemPercent = null;
		public TextView ItemName(){
			if(itemName == null)
				itemName = (TextView)base.findViewById(R.id.item_name);
			return itemName;
		}
		
		public TextView ItemProgress(){
			if(itemProgress == null)
				itemProgress = (TextView)base.findViewById(R.id.item_progress);
			return itemProgress;
		}
		
		public ImageView ItemPercent(){
			if(itemPercent == null)
				itemPercent = (ImageView)base.findViewById(R.id.item_percent);
			return itemPercent;
		}
	}

}
