package ru.droogcompanii.application.util;

import ru.droogcompanii.application.util.constants.StringConstants;

/**
 * Created by ls on 25.03.14.
 */
public class CashlessPaymentsUtils {
    public static boolean isSupportCashlessPayments(String paymentMethods) {
        String paymentMethodsInLowerCase = paymentMethods.toLowerCase();
        for (String each : StringConstants.PaymentMethods.CASHLESS_METHODS) {
            if (paymentMethodsInLowerCase.contains(each.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
