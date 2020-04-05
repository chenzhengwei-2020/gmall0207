package com.czw.gmall.item.contorller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.czw.gmall.beans.PmsProductSaleAttr;
import com.czw.gmall.beans.PmsSkuInfo;
import com.czw.gmall.beans.PmsSkuSaleAttrValue;
import com.czw.gmall.service.PmsProductService;
import com.czw.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ItemContorller {

    @Reference
    private SkuService skuService;

    @Reference
    private PmsProductService pmsProductService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map){
        //sku对象
        PmsSkuInfo pmsSkuInfo = skuService.getBySkuIdFromRedis(skuId);

        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        //spu的sku和sku销售属性的列表组合成一个静态的hash
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrHash(pmsSkuInfo.getProductId());
        Map<String,String> skuSaleAttrHash = new HashMap<>();
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String v = skuInfo.getId();
            String k = "";
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);
        map.put("skuInfo",pmsSkuInfo);
        return "item";
    }

}
