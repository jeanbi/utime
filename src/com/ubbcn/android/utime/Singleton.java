package com.ubbcn.android.utime;

import com.ubbcn.android.utime.utils.DbProvider;

public class Singleton {
	public final static Singleton instance = new Singleton();
	public Singleton(){}
	
	private DbProvider db;
	public void setDb(DbProvider db){
		this.db = db;
	}
	public DbProvider getDb(){
		return db;
	}
}
