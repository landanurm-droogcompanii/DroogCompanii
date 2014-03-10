package ru.droogcompanii.application.data.offers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.util.ListUtils;

/**
 * Created by ls on 06.03.14.
 */
public class Offers implements Serializable {

    private List<Offer> specialOffers;
    private List<Offer> actualOffers;
    private List<Offer> pastOffers;

    public Offers() {
        specialOffers = noOffers();
        actualOffers = noOffers();
        pastOffers = noOffers();
    }

    public Offers(List<Offer> offers) {
        this();
        addAll(offers);
    }

    private static List<Offer> noOffers() {
        return new ArrayList<Offer>();
    }

    public void add(Offer offer) {
        if (offer.isSpecial()) {
            specialOffers.add(offer);
        } else if (offer.isActual()) {
            actualOffers.add(offer);
        } else {
            pastOffers.add(offer);
        }
    }

    public void addAll(Iterable<Offer> offers) {
        for (Offer each : offers) {
            add(each);
        }
    }

    public List<Offer> getSpecialOffers() {
        return ListUtils.copyOf(specialOffers);
    }

    public List<Offer> getActualOffers() {
        return ListUtils.copyOf(actualOffers);
    }

    public List<Offer> getPastOffers() {
        return ListUtils.copyOf(pastOffers);
    }

    public List<Offer> getAllOffers() {
        return ListUtils.combineLists(specialOffers, actualOffers, pastOffers);
    }

    public boolean isEmpty() {
        return specialOffers.isEmpty() && actualOffers.isEmpty() && pastOffers.isEmpty();
    }
}
