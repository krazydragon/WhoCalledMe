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


import com.rbarnes.other.SearchService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;



public class MainActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get phone number dialed
		Intent intent = getIntent();
		String phoneNumber = intent.getDataString();
		
		phoneNumber = phoneNumber.replaceAll("[^0-9]+","");
		
		Log.i("PHONE NUMBER",phoneNumber);
		
		Intent searchIntent = new Intent(getApplicationContext(), SearchService.class);
		searchIntent.putExtra("phone_number", phoneNumber);
		
		startService(searchIntent);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	

}

