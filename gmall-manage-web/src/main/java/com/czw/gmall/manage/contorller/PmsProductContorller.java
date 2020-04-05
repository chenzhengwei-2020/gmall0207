package com.czw.gmall.manage.contorller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.czw.gmall.beans.PmsProductImage;
import com.czw.gmall.beans.PmsProductInfo;
import com.czw.gmall.beans.PmsProductSaleAttr;
import com.czw.gmall.manage.util.FdfsUtil;
import com.czw.gmall.service.PmsProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class PmsProductContorller {

    @Reference
    private PmsProductService pmsProductService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> spuList = pmsProductService.getSpuList(catalog3Id);
        return spuList;
    }

    //图片上传
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile){
        // 将图片或者音视频上传到分布式的文件存储系统
        // 将图片的存储路径返回给页面
        String imgUrl = FdfsUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        pmsProductService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> pmsProductImages = pmsProductService.spuImageList(spuId);
        return pmsProductImages;
    }
}
