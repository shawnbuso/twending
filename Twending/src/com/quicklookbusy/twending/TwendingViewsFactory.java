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

public class TwendingViewsFactory implements RemoteViewsFactory {

	Context context;
	//ArrayList<String> topics;

	public TwendingViewsFactory(Context context, Intent intent) {
		TwendingService.log("In factory constructor");
		this.context = context;
		//this.topics = intent.getStringArrayListExtra("topics");
	}

	@Override
	public int getCount() {
		return 20;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		//TwendingService.log("Getting view at position" + position);
		
		ArrayList<String> topics = new ArrayList<String>();
		SharedPreferences prefs = context.getSharedPreferences("TWENDING", 0);
		for(int i=0; i<20; i++) {
			topics.add(prefs.getString("topic" + i, ""));
		}
		
		
		if(position == 0) {
			TwendingService.log("Position 0 = " + topics.get(position));
		}
		
		RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.row);

		row.setTextViewText(android.R.id.text1, topics.get(position));
		row.setTextColor(android.R.id.text1, Color.BLACK);

		Intent intent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://twitter.com/search?q=" + topics.get(position).replace("#", "%23")));
		PendingIntent pendingIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		row.setOnClickPendingIntent(android.R.id.text1, pendingIntent);

		//TwendingService.log("Returning view at position" + position);
		return (row);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}
}
