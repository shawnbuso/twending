/*
 * TwendingProvider.java
 * 
 * Defines the class used for the Twending widget provider
 * 
 * Copyright (C) Shawn Busolits 2012, All Rights Reserved
 */

package com.quicklookbusy.twending;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * Implements the provider for the twending widget
 * 
 * @author Shawn Busolits
 * @version 1.0
 */
public class TwendingProvider extends AppWidgetProvider {

	/** Used to register with the AlarmManager */
	private PendingIntent service = null;

	@Override
	/**
	 * Registers the widget with the AlarmManager for updates
	 * @param context Context of the app
	 * @param appWidgetManager Used to update the widget
	 * @param appWidgetIds IDs for widgets that need to be updated
	 */
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIDs) {
		final AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		final Calendar time = Calendar.getInstance();
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);

		final Intent intent = new Intent(context, TwendingService.class);
		intent.putExtra("appWidgetIDs", appWidgetIDs);

		if (service == null) {
			service = PendingIntent.getService(context, 0, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
		}

		int minutes = 30;
		am.setRepeating(AlarmManager.RTC, time.getTime().getTime(),
				minutes * 60 * 1000, service);
	}

	@Override
	/**
	 * Un-registers the widget with the AlarmManager
	 * @param context Context of the app
	 */
	public void onDisabled(Context context) {
		final AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (service != null) {
			am.cancel(service);
		}
	}
}