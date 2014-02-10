package ru.droogcompanii.application.data.offers;

import java.io.Serializable;

import ru.droogcompanii.application.util.ConvertorToString;
import ru.droogcompanii.application.util.HashCodeCalculator;

public class OfferImpl implements Offer, Serializable {

	public int id;
	public int partnerId;
	public CalendarRange duration;
	public String shortDescription;
	public String fullDescription;
	public String imageUrl;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getPartnerId() {
		return partnerId;
	}

	@Override
	public CalendarRange getDuration() {
		return duration;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String getFullDescription() {
		return fullDescription;
	}

	@Override
	public boolean isSpecial() {
		return duration == null;
	}

	@Override
	public String getImageUrl() {
		return imageUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof Offer)) {
			return false;
		}
		Offer other = (Offer) obj;
		return ((getId() == other.getId()) &&
				(getPartnerId() == other.getPartnerId()) &&
				areEqual(getDuration(), other.getDuration()) &&
				areEqual(getShortDescription(), other.getShortDescription()) &&
				areEqual(getFullDescription(), other.getFullDescription()) &&
				areEqual(getImageUrl(), other.getImageUrl()) &&
				(isSpecial() == other.isSpecial()));
	}

	private static boolean areEqual(Object obj1, Object obj2) {
		return (obj1 == null)
				? (obj2 == null)
				: obj1.equals(obj2);
	}
	
	
	@Override
	public int hashCode() {
		return HashCodeCalculator.hashCodeFromFields(
                id, partnerId, duration, shortDescription, fullDescription, imageUrl, isSpecial()
        );
	}

    @Override
    public String toString() {
        /*
        return getClass().getSimpleName() + "( " +
                "id=" + id + ", " +
                "partner_id=" + partnerId + ", " +
                "duration=" + duration + ", " +
                "shortDescription=" + quote(shortDescription) + ", " +
                "fullDescription=" + quote(fullDescription) + ", " +
                "imageUrl=" + quote(imageUrl) + ", " +
                "isSpecial=" + isSpecial() +
        " )";
        */
        return ConvertorToString.buildFor(this)
            .withFieldNames("id", "partnerId", "duration", "shortDescription", "fullDescription", "imageUrl", "isSpecial()")
            .withFieldValues(id, partnerId, duration, shortDescription, fullDescription, imageUrl, isSpecial())
            .toString();
    }

    private static String quote(String str) {
        return "\"" + str + "\"";
    }

}
