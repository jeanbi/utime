package com.ubbcn.android.utime.utils;

import com.ubbcn.android.utime.GlobalEnv;
import com.ubbcn.android.utime.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUtililty extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private Context context;
	public DbUtililty(Context ctx, String database, CursorFactory factory, int version){
		super(ctx, database, factory, version);
		context = ctx;
	}
	
	public DbUtililty(Context ctx, String database){
		super(ctx, database, null, VERSION);
		context = ctx;
	}
	
	public DbUtililty(Context ctx, String database, int version){
		super(ctx, database, null, version);
		context = ctx;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String createActionSql = "CREATE TABLE IF NOT EXISTS " + GlobalEnv.ACTION_TABLE + "(" + GlobalEnv.ACT_ID + " integer primary key autoincrement, " + GlobalEnv.ACT_NAME + " text, "
				+ GlobalEnv.ACT_ELAPSED + " integer, " + GlobalEnv.ACT_DATE + " integer);";
		db.execSQL(createActionSql);
		
		int[] datas = new int[]{ R.string.init_drive, R.string.init_eat, R.string.init_read, R.string.init_shower, R.string.init_sleep };
		
		ContentValues values;
		for(int i = 0; i< datas.length; i ++){
			values = new ContentValues();
			values.put(GlobalEnv.ACT_NAME, context.getString(datas[i]));
			values.put(GlobalEnv.ACT_ELAPSED, 0);
			values.put(GlobalEnv.ACT_DATE, 0);
			db.insert(GlobalEnv.ACTION_TABLE, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
