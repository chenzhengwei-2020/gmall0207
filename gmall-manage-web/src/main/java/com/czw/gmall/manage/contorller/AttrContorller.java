package com.czw.gmall.manage.contorller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.czw.gmall.beans.PmsBaseAttrInfo;
import com.czw.gmall.beans.PmsBaseAttrValue;
import com.czw.gmall.beans.PmsBaseSaleAttr;
import com.czw.gmall.beans.PmsProductSaleAttr;
import com.czw.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin //跨域注解
public class AttrContorller {

    @Reference //远程调用
    private AttrService attrService;

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = attrService.getAttrInfoList(catalog3Id);
        return pmsBaseAttrInfoList;
    }

    //添加平台属性
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    //根据属性id查询属性值 用于回显
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);

        return pmsBaseAttrValues;
    }

    //查询销售属性名称
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List<PmsBaseSaleAttr> pmsBaseSaleAttrList = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrList;
    }
}
