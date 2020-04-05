package com.czw.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.czw.gmall.beans.PmsBaseAttrInfo;
import com.czw.gmall.beans.PmsBaseAttrValue;
import com.czw.gmall.beans.PmsBaseSaleAttr;
import com.czw.gmall.manage.mapper.AttrInfoMapper;
import com.czw.gmall.manage.mapper.AttrValueMapper;
import com.czw.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.czw.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.czw.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;


import java.util.ArrayList;
import java.util.List;
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private AttrInfoMapper attrMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo attrInfo : pmsBaseAttrInfos) {
            //List<PmsBaseAttrValue> pmsBaseAttrValueList = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(attrInfo.getId());
            List<PmsBaseAttrValue> baseAttrValues = attrValueMapper.select(pmsBaseAttrValue);
            attrInfo.setAttrValueList(baseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        if(StringUtils.isBlank(id)){
            //id为空  则是保存属性

            //保存属性
            attrMapper.insertSelective(pmsBaseAttrInfo);

            //保存属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                //给属性值赋值属性id
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());

                //添加属性值
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }else {
            // id不为空 修改

            //修改属性
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            attrMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

            //属性值修改
            //根据属性id删除所有属性
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
            attrValueMapper.delete(pmsBaseAttrValueDel);

            //删除后添加新的属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }

        
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = attrValueMapper.select(pmsBaseAttrValue);

        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(String valueIdStr) {
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }

}
