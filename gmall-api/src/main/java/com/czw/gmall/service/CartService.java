package com.czw.gmall.service;

import com.czw.gmall.beans.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(String memberId);

    List<OmsCartItem> getOmsCartItem(String memberId);

    void checkCart(String isChecked, String skuId, String memberId);

    void checkQuantity(String skuId, String memberId,String quantity);

    void delCart(String productSkuId,String memberId);
}
