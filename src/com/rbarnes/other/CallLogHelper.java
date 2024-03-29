/*
 * project	WhoCalledMe
 * 
 * package	com.rbarnes.other
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 24, 2013
 */
package com.rbarnes.other;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class CallLogHelper {

	public static Cursor getAllCallLogs(ContentResolver cr) {
		// reading all data in descending order according to DATE
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		Uri callUri = Uri.parse("content://call_log/calls");
		Cursor curCallLogs = cr.query(callUri, null, null, null, strOrder);

		return curCallLogs;
	}


}
