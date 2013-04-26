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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.rbarnes.other.CallLogHelper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainListActivity extends ListActivity {

	private ArrayList<String> contactNames;
	private ArrayList<String> contactNumbers;
	private ArrayList<String> contactDate;
	private ArrayList<String> contactType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    

		contactNames = new ArrayList<String>();
		contactNumbers = new ArrayList<String>();
		contactDate = new ArrayList<String>();
		contactType = new ArrayList<String>();
		

		Cursor curLog = CallLogHelper.getAllCallLogs(getContentResolver());

		setCallLogs(curLog);

		setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1, R.id.nameText, contactNames));
		
		
	}

	private class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int resource, int textViewResourceId,
				ArrayList<String> conNames) {
			super(context, resource, textViewResourceId, conNames);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = setList(position, parent);
			return row;
		}

		private View setList(int position, ViewGroup parent) {
			LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row = inf.inflate(R.layout.liststyle, parent, false);

			TextView textViewName = (TextView) row.findViewById(R.id.nameText);
			TextView textViewNumber = (TextView) row.findViewById(R.id.numText);
			TextView textViewDate = (TextView) row.findViewById(R.id.dateText);
			TextView textViewType = (TextView) row.findViewById(R.id.typeText);

			textViewName.setText(contactNames.get(position));
			textViewNumber.setText(contactNumbers.get(position));
			textViewDate.setText(contactDate.get(position));
			textViewType.setText("( " + contactType.get(position) + " )");

			return row;
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void setCallLogs(Cursor curLog) {
		while (curLog.moveToNext()) {
			

			String callName = curLog
					.getString(curLog
							.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
			if (callName == null) {
				contactNames.add("Unknown");
				String callDate = curLog.getString(curLog
						.getColumnIndex(android.provider.CallLog.Calls.DATE));
				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm");
				String dateString = formatter.format(new Date(Long
						.parseLong(callDate)));
				contactDate.add(dateString);

				String callType = curLog.getString(curLog
						.getColumnIndex(android.provider.CallLog.Calls.TYPE));
				if (callType.equals("1")) {
					contactType.add("Incoming");
				} else
					contactType.add("Outgoing");
				String callNumber = curLog.getString(curLog
						.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
				contactNumbers.add(callNumber);
			}
			
			

			


		}
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater imf = getMenuInflater();
		imf.inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.item_call_log);
		item.setVisible(false);
		this.invalidateOptionsMenu();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case android.R.id.home:
        
			Intent intent = new Intent(this, MainResultActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
