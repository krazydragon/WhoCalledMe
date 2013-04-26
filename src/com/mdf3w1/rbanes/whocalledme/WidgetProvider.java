/*
 * project	WhoCalledMe
 * 
 * package	com.rbarnes.other
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 25, 2013
 */

   
package com.mdf3w1.rbanes.whocalledme;

import com.mdf3w1.rbanes.whocalledme.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		Intent intent = new Intent(context, MainListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent(context, MainResultActivity.class);
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
}
