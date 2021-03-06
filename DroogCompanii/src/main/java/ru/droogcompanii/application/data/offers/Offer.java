package ru.droogcompanii.application.data.offers;


public interface Offer {
	int getId();
	int getPartnerId();
	boolean isSpecial();
    boolean isActual();
	CalendarRange getDuration();
	String getShortDescription();
	String getFullDescription();
	String getImageUrl();
}
