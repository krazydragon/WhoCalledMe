/*
 * project	WhoCalledMe
 * 
 * package	com.rbarnes.other
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 10, 2013
 */
package com.rbarnes.other;

import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SearchService extends IntentService{
	
	private static int _resultStatus = Activity.RESULT_CANCELED;;
	String _result = null;
	String _phoneNumber = null;

	public SearchService() {
		super("SearchService");


		
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
        if (extras != null) {
        	// Searched Item is received
        	_phoneNumber = (String) extras.get("phone_number");
        	String url = "http://api.whitepages.com/reverse_phone/1.0/?phone="+ _phoneNumber +";api_key=6c3c8559c184a9d6025a33d81219174c;outputtype=JSON";
    		URL searchURL = null;

    		try{
    			searchURL = new URL(url);
    			_result = WebInterface.getUrlStringResponse(searchURL);
    			if(_result.length() > 0){
    				
    				Log.i("RESULT", _result);
    				_resultStatus = Activity.RESULT_OK;
    			}
    		} catch (MalformedURLException e){
    			Log.e("BAD URL", "MALFORMED URL");
    			searchURL = null;
    		}
           
        } 
		
	}

}
