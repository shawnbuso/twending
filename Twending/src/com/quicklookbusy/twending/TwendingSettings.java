package com.quicklookbusy.twending;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class TwendingSettings extends Activity {

	SharedPreferences settings;
	Editor editor;
	EditText freqField;
	int mAppWidgetId;

	private class SubmitListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			editor.putInt("frequency",
					Integer.parseInt(freqField.getText().toString()));
			editor.commit();

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(TwendingSettings.this);
			RemoteViews views = new RemoteViews(
					TwendingSettings.this.getPackageName(), R.layout.widget);
			appWidgetManager.updateAppWidget(mAppWidgetId, views);
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);

			Intent intent = new Intent(TwendingSettings.this,
					TwendingProvider.class);
			intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

			// Use an array and EXTRA_APPWIDGET_IDS instead of
			// AppWidgetManager.EXTRA_APPWIDGET_ID,
			// since it seems the onUpdate() is only fired on that:
			int[] ids = { mAppWidgetId };
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
			sendBroadcast(intent);

			setResult(RESULT_OK, resultValue);

			editor.putBoolean("configured", true);
			editor.commit();

			finish();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);

		setContentView(R.layout.main);

		settings = getSharedPreferences("TWENDING", 0);
		editor = settings.edit();

		editor.putBoolean("configured", false);
		editor.commit();

		freqField = (EditText) findViewById(R.id.frequencyField);
		freqField.setText("" + settings.getInt("frequency", 60));

		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new SubmitListener());

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
	}
}
