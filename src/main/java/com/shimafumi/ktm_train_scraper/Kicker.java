package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Kicker {

	public static void main(String[] args) {
		List<DateTime> targetDates = new ArrayList<DateTime>();
		for (int i = 0; i < args.length; i++) {
			targetDates.add(new DateTime(args[i]));
		}
		new Runner().run(targetDates);
	}
}