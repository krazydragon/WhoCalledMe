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
		String phoneNubmer = intent.getDataString();
		Log.i("PHONE NUMBER",phoneNubmer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

