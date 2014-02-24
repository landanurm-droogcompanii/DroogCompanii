package ru.droogcompanii.application.data.personal_details;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsImpl implements PersonalDetails, Serializable {

    public String firstName;
    public String lastName;

    private final List<BankCard> cards;

    public PersonalDetailsImpl() {
        cards = new ArrayList<BankCard>();
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
    public List<BankCard> getCards() {
        return cards;
    }

    public void addCard(BankCard card) {
        cards.add(card);
    }
}
