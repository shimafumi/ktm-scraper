package com.shimafumi.ktm_train_scraper;

import org.joda.time.DateTime;

public class Schedule {

	DateTime departDate;
	String departTime;
	String origin;
	String destination;
	String vacancy;

	public DateTime getDepartDate() {
		return departDate;
	}

	public void setDepartDate(DateTime departDate) {
		this.departDate = departDate;
	}

	public String getDepartTime() {
		return departTime;
	}

	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getVacancy() {
		return vacancy;
	}

	public void setVacancy(String vacancy) {
		this.vacancy = vacancy;
	}

	@Override
	public String toString() {
		return "Schedule [departDate=" + departDate + ", departTime=" + departTime + ", origin=" + origin
				+ ", destination=" + destination + ", vacancy=" + vacancy + "]";
	}
}
