package ru.droogcompanii.application.data.personal_details;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public class BankCardImpl implements BankCard, Serializable {

    private Calendar dateOfAttachment;
    private String title;
    private List<CardTransaction> transactions;

    public void setDateOfAttachment(Calendar dateOfAttachment) {
        this.dateOfAttachment = dateOfAttachment;
    }

    @Override
    public Calendar getDateOfAttachment() {
        return dateOfAttachment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTransactions(List<CardTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public List<CardTransaction> getTransactions() {
        return transactions;
    }
}
