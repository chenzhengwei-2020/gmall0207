package com.czw.gmall.service;

import com.czw.gmall.beans.OmsOrder;

public interface OrderService {
    String generateCode(String memberId);

    String checkedCode(String memberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder,String memberId);

    OmsOrder getTotalPrice(String outTradeNo);

    OmsOrder getOrder(String outTradeNo);

    void updateOrder(OmsOrder omsOrder);
}
