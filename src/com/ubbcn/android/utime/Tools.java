package com.ubbcn.android.utime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.util.Log;

public class Tools {
	private static final String URL_GET = "GET", URL_POST = "POST";
	private static final String SERVER_URL = "http://localhost/code.asp";
	
	private HttpURLConnection urlConnection;
	public void ReadCommand(String code){
		try{
			URL httpUrl = new URL(SERVER_URL + code);
			urlConnection = (HttpURLConnection)httpUrl.openConnection();
			
			urlConnection.setRequestMethod(URL_GET);
			urlConnection.setRequestProperty("Content-Type","text/html; charset=UTF-8");
			
			urlConnection.connect();
			String command = ReadCommand(urlConnection.getInputStream());
			Log.d("command", command);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private String ReadCommand(InputStream stream){
		String command = "";
		try{
			int n = stream.available();
			byte[] bts = new byte[n];
			n = stream.read(bts);
			command = new String(bts);
			
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return command;
	}
	
	private void SendCommand(String code){
		try{
			URL httpUrl = new URL(SERVER_URL + code);
			urlConnection = (HttpURLConnection)httpUrl.openConnection();
			
			urlConnection.setRequestMethod(URL_POST);
			urlConnection.setRequestProperty("Content-Type","text/html; charset=UTF-8");
			
			urlConnection.connect();
			String command = ReadCommand(urlConnection.getInputStream());
			Log.d("command", command);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
