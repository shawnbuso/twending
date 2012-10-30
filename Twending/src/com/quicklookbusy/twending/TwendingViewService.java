/*
 * TwendingViewService.java
 * 
 * Defines the class which returns the instance of TwendingViewsFactory.java
 * 
 * Copyright (C) Shawn Busolits, 2012 All Rights Reserved
 */

package com.quicklookbusy.twending;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Returns the ViewsFactory used to create the widget
 * 
 * @author Shawn Busolits
 * @version 1.0
 */
public class TwendingViewService extends RemoteViewsService {
	@Override
	/**
	 * Returns an instance of the TwendingViewsFactory
	 * @return An instance of the TwendingViewsFactory
	 */
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		TwendingService.log("In remoteViewsService");
		return (new TwendingViewsFactory(this.getApplicationContext(), intent));
	}
}
