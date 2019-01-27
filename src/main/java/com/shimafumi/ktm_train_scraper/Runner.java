package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Runner {

	private final String LINE_NOTIFY_TOKEN = System.getProperty("LINE.notify.token");

	public void run(List<DateTime> targetDates) {
		List<Schedule> availables = scrape(targetDates);
		List<StringBuilder> messages = new LineNotificationBuilder().buildMessage(availables);
		sendMessages(messages);
	}


	private List<Schedule> scrape(List<DateTime> targetDates) {
		List<Schedule> availables = new ArrayList<Schedule>();
		availables.addAll(new KtmTrainScraper().scrape("Singapore", "JB Sentral", targetDates));
		availables.addAll(new KtmTrainScraper().scrape("JB Sentral", "Singapore", targetDates));
		return availables;
	}
	
	private void sendMessages(List<StringBuilder> messages) {
		LineNotificationSender sender = new LineNotificationSender();
		messages.forEach(m -> {
			sender.send(LINE_NOTIFY_TOKEN, m.toString());
		});
	}
}
