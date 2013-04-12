/*
 * project	WhoCalledMe
 * 
 * package	com.mdf3w1.rbanes.whocalledme
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 9, 2013
 */
package com.mdf3w1.rbanes.whocalledme;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rbarnes.other.SearchService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;



public class MainActivity extends Activity {
	

	private GoogleMap map;
	static LatLng _resultGPS = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get phone number dialed
		Intent intent = getIntent();
		String dialedNumber = intent.getDataString();
		Log.i("PHONE NUMBER",dialedNumber);
		
		dialedNumber = dialedNumber.replace("%20", "");
		Log.i("PHONE NUMBER",dialedNumber);
		
		dialedNumber = dialedNumber.replaceAll("[^0-9]+","");
		Log.i("PHONE NUMBER",dialedNumber);
		
		
		
		lookupNumber(dialedNumber);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	//look for information from result to determine location and business
	@SuppressLint("HandlerLeak")
	private Handler resultHandler = new Handler(){
		  public void handleMessage(Message message){
			  Object path = message.obj;
				if (message.arg1 == RESULT_OK && path != null){
					String result = (String) message.obj.toString();
					
					
					JSONObject json = null;
					JSONObject resultInfo = null;
					try {
						json = new JSONObject(result);
						Log.i("value",String.valueOf(json.getJSONObject("result").getString("message").length()));
						if(json.getJSONObject("result").getString("message").length() != 1){
							Log.e("result",json.getJSONObject("result").getString("message"));
							
						}else{
							resultInfo = json.getJSONArray("listings").getJSONObject(0);
							Log.i("info",resultInfo.getJSONObject("geodata").getString("longitude"));
							Log.i("info",resultInfo.getJSONObject("address").getString("state"));
							Log.i("info",resultInfo.getString("displayname"));
						}
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
		  
		  }
		};
		
		
	private void lookupNumber(String phoneNumber){
		//search Internet for business information using the white pages. also setup messenger to receive result
		Intent searchIntent = new Intent(getApplicationContext(), SearchService.class);
		Messenger messenger = new Messenger(resultHandler);
		searchIntent.putExtra("messenger", messenger);
		searchIntent.putExtra("phone_number", phoneNumber);
		
		startService(searchIntent);
	}
	
	
	

}

