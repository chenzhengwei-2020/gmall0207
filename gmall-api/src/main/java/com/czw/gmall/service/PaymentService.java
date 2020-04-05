package com.czw.gmall.service;

import com.czw.gmall.beans.PaymentInfo;

public interface PaymentService {
    void save(PaymentInfo paymentInfo);

    void updatePaymentInfo(PaymentInfo paymentInfo);
}
