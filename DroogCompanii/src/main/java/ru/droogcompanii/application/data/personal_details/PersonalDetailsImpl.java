package ru.droogcompanii.application.data.personal_details;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsImpl implements PersonalDetails, Serializable {

    public String firstName;
    public String lastName;

    private final List<Card> cards;

    public PersonalDetailsImpl() {
        cards = new ArrayList<Card>();
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
