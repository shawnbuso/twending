/*
 * TwendingService.java
 * 
 * Defines the class called when the Twending widget needs updating
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Service called to update the widget
 * 
 * @author Shawn Busolits
 * @version 1.0
 */
public class TwendingService extends Service {

	/** Used to get a handle to the widget */
	private RemoteViews remoteViews = null;

	/** Used to store trending topics */
	private SharedPreferences prefs = null;

	/** Used to edit stored trending topics */
	private Editor settingsEditor = null;

	/** Our handle to the widget to be updated */
	private int[] appWidgetIDs;

	/** Used for logging */
	public static String LOG_TAG = "twending";

	@Override
	/**
	 * Gets shared preferences handles when the service is created
	 */
	public void onCreate() {
		super.onCreate();
		prefs = getSharedPreferences("TWENDING", 0);
		settingsEditor = prefs.edit();
	}

	@Override
	/**
	 * Called when the service is started.
	 * @param intent Calling intent
	 * @param flags Flags passed when called
	 * @param startId Start ID
	 * @return The status of starting the service
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		appWidgetIDs = intent.getIntArrayExtra("appWidgetIDs");

		buildUpdate();

		return START_REDELIVER_INTENT;
	}

	/**
	 * Gets a handle to the remote views and kicks off the twitter request
	 */
	public void buildUpdate() {
		remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
		TwendsRequest req = new TwendsRequest(new TwendsCallback());
		req.start();
		log("Kicked off request");
	}

	/**
	 * Logs messages
	 * 
	 * @param message
	 *            Message to be logged
	 */
	public static void log(String message) {
		Log.d(LOG_TAG, message);
	}

	@Override
	/**
	 * Not used
	 */
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Callback for the twitter trends request
	 * 
	 * @author Shawn Busolits
	 * @version 1.0
	 */
	public class TwendsCallback {

		/**
		 * Called when the twitter request completes
		 * 
		 * @param topics
		 *            List of trending topics
		 */
		public void doOnResult(ArrayList<String> topics) {
			log("Got requet data");

			if (topics.size() == 0) { // Update failed, grab old topics
				log("No new topics");
				for (int i = 0; i < prefs.getInt("numTopics", 0); i++) {
					topics.add(prefs.getString("topic" + i, ""));
				}

				// Remove 'update failed' log at the end of the list (if there
				// is one)
				if (topics.get(topics.size() - 1).contains("update failed")) {
					topics.remove(topics.size() - 1);
				}

				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				topics.add(dateFormat.format(cal.getTime()) + " update failed.");
			}

			for (int i = 0; i < topics.size(); i++) {
				settingsEditor.putString("topic" + i, topics.get(i));
			}
			settingsEditor.putInt("numTopics", topics.size());
			settingsEditor.commit();

			Intent svcIntent = new Intent(getApplicationContext(),
					TwendingViewService.class);
			remoteViews.setRemoteAdapter(R.id.topics, svcIntent);

			ComponentName thisWidget = new ComponentName(TwendingService.this,
					TwendingProvider.class);
			AppWidgetManager manager = AppWidgetManager
					.getInstance(TwendingService.this);
			manager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.topics);
			manager.updateAppWidget(thisWidget, remoteViews);
			log("Updated widget");
		}
	}
}
