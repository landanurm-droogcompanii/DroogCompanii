package ru.droogcompanii.application.data.offers;

import java.io.Serializable;
import java.util.Calendar;

import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.ConvertorToString;

public class CalendarRange implements Serializable {

	private final Calendar from;
	private final Calendar to;

	public CalendarRange(Calendar from, Calendar to) {
        checkArguments(from, to);
		this.from = from;
		this.to = to;
	}

    private void checkArguments(Calendar from, Calendar to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("<from> and <to> must be not null");
        }
        if (!to.after(from)) {
            throw new IllegalArgumentException("<to> must be after <from>");
        }
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

    @Override
    public String toString() {
        return ConvertorToString.buildFor(this)
                .withFieldNames("from", "to")
                .withFieldValues(CalendarUtils.convertToString(from), CalendarUtils.convertToString(to))
                .toString();
    }
	
}
