/*
 * TwendsRequest.java
 * 
 * Defines the class used to query Twitter for a list of trending topics
 * 
 * Copyright (C) Shawn Busolits, 2012 All Rights Reserved
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
			topics.add(dateFormat.format(cal.getTime()));

		} catch (TwitterException e) {
			e.printStackTrace();
		}

		callback.doOnResult(topics);
	}
}