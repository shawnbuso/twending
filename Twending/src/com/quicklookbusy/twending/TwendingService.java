package com.quicklookbusy.twending;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class TwendingService extends Service {

	RemoteViews remoteViews = null;
	
	int[] appWidgetIDs;

	public static String LOG_TAG = "twending";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		appWidgetIDs = intent.getIntArrayExtra("appWidgetIDs");
		
		buildUpdate();

		return super.onStartCommand(intent, flags, startId);
	}

	public void buildUpdate() {
		remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
		TwendsRequest req = new TwendsRequest(new TwendsCallback());
		req.start();
		log("Kicked off request");
	}

	public static void log(String message) {
		Log.d(LOG_TAG, message);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class TwendsCallback {

		public void doOnResult(ArrayList<String> topics) throws IOException {
			// Update ListView
			log("Got requet data");
			
			SharedPreferences prefs = getSharedPreferences("TWENDING", 0);
			Editor settingsEditor = prefs.edit();
			for(int i=0; i<topics.size(); i++) {
				settingsEditor.putString("topic" + i, topics.get(i));
			}
			settingsEditor.commit();
			
			Intent svcIntent = new Intent(getApplicationContext(),
					TwendingViewService.class);
			//svcIntent.putStringArrayListExtra("topics", topics);
			remoteViews.setRemoteAdapter(R.id.topics, svcIntent);

			// Push update for this widget to the home screen
			log("Updating widget, position 0 = " + topics.get(0));
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
