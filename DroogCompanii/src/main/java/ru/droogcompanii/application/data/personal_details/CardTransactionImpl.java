package ru.droogcompanii.application.data.personal_details;

import java.util.Calendar;

/**
 * Created by ls on 03.03.14.
 */
public class CardTransactionImpl implements CardTransaction {

    private Calendar dateTime;
    private double cost;
    private double discount;
    private String id;
    private String partnerName;
    private String partnerPointName;
    private String partnerPointAddress;
    private String cashDeskId;
    private String operationType;
    private String paymentMethod;


    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Calendar getDateTime() {
        return dateTime;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Override
    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerPointName(String partnerPointName) {
        this.partnerPointName = partnerPointName;
    }

    @Override
    public String getPartnerPointName() {
        return partnerPointName;
    }

    public void setPartnerPointAddress(String partnerPointAddress) {
        this.partnerPointAddress = partnerPointAddress;
    }

    @Override
    public String getPartnerPointAddress() {
        return partnerPointAddress;
    }

    public void setCashDeskId(String cashDeskId) {
        this.cashDeskId = cashDeskId;
    }

    @Override
    public String getCashDeskId() {
        return cashDeskId;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public String getOperationType() {
        return operationType;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public double getDiscount() {
        return discount;
    }
}
