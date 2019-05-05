package com.shimafumi.ktm_train_scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class LineNotificationBuilder {

	private final String LINE_SEPARATER = System.getProperty("line.separator");

	public List<StringBuilder> buildMessage(List<Schedule> schedules) {
		Map<BreakKey1, Map<BreakKey2, List<Schedule>>> groupedSchedules = group(schedules);
		StringBuilder longMessage = buildLogMessage(groupedSchedules);
		List<StringBuilder> messages = split(longMessage);
		return paginate(messages);
	}

	private Map<BreakKey1, Map<BreakKey2, List<Schedule>>> group(List<Schedule> schedules) {
		Map<BreakKey1, Map<BreakKey2, List<Schedule>>> map = new HashMap<BreakKey1, Map<BreakKey2, List<Schedule>>>();
		schedules.forEach(s -> {
			BreakKey1 key1 = new BreakKey1(s.origin, s.destination);
			BreakKey2 key2 = new BreakKey2(s.departDate);
			if (map.containsKey(key1)) {
				Map<BreakKey2, List<Schedule>> innerMap = map.get(key1);
				if (innerMap.containsKey(key2)) {
					innerMap.get(key2).add(s);
				} else {
					List<Schedule> list = new ArrayList<Schedule>();
					list.add(s);
					innerMap.put(key2, list);

				}
			} else {
				Map<BreakKey2, List<Schedule>> innerMap = new HashMap<BreakKey2, List<Schedule>>();
				List<Schedule> list = new ArrayList<Schedule>();
				list.add(s);
				innerMap.put(key2, list);
				map.put(key1, innerMap);
			}
		});
		return map;
	}

	private StringBuilder buildLogMessage(Map<BreakKey1, Map<BreakKey2, List<Schedule>>> map) {
		StringBuilder bs = new StringBuilder();
		map.entrySet().forEach(entry -> {
			bs.append(String.format("%s > %s", entry.getKey().getOrigin(), entry.getKey().getDestination()));
			bs.append(LINE_SEPARATER);
			entry.getValue().entrySet().stream().sorted().forEach(innerMap -> {
				bs.append(String.format("%s", innerMap.getKey().getDepartTime().toString("yyyy/MM/dd EEE")));
				bs.append(LINE_SEPARATER);
				innerMap.getValue().forEach(s -> {
					bs.append(String.format("%s %s", s.getDepartTime(), s.getVacancy()));
					bs.append(LINE_SEPARATER);
				});
				bs.append(LINE_SEPARATER);
			});
		});
		return bs;
	}

	private List<StringBuilder> split(StringBuilder bs) {
		List<String> lines = Arrays.asList(bs.toString().split(LINE_SEPARATER));
		List<StringBuilder> messages = new ArrayList<StringBuilder>();
		lines.forEach(l -> {
			if (messages.isEmpty() || messages.get(messages.size() - 1).length() + l.length() > 990) {
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

	private class BreakKey1 {
		private final String origin;
		private final String destination;

		private BreakKey1(String origin, String destination) {
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
			BreakKey1 other = (BreakKey1) obj;
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

	private class BreakKey2 implements Comparable<BreakKey2>{
		private final DateTime departTime;

		public BreakKey2(DateTime departTime) {
			super();
			this.departTime = departTime;
		}

		public DateTime getDepartTime() {
			return departTime;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((departTime == null) ? 0 : departTime.hashCode());
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
			BreakKey2 other = (BreakKey2) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (departTime == null) {
				if (other.departTime != null)
					return false;
			} else if (!departTime.equals(other.departTime))
				return false;
			return true;
		}

		private LineNotificationBuilder getOuterType() {
			return LineNotificationBuilder.this;
		}

		@Override
		public int compareTo(BreakKey2 o) {
			return this.getDepartTime().compareTo(o.getDepartTime());
		}

	}
}
