package ru.droogcompanii.application.ui.screens.personal_account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.data.personal_details.AccountOwnerImpl;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.data.personal_details.BankCardImpl;
import ru.droogcompanii.application.data.personal_details.CardTransaction;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 03.03.14.
 */
public class DummyAccountOwnerMaker {

    public static AccountOwner make() {
        AccountOwnerImpl personalDetails = new AccountOwnerImpl();
        personalDetails.setFirstName("Ivan");
        personalDetails.setLastName("Ivanov");
        personalDetails.setMiddleName("Ivanovitch");
        personalDetails.setCards(createDummyCards());
        return personalDetails;
    }

    private static List<BankCard> createDummyCards() {
        List<BankCard> cards = new ArrayList<BankCard>();
        for (int i = 1; i < 10; ++i) {
            cards.add(createDummyCardByIndex(i));
        }
        return cards;
    }

    private static BankCard createDummyCardByIndex(int index) {
        BankCardImpl bankCard = new BankCardImpl();
        bankCard.setTitle("Bank Card " + index);
        bankCard.setDateOfAttachment(createDummyCalendarByIndex(index));
        bankCard.setTransactions(createDummyTransactions());
        return bankCard;
    }

    private static Calendar createDummyCalendarByIndex(int index) {
        return CalendarUtils.createByDayMonthYear(index + 1, Calendar.JANUARY, 2012);
    }

    private static List<CardTransaction> createDummyTransactions() {
        return new ArrayList<CardTransaction>();
    }
}
