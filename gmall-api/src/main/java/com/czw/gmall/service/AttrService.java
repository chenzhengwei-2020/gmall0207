package com.czw.gmall.service;

import com.czw.gmall.beans.PmsBaseAttrInfo;
import com.czw.gmall.beans.PmsBaseAttrValue;
import com.czw.gmall.beans.PmsBaseSaleAttr;
import com.czw.gmall.beans.PmsProductSaleAttr;

import java.util.List;

public interface AttrService {

    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    public List<PmsBaseAttrValue> getAttrValueList(String attrId);

    public List<PmsBaseSaleAttr> baseSaleAttrList();

    public List<PmsBaseAttrInfo> getAttrValueListByValueId(String valueIdStr);
}
