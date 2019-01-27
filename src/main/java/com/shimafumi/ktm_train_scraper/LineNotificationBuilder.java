package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineNotificationBuilder {

	private final String LINE_SEPARATER = System.getProperty("line.separator");

	public List<StringBuilder> buildMessage(List<Schedule> schedules) {
		Map<Key, List<Schedule>> groupedSchedules = group(schedules);
		StringBuilder longMessage = buildLogMessage(groupedSchedules);
		List<StringBuilder> messages = split(longMessage);
		return paginate(messages);
	}

	private Map<Key, List<Schedule>> group(List<Schedule> schedules) {
		Map<Key, List<Schedule>> map = new HashMap<Key, List<Schedule>>();
		schedules.forEach(s -> {
			Key key = new Key(s.origin, s.destination);
			if (map.containsKey(key)) {
				map.get(key).add(s);
			} else {
				List<Schedule> list = new ArrayList<Schedule>();
				list.add(s);
				map.put(key, list);
			}
		});
		return map;
	}

	private StringBuilder buildLogMessage(Map<Key, List<Schedule>> map) {
		StringBuilder bs = new StringBuilder();
		map.entrySet().forEach(key -> {
			bs.append(String.format("%s > %s", key.getKey().getOrigin(), key.getKey().getDestination()));
			bs.append(LINE_SEPARATER);
			key.getValue().forEach(s -> {
				bs.append(String.format("%s %s %s", s.getDepartDate().toString("yyyy/MM/dd EEE"), s.getDepartTime(),
						s.getVacancy()));
				bs.append(LINE_SEPARATER);
			});
			bs.append(LINE_SEPARATER);
		});
		return bs;
	}

	private List<StringBuilder> split(StringBuilder bs) {
		List<String> lines = Arrays.asList(bs.toString().split(LINE_SEPARATER));
		List<StringBuilder> messages = new ArrayList<StringBuilder>();
		lines.forEach(l -> {
			if (messages.isEmpty() || messages.get(messages.size() - 1).length() > 900) {
				StringBuilder m = new StringBuilder();
				messages.add(m);
				m.append(LINE_SEPARATER);
			}
			messages.get(messages.size() - 1).append(l).append(LINE_SEPARATER);
		});
		return messages;
	}

	private List<StringBuilder> paginate(List<StringBuilder> messages) {
		messages.forEach(m -> {
			if (messages.size() > 1) {
				m.append((messages.indexOf(m) + 1) + "/" + messages.size());
			}
		});
		return messages;
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

		private LineNotificationBuilder getOuterType() {
			return LineNotificationBuilder.this;
		}
	}
}
