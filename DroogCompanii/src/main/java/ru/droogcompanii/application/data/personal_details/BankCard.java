package ru.droogcompanii.application.data.personal_details;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public class BankCard implements Card {

    public String title;
    public CardOwner owner;

    private List<CardTransaction> transactions;

    public BankCard() {
        title = "Default title";
        transactions = new ArrayList<CardTransaction>();
        owner = new CardOwnerImpl();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public CardOwner getOwner() {
        return owner;
    }

    @Override
    public List<CardTransaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(CardTransaction transaction) {
        transactions.add(transaction);
    }
}
