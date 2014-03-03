package ru.droogcompanii.application.data.personal_details;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ls on 21.02.14.
 */
public class AccountOwnerImpl implements AccountOwner, Serializable {

    private String login;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String extraPhone;
    private String email;
    private List<BankCard> cards;

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getLogin() {
        return login;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setExtraPhone(String extraPhone) {
        this.extraPhone = extraPhone;
    }

    @Override
    public String getExtraPhone() {
        return extraPhone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setCards(List<BankCard> cards) {
        this.cards = cards;
    }

    @Override
    public List<BankCard> getBankCards() {
        return cards;
    }
}
