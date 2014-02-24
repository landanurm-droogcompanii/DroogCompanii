package ru.droogcompanii.application.data.personal_details;

import java.util.List;

import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;

/**
 * Created by ls on 21.02.14.
 */
public interface PersonalDetails {
    String getFirstName();
    String getLastName();
    List<BankCard> getCards();
}
