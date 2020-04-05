package com.czw.gmall.service;

import com.czw.gmall.beans.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    public PmsSkuInfo getById(String skuId);

    public List<PmsSkuInfo> getSkuSaleAttrHash(String productId);

    public PmsSkuInfo getBySkuIdFromRedis(String skuId);

    public List<PmsSkuInfo> getAllPmsSkuInfo();

    boolean checkPrice(String productSkuId, BigDecimal price);


}
