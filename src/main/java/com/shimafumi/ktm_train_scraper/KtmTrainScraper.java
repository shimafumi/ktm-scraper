package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

public class KtmTrainScraper {

	private static final String SEARCH_RESULT = ".search-result-div > div > div:first-child";
	private static final String DIV_DEPART_TIME = "div > .depart-time";
	private static final String DIV_ORIGIN = "div:first-child > .route-subplace";
	private static final String DIV_DESTINATION = "div:last-child > .route-subplace";
	private static final String LABEL_VACANCY = ".col-sm-1 > .vacancy";
	private static final String DIV_PRELOADER = ".trip-list > .preloader";
	private static final String URL = "http://www.ktmtrain.com/train/booking/";
	private static final String RADIO_ONE_WAY = ".radio-inline > input:first-child";
	private static final String TEXT_ORIGIN = "#txtSearchOrigin_Train";
	private static final String TEXT_DESTINATON = "#txtSearchDestination_Train";
	private static final String TEXT_DEPARTURE_DATE = "#dpDepartureDate_Train";
	private static final String BUTTON_SEARCH = ".search-btn > button";

	public List<Schedule> scrape(List<DateTime> targetDates) {
		List<Schedule> availableSchedules = new ArrayList<Schedule>();
		for (int i = 0; i < 31; i++) {
			DateTime date = DateTime.now().withTime(0, 0, 0, 0).plusDays(i);
			if (!targetDates.contains(date)) {
				continue;
			}
			Selenide.open(URL);
			Selenide.$(RADIO_ONE_WAY).click();
			Selenide.$(TEXT_ORIGIN).setValue("Singapore");
			Selenide.sleep(1000);
			Selenide.$(TEXT_DESTINATON).setValue("JB Sentral");
			Selenide.sleep(1000);
			Selenide.$(TEXT_DEPARTURE_DATE).setValue(DateTimeFormat.forPattern("yyyy-MM-dd").print(date));
			Selenide.$(BUTTON_SEARCH).click();
			Selenide.$(DIV_PRELOADER).waitUntil(Condition.disappear, 10000);
			Selenide.$$(SEARCH_RESULT).forEach(row -> {
				Schedule s = new Schedule();
				s.setDepartDate(date);
				s.setDepartTime(row.$(DIV_DEPART_TIME).text());
				s.setOrigin(row.$(DIV_ORIGIN).getText());
				s.setDestination(row.$(DIV_DESTINATION).getText());
				s.setVacancy(row.$(LABEL_VACANCY).text());
				availableSchedules.add(s);
			});
		}
		return availableSchedules;
	}
}
