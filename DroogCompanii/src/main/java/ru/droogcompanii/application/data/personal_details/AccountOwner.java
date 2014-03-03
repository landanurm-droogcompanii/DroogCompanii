package ru.droogcompanii.application.data.personal_details;

import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public interface AccountOwner {
    String getLogin();
    String getFirstName();
    String getLastName();
    String getMiddleName();
    String getPhone();
    String getExtraPhone();
    String getEmail();
    List<BankCard> getBankCards();
}
