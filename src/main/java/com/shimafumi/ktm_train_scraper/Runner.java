package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class Runner {

	private final String token = System.getProperty("LINE.notify.token");

	public void run(List<DateTime> targetDates) {
		List<Schedule> availables = new ArrayList<Schedule>();
		availables.addAll(new KtmTrainScraper().scrape("Singapore", "JB Sentral", targetDates));
		availables.addAll(new KtmTrainScraper().scrape("JB Sentral", "Singapore", targetDates));

		Map<Key, List<Schedule>> map = new HashMap<Key, List<Schedule>>();
		availables.forEach(s -> {
			Key key = new Key(s.origin, s.destination);
			if (map.containsKey(key)) {
				map.get(key).add(s);
			} else {
				List<Schedule> list = new ArrayList<Schedule>();
				list.add(s);
				map.put(key, list);
			}
		});

		StringBuilder bs = new StringBuilder();
		map.entrySet().forEach(key -> {
			bs.append(System.getProperty("line.separator"));
			bs.append(String.format("%s > %s", key.getKey().getOrigin(), key.getKey().getDestination()));
			key.getValue().forEach(s -> {
				bs.append(System.getProperty("line.separator"));
				bs.append(String.format("%s %s %s", s.getDepartDate().toString("yyyy-MM-dd EEE"), s.getDepartTime(),
						s.getVacancy()));
				if(bs.length()>900) {
					new LineNotificationSender().send(token, bs.toString());
					bs.delete(0, bs.length());
				}
			});
		});

	}

	private class Key {
		private final String origin;
		private final String destination;

		private Key(String origin, String destination) {
			super();
			this.origin = origin;
			this.destination = destination;
		}

		public String getOrigin() {
			return origin;
		}

		public String getDestination() {
			return destination;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((destination == null) ? 0 : destination.hashCode());
			result = prime * result + ((origin == null) ? 0 : origin.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (destination == null) {
				if (other.destination != null)
					return false;
			} else if (!destination.equals(other.destination))
				return false;
			if (origin == null) {
				if (other.origin != null)
					return false;
			} else if (!origin.equals(other.origin))
				return false;
			return true;
		}

		private Runner getOuterType() {
			return Runner.this;
		}
	}
}
