package ru.droogcompanii.application.data.offers;

import java.io.Serializable;

import ru.droogcompanii.application.util.ConvertorToString;
import ru.droogcompanii.application.util.Objects;

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
		return (Objects.equals(getId(), other.getId()) &&
				Objects.equals(getPartnerId(), other.getPartnerId()) &&
                Objects.equals(getDuration(), other.getDuration()) &&
                Objects.equals(getShortDescription(), other.getShortDescription()) &&
                Objects.equals(getFullDescription(), other.getFullDescription()) &&
                Objects.equals(getImageUrl(), other.getImageUrl()) &&
                Objects.equals(isSpecial(), other.isSpecial()));
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(
                id, partnerId, duration, shortDescription, fullDescription, imageUrl, isSpecial()
        );
	}

    @Override
    public String toString() {
        return ConvertorToString.buildFor(this)
            .withFieldNames(
                    "id", "partnerId", "duration", "shortDescription", "fullDescription", "imageUrl", "isSpecial()"
            )
            .withFieldValues(
                    id, partnerId, duration, shortDescription, fullDescription, imageUrl, isSpecial()
            )
            .toString();
    }

}
