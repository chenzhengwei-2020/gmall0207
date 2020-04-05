package com.czw.gmall.manage.contorller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.czw.gmall.beans.PmsSkuInfo;
import com.czw.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class SkuContorller {

    @Reference
    private SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        // 将spuId封装给productId
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        //处理默认图片 如果页面没选默认图片 那么第一张为默认图片

        if(StringUtils.isBlank(pmsSkuInfo.getSkuDefaultImg())){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        String success = skuService.saveSkuInfo(pmsSkuInfo);

        return success;
    }
}
