package com.ubbcn.android.utime.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubbcn.android.utime.GlobalEnv;
import com.ubbcn.android.utime.model.ActionModel;

public class DbProvider {
	
	private DbUtililty dbUtils;
	public DbProvider(Context ctx){
		dbUtils = new DbUtililty(ctx, GlobalEnv.DB_NAME);
	}
	
	public void save(ActionModel am){
		ContentValues values = new ContentValues();
		values.put(GlobalEnv.ACT_NAME, am.getName());
		values.put(GlobalEnv.ACT_ELAPSED, am.getElapsed());
		values.put(GlobalEnv.ACT_DATE, am.getDate());
		SQLiteDatabase db = dbUtils.getWritableDatabase();
		db.insert(GlobalEnv.ACTION_TABLE, null, values);
		db.close();
	}
	
	public List<ActionModel> ActionTimeList(long time){
		List<ActionModel> list = null;
		SQLiteDatabase db = dbUtils.getWritableDatabase();
		String where = GlobalEnv.ACT_DATE + "<=" + time;
		Cursor c = db.query(GlobalEnv.ACTION_TABLE, new String[]{ GlobalEnv.ACT_ID, GlobalEnv.ACT_NAME }, where, null, GlobalEnv.ACT_NAME, null, GlobalEnv.ACT_NAME + " ASC");
		
		if(c != null){
			list = new ArrayList<ActionModel>();
			c.moveToFirst();
			int num = c.getCount();
			for(int i = 0; i < num; i++){
				ActionModel am = new ActionModel();
				am.setId(c.getLong(0));
				am.setName(c.getString(1));
				list.add(am);
				am = null;
				c.moveToNext();
			}
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<ActionModel> QueryForReport(long start, long end){
		List<ActionModel> list = null;
		String where = GlobalEnv.ACT_DATE + ">=" + start + " and " + GlobalEnv.ACT_DATE + "<=" + end;
		SQLiteDatabase db = dbUtils.getWritableDatabase();
		Cursor c = db.query(GlobalEnv.ACTION_TABLE, new String[]{ GlobalEnv.ACT_ID, GlobalEnv.ACT_NAME, "SUM(" + GlobalEnv.ACT_ELAPSED + ")" }, where, null, GlobalEnv.ACT_NAME, null, GlobalEnv.ACT_NAME + " ASC");
		if(c != null){
			list = new ArrayList<ActionModel>();
			c.moveToFirst();
			int num = c.getCount();
			for(int i = 0; i < num; i++){
				ActionModel am = new ActionModel();
				am.setId(c.getLong(0));
				am.setName(c.getString(1));
				am.setElapsed(c.getLong(2));
				list.add(am);
				am = null;
				c.moveToNext();
			}
		}
		c.close();
		db.close();
		return list;
	}
}
