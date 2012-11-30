/*
 * TwendingProvider.java
 * 
 * Defines the class used for the Twending widget provider
 * 
 * Copyright 2012 Shawn Busolits
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */

package com.quicklookbusy.twending;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
		SharedPreferences settings = context
				.getSharedPreferences("TWENDING", 0);
		if (settings.getBoolean("configured", false)) {
			TwendingService.log("Updating");

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

			TwendingService.log("Registering alarm");
			int minutes = settings.getInt("frequency", 60);
			am.setRepeating(AlarmManager.RTC, time.getTime().getTime(),
					minutes * 60 * 1000, service);
			TwendingService.log("Registered");
		}
	}

	@Override
	/**
	 * Un-registers the widget with the AlarmManager
	 * @param context Context of the app
	 */
	public void onDisabled(Context context) {
		TwendingService.log("Unregistering alarm");
		final AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (service != null) {
			am.cancel(service);
		}
		TwendingService.log("Done");
	}
}