package ru.droogcompanii.application.data.personal_details;

import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public interface Card {
    CardOwner getOwner();
    List<CardTransaction> getTransactions();
    String getTitle();
}
