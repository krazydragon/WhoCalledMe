/*
 * project	WhoCalledMe
 * 
 * package	com.mdf3w1.rbanes.whocalledme
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 24, 2013
 */
package com.mdf3w1.rbanes.whocalledme;


import java.util.List;

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
import com.rbarnes.other.WebInterface;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;



@SuppressLint("HandlerLeak")
public class MainResultActivity extends Activity implements SearchView.OnQueryTextListener {
	
	
	private GoogleMap map;
	String _phoneNumber;
	String _title;
	String _address;
	LatLng _resultGPS;
	private SearchView numberSearchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		//Get phone number dialed
		Intent intent = getIntent();
		String dialedNumber = intent.getDataString();
		Log.i("PHONE NUMBER",dialedNumber);
		
		dialedNumber = dialedNumber.replace("%20", "");
		Log.i("PHONE NUMBER",dialedNumber);
		
		dialedNumber = dialedNumber.replaceAll("[^0-9]+","");
		Log.i("PHONE NUMBER",dialedNumber);
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//detect Internet connection
		final Boolean connected = WebInterface.getConnectionStatus(this);
		
		if(!connected){
			Crouton.makeText(this, "No network found info can not be loaded!", Style.ALERT).show();
		}else{
			Crouton.makeText(this, "Looking for who called you!", Style.INFO).show();
			lookupNumber(dialedNumber);
		}
		
		_phoneNumber = dialedNumber;
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		 numberSearchView = (SearchView) searchItem.getActionView();
	        setupSearchView(searchItem);
	 
		return true;
	}
	
	@Override
	
	public boolean onOptionsItemSelected(MenuItem item){
	
		 
	        
		
		switch(item.getItemId()) {
	        case R.id.item_call_log:
	        	
	        	Intent otherIntent = new Intent(this, MainListActivity.class);
	        	startActivity(otherIntent);
	            break;
	        case android.R.id.home:
	           
	            Intent intent = new Intent(this, MainResultActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
			}    
		
	    return true;
	}
	
	private void setupSearchView(MenuItem searchItem) {
		 
        if (isAlwaysExpanded()) {
        	numberSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
 
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
 
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            Crouton.makeText(this, "Looking for who called you!", Style.INFO).show();
            numberSearchView.setSearchableInfo(info);
        }
 
        numberSearchView.setOnQueryTextListener(this);
    }
 
    public boolean onQueryTextChange(String newText) {
    	
        return false;
    }
 
    public boolean onQueryTextSubmit(String query) {
    	lookupNumber(query);
        return true;
    }
 
    public boolean onClose() {
        
        return false;
    }
    protected boolean isAlwaysExpanded() {
        return false;
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
						
						//look for error message
						if(json.getJSONObject("result").getString("message").length() != 1){
							Log.e("result",json.getJSONObject("result").getString("message"));
							displayError(json.getJSONObject("result").getString("message"));
						
						//display results on the map
						}else{
							resultInfo = json.getJSONArray("listings").getJSONObject(0);
							Log.i("info",resultInfo.getJSONObject("geodata").getString("longitude"));
							Log.i("info",resultInfo.getJSONObject("address").getString("state"));
							// don't need since searching from the white pages 
							//Log.i("info",resultInfo.getString("displayname"));
							
							final Double latitude = Double.parseDouble(resultInfo.getJSONObject("geodata").getString("latitude"));
							final Double longitude = Double.parseDouble(resultInfo.getJSONObject("geodata").getString("longitude"));
							_resultGPS = new LatLng(latitude, longitude);
							
							_address = (resultInfo.getJSONObject("address").getString("city") + resultInfo.getJSONObject("address").getString("state"));
							    
							  setupMap();   
						}
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}else{
					//No internet
					displayError("Network error");
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
	
	@SuppressWarnings("unused")
	private void setupMap(){
		Marker resultPoint = map.addMarker(new MarkerOptions()
        .position(_resultGPS)
        .title(_title)
        .snippet(_address)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(_resultGPS, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    
		 
	}
	
	private void displayError(String errorMsg){
		Crouton.makeText(this, errorMsg, Style.ALERT).show();
	}
	

}

