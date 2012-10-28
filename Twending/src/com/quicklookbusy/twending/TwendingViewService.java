package com.quicklookbusy.twending;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TwendingViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		TwendingService.log("In remoteViewsService");
		return (new TwendingViewsFactory(this.getApplicationContext(), intent));
	}
}
