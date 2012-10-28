package com.quicklookbusy.twending;

import java.util.ArrayList;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class TwendingProvider extends AppWidgetProvider {

	private Context context;
	private RemoteViews remoteViews;
	private AppWidgetManager appWidgetManager;

	public static String LOG_TAG = "twending";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		log("Getting data");

		this.context = context;
		this.appWidgetManager = appWidgetManager;

		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				TwendingProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		for (int widgetId : allWidgetIds) {
			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget);

			TwendsRequest req = new TwendsRequest(new TwendsCallback(widgetId));
			req.start();
			log("Kicked off request");

			Intent clickIntent = new Intent(context, TwendingProvider.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);
		}
	}

	private void log(String message) {
		Log.d(LOG_TAG, message);
	}

	public class TwendsCallback {
		int widgetId;

		public TwendsCallback(int widgetId) {
			this.widgetId = widgetId;
		}

		public void doOnResult(ArrayList<String> topics) {
			// Update ListView
			log("Got requet data");
			Intent svcIntent = new Intent(context, TwendingViewService.class);
			svcIntent.putStringArrayListExtra("topics", topics);
			remoteViews.setRemoteAdapter(R.id.topics, svcIntent);

			log("Updating widget");
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			log("Updated widget");
		}
	}
}