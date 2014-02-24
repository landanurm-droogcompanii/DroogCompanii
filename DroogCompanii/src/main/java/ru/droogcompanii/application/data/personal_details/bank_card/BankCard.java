package ru.droogcompanii.application.data.personal_details.bank_card;

import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public interface BankCard {
    CardOwner getOwner();
    List<CardTransaction> getTransactions();
    String getTitle();
}
