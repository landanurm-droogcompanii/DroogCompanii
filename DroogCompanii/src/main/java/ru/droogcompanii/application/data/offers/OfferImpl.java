package ru.droogcompanii.application.data.offers;

import java.io.Serializable;

import ru.droogcompanii.application.util.CalendarUtils;
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
    public boolean isActual() {
        return !CalendarUtils.now()
                .after(duration.to());
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
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .add("partnerId", partnerId)
                .add("duration", duration)
                .add("shortDescription", shortDescription)
                .add("fullDescription", fullDescription)
                .add("imageUrl", imageUrl)
                .add("isSpecial()", isSpecial())
                .toString();
    }

}
