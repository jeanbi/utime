package com.ubbcn.android.utime.model;

import android.util.Log;

import com.ubbcn.android.utime.utils.CounterListener;

public class ActionModel implements CounterListener {
	private long _id, _elapsed, _date;
	private String _name;
	
	public void setId(long _id){
		this._id = _id;
	}
	public long getId(){
		return _id;
	}
	
	public void setName(String _name){
		this._name = _name;
	}
	public String getName(){
		return this._name;
	}
	
	public void setElapsed(long _elapsed){
		this._elapsed = _elapsed;
	}
	public long getElapsed(){
		return _elapsed;
	}
	
	public void setDate(long _date){
		this._date = _date;
	}
	public long getDate(){
		return _date;
	} 
	
	private boolean counting;
	public boolean isCounting(){
		return counting;
	}
	
	public void setCounting(boolean counting){
		this.counting = counting;
	}
	
	@Override
	public void start(ActionModel actionModel) {
		// TODO Auto-generated method stub
		Log.d("aaaaaa", "start");
	}
	@Override
	public void stop(ActionModel actionModel) {
		// TODO Auto-generated method stub
		Log.d("aaaaaa", "stop");
	}
	
	
}
