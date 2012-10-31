/*
 * TwendsRequest.java
 * 
 * Defines the class used to query Twitter for a list of trending topics
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.quicklookbusy.twending.TwendingService.TwendsCallback;

/**
 * Requests a list of trending topics for the US from Twitter
 * 
 * @author Shawn Busolits
 * @version 1.0
 */
public class TwendsRequest extends Thread {

	/** Callback to cal when the request returns */
	TwendsCallback callback;

	/**
	 * Instantiates the request with the provided callback
	 * 
	 * @param callback
	 *            Callback to call when the request returns
	 */
	public TwendsRequest(TwendsCallback callback) {
		this.callback = callback;
	}

	/**
	 * Makes the request and calls the callback with the acquired data
	 */
	public void run() {
		ArrayList<String> topics = new ArrayList<String>();
		Twitter twitter = new TwitterFactory().getInstance();
		Trends usTrends;
		try {
			usTrends = twitter.getLocationTrends(23424977);

			for (int i = 0; i < usTrends.getTrends().length; i++) {
				topics.add(usTrends.getTrends()[i].getName());
			}

			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			topics.add("Last updated at " + dateFormat.format(cal.getTime()));

		} catch (TwitterException e) {
			e.printStackTrace();
		}

		callback.doOnResult(topics);
	}
}