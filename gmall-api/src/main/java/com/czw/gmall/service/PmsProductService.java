package com.czw.gmall.service;


import com.czw.gmall.beans.PmsProductImage;
import com.czw.gmall.beans.PmsProductInfo;
import com.czw.gmall.beans.PmsProductSaleAttr;

import java.util.List;

public interface PmsProductService {

    public List<PmsProductInfo> getSpuList(String catalog3Id);

    public void saveSpuInfo(PmsProductInfo pmsProductInfo);

    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    public List<PmsProductImage> spuImageList(String spuId);

    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
