package ru.droogcompanii.application.ui.fragment.personal_details;

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

    private final AuthenticationToken token;
    private final Context context;

    public PersonalDetailsRequesterFromInet(Context context, AuthenticationToken token) {
        this.context = context;
        this.token = token;
    }

    @Override
    public Optional<PersonalDetails> requestDetails() {
        // TODO
        PersonalDetailsImpl personalDetails = new PersonalDetailsImpl();
        personalDetails.firstName = "Dummy First Name from Inet";
        personalDetails.lastName = "Dummy Last Name from Inet";
        addCardsTo(personalDetails);
        return Optional.of((PersonalDetails) personalDetails);
    }

    private void addCardsTo(PersonalDetailsImpl personalDetails) {
        for (int i = 1; i < 10; ++i) {
            addCardTo(personalDetails, i);
        }
    }

    private void addCardTo(PersonalDetailsImpl personalDetails, int index) {
        BankCardImpl bankCard = new BankCardImpl();
        bankCard.title = String.valueOf(index);
        personalDetails.addCard(bankCard);
    }
}
