package com.quicklookbusy.twending;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class TwendingProvider extends AppWidgetProvider {

	private PendingIntent service = null;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIDs) {
		final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		final Calendar time = Calendar.getInstance();
		time.set(Calendar.MINUTE,  0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		
		final Intent intent = new Intent(context, TwendingService.class);
		intent.putExtra("appWidgetIDs", appWidgetIDs);
		
		if(service == null) {
			service = PendingIntent.getService(context, 0,  intent,  PendingIntent.FLAG_CANCEL_CURRENT);
		}
		
		int minutes = 1;
		am.setRepeating(AlarmManager.RTC,  time.getTime().getTime(),  minutes*60*1000,  service);
	}
}