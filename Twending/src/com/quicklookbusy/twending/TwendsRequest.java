package com.quicklookbusy.twending;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.quicklookbusy.twending.TwendingService.TwendsCallback;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwendsRequest extends Thread {

	TwendsCallback callback;
	
	public TwendsRequest(TwendsCallback callback) {
		this.callback = callback;
	}
	
	public void run() {
		//Get list of trending topics from twitter
		ArrayList<String> topics = new ArrayList<String>();
		Twitter twitter = new TwitterFactory().getInstance();
		//ResponseList<Trends> dailyTrends;
		Trends usTrends;
		try {
			/*dailyTrends = twitter.getLocationTrends(23424977);
			//Figure out most recent
			Calendar calendar = Calendar.getInstance();
			calendar.set(0,  0, 0);
			Date maxDate = calendar.getTime();
			Trends mostRecent = null;
			for(Trends trends: dailyTrends) {
				Date asOf = trends.getAsOf();
				if(asOf.after(maxDate)) {
					maxDate = asOf;
					mostRecent = trends;
				}
			}*/
			
			usTrends = twitter.getLocationTrends(23424977);
			
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();			
			topics.add(dateFormat.format(cal.getTime()));
			
			for(int i=0; i<usTrends.getTrends().length; i++) {
				topics.add(usTrends.getTrends()[i].getName());
			}
			
			/*for(int i=0; i<mostRecent.getTrends().length; i++) {
				topics.add(mostRecent.getTrends()[i].getName());
			}*/
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		try {
			callback.doOnResult(topics);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}