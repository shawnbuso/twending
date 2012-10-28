package com.quicklookbusy.twending;

import java.util.ArrayList;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TwendingProvider extends AppWidgetProvider {

	private Context context;
	private RemoteViews remoteViews;
	private AppWidgetManager appWidgetManager;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

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

		}
	}

	public class TwendsCallback {
		int widgetId;
		
		public TwendsCallback(int widgetId) {
			this.widgetId = widgetId;
		}
		
		public void doOnResult(ArrayList<String> topics) {
			// Update ListView
			Intent svcIntent = new Intent(context, TwendingViewService.class);
			svcIntent.putStringArrayListExtra("topics", topics);
			remoteViews.setRemoteAdapter(R.id.topics, svcIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}