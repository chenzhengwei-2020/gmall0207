package com.czw.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.czw.gmall.beans.PmsProductImage;
import com.czw.gmall.beans.PmsProductInfo;
import com.czw.gmall.beans.PmsProductSaleAttr;
import com.czw.gmall.beans.PmsProductSaleAttrValue;
import com.czw.gmall.manage.mapper.*;
import com.czw.gmall.service.PmsProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> getSpuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        return pmsProductMapper.select(pmsProductInfo);
    }

    //保存spu
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        //保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        //保存商品生成的主键
        String productId = pmsProductInfo.getId();

        //保存商品图片信息
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        //保存销售属性信息
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            //保存销售属性值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }

    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            PmsProductSaleAttrValue productSaleAttrValue = new PmsProductSaleAttrValue();
            productSaleAttrValue.setProductId(spuId);
            productSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> productSaleAttrValues = pmsProductSaleAttrValueMapper.select(productSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(productSaleAttrValues);
        }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        return pmsProductImageMapper.select(pmsProductImage);
    }
    //商品销售属性列表
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
        List<PmsProductSaleAttr> productSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return productSaleAttrs;
    }
}
