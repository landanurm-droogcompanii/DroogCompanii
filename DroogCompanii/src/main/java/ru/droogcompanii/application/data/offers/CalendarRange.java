package ru.droogcompanii.application.data.offers;

import java.util.Calendar;

public class CalendarRange {

	private final Calendar from;
	private final Calendar to;

	public CalendarRange(Calendar from, Calendar to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException("<from> and <to> must be not null");
		}
		if (!to.after(from)) {
			throw new IllegalArgumentException("<to> must be after <from>");
		}
		this.from = from;
		this.to = to;
	}

	public Calendar from() {
		return from;
	}

	public Calendar to() {
		return to;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CalendarRange other = (CalendarRange) obj;
		return from.equals(other.from) && to.equals(other.to);
	}

	@Override
	public int hashCode() {
		return from.hashCode() + to.hashCode();
	}
	
}
