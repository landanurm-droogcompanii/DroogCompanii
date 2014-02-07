package ru.droogcompanii.application.util;

/**
 * Created by ls on 15.01.14.
 */
public class StringConstants {

    public static class PaymentMethods {
        public static final String visa = "Visa";
        public static final String masterCard = "MasterCard";
    }

    public static final String[] cashlessPaymentMethods = {
            PaymentMethods.visa, PaymentMethods.masterCard
    };

    public static final String discountType_Bonus = "Бонус";
    public static final String discountType_Discount = "Скидка";
    public static final String discountType_CashBack = "Cash back";

    public static class PartnersXmlConstants {

        public static class Tags {
            public static final String partnerCategories = "partner-categories";
            public static final String partnerCategory = "partner-category";
            public static final String title = "title";
            public static final String partners = "partners";
            public static final String partner = "partner";
            public static final String id = "id";
            public static final String fullTitle = "full-title";
            public static final String discountType = "discount-type";
            public static final String discount = "discount";
            public static final String partnerPoints = "partner-points";
            public static final String partnerPoint = "partner-point";
            public static final String address = "address";
            public static final String longitude = "longitude";
            public static final String latitude = "latitude";
            public static final String phones = "phones";
            public static final String phone = "phone";
            public static final String paymentMethods = "payment-methods";
            public static final String workinghours = "workinghours";
        }

        public static class DayOfWeek {
            public static final String monday = "mon";
            public static final String tuesday = "tue";
            public static final String wednesday = "wed";
            public static final String thursday = "thu";
            public static final String friday = "fri";
            public static final String saturday = "sat";
            public static final String sunday = "sun";
        }

        public static class Attributes {
            public static final String typeOfDay = "type";
        }

        public static class TypesOfDay {
            public static final String usualDay = "usual_day";
            public static final String holiday = "holiday";
            public static final String dayAndNight = "day_and_night";
        }
    }

    public static class OffersXmlConstants {

        public static class Tags {
            public static final String offers = "offers";
            public static final String offer = "offer";
            public static final String id = "id";
            public static final String partnerId = "partnerId";
            public static final String imageUrl = "imageUrl";
            public static final String shortDescription = "shortDescription";
            public static final String fullDescription = "fullDescription";
            public static final String duration = "duration";
            public static final String from = "from";
            public static final String to = "to";
        }
    }
}
