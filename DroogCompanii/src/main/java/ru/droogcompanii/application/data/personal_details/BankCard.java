package ru.droogcompanii.application.data.personal_details;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public interface BankCard {
    String getTitle();
    Calendar getDateOfAttachment();
    List<CardTransaction> getTransactions();
}
