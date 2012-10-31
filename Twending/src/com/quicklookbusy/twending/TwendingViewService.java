/*
 * TwendingViewService.java
 * 
 * Defines the class which returns the instance of TwendingViewsFactory.java
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
