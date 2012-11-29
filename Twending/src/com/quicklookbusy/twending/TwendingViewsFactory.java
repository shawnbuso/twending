/*
 * TwendingViewsFactory.java
 * 
 * Defines the class used as the RemoteViewsFactory for the Twending widget
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

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * Used to create the front end for the widget
 * 
 * @author Shawn Busolits
 * @version 1.0
 */
public class TwendingViewsFactory implements RemoteViewsFactory {

	/** Context of the app */
	Context context;

	/** Used to store the list of trending topics */
	SharedPreferences prefs = null;

	/**
	 * Instantiates the Factory with a handle to the shared preferences
	 * 
	 * @param context
	 *            Context of the app
	 * @param intent
	 *            Intent used to call the factory
	 */
	public TwendingViewsFactory(Context context, Intent intent) {
		TwendingService.log("In factory constructor");
		this.context = context;
		prefs = context.getSharedPreferences("TWENDING", 0);
	}

	@Override
	/**
	 * Returns the number of items in the ListView
	 * @return The number of items in the ListView
	 */
	public int getCount() {
		return prefs.getInt("numTopics", 0);
	}

	@Override
	/**
	 * Returns the position of the current item
	 * @return The position of the current item
	 */
	public long getItemId(int position) {
		return position;
	}

	@Override
	/**
	 * Not used
	 */
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	/**
	 * Returns the view for the specified position in the ListView
	 * @param position The position in the ListView for which to return a view
	 * @return The remote view for the specified position
	 */
	public RemoteViews getViewAt(int position) {

		ArrayList<String> topics = new ArrayList<String>();
		for (int i = 0; i < getCount(); i++) {
			topics.add(prefs.getString("topic" + i, ""));
		}

		RemoteViews row = new RemoteViews(context.getPackageName(),
				R.layout.row);

		row.setTextViewText(android.R.id.text1, topics.get(position));
		row.setTextColor(android.R.id.text1, Color.BLACK);

		if (!topics.get(position).equals("Error: could not contact twitter")) {
			// TwendingService.log("Current trend: " + topics.get(position)
			// + ", and url = " + "https://twitter.com/search?q="
			// + topics.get(position).replace("#", "%23"));
			Intent intent = new Intent();
			if (!topics.get(position).contains("Last updated")) {
				intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("https://twitter.com/search?q="
								+ topics.get(position).replace("#", "%23")));
			}
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			row.setOnClickPendingIntent(android.R.id.text1, pendingIntent);
		}

		return (row);
	}

	@Override
	/**
	 * Returns the type count of the view
	 * @return The type count of the view
	 */
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	/**
	 * Returns true if the view has stable IDs, false otherwise
	 * @return True if the view has stable IDs, false otherwise
	 */
	public boolean hasStableIds() {
		return true;
	}

	@Override
	/**
	 * Not used
	 */
	public void onCreate() {
	}

	@Override
	/**
	 * Not used
	 */
	public void onDataSetChanged() {
	}

	@Override
	/**
	 * Not used
	 */
	public void onDestroy() {
	}
}
