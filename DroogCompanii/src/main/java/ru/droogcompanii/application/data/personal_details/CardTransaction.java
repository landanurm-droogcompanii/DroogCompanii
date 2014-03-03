package ru.droogcompanii.application.data.personal_details;

import java.util.Calendar;

/**
 * Created by ls on 21.02.14.
 */
public interface CardTransaction {
    String getId();
    Calendar getDateTime();
    String getPartnerName();
    String getPartnerPointName();
    String getPartnerPointAddress();
    String getCashDeskId();
    String getOperationType();
    String getPaymentMethod();
    double getCost();
    double getDiscount();
}
