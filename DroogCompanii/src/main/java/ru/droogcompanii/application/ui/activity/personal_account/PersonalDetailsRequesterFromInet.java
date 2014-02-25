package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.PersonalDetailsImpl;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCardImpl;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsRequesterFromInet implements PersonalDetailsRequester {

    private final Context context;

    public PersonalDetailsRequesterFromInet(Context context) {
        this.context = context;
    }

    @Override
    public Optional<PersonalDetails> requestDetails(AuthenticationToken token) {
        // TODO
        return Optional.of(createDummyPersonalDetails());
    }

    private PersonalDetails createDummyPersonalDetails() {
        PersonalDetailsImpl personalDetails = new PersonalDetailsImpl();
        personalDetails.firstName = "Dummy First Name from Inet";
        personalDetails.lastName = "Dummy Last Name from Inet";
        addDummyCardsTo(personalDetails);
        return personalDetails;
    }

    private void addDummyCardsTo(PersonalDetailsImpl personalDetails) {
        for (int i = 1; i < 10; ++i) {
            addDummyCardTo(personalDetails, i);
        }
    }

    private void addDummyCardTo(PersonalDetailsImpl personalDetails, int index) {
        BankCardImpl bankCard = new BankCardImpl();
        bankCard.title = String.valueOf(index);
        personalDetails.addCard(bankCard);
    }
}
